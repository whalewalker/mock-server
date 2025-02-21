-- Insert default roles
INSERT INTO roles (id, name, description) VALUES
    (uuid_generate_v4(), 'ROLE_USER', 'Standard user role'),
    (uuid_generate_v4(), 'ROLE_ADMIN', 'Administrator role'),
    (uuid_generate_v4(), 'ROLE_SERVICE', 'Service role');

-- Insert default permissions
INSERT INTO permissions (id, name, description) VALUES
    (uuid_generate_v4(), 'READ_USER', 'Permission to read user data'),
    (uuid_generate_v4(), 'WRITE_USER', 'Permission to write user data'),
    (uuid_generate_v4(), 'DELETE_USER', 'Permission to delete user data'),
    (uuid_generate_v4(), 'READ_SERVICE', 'Permission to read service data'),
    (uuid_generate_v4(), 'WRITE_SERVICE', 'Permission to write service data'),
    (uuid_generate_v4(), 'DELETE_SERVICE', 'Permission to delete service data'),
    (uuid_generate_v4(), 'MANAGE_ROLES', 'Permission to manage roles'),
    (uuid_generate_v4(), 'MANAGE_PERMISSIONS', 'Permission to manage permissions');