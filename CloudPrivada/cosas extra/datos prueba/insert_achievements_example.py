import psycopg2
from psycopg2 import sql

# Datos de conexión
DB_HOST = "34.175.213.152"
DB_PORT = "5432"
DB_NAME = "postgres"
DB_USER = "postgres"
DB_PASSWORD = "focusmate"

# Logros a insertar
logros = [
    ("Primer Paso", "Has estudiado durante al menos 30 minutos. ¡Buen comienzo!", 0, 30, "total"),
    ("Foco en Marcha", "Completaste una sesión de 90 minutos. ¡Gran concentración!", 0, 90, "sesion"),
    ("Hora Productiva", "Acumulaste 60 minutos de estudio. ¡Sigue así!", 0, 60, "total"),
    ("Cien Minutos", "Llegaste a los 100 minutos de estudio. ¡Gran avance!", 0, 100, "total"),
    ("Resistencia", "Sesión de estudio de más de 120 minutos completada.", 0, 120, "sesion"),
    ("Pomodoro Master", "Completaste al menos 4 ciclos de pomodoro.", 0, 100, "sesion"),
    ("Maratón de Estudio", "Has alcanzado las 5 horas de estudio acumuladas.", 0, 300, "total"),
    ("Concentración Legendaria", "Realizaste una sesión de más de 3 horas seguidas.", 0, 180, "sesion"),
    ("En Racha", "Estudiaste al menos 30 minutos durante 3 días seguidos.", 3, 30, "consistencia"),
    ("Estudioso Diario", "Estudiaste 30 minutos durante todos los días durante una semana.", 7, 30, "consistencia"),
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

        # Insertar logros
        insert_query = """
            INSERT INTO achievements (nombre, descripcion, dias, minutos, tipo)
            VALUES (%s, %s, %s, %s, %s)
        """

        cur.executemany(insert_query, logros)

        print("Logros insertados correctamente.")

except Exception as e:
    print("Error al insertar logros:", e)

finally:
    if 'conn' in locals():
        conn.close()
