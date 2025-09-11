-- Tabla "role"
CREATE TABLE IF NOT EXISTS public."role" (
    "id" serial PRIMARY KEY,
    "name" varchar(100) NOT NULL UNIQUE,
    "description" text
);

-- Tabla "users"
CREATE TABLE IF NOT EXISTS public."users" (
    "id" bigserial PRIMARY KEY,
    "first_name" varchar(100) NOT NULL,
    "last_name" varchar(100) NOT NULL,
    "email" varchar(150) NOT NULL UNIQUE,
    "identity_document" varchar(50) NOT NULL,
    "phone" varchar(20),
    "birth_date" date,
    "base_salary" numeric(12, 2),
    "role_id" integer NOT NULL,
    "address" varchar(200),
    "password" varchar(255) NOT NULL
);

-- Índices
CREATE INDEX IF NOT EXISTS idx_users_email ON public."users"("email");

-- Clave foránea
ALTER TABLE public."users"
    ADD CONSTRAINT fk_user_role
    FOREIGN KEY ("role_id")
    REFERENCES public."role"("id");


-- Crear roles por defecto sólo si no existen
INSERT INTO public."role" ("id", "name", "description") VALUES
  (1, 'ADMINISTRATOR', 'Has full access to the system'),
  (2, 'CUSTOMER', 'End customer account'),
  (3, 'ANALYST', 'Performs data and business analysis'),
  (4, 'MANAGER', 'Manages teams and approvals'),
  (5, 'SUPPORT', 'Customer support and operations')
ON CONFLICT ("id") DO NOTHING;

-- Crear usuarios de ejemplo sólo si no existen (por id único)
INSERT INTO public."users" (
    "id", "first_name", "last_name", "email", "identity_document", "phone", "birth_date", "base_salary", "role_id", "address", "password"
) VALUES
  (1, 'Admin', 'System', 'admin@example.com', '00000001', '1234567890', '1990-01-01', 5000.00, 1, 'HQ', '$2a$10$gH1k1FcCbXkEKXe4eB31k.EiB0BjMQEwKbZM70ztUhq48HniZQQ6S'),
  (2, 'Customer', 'Demo', 'customer@example.com', '00000002', '1234567891', '1991-02-02', 100.00, 2, 'Street 2', '$2a$10$gH1k1FcCbXkEKXe4eB31k.EiB0BjMQEwKbZM70ztUhq48HniZQQ6S'),
  (3, 'Analyst', 'Data', 'analyst@example.com', '00000003', '1234567892', '1992-03-03', 4000.00, 3, 'Street 3', '$2a$10$gH1k1FcCbXkEKXe4eB31k.EiB0BjMQEwKbZM70ztUhq48HniZQQ6S'),
  (4, 'Manager', 'Boss', 'manager@example.com', '00000004', '1234567893', '1993-04-04', 6000.00, 4, 'Street 4', '$2a$10$gH1k1FcCbXkEKXe4eB31k.EiB0BjMQEwKbZM70ztUhq48HniZQQ6S'),
  (5, 'Support', 'Help', 'support@example.com', '00000005', '1234567894', '1994-05-05', 200.00, 5, 'Street 5', '$2a$10$gH1k1FcCbXkEKXe4eB31k.EiB0BjMQEwKbZM70ztUhq48HniZQQ6S')
ON CONFLICT ("id") DO NOTHING;

-- Sincronizar la secuencia del ID con el valor máximo actual en la tabla
SELECT setval('public.users_id_seq', (SELECT MAX(id) FROM public.users), true);