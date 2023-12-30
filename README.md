## campus-mapper

### Description
This project is an adaptation of an assignment from the CS400 Spring 2021 class. The primary objective is to implement an application based on Dijkstra's shortest path algorithm. The project focuses on creating a campus mapper that reads data from a dataset containing over fifty buildings in the eastern part of the UW-Madison campus.

Users have the capability to select two buildings by their ID, prompting the program to compute and display the shortest path between them. 
![shortest_path_searching](img/pathfinding.png)

Additionally, users can highlight buildings offering specific services such as dining, library, and parking. The program responds by displaying a mapped representation highlighting the selected buildings and providing a list of relevant buildings with the specified services.
![service_selection](img/service_select.png)

### Data Structure and Algorithm Features
- Utilized *classes, static classes, enums, and interfaces* for code organization.
- Employed *HashMap, PriorityQueue, ArrayList, and Arrays* to efficiently manage data and implement algorithms.
- Incorporated *ANSI escape sequences* for enhanced console output formatting.
- Facilitated user interaction through *Scanner and parsing* techniques.
- Implemented *Dijkstra's shortest path algorithm* for computing optimal routes between buildings in the campus mapper.

### Project Structure
```
MapApp.java
|-- Building.java
|-- DataReader.java
|-- Graph.java
|    |-- NodeTuple.java
|-- Color.java
|-- Text.java
|-- MapCell.java
```

### Usage
- Clone the whole repo to Linux environment
- Run script `run_mapper.sh` to compile and run the app
