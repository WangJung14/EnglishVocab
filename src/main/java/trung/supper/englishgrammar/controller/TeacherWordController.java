package trung.supper.englishgrammar.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import trung.supper.englishgrammar.dto.request.WordRequest;
import trung.supper.englishgrammar.dto.response.ApiRespone;
import trung.supper.englishgrammar.dto.response.WordResponse;
import trung.supper.englishgrammar.services.IWordService;

import java.util.UUID;

@RestController
@RequestMapping("/teacher/words")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
public class TeacherWordController {

    private final IWordService wordService;

    @PostMapping
    public ApiRespone<WordResponse> createWord(@Valid @RequestBody WordRequest request) {
        return ApiRespone.<WordResponse>builder()
                .result(wordService.createWord(request))
                .code(1000)
                .message("success")
                .build();
    }

    @PutMapping("/{id}")
    public ApiRespone<WordResponse> updateWord(@PathVariable UUID id, @Valid @RequestBody WordRequest request) {
        return ApiRespone.<WordResponse>builder()
                .result(wordService.updateWord(id, request))
                .code(1000)
                .message("success")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiRespone<String> deleteWord(@PathVariable UUID id) {
        wordService.deleteWord(id);
        return ApiRespone.<String>builder()
                .result("Deleted successfully")
                .code(1000)
                .message("success")
                .build();
    }
}
