ALTER TABLE follows
DROP CONSTRAINT IF EXISTS follows_no_self_follow_chk;

ALTER TABLE follows
ADD CONSTRAINT follows_no_self_follow_chk
CHECK (follower_member_id IS NULL OR member_id IS NULL OR follower_member_id <> member_id);

DROP TRIGGER IF EXISTS saved_post_no_self_bookmark ON saved_post;
DROP FUNCTION IF EXISTS prevent_self_bookmark CASCADE;

CREATE OR REPLACE FUNCTION prevent_self_bookmark()
RETURNS trigger AS $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM posts p
        WHERE p.post_id = NEW.post_id
          AND p.member_id = NEW.member_id
    ) THEN
        RAISE EXCEPTION 'Cannot bookmark own post';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE CONSTRAINT TRIGGER saved_post_no_self_bookmark
AFTER INSERT OR UPDATE ON saved_post
DEFERRABLE INITIALLY IMMEDIATE
FOR EACH ROW
EXECUTE FUNCTION prevent_self_bookmark();

