# DeMoim_Backend


 <h1 align="center" style="color:mediumpurple"> Demoim  </h1>


## ▶️ 유튜브 링크 
## 🄽 Notion 링크 


##🖥 프로젝트 소개
취업과 커리어준비 등 직업적 공백기에 있는 사람들에게 <br>
각자의 아이디어를 활용하여 팀 프로젝트를 만들어 진행해나갈 수 있는 소셜 커뮤니티 서비스입니다.

## 🔎 요약

예비개발자/디자이너/기획자 들이 직접 프로젝트 팀을 모집하고 지원 할 수 있는 팀빌딩 플랫폼

* 프로젝트 모집공고를 작성하고, 지원자를 선택하여 프로젝트 팀 구성
* 회원 간 프로필 열람을 통해 상호간 기본적인 정보 제공(포지션,연락처,간단한 소개)
* 본인이 뽐내고 싶은 프로젝트들을 공유할 수 있고, 회원 간 스몰톡을 나눌 수 있는 커뮤니티

##📆 기간
2021년 4월 23일 ~ 2021년 5월 28일

## 🗣 커뮤니케이션 툴
Github, Slack, Gather

## 🤠 팀원소개  ([Notion](https://www.notion.so/Demoim-87856b49c18545358ee657b434bff365))

총 6명
* 백엔드
  * 정석진(Spring, React)
  * 이은지(Spring)
  * 김준엽(Spring)
* 프론트엔드 ([FrontEnd Github](https://github.com/holasim91/demoim_fe))
  * 심현인(React)
  * 고정원(React)
* UX/UI 디자이너 ([Zeplin](https://app.zeplin.io/project/608a507a3f3d51355497c44c))
  * 김민경

## 🚀 기능

###[회원가입/로그인 페이지]MySQL
* 회원가입 및 로그인 기능 _ Spring Security, JWT Authentication, MySQL, Spring Data JPA
  * Cool SMS API(문자발송 API)

###[메인 페이지(팀메이킹, Detalk)]
* 팀메이킹(프로젝트 모집) _ MySQL, Spring Data JPA
  
  ```
  주최자의 경우
  Quill-Editor를 사용해서 작성자가 원하는 모집글을 쓸 수 있다.
  프론트엔드 / 백엔드 / 디자이너 / 기획자를 모집할 수 있고, 최대 인원은 총 10명이다.
  원하는 기간만큼 모집을 할 수 있으며, 지원자를 선택하여 팀원으로 뽑을 수 있다.
  주최자가 희망인원을 다 선택을 하거나 모집 기간이 지나면 모집이 마감된다
  
  참가자의 경우
  원하는 프로젝트를 지원 할 수 있다.
  ```
* Detalk
  * Exhibition(프로젝트 자랑하기) _ MySQL, Spring Data JPA
    ```
    Quill-Editor을 이용하여 사용자가 자신이 했던 프로젝트를 다른 회원들과 공유할 수 있다.
    댓글로 해당 프로젝트에 대한 피드백을 주고받을 수 있다.
    ```
  * Smalltalk(스몰톡 피드) _ MySQL, Spring Data JPA
    ```
    소소한 잡담및 정보를 나눌 수 있는 공간이다.
    댓글로 질문을 주고 받을 수 있다.
    ```
    
###[나의 로그]
* 로그인 회원의 프로필 관리(MySQL, Spring Data JPA, Storage - AWS S3)
* 나의 프로젝트 참가 이력과, 현재 참가 하고 있는 프로젝트, 내가 주최한 프로젝트 확인
* 자신이 참여중인 프로젝트의 다른 유저의 정보 확인
* 자신이 작성한 스몰톡과 프로젝트 자랑하기 피드


## 🦄 프로젝트 썸네일

<p align="center">
<img src=></img>
</p>


## 🔖 기술스택

####  백엔드 협업 툴
* Host Server: ubuntu18.04 - t2.micro(AWS EC2)
* File Storage: AWS S3 Bucket
* Database: MySQL 8.0.20(AWS RDS)

* Framework : SpringBoot 2.4.5
* Java: JDK 1.8.0
* IDE: IntelliJ IDEA 2021.1.1 x64
* Build Management: Gradle
* ORM: Spring Data JPA
* Sub-Framework: Spring Security, SpringBoot Websocket


* View Template Engine: React.js(프론트엔드)


### 👑 After 프로젝트

* 웹사이트 보안 강화
  * SSL 활용한 HTTPS 변경
* 실시간 알림 및 채팅 서비스
  * Redis Pub/Sub 활용한 WebSocket 통신 또는 외부 API(채널톡) 도입 검토
* 소셜로그인
  * 카카오, 네이버 소셜로그인(OAuth2)\
