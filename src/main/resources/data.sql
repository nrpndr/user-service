-- add admin user
-- user password is test@123
INSERT IGNORE INTO `db_user`.`users` (
	id, 
	username, 
	password, 
	firstname, 
	lastname, 
	email, 
	userrole
	)
VALUES (
	1, 
	'nrpndr',
	'8622f0f69c91819119a8acf60a248d7b36fdb7ccf857ba8f85cf7f2767ff8265',
	'Nripendra',
	'Thakur',
	'nrpndr@yopmail.com',
	'ADMIN'
	);
	
-- add one normal user
-- user password is test@123
INSERT IGNORE INTO `db_user`.`users` (
	id, 
	username, 
	password, 
	firstname, 
	lastname, 
	email, 
	userrole
	)
VALUES (
	2, 
	'nrpndr2',
	'8622f0f69c91819119a8acf60a248d7b36fdb7ccf857ba8f85cf7f2767ff8265',
	'Nripendra',
	'Thakur',
	'nrpndr2@yopmail.com',
	'USER'
	);
