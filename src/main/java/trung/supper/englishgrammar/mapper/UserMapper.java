package trung.supper.englishgrammar.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import trung.supper.englishgrammar.dto.response.UserResponse;
import trung.supper.englishgrammar.models.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "role", expression = "java(user.getRole().name())")
    @Mapping(target = "membershipType", expression = "java(user.getMembershipType().name())")
    UserResponse toUserResponseDTO(User user);
}
