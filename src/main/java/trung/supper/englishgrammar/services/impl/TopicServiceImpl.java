package trung.supper.englishgrammar.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trung.supper.englishgrammar.dto.request.TopicReorderRequest;
import trung.supper.englishgrammar.dto.request.TopicRequest;
import trung.supper.englishgrammar.dto.response.TopicResponse;
import trung.supper.englishgrammar.enums.ErrorCode;
import trung.supper.englishgrammar.enums.Level;
import trung.supper.englishgrammar.exception.AppException;
import trung.supper.englishgrammar.mapper.TopicMapper;
import trung.supper.englishgrammar.models.Category;
import trung.supper.englishgrammar.models.Topic;
import trung.supper.englishgrammar.models.User;
import trung.supper.englishgrammar.repositorys.ICategoryRepository;
import trung.supper.englishgrammar.repositorys.ITopicRepository;
import trung.supper.englishgrammar.repositorys.IUserRepository;
import trung.supper.englishgrammar.services.ITopicService;
import trung.supper.englishgrammar.utils.SlugUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements ITopicService {

    private final ITopicRepository topicRepository;
    private final ICategoryRepository categoryRepository;
    private final IUserRepository userRepository;
    private final TopicMapper topicMapper;

    @Override
    @Transactional(readOnly = true)
    public List<TopicResponse> getTopics(String categorySlug, Level level) {
        return topicRepository.findTopicsByFilter(categorySlug, level).stream()
                .map(topicMapper::toTopicResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopicResponse> getTopicsByCategorySlug(String categorySlug) {
        return topicRepository.findByCategory_SlugOrderByOrderIndexAsc(categorySlug).stream()
                .map(topicMapper::toTopicResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TopicResponse getTopicBySlug(String slug) {
        Topic topic = topicRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        return topicMapper.toTopicResponse(topic);
    }

    @Override
    @Transactional
    public TopicResponse createTopic(TopicRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));

        String slug = generateUniqueSlug(request.getName(), null);
        
        Topic topic = topicMapper.toTopic(request);
        topic.setSlug(slug);
        topic.setCategory(category);
        topic.setCreatedBy(currentUser);
        topic.setIsPublished(false); // Default logic

        topic = topicRepository.save(topic);
        return topicMapper.toTopicResponse(topic);
    }

    @Override
    @Transactional
    public TopicResponse updateTopic(UUID id, TopicRequest request) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        if (!topic.getTitle().equals(request.getName())) {
            String newSlug = generateUniqueSlug(request.getName(), id);
            topic.setSlug(newSlug);
        }

        topicMapper.updateTopic(topic, request);
        topic.setCategory(category);
        
        topic = topicRepository.save(topic);
        return topicMapper.toTopicResponse(topic);
    }

    @Override
    @Transactional
    public void deleteTopic(UUID id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        topicRepository.delete(topic);
    }

    @Override
    @Transactional
    public TopicResponse togglePublish(UUID id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        
        Boolean current = topic.getIsPublished() != null ? topic.getIsPublished() : false;
        topic.setIsPublished(!current);
        
        topic = topicRepository.save(topic);
        return topicMapper.toTopicResponse(topic);
    }

    @Override
    @Transactional
    public void reorderTopics(TopicReorderRequest request) {
        if (request.getIds() == null || request.getIds().isEmpty()) return;

        List<Topic> topics = topicRepository.findAllById(request.getIds());

        for (int i = 0; i < request.getIds().size(); i++) {
            UUID currentId = request.getIds().get(i);
            int index = i;
            topics.stream()
                .filter(t -> t.getId().equals(currentId))
                .findFirst()
                .ifPresent(t -> t.setOrderIndex(index));
        }
        topicRepository.saveAll(topics);
    }

    private String generateUniqueSlug(String name, UUID excludeId) {
        String baseSlug = SlugUtils.toSlug(name);
        String uniqueSlug = baseSlug;
        int count = 1;

        while (true) {
            String finalSlug = uniqueSlug;
            Topic existing = topicRepository.findBySlug(finalSlug).orElse(null);
            if (existing == null || existing.getId().equals(excludeId)) {
                break;
            }
            uniqueSlug = baseSlug + "-" + count;
            count++;
        }
        return uniqueSlug;
    }
}
