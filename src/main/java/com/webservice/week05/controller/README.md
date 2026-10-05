# Movie CRUD Web Service

영화 정보를 등록, 조회, 수정, 삭제할 수 있는 Spring Boot 기반 REST API 웹 서비스다.  
영화 데이터를 메모리 기반 Java Collection에 저장하며, 영화 장르를 기준으로 검색할 수 있는 추가 기능을 구현했다.

---

## 1. 프로젝트 소개

### 1-1. 주제와 관리하는 데이터

본 프로젝트는 **영화(Movie) 관리 서비스**다.

영화에 대한 다음 정보를 관리한다.

| 필드 | 설명 |
|---|---|
| id | 영화 식별자 |
| title | 영화 제목 |
| director | 감독 |
| genre | 장르 |
| releaseYear | 개봉 연도 |
| rating | 평점 |
| runningTime | 러닝타임(분) |

### 1-2. 프로젝트 구조

프로젝트는 Controller → Service → Repository 구조로 구성했다.

```text
src/main/java/com/webservice/week05
├── controller
│   └── MovieController.java
├── domain
│   └── Movie.java
├── dto
│   ├── MovieRequest.java
│   └── MovieResponse.java
├── repository
│   ├── MovieRepository.java
│   └── MemoryMovieRepository.java
├── service
│   └── MovieService.java
└── JavaWebCrud5Application.java
```

1-3. 로컬 실행 방법
프로젝트를 IntelliJ IDEA에서 실행할 수 있다.
1. 프로젝트를 IntelliJ IDEA에서 연다.
2. Gradle 의존성을 로드한다.
3. JavaWebCrud5Application.java를 실행한다.
4. Spring Boot 서버가 실행되면 다음 주소에서 API를 사용할 수 있다.
5. http://localhost:8080

1-4. API Endpoint
| 기능 | Method | Endpoint |
|---|---|---|
| 영화 등록 | POST | `/api/movies` |
| 전체 영화 조회 | GET | `/api/movies` |
| 특정 영화 조회 | GET | `/api/movies/{id}` |
| 영화 수정 | PUT | `/api/movies/{id}` |
| 영화 삭제 | DELETE | `/api/movies/{id}` |
| 장르별 검색 | GET | `/api/movies/search?genre={genre}` |

1-5. 요청 · 응답 JSON 예시
images 폴더 참고

2. 개발환경 및 Dependency
   2-1. 개발환경
   항목	사용 내용
   IDE	IntelliJ IDEA
   JDK	17
   Build Tool	Gradle
   데이터 저장	Java Collection (LinkedHashMap)
   API 테스트	Postman
   형상 관리	Git / GitHub


2-2. Dependency
프로젝트에서는 Spring Boot 기반 REST API 구현에 필요한 Dependency를 사용했다.
Spring Web
HTTP 요청을 처리하고 REST API Controller를 구현하기 위해 사용했다.

  Spring 기반 웹 애플리케이션을 구성하고 실행하기 위해 사용했다.
  Gradle
  프로젝트의 빌드와 Dependency 관리를 위해 사용했다.
  프로젝트에서 Dependency가 필요한 이유
  본 프로젝트는 영화 CRUD REST API를 구현하는 것이 목적이므로 HTTP 요청을 처리하기 위한 Spring Web이 필요하다.
  또한 프로젝트를 편리하게 빌드하고 실행하기 위해 Spring Boot와 Gradle을 사용했다.
  별도의 데이터베이스를 사용하지 않고 Java Collection을 이용해 메모리에 데이터를 저장하므로 데이터베이스 관련 Dependency는 사용하지 않았다.
3. Solution 분석
   3-1. 질문 1. Controller와 Service를 분리한 이유는 무엇인가?
   답변:
   Controller와 Service의 역할을 분리하기 위해서다.
   MovieController는 HTTP 요청과 응답을 담당하고, 실제 영화 등록이나 수정 등의 비즈니스 로직은 MovieService가 담당한다.
   역할을 분리하면 Controller가 복잡해지는 것을 방지하고 각 계층의 역할을 명확하게 할 수 있다.

  3-2. 질문 2. MovieRepository 인터페이스를 사용하는 이유는 무엇인가?
  답변:
  데이터를 저장하는 방식과 Service의 비즈니스 로직을 분리하기 위해서다.
  MovieRepository에서는 영화 데이터를 저장하고 조회하기 위한 메서드의 형태만 정의한다.
  관련 메서드는 다음과 같다.
- save()
- findAll()
- findById()
- update()
- deleteById()
- findByGenre()
  실제 구현은 MemoryMovieRepository에서 담당한다.
  따라서 이후 데이터 저장 방식을 변경하더라도 Service 계층의 변경을 최소화할 수 있다.
  3-3. 질문 3. 영화 ID를 서버에서 자동 생성한 이유는 무엇인가?
  답변:
  영화 등록 시 클라이언트가 ID를 직접 입력하지 않도록 하기 위해서다.
  MemoryMovieRepository에서는 다음과 같이 sequence 값을 증가시켜 ID를 생성한다.
  관련 필드와 메서드는 다음과 같다.
- MemoryMovieRepository.sequence
- MemoryMovieRepository.save()
  영화가 새로 등록될 때마다 sequence를 증가시키고 해당 값을 영화의 ID로 사용한다.
  따라서 클라이언트는 영화 정보만 입력하면 되고 ID는 서버가 관리한다.
- 
  3-4. 질문 4. 존재하지 않는 영화 ID에 대해 404를 반환하도록 구현한 이유는 무엇인가?
  답변:
  클라이언트가 요청한 리소스가 존재하지 않는다는 것을 HTTP 상태 코드로 명확하게 나타내기 위해서다.
  MovieService의 findMovie() 메서드에서 Repository를 통해 영화를 검색하고, 영화가 존재하지 않으면 ResponseStatusException을 발생시킨다.
  관련 메서드는 다음과 같다.
- MovieService.findMovie()
- MovieService.findById()
- MovieService.update()
- MovieService.delete()
  이 방법을 통해 조회뿐만 아니라 수정과 삭제에서도 존재하지 않는 ID를 404로 처리할 수 있다.

  3-5. 질문 5. 장르 검색 기능은 어떻게 구현했는가?
  답변:
  영화의 장르를 기준으로 영화 목록을 필터링하는 기능을 추가했다.
  Controller에서는 다음 Endpoint를 사용한다.
  GET /api/movies/search?genre=SF

관련 클래스와 메서드는 다음과 같다.
- MovieController.findByGenre()
- MovieService.findByGenre()
- MovieRepository.findByGenre()
- MemoryMovieRepository.findByGenre()
  MemoryMovieRepository에서는 store에 저장된 영화들을 Stream으로 처리하고 filter()를 사용해 요청한 장르와 일치하는 영화만 반환한다.

4. 개발 과정 요약
   프로젝트를 다음 5단계로 나누어 개발했다.
  STEP 1. 프로젝트 구조 및 Movie Domain 구현
  STEP 2. Repository 계층 구현
  STEP 3. Service 및 Controller 구현
  STEP 4. 입력값 검증 및 추가 기능 구현
  STEP 5. 테스트 및 배포 준비

5. 기능 수정 · 확장
   5-1. 장르 검색 기능
   기능을 추가한 이유
   기본 CRUD 기능만으로는 저장된 모든 영화 중 원하는 영화를 빠르게 찾기 어렵다.
   따라서 영화의 장르를 기준으로 검색할 수 있는 기능을 추가했다.

6. 배포 과정 요약
   본 프로젝트는 수업에서 학습한 배포 방법을 이용하여 배포한다.
   배포 과정은 다음과 같이 진행한다.
1. 프로젝트를 Gradle로 빌드한다.
2. Spring Boot 애플리케이션을 배포 환경에 맞게 설정한다.
3. 배포 환경에서 프로젝트를 실행한다.
4. 배포된 URL을 통해 API가 정상적으로 동작하는지 확인한다.
5. GET 및 POST 요청을 통해 배포된 서버의 응답을 확인한다.
6. 추가 기능인 장르 검색 API도 배포 URL에서 테스트한다.
   배포 중 발생한 문제와 해결 방법
   프로젝트를 개발하면서 로컬 환경에서 먼저 API를 구현하고 Postman을 이용해 기능을 테스트했다.
   특히 메모리 기반으로 데이터를 저장하기 때문에 서버가 재시작되면 기존 데이터가 사라질 수 있다는 점을 확인했다.
   따라서 배포 환경에서도 서버가 재시작될 경우 기존에 등록된 영화 데이터가 유지되지 않을 수 있다는 점을 확인하고 README에 해당 사항을 기록했다.
   배포 URL 테스트
   배포 URL:
   [실제 배포 URL 입력]

GET 요청:
[배포 URL]/api/movies

POST 요청:
[배포 URL]/api/movies

장르 검색:
[배포 URL]/api/movies/search?genre=SF

실제 배포 후 Postman을 통해 요청을 보내고 응답 결과를 확인한다.

7. Weekly Report
   Key Learning
1. REST API의 기본 구조를 이해했다.
   Controller, Service, Repository로 계층을 나누어 REST API를 구현하면서 각 계층이 어떤 역할을 하는지 이해했다.
2. DTO의 역할을 이해했다.
   MovieRequest와 MovieResponse를 사용하여 클라이언트의 요청 데이터와 서버의 응답 데이터를 분리하는 방법을 학습했다.

   Problem & Solution
   문제
   영화 데이터를 조회, 수정, 삭제할 때 존재하지 않는 ID가 입력되면 적절한 오류 처리가 필요했다.
   해결 과정
   MovieService에 findMovie() 메서드를 만들고 Repository에서 영화를 조회했다.