# --- !Ups

CREATE TABLE Roles (
    id int NOT NULL AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO Roles(name) VALUES ('simple'),('admin');

CREATE TABLE Users (
    id int NOT NULL AUTO_INCREMENT,
    login varchar(255) NOT NULL UNIQUE,
    password varchar(255) NOT NULL,
    ranking int NOT NULL,
    totalPoints int NOT NULL,
    roleId int NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (roleId) REFERENCES Roles(id)
);

INSERT INTO Users(login, password, ranking, totalPoints, roleId) VALUES ('admin', '21232f297a57a5a743894a0e4a801fc3', 1, 100000, 2);
INSERT INTO Users(login, password, ranking, totalPoints, roleId) VALUES ('nojas', '21232f297a57a5a743894a0e4a801fc3', 2, 100000, 2);

# --- !Downs

DROP TABLE Users;

DROP TABLE Roles;