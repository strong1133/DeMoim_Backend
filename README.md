

## DeMoim 서비스 소개

- 협업경험이 필요한 취준생들을 위한 팀원 모집 플랫폼
- 그외에도 자신이 작업한 프로젝트에 대한 정보를 공유
- 지원과 지원취소 기능을 통해 간편하게 지원가능

<br>

## DeMoim Frontend 
https://github.com/holasim91/demoim_fe

<br>

## 개요

- 프로젝트 명 :: DeMiom

- 프로젝트 소개 :: 프로젝트 팀원을 모집할 수 있는 웹 플랫폼.

- 개발 인원 :: 백엔드 3명, 프론트엔드 3명, 디자이너 1명

- 개발 언어 ::  Java 8 , React

- 개발 기간 :: 2021.04.23 ~2021.05.19

- 운영 기간 :: 2021.05.19 ~ (사용자의 피드백을 받아 서비스 개선)

- 담당 업무  
  * 이은지
    + 구현 기능 별 테이블 설계
    +  Coolsms를 이용한 문자인증 
    +  SmallTalk 및 프로젝트 자랑하기(게시판) 기능 구현
    +  관련 테스트 코드 작성 
  * 김준엽
    + 구현 기능별 테이블 설계
    + 팀 메이킹, 지원 ,마이페이지 Api 구현
    + 배포
  * 정석진
    + 구현  테이블 설계
    + 회원가입 및 로그인,알림,마이페이지,지원,댓글 api구현
    + 배포

- 개발 환경 :: Springboot 2.4.5, jdk 1.8 , Spring data JPA , Spring security, Junit4

- 배포환경 :: Gradle, AWS S3, AWS EC2

- 데이터 베이스 :: Mysql(AWS RDS)

- 형성 관리 툴 :: git

- 일정 관리 툴 :: Notion,Slack

- 주요 기능 
  * 마이페이지 
  * 프로젝트 지원 및 지원 취소
  * smalltalk,프로젝트 자랑하기 게시글 및 댓글 CRUD
  * 서비스 데이터 자동 최신화(프로젝트 모집글)

<br>

## 테이블 설계 

![image](https://user-images.githubusercontent.com/78028746/119464301-8da69b80-bd7d-11eb-9f0e-b94edf8f95c2.png)


<br>

## 기술소개

### Quill 에디터를 사용해 s3에 이미지 업로드 

* Quill-Editor은 가볍고 커스터마이징하기에 용이
* 에디터에 이미지를 첨부하는 경우 s3 업로드 api에 요청을 보내 s3_url을 리턴받아 <mg src=""/>형태로 만들어줌.
* ![image](https://user-images.githubusercontent.com/78028746/119853471-31917200-bf4b-11eb-9536-e670e32a1cb1.png)

<br>
<br>

### CoolSms 발송 api를 통한 인증 번호 조회
* 알리고, NHN Cloud 서비스 후 coolSms 선택 
* 선택 사유 : 많이 사용하는 서비스이다보니 관련 레퍼런스가 많아 트러블 슈팅이 용이, 토스& 한국투자 증권등의 기업에서도 사용할정도의 안전성
![image](https://user-images.githubusercontent.com/78028746/120095121-14cd8800-c15f-11eb-8e3b-f8c71a55099f.png)

### @Entity Graph를 이용해 n+1문제 해결
* join fetch를 통해 n+1 문제를 해결하고자했지만 n+1문제해결 시도.
* paging되어있는 경우 join fetch이 적용되지않는 다는 문제를 발견 -> @Entity Graph를 통해 n+1 문제 해결
![image](https://user-images.githubusercontent.com/78028746/120094693-e5b61700-c15c-11eb-8e3e-3ba2ec694117.png)




## After 프로젝트

* 웹사이트 보안 강화
  * SSL 활용한 HTTPS 변경 -> 변경 완료 2021.05.27
* 실시간 알림 및 채팅 서비스
  * Redis Pub/Sub 활용한 WebSocket 통신 또는 외부 API(채널톡) 도입 검토
* 소셜로그인
  * 카카오, 네이버 소셜로그인(OAuth2)\
