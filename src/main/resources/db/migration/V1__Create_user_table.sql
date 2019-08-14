create table user1
(
	id int auto_increment,
	acount_id VARCHAR(100),
	name VARCHAR(50),
	token VARCHAR(36),
	gmt_create BIGINT,
	gmt bigint,
	constraint user_pk
		primary key (id)
);

