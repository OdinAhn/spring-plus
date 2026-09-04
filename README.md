# SPRING PLUS

## 13. 대용량 데이터 처리

### 유저 100만 건 생성

JDBC로 배치 insert 해서 유저 100만 건을 생성했다. (`src/test/java/.../domain/user/repository/BulkUserInsertTest.java`, 평소엔 `@Disabled` 처리해두고 필요할 때만 켜서 실행)

- 닉네임은 랜덤 문자열 8자리 뒤에 순번(index)을 붙여서 만들었다. 순번이 겹치지 않으니 닉네임도 자연히 겹치지 않는다.
- application.yml의 datasource url에 `rewriteBatchedStatements=true`를 추가했다. 이거 없으면 `addBatch()`를 써도 실제로는 쿼리가 한 건씩 나간다.
- 로컬에서 실행해보니 100만 건 insert에 23.8초 걸렸다.

### 닉네임 검색 API

`GET /users/search?nickname=xxx`

- `UserRepository.findByNickname()` 추가 (정확히 일치하는 경우만 조회됨)

### 조회 속도 개선

`SELECT * FROM users WHERE nickname = ?` 쿼리를 5회씩 반복 실행해서 측정했다.

| 방법 | 평균 응답 시간 | EXPLAIN |
| --- | --- | --- |
| 인덱스 없음 | 약 448ms | type=ALL, rows=990319 (풀스캔) |
| nickname 컬럼에 인덱스 추가 | 약 4ms (첫 조회 17ms, 이후 0~2ms) | type=ref, rows=1 |

측정한 원본 값(ms)

- 인덱스 없음: 435, 413, 442, 488, 464
- 인덱스 추가 후: 17, 2, 0, 1, 1

인덱스를 하나 추가한 것만으로 100배 이상 차이가 났다. `User` 엔티티에 `@Table(indexes = @Index(name = "idx_user_nickname", columnList = "nickname"))`로 추가해서 앱을 실행하면(ddl-auto: update) 자동으로 생성되도록 해뒀다.
