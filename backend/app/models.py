from sqlalchemy import Column, Integer, String, Float, DateTime
from datetime import datetime

from .database import Base


# ---------------------------------------------------
# DANGER ZONES
# ---------------------------------------------------

class DangerZone(Base):
    __tablename__ = "danger_zones"

    id = Column(Integer, primary_key=True, index=True)

    title = Column(String)
    description = Column(String)

    issue_type = Column(String)
    severity = Column(String)

    latitude = Column(Float)
    longitude = Column(Float)

    created_at = Column(
        DateTime,
        default=datetime.utcnow
    )


# ---------------------------------------------------
# ECO STATS
# ---------------------------------------------------

class EcoStat(Base):
    __tablename__ = "eco_stats"

    id = Column(Integer, primary_key=True, index=True)

    username = Column(String)

    distance_km = Column(Float)

    co2_saved = Column(Float)

    created_at = Column(
        DateTime,
        default=datetime.utcnow
    )


# ---------------------------------------------------
# BUDDY LOCATIONS
# ---------------------------------------------------

class BuddyLocation(Base):
    __tablename__ = "buddy_locations"

    id = Column(Integer, primary_key=True, index=True)

    username = Column(String)

    latitude = Column(Float)

    longitude = Column(Float)

    destination = Column(String)