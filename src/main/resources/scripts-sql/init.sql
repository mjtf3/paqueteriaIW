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
       )
ON CONFLICT (correo) DO UPDATE SET
    apodo = EXCLUDED.apodo,
    nombre = EXCLUDED.nombre,
    apellidos = EXCLUDED.apellidos,
    tipo = EXCLUDED.tipo,
    telefono = EXCLUDED.telefono,
    contrasena = EXCLUDED.contrasena,
    fecha_creacion = EXCLUDED.fecha_creacion,
    activa = EXCLUDED.activa,
    nombre_tienda = EXCLUDED.nombre_tienda,
    peso_maximo = EXCLUDED.peso_maximo;

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
       )
ON CONFLICT (correo) DO UPDATE SET
    apodo = EXCLUDED.apodo,
    nombre = EXCLUDED.nombre,
    apellidos = EXCLUDED.apellidos,
    tipo = EXCLUDED.tipo,
    telefono = EXCLUDED.telefono,
    contrasena = EXCLUDED.contrasena,
    fecha_creacion = EXCLUDED.fecha_creacion,
    activa = EXCLUDED.activa,
    nombre_tienda = EXCLUDED.nombre_tienda,
    peso_maximo = EXCLUDED.peso_maximo;

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
       )
ON CONFLICT (correo) DO UPDATE SET
    apodo = EXCLUDED.apodo,
    nombre = EXCLUDED.nombre,
    apellidos = EXCLUDED.apellidos,
    tipo = EXCLUDED.tipo,
    telefono = EXCLUDED.telefono,
    contrasena = EXCLUDED.contrasena,
    fecha_creacion = EXCLUDED.fecha_creacion,
    activa = EXCLUDED.activa,
    nombre_tienda = EXCLUDED.nombre_tienda,
    peso_maximo = EXCLUDED.peso_maximo;






-- 4. Insertamos envíos de ejemplo con diferentes estados
-- Envío EN ALMACÉN (PENDIENTE)
INSERT INTO envio (localizador, direccion_origen, direccion_destino, estado, nombre_comprador, peso, distancia, fragil, numero_paquetes, coste_total, fecha, usuario_id, tarifa_distancia_id, tarifa_rango_peso_id, nota) VALUES
    ('ENV-2024-001', 'Calle Mayor 123, Madrid', 'Avenida Libertad 45, Barcelona', 'RUTA', 'Carlos Ruiz', 2.5, 'NACIONAL', false, 6, 17.00, '2024-01-16', 3, 3, 2, 'Entregar antes de las 18:00'),
    ('ENV-2024-002', 'Plaza España 10, Sevilla', 'Calle Real 8, Málaga', 'RUTA', 'Ana Martín', 0.8, 'CIUDAD', true, 1, 7.00, '2024-01-16', 3, 1, 1, 'Frágil - Manejar con cuidado')
ON CONFLICT (localizador) DO UPDATE SET
    direccion_origen = EXCLUDED.direccion_origen,
    direccion_destino = EXCLUDED.direccion_destino,
    estado = EXCLUDED.estado,
    nombre_comprador = EXCLUDED.nombre_comprador,
    peso = EXCLUDED.peso,
    distancia = EXCLUDED.distancia,
    fragil = EXCLUDED.fragil,
    numero_paquetes = EXCLUDED.numero_paquetes,
    coste_total = EXCLUDED.coste_total,
    fecha = EXCLUDED.fecha,
    usuario_id = EXCLUDED.usuario_id,
    tarifa_distancia_id = EXCLUDED.tarifa_distancia_id,
    tarifa_rango_peso_id = EXCLUDED.tarifa_rango_peso_id,
    nota = EXCLUDED.nota;

-- Crear un repartidor adicional para pruebas (si no existe, actualizará por correo)
INSERT INTO usuario (apodo, nombre, apellidos, tipo, correo, telefono, contrasena, fecha_creacion, activa, nombre_tienda, peso_maximo)
VALUES (
           'Runner2',
           'Luis',
           'Hernández Pérez',
           'REPARTIDOR',
           'repartidor2@paqueteria.com',
           '600444555',
           '$2a$10$eTKikObpwecAKYRGzO1ile6kZcS6HTo2P67BRPpBr10WlURZBWRWq', -- 123
           '2025-01-12',
           true,
           NULL,
           60.00
       )
ON CONFLICT (correo) DO UPDATE SET
    apodo = EXCLUDED.apodo,
    nombre = EXCLUDED.nombre,
    apellidos = EXCLUDED.apellidos,
    tipo = EXCLUDED.tipo,
    telefono = EXCLUDED.telefono,
    contrasena = EXCLUDED.contrasena,
    fecha_creacion = EXCLUDED.fecha_creacion,
    activa = EXCLUDED.activa,
    nombre_tienda = EXCLUDED.nombre_tienda,
    peso_maximo = EXCLUDED.peso_maximo;

-- Rutas finalizadas para otros repartidores
-- Ruta 200 para el repartidor con correo 'repartidor@paqueteria.com'
INSERT INTO ruta (id, fecha, usuario_id) VALUES
    (200, '2025-01-12', (SELECT id FROM usuario WHERE correo = 'repartidor@paqueteria.com'))
ON CONFLICT (id) DO UPDATE SET
    fecha = EXCLUDED.fecha,
    usuario_id = EXCLUDED.usuario_id;

-- Ruta 201 para el repartidor con correo 'repartidor2@paqueteria.com'
INSERT INTO ruta (id, fecha, usuario_id) VALUES
    (201, '2025-01-12', (SELECT id FROM usuario WHERE correo = 'repartidor2@paqueteria.com'))
ON CONFLICT (id) DO UPDATE SET
    fecha = EXCLUDED.fecha,
    usuario_id = EXCLUDED.usuario_id;

-- Envíos entregados para la ruta 200 (más envíos)
INSERT INTO envio (localizador, direccion_origen, direccion_destino, estado, nombre_comprador, peso, distancia, fragil, numero_paquetes, coste_total, fecha, usuario_id, tarifa_distancia_id, tarifa_rango_peso_id, ruta_id, nota) VALUES
    ('ENV-2025-101', 'Calle Comercio 1, Madrid', 'Calle Alcalá 10, Madrid', 'ENTREGADO', 'Cliente G', 1.0, 'CIUDAD', false, 1, 9.00, '2025-01-12', (SELECT id FROM usuario WHERE correo = 'repartidor@paqueteria.com'), 1, 1, 200, 'Entregado tienda'),
    ('ENV-2025-102', 'Calle Comercio 2, Madrid', 'Calle Luna 4, Madrid', 'ENTREGADO', 'Cliente H', 2.5, 'CIUDAD', false, 1, 12.00, '2025-01-12', (SELECT id FROM usuario WHERE correo = 'repartidor@paqueteria.com'), 1, 2, 200, NULL),
    ('ENV-2025-103', 'Calle Comercio 3, Madrid', 'Avenida Sol 7, Madrid', 'ENTREGADO', 'Cliente I', 3.2, 'CIUDAD', false, 2, 18.00, '2025-01-12', (SELECT id FROM usuario WHERE correo = 'repartidor@paqueteria.com'), 1, 2, 200, 'Dejado en conserjería'),
    ('ENV-2025-104', 'Calle Comercio 4, Madrid', 'Calle Norte 9, Madrid', 'ENTREGADO', 'Cliente J', 0.7, 'CIUDAD', false, 1, 8.00, '2025-01-12', (SELECT id FROM usuario WHERE correo = 'repartidor@paqueteria.com'), 1, 1, 200, NULL),
    ('ENV-2025-105', 'Calle Comercio 5, Madrid', 'Calle Sur 11, Madrid', 'ENTREGADO', 'Cliente K', 4.0, 'PROVINCIAL', false, 2, 22.00, '2025-01-12', (SELECT id FROM usuario WHERE correo = 'repartidor@paqueteria.com'), 2, 3, 200, 'Firma recibida')
ON CONFLICT (localizador) DO UPDATE SET
    direccion_origen = EXCLUDED.direccion_origen,
    direccion_destino = EXCLUDED.direccion_destino,
    estado = EXCLUDED.estado,
    nombre_comprador = EXCLUDED.nombre_comprador,
    peso = EXCLUDED.peso,
    distancia = EXCLUDED.distancia,
    fragil = EXCLUDED.fragil,
    numero_paquetes = EXCLUDED.numero_paquetes,
    coste_total = EXCLUDED.coste_total,
    fecha = EXCLUDED.fecha,
    usuario_id = EXCLUDED.usuario_id,
    tarifa_distancia_id = EXCLUDED.tarifa_distancia_id,
    tarifa_rango_peso_id = EXCLUDED.tarifa_rango_peso_id,
    ruta_id = EXCLUDED.ruta_id,
    nota = EXCLUDED.nota;

-- Envíos entregados para la ruta 201 (repartidor2)
INSERT INTO envio (localizador, direccion_origen, direccion_destino, estado, nombre_comprador, peso, distancia, fragil, numero_paquetes, coste_total, fecha, usuario_id, tarifa_distancia_id, tarifa_rango_peso_id, ruta_id, nota) VALUES
    ('ENV-2025-106', 'Camino Viejo 1, Toledo', 'Plaza Mayor 2, Toledo', 'ENTREGADO', 'Cliente L', 1.5, 'CIUDAD', false, 1, 10.00, '2025-01-12', (SELECT id FROM usuario WHERE correo = 'repartidor2@paqueteria.com'), 1, 1, 201, NULL),
    ('ENV-2025-107', 'Camino Viejo 2, Toledo', 'Calle Portal 3, Toledo', 'ENTREGADO', 'Cliente M', 2.2, 'CIUDAD', false, 1, 11.00, '2025-01-12', (SELECT id FROM usuario WHERE correo = 'repartidor2@paqueteria.com'), 1, 1, 201, 'Recibido por vecino'),
    ('ENV-2025-108', 'Camino Viejo 3, Toledo', 'Calle Mayor 5, Toledo', 'ENTREGADO', 'Cliente N', 3.8, 'PROVINCIAL', false, 2, 20.00, '2025-01-12', (SELECT id FROM usuario WHERE correo = 'repartidor2@paqueteria.com'), 2, 3, 201, NULL)
ON CONFLICT (localizador) DO UPDATE SET
    direccion_origen = EXCLUDED.direccion_origen,
    direccion_destino = EXCLUDED.direccion_destino,
    estado = EXCLUDED.estado,
    nombre_comprador = EXCLUDED.nombre_comprador,
    peso = EXCLUDED.peso,
    distancia = EXCLUDED.distancia,
    fragil = EXCLUDED.fragil,
    numero_paquetes = EXCLUDED.numero_paquetes,
    coste_total = EXCLUDED.coste_total,
    fecha = EXCLUDED.fecha,
    usuario_id = EXCLUDED.usuario_id,
    tarifa_distancia_id = EXCLUDED.tarifa_distancia_id,
    tarifa_rango_peso_id = EXCLUDED.tarifa_rango_peso_id,
    ruta_id = EXCLUDED.ruta_id,
    nota = EXCLUDED.nota;

-- Rutas nuevas para el usuario con id = 3
-- Usamos ids fijos (100 y 101) para evitar colisiones y mantener idempotencia
INSERT INTO ruta (id, fecha, usuario_id) VALUES
    (100, '2025-01-10', 3),
    (101, '2025-01-11', 3)
ON CONFLICT (id) DO UPDATE SET
    fecha = EXCLUDED.fecha,
    usuario_id = EXCLUDED.usuario_id;

-- Envíos ENTREGADOS asociados a la ruta 100 (2 envíos)
INSERT INTO envio (localizador, direccion_origen, direccion_destino, estado, nombre_comprador, peso, distancia, fragil, numero_paquetes, coste_total, fecha, usuario_id, tarifa_distancia_id, tarifa_rango_peso_id, ruta_id, nota) VALUES
    ('ENV-2025-001', 'Calle Falsa 1, Madrid', 'Calle Real 2, Toledo', 'ENTREGADO', 'Cliente A', 1.2, 'PROVINCIAL', false, 1, 12.00, '2025-01-10', 3, 2, 1, 100, 'Entrega sin incidencias'),
    ('ENV-2025-002', 'Calle Falsa 3, Madrid', 'Calle Nueva 5, Madrid', 'ENTREGADO', 'Cliente B', 0.5, 'CIUDAD', false, 1, 8.00, '2025-01-10', 3, 1, 1, 100, 'Entregado en mano')
ON CONFLICT (localizador) DO UPDATE SET
    direccion_origen = EXCLUDED.direccion_origen,
    direccion_destino = EXCLUDED.direccion_destino,
    estado = EXCLUDED.estado,
    nombre_comprador = EXCLUDED.nombre_comprador,
    peso = EXCLUDED.peso,
    distancia = EXCLUDED.distancia,
    fragil = EXCLUDED.fragil,
    numero_paquetes = EXCLUDED.numero_paquetes,
    coste_total = EXCLUDED.coste_total,
    fecha = EXCLUDED.fecha,
    usuario_id = EXCLUDED.usuario_id,
    tarifa_distancia_id = EXCLUDED.tarifa_distancia_id,
    tarifa_rango_peso_id = EXCLUDED.tarifa_rango_peso_id,
    ruta_id = EXCLUDED.ruta_id,
    nota = EXCLUDED.nota;

-- Envíos ENTREGADOS asociados a la ruta 101 (4 envíos)
INSERT INTO envio (localizador, direccion_origen, direccion_destino, estado, nombre_comprador, peso, distancia, fragil, numero_paquetes, coste_total, fecha, usuario_id, tarifa_distancia_id, tarifa_rango_peso_id, ruta_id, nota) VALUES
    ('ENV-2025-003', 'Avenida Alta 10, Valladolid', 'Calle Baja 4, Burgos', 'ENTREGADO', 'Cliente C', 2.0, 'PROVINCIAL', false, 2, 18.00, '2025-01-11', 3, 2, 2, 101, 'Recibido por conserje'),
    ('ENV-2025-004', 'Plaza Verde 6, León', 'Calle Mayor 8, Oviedo', 'ENTREGADO', 'Cliente D', 3.3, 'NACIONAL', false, 1, 25.00, '2025-01-11', 3, 3, 3, 101, NULL),
    ('ENV-2025-005', 'Calle Río 12, Salamanca', 'Avenida Sol 20, Zamora', 'ENTREGADO', 'Cliente E', 0.9, 'CIUDAD', false, 1, 9.00, '2025-01-11', 3, 1, 1, 101, 'Puerta lateral'),
    ('ENV-2025-006', 'Camino Antiguo 7, Segovia', 'Calle Puerta 2, Ávila', 'ENTREGADO', 'Cliente F', 5.0, 'NACIONAL', false, 3, 30.00, '2025-01-11', 3, 3, 4, 101, 'Dejar en recepción')
ON CONFLICT (localizador) DO UPDATE SET
    direccion_origen = EXCLUDED.direccion_origen,
    direccion_destino = EXCLUDED.direccion_destino,
    estado = EXCLUDED.estado,
    nombre_comprador = EXCLUDED.nombre_comprador,
    peso = EXCLUDED.peso,
    distancia = EXCLUDED.distancia,
    fragil = EXCLUDED.fragil,
    numero_paquetes = EXCLUDED.numero_paquetes,
    coste_total = EXCLUDED.coste_total,
    fecha = EXCLUDED.fecha,
    usuario_id = EXCLUDED.usuario_id,
    tarifa_distancia_id = EXCLUDED.tarifa_distancia_id,
    tarifa_rango_peso_id = EXCLUDED.tarifa_rango_peso_id,
    ruta_id = EXCLUDED.ruta_id,
    nota = EXCLUDED.nota;

-- Envío EN REPARTO (RUTA)
INSERT INTO envio (localizador, direccion_origen, direccion_destino, estado, nombre_comprador, peso, distancia, fragil, numero_paquetes, coste_total, fecha, usuario_id, tarifa_distancia_id, tarifa_rango_peso_id, nota) VALUES
                                                                                                                                                                                                                              ('ENV-2024-003', 'Calle Sol 56, Valencia', 'Avenida del Mar 22, Alicante', 'RUTA', 'Pedro Sánchez', 4.2, 'PROVINCIAL', false, 2, 21.00, '2024-01-15', -1, 2, 2, NULL),
                                                                                                                                                                                                                              ('ENV-2024-004', 'Calle Luna 33, Zaragoza', 'Plaza Mayor 5, Teruel', 'RUTA', 'Laura Jiménez', 1.5, 'CIUDAD', false, 1, 10.00, '2024-01-15', -1, 1, 2, 'Llamar al llegar')
ON CONFLICT (localizador) DO UPDATE SET
    direccion_origen = EXCLUDED.direccion_origen,
    direccion_destino = EXCLUDED.direccion_destino,
    estado = EXCLUDED.estado,
    nombre_comprador = EXCLUDED.nombre_comprador,
    peso = EXCLUDED.peso,
    distancia = EXCLUDED.distancia,
    fragil = EXCLUDED.fragil,
    numero_paquetes = EXCLUDED.numero_paquetes,
    coste_total = EXCLUDED.coste_total,
    fecha = EXCLUDED.fecha,
    usuario_id = EXCLUDED.usuario_id,
    tarifa_distancia_id = EXCLUDED.tarifa_distancia_id,
    tarifa_rango_peso_id = EXCLUDED.tarifa_rango_peso_id,
    nota = EXCLUDED.nota;

-- Envío ENTREGADO
INSERT INTO envio (localizador, direccion_origen, direccion_destino, estado, nombre_comprador, peso, distancia, fragil, numero_paquetes, coste_total, fecha, usuario_id, tarifa_distancia_id, tarifa_rango_peso_id, nota) VALUES
                                                                                                                                                                                                                              ('ENV-2024-005', 'Avenida Principal 78, Bilbao', 'Calle Secundaria 12, San Sebastián', 'ENTREGADO', 'Miguel Fernández', 3.0, 'CIUDAD', false, 1, 10.00, '2024-01-14', -1, 1, 2, NULL),
                                                                                                                                                                                                                              ('ENV-2024-006', 'Calle Comercio 90, Madrid', 'Rue de la Paix 10, Paris, Francia', 'ENTREGADO', 'Sophie Dubois', 6.5, 'INTERNACIONAL', true, 1, 33.00, '2024-01-10', -1, 4, 3, 'Documentos importantes'),
                                                                                                                                                                                                                              ('ENV-2024-007', 'Plaza Central 5, Granada', 'Calle Norte 18, Almería', 'ENTREGADO', 'José Luis Moreno', 2.0, 'PROVINCIAL', false, 3, 13.00, '2024-01-13', -1, 2, 2, NULL)
ON CONFLICT (localizador) DO UPDATE SET
    direccion_origen = EXCLUDED.direccion_origen,
    direccion_destino = EXCLUDED.direccion_destino,
    estado = EXCLUDED.estado,
    nombre_comprador = EXCLUDED.nombre_comprador,
    peso = EXCLUDED.peso,
    distancia = EXCLUDED.distancia,
    fragil = EXCLUDED.fragil,
    numero_paquetes = EXCLUDED.numero_paquetes,
    coste_total = EXCLUDED.coste_total,
    fecha = EXCLUDED.fecha,
    usuario_id = EXCLUDED.usuario_id,
    tarifa_distancia_id = EXCLUDED.tarifa_distancia_id,
    tarifa_rango_peso_id = EXCLUDED.tarifa_rango_peso_id,
    nota = EXCLUDED.nota;

-- Envío AUSENTE
INSERT INTO envio (localizador, direccion_origen, direccion_destino, estado, nombre_comprador, peso, distancia, fragil, numero_paquetes, coste_total, fecha, usuario_id, tarifa_distancia_id, tarifa_rango_peso_id, nota) VALUES
    ('ENV-2024-008', 'Calle Este 44, Córdoba', 'Avenida Oeste 67, Jaén', 'AUSENTE', 'Carmen López', 1.2, 'CIUDAD', false, 1, 7.00, '2024-01-15', -1, 1, 2, 'Segundo intento de entrega')
ON CONFLICT (localizador) DO UPDATE SET
    direccion_origen = EXCLUDED.direccion_origen,
    direccion_destino = EXCLUDED.direccion_destino,
    estado = EXCLUDED.estado,
    nombre_comprador = EXCLUDED.nombre_comprador,
    peso = EXCLUDED.peso,
    distancia = EXCLUDED.distancia,
    fragil = EXCLUDED.fragil,
    numero_paquetes = EXCLUDED.numero_paquetes,
    coste_total = EXCLUDED.coste_total,
    fecha = EXCLUDED.fecha,
    usuario_id = EXCLUDED.usuario_id,
    tarifa_distancia_id = EXCLUDED.tarifa_distancia_id,
    tarifa_rango_peso_id = EXCLUDED.tarifa_rango_peso_id,
    nota = EXCLUDED.nota;

-- Envío RECHAZADO
INSERT INTO envio (localizador, direccion_origen, direccion_destino, estado, nombre_comprador, peso, distancia, fragil, numero_paquetes, coste_total, fecha, usuario_id, tarifa_distancia_id, tarifa_rango_peso_id, nota) VALUES
    ('ENV-2024-009', 'Calle Sur 22, Murcia', 'Plaza Norte 9, Cartagena', 'RECHAZADO', 'Antonio García', 5.5, 'NACIONAL', false, 1, 20.00, '2024-01-14', -1, 3, 2, 'Paquete dañado - devolver al remitente')
ON CONFLICT (localizador) DO UPDATE SET
    direccion_origen = EXCLUDED.direccion_origen,
    direccion_destino = EXCLUDED.direccion_destino,
    estado = EXCLUDED.estado,
    nombre_comprador = EXCLUDED.nombre_comprador,
    peso = EXCLUDED.peso,
    distancia = EXCLUDED.distancia,
    fragil = EXCLUDED.fragil,
    numero_paquetes = EXCLUDED.numero_paquetes,
    coste_total = EXCLUDED.coste_total,
    fecha = EXCLUDED.fecha,
    usuario_id = EXCLUDED.usuario_id,
    tarifa_distancia_id = EXCLUDED.tarifa_distancia_id,
    tarifa_rango_peso_id = EXCLUDED.tarifa_rango_peso_id,
    nota = EXCLUDED.nota;
