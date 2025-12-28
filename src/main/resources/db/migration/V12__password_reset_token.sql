CREATE TABLE IF NOT EXISTS password_reset_token (
    password_reset_token_id BIGSERIAL PRIMARY KEY,
    member_id BIGINT NOT NULL,
    token_hash VARCHAR(64) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    used_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_password_reset_token_member
        FOREIGN KEY (member_id) REFERENCES member (member_id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_password_reset_token_hash
    ON password_reset_token(token_hash);

CREATE INDEX IF NOT EXISTS ix_password_reset_token_member_id
    ON password_reset_token(member_id);

