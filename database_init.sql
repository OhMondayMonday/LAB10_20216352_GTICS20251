-- Database creation
CREATE DATABASE IF NOT EXISTS buscaminas;
USE buscaminas;

-- Table: configuracion
CREATE TABLE IF NOT EXISTS configuracion (
    idMina INT PRIMARY KEY,
    dim_mina_x INT NOT NULL,
    dim_mina_y INT NOT NULL,
    cant_bombas INT NOT NULL,
    cant_intentos INT NOT NULL,
    cant_intentos_actual INT NOT NULL
);

-- Table: posicionbomba
CREATE TABLE IF NOT EXISTS posicionbomba (
    idBomba INT AUTO_INCREMENT PRIMARY KEY,
    coordenadaX INT NOT NULL,
    coordenadaY INT NOT NULL,
    idMina INT NOT NULL
);

-- Table: Para el historial
CREATE TABLE IF NOT EXISTS movimiento (
    id_movimiento INT AUTO_INCREMENT PRIMARY KEY,
    id_mina INT NOT NULL,
    coordenada_x INT NOT NULL,
    coordenada_y INT NOT NULL,
    es_bomba BOOLEAN NOT NULL,
    numero_vecinas INT NOT NULL,
    fecha_movimiento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    numero_movimiento INT NOT NULL,
    resultado VARCHAR(20) NOT NULL,
    FOREIGN KEY (id_mina) REFERENCES configuracion(idMina)
);

-- Insert sample data for 6x6 board
INSERT INTO configuracion (idMina, dim_mina_x, dim_mina_y, cant_bombas, cant_intentos, cant_intentos_actual) VALUES 
(1, 6, 6, 5, 2, 2);

INSERT INTO posicionbomba (idBomba, coordenadaX, coordenadaY, idMina) VALUES 
(1, 3, 2, 1),
(2, 4, 6, 1),
(3, 5, 4, 1),
(4, 4, 5, 1),
(5, 6, 2, 1);
