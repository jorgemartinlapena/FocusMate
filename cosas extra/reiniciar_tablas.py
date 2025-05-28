import psycopg2

# Parámetros de conexión
DB_HOST = "34.175.213.152"
DB_PORT = "5432"
DB_NAME = "postgres"
DB_USER = "postgres"
DB_PASSWORD = "focusmate"

# Script SQL para eliminar y crear tablas
sql_script = """
SET search_path TO "fmSchema";

-- Eliminar tablas en orden de dependencias
DROP TABLE IF EXISTS user_achievements CASCADE;
DROP TABLE IF EXISTS user_sessions CASCADE;
DROP TABLE IF EXISTS achievements CASCADE;
DROP TABLE IF EXISTS metodos_estudio CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Crear tabla de usuarios
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear tabla con los metodos de estudio predefinidos
CREATE TABLE metodos_estudio (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    repeticiones INTEGER NOT NULL,
    tiempo_estudio INTEGER NOT NULL,
    tiempo_descanso INTEGER NOT NULL,
    tiempo_descanso_final INTEGER NOT NULL,
    descripcion TEXT NOT NULL,
    tiempo_total_estudio INTEGER NOT NULL
);

-- Crear tabla de logros (achievements)
CREATE TABLE achievements (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT NOT NULL,
    dias INTEGER NOT NULL DEFAULT 0,
    minutos INTEGER NOT NULL DEFAULT 0,
    tipo VARCHAR(50) NOT NULL
);

-- Crear tabla para logros desbloqueados por usuario
CREATE TABLE user_achievements (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    achievement_id INT NOT NULL,
    unlocked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (achievement_id) REFERENCES achievements(id) ON DELETE CASCADE
);

-- Crear tabla de sesiones de estudio del usuario
CREATE TABLE user_sessions (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    method_id INT,
    session_timestamp TIMESTAMP NOT NULL,
    duration_minutes INT NOT NULL,
    task_type VARCHAR(50),
    productivity_level INT CHECK (productivity_level BETWEEN 1 AND 5),
    average_pulse INT DEFAULT NULL,
    average_movement INT DEFAULT NULL,
    concentration_level INT CHECK (concentration_level BETWEEN 0 AND 100),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (method_id) REFERENCES metodos_estudio(id) ON DELETE SET NULL
);
"""

try:
    # Conexión
    conn = psycopg2.connect(
        host=DB_HOST,
        port=DB_PORT,
        dbname=DB_NAME,
        user=DB_USER,
        password=DB_PASSWORD
    )
    cur = conn.cursor()

    # Ejecutar script
    cur.execute(sql_script)
    conn.commit()

    print("✅ Tablas eliminadas y recreadas correctamente en el esquema 'fmSchema'.")

except Exception as e:
    print("❌ Error ejecutando el script:", e)

finally:
    if cur:
        cur.close()
    if conn:
        conn.close()
