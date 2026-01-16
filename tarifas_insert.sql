-- Insertar Tarifas de Distancia
INSERT INTO tarifa_distancia (distancia, coste, activa) VALUES
('CIUDAD', 1.00, true),
('PROVINCIAL', 3.00, true),
('NACIONAL', 5.00, true),
('INTERNACIONAL', 10.00, true);

-- Insertar Tarifas de Rango de Peso
INSERT INTO tarifa_rango_peso (peso_minimo, peso_maximo, coste, activa) VALUES
(0, 10, 1.00, true),
(10, 20, 2.00, true),
(20, 40, 4.00, true),
(40, 2147483647, 8.00, true);  -- 2147483647 es el valor máximo de INTEGER en PostgreSQL

