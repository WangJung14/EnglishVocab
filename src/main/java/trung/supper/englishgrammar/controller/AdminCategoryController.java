package trung.supper.englishgrammar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import trung.supper.englishgrammar.dto.request.CategoryReorderRequest;
import trung.supper.englishgrammar.dto.request.CategoryRequest;
import trung.supper.englishgrammar.dto.response.ApiRespone;
import trung.supper.englishgrammar.dto.response.CategoryResponse;
import trung.supper.englishgrammar.services.ICategoryService;

import jakarta.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminCategoryController {

    private final ICategoryService categoryService;

    @PostMapping
    public ApiRespone<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        return ApiRespone.<CategoryResponse>builder()
                .result(categoryService.createCategory(request))
                .code(1000)
                .message("success")
                .build();
    }

    @PutMapping("/{id}")
    public ApiRespone<CategoryResponse> updateCategory(@PathVariable UUID id, @Valid @RequestBody CategoryRequest request) {
        return ApiRespone.<CategoryResponse>builder()
                .result(categoryService.updateCategory(id, request))
                .code(1000)
                .message("success")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiRespone<String> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ApiRespone.<String>builder()
                .result("Deleted successfully")
                .code(1000)
                .message("success")
                .build();
    }

    @PutMapping("/reorder")
    public ApiRespone<String> reorderCategories(@RequestBody CategoryReorderRequest request) {
        categoryService.reorderCategories(request);
        return ApiRespone.<String>builder()
                .result("Reordered successfully")
                .code(1000)
                .message("success")
                .build();
    }
}
