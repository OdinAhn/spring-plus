package org.example.expert.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleChangeRequest {

    private String role;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String role;

        public Builder role(String role) { this.role = role; return this; }

        public UserRoleChangeRequest build() {
            return new UserRoleChangeRequest(role);
        }
    }
}
