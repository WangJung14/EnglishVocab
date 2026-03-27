package trung.supper.englishgrammar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import trung.supper.englishgrammar.dto.response.ApiRespone;
import trung.supper.englishgrammar.dto.response.TopicResponse;
import trung.supper.englishgrammar.enums.Level;
import trung.supper.englishgrammar.services.ITopicService;
import trung.supper.englishgrammar.dto.response.LessonResponse;
import trung.supper.englishgrammar.services.ILessonService;

import java.util.List;

@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicController {

    private final ITopicService topicService;
    private final ILessonService lessonService;

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

    @GetMapping("/{topicSlug}/lessons")
    public ApiRespone<List<LessonResponse>> getLessonsByTopicSlug(@PathVariable String topicSlug) {
        return ApiRespone.<List<LessonResponse>>builder()
                .result(lessonService.getLessonsByTopicSlug(topicSlug))
                .code(1000)
                .message("success")
                .build();
    }
}
