package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.user.entity.QUser.user;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class TodoRepositoryCustomImpl implements TodoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        Todo result = queryFactory
                .selectFrom(todo)
                .leftJoin(todo.user, user).fetchJoin()
                .where(todo.id.eq(todoId))
                .fetchOne();

        return Optional.ofNullable(result);
    }
    @Override
    public Page<TodoSearchResponse> searchTodos(
            String keyword,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String nickname,
            Pageable pageable
    ) {
        QManager subManager = new QManager("subManager");
        QComment subComment = new QComment("subComment");

        List<TodoSearchResponse> content = queryFactory
                .select(Projections.constructor(
                        TodoSearchResponse.class,
                        todo.title,
                        JPAExpressions.select(subManager.count())
                                .from(subManager)
                                .where(subManager.todo.id.eq(todo.id)),
                        JPAExpressions.select(subComment.count())
                                .from(subComment)
                                .where(subComment.todo.id.eq(todo.id))
                ))
                .from(todo)
                .where(
                        titleContains(keyword),
                        createdDateBetween(startDate, endDate),
                        managerNicknameContains(nickname)
                )
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(todo.count())
                .from(todo)
                .where(
                        titleContains(keyword),
                        createdDateBetween(startDate, endDate),
                        managerNicknameContains(nickname)
                )
                .fetchOne();

        long totalCount = total != null ? total : 0L;

        return new PageImpl<>(content, pageable, totalCount);
    }

    private BooleanExpression titleContains(String keyword) {
        return StringUtils.hasText(keyword) ? todo.title.containsIgnoreCase(keyword) : null;
    }

    private BooleanExpression createdDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null) {
            return todo.createdAt.between(startDate, endDate);
        } else if (startDate != null) {
            return todo.createdAt.goe(startDate);
        } else if (endDate != null) {
            return todo.createdAt.loe(endDate);
        }
        return null;
    }

    private BooleanExpression managerNicknameContains(String nickname) {
        if (!StringUtils.hasText(nickname)) {
            return null;
        }
        QManager subManager = new QManager("subManagerNick");
        return JPAExpressions.selectOne()
                .from(subManager)
                .where(
                        subManager.todo.id.eq(todo.id),
                        subManager.user.nickname.containsIgnoreCase(nickname)
                )
                .exists();
    }
}
