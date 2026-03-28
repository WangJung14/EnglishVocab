package trung.supper.englishgrammar.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trung.supper.englishgrammar.dto.request.OptionRequest;
import trung.supper.englishgrammar.dto.response.OptionResponse;
import trung.supper.englishgrammar.enums.ErrorCode;
import trung.supper.englishgrammar.exception.AppException;
import trung.supper.englishgrammar.mapper.QuestionOptionMapper;
import trung.supper.englishgrammar.models.Question;
import trung.supper.englishgrammar.models.QuestionOption;
import trung.supper.englishgrammar.repositorys.IQuestionOptionRepository;
import trung.supper.englishgrammar.repositorys.IQuestionRepository;
import trung.supper.englishgrammar.services.IQuestionOptionService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestionOptionServiceImpl implements IQuestionOptionService {

    private final IQuestionOptionRepository optionRepository;
    private final IQuestionRepository questionRepository;
    private final QuestionOptionMapper optionMapper;

    @Override
    @Transactional
    public OptionResponse addOption(UUID questionId, OptionRequest request) {
        Question question = validateQuestionExists(questionId);

        // Prevent duplicate option content within same question
        if (optionRepository.existsByQuestionIdAndContent(questionId, request.getContent())) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        Integer currentMaxOrder = optionRepository.findMaxOrderIndexByQuestionId(questionId);
        int nextOrderIndex = (currentMaxOrder != null ? currentMaxOrder : 0) + 1;

        QuestionOption newOption = optionMapper.toEntity(request);
        newOption.setQuestion(question);
        newOption.setOrderIndex(nextOrderIndex);

        QuestionOption saved = optionRepository.save(newOption);
        return optionMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public OptionResponse updateOption(UUID questionId, UUID optionId, OptionRequest request) {
        validateQuestionExists(questionId);
        QuestionOption option = optionRepository.findById(optionId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        // Ensure option belongs to the specified question
        if (!option.getQuestion().getId().equals(questionId)) {
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND);
        }

        optionMapper.updateEntity(option, request);
        QuestionOption updated = optionRepository.save(option);
        return optionMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteOption(UUID questionId, UUID optionId) {
        validateQuestionExists(questionId);
        QuestionOption option = optionRepository.findById(optionId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        if (!option.getQuestion().getId().equals(questionId)) {
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND);
        }

        optionRepository.delete(option);
    }

    private Question validateQuestionExists(UUID questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
    }
}
