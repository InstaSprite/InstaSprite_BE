ALTER TABLE member
    ADD COLUMN IF NOT EXISTS member_email_verified BOOLEAN NOT NULL DEFAULT FALSE;

CREATE TABLE IF NOT EXISTS email_verification_token (
    email_verification_token_id BIGSERIAL PRIMARY KEY,
    member_id BIGINT NOT NULL,
    token_hash VARCHAR(64) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    used_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_email_verification_token_member
        FOREIGN KEY (member_id) REFERENCES member (member_id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_email_verification_token_hash
    ON email_verification_token(token_hash);

CREATE INDEX IF NOT EXISTS ix_email_verification_token_member_id
    ON email_verification_token(member_id);


