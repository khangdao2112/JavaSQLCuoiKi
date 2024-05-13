CREATE DATABASE javasqlcuoiki;

create table courses
(
    course_id          int auto_increment
        primary key,
    course_name        varchar(100) not null,
    course_description text         not null,
    instructor         varchar(50)  not null,
    department         varchar(50)  not null
);

create table students
(
    student_id     int auto_increment
        primary key,
    last_name      varchar(50)  not null,
    middle_name    varchar(50)  null,
    first_name     varchar(50)  not null,
    gender         char         not null,
    date_of_birth  date         not null,
    birthplace     varchar(128) not null,
    contact_number varchar(20)  not null,
    email          varchar(100) not null,
    address        varchar(255) not null,
    constraint email
        unique (email)
);

create table enrollments
(
    student_id int              not null,
    course_id  int              not null,
    credits    tinyint unsigned null,
    grade      float            null,
    primary key (student_id, course_id),
    constraint enrollments_ibfk_1
        foreign key (student_id) references students (student_id),
    constraint enrollments_ibfk_2
        foreign key (course_id) references courses (course_id)
);

create index course_id
    on enrollments (course_id);

create table courses
(
    course_id          int auto_increment
        primary key,
    course_name        varchar(100) not null,
    course_description text         not null,
    instructor         varchar(50)  not null,
    department         varchar(50)  not null
);

create table enrollments
(
    student_id int              not null,
    course_id  int              not null,
    credits    tinyint unsigned null,
    grade      float            null,
    primary key (student_id, course_id),
    constraint enrollments_ibfk_1
        foreign key (student_id) references students (student_id),
    constraint enrollments_ibfk_2
        foreign key (course_id) references courses (course_id)
);

create index course_id
    on enrollments (course_id);

create table students
(
    student_id     int auto_increment
        primary key,
    last_name      varchar(50)  not null,
    middle_name    varchar(50)  null,
    first_name     varchar(50)  not null,
    gender         char         not null,
    date_of_birth  date         not null,
    birthplace     varchar(128) not null,
    contact_number varchar(20)  not null,
    email          varchar(100) not null,
    address        varchar(255) not null,
    constraint email
        unique (email)
);
