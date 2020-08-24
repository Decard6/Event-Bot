create table characters (
	user_id bigint not null unique,
	name varchar(12) not null unique primary key,
	class_name varchar(20) not null,
	spec varchar(32)
);

create table events (
	id int unsigned auto_increment primary key,
	name varchar(64) not null,
	date timestamp	not null,
	channel_id bigint not null,
	message_id bigint
);

create table signups (
	event_id int unsigned not null,
	character_name varchar(12) not null,
	signup_status tinyint default 0,
	confirm_status tinyint default 0,
	foreign key (event_id)
	references events (id)
		on update cascade on delete cascade,
	foreign key (char_name)
	references characters (name)
		on update cascade on delete cascade,
	primary key(event_id, char_name)
);