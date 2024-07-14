SET sql_mode = '';
create table trips (
   id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
    destination VARCHAR(255) NOT NULL,
    starts_at TIMESTAMP NOT NULL,
    ends_at TIMESTAMP not null,
    is_confirmed BOOLEAN NOT NULL,
    owner_name VARCHAR(255)  UNIQUE NOT NULL,
    owner_email VARCHAR(255)  UNIQUE  NOT NULL
)Engine=InnoDB;
