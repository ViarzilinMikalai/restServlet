CREATE TABLE users (
    id serial primary key,
    username VARCHAR(100),
    password VARCHAR(50)
);


CREATE TABLE post (
    id serial primary key,
    title VARCHAR(255),
    text VARCHAR(2000),
    author INTEGER REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE subscription (
    owner_id INTEGER REFERENCES users (id) ON DELETE CASCADE,
    subscriber_id INTEGER REFERENCES users (id) ON DELETE CASCADE
);