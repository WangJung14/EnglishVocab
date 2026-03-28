package trung.supper.englishgrammar.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trung.supper.englishgrammar.dto.request.AnswerRequest;
import trung.supper.englishgrammar.dto.request.SubmitAttemptRequest;
import trung.supper.englishgrammar.dto.response.*;
import trung.supper.englishgrammar.enums.AttemptStatus;
import trung.supper.englishgrammar.enums.ErrorCode;
import trung.supper.englishgrammar.exception.AppException;
import trung.supper.englishgrammar.models.*;
import trung.supper.englishgrammar.repositorys.*;
import trung.supper.englishgrammar.services.IExerciseAttemptService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseAttemptServiceImpl implements IExerciseAttemptService {

    private final IUserExerciseAttemptRepository attemptRepository;
    private final IUserAnswerLogRepository answerLogRepository;
    private final IExerciseRepository exerciseRepository;
    private final IQuestionRepository questionRepository;
    private final IQuestionOptionRepository optionRepository;

    // ─── Start ───

    @Override
    @Transactional
    public StartAttemptResponse startAttempt(UUID exerciseId, UUID userId) {
        Exercise exercise = validateExerciseExists(exerciseId);

        // Check if user already has an IN_PROGRESS attempt for this exercise
        attemptRepository.findByExerciseIdAndUserIdAndStatus(exerciseId, userId, AttemptStatus.IN_PROGRESS)
                .ifPresent(existing -> {
                    throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
                });

        User userRef = new User();
        userRef.setId(userId);

        UserExerciseAttempt attempt = UserExerciseAttempt.builder()
                .user(userRef)
                .exercise(exercise)
                .status(AttemptStatus.IN_PROGRESS)
                .build();

        UserExerciseAttempt saved = attemptRepository.save(attempt);

        return StartAttemptResponse.builder()
                .attemptId(saved.getId())
                .startTime(saved.getAttemptedAt())
                .build();
    }

    // ─── Submit ───

    @Override
    @Transactional
    public AttemptResponse submitAttempt(UUID exerciseId, UUID userId, SubmitAttemptRequest request) {
        validateExerciseExists(exerciseId);

        UserExerciseAttempt attempt = attemptRepository
                .findByExerciseIdAndUserIdAndStatus(exerciseId, userId, AttemptStatus.IN_PROGRESS)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        // Score calculation
        double totalScore = 0;
        double maxScore = 0;
        List<UserAnswerLog> logs = new ArrayList<>();

        for (AnswerRequest answer : request.getAnswers()) {
            Question question = questionRepository.findById(answer.getQuestionId())
                    .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

            int weight = question.getScoreWeight() != null ? question.getScoreWeight() : 1;
            maxScore += weight;

            boolean isCorrect = evaluateAnswer(question, answer);
            if (isCorrect) {
                totalScore += weight;
            }

            // For MULTIPLE_CHOICE — log each selected option separately
            if (answer.getSelectedOptionIds() != null && !answer.getSelectedOptionIds().isEmpty()) {
                for (UUID optionId : answer.getSelectedOptionIds()) {
                    QuestionOption option = optionRepository.findById(optionId)
                            .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
                    logs.add(UserAnswerLog.builder()
                            .attempt(attempt)
                            .question(question)
                            .selectedOption(option)
                            .isCorrect(option.getIsCorrect())
                            .build());
                }
            } else {
                // Text-based answer
                logs.add(UserAnswerLog.builder()
                        .attempt(attempt)
                        .question(question)
                        .textAnswer(answer.getTextAnswer())
                        .isCorrect(isCorrect)
                        .build());
            }
        }

        // Update attempt
        attempt.setScore(totalScore);
        attempt.setMaxScore(maxScore);
        attempt.setStatus(AttemptStatus.SUBMITTED);
        attempt.setSubmittedAt(LocalDateTime.now());

        // Calculate isPassed based on exercise.passScore
        Exercise exercise = attempt.getExercise();
        if (exercise.getPassScore() != null && maxScore > 0) {
            double percentage = (totalScore / maxScore) * 100;
            attempt.setIsPassed(percentage >= exercise.getPassScore());
        } else {
            attempt.setIsPassed(true);
        }

        // Calculate time spent
        if (attempt.getAttemptedAt() != null) {
            long seconds = java.time.Duration.between(attempt.getAttemptedAt(), attempt.getSubmittedAt()).getSeconds();
            attempt.setTimeSpentSeconds((int) seconds);
        }

        attemptRepository.save(attempt);
        answerLogRepository.saveAll(logs);

        return toAttemptResponse(attempt);
    }

    // ─── History ───

    @Override
    @Transactional(readOnly = true)
    public List<AttemptResponse> getMyAttempts(UUID exerciseId, UUID userId) {
        validateExerciseExists(exerciseId);
        return attemptRepository.findByExerciseIdAndUserIdOrderByAttemptedAtDesc(exerciseId, userId)
                .stream().map(this::toAttemptResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AttemptDetailResponse getAttemptDetail(UUID attemptId, UUID userId) {
        UserExerciseAttempt attempt = attemptRepository.findByIdAndUserId(attemptId, userId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        return toAttemptDetailResponse(attempt);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttemptResponse> getAllAttempts(UUID exerciseId) {
        validateExerciseExists(exerciseId);
        return attemptRepository.findByExerciseIdOrderByAttemptedAtDesc(exerciseId)
                .stream().map(this::toAttemptResponse).collect(Collectors.toList());
    }

    // ─── Scoring Logic (extensible for Strategy Pattern) ───

    private boolean evaluateAnswer(Question question, AnswerRequest answer) {
        if (answer.getSelectedOptionIds() != null && !answer.getSelectedOptionIds().isEmpty()) {
            // Option-based: all selected must be correct, and all correct must be selected
            List<QuestionOption> options = optionRepository.findByQuestionIdOrderByOrderIndexAsc(question.getId());
            List<UUID> correctIds = options.stream()
                    .filter(QuestionOption::getIsCorrect)
                    .map(QuestionOption::getId)
                    .collect(Collectors.toList());

            List<UUID> selectedIds = answer.getSelectedOptionIds();
            return correctIds.size() == selectedIds.size() && correctIds.containsAll(selectedIds);
        }
        // Text-based answers: for now return false (need manual review or strategy)
        return false;
    }

    // ─── Mapping Helpers ───

    private AttemptResponse toAttemptResponse(UserExerciseAttempt attempt) {
        return AttemptResponse.builder()
                .id(attempt.getId())
                .score(attempt.getScore())
                .maxScore(attempt.getMaxScore())
                .isPassed(attempt.getIsPassed())
                .status(attempt.getStatus())
                .startedAt(attempt.getAttemptedAt())
                .submittedAt(attempt.getSubmittedAt())
                .build();
    }

    private AttemptDetailResponse toAttemptDetailResponse(UserExerciseAttempt attempt) {
        List<UserAnswerLog> logs = answerLogRepository.findByAttemptId(attempt.getId());

        List<AnswerDetailResponse> answerDetails = logs.stream().map(log -> {
            // Find correct options for reference
            List<UUID> correctOptionIds = optionRepository
                    .findByQuestionIdOrderByOrderIndexAsc(log.getQuestion().getId())
                    .stream()
                    .filter(QuestionOption::getIsCorrect)
                    .map(QuestionOption::getId)
                    .collect(Collectors.toList());

            return AnswerDetailResponse.builder()
                    .questionId(log.getQuestion().getId())
                    .questionContent(log.getQuestion().getContent())
                    .selectedOptionId(log.getSelectedOption() != null ? log.getSelectedOption().getId() : null)
                    .selectedOptionContent(log.getSelectedOption() != null ? log.getSelectedOption().getContent() : null)
                    .textAnswer(log.getTextAnswer())
                    .isCorrect(log.getIsCorrect())
                    .correctOptionIds(correctOptionIds)
                    .build();
        }).collect(Collectors.toList());

        return AttemptDetailResponse.builder()
                .id(attempt.getId())
                .score(attempt.getScore())
                .maxScore(attempt.getMaxScore())
                .isPassed(attempt.getIsPassed())
                .status(attempt.getStatus())
                .startedAt(attempt.getAttemptedAt())
                .submittedAt(attempt.getSubmittedAt())
                .answers(answerDetails)
                .build();
    }

    private Exercise validateExerciseExists(UUID exerciseId) {
        return exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
    }
}
