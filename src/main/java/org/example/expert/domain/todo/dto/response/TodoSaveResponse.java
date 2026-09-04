package org.example.expert.domain.todo.dto.response;

import lombok.Getter;
import org.example.expert.domain.user.dto.response.UserResponse;

@Getter
public class TodoSaveResponse {

    private final Long id;
    private final String title;
    private final String contents;
    private final String weather;
    private final UserResponse user;

    private TodoSaveResponse(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.contents = builder.contents;
        this.weather = builder.weather;
        this.user = builder.user;
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

        public Builder id(Long id) { this.id = id; return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder contents(String contents) { this.contents = contents; return this; }
        public Builder weather(String weather) { this.weather = weather; return this; }
        public Builder user(UserResponse user) { this.user = user; return this; }

        public TodoSaveResponse build() {
            return new TodoSaveResponse(this);
        }
    }
}
