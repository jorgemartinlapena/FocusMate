import random
import requests
from requests.exceptions import ConnectionError
from datetime import datetime

BASE_URL = "http://localhost:8000"

def generar_datos_aleatorios():
    # Generar datos aleatorios dentro de los rangos existentes
    average_pulse = random.randint(60, 100)  # Pulso promedio (ejemplo: 60-100 bpm)
    average_movement = random.randint(0, 100)  # Movimiento promedio (ejemplo: 0-30 unidades)
    return average_pulse, average_movement

def enviar_sesion(usuario_id, metodo_id):
    average_pulse, average_movement = generar_datos_aleatorios()
    payload = {
        "user_id": usuario_id,
        "method_id": metodo_id,
        "session_timestamp": datetime.now().isoformat(),
        "duration_minutes": random.randint(10, 120),  # Duración aleatoria entre 10 y 120 minutos
        "task_type": random.choice(["lectura", "creativa", "repetitiva"]),  # Tipo de tarea aleatoria
        "productivity_level": random.randint(1, 5),  # Nivel de productividad (1-5)
        "average_pulse": average_pulse,
        "average_movement": average_movement
    }
    try:
        response = requests.post(f"{BASE_URL}/usuario/sesiones/agregar", json=payload)
        if response.status_code == 200:
            print("Sesión enviada correctamente. Datos enviados:", payload)
        else:
            print("Error al enviar la sesión:", response.status_code, response.text)
    except ConnectionError as e:
        print("Error de conexión al servidor:", e)

if __name__ == "__main__":
    usuario_id = 1  # ID del usuario
    metodo_id = 1   # ID del método de estudio
    enviar_sesion(usuario_id, metodo_id)
