create table Characters (
DiscordID bigint not null unique,
CharName varchar(12) not null unique primary key,
ClassName varchar(20) not null,
Spec varchar(32)
);

create table Events (
EventID int unsigned auto_increment primary key,
EventName varchar(64) not null,
EventDate date not null,
ChannelID bigint not null,
MessageID bigint
);

create table SignUps (
EventID int unsigned not null,
CharName varchar(12) not null,
SignUpStatus tinyint default 0,
ConfirmStatus tinyint default 0,
foreign key (EventID)
	references Events (EventID)
	on update cascade on delete cascade,
foreign key (CharName)
	references Characters (CharName)
	on update cascade on delete cascade,
primary key(EventID, CharName)
);

create view EventView
as
select
	EventID,
	CharName,
	ClassName,
	Spec,
	SignUpStatus,
	ConfirmStatus
from
	events
natural join
	characters;
