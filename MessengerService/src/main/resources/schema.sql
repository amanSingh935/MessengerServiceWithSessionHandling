CREATE TABLE IF NOT EXISTS users(
    `username` varchar(255),
    `passwd` varchar(255),
    `email` varchar(300) DEFAULT NULL,
    PRIMARY KEY (`username`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
