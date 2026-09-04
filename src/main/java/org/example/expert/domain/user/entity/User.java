package org.example.expert.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.entity.Timestamped;
import org.example.expert.domain.user.enums.UserRole;

@Getter
@Entity
@Table(name = "users")
public class User extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    private String nickname;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    protected User() {
    }

    private User(Builder builder) {
        this.id = builder.id;
        this.email = builder.email;
        this.password = builder.password;
        this.nickname = builder.nickname;
        this.userRole = builder.userRole;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String email;
        private String password;
        private String nickname;
        private UserRole userRole;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder password(String password) { this.password = password; return this; }
        public Builder nickname(String nickname) { this.nickname = nickname; return this; }
        public Builder userRole(UserRole userRole) { this.userRole = userRole; return this; }

        public User build() {
            return new User(this);
        }
    }

    public static User fromAuthUser(AuthUser authUser) {
        return User.builder()
                .id(authUser.getId())
                .email(authUser.getEmail())
                .nickname(authUser.getNickname())
                .userRole(authUser.getUserRole())
                .build();
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void updateRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
