package no.hiof.mariusca.stitur.model

// This was used when trying to implement data from Turrutebasen.
// This converted from Geojson to our own custom Trip.

data class GeoJsonFeatureCollection(
    val type: String,
    val features: List<GeoJsonFeature>
)

data class GeoJsonFeature(
    val type: String,
    val properties: Map<String, Any>,
    val geometry: GeoJsonGeometry
)

data class GeoJsonGeometry(
    val type: String,
    val coordinates: List<List<Double>>
)

fun tripToGeoJSONFeature(trip: Trip): GeoJsonFeature {
    val properties = mutableMapOf<String, Any>()
    properties["uid"] = trip.uid
    properties["created"] = trip.created?.toDate().toString()
    properties["updated"] = trip.created?.toDate().toString()
    properties["difficulty"] = trip.difficulty
    properties["lengthInMeters"] = trip.lengthInMeters
    properties["routeDescription"] = trip.routeDescription
    properties["routeName"] = trip.routeName

    val coordinates = trip.coordinates.map { coordinate ->
        listOf(coordinate.lat.toDouble(), coordinate.long.toDouble())
    }
    val geometry = GeoJsonGeometry(trip.geometryType, coordinates)
    return GeoJsonFeature("Feature", properties, geometry)
}

fun tripsToGeoJSONFeatureCollection(trips: List<Trip>): GeoJsonFeatureCollection {
    val features = trips.map { tripToGeoJSONFeature(it) }
    return GeoJsonFeatureCollection("FeatureCollection", features)
}

