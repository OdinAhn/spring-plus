package org.example.expert.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class SignupResponse {

    private final String bearerToken;

    private SignupResponse(Builder builder) {
        this.bearerToken = builder.bearerToken;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String bearerToken;

        public Builder bearerToken(String bearerToken) { this.bearerToken = bearerToken; return this; }

        public SignupResponse build() {
            return new SignupResponse(this);
        }
    }
}
