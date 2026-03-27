package trung.supper.englishgrammar.services.strategy;

import org.springframework.stereotype.Component;
import trung.supper.englishgrammar.enums.LessonType;
import trung.supper.englishgrammar.enums.ErrorCode;
import trung.supper.englishgrammar.exception.AppException;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Factory class automatically gathering all registered ILessonStrategy implementations 
 * and routing executions to the correct strategy dependent on the provided Type.
 */
@Component
public class LessonStrategyFactory {

    private final Map<LessonType, ILessonStrategy> strategies;

    public LessonStrategyFactory(List<ILessonStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(ILessonStrategy::getType, Function.identity()));
    }

    public ILessonStrategy getStrategy(LessonType type) {
        ILessonStrategy strategy = strategies.get(type);
        if (strategy == null) {
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND);
        }
        return strategy;
    }
}
