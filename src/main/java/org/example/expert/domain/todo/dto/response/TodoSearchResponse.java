package org.example.expert.domain.todo.dto.response;

import lombok.Getter;

@Getter
public class TodoSearchResponse {

    private final String title;
    private final Long managerCount;
    private final Long commentCount;

    private TodoSearchResponse(Builder builder) {
        this.title = builder.title;
        this.managerCount = builder.managerCount;
        this.commentCount = builder.commentCount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String title;
        private Long managerCount;
        private Long commentCount;

        public Builder title(String title) { this.title = title; return this; }
        public Builder managerCount(Long managerCount) { this.managerCount = managerCount; return this; }
        public Builder commentCount(Long commentCount) { this.commentCount = commentCount; return this; }

        public TodoSearchResponse build() {
            return new TodoSearchResponse(this);
        }
    }
}
