package com.example.bankcards.repository;

import com.example.bankcards.entity.RoleType;
import com.example.bankcards.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserSpecification {

    public static Specification<User> byEmail(String email) {
        return (root, query, cb) -> email == null || email.isEmpty()
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }

    public static Specification<User> byFirstName(String firstName) {
        return (root, query, cb) -> firstName == null || firstName.isEmpty()
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("firstname")), "%" + firstName.toLowerCase() + "%");
    }

    public static Specification<User> byLastName(String lastName) {
        return (root, query, cb) -> lastName == null || lastName.isEmpty()
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("lastname")), "%" + lastName.toLowerCase() + "%");
    }

    public static Specification<User> byEnabled(Boolean enabled) {
        return (root, query, cb) -> enabled == null
                ? cb.conjunction()
                : cb.equal(root.get("enabled"), enabled);
    }

    public static Specification<User> byRole(RoleType role) {
        return (root, query, cb) -> role == null
                ? cb.conjunction()
                : cb.isMember(role, root.get("roles"));
    }

    public static Specification<User> byRoles(Set<RoleType> roles) {
        return (root, query, cb) -> roles == null || roles.isEmpty()
                ? cb.conjunction()
                : cb.isTrue(root.join("roles").in(roles));
    }

    public static Specification<User> withActiveCards() {
        return (root, query, cb) -> cb.greaterThan(cb.size(root.get("cards")), 0);
    }

    public static Specification<User> withoutCards() {
        return (root, query, cb) -> cb.equal(cb.size(root.get("cards")), 0);
    }
}
