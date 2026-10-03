import java.time.LocalDateTime;

// Stores the data of one vehicle
public class Vehicle {
    String plate;
    int type;               // 1 = Bike, 2 = Car
    boolean isVip;          // is the owner a VIP member?
    LocalDateTime entryTime;

    public Vehicle(String plate, int type, boolean isVip, LocalDateTime entryTime) {
        this.plate = plate;
        this.type = type;
        this.isVip = isVip;
        this.entryTime = entryTime;
    }

    public String getTypeName() {
        if (type == 1) {
            return "Bike";
        } else {
            return "Car";
        }
    }
}
