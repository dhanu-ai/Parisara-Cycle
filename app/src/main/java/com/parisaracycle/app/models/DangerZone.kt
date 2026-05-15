package com.parisaracycle.app.models

data class DangerZone(
    val id: Int,
    val title: String,
    val description: String,
    val issue_type: String,
    val severity: String,
    val latitude: Double,
    val longitude: Double
)