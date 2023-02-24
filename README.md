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
Database name: sql6589571
Database user: sql6589571
Database password: dXiafAiM7S
Port number: 3306

# create table (run sql query)
create table sql6589571.users (
    id_user int(10) NOT NULL AUTO_INCREMENT,
    name varchar(50) not null,
    user_name varchar(50) not null,
    password varchar(50) not null,
    primary key (id_user)
);

create table sql6589571.rooms (
    id_room int(10) NOT NULL AUTO_INCREMENT,
    name varchar(50) not null,
    ip varchar(50) not null,
    port varchar(50) not null,
    code_room varchar(50) not null,
    id_user_admin int not null,
    primary key (id_room)
);

create table sql6589571.user_room (
    id int(10) NOT NULL AUTO_INCREMENT,
    id_user int not null,
    id_room int not null,
    primary key (id)
);


ALTER TABLE sql6589571.users CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci;
ALTER TABLE sql6589571.rooms CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci;
ALTER TABLE sql6589571.user_room CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci;
```

### Project report:
[Báo cáo đồ án](./do-an/bao-cao-do-an-co-so-4.pdf)

### Tham khảo:
```bash
# Tham khảo cách truyền dữ liệu client-server, chat, voice:
https://github.com/hachihao792001/ChatApplication
```

## Dev: Phan Văn Bằng
###### Facebook: fb.com/it0902
###### Github: github.com/pvbang
###### Email: pvbang23092002@gmail.com

