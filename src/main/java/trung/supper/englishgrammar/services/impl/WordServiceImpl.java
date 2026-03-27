package trung.supper.englishgrammar.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trung.supper.englishgrammar.dto.request.WordRequest;
import trung.supper.englishgrammar.dto.response.WordResponse;
import trung.supper.englishgrammar.enums.CefrLevel;
import trung.supper.englishgrammar.enums.ErrorCode;
import trung.supper.englishgrammar.enums.PartOfSpeech;
import trung.supper.englishgrammar.exception.AppException;
import trung.supper.englishgrammar.mapper.WordMapper;
import trung.supper.englishgrammar.models.User;
import trung.supper.englishgrammar.models.Word;
import trung.supper.englishgrammar.repositorys.IUserRepository;
import trung.supper.englishgrammar.repositorys.IWordRepository;
import trung.supper.englishgrammar.services.IWordService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WordServiceImpl implements IWordService {

    private final IWordRepository wordRepository;
    private final IUserRepository userRepository;
    private final WordMapper wordMapper;

    @Override
    @Transactional(readOnly = true)
    public List<WordResponse> getWords(CefrLevel level, PartOfSpeech partOfSpeech) {
        return wordRepository.findWordsByFilter(level, partOfSpeech).stream()
                .map(wordMapper::toWordResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public WordResponse getWordById(UUID id) {
        Word word = wordRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        return wordMapper.toWordResponse(word);
    }

    @Override
    @Transactional
    public WordResponse createWord(WordRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));

        Word word = wordMapper.toWord(request);
        word.setCreatedBy(currentUser);

        word = wordRepository.save(word);
        return wordMapper.toWordResponse(word);
    }

    @Override
    @Transactional
    public WordResponse updateWord(UUID id, WordRequest request) {
        Word word = wordRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        wordMapper.updateWord(word, request);
        word = wordRepository.save(word);
        return wordMapper.toWordResponse(word);
    }

    @Override
    @Transactional
    public void deleteWord(UUID id) {
        Word word = wordRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        wordRepository.delete(word);
    }
}
