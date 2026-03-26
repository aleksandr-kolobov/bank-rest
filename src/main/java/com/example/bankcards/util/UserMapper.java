package com.example.bankcards.util;

import com.example.bankcards.dto.CreateUserRequest;
import com.example.bankcards.dto.CreateUserResponse;
import com.example.bankcards.dto.UpdateUserRequest;
import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.RoleType;
import com.example.bankcards.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * MapStruct mapper for {@link User}
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class UserMapper {

    public abstract CreateUserResponse map(User user);

    public abstract User map(CreateUserRequest request);

    public abstract UserDTO toUserDTO(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    public abstract User updateUserEntity(UpdateUserRequest request, @MappingTarget User user);

    @Named("rolesToString")
    public Set<String> rolesToString(Set<RoleType> roles) {
        if (roles == null) return Set.of();
        return roles.stream()
                .map(RoleType::name)
                .collect(Collectors.toSet());
    }

    @Named("stringToRoles")
    public Set<RoleType> stringToRoles(Set<String> roles) {
        if (roles == null) return Set.of();
        return roles.stream()
                .map(RoleType::valueOf)
                .collect(Collectors.toSet());
    }

}
