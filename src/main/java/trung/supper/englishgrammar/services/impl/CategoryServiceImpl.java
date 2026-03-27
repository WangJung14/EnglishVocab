package trung.supper.englishgrammar.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trung.supper.englishgrammar.dto.request.CategoryReorderRequest;
import trung.supper.englishgrammar.dto.request.CategoryRequest;
import trung.supper.englishgrammar.dto.response.CategoryResponse;
import trung.supper.englishgrammar.enums.ErrorCode;
import trung.supper.englishgrammar.exception.AppException;
import trung.supper.englishgrammar.mapper.CategoryMapper;
import trung.supper.englishgrammar.models.Category;
import trung.supper.englishgrammar.repositorys.ICategoryRepository;
import trung.supper.englishgrammar.services.ICategoryService;
import trung.supper.englishgrammar.utils.SlugUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {

    private final ICategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .sorted((c1, c2) -> {
                    Integer o1 = c1.getOrderIndex();
                    Integer o2 = c2.getOrderIndex();
                    if (o1 == null) o1 = Integer.MAX_VALUE;
                    if (o2 == null) o2 = Integer.MAX_VALUE;
                    return o1.compareTo(o2);
                })
                .map(categoryMapper::toCategoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryBySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        String slug = generateUniqueSlug(request.getName(), null);
        Category category = categoryMapper.toCategory(request);
        category.setSlug(slug);
        
        category = categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(UUID id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        
        if (!category.getName().equals(request.getName())) {
            String newSlug = generateUniqueSlug(request.getName(), id);
            category.setSlug(newSlug);
        }
        
        categoryMapper.updateCategory(category, request);
        category = categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    @Transactional
    public void deleteCategory(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        categoryRepository.delete(category);
    }

    @Override
    @Transactional
    public void reorderCategories(CategoryReorderRequest request) {
        if (request.getIds() == null || request.getIds().isEmpty()) return;
        
        List<Category> categories = categoryRepository.findAllById(request.getIds());
        
        for (int i = 0; i < request.getIds().size(); i++) {
            UUID currentId = request.getIds().get(i);
            int index = i;
            categories.stream()
                .filter(c -> c.getId().equals(currentId))
                .findFirst()
                .ifPresent(c -> c.setOrderIndex(index));
        }
        categoryRepository.saveAll(categories);
    }

    private String generateUniqueSlug(String name, UUID excludeId) {
        String baseSlug = SlugUtils.toSlug(name);
        String uniqueSlug = baseSlug;
        int count = 1;

        while (true) {
            String finalSlug = uniqueSlug;
            Category existing = categoryRepository.findBySlug(finalSlug).orElse(null);
            if (existing == null || existing.getId().equals(excludeId)) {
                break;
            }
            uniqueSlug = baseSlug + "-" + count;
            count++;
        }
        return uniqueSlug;
    }
}
