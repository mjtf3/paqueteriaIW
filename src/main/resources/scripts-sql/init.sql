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
-- Si ya existe (id=1), actualiza los datos
INSERT INTO usuario (id, nombre, apellidos, tipo, correo, telefono, contrasena, fecha_creacion, peso_maximo, activa, apodo, nombre_tienda)
VALUES (-1, 'Usuario', 'Ejemplo', 'CLIENTE', 'usuario@ejemplo.com', '600123456', 'password123', CURRENT_DATE, 100.0, true, 'usuarioDemo', 'Tienda Demo')
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


-- 4. Insertamos envíos de ejemplo con diferentes estados
-- Envío EN ALMACÉN (PENDIENTE)
INSERT INTO envio (localizador, direccion_origen, direccion_destino, estado, nombre_comprador, peso, distancia, fragil, numero_paquetes, coste_total, fecha, usuario_id, tarifa_distancia_id, tarifa_rango_peso_id, nota) VALUES
                                                                                                                                                                                                                              ('ENV-2024-001', 'Calle Mayor 123, Madrid', 'Avenida Libertad 45, Barcelona', 'PENDIENTE', 'Carlos Ruiz', 2.5, 'NACIONAL', false, 1, 17.00, '2024-01-16', -1, 3, 2, 'Entregar antes de las 18:00'),
                                                                                                                                                                                                                              ('ENV-2024-002', 'Plaza España 10, Sevilla', 'Calle Real 8, Málaga', 'PENDIENTE', 'Ana Martín', 0.8, 'CIUDAD', true, 1, 7.00, '2024-01-16', -1, 1, 1, 'Frágil - Manejar con cuidado');

-- Envío EN REPARTO (RUTA)
INSERT INTO envio (localizador, direccion_origen, direccion_destino, estado, nombre_comprador, peso, distancia, fragil, numero_paquetes, coste_total, fecha, usuario_id, tarifa_distancia_id, tarifa_rango_peso_id, nota) VALUES
                                                                                                                                                                                                                              ('ENV-2024-003', 'Calle Sol 56, Valencia', 'Avenida del Mar 22, Alicante', 'RUTA', 'Pedro Sánchez', 4.2, 'PROVINCIAL', false, 2, 21.00, '2024-01-15', -1, 2, 2, NULL),
                                                                                                                                                                                                                              ('ENV-2024-004', 'Calle Luna 33, Zaragoza', 'Plaza Mayor 5, Teruel', 'RUTA', 'Laura Jiménez', 1.5, 'CIUDAD', false, 1, 10.00, '2024-01-15', -1, 1, 2, 'Llamar al llegar');

-- Envío ENTREGADO
INSERT INTO envio (localizador, direccion_origen, direccion_destino, estado, nombre_comprador, peso, distancia, fragil, numero_paquetes, coste_total, fecha, usuario_id, tarifa_distancia_id, tarifa_rango_peso_id, nota) VALUES
                                                                                                                                                                                                                              ('ENV-2024-005', 'Avenida Principal 78, Bilbao', 'Calle Secundaria 12, San Sebastián', 'ENTREGADO', 'Miguel Fernández', 3.0, 'CIUDAD', false, 1, 10.00, '2024-01-14', -1, 1, 2, NULL),
                                                                                                                                                                                                                              ('ENV-2024-006', 'Calle Comercio 90, Madrid', 'Rue de la Paix 10, Paris, Francia', 'ENTREGADO', 'Sophie Dubois', 6.5, 'INTERNACIONAL', true, 1, 33.00, '2024-01-10', -1, 4, 3, 'Documentos importantes'),
                                                                                                                                                                                                                              ('ENV-2024-007', 'Plaza Central 5, Granada', 'Calle Norte 18, Almería', 'ENTREGADO', 'José Luis Moreno', 2.0, 'PROVINCIAL', false, 3, 13.00, '2024-01-13', -1, 2, 2, NULL);

-- Envío AUSENTE
INSERT INTO envio (localizador, direccion_origen, direccion_destino, estado, nombre_comprador, peso, distancia, fragil, numero_paquetes, coste_total, fecha, usuario_id, tarifa_distancia_id, tarifa_rango_peso_id, nota) VALUES
    ('ENV-2024-008', 'Calle Este 44, Córdoba', 'Avenida Oeste 67, Jaén', 'AUSENTE', 'Carmen López', 1.2, 'CIUDAD', false, 1, 7.00, '2024-01-15', -1, 1, 2, 'Segundo intento de entrega');

-- Envío RECHAZADO
INSERT INTO envio (localizador, direccion_origen, direccion_destino, estado, nombre_comprador, peso, distancia, fragil, numero_paquetes, coste_total, fecha, usuario_id, tarifa_distancia_id, tarifa_rango_peso_id, nota) VALUES
    ('ENV-2024-009', 'Calle Sur 22, Murcia', 'Plaza Norte 9, Cartagena', 'RECHAZADO', 'Antonio García', 5.5, 'NACIONAL', false, 1, 20.00, '2024-01-14', -1, 3, 2, 'Paquete dañado - devolver al remitente');
