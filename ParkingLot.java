import java.time.LocalDateTime;
import java.util.ArrayList;

// All the parking logic lives here.
public class ParkingLot {
    ArrayList<ParkingSlot> slots = new ArrayList<>();
    double todayIncome = 0;     // today's earnings
    int vehiclesServed = 0;     // how many vehicles left today

    // carSlots = number of car slots, bikeSlots = number of bike slots
    public ParkingLot(int carSlots, int bikeSlots) {
        for (int i = 1; i <= carSlots; i++) {
            slots.add(new ParkingSlot("C" + i, 2));
        }
        for (int i = 1; i <= bikeSlots; i++) {
            slots.add(new ParkingSlot("B" + i, 1));
        }
    }

    // Find the first free slot of this type (null if none)
    public ParkingSlot findFreeSlot(int type) {
        for (ParkingSlot s : slots) {
            if (s.type == type && s.isFree()) {
                return s;
            }
        }
        return null;
    }

    // Find a slot by number plate (null if not found)
    public ParkingSlot findSlotByPlate(String plate) {
        for (ParkingSlot s : slots) {
            if (!s.isFree() && s.vehicle.plate.equalsIgnoreCase(plate)) {
                return s;
            }
        }
        return null;
    }

    // Find a slot by its ID (used when loading from the file)
    public ParkingSlot findSlotById(String id) {
        for (ParkingSlot s : slots) {
            if (s.id.equalsIgnoreCase(id)) {
                return s;
            }
        }
        return null;
    }

    // Park a vehicle. Returns the slot, or null if the parking is full
    public ParkingSlot enterVehicle(Vehicle vehicle) {
        ParkingSlot slot = findFreeSlot(vehicle.type);
        if (slot == null) {
            return null;
        }
        slot.vehicle = vehicle;
        return slot;
    }

    // Remove a vehicle and return its bill (null if the vehicle is not found)
    public BillCalculator exitVehicle(String plate, LocalDateTime exitTime) {
        ParkingSlot slot = findSlotByPlate(plate);
        if (slot == null) {
            return null;
        }
        BillCalculator bill = new BillCalculator(slot.vehicle, exitTime);
        todayIncome = todayIncome + bill.total;
        vehiclesServed++;
        slot.vehicle = null;    // the slot is free again
        return bill;
    }

    // How many slots of this type are free
    public int countFree(int type) {
        int count = 0;
        for (ParkingSlot s : slots) {
            if (s.type == type && s.isFree()) {
                count++;
            }
        }
        return count;
    }

    // How many vehicles are parked right now
    public int countParked() {
        int count = 0;
        for (ParkingSlot s : slots) {
            if (!s.isFree()) {
                count++;
            }
        }
        return count;
    }
}
