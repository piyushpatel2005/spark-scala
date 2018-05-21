Open a Jupyter notebook.

```scala
sc

val airlinesPath = "flightDelaysData/airlines.csv"

// Airports file has following fields

val airportsPath = "flightDelaysData/airports.csv"

// Flights files has following fields
// Flight Date, Airline code, Flight number, Source Airport, Destination Airport, Departure time, Departure delay, Arrival time, arrival delay, Airtime, Distance
val flightsPath = "flightDelaysData/flights.csv"

val airlines = sc.textFile(airlinesPath)
airlines // See the type of value

airlines.collect

airlines.first

airlines.take(10)

airlines.count()
