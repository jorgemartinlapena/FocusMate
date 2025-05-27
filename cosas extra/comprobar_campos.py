import psycopg2

# Parámetros de conexión
DB_HOST = "34.175.213.152"
DB_PORT = "5432"
DB_NAME = "postgres"
DB_USER = "postgres"
DB_PASSWORD = "focusmate"

# Nombre de la tabla que quieres inspeccionar
table_name = "users"  # Cambia por la tabla que desees consultar

# Conexión
conn = psycopg2.connect(
    host=DB_HOST,
    port=DB_PORT,
    dbname=DB_NAME,
    user=DB_USER,
    password=DB_PASSWORD
)
cur = conn.cursor()

# Consulta para obtener columnas
cur.execute("""
    SELECT column_name, data_type, is_nullable, column_default
    FROM information_schema.columns
    WHERE table_schema = 'fmSchema' AND table_name = %s;
""", (table_name,))

# Mostrar resultados
columns = cur.fetchall()
print(f"Columnas de la tabla '{table_name}' en el schema 'fmSchema':\n")
for col in columns:
    print(f"Nombre: {col[0]}, Tipo: {col[1]}, Nulo: {col[2]}, Por defecto: {col[3]}")

# Cierre
cur.close()
conn.close()
