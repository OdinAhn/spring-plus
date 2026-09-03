package org.example.expert.domain.todo.service;

import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @Test
    @DisplayName("일정 검색 성공 - 페이징 및 조건 검증")
    void searchTodos_success() {
        // given
        String keyword = "회의";
        LocalDateTime startDate = LocalDateTime.of(2026, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2026, 1, 31, 23, 59);
        String nickname = "홍길동";
        int page = 1;
        int size = 10;

        List<TodoSearchResponse> list = List.of(
                new TodoSearchResponse("팀 주간 회의", 3L, 5L),
                new TodoSearchResponse("프로젝트 킥오프 회의", 2L, 0L)
        );
        Page<TodoSearchResponse> expectedPage = new PageImpl<>(list, PageRequest.of(0, 10), list.size());

        given(todoRepository.searchTodos(
                eq(keyword),
                eq(startDate),
                eq(endDate),
                eq(nickname),
                any(Pageable.class)
        )).willReturn(expectedPage);

        // when
        Page<TodoSearchResponse> result = todoService.searchTodos(keyword, startDate, endDate, nickname, page, size);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("팀 주간 회의");
        assertThat(result.getContent().get(0).getManagerCount()).isEqualTo(3L);
        assertThat(result.getContent().get(0).getCommentCount()).isEqualTo(5L);

        // 1-based page가 0-based pageable(PageRequest.of(0, 10))로 정상 전달되었는지 확인
        verify(todoRepository).searchTodos(
                eq(keyword),
                eq(startDate),
                eq(endDate),
                eq(nickname),
                eq(PageRequest.of(0, size))
        );
    }
}