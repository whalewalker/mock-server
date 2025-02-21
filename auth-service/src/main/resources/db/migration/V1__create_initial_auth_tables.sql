-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp"; -- This is required to generate UUIDs

CREATE TABLE IF NOT EXISTS users (
        id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
        name VARCHAR(100) NOT NULL,
        email VARCHAR(100) UNIQUE NOT NULL,
        password VARCHAR(255),
        provider VARCHAR(20) NOT NULL,
        provider_id VARCHAR(100),
        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

    CREATE TABLE IF NOT EXISTS roles (
        id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
        name VARCHAR(20) UNIQUE NOT NULL,
        description VARCHAR(255)
    );

    CREATE TABLE IF NOT EXISTS permissions (
        id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
        name VARCHAR(20) UNIQUE NOT NULL,
        description VARCHAR(255)
    );

    CREATE TABLE IF NOT EXISTS user_roles (
        user_id UUID REFERENCES users(id),
        role_id UUID REFERENCES roles(id),
        PRIMARY KEY (user_id, role_id)
    );

    CREATE TABLE IF NOT EXISTS role_permissions (
        role_id UUID REFERENCES roles(id),
        permission_id UUID REFERENCES permissions(id),
        PRIMARY KEY (role_id, permission_id)
    );

    CREATE TABLE IF NOT EXISTS refresh_tokens (
        id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
        user_id UUID REFERENCES users(id),
        token VARCHAR(255) UNIQUE NOT NULL,
        expires_at TIMESTAMP NOT NULL,
        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );