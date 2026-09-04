package org.example.expert.domain.user.repository;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * JDBC로 유저 100만 건을 배치 insert 하는 테스트.
 * 실행 시 DB에 실제로 100만 건이 쌓이므로 평소 테스트 실행 시에는 제외한다.
 * 필요할 때만 @Disabled를 지우고 개인 로컬 DB에서 실행할 것.
 */
@Disabled("대용량 데이터 생성용 - 필요할 때만 수동으로 실행")
class BulkUserInsertTest {

    private static final String URL = "jdbc:mysql://localhost:3306/spring_plus?rewriteBatchedStatements=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    private static final String NICKNAME_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String DUMMY_PASSWORD = "$2a$10$dummyPasswordHashForBulkTestData000000000000000000000";

    private static final int TOTAL_COUNT = 1_000_000;
    private static final int BATCH_SIZE = 1000;

    @Test
    void insertOneMillionUsers() throws Exception {
        String sql = "INSERT INTO users (email, password, nickname, user_role, created_at, modified_at) VALUES (?, ?, ?, ?, ?, ?)";
        Random random = new Random();
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                long start = System.currentTimeMillis();

                for (int i = 1; i <= TOTAL_COUNT; i++) {
                    ps.setString(1, "bulk_user" + i + "@test.com");
                    ps.setString(2, DUMMY_PASSWORD);
                    ps.setString(3, randomNickname(random, i));
                    ps.setString(4, "USER");
                    ps.setTimestamp(5, now);
                    ps.setTimestamp(6, now);
                    ps.addBatch();

                    if (i % BATCH_SIZE == 0) {
                        ps.executeBatch();
                        conn.commit();
                    }
                }
                ps.executeBatch();
                conn.commit();

                long elapsed = System.currentTimeMillis() - start;
                System.out.println("[BulkUserInsertTest] " + TOTAL_COUNT + "건 insert 완료, 소요 시간: " + elapsed + "ms");
            }
        }
    }

    // 랜덤 문자열 + 순번을 붙여서 중복 없이 생성
    private String randomNickname(Random random, int index) {
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            sb.append(NICKNAME_CHARS.charAt(random.nextInt(NICKNAME_CHARS.length())));
        }
        return sb.append('_').append(index).toString();
    }
}
