import os
from app.models.achievements import *
from app.models.methods import *
from app.models.user_achievements import *
from app.models.user_sessions import *
from app.services.functions import *

# Imprimir todos los logros
def test_get_achievements():
    print("Probando obtener todos los logros:")
    logros = get_achievements()
    for logro in logros:
        print(logro)

# Imprimir los detalles de un logro específico
def test_get_achievent_by_id(achievement_id):
    print(f"\nProbando obtener un logro con id {achievement_id}:")
    logro = get_achievement_by_id(achievement_id)
    print(logro)

# Imprimir todos los métodos de estudio
def test_get_methods():
    print("\nProbando obtener todos los métodos de estudio:")
    metodos = get_methods()
    for metodo in metodos:
        print(metodo)

# Imprimir los detalles de un método de estudio específico
def test_get_method_by_id(method_id):
    print(f"\nProbando obtener un método con id {method_id}:")
    metodo = get_method_by_id(method_id)
    print(metodo)

# Probar obtener las sesiones de un usuario
def test_get_user_sessions(user_id):
    print(f"\nProbando obtener sesiones de usuario con id {user_id}:")
    sesiones = get_user_sessions(user_id)
    for sesion in sesiones:
        print(sesion)

# Probar agregar una nueva sesión de usuario
def test_set_user_session(user_id, method_id, session_timestamp, duration_minutes, task_type, productivity_level):
    print(f"\nProbando agregar una sesión para el usuario {user_id}:")
    set_user_session(user_id, method_id, session_timestamp, duration_minutes, task_type, productivity_level)
    print("Sesión agregada correctamente.")

# Probar obtener los logros de un usuario
def test_get_user_achievements(user_id):
    print(f"\nProbando obtener logros de usuario con id {user_id}:")
    logros_usuario = get_user_achievements(user_id)
    for logro in logros_usuario:
        print(logro)

# Probar agregar un nuevo logro a un usuario
def test_set_user_achievement(user_id, achievement_id):
    print(f"\nProbando agregar un logro {achievement_id} al usuario {user_id}:")
    set_user_achievement(user_id, achievement_id)
    print("Logro agregado correctamente.")

# Probar la función que verifica y actualiza los logros de un usuario
def test_check_and_update_achievements(user_id):
    print(f"\nProbando la comprobación y actualización de logros para el usuario {user_id}:")
    print("Logros antes de la actualización:")
    logros_usuario = get_user_achievements(user_id)
    for logro in logros_usuario:
        print(logro)
    # Llamar a la función para verificar y actualizar logros
    check_and_update_achievements(user_id)
    print("Logros actualizados correctamente.")
    print("Logros después de la actualización:")
    logros_usuario_actualizados = get_user_achievements(user_id)
    for logro in logros_usuario_actualizados:
        print(logro)

# Ejecutar todas las pruebas
if __name__ == "__main__":
    # ID de usuario para las pruebas
    user_id = 2
    # ID de logro y método para las pruebas
    achievement_id = 1
    method_id = 1

    # Ejecutar las funciones de prueba
    test_get_achievements()
    test_get_achievent_by_id(achievement_id)
    test_get_methods()
    test_get_method_by_id(method_id)
    test_get_user_sessions(user_id)
    test_set_user_session(user_id, method_id, "2025-05-06 14:00:00", 90, "estudio", 5)
    test_get_user_sessions(user_id)
    test_get_user_achievements(user_id)
    test_set_user_achievement(user_id, achievement_id)
    test_check_and_update_achievements(user_id)
