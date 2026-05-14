DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_type t
    JOIN pg_namespace n ON n.oid = t.typnamespace
    WHERE t.typname = 'meal_type_enum' AND n.nspname = 'public'
  ) THEN
    CREATE TYPE public.meal_type_enum AS ENUM ('BREAKFAST','LUNCH','DINNER','SNACK');
  END IF;
END
$$;
