CREATE TABLE message (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, name VARCHAR(256),
description VARCHAR(256));
CREATE TABLE userDto (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, username VARCHAR(256) NOT NULL,
password VARCHAR(256) NOT NULL, name VARCHAR(256) , surname VARCHAR(256) , address VARCHAR(256),
security_pin VARCHAR(256) , gender VARCHAR(256), postal_code INT)