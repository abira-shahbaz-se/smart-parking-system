import java.time.Duration;
import java.time.LocalDateTime;

// Calculates the bill (peak hours + VIP discount)
public class BillCalculator {

    // Rates (change them here in one place)
    public static final double CAR_RATE = 50;       // car rate per hour
    public static final double BIKE_RATE = 20;      // bike rate per hour
    public static final double PEAK_EXTRA = 20;     // extra Rs. 20 per hour during peak
    public static final int PEAK_START = 17;        // 5 PM
    public static final int PEAK_END = 21;          // 9 PM
    public static final double VIP_DISCOUNT = 0.20; // 20% discount

    // Bill details (ConsoleApp prints these)
    Vehicle vehicle;
    LocalDateTime exitTime;
    long totalMinutes;
    int chargedHours;
    int normalHours;
    int peakHours;
    double normalRate;
    double peakRate;
    double subtotal;
    double discount;
    double total;

    public BillCalculator(Vehicle vehicle, LocalDateTime exitTime) {
        this.vehicle = vehicle;
        this.exitTime = exitTime;

        // Rate depends on the vehicle type
        if (vehicle.type == 1) {
            normalRate = BIKE_RATE;
        } else {
            normalRate = CAR_RATE;
        }
        peakRate = normalRate + PEAK_EXTRA;

        // How long the vehicle stayed
        totalMinutes = Duration.between(vehicle.entryTime, exitTime).toMinutes();
        if (totalMinutes < 0) {
            totalMinutes = 0;
        }

        // A part of an hour counts as a full hour, minimum 1 hour
        long hours = totalMinutes / 60;
        if (totalMinutes % 60 != 0) {
            hours++;
        }
        if (hours < 1) {
            hours = 1;
        }
        chargedHours = (int) hours;

        // Check each charged hour: is it a peak hour or a normal hour?
        for (int i = 0; i < chargedHours; i++) {
            LocalDateTime t = vehicle.entryTime.plusHours(i);
            int hour = t.getHour();
            if (hour >= PEAK_START && hour < PEAK_END) {
                peakHours++;
            } else {
                normalHours++;
            }
        }

        subtotal = (normalHours * normalRate) + (peakHours * peakRate);

        // VIP members get 20% off
        if (vehicle.isVip) {
            discount = subtotal * VIP_DISCOUNT;
        } else {
            discount = 0;
        }
        total = subtotal - discount;
    }
}
