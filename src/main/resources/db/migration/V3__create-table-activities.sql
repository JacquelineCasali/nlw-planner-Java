
CREATE TABLE activities (
    id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    occurs_at TIMESTAMP NOT NULL,
   trip_id int not null,
 foreign key (trip_id) references trips (id)
)Engine=InnoDB;