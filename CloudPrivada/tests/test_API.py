import os
import sys
import requests

# Add the root directory to PYTHONPATH
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '../../CloudPrivada')))

BASE_URL = "http://localhost:8000"

def test_obtener_metodos():
    response = requests.get(f"{BASE_URL}/metodos")
    assert response.status_code == 200
    print("test_obtener_metodos passed:", response.json())

def test_obtener_metodo():
    metodo_id = 1
    response = requests.get(f"{BASE_URL}/metodos/buscar", params={"metodo_id": metodo_id})
    assert response.status_code == 200
    print("test_obtener_metodo passed:", response.json())

def test_obtener_logros():
    response = requests.get(f"{BASE_URL}/logros")
    assert response.status_code == 200
    print("test_obtener_logros passed:", response.json())

def test_obtener_logro():
    logro_id = 1
    response = requests.get(f"{BASE_URL}/logros/buscar", params={"logro_id": logro_id})
    assert response.status_code == 200
    print("test_obtener_logro passed:", response.json())

def test_obtener_logros_usuario():
    usuario_id = 1
    response = requests.get(f"{BASE_URL}/usuario/logros", params={"usuario_id": usuario_id})
    assert response.status_code == 200
    print("test_obtener_logros_usuario passed:", response.json())

def test_agregar_logro_usuario():
    payload = {"user_id": 1, "achievement_id": 1}
    response = requests.post(f"{BASE_URL}/usuario/logros/agregar", json=payload)
    assert response.status_code == 200
    print("test_agregar_logro_usuario passed:", response.json())

def test_obtener_sesiones_usuario():
    usuario_id = 1
    response = requests.get(f"{BASE_URL}/usuario/sesiones", params={"usuario_id": usuario_id})
    assert response.status_code == 200
    print("test_obtener_sesiones_usuario passed:", response.json())

def test_agregar_sesion_usuario():
    payload = {
        "user_id": 1,
        "method_id": 1,
        "session_timestamp": "2023-01-01T10:00:00",
        "duration_minutes": 30,
        "task_type": "task",
        "productivity_level": 5,
        "average_pulse": 70,
        "average_movement": 15
    }
    response = requests.post(f"{BASE_URL}/usuario/sesiones/agregar", json=payload)
    assert response.status_code == 200
    print("test_agregar_sesion_usuario passed:", response.json())

def test_comprobar_logros():
    usuario_id = 1
    response = requests.get(f"{BASE_URL}/comprobar_logros", params={"usuario_id": usuario_id})
    assert response.status_code == 200
    print("test_comprobar_logros passed:", response.json())

if __name__ == "__main__":
    test_obtener_metodos()
    test_obtener_metodo()
    test_obtener_logros()
    test_obtener_logro()
    test_obtener_logros_usuario()
    test_agregar_logro_usuario()
    test_obtener_sesiones_usuario()
    test_agregar_sesion_usuario()
    test_comprobar_logros()

