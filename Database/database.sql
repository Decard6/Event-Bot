create table Characters (
CharID int unsigned auto_increment primary key,
Discord varchar(32) not null unique,
CharName varchar(12) not null unique,
ClassName varchar(20) not null,
Spec varchar(32)
);

create table Events (
EventID int unsigned auto_increment primary key,
EventName varchar(64) not null,
EventDate date not null,
TextChannel VARCHAR(64) not null,
MessageID bigint
);

create table SignUps (
SignUpID bigint unsigned auto_increment primary key,
EventID int unsigned not null,
CharID int unsigned not null,
SignUpStatus tinyint default 0,
ConfirmStatus tinyint default 0,
foreign key (EventID)
	references Events (EventID)
	on update cascade on delete cascade,
foreign key (CharID)
	references Characters (CharID)
	on update cascade on delete cascade
);
