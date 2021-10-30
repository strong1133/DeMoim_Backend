# DeMoim_Backend

 <h1 align="center" style="color:mediumpurple"> Demoim  </h1>

## [▶️ 유튜브 링크](https://www.youtube.com/watch?v=fzInCo4VSLE)

## [🄽 Notion 링크](https://hollow-slipper-e81.notion.site/DeMoim_Public-2412eac814614a45927deb0ab963c072)

<br/>
<br/>

## 🖥 프로젝트 소개

취업과 커리어준비 등 직업적 공백기에 있는 사람들에게 <br>
각자의 아이디어를 활용하여 팀 프로젝트를 만들어 진행해나갈 수 있는 소셜 커뮤니티 서비스입니다.

<br/>

## 🔎 요약

예비개발자/디자이너/기획자 들이 직접 프로젝트 팀을 모집하고 지원 할 수 있는 팀빌딩 플랫폼

- 프로젝트 모집공고를 작성하고, 지원자를 선택하여 프로젝트 팀 구성
- 회원 간 프로필 열람을 통해 상호간 기본적인 정보 제공(포지션,연락처,간단한 소개)
- 본인이 뽐내고 싶은 프로젝트들을 공유할 수 있고, 회원 간 스몰톡을 나눌 수 있는 커뮤니티

<br/>

## 📆 기간

2021년 4월 23일 ~ 2021년 5월 28일

<br/>

## 🗣 커뮤니케이션 툴

Github, Slack, Gather

## 🤠 팀원소개 ([Notion](https://www.notion.so/Demoim-87856b49c18545358ee657b434bff365))

<br/>

총 6명

- 백엔드
  - 정석진(Spring, React)
  - 이은지(Spring)
  - 김준엽(Spring)
- 프론트엔드 ([FrontEnd Github](https://github.com/holasim91/demoim_fe))
  - 심현인(React)
  - 고정원(React)
- UX/UI 디자이너 ([Zeplin](https://app.zeplin.io/project/608a507a3f3d51355497c44c))
  - 김민경

## 🚀 기능

### [회원가입/로그인 페이지] MySQL

- 회원가입 및 로그인 기능 \_ Spring Security, JWT Authentication, MySQL, Spring Data JPA
  - Cool SMS API(문자발송 API)

### [메인 페이지(팀메이킹, Detalk)]

- 팀메이킹(프로젝트 모집) \_ MySQL, Spring Data JPA

  ```
  주최자의 경우
  Quill-Editor를 사용해서 작성자가 원하는 모집글을 쓸 수 있다.
  프론트엔드 / 백엔드 / 디자이너 / 기획자를 모집할 수 있고, 최대 인원은 총 10명이다.
  원하는 기간만큼 모집을 할 수 있으며, 지원자를 선택하여 팀원으로 뽑을 수 있다.
  주최자가 희망인원을 다 선택을 하거나 모집 기간이 지나면 모집이 마감된다

  참가자의 경우
  원하는 프로젝트를 지원 할 수 있다.
  ```

- Detalk
  - Exhibition(프로젝트 자랑하기) \_ MySQL, Spring Data JPA
    ```
    Quill-Editor을 이용하여 사용자가 자신이 했던 프로젝트를 다른 회원들과 공유할 수 있다.
    댓글로 해당 프로젝트에 대한 피드백을 주고받을 수 있다.
    ```
  - Smalltalk(스몰톡 피드) \_ MySQL, Spring Data JPA
    ```
    소소한 잡담및 정보를 나눌 수 있는 공간이다.
    댓글로 질문을 주고 받을 수 있다.
    ```

### [나의 로그]

- 로그인 회원의 프로필 관리(MySQL, Spring Data JPA, Storage - AWS S3)
- 나의 프로젝트 참가 이력과, 현재 참가 하고 있는 프로젝트, 내가 주최한 프로젝트 확인
- 자신이 참여중인 프로젝트의 다른 유저의 정보 확인
- 자신이 작성한 스몰톡과 프로젝트 자랑하기 피드

## 🦄 프로젝트 썸네일

![image](https://user-images.githubusercontent.com/78028746/121785601-a7593700-cbf5-11eb-8406-009f18524c06.png)

## 🔖 기술스택

#### 백엔드 협업 툴

- Host Server: ubuntu18.04 - t2.micro(AWS EC2)
- File Storage: AWS S3 Bucket
- Database: MySQL 8.0.20(AWS RDS)

- Framework : SpringBoot 2.4.5
- Java: JDK 1.8.0
- IDE: IntelliJ IDEA 2021.1.1 x64
- Build Management: Gradle
- ORM: Spring Data JPA
- Sub-Framework: Spring Security, SpringBoot Websocket

- View Template Engine: React.js(프론트엔드)

### 👑 After 프로젝트

- 웹사이트 보안 강화
  - SSL 활용한 HTTPS 변경
- 실시간 알림 및 채팅 서비스
  - Redis Pub/Sub 활용한 WebSocket 통신 또는 외부 API(채널톡) 도입 검토
- 소셜로그인
  - 카카오, 네이버 소셜로그인(OAuth2)\

<br>

## 테이블 설계

![image](https://user-images.githubusercontent.com/78028746/119464301-8da69b80-bd7d-11eb-9f0e-b94edf8f95c2.png)

<br>
<br>

## 주요 기능 소개

<br>

### 엔티티 설계시 양방향 매핑

- 테이블과 패러다임의 불일치를 해소하기위해서 객체가 서로를 참조 할 수있도록 양방향 .
- LAZY 타입을 통해 불필요하게 참조되는 데이터 조회를 해결 -> 성능 이슈를 방지

```java
 @ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "userId")
private User smallTalkUser;

@Builder
public SmallTalk(String contents) {
        this.contents = contents;
        }

public void setUser(User user){

        //기존에 있던 smallTalk을 제거
        if(this.smallTalkUser != null){
        this.smallTalkUser.getSmallTalks().remove(this);
        }
        this.smallTalkUser = user;
        smallTalkUser.getSmallTalks().add(this);
        }
```

<br>

### S3를 이용한 이미지 업로드

```java
// Service 단 코드 발췌
import com.amazonaws.services.s3.model.ObjectMetadata;

@RequiredArgsConstructor
@Service
public class FileUploadService {

  private final S3Service s3Service;

  public String uploadImage(MultipartFile file) {
    String fileName = createFileName(file.getOriginalFilename());

    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentType(file.getContentType());

    try (InputStream inputStream = file.getInputStream()) {
      s3Service.uploadFile(inputStream, objectMetadata, fileName);
    } catch (IOException e) {
      throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생하였습니다 (%s)", file.getOriginalFilename()));
    }
    return s3Service.getFileUrl(fileName);
  }

  private String createFileName(String originalFileName) {
    return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
  }

  private String getFileExtension(String fileName) {
    try {
      return fileName.substring(fileName.lastIndexOf("."));
    } catch (StringIndexOutOfBoundsException e) {
      throw new IllegalArgumentException(String.format("잘못된 형식의 파일 (%s) 입니다", fileName));
    }
  }
}
```

- import com.amazonaws.services.s3.model.ObjectMetadata; / aws s3 라이브러리 임포트

<br>

<br>
<br>

## 최종 성과

- 작은 숫자라고 할수있지만 실제로 사용자를 런칭하고 피드백을 받아보는 목표를 실제로 이루워낸 경험입니다. 제가 개발한 사이트를 실제로 사용해주시는 분들이 있다는 점에서 뿌듯함을 느꼈던 프로젝트입니다.

![image](https://user-images.githubusercontent.com/78028746/121785420-ab388980-cbf4-11eb-98e8-0a2f4935797c.png)

![image](https://user-images.githubusercontent.com/78028746/121783238-6dcdff00-cbe8-11eb-9f34-2abfa34a9e0b.png)

<br>
<br>

## 실제 사이트에 올라온 사용자 게시글

![image](https://user-images.githubusercontent.com/78028746/121785373-74fb0a00-cbf4-11eb-8e79-d7c237ac3171.png)
![image](https://user-images.githubusercontent.com/78028746/121785380-7cbaae80-cbf4-11eb-91d6-7f59deadca97.png)
![image](https://user-images.githubusercontent.com/78028746/121785386-83e1bc80-cbf4-11eb-8999-614cba71e9cb.png)
![image](https://user-images.githubusercontent.com/78028746/121785393-87754380-cbf4-11eb-95f0-ade27b7436a5.png)
