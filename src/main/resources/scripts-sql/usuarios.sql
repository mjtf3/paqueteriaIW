-- Usuarios de prueba para la aplicación de paquetería
-- Contraseñas sin encriptar (para referencia):
-- Admin: admin123
-- Repartidor: repartidor123
-- Tienda: tienda123

-- 1. WEBMASTER (Admin)
INSERT INTO usuario (apodo, nombre, apellidos, tipo, correo, telefono, contrasena, fecha_creacion, activa, nombre_tienda, peso_maximo)
VALUES (
    'SuperAdmin',
    'Carlos',
    'García López',
    'WEBMASTER',
    'admin@paqueteria.com',
    '600111222',
    '$2a$10$eTKikObpwecAKYRGzO1ile6kZcS6HTo2P67BRPpBr10WlURZBWRWq', -- 123
    '2025-01-01',
    true,
    NULL,
    NULL
);

-- 2. REPARTIDOR
INSERT INTO usuario (apodo, nombre, apellidos, tipo, correo, telefono, contrasena, fecha_creacion, activa, nombre_tienda, peso_maximo)
VALUES (
    'Speedy',
    'María',
    'Rodríguez Fernández',
    'REPARTIDOR',
    'repartidor@paqueteria.com',
    '600222333',
    '$2a$10$eTKikObpwecAKYRGzO1ile6kZcS6HTo2P67BRPpBr10WlURZBWRWq', -- 123
    '2025-06-15',
    true,
    NULL,
    50.00
);

-- 3. CLIENTE (Tienda)
INSERT INTO usuario (apodo, nombre, apellidos, tipo, correo, telefono, contrasena, fecha_creacion, activa, nombre_tienda, peso_maximo)
VALUES (
    NULL,
    'Juan',
    'Martínez Sánchez',
    'CLIENTE',
    'tienda@example.com',
    '600333444',
    '$2a$10$eTKikObpwecAKYRGzO1ile6kZcS6HTo2P67BRPpBr10WlURZBWRWq', -- 123
    '2025-09-20',
    true,
    'Electrónica Madrid',
    NULL
);



