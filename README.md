## Meeting Application - Java (Client - Server)

### Run:
```bash
# run Java Application:
Meeting
```

### Database:
```bash
# Free SQL Database: https://www.freesqldatabase.com
# Database: https://www.phpmyadmin.co
Host: sql6.freesqldatabase.com
Database name: sql6581708
Database user: sql6581708
Database password: lfvceV4iVW
Port number: 3306

# create table (run sql query)
create table sql6581708.users (
    id int(10) NOT NULL AUTO_INCREMENT,
    name varchar(50) not null,
    user_name varchar(50) not null,
    password varchar(50) not null,
    primary key (id)
);

create table sql6581708.rooms (
    id int(10) NOT NULL AUTO_INCREMENT,
    name varchar(50) not null,
    ip varchar(50) not null,
    port varchar(50) not null,
    code_room varchar(50) not null,
    id_user int not null,
    primary key (id)
);

create table sql6581708.user_room (
    id int(10) NOT NULL AUTO_INCREMENT,
    id_user int not null,
    id_room int not null,
    primary key (id)
);

ALTER DATABASE sql6581708 CHARACTER SET utf8 COLLATE utf8_unicode_ci;
ALTER TABLE sql6581708.users CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci;
ALTER TABLE sql6581708.rooms CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci;
ALTER TABLE sql6581708.user_room CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci;
```

## Dev: Phan Văn Bằng
###### Facebook: fb.com/it0902
###### Github: github.com/ilyouu
###### Email: pvbang23092002@gmail.com

