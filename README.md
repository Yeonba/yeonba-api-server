<div align="center">
  <img width="690" alt="image" src="https://github.com/user-attachments/assets/3e69694c-b708-4393-b1b6-4c0e84ada91a">
</div>

## 기술 스택
### Backend
<img width="558" alt="image" src="https://github.com/user-attachments/assets/2f95cacf-72a1-492b-897a-6d07258745eb">

### Infra
<img width="566" alt="image" src="https://github.com/user-attachments/assets/4ed7b528-0bc7-4e3a-9e01-d3de89b3a94f">

## 인프라 구조
<img width="778" alt="image" src="https://github.com/user-attachments/assets/e02f9742-16c2-482d-9cd6-c41ff5638884">


## 👨‍💻 기술 스택 선택 이유
### Java 17
- 많이 사용되는 Java 8, 11에 비해 GC의 성능이 개선

- `record`, `toList()`, 텍스트 블록 등 다양한 부가 편의 기능 제공

- Java 8에 비견되는 안정적인 지원 기간

- Spring Boot 3.0부터 JDK 17이상을 지원, 다음 세대 플랫폼 호환 준비

### SpringBoot 3.2.1
- 프레임워크 지원기간 비교 시 추후 마이그레이션 비용을 고려하여 SpringBoot 3.2.1 선택

### Spring Data JPA
- SQL 작성 및 DB 연결, 매핑 등 반복적 작업 자동화

- ORM을 통해 객체지향적 코드 작성 가능, 유지보수가 용이한 형태로 설계 가능

- DB와 연관된 다양한 작업을 자동화, 개발자는 비즈니스 로직에 집중할 수 있음

### QueryDSL
- 복잡한 동적 쿼리 작성을 위해 도입

- 자바 코드로 쿼리를 생성, 컴파일 단계에서 오류 파악 가능

- 조인, 서브쿼리, 집계함수 등 복잡한 쿼리 작성에 필요한 다양한 기능 지원

### MySQL
- 가장 보편적으로 사용되며 무료로 사용 가능한 DB

- 팀원들이 가장 익숙한 DB

### Swagger
- 코드(어노테이션) 기반으로 간편히 API 명세서 생성 가능
  
- 깔끔하고 가독성 높은 UI 제공

- API 문서를 생성하는데 드는 공수를 덜고 본질적인 개발에 집중 가능

## 🌴Git & Branch convention
### Git 컨벤션
- PR merge : dev 브랜치 merge 시 squash and merge 사용
    - PR 단위로 커밋 히스토리에서 관리 가능

- PR 제목은 대괄호 안에 키워드, 추적하기 쉽게 작업 내용을 간단히 설명
    - ex) [소셜 로그인] 사용자는 카카오 또는 애플 인증으로 로그인할 수 있다.

### 브랜치 관리
- mono repository (fork 하지 않음)

### 브랜치 컨벤션
```
feat-#22-user-login
```
#### prefix
- feat : 새로운 기능 추가

- fix : 버그 수정

- refactor : 성능 개선 등의 리팩토링 수행, 코드 변경 x

- docs : 문서 작업(ex. API 명세)

- chore : 빌드 스크립트 수정 등의 개발외 작업

#### infix
- #01, 이슈 번호

#### postfix
- content, 작업 내용을 간단히 설명


## 📖 Code Convention
### 패키지 구조
- 최상위 도메인 -> 용도별 분리

- user
    - controller
    - service
    - repository

### 코드 컨벤션
- 어노테이션은 길이 순서대로 작성

- 필드에 어노테이션이 존재할 경우, 한 라인씩 여유를 두기

    - 어노테이션이 존재하지 않을 경우 붙여서 구성

- 스웨거 명세 어노테이션 속성, 한 라인에 하나씩 배치하기 않기, 코드가 더 번잡해지는 느낌

- 파라미터 같은 경우 가독성을 고려해 한 라인에 하나씩 배치하는 형태 허용

- API Response DTO 필드, Optional 아닌 경우 기본 타입으로 제공

- 메서드 선언 형식

```java
public ResponseEntity<CustomResponse<UserSimpleProfileResponse>> getSimpleProfile(
        @RequestAttribute("userId") long userId) {
 // 로직
}
```

- `return` 문 바로 윗 줄은 빈 줄로!

- 검증은 일괄적으로 `@Valid` 이용 수행

- 예외 정보는 메시지 형태로 프론트에 전달

- `boolean`을 반환하는 메서드들은 조건에 부합하지 않을 경우 `true`를 반환토록 작성

- 조건 판별 로직 작성시

    - 두 가지 이상 조건이면 별도 메서드 추출

    - 그 메서드 내에서도 `if`문 분리해 조건 작성

- `public` 메서드의 경우 비교적 추상화 되어있는 타 클래스의 메서드, `private` 메서드를 호출하기 때문에 메서드 중간에 `//`로 주석 작성

- `private` 메서드의 경우 그 자체적으로 어떤 기능에서 일부 역할만 수행, 메서드 위에 주석 기술

### DTO 네이밍룰
- prefix : 도메인(user, post)

- infix : 용도(Login, Join)

- postfix : Request, Response

## 🗃️ DB 설계
![Yeonba_ERD](https://github.com/user-attachments/assets/635b906e-75e9-4131-8320-03026ce1977c)

### DB convertion
- 테이블 이름 복수형(`users`)

- 테이블/칼럼 이름 소문자, 나머지 대문자 작성

- 이미지 지칭하는 단어, `photo`로 통일

### 시간 공통 칼럼
- 레코드 생성 일시 : `created_at`
  
- 레코드 최종 수정 일시 : `updated_at`


### 시간 데이터 형식
- DB에 있는 모든 데이터는 UTC
  
- WAS에서 응답할 때 KST로 변환하여 제공
