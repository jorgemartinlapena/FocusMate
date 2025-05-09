from app.db.connection import get_db_connection

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
