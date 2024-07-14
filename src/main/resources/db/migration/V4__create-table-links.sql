CREATE TABLE links (
    id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    url VARCHAR(255) NOT NULL,
   trip_id int not null,
 foreign key (trip_id) references trips (id)
)Engine=InnoDB;