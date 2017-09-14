[![Build Status](https://travis-ci.org/mental-soft/authorization.svg?branch=master)](https://travis-ci.org/mental-soft/authorization)


# Mental Authorization

# Dependency
- Postgresql

### Run postgresql with docker 
<code>docker run --name cont_postgresql 
-itd -p 5432:5432 --restart always 
-e DB_NAME=authorization-dev,authorization-qa 
-e DB_USER=dbuser -e DB_PASS=12345 sameersbn/postgresql</code>


>Note: Locale makinenizi hem developer hem de qa makinesi olarak düşünebilirsiniz.
Qa makineniz varsa authorization-qa databasei oluşturmanıza gerek yok.

#### Environment
3 tane environmentimiz vardır. 
- [Default](src/main/resources/config/application-default.yml)
- [Quality Assurance](src/main/resources/config/application-qa.yml)
- [Production](src/main/resources/config/application-prod.yml)

# Build
Gradle build işlemi için

<code>gradlew build</code>

# Run

#### Gradle üzerinden run etmek için

<code>gradlew bootRun</code> or


<code>gradlew bRun</code>

###### Environment
İstediğiniz environment için komutun sonuna -Dspring.profiles.active={profile-name} ekleyin.

Örneğin

<code>gradlew bootRun -Dspring.profiles.active=qa</code>


#### Java üzerinden run etmek için

<code>java -jar build/libs/authorization.jar</code>

###### Environment
İstediğiniz environment için komutun sonuna --spring.profiles.active={profile-name} ekleyin.

Örneğin

<code>java -jar build/libs/authentication.jar --spring.profiles.active=qa</code>


# Docker

## Projeyi docker komutundan çalıştırma
>Docker run ile çalıştırırken dependent olan containerları ayağa kaldırmalısınız.

<code>docker run -it --name cont_authorization -p 10020:10020 --link cont_postgresql mental/authorization</code>

# Docker compose dan çalıştırma
> Compose ile çalıştırırken dependent olan başka containerlar var ise durdurunuz. Yoksa port çakışması olur.

<code>docker-compose up</code>


## Up yaparken build etmek için
<code>docker-compose up --build</code>


## Build docker
> Imagei değiştirmek isterseniz.

Projeyi gradle ile build ettikten sonra docker 
imageini oluşturmak için docker build etmelisiniz.

<code>docker build -t mental/authorization .</code>


# Browse

[http://localhost:10030/authorization](http://localhost:10030/authorization)


