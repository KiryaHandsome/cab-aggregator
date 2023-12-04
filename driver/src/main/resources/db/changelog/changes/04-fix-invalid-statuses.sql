UPDATE drivers
SET status = '1'
WHERE status NOT IN ('0', '1', '2');
