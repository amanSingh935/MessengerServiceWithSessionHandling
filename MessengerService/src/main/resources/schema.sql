CREATE TABLE IF NOT EXISTS users(
    `username` varchar(255),
    `passwd` varchar(255),
    `email` varchar(300) DEFAULT NULL,
    PRIMARY KEY (`username`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--CREATE TABLE IF NOT EXISTS messages(
--        `id` INT(10) AUTO_INCREMENT PRIMARY_KEY,
--        `userFrom` varchar(255) NOT NULL,
--        `userTo` varchar(255) NOT NULL,
--        `data` varchar(600) DEFAULT NULL,
--        `isRead` boolean DEFAULT FALSE,
--        `createdTime` datetime NOT NULL,
--)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

