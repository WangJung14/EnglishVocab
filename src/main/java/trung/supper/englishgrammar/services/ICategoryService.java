package trung.supper.englishgrammar.services;

import trung.supper.englishgrammar.dto.request.CategoryReorderRequest;
import trung.supper.englishgrammar.dto.request.CategoryRequest;
import trung.supper.englishgrammar.dto.response.CategoryResponse;

import java.util.List;
import java.util.UUID;

public interface ICategoryService {
    List<CategoryResponse> getAllCategories();
    CategoryResponse getCategoryBySlug(String slug);
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse updateCategory(UUID id, CategoryRequest request);
    void deleteCategory(UUID id);
    void reorderCategories(CategoryReorderRequest request);
}
