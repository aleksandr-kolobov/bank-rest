package com.example.bankcards.util;

import com.example.bankcards.dto.CreateUserRequest;
import com.example.bankcards.dto.CreateUserResponse;
import com.example.bankcards.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * MapStruct mapper for {@link User}
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class UserMapper {

    public abstract CreateUserResponse map(User user);

    public abstract User map(CreateUserRequest request);

}
