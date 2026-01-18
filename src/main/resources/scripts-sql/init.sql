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

-- Más REPARTIDORES para pruebas
INSERT INTO usuario (apodo, nombre, apellidos, tipo, correo, telefono, contrasena, fecha_creacion, activa, nombre_tienda, peso_maximo)
VALUES (
           'FastRunner',
           'Pedro',
           'López García',
           'REPARTIDOR',
           'pedro@paqueteria.com',
           '600444555',
           '$2a$10$eTKikObpwecAKYRGzO1ile6kZcS6HTo2P67BRPpBr10WlURZBWRWq', -- 123
           '2025-03-10',
           true,
           NULL,
           75.00
       );

INSERT INTO usuario (apodo, nombre, apellidos, tipo, correo, telefono, contrasena, fecha_creacion, activa, nombre_tienda, peso_maximo)
VALUES (
           'Veloz',
           'Ana',
           'Fernández Ruiz',
           'REPARTIDOR',
           'ana@paqueteria.com',
           '600555666',
           '$2a$10$eTKikObpwecAKYRGzO1ile6kZcS6HTo2P67BRPpBr10WlURZBWRWq', -- 123
           '2025-07-22',
           true,
           NULL,
           60.00
       );

-- ENVÍOS DE PRUEBA
-- Nota: Asumiendo que los IDs de usuario son 1 (Webmaster), 2 (Repartidor), 3 (Cliente/Tienda), 4 (Pedro), 5 (Ana)
-- y que las tarifas tienen los IDs 1-4 como se definieron arriba

-- ENVÍOS PENDIENTES (para poder asignar repartidor)
INSERT INTO envio (localizador, direccion_origen, direccion_destino, estado, nombre_comprador, nota, peso, distancia, fragil, numero_paquetes, coste_total, fecha, usuario_id, tarifa_distancia_id, tarifa_rango_peso_id)
VALUES
('PEND-001-2026', 'Calle Mayor 15, Madrid', 'Avenida de la Constitución 23, Madrid', 'PENDIENTE', 'Laura Gómez', 'Entregar en horario de mañana', 5.5, 'CIUDAD', false, 1, 60.0, '2026-01-15', 3, 1, 1),
('PEND-002-2026', 'Gran Vía 45, Madrid', 'Calle Serrano 89, Madrid', 'PENDIENTE', 'Carlos Ruiz', 'Piso 3º derecha', 15.0, 'CIUDAD', false, 2, 70.0, '2026-01-16', 3, 1, 2),
('PEND-003-2026', 'Plaza España 12, Madrid', 'Calle Alcalá 156, Madrid', 'PENDIENTE', 'Marta Jiménez', NULL, 8.2, 'CIUDAD', true, 1, 60.0, '2026-01-16', 3, 1, 1),
('PEND-004-2026', 'Calle Toledo 67, Madrid', 'Paseo de la Castellana 200, Madrid', 'PENDIENTE', 'Roberto Sánchez', 'Llamar al llegar', 25.5, 'CIUDAD', false, 3, 85.0, '2026-01-17', 3, 1, 3),
('PEND-005-2026', 'Avenida América 34, Madrid', 'Calle Princesa 78, Madrid', 'PENDIENTE', 'Isabel Torres', NULL, 12.0, 'CIUDAD', false, 1, 70.0, '2026-01-17', 3, 1, 2),
('PEND-006-2026', 'Calle Bravo Murillo 123, Madrid', 'Glorieta de Bilbao 5, Madrid', 'PENDIENTE', 'Francisco Díaz', 'Frágil - manipular con cuidado', 7.8, 'CIUDAD', true, 1, 60.0, '2026-01-17', 3, 1, 1),
('PEND-007-2026', 'Calle Goya 56, Madrid', 'Calle Velázquez 90, Madrid', 'PENDIENTE', 'Carmen Moreno', NULL, 18.5, 'CIUDAD', false, 2, 70.0, '2026-01-17', 3, 1, 2),
('PEND-008-2026', 'Paseo Recoletos 12, Madrid', 'Calle Fuencarral 145, Madrid', 'PENDIENTE', 'Alberto Castro', 'Portón negro', 22.0, 'CIUDAD', false, 1, 85.0, '2026-01-17', 3, 1, 3),
('PEND-009-2026', 'Calle Atocha 89, Madrid', 'Plaza Cibeles 1, Madrid', 'PENDIENTE', 'Beatriz Romero', NULL, 9.5, 'CIUDAD', false, 1, 60.0, '2026-01-17', 3, 1, 1),
('PEND-010-2026', 'Gran Vía 78, Madrid', 'Calle Preciados 23, Madrid', 'PENDIENTE', 'David Navarro', 'Tocar timbre 2 veces', 14.2, 'CIUDAD', false, 2, 70.0, '2026-01-17', 3, 1, 2),
('PEND-011-2026', 'Calle Orense 34, Madrid', 'Calle Hermosilla 67, Madrid', 'PENDIENTE', 'Elena Vázquez', NULL, 35.0, 'CIUDAD', false, 4, 85.0, '2026-01-17', 3, 1, 3),
('PEND-012-2026', 'Avenida Filipinas 12, Madrid', 'Calle María de Molina 45, Madrid', 'PENDIENTE', 'Miguel Ángel Pérez', 'Edificio con código de acceso', 6.8, 'CIUDAD', true, 1, 60.0, '2026-01-17', 3, 1, 1);

-- ENVÍOS AUSENTES (cliente no estaba en casa)
INSERT INTO envio (localizador, direccion_origen, direccion_destino, estado, nombre_comprador, nota, peso, distancia, fragil, numero_paquetes, coste_total, fecha, usuario_id, tarifa_distancia_id, tarifa_rango_peso_id)
VALUES
('AUS-001-2026', 'Calle Luna 23, Madrid', 'Calle Sol 45, Madrid', 'AUSENTE', 'Jorge Martín', 'No había nadie en casa', 11.5, 'CIUDAD', false, 1, 70.0, '2026-01-14', 3, 1, 2),
('AUS-002-2026', 'Plaza Mayor 8, Madrid', 'Calle Mayor 112, Madrid', 'AUSENTE', 'Patricia González', 'Llamaron pero no abrieron', 8.0, 'CIUDAD', false, 1, 60.0, '2026-01-14', 3, 1, 1),
('AUS-003-2026', 'Calle Arenal 34, Madrid', 'Gran Vía 123, Madrid', 'AUSENTE', 'Sergio Ramírez', NULL, 19.5, 'CIUDAD', false, 2, 70.0, '2026-01-15', 3, 1, 2),
('AUS-004-2026', 'Paseo Prado 56, Madrid', 'Calle Alcalá 234, Madrid', 'AUSENTE', 'Lucía Herrera', 'Buzón lleno, no se pudo dejar aviso', 13.2, 'CIUDAD', true, 1, 70.0, '2026-01-15', 3, 1, 2),
('AUS-005-2026', 'Calle Hortaleza 89, Madrid', 'Calle Montera 67, Madrid', 'AUSENTE', 'Raúl Medina', NULL, 27.0, 'CIUDAD', false, 3, 85.0, '2026-01-16', 3, 1, 3),
('AUS-006-2026', 'Avenida Reina Victoria 45, Madrid', 'Calle Bravo Murillo 234, Madrid', 'AUSENTE', 'Silvia Campos', 'Portero no atendió', 9.8, 'CIUDAD', false, 1, 60.0, '2026-01-16', 3, 1, 1),
('AUS-007-2026', 'Calle Alberto Aguilera 78, Madrid', 'Calle Princesa 145, Madrid', 'AUSENTE', 'Óscar Delgado', NULL, 16.5, 'CIUDAD', false, 2, 70.0, '2026-01-16', 3, 1, 2),
('AUS-008-2026', 'Glorieta Cuatro Caminos 3, Madrid', 'Calle Bravo Murillo 89, Madrid', 'AUSENTE', 'Cristina Ortiz', 'Dirección confusa', 21.0, 'CIUDAD', false, 2, 85.0, '2026-01-16', 3, 1, 3),
('AUS-009-2026', 'Calle Arturo Soria 123, Madrid', 'Calle María de Molina 234, Madrid', 'AUSENTE', 'Antonio Blanco', NULL, 7.5, 'CIUDAD', true, 1, 60.0, '2026-01-16', 3, 1, 1),
('AUS-010-2026', 'Paseo Castellana 145, Madrid', 'Calle Serrano 156, Madrid', 'AUSENTE', 'Rosa Gil', 'Timbre no funciona', 12.8, 'CIUDAD', false, 1, 70.0, '2026-01-16', 3, 1, 2),
('AUS-011-2026', 'Calle Diego de León 67, Madrid', 'Calle Velázquez 178, Madrid', 'AUSENTE', 'Javier Pascual', NULL, 32.0, 'CIUDAD', false, 3, 85.0, '2026-01-17', 3, 1, 3),
('AUS-012-2026', 'Calle Goya 234, Madrid', 'Calle Conde de Peñalver 89, Madrid', 'AUSENTE', 'Nuria Santos', 'Oficina cerrada', 5.2, 'CIUDAD', false, 1, 60.0, '2026-01-17', 3, 1, 1);

-- ENVÍOS RECHAZADOS (cliente rechazó el paquete)
INSERT INTO envio (localizador, direccion_origen, direccion_destino, estado, nombre_comprador, nota, peso, distancia, fragil, numero_paquetes, coste_total, fecha, usuario_id, tarifa_distancia_id, tarifa_rango_peso_id)
VALUES
('REC-001-2026', 'Calle Bailén 45, Madrid', 'Plaza de España 23, Madrid', 'RECHAZADO', 'Fernando Iglesias', 'Cliente rechazó - no coincide pedido', 14.0, 'CIUDAD', false, 1, 70.0, '2026-01-13', 3, 1, 2),
('REC-002-2026', 'Calle Segovia 67, Madrid', 'Calle Toledo 123, Madrid', 'RECHAZADO', 'Pilar Ramos', 'Paquete dañado - rechazado', 10.5, 'CIUDAD', true, 1, 60.0, '2026-01-13', 3, 1, 1),
('REC-003-2026', 'Gran Vía 234, Madrid', 'Calle Preciados 89, Madrid', 'RECHAZADO', 'Andrés Molina', 'No es lo que pedí', 17.8, 'CIUDAD', false, 2, 70.0, '2026-01-14', 3, 1, 2),
('REC-004-2026', 'Calle Mayor 178, Madrid', 'Plaza Mayor 34, Madrid', 'RECHAZADO', 'Victoria Prieto', NULL, 23.5, 'CIUDAD', false, 2, 85.0, '2026-01-14', 3, 1, 3),
('REC-005-2026', 'Paseo Recoletos 89, Madrid', 'Calle Alcalá 345, Madrid', 'RECHAZADO', 'Rubén Cortés', 'Ya compré en otro sitio', 8.8, 'CIUDAD', false, 1, 60.0, '2026-01-15', 3, 1, 1),
('REC-006-2026', 'Calle Fuencarral 234, Madrid', 'Gran Vía 345, Madrid', 'RECHAZADO', 'Sandra Calvo', 'Llegó tarde - ya no lo necesito', 19.2, 'CIUDAD', false, 2, 70.0, '2026-01-15', 3, 1, 2),
('REC-007-2026', 'Avenida América 123, Madrid', 'Calle O''Donnell 67, Madrid', 'RECHAZADO', 'Pablo Núñez', NULL, 28.0, 'CIUDAD', false, 3, 85.0, '2026-01-15', 3, 1, 3),
('REC-008-2026', 'Calle Velázquez 234, Madrid', 'Paseo Castellana 456, Madrid', 'RECHAZADO', 'Mónica Vega', 'Precio incorrecto en factura', 6.5, 'CIUDAD', true, 1, 60.0, '2026-01-16', 3, 1, 1),
('REC-009-2026', 'Calle Serrano 345, Madrid', 'Calle Goya 456, Madrid', 'RECHAZADO', 'Iván Hidalgo', 'Cambié de opinión', 15.5, 'CIUDAD', false, 2, 70.0, '2026-01-16', 3, 1, 2),
('REC-010-2026', 'Calle María de Molina 123, Madrid', 'Calle Arturo Soria 234, Madrid', 'RECHAZADO', 'Gloria Suárez', NULL, 11.0, 'CIUDAD', false, 1, 70.0, '2026-01-16', 3, 1, 2),
('REC-011-2026', 'Calle Hermosilla 178, Madrid', 'Calle Conde de Peñalver 234, Madrid', 'RECHAZADO', 'Álvaro Mora', 'Embalaje roto', 34.0, 'CIUDAD', false, 4, 85.0, '2026-01-16', 3, 1, 3),
('REC-012-2026', 'Paseo Prado 123, Madrid', 'Calle Atocha 234, Madrid', 'RECHAZADO', 'Teresa León', 'Pedido duplicado', 7.2, 'CIUDAD', false, 1, 60.0, '2026-01-17', 3, 1, 1);
