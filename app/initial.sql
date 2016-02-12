CREATE TABLE messages (
  id      BIGSERIAL     NOT NULL PRIMARY KEY,
  room    VARCHAR(1000) NOT NULL,
  author  VARCHAR(1000) NOT NULL,
  message TEXT          NOT NULL
);

CREATE INDEX messages_room on messages(room);