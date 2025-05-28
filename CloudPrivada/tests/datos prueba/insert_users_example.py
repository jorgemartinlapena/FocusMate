import psycopg2
from psycopg2 import sql
import bcrypt

# Datos de conexión
DB_HOST = "34.175.213.152"
DB_PORT = "5432"
DB_NAME = "postgres"
DB_USER = "postgres"
DB_PASSWORD = "focusmate"

# Usuarios a insertar: (username, email, password)
usuarios = [
    ("ana_sanchez", "ana.sanchez@example.com", "contraseña123"),
    ("carlos_87", "carlos.87@example.com", "contraseña123"),
    ("lucia_m", "lucia.m@example.com", "contraseña123"),
    ("jose.gomez", "jose.gomez@example.com", "contraseña123"),
    ("marta.dev", "marta.dev@example.com", "contraseña123"),
    ("pablo.rdz", "pablo.rdz@example.com", "contraseña123"),
    ("elena_torres", "elena.torres@example.com", "contraseña123"),
    ("raul123", "raul123@example.com", "contraseña123"),
    ("david.pm", "david.pm@example.com", "contraseña123"),
    ("laura.q", "laura.q@example.com", "contraseña123"),
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

        # Insertar usuarios con contraseña cifrada
        insert_query = """
            INSERT INTO users (username, email, password_hash)
            VALUES (%s, %s, %s)
            ON CONFLICT (username) DO NOTHING;
        """

        usuarios_con_hash = [
            (u, e, bcrypt.hashpw(p.encode('utf-8'), bcrypt.gensalt()).decode('utf-8'))
            for u, e, p in usuarios
        ]

        cur.executemany(insert_query, usuarios_con_hash)

        print("Usuarios insertados correctamente.")

except Exception as e:
    print("Error al insertar usuarios:", e)

finally:
    if 'conn' in locals():
        conn.close()
