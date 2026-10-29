-- 1. USERS
CREATE TABLE users
(
    id            UUID PRIMARY KEY DEFAULT Gen_random_uuid(),
    email         VARCHAR(255) NOT NULL,
    username      VARCHAR(50) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    status        VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at    TIMESTAMPTZ NOT NULL DEFAULT current_timestamp,
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT current_timestamp,
    CONSTRAINT uq_users_username UNIQUE (username),
    CONSTRAINT chk_users_status CHECK (status IN ('ACTIVE', 'DISABLED',
                                                  'LOCKED')),
    CONSTRAINT chk_users_email_not_blank CHECK (Length(Btrim(email)) > 0),
    CONSTRAINT chk_users_username_not_blank CHECK (Length(Btrim(username)) > 0)
    ,
    CONSTRAINT chk_users_password_hash_not_blank CHECK (Length(Btrim(
            password_hash)) > 0)
);

CREATE UNIQUE INDEX uq_users_email_lower
    ON users (Lower(email));

-- 2. USER ROLES
CREATE TABLE user_roles
(
    user_id UUID NOT NULL,
    ROLE    VARCHAR(20) NOT NULL,
    CONSTRAINT pk_user_roles PRIMARY KEY (user_id, ROLE),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id) ON
        DELETE CASCADE,
    CONSTRAINT chk_user_roles_role CHECK (ROLE IN ('USER', 'ADMIN'))
);

-- 3. REFRESH TOKENS
CREATE TABLE refresh_tokens
(
    id         UUID PRIMARY KEY DEFAULT Gen_random_uuid(),
    user_id    UUID NOT NULL,
    token_hash CHAR(64) NOT NULL UNIQUE,
    expires_at TIMESTAMPTZ NOT NULL,
    revoked_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT current_timestamp,
    CONSTRAINT fk_refresh_tokens_user FOREIGN KEY (user_id) REFERENCES users(id
        ) ON DELETE CASCADE,
    CONSTRAINT chk_refresh_token_hash CHECK (token_hash ~ '^[0-9a-f]{64}$'),
     CONSTRAINT chk_refresh_token_expiry CHECK (expires_at > created_at)
  );

CREATE INDEX idx_refresh_tokens_user_id
    ON refresh_tokens(user_id);