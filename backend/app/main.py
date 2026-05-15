from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from app.database import engine
from app.models import Base

from app.routes import buddy
from app.routes import danger
from app.routes import eco


# ---------------------------------------------------
# CREATE DATABASE TABLES
# ---------------------------------------------------

Base.metadata.create_all(bind=engine)


# ---------------------------------------------------
# FASTAPI APP
# ---------------------------------------------------

app = FastAPI(
    title="Parisara Cycle API"
)


# ---------------------------------------------------
# ENABLE CORS
# ---------------------------------------------------

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


# ---------------------------------------------------
# INCLUDE ROUTES
# ---------------------------------------------------

app.include_router(buddy.router)

app.include_router(danger.router)

app.include_router(eco.router)


# ---------------------------------------------------
# ROOT
# ---------------------------------------------------

@app.get("/")
def root():

    return {
        "message": "Parisara Cycle Backend Running"
    }


# ---------------------------------------------------
# HEALTH CHECK
# ---------------------------------------------------

@app.get("/health")
def health():

    return {
        "status": "healthy"
    }