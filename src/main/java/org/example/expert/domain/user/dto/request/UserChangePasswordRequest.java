package org.example.expert.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserChangePasswordRequest {

    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String oldPassword;
        private String newPassword;

        public Builder oldPassword(String oldPassword) { this.oldPassword = oldPassword; return this; }
        public Builder newPassword(String newPassword) { this.newPassword = newPassword; return this; }

        public UserChangePasswordRequest build() {
            return new UserChangePasswordRequest(oldPassword, newPassword);
        }
    }
}
