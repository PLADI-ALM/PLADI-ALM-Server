# (주)플래디 사내 관리 시스템
<br>

![image](https://github.com/PLADI-ALM/PLADI-ALM-Web/assets/62008784/0ecb70cf-fcae-461e-8dcd-d2d612fee0e3)

<br>

## Tech Stack
### Backend
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> ![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens) <img src="https://img.shields.io/badge/spring data jpa-6DB33F?style=for-the-badge&logoColor=white"> <img src="https://img.shields.io/badge/querydsl-6DB33F?style=for-the-badge&logoColor=white"> <img src="https://img.shields.io/badge/hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white"> <img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
<img src="https://img.shields.io/badge/junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white">
### DB
<img src="https://img.shields.io/badge/amazon rds-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white"> <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/jasypt-0769AD?style=for-the-badge&logoColor=white"> <img src="https://img.shields.io/badge/redis-DC382D?style=for-the-badge&logo=redis&logoColor=white"> <img src="https://img.shields.io/badge/firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=white"> <img src="https://img.shields.io/badge/amazon s3-569A31?style=for-the-badge&logo=amazons3&logoColor=white">

### CI/CD
<img src="https://img.shields.io/badge/github actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white"> <img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"> <img src="https://img.shields.io/badge/docker hub-2496ED?style=for-the-badge&logo=docker&logoColor=white"> <img src="https://img.shields.io/badge/sonarcloud-F3702A?style=for-the-badge&logo=sonarcloud&logoColor=white"> 

### Deploy
<img src="https://img.shields.io/badge/amazon ec2-FF9900?style=for-the-badge&logo=amazon ec2&logoColor=white"> <img src="https://img.shields.io/badge/amazon api gateway-FF4F8B?style=for-the-badge&logo=amazonapigateway&logoColor=white"> <img src="https://img.shields.io/badge/aws lambda-FF9900?style=for-the-badge&logo=awslambda&logoColor=white"> 

### Develop Tool
<img src="https://img.shields.io/badge/intelliJ-000000?style=for-the-badge&logo=intellij idea&logoColor=white"> <img src="https://img.shields.io/badge/swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=white"> <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"> <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white"> 

### Etc.
<img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white"/> <img src="https://img.shields.io/badge/Jira-0052CC?style=for-the-badge&logo=jira&logoColor=white"/>
<img src="https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white"/> <img src="https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=Figma&logoColor=white"/>

<br> 
<br>

## Project Architecture
<details>
<summary>Final Architecture</summary>
<br>
  
![Group 48018](https://github.com/PLADI-ALM/PLADI-ALM-Server/assets/90022940/0e680667-e407-4a4e-8fa5-f02b57a187a5)
</details>

<details>
<summary>CI/CD</summary>
<br>
 
![cicd](https://github.com/PLADI-ALM/PLADI-ALM-Server/assets/90022940/35372c6b-27bc-4321-9066-7efdf1c6444d)
</details>

<details>
<summary>AWS Lambda</summary>
  <br>
 
![Group 48018](https://github.com/PLADI-ALM/PLADI-ALM-Server/assets/90022940/7f07a5c9-2829-45c5-a035-1a6a85294254)
</details>

<details>
<summary>FCM(Firebase Cloud Messaging)</summary>
   <br>
  
![Group 48018](https://github.com/PLADI-ALM/PLADI-ALM-Server/assets/90022940/0ab6d61e-179f-49e7-a318-b706e05a5d7d)
</details>

<details>
<summary>SMTP(Simple Mail Transfer Protocol)</summary>
    <br>
 
![Group 48018](https://github.com/PLADI-ALM/PLADI-ALM-Server/assets/90022940/18365afd-e04e-49e1-b750-8b8b234fd202)
</details>
<br>

## Project Structure

<details>
<summary>Details</summary>

```jsx
src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── example
    │   │           └── pladialmserver
    │   │               ├── PladiAlmServerApplication.java
    │   │               ├── booking
    │   │               │   ├── controller
    │   │               │   │   ├── car
    │   │               │   │   │   ├── CarBookingAdminController.java
    │   │               │   │   │   └── CarBookingController.java
    │   │               │   │   ├── office
    │   │               │   │   │   ├── OfficeBookingAdminController.java
    │   │               │   │   │   └── OfficeBookingController.java
    │   │               │   │   └── resource
    │   │               │   │       ├── ResourceBookingAdminController.java
    │   │               │   │       └── ResourceBookingController.java
    │   │               │   ├── dto
    │   │               │   │   ├── request
    │   │               │   │   │   ├── ReturnProductReq.java
    │   │               │   │   │   └── SendEmailReq.java
    │   │               │   │   └── response
    │   │               │   │       ├── AdminBookingRes.java
    │   │               │   │       ├── BookingRes.java
    │   │               │   │       ├── OfficeBookingDetailRes.java
    │   │               │   │       └── ProductBookingDetailRes.java
    │   │               │   ├── entity
    │   │               │   │   ├── CarBooking.java
    │   │               │   │   ├── OfficeBooking.java
    │   │               │   │   └── ResourceBooking.java
    │   │               │   ├── repository
    │   │               │   │   ├── carBooking
    │   │               │   │   │   ├── CarBookingCustom.java
    │   │               │   │   │   ├── CarBookingRepository.java
    │   │               │   │   │   └── CarBookingRepositoryImpl.java
    │   │               │   │   ├── officeBooking
    │   │               │   │   │   ├── OfficeBookingCustom.java
    │   │               │   │   │   ├── OfficeBookingRepository.java
    │   │               │   │   │   └── OfficeBookingRepositoryImpl.java
    │   │               │   │   └── resourceBooking
    │   │               │   │       ├── ResourceBookingCustom.java
    │   │               │   │       ├── ResourceBookingRepository.java
    │   │               │   │       └── ResourceBookingRepositoryImpl.java
    │   │               │   └── service
    │   │               │       ├── CarBookingService.java
    │   │               │       ├── OfficeBookingService.java
    │   │               │       ├── ProductBookingService.java
    │   │               │       └── ResourceBookingService.java
    │   │               ├── equipment
    │   │               │   ├── controller
    │   │               │   │   └── EquipmentController.java
    │   │               │   ├── dto
    │   │               │   │   ├── request
    │   │               │   │   │   ├── RegisterEquipmentReq.java
    │   │               │   │   │   └── UpdateEquipmentReq.java
    │   │               │   │   └── response
    │   │               │   │       ├── EquipmentCategoryRes.java
    │   │               │   │       ├── GetEquipmentRes.java
    │   │               │   │       └── SearchEquipmentRes.java
    │   │               │   ├── entity
    │   │               │   │   ├── Equipment.java
    │   │               │   │   └── EquipmentCategory.java
    │   │               │   ├── repository
    │   │               │   │   ├── EquipmentCategoryRepository.java
    │   │               │   │   └── EquipmentRepository.java
    │   │               │   └── service
    │   │               │       └── EquipmentService.java
    │   │               ├── global
    │   │               │   ├── Constants.java
    │   │               │   ├── CustomPage.java
    │   │               │   ├── config
    │   │               │   │   ├── AsyncConfig.java
    │   │               │   │   ├── EmailConfig.java
    │   │               │   │   ├── JasyptConfig.java
    │   │               │   │   ├── QuerydslConfig.java
    │   │               │   │   ├── RedisConfig.java
    │   │               │   │   ├── SwaggerConfig.java
    │   │               │   │   └── WebConfig.java
    │   │               │   ├── controller
    │   │               │   │   └── HealthCheckController.java
    │   │               │   ├── entity
    │   │               │   │   ├── BaseEntity.java
    │   │               │   │   └── BookingStatus.java
    │   │               │   ├── entityListener
    │   │               │   │   └── UserEntityListener.java
    │   │               │   ├── exception
    │   │               │   │   ├── BaseException.java
    │   │               │   │   ├── BaseResponseCode.java
    │   │               │   │   └── ExceptionHandlerAdvice.java
    │   │               │   ├── feign
    │   │               │   │   ├── dto
    │   │               │   │   │   └── UserReq.java
    │   │               │   │   ├── event
    │   │               │   │   │   ├── DeleteUserEvent.java
    │   │               │   │   │   └── UserEvent.java
    │   │               │   │   ├── feignClient
    │   │               │   │   │   └── ArchivingServerClient.java
    │   │               │   │   ├── handler
    │   │               │   │   │   ├── ArchivingServerEventHandler.java
    │   │               │   │   │   └── ArchivingServerEventHandlerImpl.java
    │   │               │   │   └── publisher
    │   │               │   │       ├── ArchivingServerEventPublisher.java
    │   │               │   │       └── ArchivingServerEventPublisherImpl.java
    │   │               │   ├── resolver
    │   │               │   │   ├── Account.java
    │   │               │   │   └── LoginResolver.java
    │   │               │   ├── response
    │   │               │   │   └── ResponseCustom.java
    │   │               │   ├── service
    │   │               │   └── utils
    │   │               │       ├── AwsS3ImageUrlUtil.java
    │   │               │       ├── BeanUtil.java
    │   │               │       ├── DateTimeUtil.java
    │   │               │       ├── DateTimeUtils.java
    │   │               │       ├── EmailUtil.java
    │   │               │       ├── JwtUtil.java
    │   │               │       └── RedisUtil.java
    │   │               ├── notification
    │   │               │   ├── dto
    │   │               │   │   ├── FcmMessage.java
    │   │               │   │   ├── Message.java
    │   │               │   │   └── Notification.java
    │   │               │   ├── entity
    │   │               │   │   └── PushNotification.java
    │   │               │   ├── repository
    │   │               │   │   └── PushNotificationRepository.java
    │   │               │   └── service
    │   │               │       └── PushNotificationService.java
    │   │               ├── office
    │   │               │   ├── controller
    │   │               │   │   ├── OfficeAdminController.java
    │   │               │   │   └── OfficeController.java
    │   │               │   ├── dto
    │   │               │   │   ├── request
    │   │               │   │   │   ├── CreateOfficeReq.java
    │   │               │   │   │   └── OfficeReq.java
    │   │               │   │   └── response
    │   │               │   │       ├── AdminOfficeRes.java
    │   │               │   │       ├── AdminOfficesDetailsRes.java
    │   │               │   │       ├── BookedTimeRes.java
    │   │               │   │       ├── BookingStateRes.java
    │   │               │   │       ├── OfficeRes.java
    │   │               │   │       ├── OfficeReservatorRes.java
    │   │               │   │       └── OfficesList.java
    │   │               │   ├── entity
    │   │               │   │   ├── Facility.java
    │   │               │   │   ├── Office.java
    │   │               │   │   └── OfficeFacility.java
    │   │               │   ├── repository
    │   │               │   │   ├── OfficeFacilityRepository.java
    │   │               │   │   ├── facility
    │   │               │   │   │   ├── FacilityCustom.java
    │   │               │   │   │   ├── FacilityRepository.java
    │   │               │   │   │   └── FacilityRepositoryImpl.java
    │   │               │   │   └── office
    │   │               │   │       ├── OfficeCustom.java
    │   │               │   │       ├── OfficeRepository.java
    │   │               │   │       └── OfficeRepositoryImpl.java
    │   │               │   └── service
    │   │               │       └── OfficeService.java
    │   │               ├── product
    │   │               │   ├── car
    │   │               │   │   ├── controller
    │   │               │   │   │   ├── CarAdminController.java
    │   │               │   │   │   └── CarController.java
    │   │               │   │   ├── dto
    │   │               │   │   │   └── CarRes.java
    │   │               │   │   ├── entity
    │   │               │   │   │   └── Car.java
    │   │               │   │   ├── repository
    │   │               │   │   │   ├── CarCustom.java
    │   │               │   │   │   ├── CarRepository.java
    │   │               │   │   │   └── CarRepositoryImpl.java
    │   │               │   │   └── service
    │   │               │   │       └── CarService.java
    │   │               │   ├── dto
    │   │               │   │   ├── request
    │   │               │   │   │   ├── CreateProductReq.java
    │   │               │   │   │   └── ProductReq.java
    │   │               │   │   └── response
    │   │               │   │       ├── AdminProductDetailsRes.java
    │   │               │   │       ├── AdminProductRes.java
    │   │               │   │       ├── AdminProductsRes.java
    │   │               │   │       ├── ProductBookingRes.java
    │   │               │   │       ├── ProductDetailRes.java
    │   │               │   │       └── ProductList.java
    │   │               │   ├── resource
    │   │               │   │   ├── controller
    │   │               │   │   │   ├── ResourceAdminController.java
    │   │               │   │   │   └── ResourceController.java
    │   │               │   │   ├── dto
    │   │               │   │   │   └── ResourceRes.java
    │   │               │   │   ├── entity
    │   │               │   │   │   └── Resource.java
    │   │               │   │   ├── repository
    │   │               │   │   │   ├── ResourceCustom.java
    │   │               │   │   │   ├── ResourceRepository.java
    │   │               │   │   │   └── ResourceRepositoryImpl.java
    │   │               │   │   └── service
    │   │               │   │       └── ResourceService.java
    │   │               │   └── service
    │   │               │       └── ProductService.java
    │   │               └── user
    │   │                   ├── controller
    │   │                   │   ├── AdminUserController.java
    │   │                   │   └── UserController.java
    │   │                   ├── dto
    │   │                   │   ├── TokenDto.java
    │   │                   │   ├── request
    │   │                   │   │   ├── AdminUpdateUserReq.java
    │   │                   │   │   ├── CheckEmailCodeReq.java
    │   │                   │   │   ├── CreateUserReq.java
    │   │                   │   │   ├── EmailPWReq.java
    │   │                   │   │   ├── ResetPWReq.java
    │   │                   │   │   ├── ResponsibilityListRes.java
    │   │                   │   │   ├── ResponsibilityRes.java
    │   │                   │   │   ├── SendAssetsEmailReq.java
    │   │                   │   │   ├── UpdateUserReq.java
    │   │                   │   │   └── VerifyEmailReq.java
    │   │                   │   └── response
    │   │                   │       ├── DepartmentListDto.java
    │   │                   │       ├── NotificationRes.java
    │   │                   │       ├── UserNameRes.java
    │   │                   │       └── UserRes.java
    │   │                   ├── entity
    │   │                   │   ├── Affiliation.java
    │   │                   │   ├── Department.java
    │   │                   │   ├── Role.java
    │   │                   │   └── User.java
    │   │                   ├── repository
    │   │                   │   ├── AffiliationRepository.java
    │   │                   │   ├── DepartmentRepository.java
    │   │                   │   └── user
    │   │                   │       ├── UserCustom.java
    │   │                   │       ├── UserRepository.java
    │   │                   │       └── UserRepositoryImpl.java
    │   │                   └── service
    │   │                       └── UserService.java
    │   └── resources
    │       ├── application-dev.yml
    │       ├── application-prod.yml
    │       ├── application.properties
    │       ├── application.yml
    │       └── templates
    │           ├── assets.html
    │           ├── booking.html
    │           └── email.html
    └── test
        ├── java
            └── com
                └── example
                    └── pladialmserver
                        ├── PladiAlmServerApplicationTests.java
                        ├── booking
                        │   ├── controller
                        │   │   └── resource
                        │   └── service
                        │       ├── ResourceBookingServiceTest.java
                        │       └── model
                        │           └── TestResourceBookingInfo.java
                        ├── equipment
                        │   ├── controller
                        │   │   └── EquipmentControllerTest.java
                        │   └── service
                        │       ├── EquipmentServiceTest.java
                        │       └── model
                        │           └── TestEquipmentInfo.java
                        ├── global
                        │   ├── ControllerTestSupport.java
                        │   └── IntegrationTestSupport.java
                        ├── infra
                        │   └── HealthCheckControllerTest.java
                        ├── office
                        │   ├── controller
                        │   │   └── OfficeControllerTest.java
                        │   └── service
                        │       ├── OfficeServiceBookingTest.java
                        │       ├── OfficeServiceTest.java
                        │       └── model
                        │           └── TestOfficeInfo.java
                        ├── resource
                        │   ├── controller
                        │   │   └── ResourceControllerTest.java
                        │   └── service
                        │       ├── ResourceServiceTest.java
                        │       └── model
                        │           └── TestResourceInfo.java
                        └── user
                            └── service
                                ├── UserServiceTest.java
                                └── model
                                    └── TestUserInfo.java
```
<br>
</details>
<br><br>


## DB 
![Untitled 2](https://github.com/PLADI-ALM/PLADI-ALM-Server/assets/90022940/54bdb4d3-92b4-4a4b-a69b-aca5dd57bc02)

<br><br>

<br>

## Develop Convention
**Commit**
> [지라번호] feat: 설명
```
[PDS-5] feat: 회의실 목록 조회 API 생성
```

- feat: 새로운 주요 기능 추가
- fix: 일반적인 수정 사항
- chore: config, 라이브러리, 빌드 관련 파일 수정
- refactor: 코드 리팩토링
- rename: 파일, 클래스, 변수명 등 이름 변경
- docs: 문서 수정
- comment: 주석 추가 및 수정
- hotfix: 긴급한 수정 사항
- test: 테스트 코드 작성

<br>

**Pull Request**
> [지라번호/feat] PR설명
```
[PDS-5/feat] 회의실 목록 조회 API
```

<br>

**Branch**
> feat/PDS-지라번호-메소드명(카멜 케이스)
```
feat/PDS-5-createOffice, fix/PDS-6-modifyReservation
```

- main
    - 사용자가 사용하는 브랜치, 언제나 실행가능한 상태
- develop
    - 개발자가 사용하는 브랜치
    - 실행 가능한 상태를 만들어 가는 과정
- feat
    - 새로운 기능 브랜치
- test
    - 테스트가 필요한 코드용 브랜치
- fix
    - 버그 발생 시 버그 수정
- hotfix
    - 긴급하게 버그를 수정하는 브랜치

<br>



<br>

## Member
|[김민기](https://github.com/dangnak2)|[박서연](https://github.com/psyeon1120)|[박소정](https://github.com/sojungpp)|[이승학](https://github.com/leeseunghakhello)|[장채은](https://github.com/chaerlo127)|
|:---:|:---:|:---:|:---:|:---:|
|<img src="https://github.com/dangnak2.png" width="180" height="180" >|<img src="https://github.com/psyeon1120.png" width="180" height="180" >|<img src="https://github.com/sojungpp.png" width="180" height="180" >|<img src="https://github.com/leeseunghakhello.png" width="180" height="180" >|<img src="https://github.com/chaerlo127.png" width="180" height="180" >|
| **Server Architect <br> Backend Developer** | **Project Manager <br> Backend Developer**| **Project Manager <br> Backend Developer** | **Test Leader <br> Backend Developer** | **DB Developer <br> Backend Developer** |
