# MarBox
![Java](https://img.shields.io/badge/Java-17-green.svg)
![Spring Boot](https://img.shields.io/badge/Spring&nbsp;Boot-2.7.0-green.svg)
![Spring Security](https://img.shields.io/badge/Spring&nbsp;Security-5.7.1-green.svg)
![JPA](https://img.shields.io/badge/JPA-2.7.0-green.svg)
![Jakarta Validation](https://img.shields.io/badge/Jakarta&nbsp;Validation-2.0.2-green.svg)
![JUnit5](https://img.shields.io/badge/JUnit5-5.8.2-green.svg)
![H2](https://img.shields.io/badge/H2-2.1.2-green.svg)
![My SQL](https://img.shields.io/badge/My&nbsp;SQL-8.0.28-green.svg)
![Gradle](https://img.shields.io/badge/Gradle-7.4.1-green.svg)
![Ubuntu](https://img.shields.io/badge/Ubuntu-20.04-green.svg)
![Docker](https://img.shields.io/badge/Docker-20.10.14-green.svg)
![Issues](https://img.shields.io/github/issues/prgrms-be-devcourse/BE-02-MarBox)
![Forks](https://img.shields.io/github/forks/prgrms-be-devcourse/BE-02-MarBox)

## Index
- [What is MarBox?](#what-is-marbox?)
- Get Start
- Member
- 회고

## 🎬 What is MarBox?
> MarBox는 CGV 영화 예매 클론 프로젝트입니다.

### 🍿 우리는 다음과 같은 기능을 제공합니다
- 관리자
  - 영화관 추가
  - 영화 추가
  - 영화 스케쥴 추가
- 손님
  - 영화 예매
  - 티켓 조회

### 🍿 목적
- Agile 방법으로 협업 경험
- 상용되는 서비스 도메인 분석 및 구현
- CI 및 배포 경험
- Restful Api 설계 경험
- 기한내 목표한 MVP 개발 경험

## 🎬 기획
- 2022.06.15 ~ 2022.07.06
- mvp1 (06.15 ~ 06.26)
  - 도메인 설계, 로그인, 영화 예매정보 조회구현

캡처1

- mvp2 (06.27 ~ 07.06)
  - 영화 예매 구현

캡처2

## 🎬 Get Start
```
//start in dev environment
git clone https://github.com/prgrms-be-devcourse/BE-02-MarBox.git

cd ./BE-02-MarBox/

//set database
docker-compose up -d

//build & execute
./gradlew clean build
java -jar ./build/libs/BE-02-MarBox-0.0.1-SNAPSHOT.jar
```

## 🎬 What we use

### 🍿 Backend
- Java
- Spring Boot
- Spring Security
- Jpa


### 🍿 Devops
- AWS
  - EC2
  - RDS
- Docker
- Nginx
- Github Action (CI)

### 🍿 Tool
- IntelliJ
- Postman

### 🍿 Collaborate
- Check Style
- Git
- Github
- Notion
- Slack


## 🎬 Convention
### 🍿 브랜치 전략
### 🍿 Code Convention
- Naver
- [Notion]()

## 🎬 Environment
### 🍿 Dev Server
![dev-enviornment](https://user-images.githubusercontent.com/29492667/177267082-226a0517-f61d-4dca-bf95-91b318e3fe01.png)
### 🍿 Test Server
![local-enviornment](https://user-images.githubusercontent.com/29492667/177266037-f9049043-1b74-4e6f-960f-932d005b4b4d.png)


## 🎬 Member
|     Name      | Profile                                                                          | GitHub                                |      Role       |
|:-------------:|:----------------------------------------------------------------------------------:|:-------------------------------------:|:---------------:|
|  Taesan Kang  | <img src="https://avatars.githubusercontent.com/u/26343023?v=4" width = "200px"> | [🍿 click](https://github.com/Pawer0223) |  Scrum Master   |
|  Jiwoong Kim  | <img src="https://avatars.githubusercontent.com/u/54886222?v=4" width = "200px"> | [🍿 click](https://github.com/wisehero)  |    Developer    |
| Eunhyuk Bang  | <img src="https://avatars.githubusercontent.com/u/29492667?v=4" width = "200px"> | [🍿 click](https://github.com/hyuk0309)  |  Scrum Master   |
|   Hanju Lee   | <img src="https://avatars.githubusercontent.com/u/43159295?v=4" width = "200px"> | [🍿 click](https://github.com/yanJuicy)  |  Product Owner  |
| Dahyeon Jeong | <img src="https://avatars.githubusercontent.com/u/86591021?v=4" width = "200px"> | [🍿 click](https://github.com/sdardew)   |    Developer    |

## 🎬 회고
[링크]()