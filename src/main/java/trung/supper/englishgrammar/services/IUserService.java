package trung.supper.englishgrammar.services;

import org.springframework.stereotype.Service;
import trung.supper.englishgrammar.dto.response.UserResponseDTO;

import java.util.List;
import java.util.UUID;

public interface IUserService {

    public UserResponseDTO getMyProfile(UUID userId);
    public List<UserResponseDTO> getAllUsers();
}
