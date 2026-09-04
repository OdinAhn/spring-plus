package org.example.expert.domain.user.repository;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 닉네임 검색 속도를 측정하기 위한 테스트.
 * BulkUserInsertTest로 100만 건을 미리 넣어둔 상태에서 실행해야 의미가 있다.
 * 인덱스 적용 전/후 속도를 비교할 때 필요한 메서드만 골라서 수동으로 실행할 것.
 */
@Disabled("성능 측정용 - 필요할 때만 수동으로 실행")
class UserSearchPerformanceTest {

    private static final String URL = "jdbc:mysql://localhost:3306/spring_plus?rewriteBatchedStatements=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    private static final String TARGET_NICKNAME = "benchmark_target_user";
    private static final int TRY_COUNT = 5;

    @Test
    void insertBenchmarkTarget() throws Exception {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO users (email, password, nickname, user_role, created_at, modified_at) " +
                             "VALUES ('benchmark_target@test.com', 'dummy', ?, 'USER', NOW(), NOW())")) {
            ps.setString(1, TARGET_NICKNAME);
            ps.executeUpdate();
        }
    }

    @Test
    void measureSearchByNickname() throws Exception {
        String sql = "SELECT * FROM users WHERE nickname = ?";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            for (int i = 1; i <= TRY_COUNT; i++) {
                long start = System.currentTimeMillis();
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, TARGET_NICKNAME);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            rs.getLong("id");
                        }
                    }
                }
                long elapsed = System.currentTimeMillis() - start;
                System.out.println("[측정 " + i + "회차] " + elapsed + "ms");
            }
        }
    }

    @Test
    void createNicknameIndex() throws Exception {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE INDEX idx_user_nickname ON users (nickname)");
        }
    }
}
