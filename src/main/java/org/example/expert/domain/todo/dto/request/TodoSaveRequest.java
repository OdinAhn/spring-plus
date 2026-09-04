package org.example.expert.domain.todo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoSaveRequest {

    @NotBlank
    private String title;
    @NotBlank
    private String contents;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String title;
        private String contents;

        public Builder title(String title) { this.title = title; return this; }
        public Builder contents(String contents) { this.contents = contents; return this; }

        public TodoSaveRequest build() {
            return new TodoSaveRequest(title, contents);
        }
    }
}
