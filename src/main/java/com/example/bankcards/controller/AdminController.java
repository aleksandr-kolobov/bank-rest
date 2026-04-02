package com.example.bankcards.controller;

import com.example.bankcards.api.AdminUserManagementApi;
import com.example.bankcards.dto.GetAllUsers200Response;
import com.example.bankcards.dto.UpdateUserRequest;
import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminController implements AdminUserManagementApi {

    private final UserService userService;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GetAllUsers200Response> getAllUsers(Integer page, Integer size) {
        log.debug("Getting all users - page: {}, size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<UserDTO> usersPage = userService.getAllUsers(pageable);

        GetAllUsers200Response response = new GetAllUsers200Response();
        response.setContent(usersPage.getContent());
        response.setTotalElements((int) usersPage.getTotalElements());
        response.setTotalPages(usersPage.getTotalPages());
        response.setSize(usersPage.getSize());
        response.setNumber(usersPage.getNumber());
        response.setFirst(usersPage.isFirst());
        response.setLast(usersPage.isLast());
        response.setEmpty(usersPage.isEmpty());

        return ResponseEntity.ok(response);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserById(UUID userId) {
        log.debug("Getting user by id: {}", userId);
        UserDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUser(UUID userId, UpdateUserRequest updateUserRequest) {
        log.debug("Updating user: {} with request: {}", userId, updateUserRequest);
        UserDTO updatedUser = userService.updateUser(userId, updateUserRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(UUID userId) {
        log.debug("Deleting user: {}", userId);
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
