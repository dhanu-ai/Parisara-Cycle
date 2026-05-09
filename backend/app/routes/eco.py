from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session

from backend.app.database import get_db
from backend.app import schemas
from backend.app import models

router = APIRouter(
    prefix="/eco",
    tags=["Eco Stats"]
)


CO2_PER_KM = 120


@router.post("/")
def add_eco_stat(
    eco: schemas.EcoStatCreate,
    db: Session = Depends(get_db)
):

    co2_saved = eco.distance_km * CO2_PER_KM

    stat = models.EcoStat(
        username=eco.username,
        distance_km=eco.distance_km,
        co2_saved=co2_saved
    )

    db.add(stat)
    db.commit()
    db.refresh(stat)

    return stat


@router.get("/{username}")
def get_user_eco_stats(
    username: str,
    db: Session = Depends(get_db)
):

    stats = db.query(models.EcoStat).filter(
        models.EcoStat.username == username
    ).all()

    total_distance = sum(s.distance_km for s in stats)
    total_co2 = sum(s.co2_saved for s in stats)

    return {
        "username": username,
        "total_distance": total_distance,
        "total_co2_saved": total_co2,
        "history": stats
    }