-- Inserts para TarifaDistancia
-- Si ya existe (por id), actualiza los datos
INSERT INTO tarifa_distancia (id, distancia, coste, activa) VALUES
(1, 'CIUDAD', 50.0, true),
(2, 'PROVINCIAL', 100.0, true),
(3, 'NACIONAL', 200.0, true),
(4, 'INTERNACIONAL', 500.0, true)
ON CONFLICT (id) DO UPDATE SET
    distancia = EXCLUDED.distancia,
    coste = EXCLUDED.coste,
    activa = EXCLUDED.activa;

-- Inserts para TarifaRangoPeso
-- Si ya existe (por id), actualiza los datos
INSERT INTO tarifa_rango_peso (id, peso_minimo, peso_maximo, coste, descripcion, activa) VALUES
(1, 0, 10, 10.0, 'menor de 10kg', true),
(2, 10, 20, 20.0, '10kg - 20kg', true),
(3, 20, 40, 35.0, '20kg - 40kg', true),
(4, 40, 500, 50.0, 'mayor de 40kg', true)
ON CONFLICT (id) DO UPDATE SET
    peso_minimo = EXCLUDED.peso_minimo,
    peso_maximo = EXCLUDED.peso_maximo,
    coste = EXCLUDED.coste,
    descripcion = EXCLUDED.descripcion,
    activa = EXCLUDED.activa;

-- Insert de usuario de ejemplo
-- Si ya existe (id=-1), actualiza los datos
INSERT INTO usuario (id, nombre, apellidos, tipo, correo, telefono, contrasena, fecha_creacion, peso_maximo, activa, apodo, nombre_tienda)
VALUES (-1, 'Usuario', 'Ejemplo', 'CLIENTE', 'usuario1@ejemplo.com', '600123456', 'password123', CURRENT_DATE, 100.0, true, 'usuarioDemo', 'Tienda Demo')
ON CONFLICT (id) DO UPDATE SET
    nombre = EXCLUDED.nombre,
    apellidos = EXCLUDED.apellidos,
    tipo = EXCLUDED.tipo,
    correo = EXCLUDED.correo,
    telefono = EXCLUDED.telefono,
    contrasena = EXCLUDED.contrasena,
    peso_maximo = EXCLUDED.peso_maximo,
    activa = EXCLUDED.activa,
    apodo = EXCLUDED.apodo,
    nombre_tienda = EXCLUDED.nombre_tienda;


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



