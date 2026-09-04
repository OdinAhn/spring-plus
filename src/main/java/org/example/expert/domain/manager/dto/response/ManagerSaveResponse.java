package org.example.expert.domain.manager.dto.response;

import lombok.Getter;
import org.example.expert.domain.user.dto.response.UserResponse;

@Getter
public class ManagerSaveResponse {

    private final Long id;
    private final UserResponse user;

    private ManagerSaveResponse(Builder builder) {
        this.id = builder.id;
        this.user = builder.user;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private UserResponse user;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder user(UserResponse user) { this.user = user; return this; }

        public ManagerSaveResponse build() {
            return new ManagerSaveResponse(this);
        }
    }
}
