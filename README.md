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
![SwaggerUI](https://img.shields.io/badge/SwaggerUI-3.0.0-green.svg)
![Spring Rest Docs](https://img.shields.io/badge/Spring&nbsp;Rest&nbsp;Docs-2.0.6-green.svg)
![Issues](https://img.shields.io/github/issues/prgrms-be-devcourse/BE-02-MarBox)
![Forks](https://img.shields.io/github/forks/prgrms-be-devcourse/BE-02-MarBox)

## Index
- [What is MarBox?](#what-is-marbox?)
- [Planning](#planning)
- [ERD & EntityDiagram](#ERD-&-EntityDiagram)
- [Environment](#Environment)
- [What we use](#What-we-use)
- [Convention](#Convention)
- [Get Start](#Get-Start)
- [Member](#Member)
- [Retrospect](#retrospect)

## 🎬 What is MarBox?
> MarBox는 CGV 영화 예매 클론 프로젝트입니다.

### 🍿 우리는 다음과 같은 기능을 제공합니다
- [API](https://documenter.getpostman.com/view/12334483/UzJJtcka#intro) 

### 🍿 목적
- Agile 방법으로 협업 경험
- 상용되는 서비스 도메인 분석 및 구현
- CI 및 배포 경험
- Restful Api 설계 경험
- 기한내 목표한 MVP 개발 경험


## 🎬 Planning
프로젝트 기간: 2022.06.15 ~ 2022.07.06
### 🍿 mvp1 (06.15 ~ 06.26)
- 도메인 설계, 로그인, 영화 예매정보 조회
- [상세](https://caring-pin-8ac.notion.site/MVP1-6-15-6-26-bdc09db96be247d9bdcc94aa653a3875)
- [BackLog](https://github.com/prgrms-be-devcourse/BE-02-MarBox/projects/1)

### 🍿 mvp2 (06.27 ~ 07.06)
- 영화 예매 구현
- [상세](https://caring-pin-8ac.notion.site/MVP2-6-27-7-06-7262927eafc64fd8bf97b84e1143f6b5)
- [BackLog](https://github.com/prgrms-be-devcourse/BE-02-MarBox/projects/2)

## 🎬 ERD & EntityDiagram
### 🍿 TableDiagram
![table](https://user-images.githubusercontent.com/29492667/178174500-e3f07c6c-0a12-4f20-bfc8-b16dec9d877c.png)

### 🍿 EntityDiagram
![erd](https://user-images.githubusercontent.com/29492667/178174504-86b45617-d539-4c17-8671-d6c4292f286f.png)

## 🎬 Environment
### 🍿 Dev Server
![dev-environment](https://user-images.githubusercontent.com/29492667/177974855-9870e1d2-5e78-408f-8b36-15d833564dec.png)
### 🍿 Test Server
![test-environment](https://user-images.githubusercontent.com/29492667/177974984-92aa6ee3-b59a-4816-bb85-d3ce86998ae9.png)

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

### 🍿 Docs
- Spring Rest Docs
- Swagger-UI

## 🎬 Convention
### 🍿 브랜치 전략
![branch-strategy](https://user-images.githubusercontent.com/86591021/177478080-8cbc253d-4fb6-496a-a43c-f65ca2c29d77.png)

### 🍿 Code Convention
- [Naver Java Convention](https://naver.github.io/hackday-conventions-java/)
- [Custom Convention](https://github.com/hyuk0309/BE-02-MarBox/wiki/Team-Custom-Code-Convention)

### 🍿 Commit Convention
- [AngularJS commit convention](https://gist.github.com/stephenparish/9941e89d80e2bc58a153)

### 🍿 PR, Issue Convention
- [PR Convention](https://github.com/prgrms-be-devcourse/BE-02-MarBox/blob/develop/.github/pull_request_template.md)
- [Issue Convention](https://github.com/prgrms-be-devcourse/BE-02-MarBox/tree/develop/.github/ISSUE_TEMPLATE)

### 🍿 Merge Convention
- [MarBox CI](https://github.com/prgrms-be-devcourse/BE-02-MarBox/blob/develop/.github/workflows/marbox-ci.yml) 통과
- Review Approve 2개

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

## 🎬 Member
|     Name      | Profile                                                                          | GitHub                                |      Role       |
|:-------------:|:----------------------------------------------------------------------------------:|:-------------------------------------:|:---------------:|
|  Taesan Kang  | <img src="https://avatars.githubusercontent.com/u/26343023?v=4" width = "200px"> | [🍿 click](https://github.com/Pawer0223) |  Scrum Master   |
|  Jiwoong Kim  | <img src="https://avatars.githubusercontent.com/u/54886222?v=4" width = "200px"> | [🍿 click](https://github.com/wisehero)  |    Developer    |
| Eunhyuk Bang  | <img src="https://avatars.githubusercontent.com/u/29492667?v=4" width = "200px"> | [🍿 click](https://github.com/hyuk0309)  |  Scrum Master   |
|   Hanju Lee   | <img src="https://avatars.githubusercontent.com/u/43159295?v=4" width = "200px"> | [🍿 click](https://github.com/yanJuicy)  |  Product Owner  |
| Dahyeon Jeong | <img src="https://avatars.githubusercontent.com/u/86591021?v=4" width = "200px"> | [🍿 click](https://github.com/sdardew)   |    Developer    |

## 🎬 Retrospect
[링크](https://caring-pin-8ac.notion.site/97752ce3a8194036bccd0623464edf3c)