CREATE TABLE items
(id INTEGER PRIMARY KEY AUTOINCREMENT,
 name VARCHAR(30) NOT NULL,
 status VARCHAR(10) DEFAULT 'open',
 timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP);