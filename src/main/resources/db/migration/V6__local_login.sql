
ALTER TABLE member
    ADD COLUMN IF NOT EXISTS member_provider VARCHAR(20) DEFAULT 'GOOGLE';


UPDATE member
SET member_provider = 'GOOGLE'
WHERE member_provider IS NULL
  AND member_google_id IS NOT NULL;

UPDATE member
SET member_provider = 'LOCAL'
WHERE member_provider IS NULL
  AND member_google_id IS NULL;


ALTER TABLE member
    ALTER COLUMN member_provider SET NOT NULL;


ALTER TABLE member
    ADD CONSTRAINT member_provider_chk
    CHECK (member_provider IN ('GOOGLE', 'LOCAL'));


CREATE UNIQUE INDEX IF NOT EXISTS ux_member_username ON member(member_username);
CREATE UNIQUE INDEX IF NOT EXISTS ux_member_email ON member(member_email);
CREATE UNIQUE INDEX IF NOT EXISTS ux_member_google_id ON member(member_google_id);

ALTER TABLE member
    ADD CONSTRAINT member_local_auth_req_chk
    CHECK (
        member_provider <> 'LOCAL'
        OR (member_email IS NOT NULL AND member_password IS NOT NULL)
    );

