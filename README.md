# Maximum-Capacity-between-two-sources-of-National-Airspace-System-NAS

##Input
The code takes a .csv file containing the flight details in the order origin, destination, departure time, arrival time, capacity. Flight data are taken from expedia.com between the given 10 airports on the date of 6th January 2020. This data is parsed into flights.csv file.

##Algorithm
In the algorithm, approach is to divide the time into 24 1-hour intervals and round every flight departure and arrival time to the nearest hour, i.e., the flight departing at time 10:15 is rounded to time 10:00 and the flight departing at 21:40 is rounded to time 22:00. Then, all the flights with depart time and arrival time are each taken as nodes in a graph map. The connecting flights between these nodes in the graph become edges with its capacity as weight.
For example, in the csv file, each row represents a flight.
SEA DEN 12:35 16:21 180
For this input, the nodes are named as SEA13 and DEN16 and there is a directed edge between
these nodes with weight 180.
We create an adjacency matrix from the above information with the size NxN where N is the different permutations of nodes/cities with 24 1-hr intervals. Now, we use the Ford-Fulkerson algorithm to calculate the individual maximum flow of all possible combinations of origins and destinations and add them to get total maximum capacity.
Breadth first search is used to find the augmented path from source to sink.
