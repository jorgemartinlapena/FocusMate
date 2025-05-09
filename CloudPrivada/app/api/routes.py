from fastapi import APIRouter # type: ignore
from app.models.achievements import get_achievements, get_achievement_by_id
from app.models.methods import get_methods, get_method_by_id
from app.models.user_sessions import get_user_sessions, set_user_session
from app.models.user_achievements import get_user_achievements, set_user_achievement
from app.services.functions import check_and_update_achievements

router = APIRouter()

@router.get("/metodos")
def obtener_metodos():
    return {"metodos": get_methods()}

@router.get("/metodos/buscar")
def obtener_metodo(metodo_id: int):
    metodo = get_method_by_id(metodo_id)
    if metodo:
        return metodo
    return {"error": "Método no encontrado"}

@router.get("/logros")
def obtener_logros():
    return get_achievements()

@router.get("/logros/buscar")
def obtener_logro(logro_id: int):
    logro = get_achievement_by_id(logro_id)
    if logro:
        return logro
    return {"error": "Logro no encontrado"}

@router.get("/usuario/logros")
def obtener_logros_usuario(usuario_id: int):
    return get_user_achievements(usuario_id)

@router.post("/usuario/logros/agregar")
def agregar_logro_usuario(user_id: int, achievement_id: int):
    return set_user_achievement(user_id, achievement_id)

@router.get("/usuario/sesiones")
def obtener_sesiones_usuario(usuario_id: int):
    return get_user_sessions(usuario_id)

@router.post("/usuario/sesiones/agregar")
def agregar_sesion_usuario(user_id: int, method_id: int, session_timestamp: str, duration_minutes: int, task_type: str, productivity_level: int):
    return set_user_session(user_id, method_id, session_timestamp, duration_minutes, task_type, productivity_level)

@router.get("/comprobar_logros")
def comprobar_logros(usuario_id: int):
    return check_and_update_achievements(usuario_id)