CREATE TABLE users(
 id BINARY(16) UNIQUE,
 email VARCHAR(128) NOT NULL UNIQUE,
 createdOn TIMESTAMP,
 updatedOn TIMESTAMP,
 deletedOn TIMESTAMP NULL
);