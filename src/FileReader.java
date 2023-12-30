/**
 * @file   FileReader.java
 * @brief  This class provides methods to read a data file and a map file, 
 *         extracting necessary data on campus buildings.
 *
 * @author Xin Cai
 */
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

/** 
 * For implementing different readLine methods to read data file and map file.
 */
@FunctionalInterface
interface LineReader {
    void readLine(String line);
}

class FileReader {
    private Graph buildingGraph;
    private HashMap<Integer, Building> buildingTable;
    private ArrayList<String> campusMapRows;
    private MapCell[][] mapBoard;
    private HashMap<Integer, ArrayList<int[]>> borderTable;
    
    /**
     * Construct a DataReader, reading the specified files.
     * 
     * @param datFile input file path for campus buildings
     * @param mapFile input file path for campus map
     * @throws IOException
     */
    public FileReader(String datFile, String mapFile) {
        this.buildingGraph = new Graph();
        this.buildingTable = new HashMap<>();
        this.campusMapRows = new ArrayList<>();
        this.borderTable   = new HashMap<>();

        this.readFile(datFile, this::readDatLine);
        this.readFile(mapFile, this::readMapLine);
        this.buildMap();
        this.buildBorderTable();
    }

    /**
     * Read data file or map file.
     * 
     * @param datFile input data file
     */
    private void readFile(String datFile, LineReader lnReader) {
        final Charset utf8 = StandardCharsets.UTF_8;
        String line;
        
        try (
            // Allocate reader-resources
            FileInputStream inStream = new FileInputStream(datFile);
            InputStreamReader inReader = new InputStreamReader(inStream, utf8);
            BufferedReader bfReader = new BufferedReader(inReader)
        ) {
            // Read file line by line
            while ((line = bfReader.readLine()) != null)
                lnReader.readLine(line);
    
        } catch (IOException e) {
            // Handle exception
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Read each line of the file, extracting info of each building and 
     * the topology of the compus buildings.
     * 
     * @param line a string representing a line of the input file.
     */
    private void readDatLine(String line) {
        final String[] tokens = line.split("\\s+");
        int i = 0;

        try {
            // parse building id
            int id = Integer.parseInt(tokens[i++]);

            // extract building name
            StringBuilder name = new StringBuilder();

            while (!tokens[i].equals("$")) {
                if (name.length() > 0) {
                    name.append(" ");
                }
                name.append(tokens[i++]);
            }
            // skip "$"
            i++;

            // extract building services
            int serviceBits = 0;
            for (int j = 0; j < 3; ++j) {
                int tempBit = Integer.parseInt(tokens[j + i]);
                serviceBits |= (tempBit << j);
            }

            // add this new building to table
            this.buildingTable.put(id, 
                new Building(id, name.toString(), serviceBits));

            // read neighbor-cost info
            for (i += 3; i < tokens.length; i += 2) {
                int neib = Integer.parseInt(tokens[i]);
                int cost = Integer.parseInt(tokens[i + 1]);
                this.buildingGraph.addEdge(id, neib, cost);
            }
        } catch (NumberFormatException e) {
            // handle exception
            System.err.println("Error parsing integer: " + e.getMessage());
        }
    }

    /**
     * Read each line of the map file, copying the chars into buffer.
     * 
     * @param line a string representing a line of the input file.
     */
    private void readMapLine(String line) {
        this.campusMapRows.add(line);
    }

    /**
     * Construct a 2d Array of MapCell.
     */
    private void buildMap() {
        final int nrow = this.campusMapRows.size();
        final int ncol = this.campusMapRows.get(0).length();

        // Add one to ncol to include the newline-char cell
        mapBoard = new MapCell[nrow][ncol + 1];

        for (int r = 0; r < nrow; ++r) {
            // convert each row to char array
            char[] currRow = this.campusMapRows.get(r).toCharArray();

            for (int c = 0; c < ncol; ++c) {
                mapBoard[r][c] = new MapCell(currRow[c]);
            }
            // add an end cell with newline char
            mapBoard[r][ncol] = new MapCell('\n');
        }
    }

    /**
     * Consturct the borderTable, which maps from the building ID to a list 
     * of coordinates of the (most left and most right) border cells.
     */
    private void buildBorderTable() {
        for (int r = 1; r < mapBoard.length - 1; ++r) {
            for (int c = 1; c < mapBoard[0].length; ++c) {
                // Locate an entry point of a building
                if (foundNewBuilding(r, c)) {
                    // Parse building Id and find row/col bounds
                    int bid    = parseBuildingId(r, c);
                    int endCol = foundEndCol(r, c);
                    int endRow = foundEndRow(r, c);
                    
                    // Create a list of border-coordinates
                    ArrayList<int[]> borders = new ArrayList<>();
                    for (int i = r; i <= endRow; ++i) {
                        borders.add(new int[]{i, c - 1});
                        borders.add(new int[]{i, endCol});
                    }
                    // Add to borderTable and adjust `c` pointer
                    borderTable.put(bid, borders);
                    c = endCol;
                }
            }
        }
    }

    /**
     * @param r row index
     * @param c col index
     * @return True if current cell is en entry point of a building.
     */
    private boolean foundNewBuilding(int r, int c) {
        return mapBoard[r][c - 1].ch == '[' && 
            Character.isDigit(mapBoard[r][c].ch);
    }

    /**
     * @param r row index
     * @param c col index
     * @return The Id of the building.
     */
    private int parseBuildingId(int r, int c) {
        int a = (int) (mapBoard[r][c].ch - '0');
        int b = (int) (mapBoard[r][c + 1].ch - '0');
        return 10 * a + b;
    }

    /**
     * @param r row index
     * @param c col index
     * @return The column index of the closed tag of this building.
     */
    private int foundEndCol(int r, int c) {
        if (mapBoard[r][c].ch == ']')
            return c;
        return foundEndCol(r, c + 1);
    }

    /**
     * @param r row index
     * @param c col index
     * @return The row index of the closed tag of this building.
     */
    private int foundEndRow(int r, int c) {
        if (mapBoard[r + 1][c - 1].ch != '[' || 
            Character.isDigit(mapBoard[r + 1][c].ch)) {
            return r;
        }
        return foundEndRow(r + 1, c);
    }
    
    /**
     * @return A graph represents topology of campus building.
     */
    public Graph getBuildingGraph() {
        return this.buildingGraph;
    }

    /**
     * @return A hash table contains building id and a Building object.
     */
    public HashMap<Integer, Building> getBuildingTable() {
        return this.buildingTable;
    }

    /**
     * @return A 2D Array of MapCell representing the campus map.
     */
    public MapCell[][] getMapBoard() {
        return this.mapBoard;
    }

    /**
     * @return A map from building ID to building's border cells.
     */
    public HashMap<Integer, ArrayList<int[]>> getBorderTable() {
        return this.borderTable;
    }
}
