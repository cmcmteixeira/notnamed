CREATE TABLE members (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  group_id BIGINT REFERENCES groups(id),
  createdOn LONG
);