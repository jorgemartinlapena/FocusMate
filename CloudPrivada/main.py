from fastapi import FastAPI, Query
from app.dataBase import *

app = FastAPI()

@app.get("/metodos")
def obtener_metodos():
    return {"metodos": get_methods()}

@app.get("/metodos/buscar")
def obtener_metodo(metodo_id: int):
    metodo = get_method_by_id(metodo_id)
    if metodo:
        return metodo
    return {"error": "Método no encontrado"}

@app.get("/logros")
def obtener_logros():
    return get_achievements()

@app.get("/logros/buscar")
def obtener_logro(logro_id: int):
    logro = get_achievent_by_id(logro_id)
    if logro:
        return logro
    return {"error": "Logro no encontrado"}

@app.get("/usuario/logros")
def obtener_logros_usuario(usuario_id: int):
    return get_user_achievements(usuario_id)

@app.get("/usuario/logros/agregar")
def agregar_logro_usuario(user_id: int, method_id: int, session_timestamp: str, duration_minutes: int, task_type: str, productivity_level: int):
    return set_user_session(user_id, method_id, session_timestamp, duration_minutes, task_type, productivity_level)

@app.get("/usuario/sesiones")
def obtener_sesiones_usuario(usuario_id: int):
    return get_user_sessions(usuario_id)

@app.get("/usuario/sesiones/agregar")
def agregar_sesion_usuario(user_id: int, achievement_id: int):
    return set_user_achievement(user_id, achievement_id)

@app.get("/comprobar_logros")
def comprobar_logros(usuario_id: int):
    return check_and_update_achievements(usuario_id)

