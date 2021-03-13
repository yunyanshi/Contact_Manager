CREATE TABLE IF NOT EXISTS Contacts(
user_id INT,
name VARCHAR(100),
phone_number  CHAR(10),
email VARCHAR(50),
birthday DATE,
address VARCHAR(200),
notes VARCHAR(200),
PRIMARY KEY (user_id)
);

insert into contacts values 
(1, 'John, Mayers', '8563752365', 'John856@gmail.com', '1991-02-11', '6481 US-212, Red Lodge, MT 59068', null), 
(2, 'William, Smith', '6503651134', 'Will650@gmail.com', '2005-06-16', '3757 Independence Ave, Kansas City, MO 64124', null), 
(3, 'James, Lincoln', '3425643287', 'Jame342@gmail.com', '1976-06-21', '1703 W 9th St, Kansas City, MO 64101', null),
(4, 'George, Monroe', '8678003785', 'Geog867@gmal.com', '199-7-31', '310 N Washington St, Seymour, TX 76380', null),
(5, 'Charles, Mandela', '7563977702', 'Charles756@gmail.com', '1967-06-13', '715 S Main St, Seymour, TX 76380', null),
(6, 'Frank, Churchill', '3546870926', 'Frank354@gmail.com', '1971-05-21', '701 NE Sanchez Ave, Ocala, FL 34470', null),
(7, 'Joseph, Ali', '8567511872', 'Joseph856@gmail.com', '2001-02-16', '2223 N Westshore Blvd, Tampa, FL 33607', null),
(8, 'Henry, Teersa', '3760789652', 'Henry376@gmail.com', '981-02-24', '281 W Lane Ave, Columbus, OH 43210', null),
(9, 'Robert, Edison', '3879081437', 'Robert387@gmail.com', '1954-07-21', '1979 W 25th St, Cleveland, OH 44113', null),
(10, 'Thomas, Nehru', '3767650975', 'Thomas376@gmail.com', '1999-02-11', '7300 S Cicero Ave, Chicago, IL 60629', null),
(11, 'Mary, Ford', '8564980765', 'Mary856@gmail.com', '1996-02-24', '4315 Marlton Pike, Pennsauken Township, NJ 08109', null),
(12, 'Anna, WaterDrink', '7443750170', 'Anna744@gmail.com', '1997-08-21', '1100 Kings Hwy N, Cherry Hill, NJ 08034', null),
(13, 'Emma, Lenin', '9273644490', 'Emma927@gmail.com', '2003-09-12', '20 W 34th St, New York, NY 10001', null),
(14, 'Elizabeth, Bergman', '9073472583', 'Elizabeth907@gmail.com', '1956-10-23', '175 5th Ave, New York, NY 10010', null),
(15, 'Margaret, Castro', '5043750983', 'Margaret504@gmail.com', '987-04-30', '105 Alewife Brook Pkwy, Somerville, MA 02144', null),
(16, 'Ida, Pasteur', '8483008923', 'Ida848@gmail.com', '1949-08-24', '1 College Cir, Bangor, ME 04401', null),
(17, 'Bertha, Hari', '8343772033', 'Bertha834@gmail.com', '1994-09-30', '400 Broad St, Seattle, WA 98109', null),
(18, 'Clara, Kelly', '7653860423', 'Clara765@gmail.com', '1993-10-26', '2855 Stevens Creek Blvd, Santa Clara, CA 95050', null),
(19, 'Alice, Woods', '5863820459', 'Alice586@gmail.com', '2000-11-28', '525 S Winchester Blvd, San Jose, CA 95128', null),
(20, 'Smith, Frank', '2950391857', 'Smith295@gmail.com', '1971-04-22', '739 E 60th St, Los Angeles, CA 90001', null),
(21, 'lil, Lucas', '4820581057', 'lil482@gmail.com', '1985-04-27', '1713 E Vernon Ave Lounge, Los Angeles, CA 90058', null);


select * from contacts;

create table friends(
    f_id varchar(4),
    user_id int, 
    primary key(f_id)
);



insert into friends values 
('f1', 1),
('f2', 5),
('f3', 3),
('f4', 7);



create table favorites(
    fa_id varchar(4),
    user_id int, 
    primary key(fa_id)
);



insert into favorites values 
('fa1', 2),
('fv2', 6),
('fv3', 12),
('fv4', 13),
('fv5', 14),
('fv6', 1),
('fv7', 5),
('fv8', 20);




create table family(
    fv_id varchar(4),
    user_id int, 
    primary key(fv_id)
);

insert into family values 
('fa1', 21),
('fa2', 17),
('fa3', 18),
('fa4', 19),
('fa5', 20);


CREATE TABLE IF NOT EXISTS Contacts(
user_id INT,
name VARCHAR(100),
phone_number  CHAR(10),
email VARCHAR(50),
birthday DATE,
address VARCHAR(200),
notes VARCHAR(200),
PRIMARY KEY (user_id)
);


/**select * from contacts;**/

DELIMITER $$
create function addNewContacts (
    name VARCHAR(100),
    phone_number  CHAR(10),
    email VARCHAR(50),
    birthday DATE,
    address VARCHAR(200),
    notes VARCHAR(200)
)
RETURNS VARCHAR(40) DETERMINISTIC

BEGIN
    declare result varchar(40) default 'duplicate user!';
    declare user_id_var int;
    declare duplicate_user_id int;

    select c.user_id into duplicate_user_id from contacts c where c.name = name 
        and c.phone_number = phone_number and c.email = email and c.birthday = birthday and c.address = address;
    if duplicate_user_id > 0
    then 
        set result = 'duplicate user!';
    else
        select (max(user_id)+1) into user_id_var from contacts;
        insert into contacts values (user_id_var, name, phone_number, email, birthday, address, notes); 
        set result = "New Contacts added successfully!";
    end if; 
    return result;
END $$
DELIMITER ;

select addNewContacts('Ce', '8563750150', 'pcdota123@gmail.com', '1993-09-07', 'sdasdsadasdasdas', null);

select * from contacts;

select addNewContacts('Ce', '8563750150', 'pcdota123@gmail.com', '1993-09-07', 'sdasdsadasdasdas', null);



DELIMITER $$
create function updateExistingContact
(
    user_id int,
    name VARCHAR(100),
    phone_number  CHAR(10),
    email VARCHAR(50),
    birthday DATE,
    address VARCHAR(200),
    notes VARCHAR(200)
)
RETURNS VARCHAR(40) DETERMINISTIC
BEGIN
    declare result varchar(40) default 'updates successfully!';
    declare new_user_id int;
    delete from contacts c where c.user_id = user_id;
    select max(user_id) into new_user_id from contacts;
    insert into contacts values(new_user_id, name, phone_number, email, birthday, address, notes);
    return result;
END $$
DELIMITER ;




select updateExistingContact(22, 'Ce', '8563750150', 'pcdota123@gmail.com', '1993-09-07', 'sdasdsadasdasdas', 'Upates Checking');



