ALTER TABLE member
  ADD COLUMN member_email VARCHAR(255),
  ADD COLUMN member_google_id VARCHAR(255),
  DROP COLUMN IF EXISTS member_website,
    DROP COLUMN IF EXISTS member_contact,
DROP COLUMN IF EXISTS member_gender;

