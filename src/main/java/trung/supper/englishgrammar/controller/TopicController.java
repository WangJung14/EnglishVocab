package trung.supper.englishgrammar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import trung.supper.englishgrammar.dto.response.ApiRespone;
import trung.supper.englishgrammar.dto.response.TopicResponse;
import trung.supper.englishgrammar.enums.Level;
import trung.supper.englishgrammar.services.ITopicService;

import java.util.List;

@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicController {

    private final ITopicService topicService;

    @GetMapping
    public ApiRespone<List<TopicResponse>> getTopics(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Level level) {
        return ApiRespone.<List<TopicResponse>>builder()
                .result(topicService.getTopics(category, level))
                .code(1000)
                .message("success")
                .build();
    }

    @GetMapping("/{slug}")
    public ApiRespone<TopicResponse> getTopicBySlug(@PathVariable String slug) {
        return ApiRespone.<TopicResponse>builder()
                .result(topicService.getTopicBySlug(slug))
                .code(1000)
                .message("success")
                .build();
    }
}
