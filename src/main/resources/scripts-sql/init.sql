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
(4, 40, 2147483647, 50.0, 'mayor de 40kg', true)
ON CONFLICT (id) DO UPDATE SET
    peso_minimo = EXCLUDED.peso_minimo,
    peso_maximo = EXCLUDED.peso_maximo,
    coste = EXCLUDED.coste,
    descripcion = EXCLUDED.descripcion,
    activa = EXCLUDED.activa;

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