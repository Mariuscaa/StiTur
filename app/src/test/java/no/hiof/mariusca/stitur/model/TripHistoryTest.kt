package no.hiof.mariusca.stitur.model

import junit.framework.TestCase.assertEquals
import org.junit.Test

class TripHistoryTest {

    @Test
    fun calculateDistanceKM_withMultipleCoordinates_returnsCorrectDistance() {
        val coordinates = listOf(
            Coordinate("59.911491", "10.757933"),
            Coordinate("60.391263", "5.322054"),
            Coordinate("58.969975", "5.733107")
        )

        val result = calculateDistanceKM(coordinates)
        val expectedDistance = 465.15

        assertEquals(expectedDistance, result, 5.0)
    }

    @Test
    fun calculateDistanceKM_withTwoCoordinates_returnsCorrectDistance() {
        val coordinates = listOf(
            Coordinate("59.911491", "10.757933"),
            Coordinate("60.391263", "5.322054")
        )

        val result = calculateDistanceKM(coordinates)
        val expectedDistance = 305.0

        assertEquals(expectedDistance, result, 5.0)
    }

    @Test
    fun calculateDistanceKM_withIdenticalCoordinates_returnsZero() {
        val coordinates = listOf(
            Coordinate("59.911491", "10.757933"),
            Coordinate("59.911491", "10.757933")
        )

        val result = calculateDistanceKM(coordinates)
        val expectedDistance = 0.0

        assertEquals(expectedDistance, result, 0.0)
    }

    @Test
    fun calculateDistanceKM_withEmptyList_returnsZero() {
        val coordinates = emptyList<Coordinate>()

        val result = calculateDistanceKM(coordinates)
        val expectedDistance = 0.0

        assertEquals(expectedDistance, result, 0.0)
    }

    @Test
    fun calculateDistanceKM_withSingleCoordinate_returnsZero() {
        val coordinates = listOf(Coordinate("59.911491", "10.757933"))

        val result = calculateDistanceKM(coordinates)
        val expectedDistance = 0.0

        assertEquals(expectedDistance, result, 0.0)
    }
}


data class Coordinate(val lat: String, val long: String)