from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session

from app.database import get_db
from app import schemas
from app import models

router = APIRouter(
    prefix="/danger",
    tags=["Danger Zones"]
)


# ---------------------------------------------------
# CREATE DANGER ZONE
# ---------------------------------------------------

@router.post("/")
def create_danger_zone(
    danger: schemas.DangerZoneCreate,
    db: Session = Depends(get_db)
):

    new_zone = models.DangerZone(
        title=danger.title,
        description=danger.description,
        issue_type=danger.issue_type,
        severity=danger.severity,
        latitude=danger.latitude,
        longitude=danger.longitude
    )

    db.add(new_zone)

    db.commit()

    db.refresh(new_zone)

    return new_zone


# ---------------------------------------------------
# GET ALL DANGER ZONES
# ---------------------------------------------------

@router.get("/")
def get_all_danger_zones(
    db: Session = Depends(get_db)
):

    zones = db.query(
        models.DangerZone
    ).all()

    return zones


# ---------------------------------------------------
# DELETE DANGER ZONE
# ---------------------------------------------------

@router.delete("/{zone_id}")
def delete_danger_zone(
    zone_id: int,
    db: Session = Depends(get_db)
):

    zone = db.query(
        models.DangerZone
    ).filter(
        models.DangerZone.id == zone_id
    ).first()

    if not zone:

        return {
            "error": "Danger zone not found"
        }

    db.delete(zone)

    db.commit()

    return {
        "message": "Danger zone deleted successfully"
    }