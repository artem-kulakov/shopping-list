ALTER TABLE items
ADD COLUMN sort REAL;
--;;
UPDATE items SET sort = id;