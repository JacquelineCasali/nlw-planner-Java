CREATE TABLE participants (
id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name  VARCHAR(255)  NOT NULL,
    email  VARCHAR(255)  NOT NULL,
    is_confirmed BOOLEAN NOT NULL,
   trip_id int not null,
   foreign key (trip_id) references trips (id)
)Engine=InnoDB;


