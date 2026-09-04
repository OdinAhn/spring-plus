package org.example.expert.domain.todo.dto.response;

import lombok.Getter;
import org.example.expert.domain.user.dto.response.UserResponse;

import java.time.LocalDateTime;

@Getter
public class TodoResponse {

    private final Long id;
    private final String title;
    private final String contents;
    private final String weather;
    private final UserResponse user;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    private TodoResponse(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.contents = builder.contents;
        this.weather = builder.weather;
        this.user = builder.user;
        this.createdAt = builder.createdAt;
        this.modifiedAt = builder.modifiedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String title;
        private String contents;
        private String weather;
        private UserResponse user;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder contents(String contents) { this.contents = contents; return this; }
        public Builder weather(String weather) { this.weather = weather; return this; }
        public Builder user(UserResponse user) { this.user = user; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder modifiedAt(LocalDateTime modifiedAt) { this.modifiedAt = modifiedAt; return this; }

        public TodoResponse build() {
            return new TodoResponse(this);
        }
    }
}
