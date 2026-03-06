package trung.supper.englishgrammar.mapper;

import org.mapstruct.Mapper;
import trung.supper.englishgrammar.dto.response.UserResponseDTO;
import trung.supper.englishgrammar.models.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDTO toUserResponseDTO(User user);
}
