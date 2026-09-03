package org.example.expert.domain.manager.service;

import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.log.entity.Log;
import org.example.expert.domain.log.repository.LogRepository;
import org.example.expert.domain.manager.dto.request.ManagerSaveRequest;
import org.example.expert.domain.manager.repository.ManagerRepository;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ManagerServiceIntegrationTest {

    @Autowired
    private ManagerService managerService;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private LogRepository logRepository;

    @AfterEach
    void tearDown() {
        // 테스트 간 격리를 위해 역순으로 데이터 정리
        logRepository.deleteAll();
        managerRepository.deleteAll();
        todoRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("매니저 등록 실패 시 - 매니저는 롤백되지만 로그는 DB에 저장된다")
    void saveManager_fail_but_log_saved() {
        // given
        User author = userRepository.save(new User("author@test.com", "password", "author", UserRole.USER));
        Todo todo = todoRepository.save(new Todo("테스트 일정", "내용", "맑음", author));

        // 예외 유발 조건: 작성자 본인을 담당자로 등록 시도 (InvalidRequestException 발생)
        AuthUser authUser = new AuthUser(author.getId(), author.getEmail(), author.getNickname(), UserRole.USER);
        ManagerSaveRequest request = new ManagerSaveRequest(author.getId());

        // Todo 생성 시 작성자 본인이 매니저로 함께 등록되므로(Todo 생성자 참고), 등록 시도 전 개수를 기준으로 비교한다
        long managerCountBeforeAttempt = managerRepository.count();

        // when & then
        // 1. 매니저 등록 요청 시 예외가 발생함을 확인
        assertThatThrownBy(() -> managerService.saveManager(authUser, todo.getId(), request))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("일정 작성자는 본인을 담당자로 등록할 수 없습니다.");

        // 2. 매니저가 새로 등록되지 않았는지 확인 (롤백 검증)
        assertThat(managerRepository.count()).isEqualTo(managerCountBeforeAttempt);

        // 3. 로그 테이블에는 독립 트랜잭션(REQUIRES_NEW)으로 커밋되어 남아있는지 확인
        List<Log> logs = logRepository.findAll();
        assertThat(logs).hasSize(1);

        Log log = logs.get(0);
        assertThat(log.getAction()).isEqualTo("MANAGER_REGISTRATION");
        assertThat(log.getStatus()).isEqualTo("FAILED");
        assertThat(log.getRequesterUserId()).isEqualTo(author.getId());
        assertThat(log.getTargetUserId()).isEqualTo(author.getId());
        assertThat(log.getTodoId()).isEqualTo(todo.getId());
        assertThat(log.getMessage()).contains("일정 작성자는 본인을 담당자로 등록할 수 없습니다.");
        assertThat(log.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("존재하지 않는 유저로 매니저 등록 실패 시 - 실패 로그가 정상 기록된다")
    void saveManager_userNotFound_fail_but_log_saved() {
        // given
        User author = userRepository.save(new User("author@test.com", "password", "author", UserRole.USER));
        Todo todo = todoRepository.save(new Todo("테스트 일정", "내용", "맑음", author));

        AuthUser authUser = new AuthUser(author.getId(), author.getEmail(), author.getNickname(), UserRole.USER);
        long nonExistentUserId = 999999L;
        ManagerSaveRequest request = new ManagerSaveRequest(nonExistentUserId);

        // Todo 생성 시 작성자 본인이 매니저로 함께 등록되므로(Todo 생성자 참고), 등록 시도 전 개수를 기준으로 비교한다
        long managerCountBeforeAttempt = managerRepository.count();

        // when & then
        assertThatThrownBy(() -> managerService.saveManager(authUser, todo.getId(), request))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("등록하려고 하는 담당자 유저가 존재하지 않습니다.");

        // 롤백 확인 (매니저가 새로 등록되지 않았는지)
        assertThat(managerRepository.count()).isEqualTo(managerCountBeforeAttempt);

        // 로그 저장 확인
        List<Log> logs = logRepository.findAll();
        assertThat(logs).hasSize(1);
        assertThat(logs.get(0).getStatus()).isEqualTo("FAILED");
        assertThat(logs.get(0).getTargetUserId()).isEqualTo(nonExistentUserId);
        assertThat(logs.get(0).getMessage()).contains("등록하려고 하는 담당자 유저가 존재하지 않습니다.");
    }
}