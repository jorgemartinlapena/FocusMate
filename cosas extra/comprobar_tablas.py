import psycopg2

# Parámetros de conexión
DB_HOST = "34.175.213.152"
DB_PORT = "5432"
DB_NAME = "postgres"
DB_USER = "postgres"
DB_PASSWORD = "focusmate"

# Conexión
conn = psycopg2.connect(
    host=DB_HOST,
    port=DB_PORT,
    dbname=DB_NAME,
    user=DB_USER,
    password=DB_PASSWORD
)
cur = conn.cursor()

# Consulta para obtener todas las tablas del esquema 'fmSchema'
cur.execute("""
    SELECT table_name
    FROM information_schema.tables
    WHERE table_schema = 'fmSchema'
    ORDER BY table_name;
""")

# Mostrar resultados
tables = cur.fetchall()
print("Tablas en el schema 'fmSchema':\n")
for table in tables:
    print(f"- {table[0]}")

# Cierre
cur.close()
conn.close()