from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session

from backend.app.database import get_db
from backend.app import schemas
from backend.app import models

router = APIRouter(
    prefix="/danger",
    tags=["Danger Zones"]
)


@router.post("/")
def create_danger_zone(
    danger: schemas.DangerZoneCreate,
    db: Session = Depends(get_db)
):

    new_zone = models.DangerZone(
        title=danger.title,
        description=danger.description,
        latitude=danger.latitude,
        longitude=danger.longitude
    )

    db.add(new_zone)
    db.commit()
    db.refresh(new_zone)

    return new_zone


@router.get("/")
def get_all_danger_zones(
    db: Session = Depends(get_db)
):

    zones = db.query(models.DangerZone).all()

    return zones