import psycopg2
from psycopg2 import sql

# Parámetros de conexión
DB_HOST = "34.175.213.152"
DB_PORT = "5432"
DB_NAME = "postgres"
DB_USER = "postgres"
DB_PASSWORD = "focusmate"

# Nombre de la tabla que quieres consultar
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

# Consulta para obtener todos los datos de la tabla
query = sql.SQL("SELECT * FROM {}.{}").format(
    sql.Identifier('fmSchema'),
    sql.Identifier(table_name)
)
cur.execute(query)

# Obtener nombres de columnas
column_names = [desc[0] for desc in cur.description]

# Mostrar resultados
rows = cur.fetchall()
print(f"Datos de la tabla '{table_name}' en el schema 'fmSchema':\n")
print("Columnas:", ", ".join(column_names))
print("-" * 50)

for row in rows:
    for i, value in enumerate(row):
        print(f"{column_names[i]}: {value}")
    print("-" * 30)

print(f"\nTotal de registros: {len(rows)}")

# Cierre
cur.close()
conn.close()