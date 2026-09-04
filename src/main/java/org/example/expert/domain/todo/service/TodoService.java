package org.example.expert.domain.todo.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;
    private final WeatherClient weatherClient;

    @Transactional
    public TodoSaveResponse saveTodo(AuthUser authUser, TodoSaveRequest todoSaveRequest) {
        User user = User.fromAuthUser(authUser);

        String weather = weatherClient.getTodayWeather();

        Todo newTodo = Todo.builder()
                .title(todoSaveRequest.getTitle())
                .contents(todoSaveRequest.getContents())
                .weather(weather)
                .user(user)
                .build();
        Todo savedTodo = todoRepository.save(newTodo);

        return TodoSaveResponse.builder()
                .id(savedTodo.getId())
                .title(savedTodo.getTitle())
                .contents(savedTodo.getContents())
                .weather(weather)
                .user(UserResponse.builder().id(user.getId()).email(user.getEmail()).build())
                .build();
    }

    public Page<TodoResponse> getTodos(int page, int size, String weather, LocalDateTime modifiedAtFrom, LocalDateTime modifiedAtTo) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Todo> todos = todoRepository.searchTodos(weather, modifiedAtFrom, modifiedAtTo, pageable);

        return todos.map(todo -> TodoResponse.builder()
                .id(todo.getId())
                .title(todo.getTitle())
                .contents(todo.getContents())
                .weather(todo.getWeather())
                .user(UserResponse.builder().id(todo.getUser().getId()).email(todo.getUser().getEmail()).build())
                .createdAt(todo.getCreatedAt())
                .modifiedAt(todo.getModifiedAt())
                .build());
    }

    public TodoResponse getTodo(long todoId) {
        Todo todo = todoRepository.findByIdWithUser(todoId)
                .orElseThrow(() -> new InvalidRequestException("Todo not found"));

        User user = todo.getUser();

        return TodoResponse.builder()
                .id(todo.getId())
                .title(todo.getTitle())
                .contents(todo.getContents())
                .weather(todo.getWeather())
                .user(UserResponse.builder().id(user.getId()).email(user.getEmail()).build())
                .createdAt(todo.getCreatedAt())
                .modifiedAt(todo.getModifiedAt())
                .build();
    }

    public Page<TodoSearchResponse> searchTodos(
            String keyword,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String nickname,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return todoRepository.searchTodos(keyword, startDate, endDate, nickname, pageable);
    }

}
