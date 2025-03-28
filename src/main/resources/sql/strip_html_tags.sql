CREATE OR REPLACE FUNCTION strip_html_tags(input TEXT) RETURNS TEXT AS $$
BEGIN
RETURN regexp_replace(input, '<[^>]+>', '', 'g');
END;
$$ LANGUAGE plpgsql;