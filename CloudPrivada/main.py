from fastapi import FastAPI # type: ignore
from app.api.routes import router

app = FastAPI()
app.include_router(router)
