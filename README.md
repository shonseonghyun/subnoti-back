# ⏰ 플랩 노티
```
  플랩풋볼이라는 개인 참여형 매칭 서비스의 매칭 무료 참가(슈퍼 서브,매니저 프리) 정책 활성화 시
  누구보다 빠르게 알려줄 수 있는 알림 서비스
```

<br/>

# 개발 기간 / 개발 인원
2024.12 ~ 현재 / 1 명

<br/>

# 패키지 구조
    ├─main
    │  ├─java
    │  │  └─com
    │  │      └─example
    │  │          └─library
    │  │              ├─annotation
    │  │              ├─aspects
    │  │              ├─config
    │  │              │  └─batch
    │  │              │      └─custom
    │  │              │          ├─dto
    │  │              │          └─reader
    │  │              │              └─dto
    │  │              ├─domain
    │  │              │  ├─book
    │  │              │  │  ├─api
    │  │              │  │  ├─application
    │  │              │  │  │  ├─dto
    │  │              │  │  │  └─Impl
    │  │              │  │  ├─domain
    │  │              │  │  │  ├─converter
    │  │              │  │  │  ├─dto
    │  │              │  │  │  └─repository
    │  │              │  │  ├─enums
    │  │              │  │  └─infrastructure
    │  │              │  ├─heart
    │  │              │  │  ├─api
    │  │              │  │  ├─application
    │  │              │  │  │  ├─dto
    │  │              │  │  │  └─event
    │  │              │  │  ├─domain
    │  │              │  │  │  ├─dto
    │  │              │  │  │  └─repository
    │  │              │  │  └─infrastructure
    │  │              │  │      └─repository
    │  │              │  ├─rent
    │  │              │  │  ├─api
    │  │              │  │  ├─application
    │  │              │  │  │  └─event
    │  │              │  │  ├─domain
    │  │              │  │  │  └─dto
    │  │              │  │  ├─enums
    │  │              │  │  └─infrastructure
    │  │              │  │      ├─entity
    │  │              │  │      │  └─converter
    │  │              │  │      └─repository
    │  │              │  ├─review
    │  │              │  │  ├─api
    │  │              │  │  ├─application
    │  │              │  │  │  ├─dto
    │  │              │  │  │  └─Impl
    │  │              │  │  ├─domain
    │  │              │  │  │  ├─dto
    │  │              │  │  │  └─repository
    │  │              │  │  └─infrastructure
    │  │              │  │      └─repository
    │  │              │  └─user
    │  │              │      ├─api
    │  │              │      ├─application
    │  │              │      │  ├─dto
    │  │              │      │  ├─event
    │  │              │      │  └─Impl
    │  │              │      ├─domain
    │  │              │      │  ├─converter
    │  │              │      │  └─repository
    │  │              │      ├─enums
    │  │              │      └─infrastructure
    │  │              │          └─repository
    │  │              ├─exception
    │  │              │  └─exceptions
    │  │              ├─global
    │  │              │  ├─entityListener
    │  │              │  │  └─Entity
    │  │              │  ├─event
    │  │              │  ├─file
    │  │              │  ├─mail
    │  │              │  │  └─domain
    │  │              │  │      └─mail
    │  │              │  │          ├─application
    │  │              │  │          │  ├─dto
    │  │              │  │          │  └─event
    │  │              │  │          ├─domain
    │  │              │  │          │  ├─mailForm
    │  │              │  │          │  └─mailHistory
    │  │              │  │          │      └─converter
    │  │              │  │          ├─enums
    │  │              │  │          └─infrastructure
    │  │              │  │              ├─mailForm
    │  │              │  │              └─mailHistory
    │  │              │  ├─response
    │  │              │  ├─security
    │  │              │  │  ├─handler
    │  │              │  │  ├─login
    │  │              │  │  │  └─filter
    │  │              │  │  └─oauth2
    │  │              │  │      ├─handler
    │  │              │  │      ├─principal
    │  │              │  │      └─userInfo
    │  │              │  │          └─OAuthUserInfo
    │  │              │  │              └─userInfos
    │  │              │  └─utils
    │  └─resources
    │      ├─properties
    │      └─static
    │          └─docs
    └─test
    
# ⚙개발 환경 및 사용 기술
- Java 17
  
- IDE : IntelliJ
  
- Springboot(3.x)
  
- mySql
  
- AWS EC2
  
- Docker
  * 하나의 AWS EC2 서버에서 컨테이너를 띄워 웹 배포, Jenkins 컨테이너를 띄워 자동배포 구현

- Jenkins
  * sunghyun-develop 브랜치 푸쉬 시 자동배포 구현
    
# 주요 기능
## 스프링 시큐리티
 - JWt 토큰 통한 일반 로그인, 소셜로그인(네이버,구글) 구현
 - 권한에 따른 자원에 대한 접근 제어


 ## 스프링 스케줄러 및 배치
  - 대여 히스토리 내 연체 대상인 히스토리 연체 등록 , 연체 유저 등록, 연체 유저 등록 해제, 이메일 미발송 알림 재발송 기능 구현에 이용
  - 조건에 맞는 데이터를 읽어들여 데이터 가공처리가 필요했기에 tasklet 대신 chunk 방식 사용
  - read 시 querydsl을 활용하여 조회 위해 QuerydslItemReader 개발 및 NoOffset, ZeroOffset 커스텀Reader 통한 조회 성능 개선


## 에러 공통 처리 및 API 공통 응답 처리 
- 에러 코드 ENUM으로 관리 및 API 공통응답 처리


## Merge커스텀 어노테이션 개발
- 회원정보 수정 요청 시 @Merge어노테이션 존재하는 필드만 업데이트, 그 외 필드는 데이터가 인입되어도 업데이트 제외


## Repository 패턴 적용
- 도메인 모델과 구현 기술을 분리
- 패턴 적용 시 내가 생각한 장단점은 아래와 같았다.
    * 장점
        1. 도메인 모델에서 영속 관련 코드가 제거되어 비즈니스 로직에 집중
        2. 도메인 관점에서 바라보면 일관된 인터페이스를 통하여 데이터 요청
   * 단점
        1. 엔티티와 도메인을 분리했을 때 많은 컨버팅 코드 존재
        2. LazyLoading 사용 불가 -> 직접 참조가 아닌 간접 참조로 해결은 함.. 


# 개선 사항
- Valiadtion 처리
- 일자 별로 서버 내 로그 파일 관리
- 개발/운영 환경설정 파일 분리
- 브랜치 전략 사용
- TDD + RestDocs로 API 문서화
- 회의록 작성
- 관리자 도메인 추가 개발
- 에러 응답 시에도 HTTP 200으로 무조건 내려가며 바디에 에러 상세 코드 및 메시지 담겨져서 내려간다. 각 케이스에 맞는 HTTP 상태코드 내릴 수 있도록 개선 


# 프로젝트를 통한 배운 경험 및 소감
프론트와 배포까지 모두 구현을 목표로 두었기에 공부를 병행하며 진행하였기에 시간도 오래 걸리고 코드의 질이 높진 않다ㅠㅠ그러나 배운 것은 정말 많았다. DB설계의 중요성부터 패키지 구조 잡기 등등... 
특히 요새 MSA,DDD 대두되고 있기에 간단하게라도 맛보고자 해당 내용을 접목시켜서 프로젝트를 진행하였는데 하면서도 이게 맞는건지, 궁금한 점도 정말로 많고 다음 개인 프로젝트에서도 더 공부하고 파볼 생각이다.
첫 배포를 진행하면서도 환경 설정 파일 분리, 로그파일 관리 라든지 기본적인 것도 생각하지 못한 채 프로젝트를 시작하는 실수는 더 이상 하지 않을 수 있게 되어 기쁘다.   
이후 프로젝트에선 개선 사항에 정리된 내용, 내가 구현하였던 내용을 까먹지 않고 적용시키고 개선시켜야겠다!! 
