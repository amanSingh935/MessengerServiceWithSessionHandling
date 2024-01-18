# MessengerService
## How to run?
1. Clone the repository.
2. Go into MessengerService: `cd MessengerService`
3. Make sure you have docker installed: https://docs.docker.com/desktop/install/linux-install/
4. Run: `docker compose build && docker compose up`

## Session Handling and Authentication:
I've used JWT to handle sessions and authenticate user requests. An initial JWT token is generated each time requested from user. 
The user can perform messaging operations with help of this token. Later, the user can logout by calling logout endpoint and the token is no longer valid.

## API Endpoints and sample requests:
### User create endpoint: 
```
curl --location 'localhost:8080/users/create' --header 'Accept: application/json' --header 'Content-Type: application/json' --data '{
    "username":"newUser8",
    "password":"pwd8"
}'
```

### List all users:
```
curl --location 'localhost:8080/users/all'
```

### User login:
```
curl --location 'localhost:8080/login' --header 'Content-Type: application/json' --header --data '
{
    "username":"newUser8",
    "password":"pwd8"
}'
```

### Publish Message:
```
curl --location 'localhost:8080/user/USER_NAME/message' --header 'Content-Type: application/json' --header 'Authorization: Bearer JWT_TOKEN_RETURNED' --data '{
    "to":"newUser3",
    "text":"Initial Message 234"
}'
```

### Get Unread Messages:
#### Get All Unread Messages for user(and sent messages[for displaying chat dialog])
```
curl --location 'localhost:8080/user/USER_NAME/message?filterReadMessages=0' --header 'Authorization: Bearer JWT_TOKEN_RETURNED'
```

- Toggle filterReadMessages RequestParam to 0 or 1 to filter read Messages
- When filterReadMessages = 1, only unread messages are returned
- When filterReadMessages = 0, all messages, read or unread are returned.

#### Get all unread conversations with user and their friend
Replace `FRIENDS_USER_NAME` with user for which conversation of user is needed.
```
curl --location 'localhost:8080/user/USER_NAME/message?friend=FRIENDS_USER_NAME' --header 'Authorization: Bearer JWT_TOKEN_RETURNED'
```

### User Logout:
Replace USER_NAME in below request to any saved user and JWT token as returned during login.

Please note: the JWT token has an expiry timer already that will timeout if the token is used for long. We can use this API to manually invalidate any token.
This is acheived by saving the token and refreshing it in the DB everytime a new token is generated.

```
curl --location --request POST 'localhost:8080/logout/USER_NAME' --header 'Authorization: Bearer JWT_TOKEN_RETURNED' --data ''
```
