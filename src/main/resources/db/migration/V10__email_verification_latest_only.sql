WITH ranked AS (
    SELECT
        email_verification_token_id,
        ROW_NUMBER() OVER (
            PARTITION BY member_id
            ORDER BY created_at DESC, email_verification_token_id DESC
        ) AS rn
    FROM email_verification_token
    WHERE used_at IS NULL
)
DELETE FROM email_verification_token t
USING ranked r
WHERE t.email_verification_token_id = r.email_verification_token_id
  AND r.rn > 1;

CREATE UNIQUE INDEX IF NOT EXISTS ux_email_verification_token_member_active
    ON email_verification_token(member_id)
    WHERE used_at IS NULL;

