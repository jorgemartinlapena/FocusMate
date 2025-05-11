import psycopg2
from psycopg2 import sql

# Datos de conexión
DB_HOST = "34.175.213.152"
DB_PORT = "5432"
DB_NAME = "postgres"
DB_USER = "postgres"
DB_PASSWORD = "focusmate"

# Metodos a insertar
metodos = [
    ("Pomodoro Clásico", 4, 25, 5, 15, "Técnica pomodoro con 4 bloques de 25 min y descanso largo.", 100),
    ("Técnica 52/17", 3, 52, 17, 0, "Sesiones largas y enfocadas de 52 minutos con descansos.", 156),
    ("Microbloques", 6, 10, 2, 5, "Estudio en bloques muy cortos, ideal para periodos cortos.", 60),
    ("Técnica 90/20", 2, 90, 20, 0, "Bloques largos de estudio profundo con descanso largo.", 180),
    ("Pomodoro Extendido", 4, 40, 10, 20, "Variante del pomodoro con más tiempo por bloque.", 160),
    ("Estudio Relámpago", 2, 20, 5, 0, "Sesión rápida para repasar o tareas ligeras.", 40),
    ("Rutina Matutina", 3, 30, 10, 10, "Pensada para primeras horas del día, buen equilibrio.", 90),
    ("Maratón de Estudio", 4, 45, 15, 30, "Para jornadas intensivas, con descansos regulares.", 180),
    ("Sprint Pre-examen", 3, 20, 5, 10, "Estudio breve antes de una prueba, centrado en repasar.", 60),
    ("Estudio Vespertino", 2, 35, 10, 15, "Diseñada para sesiones después del almuerzo.", 70),
]

try:
    # Conectar a la base de datos
    conn = psycopg2.connect(
        host=DB_HOST,
        port=DB_PORT,
        dbname=DB_NAME,
        user=DB_USER,
        password=DB_PASSWORD
    )
    conn.autocommit = True  # Para evitar tener que hacer commit manual

    with conn.cursor() as cur:
        # Establecer el esquema
        cur.execute('SET search_path TO "fmSchema";')

        # Insertar metodos
        insert_query = """
            INSERT INTO metodos_estudio (nombre, repeticiones, tiempo_estudio, tiempo_descanso, tiempo_descanso_final, descripcion, tiempo_total_estudio)
            VALUES (%s, %s, %s, %s, %s, %s, %s)
        """

        cur.executemany(insert_query, metodos)

        print("Metodos insertados correctamente.")

except Exception as e:
    print("Error al insertar metodos:", e)

finally:
    if 'conn' in locals():
        conn.close()
