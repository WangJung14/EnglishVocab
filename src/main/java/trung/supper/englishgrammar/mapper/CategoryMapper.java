package trung.supper.englishgrammar.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import trung.supper.englishgrammar.dto.request.CategoryRequest;
import trung.supper.englishgrammar.dto.response.CategoryResponse;
import trung.supper.englishgrammar.models.Category;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {

    @Mapping(source = "orderIndex", target = "displayOrder")
    @Mapping(target = "isActive", constant = "true")
    CategoryResponse toCategoryResponse(Category category);

    @Mapping(source = "displayOrder", target = "orderIndex")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "iconUrl", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "topics", ignore = true)
    Category toCategory(CategoryRequest request);

    @Mapping(source = "displayOrder", target = "orderIndex")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "iconUrl", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "topics", ignore = true)
    void updateCategory(@MappingTarget Category category, CategoryRequest request);
}
