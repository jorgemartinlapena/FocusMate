from app.db.connection import get_db_connection

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
