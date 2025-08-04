## API 명세

### 1. 일정(Schedule) API

#### 1.1 일정 생성
| 항목 | 내용 |
|------|------|
| **URL** | `POST /schedules` |
| **Request Body** | `title` (String, 필수, 최대 30자)<br>`content` (String, 필수, 최대 200자)<br>`author` (String, 필수)<br>`password` (String, 필수) |
| **Response** | `200 OK` |
| **Response Body** | `id`, `title`, `content`, `author`, `createdAt`, `modifiedAt` |
| **Error** | `400 Bad Request` - 필수값 누락, 글자수 초과 |

#### 1.2 일정 목록 조회
| 항목 | 내용 |
|------|------|
| **URL** | `GET /schedules` |
| **Query Parameter** | `author` (String, 선택) - 작성자명으로 필터링 |
| **Response** | `200 OK` |
| **Response Body** | 일정 목록 배열 (`id`, `title`, `content`, `author`, `createdAt`, `modifiedAt`) |

#### 1.3 일정 단건 조회
| 항목 | 내용 |
|------|------|
| **URL** | `GET /schedules/{scheduleId}` |
| **Path Variable** | `scheduleId` (Long, 필수) |
| **Response** | `200 OK` |
| **Response Body** | 일정 정보 + `comments` 배열 |

#### 1.4 일정 수정
| 항목 | 내용 |
|------|------|
| **URL** | `PUT /schedules/{scheduleId}` |
| **Path Variable** | `scheduleId` (Long, 필수) |
| **Request Body** | `title` (String, 필수)<br>`content` (String, 필수)<br>`author` (String, 필수)<br>`password` (String, 필수) |
| **Response** | `200 OK` |
| **Response Body** | 수정된 일정 정보 |
| **Error** | `400 Bad Request` - 비밀번호 불일치 |
| **비고** | 제목과 작성자만 수정 가능 |

#### 1.5 일정 삭제
| 항목 | 내용 |
|------|------|
| **URL** | `DELETE /schedules/{scheduleId}` |
| **Path Variable** | `scheduleId` (Long, 필수) |
| **Query Parameter** | `password` (String, 필수) |
| **Response** | `200 OK` |
| **Error** | `400 Bad Request` - 비밀번호 불일치 |

<br>

### 2. 댓글(Comment) API

#### 2.1 댓글 생성
| 항목 | 내용 |
|------|------|
| **URL** | `POST /schedules/{scheduleId}/comments` |
| **Path Variable** | `scheduleId` (Long, 필수) |
| **Request Body** | `content` (String, 필수, 최대 100자)<br>`author` (String, 필수)<br>`password` (String, 필수) |
| **Response** | `200 OK` |
| **Response Body** | `id`, `content`, `author`, `scheduleId`, `createdAt`, `updatedAt` |
| **Error** | `400 Bad Request` - 필수값 누락, 글자수 초과, 댓글 10개 초과 |
| **제약사항** | 하나의 일정당 최대 10개 댓글 |

<br>

## ERD (Entity Relationship Diagram)

![erd](./docs/erd.png)
