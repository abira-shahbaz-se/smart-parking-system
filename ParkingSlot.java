// One parking slot (for example C1 or B2)
public class ParkingSlot {
    String id;          // slot name, like C1, B2
    int type;           // 1 = Bike slot, 2 = Car slot
    Vehicle vehicle;    // null means the slot is empty

    public ParkingSlot(String id, int type) {
        this.id = id;
        this.type = type;
        this.vehicle = null;
    }

    public boolean isFree() {
        return vehicle == null;
    }
}
