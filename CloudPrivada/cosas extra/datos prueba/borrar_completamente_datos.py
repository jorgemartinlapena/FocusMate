import psycopg2

# Parámetros de conexión
DB_HOST = "34.175.213.152"
DB_PORT = "5432"
DB_NAME = "postgres"
DB_USER = "postgres"
DB_PASSWORD = "focusmate"

# Orden correcto de borrado (por dependencias)
tables = [
    "user_achievements",
    "user_sessions",
    "achievements",
    "metodos_estudio",
    "users"
]

try:
    conn = psycopg2.connect(
        host=DB_HOST,
        port=DB_PORT,
        dbname=DB_NAME,
        user=DB_USER,
        password=DB_PASSWORD
    )
    cur = conn.cursor()
    cur.execute('SET search_path TO "fmSchema";')

    for table in tables:
        print(f"Borrando datos de '{table}'...")
        cur.execute(f'DELETE FROM {table};')

    conn.commit()
    print("✅ Todos los datos han sido eliminados correctamente.")

except Exception as e:
    print("❌ Error al borrar los datos:", e)

finally:
    if cur:
        cur.close()
    if conn:
        conn.close()
