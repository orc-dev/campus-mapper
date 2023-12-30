/**
 * @file   Building.java
 * @brief  This class represents an Building object. It contains the Id, name,
 *         services of the building.
 *
 * @author Xin Cai
 */
import java.util.ArrayList;


class Building {
    /* static fields of services for all building objects */
    static final String[] SERVICE_NAME = {"Dining", "Library", "Parking"};
    static final int[]    SERVICE_MASK = {   0b001,     0b010,     0b100};
    
    /* instance fields */
    public int id = -1;
    public String name = "unknown";
    private int serviceBits = 0;
    private ArrayList<String> services = new ArrayList<>();
    private StringBuilder serviceMessage = new StringBuilder();
    
    /**
     * Construct a new Building.
     * 
     * @param id Id of the building.
     * @param name Name of the building.
     * @param serviceBits Service bit of the building.
     */
    public Building(int id, String name, int serviceBits) {
        this.id = id;
        this.name = name;
        this.setServices(serviceBits);
    }

    /**
     * set service bit for this building.
     */
    public void setServices(int serviceBits) {
        this.serviceBits = serviceBits;
        this.updateServiceMessage();
    }

    /**
     * @return True if the building provides dining service.
     */
    public boolean hasDining() {
        return (Building.SERVICE_MASK[0] & this.serviceBits) > 0;
    }

    /**
     * @return True if the building provides library service.
     */
    public boolean hasLibrary() {
        return (Building.SERVICE_MASK[1] & this.serviceBits) > 0;
    }

    /**
     * @return True if the building provides parking service.
     */
    public boolean hasParking() {
        return (Building.SERVICE_MASK[2] & this.serviceBits) > 0;
    }

    /**
     * @param svBit A service bit argument.
     * @return True if the building provides services matching the given svBit.
     */
    public boolean hasService(int svBit) {
        return (svBit & this.serviceBits) > 0;
    }

    /**
     * Update the services message as a string in specified formatting.
     */
    private void updateServiceMessage() {
        // reset services
        this.services.clear();
        this.serviceMessage = new StringBuilder();

        // no services
        if (this.serviceBits == 0)
            return;
        
        // check services
        for (int i = 0; i < Building.SERVICE_NAME.length; ++i) {
            if ((Building.SERVICE_MASK[i] & this.serviceBits) > 0) {
                this.services.add(Building.SERVICE_NAME[i]);
            }
        }
        // static symbols for printout
        final String OPEN  = "(";
        final String CLOSE = ")";
        final String SEP   = " | ";

        // build service message
        this.serviceMessage.append(OPEN);
        this.serviceMessage.append(this.services.get(0));

        for (int i = 1; i < this.services.size(); ++i) {
            this.serviceMessage.append(SEP);
            this.serviceMessage.append(this.services.get(i));
        }
        this.serviceMessage.append(CLOSE);
    }

    /**
     * @return String of services provided by this building.
     */
    public String getServiceMessage() {
        return this.serviceMessage.toString();
    }

    /**
     * Print the ID and name of the building.
     */
    @Override
    public String toString() {
        StringBuilder buildingInfo = new StringBuilder();

        if (this.id < 10)
            buildingInfo.append("0");

        buildingInfo.append(this.id).append(" - ")
            .append(this.name);
        
        return buildingInfo.toString();
    }
}
