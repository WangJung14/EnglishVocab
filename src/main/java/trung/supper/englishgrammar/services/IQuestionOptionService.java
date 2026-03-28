package trung.supper.englishgrammar.services;

import trung.supper.englishgrammar.dto.request.OptionRequest;
import trung.supper.englishgrammar.dto.response.OptionResponse;

import java.util.UUID;

public interface IQuestionOptionService {
    OptionResponse addOption(UUID questionId, OptionRequest request);
    OptionResponse updateOption(UUID questionId, UUID optionId, OptionRequest request);
    void deleteOption(UUID questionId, UUID optionId);
}
