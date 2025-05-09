from datetime import datetime, timedelta
import psycopg2
import os

DB_HOST = os.getenv("DB_HOST", "34.175.213.152")
DB_PORT = os.getenv("DB_PORT", "5432")
DB_NAME = os.getenv("DB_NAME", "postgres")
DB_USER = os.getenv("DB_USER", "postgres")
DB_PASSWORD = os.getenv("DB_PASSWORD", "focusmate")

# Datos de conexión con la base de datos
def get_db_connection():
    return psycopg2.connect(
        host=DB_HOST,
        port=DB_PORT,
        dbname=DB_NAME,
        user=DB_USER,
        password=DB_PASSWORD
    )

# Devuelve todos los logros
def get_achievements():
    with get_db_connection() as conn:
        with conn.cursor() as cursor:
            cursor.execute('SET search_path TO "fmSchema";')
            cursor.execute('SELECT id, nombre, descripcion, dias, minutos, tipo FROM achievements;')
            return [
                {
                    "id": row[0],
                    "nombre": row[1],
                    "descripcion": row[2],
                    "dias": row[3],
                    "minutos": row[4],
                    "tipo": row[5]
                } for row in cursor.fetchall()
            ]
        
# Devuelve la información de un logro específico
def get_achievement_by_id(achievement_id: int):
    with get_db_connection() as conn:
        with conn.cursor() as cursor:
            cursor.execute('SET search_path TO "fmSchema";')
            cursor.execute('SELECT id, nombre, descripcion, dias, minutos, tipo FROM achievements WHERE id = %s;', (achievement_id,))
            row = cursor.fetchone()
            if row:
                return {
                    "id": row[0],
                    "nombre": row[1],
                    "descripcion": row[2],
                    "dias": row[3],
                    "minutos": row[4],
                    "tipo": row[5]
                }
            return None

# Devuelve los metodos de estudio
def get_methods():
    with get_db_connection() as conn:
        with conn.cursor() as cursor:
            cursor.execute('SET search_path TO "fmSchema";')
            cursor.execute('SELECT id, nombre, repeticiones, tiempo_estudio, tiempo_descanso, ' \
            'tiempo_descanso_final, descripcion, tiempo_total_estudio FROM metodos_estudio;')
            return [
                {
                    "id": row[0],
                    "nombre": row[1],
                    "repeticiones": row[2],
                    "tiempo_estudio": row[3],
                    "tiempo_descanso": row[4],
                    "tiempo_descanso_final": row[5],
                    "descripcion": row[6],
                    "tiempo_total_estudio": row[7]
                } for row in cursor.fetchall()
            ]
        
# Devuelve la información de un metodo específico
def get_method_by_id(method_id: int):
    with get_db_connection() as conn:
        with conn.cursor() as cursor:
            cursor.execute('SET search_path TO "fmSchema";')
            cursor.execute('SELECT id, nombre, repeticiones, tiempo_estudio, tiempo_descanso, ' \
            'tiempo_descanso_final, descripcion, tiempo_total_estudio FROM metodos_estudio WHERE id = %s;', (method_id,))
            row = cursor.fetchone()
            if row:
                return {
                    "id": row[0],
                    "nombre": row[1],
                    "repeticiones": row[2],
                    "tiempo_estudio": row[3],
                    "tiempo_descanso": row[4],
                    "tiempo_descanso_final": row[5],
                    "descripcion": row[6],
                    "tiempo_total_estudio": row[7]
                }
            return None
        
# Devuelve las sesiones de estudio de un usuario especifico
def get_user_sessions(user_id: int):
    with get_db_connection() as conn:
        with conn.cursor() as cursor:
            cursor.execute('SET search_path TO "fmSchema";')
            cursor.execute("""
                SELECT id, user_id, method_id, session_timestamp, duration_minutes, task_type, productivity_level
                FROM user_sessions 
                WHERE user_id = %s ORDER BY session_timestamp DESC;
            """, (user_id,))
            return [
                {
                    "id": row[0],
                    "user_id": row[1],
                    "method_id": row[2],
                    "session_timestamp": row[3],
                    "duration_minutes": row[4],
                    "task_type": row[5],
                    "productivity_level": row[6]
                } for row in cursor.fetchall()
            ]

# Agrega una nueva sesión de estudio para un usuario
def set_user_session(user_id: int, method_id: int, session_timestamp: str, duration_minutes: int, task_type: str, productivity_level: int):
    with get_db_connection() as conn:
        with conn.cursor() as cursor:
            cursor.execute('SET search_path TO "fmSchema";')
            cursor.execute("""
                INSERT INTO user_sessions (user_id, method_id, session_timestamp, duration_minutes, task_type, productivity_level)
                VALUES (%s, %s, %s, %s, %s, %s);
            """, (user_id, method_id, session_timestamp, duration_minutes, task_type, productivity_level))
            conn.commit()

# Devuelve los logros desbloqueados por un usuario especifico        
def get_user_achievements(user_id: int):
    with get_db_connection() as conn:
        with conn.cursor() as cursor:
            cursor.execute('SET search_path TO "fmSchema";')
            cursor.execute("""
                SELECT id, user_id, achievement_id, unlocked_at
                FROM user_achievements 
                WHERE user_id = %s ORDER BY unlocked_at DESC;
            """, (user_id,))
            return [
                {
                    "id": row[0],
                    "user_id": row[1],
                    "achievement_id": row[2],
                    "unlocked_at": row[3]
                } for row in cursor.fetchall()
            ]

def set_user_achievement(user_id: int, achievement_id: int):
    with get_db_connection() as conn:
        with conn.cursor() as cursor:
            cursor.execute('SET search_path TO "fmSchema";')
            cursor.execute("""
                INSERT INTO user_achievements (user_id, achievement_id, unlocked_at)
                VALUES (%s, %s, NOW());
            """, (user_id, achievement_id))
            conn.commit()

# Comprueba y actualiza logros nuevos
def check_and_update_achievements(user_id: int):
    all_achievements = get_achievements()
    user_achievements = get_user_achievements(user_id)
    user_sessions = get_user_sessions(user_id)

    new_achievements = []
    user_achievement_ids = {achievement["achievement_id"] for achievement in user_achievements}

    for achievement in all_achievements:
        # Comprobamos si el logro ya ha sido desbloqueado por el usuario
        if achievement["id"] not in user_achievement_ids :
            if achievement["tipo"] == "total":
                # verificamos si el usuario ha alcanzado el tiempo total requerido
                total_time = sum(session["duration_minutes"] for session in user_sessions)
                if total_time >= achievement["minutos"]:
                    new_achievements.append(achievement)

            elif achievement["tipo"] == "sesion":
                # obtenemos el tiempo de las última sesión, que debería ser la primera
                tiempo_ultima_sesion = user_sessions[0]["duration_minutes"] if user_sessions else 0
                if tiempo_ultima_sesion >= achievement["minutos"]:
                    new_achievements.append(achievement)
                

            elif achievement["tipo"] == "consistencia":
                # obtenemos los días a comprobar
                dias = achievement["dias"]
                minutos = achievement["minutos"]
                cumplido = True
                comprobado = 0
                
                # Comprobamos si al menos una sesión de estudio cumple la condición en cada uno de los dias del objetivo
                while comprobado < dias and cumplido:
                    dia_objetivo = (datetime.now() - timedelta(days=comprobado)).date()
                    dia_cumplido = any(
                        session["session_timestamp"].date() == dia_objetivo and
                        session["duration_minutes"] >= minutos
                        for session in user_sessions
                    )

                    comprobado += 1
                    if not dia_cumplido:
                        cumplido = False

                if cumplido:
                    new_achievements.append(achievement)
                        

    # Agregar los nuevos logros a la base de datos
    for achievement in new_achievements:
        set_user_achievement(user_id, achievement["id"])
    