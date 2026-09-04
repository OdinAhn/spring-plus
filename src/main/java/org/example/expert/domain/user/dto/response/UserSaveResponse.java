package org.example.expert.domain.user.dto.response;

import lombok.Getter;

@Getter
public class UserSaveResponse {

    private final String bearerToken;

    private UserSaveResponse(Builder builder) {
        this.bearerToken = builder.bearerToken;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String bearerToken;

        public Builder bearerToken(String bearerToken) { this.bearerToken = bearerToken; return this; }

        public UserSaveResponse build() {
            return new UserSaveResponse(this);
        }
    }
}
