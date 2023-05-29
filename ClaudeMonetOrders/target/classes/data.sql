CREATE TABLE user_data
(
    user_id       INT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(50) UNIQUE  NOT NULL,
    email         VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255)        NOT NULL,
    role          VARCHAR(10)         NOT NULL CHECK (role IN ('customer', 'chef', 'manager')),
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP()
);

CREATE TABLE session
(
    session_id    INT AUTO_INCREMENT PRIMARY KEY,
    user_id       INT          NOT NULL,
    session_token VARCHAR(255) NOT NULL,
    expires_at    TIMESTAMP    NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user_data (user_id)
);

CREATE TABLE dish
(
    dish_id      INT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(100)   NOT NULL,
    description  TEXT,
    price        DECIMAL(10, 2) NOT NULL,
    quantity     INT            NOT NULL,
    is_available BOOLEAN        NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP()
);

CREATE TABLE order_data
(
    order_id         INT AUTO_INCREMENT PRIMARY KEY,
    user_id          INT         NOT NULL,
    status           VARCHAR(50) NOT NULL,
    special_requests TEXT,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP(),
    FOREIGN KEY (user_id) REFERENCES user_data (user_id)
);

CREATE TABLE order_dish
(
    order_dish_id       INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT            NOT NULL,
    dish_id  INT            NOT NULL,
    quantity INT            NOT NULL,
    price    DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES order_data (order_id),
    FOREIGN KEY (dish_id) REFERENCES dish (dish_id)
);




