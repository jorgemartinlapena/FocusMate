from fastapi import APIRouter, Body # type: ignore
from app.models.achievements import get_achievements, get_achievement_by_id
from app.models.methods import get_methods, get_method_by_id
from app.models.user_sessions import get_user_sessions, set_user_session
from app.models.user_achievements import get_user_achievements, set_user_achievement
from app.functions.estimate_concentration import estimate_concentration
from app.functions.update_achievements import check_and_update_achievements

router = APIRouter()

@router.get("/metodos")
def obtener_metodos():
    return {"metodos": get_methods()}

@router.get("/metodos/buscar")
def obtener_metodo(method_id: int):
    metodo = get_method_by_id(method_id)
    if metodo:
        return metodo
    return {"error": "Método no encontrado"}

@router.get("/logros")
def obtener_logros():
    return get_achievements()

@router.get("/logros/buscar")
def obtener_logro(achievement_id: int):
    logro = get_achievement_by_id(achievement_id)
    if logro:
        return logro
    return {"error": "Logro no encontrado"}

@router.get("/usuario/logros")
def obtener_logros_usuario(user_id: int):
    return get_user_achievements(user_id)

@router.post("/usuario/logros/agregar")
def agregar_logro_usuario(data: dict = Body(...)):
    user_id = data.get("user_id")
    achievement_id = data.get("achievement_id")
    return set_user_achievement(user_id, achievement_id)

@router.get("/usuario/sesiones")
def obtener_sesiones_usuario(user_id: int):
    return get_user_sessions(user_id)

@router.post("/usuario/sesiones/agregar")
def agregar_sesion_usuario(data: dict = Body(...)):
    user_id = data.get("user_id")
    method_id = data.get("method_id")
    session_timestamp = data.get("session_timestamp")
    duration_minutes = data.get("duration_minutes")
    task_type = data.get("task_type")
    productivity_level = data.get("productivity_level")
    average_pulse = data.get("average_pulse", None)
    average_movement = data.get("average_movement", None)
    return set_user_session(user_id, method_id, session_timestamp, duration_minutes, task_type, productivity_level, average_pulse, average_movement)

@router.get("/comprobar_logros")
def comprobar_logros(user_id: int):
    return check_and_update_achievements(user_id)

@router.get("/estimar_concentracion")
def estimar_concentracion(average_pulse: int, average_movement: int):
    return estimate_concentration(average_pulse, average_movement)