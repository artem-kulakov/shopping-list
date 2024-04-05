-- :name save-message! :! :n
-- :doc creates a new message
INSERT INTO guestbook
(name, message)
VALUES (:name, :message)

-- :name get-messages :? :*
-- :doc selects all available messages
SELECT * FROM guestbook





-- :name get-items :? :*
-- :doc selects all available items
SELECT * FROM items

-- :name add-item! :! :n
-- :doc creates a new item
INSERT INTO items
(name)
VALUES (:name)

-- :name update-item-status! :! :n
-- :doc updates the item status
UPDATE items
SET status = :status
WHERE id = :id
