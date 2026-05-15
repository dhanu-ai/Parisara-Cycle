# File: app/routes/buddy.py

from fastapi import APIRouter
import random

router = APIRouter(
    prefix="/buddy",
    tags=["Buddy System"]
)

# ---------------------------------------------------
# Dummy Routes (Fake Moving Riders)
# ---------------------------------------------------

rider_routes = {
    "Rahul": [
        {"lat": 12.9716, "lng": 77.5946},
        {"lat": 12.9720, "lng": 77.5950},
        {"lat": 12.9725, "lng": 77.5955},
        {"lat": 12.9730, "lng": 77.5960},
    ],

    "Anjali": [
        {"lat": 12.9690, "lng": 77.5920},
        {"lat": 12.9695, "lng": 77.5925},
        {"lat": 12.9700, "lng": 77.5930},
        {"lat": 12.9705, "lng": 77.5935},
    ],

    "Kiran": [
        {"lat": 12.9740, "lng": 77.5970},
        {"lat": 12.9745, "lng": 77.5975},
        {"lat": 12.9750, "lng": 77.5980},
        {"lat": 12.9755, "lng": 77.5985},
    ]
}

# ---------------------------------------------------
# Current Position Index
# ---------------------------------------------------

rider_index = {
    "Rahul": 0,
    "Anjali": 0,
    "Kiran": 0
}

# ---------------------------------------------------
# GET ACTIVE BUDDIES
# ---------------------------------------------------

@router.get("/")
def get_dummy_buddies():

    buddies = []

    for rider_name, route in rider_routes.items():

        current_idx = rider_index[rider_name]

        current_location = route[current_idx]

        buddies.append({
            "username": rider_name,
            "latitude": current_location["lat"],
            "longitude": current_location["lng"],
            "destination": random.choice([
                "VVIT",
                "Bus Stand",
                "Market",
                "Library"
            ])
        })

        # Move rider to next point
        rider_index[rider_name] = (
            current_idx + 1
        ) % len(route)

    return buddies