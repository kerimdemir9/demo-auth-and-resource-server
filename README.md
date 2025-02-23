
# Demo Auth and Resource Server
- Implemented a Spring Authorization Server with a matching Resource Server that gets its JWT tokens from the Auth Server 
- Redis is integrated for demo purposes into the book controller
- Chatting feature is also added using Websocket where users need to send JWT tokens obtained from the Auth Server while connecting to the socket for the first time and sending any messages
- Users need to have Admin role to be able send broadcast messages
- Sample endpoints are created to test role based auth inside helloController

## API Usage

#### Login

```http
  GET http://localhost:9000/api/login
```

| Body       | Type     | Value              |
| :--------  | :------- | :------------------------- |
| `username` | `string` | admin or user 
| `password` | `string` | admin or user 


#### Register
```http
  GET http://localhost:9000/api/register
```

| Body       | Type     | Value              |
| :--------  | :------- | :------------------------- |
| `username` | `string` | admin or user 
| `password` | `string` | admin or user 
| `role` | `string` | ["ADMIN", "USER"] or ["USER"]


#### Test Admin Role

```http
  GET http://localhost:8888/hello-admin
```
| Header            | Value              |
| :--------         | :----------------- |
| `Authorization`   | `Bearer JWT_TOKEN` |  

#### Test User Role

```http
 GET http://localhost:8888/hello-auth
```
| Header            | Value              |
| :--------         | :----------------- |
| `Authorization`   | `Bearer JWT_TOKEN` |  

#### Public URL

```http
 GET http://localhost:8888/
```

## Chatting Feature (with WebSockets)

#### Connect To Socket (token required)
```http
http://localhost:8888/chat
```

#### Subscribe To Own Queue
```http
'/user/${username}/queue/private'
```

#### Subscribe To Broadcast Channel
```http
'/topic/public'
```

#### Send Private Message (token required)

```http
'/app/private'
```


#### Send Broadcast Message (token required/only for admin)

```http
'/app/broadcast'
```
## DDL Script For Database Creation

   ```sql
   create table books
(
    id     int auto_increment primary key,
    name   varchar(255) null,
    author varchar(255) null
);

create table user
(
    id            int auto_increment primary key,
    username      varchar(255)                        not null unique,
    password      varchar(255)                        not null,
    refresh_token text                                null,
    role          json                                not null,
    created       timestamp default CURRENT_TIMESTAMP null
);

create table messages
(
    id           int auto_increment primary key,
    message_from varchar(255) not null,
    message_to   varchar(255) null,
    text         varchar(255) not null,
    seen         tinyint(1)   not null,
    created      datetime     not null,
    type         varchar(255) not null,
    constraint messages_ibfk_1
        foreign key (message_from) references user (username)
            on delete cascade,
    constraint messages_ibfk_2
        foreign key (message_to) references user (username)
            on delete cascade
);

   ```
