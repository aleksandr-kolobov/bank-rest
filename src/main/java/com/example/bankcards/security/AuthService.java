package com.example.bankcards.security;

import com.example.bankcards.dto.AuthResponse;
import com.example.bankcards.dto.CreateUserRequest;
import com.example.bankcards.dto.CreateUserResponse;
import com.example.bankcards.dto.LoginRequest;
import com.example.bankcards.dto.RefreshTokenRequest;
import com.example.bankcards.entity.RefreshToken;
import com.example.bankcards.entity.RoleType;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.AlreadyExistsException;
import com.example.bankcards.exception.RefreshTokenException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for authentification
 */

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final RefreshTokenService refreshTokenService;

    private final UserMapper userMapper;

    private final JwtUtils jwtUtils;

    public CreateUserResponse registerUser(CreateUserRequest request) {
        String email = request.email();
        if (userRepository.existsByEmail(email)) {
            throw new AlreadyExistsException("Пользователь с email:%s уже существует!".formatted(email));
        }
        User user = userMapper.map(request);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        return userMapper.map(userRepository.save(user));
    }

    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();

        String email = userDetails.getUsername();
        UUID userId = userDetails.getId();
        String roles = userDetails
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userId);

        return new AuthResponse(userDetails.getUsername(),
                jwtUtils.generateJwtToken(email, userId, roles),
                refreshToken.getToken().toString());
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        UUID refreshToken = UUID.fromString(request.token());

        return refreshTokenService.findByRefreshToken(refreshToken)
                .map(refreshTokenService::checkRefreshToken)
                .map(RefreshToken::getUserId)
                .map(userId -> {
                    User user = userRepository.findById(userId).orElseThrow(() ->
                            new RefreshTokenException("Токен обновления не найден, userId: " + userId));
                    String email = user.getEmail();
                    String roles = user.getRoles().stream()
                            .map(RoleType::name)
                            .collect(Collectors.joining(", "));
                    String newAccessToken = jwtUtils.generateJwtToken(email, userId, roles);
                    String newRefreshToken = refreshTokenService.createRefreshToken(userId).getToken().toString();
                    return new AuthResponse(email, newAccessToken, newRefreshToken);
                }).orElseThrow(() -> new RefreshTokenException(refreshToken, "Токен обновления не найден"));
    }

    public void logout() {
        var currentPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentPrincipal instanceof AppUserDetails userDetails) {
            UUID userId = userDetails.getId();
            refreshTokenService.deleteRefreshToken(userId);
        }
    }
}
