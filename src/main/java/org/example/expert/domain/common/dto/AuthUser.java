package org.example.expert.domain.common.dto;

import lombok.Getter;
import org.example.expert.domain.user.enums.UserRole;

@Getter
public class AuthUser {

    private final Long id;
    private final String email;
    private final String nickname;
    private final UserRole userRole;

    private AuthUser(Builder builder) {
        this.id = builder.id;
        this.email = builder.email;
        this.nickname = builder.nickname;
        this.userRole = builder.userRole;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String email;
        private String nickname;
        private UserRole userRole;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder nickname(String nickname) { this.nickname = nickname; return this; }
        public Builder userRole(UserRole userRole) { this.userRole = userRole; return this; }

        public AuthUser build() {
            return new AuthUser(this);
        }
    }
}
