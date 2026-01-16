-- Inserts para TarifaDistancia
-- Las columnas deben ser: distancia (ENUM), coste, activa
DELETE FROM tarifa_distancia; -- Limpia la tabla antes de insertar nuevos datos
INSERT INTO tarifa_distancia (distancia, coste, activa) VALUES
('CIUDAD', 50.0, true),
('PROVINCIAL', 100.0, true),
('NACIONAL', 200.0, true),
('INTERNACIONAL', 500.0, true);

-- Inserts para TarifaRangoPeso
-- Ajusta según tu modelo TarifaRangoPeso
DELETE FROM tarifa_rango_peso; -- Limpia la tabla antes de insertar nuevos datos
INSERT INTO tarifa_rango_peso (peso_minimo, peso_maximo, coste, descripcion, activa) VALUES
(0, 10, 10.0, 'menor de 10kg', true),
(10, 20, 20.0, '10kg - 20kg', true),
(20, 40, 35.0, '20kg - 40kg', true),
(40, 2147483647, 50.0, 'mayor de 40kg', true);

-- Insert de usuario de ejemplo
-- Si ya existe (id=1), actualiza los datos
INSERT INTO usuario (id, nombre, apellidos, tipo, correo, telefono, contrasena, fecha_creacion, peso_maximo, activa, apodo, nombre_tienda)
VALUES (1, 'Usuario', 'Ejemplo', 'CLIENTE', 'usuario@ejemplo.com', '600123456', 'password123', CURRENT_DATE, 100.0, true, 'usuarioDemo', 'Tienda Demo')
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