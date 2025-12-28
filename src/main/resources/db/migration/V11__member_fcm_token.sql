CREATE TABLE IF NOT EXISTS member_fcm_token (
    member_fcm_token_id BIGSERIAL PRIMARY KEY,
    member_id BIGINT NOT NULL REFERENCES member (member_id) ON DELETE CASCADE,
    token TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_member_fcm_token_member_id_token
    ON member_fcm_token (member_id, token);

CREATE INDEX IF NOT EXISTS idx_member_fcm_token_member_id
    ON member_fcm_token (member_id);


