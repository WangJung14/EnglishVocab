package trung.supper.englishgrammar.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trung.supper.englishgrammar.dto.request.LessonReorderRequest;
import trung.supper.englishgrammar.dto.request.LessonRequest;
import trung.supper.englishgrammar.dto.response.LessonResponse;
import trung.supper.englishgrammar.enums.ErrorCode;
import trung.supper.englishgrammar.exception.AppException;
import trung.supper.englishgrammar.mapper.LessonMapper;
import trung.supper.englishgrammar.models.Lesson;
import trung.supper.englishgrammar.models.Topic;
import trung.supper.englishgrammar.models.User;
import trung.supper.englishgrammar.repositorys.ILessonRepository;
import trung.supper.englishgrammar.repositorys.ITopicRepository;
import trung.supper.englishgrammar.repositorys.IUserRepository;
import trung.supper.englishgrammar.services.ILessonService;
import trung.supper.englishgrammar.services.strategy.LessonStrategyFactory;
import trung.supper.englishgrammar.services.strategy.ILessonStrategy;
import trung.supper.englishgrammar.utils.SlugUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements ILessonService {

    private final ILessonRepository lessonRepository;
    private final ITopicRepository topicRepository;
    private final IUserRepository userRepository;
    private final LessonMapper lessonMapper;
    private final LessonStrategyFactory strategyFactory;

    @Override
    @Transactional(readOnly = true)
    public List<LessonResponse> getLessonsByTopicSlug(String topicSlug) {
        return lessonRepository.findByTopic_SlugOrderByOrderIndexAsc(topicSlug).stream()
                .map(lessonMapper::toLessonResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public LessonResponse getLessonBySlug(String slug) {
        Lesson lesson = lessonRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        return lessonMapper.toLessonResponse(lesson);
    }

    @Override
    @Transactional
    public LessonResponse createLesson(LessonRequest request) {
        Topic topic = topicRepository.findById(request.getTopicId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));

        Lesson lesson = lessonMapper.toLesson(request);
        
        // Strategy Processing
        ILessonStrategy strategy = strategyFactory.getStrategy(request.getLessonType());
        strategy.processContent(lesson, request);

        String slug = generateUniqueSlug(request.getTitle(), null);
        lesson.setSlug(slug);
        lesson.setTopic(topic);
        lesson.setCreatedBy(currentUser);
        lesson.setIsPublished(false); // explicit default

        lesson = lessonRepository.save(lesson);
        return lessonMapper.toLessonResponse(lesson);
    }

    @Override
    @Transactional
    public LessonResponse updateLesson(UUID id, LessonRequest request) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        Topic topic = topicRepository.findById(request.getTopicId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        if (!lesson.getTitle().equals(request.getTitle())) {
            String newSlug = generateUniqueSlug(request.getTitle(), id);
            lesson.setSlug(newSlug);
        }

        lessonMapper.updateLesson(lesson, request);
        
        // Strategy Processing for updates
        ILessonStrategy strategy = strategyFactory.getStrategy(request.getLessonType());
        strategy.processContent(lesson, request);
        
        lesson.setTopic(topic);

        lesson = lessonRepository.save(lesson);
        return lessonMapper.toLessonResponse(lesson);
    }

    @Override
    @Transactional
    public void deleteLesson(UUID id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        lessonRepository.delete(lesson);
    }

    @Override
    @Transactional
    public LessonResponse togglePublish(UUID id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        Boolean current = lesson.getIsPublished() != null ? lesson.getIsPublished() : false;
        lesson.setIsPublished(!current);
        lesson = lessonRepository.save(lesson);
        return lessonMapper.toLessonResponse(lesson);
    }

    @Override
    @Transactional
    public void reorderLessons(LessonReorderRequest request) {
        if (request.getIds() == null || request.getIds().isEmpty()) return;

        List<Lesson> lessons = lessonRepository.findAllById(request.getIds());

        for (int i = 0; i < request.getIds().size(); i++) {
            UUID currentId = request.getIds().get(i);
            int index = i;
            lessons.stream()
                .filter(l -> l.getId().equals(currentId))
                .findFirst()
                .ifPresent(l -> l.setOrderIndex(index));
        }
        lessonRepository.saveAll(lessons);
    }

    private String generateUniqueSlug(String title, UUID excludeId) {
        String baseSlug = SlugUtils.toSlug(title);
        String uniqueSlug = baseSlug;
        int count = 1;

        while (true) {
            String finalSlug = uniqueSlug;
            Lesson existing = lessonRepository.findBySlug(finalSlug).orElse(null);
            if (existing == null || existing.getId().equals(excludeId)) {
                break;
            }
            uniqueSlug = baseSlug + "-" + count;
            count++;
        }
        return uniqueSlug;
    }
}
