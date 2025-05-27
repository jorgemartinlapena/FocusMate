SET search_path TO "fmSchema";
-- Crear tabla de usuarios
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL;
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear tabla con los metodos de estudio predefinidos
CREATE TABLE metodos_estudio (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    repeticiones INTEGER NOT NULL,
    tiempo_estudio INTEGER NOT NULL,         -- En minutos
    tiempo_descanso INTEGER NOT NULL,        -- En minutos
    tiempo_descanso_final INTEGER NOT NULL,  -- En minutos
    descripcion TEXT NOT NULL,
    tiempo_total_estudio INTEGER NOT NULL    -- En minutos
);

-- Crear tabla de logros (achievements)
CREATE TABLE achievements (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT NOT NULL,
    dias INTEGER NOT NULL DEFAULT 0,        -- Días consecutivos necesarios para desbloquear el logro
    minutos INTEGER NOT NULL DEFAULT 0,     -- Minutos necesarios para desbloquear el logro
    tipo VARCHAR(50) NOT NULL               -- Tipo: 'total', 'sesion', 'consistencia', etc.
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
    method_id INT,  -- Puede ser NULL si no se usó un método predefinido
    session_timestamp TIMESTAMP NOT NULL,
    duration_minutes INT NOT NULL,
    task_type VARCHAR(50), -- Ej: "lectura", "creativa", "repetitiva"
    productivity_level INT CHECK (productivity_level BETWEEN 1 AND 5), -- Autoevaluación 1-5
    average_pulse INT DEFAULT NULL,
    average_movement INT DEFAULT NULL,
    concentration_level INT CHECK (concentration_level BETWEEN 0 AND 100),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (method_id) REFERENCES metodos_estudio(id) ON DELETE SET NULL
);
