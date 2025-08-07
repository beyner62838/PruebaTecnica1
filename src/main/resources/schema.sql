-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS bank_db;

-- Crear las tablas
CREATE TABLE IF NOT EXISTS clientes (
    id SERIAL PRIMARY KEY,
    tipo_identificacion VARCHAR(20) NOT NULL,
    numero_identificacion VARCHAR(20) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    correo_electronico VARCHAR(100) NOT NULL UNIQUE,
    fecha_nacimiento DATE NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL,
    fecha_modificacion TIMESTAMP
);

CREATE TABLE IF NOT EXISTS cuentas (
    id SERIAL PRIMARY KEY,
    tipo_cuenta VARCHAR(20) NOT NULL,
    numero_cuenta VARCHAR(10) NOT NULL UNIQUE,
    estado VARCHAR(20) NOT NULL,
    saldo DECIMAL(19,2) NOT NULL DEFAULT 0.00,
    exenta_gmf BOOLEAN NOT NULL DEFAULT FALSE,
    fecha_creacion TIMESTAMP NOT NULL,
    fecha_modificacion TIMESTAMP,
    cliente_id BIGINT NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

CREATE TABLE IF NOT EXISTS transacciones (
    id SERIAL PRIMARY KEY,
    tipo_transaccion VARCHAR(20) NOT NULL,
    valor DECIMAL(19,2) NOT NULL,
    fecha TIMESTAMP NOT NULL,
    descripcion VARCHAR(500),
    cuenta_origen_id BIGINT,
    cuenta_destino_id BIGINT,
    FOREIGN KEY (cuenta_origen_id) REFERENCES cuentas(id),
    FOREIGN KEY (cuenta_destino_id) REFERENCES cuentas(id)
);
