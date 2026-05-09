from sqlalchemy import Column, Integer, String, Float

from .database import Base


class DangerZone(Base):
    __tablename__ = "danger_zones"

    id = Column(Integer, primary_key=True, index=True)
    title = Column(String)
    description = Column(String)
    latitude = Column(Float)
    longitude = Column(Float)


class EcoStat(Base):
    __tablename__ = "eco_stats"

    id = Column(Integer, primary_key=True, index=True)
    username = Column(String)
    distance_km = Column(Float)
    co2_saved = Column(Float)


class BuddyLocation(Base):
    __tablename__ = "buddy_locations"

    id = Column(Integer, primary_key=True, index=True)
    username = Column(String)
    latitude = Column(Float)
    longitude = Column(Float)
    destination = Column(String)