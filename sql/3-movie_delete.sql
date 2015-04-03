DELIMITER $$
CREATE PROCEDURE add_deleted_to_movies()
BEGIN
    DECLARE _count INT;
    SET _count = (  SELECT COUNT(*) 
                    FROM INFORMATION_SCHEMA.COLUMNS
                    WHERE   TABLE_NAME = 'movies' AND 
                            COLUMN_NAME = 'deleted');
    IF _count = 0 THEN
        ALTER TABLE movies
            ADD COLUMN deleted TINYINT(1) DEFAULT 0;
    END IF;
END $$
DELIMITER ;

USE towatchlist;
CALL add_deleted_to_movies();

USE towatchlist_dev;
CALL add_deleted_to_movies();