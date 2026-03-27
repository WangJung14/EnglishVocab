package trung.supper.englishgrammar.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import trung.supper.englishgrammar.dto.request.TopicRequest;
import trung.supper.englishgrammar.dto.response.TopicResponse;
import trung.supper.englishgrammar.models.Topic;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TopicMapper {

    @Mapping(source = "title", target = "name")
    @Mapping(source = "category.name", target = "categoryName")
    TopicResponse toTopicResponse(Topic topic);

    @Mapping(source = "name", target = "title")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "thumbnailUrl", ignore = true)
    @Mapping(target = "isPublished", ignore = true)
    @Mapping(target = "orderIndex", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    Topic toTopic(TopicRequest request);

    @Mapping(source = "name", target = "title")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "thumbnailUrl", ignore = true)
    @Mapping(target = "isPublished", ignore = true)
    @Mapping(target = "orderIndex", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    void updateTopic(@MappingTarget Topic topic, TopicRequest request);
}
