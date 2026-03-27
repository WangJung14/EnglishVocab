package trung.supper.englishgrammar.services;

import trung.supper.englishgrammar.dto.request.TopicReorderRequest;
import trung.supper.englishgrammar.dto.request.TopicRequest;
import trung.supper.englishgrammar.dto.response.TopicResponse;
import trung.supper.englishgrammar.enums.Level;

import java.util.List;
import java.util.UUID;

public interface ITopicService {
    List<TopicResponse> getTopics(String categorySlug, Level level);
    List<TopicResponse> getTopicsByCategorySlug(String categorySlug);
    TopicResponse getTopicBySlug(String slug);
    TopicResponse createTopic(TopicRequest request);
    TopicResponse updateTopic(UUID id, TopicRequest request);
    void deleteTopic(UUID id);
    TopicResponse togglePublish(UUID id);
    void reorderTopics(TopicReorderRequest request);
}
