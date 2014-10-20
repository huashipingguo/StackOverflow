-- ----------------------------
-- Table structure for lda
-- ----------------------------

create table if not exists document
(	
	id INTEGER,
	name varchar(1000),
	projectName varchar(100)
);


create table if not exists topicToDocument
(
	topicID int not null,
	documentID int,
	documentDistributionValue double,
	projectName varchar(100)
);

create table if not exists word
(	
	id int,
	name varchar(50),
	projectName varchar(100)
);



create table if not exists topicToWord
(
	topicID int not null,
	wordID int,
	wordDistributionValue double,
	projectName varchar(100)
);

-- ----------------------------
-- Table structure for ast
-- ----------------------------

