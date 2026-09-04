package org.example.expert.domain.user.dto.response;

import lombok.Getter;

@Getter
public class UserSearchResponse {

    private final Long id;
    private final String email;
    private final String nickname;

    private UserSearchResponse(Builder builder) {
        this.id = builder.id;
        this.email = builder.email;
        this.nickname = builder.nickname;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String email;
        private String nickname;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder nickname(String nickname) { this.nickname = nickname; return this; }

        public UserSearchResponse build() {
            return new UserSearchResponse(this);
        }
    }
}
