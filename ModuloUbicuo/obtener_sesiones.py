import psycopg2

# Parámetros de conexión
DB_HOST = "34.175.213.152"
DB_PORT = "5432"
DB_NAME = "postgres"
DB_USER = "postgres"
DB_PASSWORD = "focusmate"

def obtener_sesiones_usuario(usuario_id):
    try:
        # Conectar a la base de datos
        conn = psycopg2.connect(
            host=DB_HOST,
            port=DB_PORT,
            dbname=DB_NAME,
            user=DB_USER,
            password=DB_PASSWORD
        )
        with conn.cursor() as cur:
            # Establecer el esquema
            cur.execute('SET search_path TO "fmSchema";')

            # Consultar sesiones del usuario
            cur.execute("""
                SELECT id, user_id, method_id, session_timestamp, duration_minutes, task_type, 
                       productivity_level, average_pulse, average_movement, concentration_level
                FROM user_sessions
                WHERE user_id = %s
                ORDER BY session_timestamp DESC;
            """, (usuario_id,))
            
            sesiones = cur.fetchall()
            for sesion in sesiones:
                print({
                    "id": sesion[0],
                    "user_id": sesion[1],
                    "method_id": sesion[2],
                    "session_timestamp": sesion[3],
                    "duration_minutes": sesion[4],
                    "task_type": sesion[5],
                    "productivity_level": sesion[6],
                    "average_pulse": sesion[7],
                    "average_movement": sesion[8],
                    "concentration_level": sesion[9]
                })

    except Exception as e:
        print("Error al obtener sesiones:", e)

    finally:
        if 'conn' in locals():
            conn.close()

if __name__ == "__main__":
    usuario_id = 1  # ID del usuario
    obtener_sesiones_usuario(usuario_id)
