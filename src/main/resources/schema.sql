CREATE TABLE Tree (
    id INT AUTO_INCREMENT PRIMARY KEY,
    parent_id INT,
    name VARCHAR(MAX) NOT NULL,
    FOREIGN KEY (parent_id) references Tree(id) ON DELETE CASCADE,
    UNIQUE KEY tree_name (name)
);