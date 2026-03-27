package trung.supper.englishgrammar.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import trung.supper.englishgrammar.dto.response.ApiRespone;
import trung.supper.englishgrammar.dto.response.CategoryResponse;
import trung.supper.englishgrammar.services.ICategoryService;
import trung.supper.englishgrammar.dto.response.TopicResponse;
import trung.supper.englishgrammar.services.ITopicService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService categoryService;
    private final ITopicService topicService;

    @GetMapping
    public ApiRespone<List<CategoryResponse>> getAllCategories() {
        return ApiRespone.<List<CategoryResponse>>builder()
                .result(categoryService.getAllCategories())
                .code(1000)
                .message("success")
                .build();
    }

    @GetMapping("/{slug}")
    public ApiRespone<CategoryResponse> getCategoryBySlug(@PathVariable String slug) {
        return ApiRespone.<CategoryResponse>builder()
                .result(categoryService.getCategoryBySlug(slug))
                .code(1000)
                .message("success")
                .build();
    }

    @GetMapping("/{slug}/topics")
    public ApiRespone<List<TopicResponse>> getTopicsByCategorySlug(@PathVariable String slug) {
        return ApiRespone.<List<TopicResponse>>builder()
                .result(topicService.getTopicsByCategorySlug(slug))
                .code(1000)
                .message("success")
                .build();
    }
}
