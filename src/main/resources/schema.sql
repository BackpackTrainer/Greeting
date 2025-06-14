DROP TABLE IF EXISTS greeting;

CREATE TABLE greeting (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) UNIQUE,
                          message VARCHAR(255)
);
