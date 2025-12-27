UPDATE member
SET member_email_verified = TRUE
WHERE member_provider = 'GOOGLE'
  AND member_email_verified = FALSE;


