-- :name get-items :? :*
-- :doc selects all available items
SELECT *
FROM items
ORDER BY sort DESC

-- :name add-item! :! :n
-- :doc creates a new item
INSERT INTO items
(name, sort)
VALUES (
  :name,
  (SELECT MAX(sort) FROM items) + 1
)

-- :name update-item-complete! :! :n
-- :doc updates the item's "complete" field
UPDATE items
SET complete = :complete
WHERE id = :id

-- :name sort-items! :! :n
-- :doc sorts the items and updates the values of the "sort" column
WITH sorted_items AS (
	SELECT id, ROW_NUMBER() OVER (ORDER BY complete DESC, sort ASC) AS new_sort FROM items
) UPDATE items
SET sort = sorted_items.new_sort
FROM sorted_items
WHERE sorted_items.id = items.id