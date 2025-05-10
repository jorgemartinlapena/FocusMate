from datetime import datetime, timedelta
from app.models.achievements import get_achievements
from app.models.user_achievements import get_user_achievements, set_user_achievement
from app.models.user_sessions import get_user_sessions

# Comprueba y actualiza logros nuevos
def check_and_update_achievements(user_id: int):
    all_achievements = get_achievements()
    user_achievements = get_user_achievements(user_id)
    user_sessions = get_user_sessions(user_id)

    new_achievements = []
    user_achievement_ids = {achievement["achievement_id"] for achievement in user_achievements}

    for achievement in all_achievements:
        # Comprobamos si el logro ya ha sido desbloqueado por el usuario
        if achievement["id"] not in user_achievement_ids :
            if achievement["tipo"] == "total":
                # verificamos si el usuario ha alcanzado el tiempo total requerido
                total_time = sum(session["duration_minutes"] for session in user_sessions)
                if total_time >= achievement["minutos"]:
                    new_achievements.append(achievement)

            elif achievement["tipo"] == "sesion":
                # obtenemos el tiempo de las última sesión, que debería ser la primera
                tiempo_ultima_sesion = user_sessions[0]["duration_minutes"] if user_sessions else 0
                if tiempo_ultima_sesion >= achievement["minutos"]:
                    new_achievements.append(achievement)
                

            elif achievement["tipo"] == "consistencia":
                # obtenemos los días a comprobar
                dias = achievement["dias"]
                minutos = achievement["minutos"]
                cumplido = True
                comprobado = 0
                
                # Comprobamos si al menos una sesión de estudio cumple la condición en cada uno de los dias del objetivo
                while comprobado < dias and cumplido:
                    dia_objetivo = (datetime.now() - timedelta(days=comprobado)).date()
                    dia_cumplido = any(
                        session["session_timestamp"].date() == dia_objetivo and
                        session["duration_minutes"] >= minutos
                        for session in user_sessions
                    )

                    comprobado += 1
                    if not dia_cumplido:
                        cumplido = False

                if cumplido:
                    new_achievements.append(achievement)
                        

    # Agregar los nuevos logros a la base de datos
    for achievement in new_achievements:
        set_user_achievement(user_id, achievement["id"])
    
def estimate_concentration(average_pulse: int, average_movement: int):
    # Valores ideales
    ideal_pulse = 70
    ideal_movement = 15

    # Ponderaciones para pulso y movimiento
    pulse_weight = 0.6
    movement_weight = 0.4

    # Cálculo de desviaciones normalizadas
    pulse_deviation = max(0, 100 - abs(average_pulse - ideal_pulse) * 2)
    movement_deviation = max(0, 100 - abs(average_movement - ideal_movement) * 5)

    # Cálculo de concentración como combinación ponderada
    valor = (pulse_deviation * pulse_weight) + (movement_deviation * movement_weight)

    # Limitar el valor entre 0 y 100 para cumplor las condiciones de la base de datos
    return min(100, max(0, round(valor)))
