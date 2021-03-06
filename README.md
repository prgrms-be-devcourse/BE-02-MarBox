# MarBox
![Java](https://img.shields.io/badge/Java-17-green.svg)
![Spring Boot](https://img.shields.io/badge/Spring&nbsp;Boot-2.7.0-green.svg)
![Spring Security](https://img.shields.io/badge/Spring&nbsp;Security-5.7.1-green.svg)
![JPA](https://img.shields.io/badge/JPA-2.7.0-green.svg)
![Jakarta Validation](https://img.shields.io/badge/Jakarta&nbsp;Validation-2.0.2-green.svg)
![QueryDSL](https://img.shields.io/badge/QueryDSL-5.0.0-green.svg)
![Jacoco](https://img.shields.io/badge/Jacoco-0.8.7-green.svg)
![JUnit5](https://img.shields.io/badge/JUnit5-5.8.2-green.svg)
![H2](https://img.shields.io/badge/H2-2.1.2-green.svg)
![My SQL](https://img.shields.io/badge/My&nbsp;SQL-8.0.28-green.svg)
![Redis](https://img.shields.io/badge/Redis-alpine-green.svg)
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

## ๐ฌ What is MarBox?
> MarBox๋ CGV ์ํ ์๋งค ํด๋ก  ํ๋ก์ ํธ์๋๋ค.

### ๐ฟ ์ฐ๋ฆฌ๋ ๋ค์๊ณผ ๊ฐ์ ๊ธฐ๋ฅ์ ์ ๊ณตํฉ๋๋ค
- [API](https://documenter.getpostman.com/view/12334483/UzJJtcka#intro) 

### ๐ฟ ๋ชฉ์ 
- Agile ๋ฐฉ๋ฒ์ผ๋ก ํ์ ๊ฒฝํ
- ์์ฉ๋๋ ์๋น์ค ๋๋ฉ์ธ ๋ถ์ ๋ฐ ๊ตฌํ
- CI ๋ฐ ๋ฐฐํฌ ๊ฒฝํ
- Restful Api ์ค๊ณ ๊ฒฝํ
- ๊ธฐํ๋ด ๋ชฉํํ MVP ๊ฐ๋ฐ ๊ฒฝํ


## ๐ฌ Planning
ํ๋ก์ ํธ ๊ธฐ๊ฐ: 2022.06.15 ~ 2022.07.18
### ๐ฟ mvp1 (06.15 ~ 06.26)
- ๋๋ฉ์ธ ์ค๊ณ, ๋ก๊ทธ์ธ, ์ํ ์๋งค์ ๋ณด ์กฐํ
- [์์ธ](https://caring-pin-8ac.notion.site/MVP1-6-15-6-26-bdc09db96be247d9bdcc94aa653a3875)
- [BackLog](https://github.com/prgrms-be-devcourse/BE-02-MarBox/projects/1)

### ๐ฟ mvp2 (06.27 ~ 07.06)
- ์ํ ์๋งค ๊ตฌํ
- [์์ธ](https://caring-pin-8ac.notion.site/MVP2-6-27-7-06-7262927eafc64fd8bf97b84e1143f6b5)
- [BackLog](https://github.com/prgrms-be-devcourse/BE-02-MarBox/projects/2)

### ๐ฟ mvp3 (7.11 ~7.18)
- ๊ฒฐ์  ๊ตฌํ, ํ๋ก์ ํธ ๋ฆฌํํฐ๋ง
- [์์ธ](https://caring-pin-8ac.notion.site/MVP3-7-11-7-18-a6703f880f0c48e5b21a853547bcad70)
- [BackLog](https://github.com/prgrms-be-devcourse/BE-02-MarBox/projects/3)

## ๐ฌ ERD & EntityDiagram
### ๐ฟ TableDiagram
![table-diagram](https://user-images.githubusercontent.com/29492667/179153690-e663b225-cfdf-4ee3-b803-72e7c19bf050.png)

### ๐ฟ EntityDiagram
![erd](https://user-images.githubusercontent.com/26343023/179152786-ac9b2aab-b4b9-49a3-95a7-a28a602f3df1.png)

## ๐ฌ Environment
### ๐ฟ Dev Server
![dev-environment](https://user-images.githubusercontent.com/29492667/177974855-9870e1d2-5e78-408f-8b36-15d833564dec.png)
### ๐ฟ Test Server
![test-environment](https://user-images.githubusercontent.com/29492667/179156796-4a4a2954-82ab-4d7f-bad3-1cb96c917c73.png)

## ๐ฌ What we use

### ๐ฟ Backend
- Java
- Spring Boot
- Spring Security
- Jpa
- QueryDSL

### ๐ฟ Infra
- AWS
  - EC2
  - RDS
  - S3
- Docker
  - Mysql
  - Redis
- Nginx

### ๐ฟ DevOps
- GitHub Actions(CI)
  - Check Style
  - Jacoco

### ๐ฟ Tool
- IntelliJ
- Postman

### ๐ฟ Collaborate
- Git
- GitHub
- Notion
- Slack

### ๐ฟ Docs
- Spring Rest Docs
- Swagger-UI

## ๐ฌ Convention
### ๐ฟ ๋ธ๋์น ์ ๋ต
![branch-strategy](https://user-images.githubusercontent.com/86591021/177478080-8cbc253d-4fb6-496a-a43c-f65ca2c29d77.png)

### ๐ฟ Code Convention
- [Naver Java Convention](https://naver.github.io/hackday-conventions-java/)
- [Custom Convention](https://github.com/hyuk0309/BE-02-MarBox/wiki/Team-Custom-Code-Convention)

### ๐ฟ Commit Convention
- [AngularJS commit convention](https://gist.github.com/stephenparish/9941e89d80e2bc58a153)

### ๐ฟ PR, Issue Convention
- [PR Convention](https://github.com/prgrms-be-devcourse/BE-02-MarBox/blob/develop/.github/pull_request_template.md)
- [Issue Convention](https://github.com/prgrms-be-devcourse/BE-02-MarBox/tree/develop/.github/ISSUE_TEMPLATE)

### ๐ฟ Merge Convention
- [MarBox CI](https://github.com/prgrms-be-devcourse/BE-02-MarBox/blob/develop/.github/workflows/marbox-ci.yml) ํต๊ณผ
- Review Approve 2๊ฐ

## ๐ฌ Get Start
```
//start in dev environment
git clone https://github.com/prgrms-be-devcourse/BE-02-MarBox.git

cd ./BE-02-MarBox/

//set database
docker-compose up -d

// need application-aws.yml (for S3 keys)

//build & execute
./gradlew clean build
java -jar ./build/libs/BE-02-MarBox-0.0.1-SNAPSHOT.jar
```

## ๐ฌ Member
|     Name      | Profile                                                                          | GitHub                                |      Role       |
|:-------------:|:----------------------------------------------------------------------------------:|:-------------------------------------:|:---------------:|
|  Taesan Kang  | <img src="https://avatars.githubusercontent.com/u/26343023?v=4" width = "200px"> | [๐ฟ click](https://github.com/Pawer0223) |  Scrum Master   |
|  Jiwoong Kim  | <img src="https://avatars.githubusercontent.com/u/54886222?v=4" width = "200px"> | [๐ฟ click](https://github.com/wisehero)  |    Developer    |
| Eunhyuk Bang  | <img src="https://avatars.githubusercontent.com/u/29492667?v=4" width = "200px"> | [๐ฟ click](https://github.com/hyuk0309)  |  Scrum Master   |
|   Hanju Lee   | <img src="https://avatars.githubusercontent.com/u/43159295?v=4" width = "200px"> | [๐ฟ click](https://github.com/yanJuicy)  |  Product Owner  |
| Dahyeon Jeong | <img src="https://avatars.githubusercontent.com/u/86591021?v=4" width = "200px"> | [๐ฟ click](https://github.com/sdardew)   |    Developer    |

## ๐ฌ Retrospect
[๋งํฌ](https://caring-pin-8ac.notion.site/97752ce3a8194036bccd0623464edf3c)