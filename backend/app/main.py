from fastapi import FastAPI

from backend.app.routes import buddy
from backend.app.routes import danger
from backend.app.routes import eco

app = FastAPI(
    title="Parisara Cycle API"
)

app.include_router(buddy.router)
app.include_router(danger.router)
app.include_router(eco.router)

@app.get("/")
def root():
    return {
        "message": "Parisara Cycle Backend Running"
    }