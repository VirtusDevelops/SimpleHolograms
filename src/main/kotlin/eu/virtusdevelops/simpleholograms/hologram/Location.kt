package eu.virtusdevelops.simpleholograms.hologram

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

data class Location(
    var x: Double,
    var y: Double,
    var z: Double,
    var world: String
) {

    fun getDistance(location: Location): Double{
        return abs((x - location.x).pow( 2) + (y - location.y).pow(2) + (z - location.z).pow(2))
    }

    fun getDistance(location: org.bukkit.Location): Double{
        return abs((x - location.x).pow( 2) + (y - location.y).pow(2) + (z - location.z).pow(2))
    }

    fun getDistanceSquared(location: org.bukkit.Location): Double{
        return sqrt(abs((x - location.x).pow( 2) + (y - location.y).pow(2) + (z - location.z).pow(2)))
    }

    fun getDistanceSquared(location: Location): Double{
        return sqrt(abs((x - location.x).pow( 2) + (y - location.y).pow(2) + (z - location.z).pow(2)))
    }
}