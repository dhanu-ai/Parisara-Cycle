from pydantic import BaseModel


# -----------------------------
# Danger Zone
# -----------------------------

class DangerZoneCreate(BaseModel):
    title: str
    description: str
    latitude: float
    longitude: float


class DangerZoneResponse(DangerZoneCreate):
    id: int

    class Config:
        from_attributes = True


# -----------------------------
# Eco Stats
# -----------------------------

class EcoStatCreate(BaseModel):
    username: str
    distance_km: float


class EcoStatResponse(BaseModel):
    id: int
    username: str
    distance_km: float
    co2_saved: float

    class Config:
        from_attributes = True


# -----------------------------
# Buddy System
# -----------------------------

class BuddyCreate(BaseModel):
    username: str
    latitude: float
    longitude: float
    destination: str


class BuddyResponse(BuddyCreate):
    id: int

    class Config:
        from_attributes = True