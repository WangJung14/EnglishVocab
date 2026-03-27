package trung.supper.englishgrammar.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import trung.supper.englishgrammar.dto.request.TopicReorderRequest;
import trung.supper.englishgrammar.dto.request.TopicRequest;
import trung.supper.englishgrammar.dto.response.ApiRespone;
import trung.supper.englishgrammar.dto.response.TopicResponse;
import trung.supper.englishgrammar.services.ITopicService;

import java.util.UUID;

@RestController
@RequestMapping("/teacher/topics")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
public class TeacherTopicController {

    private final ITopicService topicService;

    @PostMapping
    public ApiRespone<TopicResponse> createTopic(@Valid @RequestBody TopicRequest request) {
        return ApiRespone.<TopicResponse>builder()
                .result(topicService.createTopic(request))
                .code(1000)
                .message("success")
                .build();
    }

    @PutMapping("/{id}")
    public ApiRespone<TopicResponse> updateTopic(@PathVariable UUID id, @Valid @RequestBody TopicRequest request) {
        return ApiRespone.<TopicResponse>builder()
                .result(topicService.updateTopic(id, request))
                .code(1000)
                .message("success")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiRespone<String> deleteTopic(@PathVariable UUID id) {
        topicService.deleteTopic(id);
        return ApiRespone.<String>builder()
                .result("Deleted successfully")
                .code(1000)
                .message("success")
                .build();
    }

    @PutMapping("/{id}/publish")
    public ApiRespone<TopicResponse> togglePublish(@PathVariable UUID id) {
        return ApiRespone.<TopicResponse>builder()
                .result(topicService.togglePublish(id))
                .code(1000)
                .message("success")
                .build();
    }

    @PutMapping("/reorder")
    public ApiRespone<String> reorderTopics(@RequestBody TopicReorderRequest request) {
        topicService.reorderTopics(request);
        return ApiRespone.<String>builder()
                .result("Reordered successfully")
                .code(1000)
                .message("success")
                .build();
    }
}
