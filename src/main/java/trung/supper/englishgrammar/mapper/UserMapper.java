package trung.supper.englishgrammar.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import trung.supper.englishgrammar.dto.request.UpdateUserRequest;
import trung.supper.englishgrammar.dto.response.MembershipResponse;
import trung.supper.englishgrammar.dto.response.UserResponse;
import trung.supper.englishgrammar.models.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "role", expression = "java(user.getRole().name())")
    @Mapping(target = "membershipType", expression = "java(user.getMembershipType().name())")
    UserResponse toUserResponseDTO(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(@MappingTarget User user, UpdateUserRequest request);

    @Mapping(target = "isActive", expression = "java(user.getMembershipExpiresAt() != null && user.getMembershipExpiresAt().isAfter(java.time.LocalDateTime.now()) && user.getMembershipType() != trung.supper.englishgrammar.enums.MembershipType.FREE)")
    MembershipResponse toMembershipResponse(User user);
}
