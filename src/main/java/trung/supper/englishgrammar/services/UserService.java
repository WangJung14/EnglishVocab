package trung.supper.englishgrammar.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import trung.supper.englishgrammar.dto.response.UserResponseDTO;
import trung.supper.englishgrammar.repositorys.UserRepository;

import java.util.UUID;

@Service
public interface UserService {

    public UserResponseDTO getMyProfile(UUID userId);
}
