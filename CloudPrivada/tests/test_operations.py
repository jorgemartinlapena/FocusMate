import os
import sys

# Add the root directory to PYTHONPATH
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '../../CloudPrivada')))

from app.models.achievements import get_achievements, get_achievement_by_id
from app.models.methods import get_methods, get_method_by_id
from app.models.user_sessions import get_user_sessions, set_user_session
from app.models.user_achievements import get_user_achievements, set_user_achievement
from app.services.functions import check_and_update_achievements

def main():
    print("=== Validación manual de operaciones ===")

    # Validar métodos de estudio
    print("\n--- Métodos de Estudio ---")
    print("Obteniendo todos los métodos:")
    print(get_methods())
    print("Obteniendo un método específico (ID=1):")
    print(get_method_by_id(1))

    # Validar logros
    print("\n--- Logros ---")
    print("Obteniendo todos los logros:")
    print(get_achievements())
    print("Obteniendo un logro específico (ID=1):")
    print(get_achievement_by_id(1))

    # Validar sesiones de usuario
    print("\n--- Sesiones de Usuario ---")
    print("Agregando una nueva sesión para el usuario ID=1:")
    set_user_session(1, 1, "2023-01-01T10:00:00", 30, "task", 5)
    print("Obteniendo todas las sesiones del usuario ID=1:")
    print(get_user_sessions(1))

    # Validar logros de usuario
    print("\n--- Logros de Usuario ---")
    print("Agregando un logro al usuario ID=1:")
    set_user_achievement(1, 1)
    print("Obteniendo todos los logros del usuario ID=1:")
    print(get_user_achievements(1))

    # Validar comprobación de logros
    print("\n--- Comprobación de Logros ---")
    print("Comprobando y actualizando logros para el usuario ID=1:")
    check_and_update_achievements(1)
    print("Logros del usuario después de la comprobación:")
    print(get_user_achievements(1))

if __name__ == "__main__":
    main()

