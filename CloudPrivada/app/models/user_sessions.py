from app.db.connection import get_db_connection
from app.functions.estimate_concentration import estimate_concentration

# Devuelve las sesiones de estudio de un usuario especifico
def get_user_sessions(user_id: int):
    with get_db_connection() as conn:
        with conn.cursor() as cursor:
            cursor.execute('SET search_path TO "fmSchema";')
            cursor.execute("""
                SELECT id, user_id, method_id, session_timestamp, duration_minutes, task_type, productivity_level, average_pulse, average_movement, concentration_level
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
                    "productivity_level": row[6],
                    "average_pulse": row[7],
                    "average_movement": row[8],
                    "concentration_level": row[9]
                } for row in cursor.fetchall()
            ]

# Agrega una nueva sesión de estudio para un usuario
def set_user_session(user_id: int, method_id: int, session_timestamp: str, duration_minutes: int, 
                     task_type: str, productivity_level: int, average_pulse: int = None, 
                     average_movement: int = None):
    with get_db_connection() as conn:
        with conn.cursor() as cursor:
            cursor.execute('SET search_path TO "fmSchema";')
            
            if average_pulse is None or average_movement is None:
                cursor.execute("""
                    INSERT INTO user_sessions (user_id, method_id, session_timestamp, duration_minutes, task_type, productivity_level)
                    VALUES (%s, %s, %s, %s, %s, %s);
                """, (user_id, method_id, session_timestamp, duration_minutes, task_type, productivity_level))
            else:
                concentration_level = estimate_concentration(average_pulse, average_movement)

                cursor.execute("""
                    INSERT INTO user_sessions (user_id, method_id, session_timestamp, duration_minutes, task_type, productivity_level, average_pulse, average_movement, concentration_level)
                    VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s);
                """, (user_id, method_id, session_timestamp, duration_minutes, task_type, productivity_level, average_pulse, average_movement, concentration_level))
            
            conn.commit()
            return {"message": "Sesión de estudio agregada exitosamente."}