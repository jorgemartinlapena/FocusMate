from app.db.connection import get_db_connection

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
            return {"message": "Logro desbloqueado exitosamente."}