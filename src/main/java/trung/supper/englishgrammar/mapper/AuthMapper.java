package trung.supper.englishgrammar.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import trung.supper.englishgrammar.dto.request.RegisterRequest;
import trung.supper.englishgrammar.models.User;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "role", constant = "STUDENT")
    @Mapping(target = "membershipType", constant = "FREE")
    @Mapping(target = "isActive", constant = "true")
    User toUser(RegisterRequest request);

}
