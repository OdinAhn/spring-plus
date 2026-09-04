package org.example.expert.domain.manager.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ManagerSaveRequest {

    @NotNull
    private Long managerUserId; // 일정 작상자가 배치하는 유저 id

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long managerUserId;

        public Builder managerUserId(Long managerUserId) { this.managerUserId = managerUserId; return this; }

        public ManagerSaveRequest build() {
            return new ManagerSaveRequest(managerUserId);
        }
    }
}
