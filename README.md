# E-Shop 

*Нинтендо не бань*

Потыкаться можно на https://server.pasvitas.online/staging/eshop/

Админ-аккаунт: **admin/admin**

## Саммари:

Просто электронный магазин

* Товары
* Регистрация-Авторизация
* Корзина
* Профиль с заказами
* Админ-панель для товаров

## Стек

* Java 17
* Spring Boot
* Vaadin
* MongoDB (Spring Data Mongo)

### Деплой:

* Docker
* Jenkins
* Helm


## Сбор production-билда:

#### А еще нужно настроить коннект к БД

` mvn clean package -Pproduction `

` java -jar target/eshop-0.0.1-SNASHOT.jar` 

Адрес по-умолчанию http://localhost:8090

Роль администратора: **ADMIN**