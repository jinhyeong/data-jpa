<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
"**Contents**"

- [실전! 스프링 데이터 JPA 대시보드 - 인프런 | 강의](#%EC%8B%A4%EC%A0%84-%EC%8A%A4%ED%94%84%EB%A7%81-%EB%8D%B0%EC%9D%B4%ED%84%B0-jpa-%EB%8C%80%EC%8B%9C%EB%B3%B4%EB%93%9C---%EC%9D%B8%ED%94%84%EB%9F%B0--%EA%B0%95%EC%9D%98)
    - [의존성 확인](#%EC%9D%98%EC%A1%B4%EC%84%B1-%ED%99%95%EC%9D%B8)
    - [h2 설정](#h2-%EC%84%A4%EC%A0%95)
    - [쿼리 메소드 필터 조건](#%EC%BF%BC%EB%A6%AC-%EB%A9%94%EC%86%8C%EB%93%9C-%ED%95%84%ED%84%B0-%EC%A1%B0%EA%B1%B4)
    - [Supported Query Return Types](#supported-query-return-types)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# [실전! 스프링 데이터 JPA 대시보드 - 인프런 | 강의](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%EB%8D%B0%EC%9D%B4%ED%84%B0-JPA-%EC%8B%A4%EC%A0%84/dashboard)

```bach
./gradlew dependencies --configuration compileClasspath
```

## 의존성 확인
[Spring Boot Reference Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/index.html)  
[Dependency Versions](https://docs.spring.io/spring-boot/docs/current/reference/html/dependency-versions.html#appendix.dependency-versions)

## h2 설정
1. `jdbc:h2:~/datajpa`
2. `jdbc:h2:tcp://localhost/~/datajpa`

## 쿼리 메소드 필터 조건
[스프링 데이터 JPA 공식 문서](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation)

## Supported Query Return Types
[Supported Query Return Types](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repository-query-return-types)
