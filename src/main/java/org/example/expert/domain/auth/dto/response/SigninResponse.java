package org.example.expert.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class SigninResponse {

    private final String bearerToken;

    private SigninResponse(Builder builder) {
        this.bearerToken = builder.bearerToken;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String bearerToken;

        public Builder bearerToken(String bearerToken) { this.bearerToken = bearerToken; return this; }

        public SigninResponse build() {
            return new SigninResponse(this);
        }
    }
}
