# Жмурина Ксения Игоревна, БПИ218. ДЗ по КПО 4
# Документация в Postman: https://www.postman.com/supply-physicist-32979132/workspace/claudemonet/collection/27667068-bded2baa-6f22-4100-8ca8-378c66b627b7?action=share&creator=27667068
### По этой ссылке должна открыться сама документация https://www.postman.com/supply-physicist-32979132/workspace/claudemonet/documentation/27667068-bded2baa-6f22-4100-8ca8-378c66b627b7?entity=&branch=&version=
# Сервисы:
## ClaudeMonetAuthorization - авторизация и регистрация пользователя
## ClaudeMonetOrder - сервис обработки заказов и блюд
# База данных
```
spring.datasource.url=jdbc:h2:file:~/data/users;AUTO_SERVER=TRUE
spring.datasource.username=sa
spring.datasource.password=password
```
## Сначала необходимо запускать сервис авторизации, затем заказов
# Архитектура
## Авторизация
### Клиент отправляет запрос на сервер. Программа обрабатывает его и создает JWT-токен, который возвращает обратно. Затем пользователь получает доступ к ресторану.
![image](https://github.com/Jaklbela/RestaurantMicroservices/assets/60582691/af3324cf-e8a3-4cd8-834e-d13446f70f71)
## Ресторан
### Клиент отправляет запросы. Программа обрабатывает и возвращает результат. Если был создан заказ, то параллельно он меняет свой статус в течение некоторого времени.
![image](https://github.com/Jaklbela/RestaurantMicroservices/assets/60582691/3d7d4b84-e7c5-408f-8342-8140bf2d2e15)
