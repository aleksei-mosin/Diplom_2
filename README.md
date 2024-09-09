# Diplom_2
## Тестирование API для Stellar Burgers
Документация по API - https://code.s3.yandex.net/qa-automation-engineer/java/cheatsheets/paid-track/diplom/api-documentation.pdf
###
| **Использованные библиотеки** |
|-------------------------------|
| java 11                       |
| junit 4.13.2                  |
| maven 3.9.6                   |
| RestAssured 5.3.0              |
| gson 2.10.1                   |
| maven-surefire 2.22.2         |
| allure-maven 2.15.0           |

#### Запуск тестов
```
mvn clean test
```


##### Создание отчета Allure
```
mvn allure:serve
```