package org.example.expert.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentSaveRequest {

    @NotBlank
    private String contents;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String contents;

        public Builder contents(String contents) { this.contents = contents; return this; }

        public CommentSaveRequest build() {
            return new CommentSaveRequest(contents);
        }
    }
}
