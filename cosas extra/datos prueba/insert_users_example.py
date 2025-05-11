import psycopg2
from psycopg2 import sql

# Datos de conexión
DB_HOST = "34.175.213.152"
DB_PORT = "5432"
DB_NAME = "postgres"
DB_USER = "postgres"
DB_PASSWORD = "focusmate"

# Usuarios a insertar
usuarios = [
    ("ana_sanchez", "ana.sanchez@example.com"),
    ("carlos_87", "carlos.87@example.com"),
    ("lucia_m", "lucia.m@example.com"),
    ("jose.gomez", "jose.gomez@example.com"),
    ("marta.dev", "marta.dev@example.com"),
    ("pablo.rdz", "pablo.rdz@example.com"),
    ("elena_torres", "elena.torres@example.com"),
    ("raul123", "raul123@example.com"),
    ("david.pm", "david.pm@example.com"),
    ("laura.q", "laura.q@example.com"),
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

        # Insertar usuarios
        insert_query = """
            INSERT INTO users (username, email)
            VALUES (%s, %s)
            ON CONFLICT (username) DO NOTHING;
        """

        cur.executemany(insert_query, usuarios)

        print("Usuarios insertados correctamente.")

except Exception as e:
    print("Error al insertar usuarios:", e)

finally:
    if 'conn' in locals():
        conn.close()
