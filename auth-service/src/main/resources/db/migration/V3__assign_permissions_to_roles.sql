-- Assign READ_USER, WRITE_USER, and DELETE_USER permissions to ROLE_USER
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'ROLE_USER' AND p.name IN ('READ_USER', 'WRITE_USER', 'DELETE_USER');

-- Assign READ_SERVICE, WRITE_SERVICE, MANAGE_ROLES, and MANAGE_PERMISSIONS permissions to ROLE_ADMIN
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'ROLE_ADMIN' AND p.name IN ('READ_SERVICE', 'WRITE_SERVICE', 'MANAGE_ROLES', 'MANAGE_PERMISSIONS');