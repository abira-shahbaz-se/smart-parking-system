import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

// The program starts here. Only the menu and println/Scanner code is in this file.
public class ConsoleApp {

    static Scanner sc = new Scanner(System.in);
    static ParkingLot lot = new ParkingLot(4, 3);       // 4 car slots, 3 bike slots
    static final String FILE_PATH = "data/parking.txt";
    static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("h:mm a");

    public static void main(String[] args) {
        FileManager.load(lot, FILE_PATH);
        System.out.println("Welcome! Currently " + lot.countParked() + " vehicles are parked.");

        boolean running = true;
        while (running) {
            showMenu();
            int choice = readInt("Choice: ");

            switch (choice) {
                case 1:
                    enterVehicle();
                    break;
                case 2:
                    exitVehicle();
                    break;
                case 3:
                    showLiveStatus();
                    break;
                case 4:
                    searchVehicle();
                    break;
                case 5:
                    showIncome();
                    break;
                case 6:
                    FileManager.save(lot, FILE_PATH);
                    System.out.println("Data saved. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    static void showMenu() {
        System.out.println();
        System.out.println("=== SMART PARKING ===");
        System.out.println("1. Vehicle entry");
        System.out.println("2. Vehicle exit (bill)");
        System.out.println("3. Live status (view slots)");
        System.out.println("4. Search vehicle");
        System.out.println("5. Today's earnings");
        System.out.println("6. Exit");
    }

    // ---------- 1. Vehicle entry ----------
    static void enterVehicle() {
        System.out.print("Number plate: ");
        String plate = readLine().toUpperCase();

        if (plate.isEmpty() || plate.contains("|")) {
            System.out.println("Invalid number plate.");
            return;
        }
        if (lot.findSlotByPlate(plate) != null) {
            System.out.println("This vehicle is already in the parking!");
            return;
        }

        int type = 0;
        while (type != 1 && type != 2) {
            type = readInt("Type (1. Bike  2. Car): ");
        }
        boolean isVip = readYesNo("VIP member? (Y/N): ");

        Vehicle v = new Vehicle(plate, type, isVip, LocalDateTime.now());
        ParkingSlot slot = lot.enterVehicle(v);

        if (slot == null) {
            System.out.println("PARKING FULL! No free slot for a " + v.getTypeName() + ".");
        } else {
            System.out.println("Slot " + slot.id + " assigned | Time: "
                    + v.entryTime.format(timeFormat) + " | VIP: " + (isVip ? "Yes" : "No"));
            FileManager.save(lot, FILE_PATH);
        }
    }

    // ---------- 2. Vehicle exit and bill ----------
    static void exitVehicle() {
        System.out.print("Number plate: ");
        String plate = readLine().toUpperCase();

        BillCalculator bill = lot.exitVehicle(plate, LocalDateTime.now());
        if (bill == null) {
            System.out.println("This vehicle was not found in the parking.");
            return;
        }
        printBill(bill);
        FileManager.save(lot, FILE_PATH);
    }

    static void printBill(BillCalculator b) {
        long hrs = b.totalMinutes / 60;
        long mins = b.totalMinutes % 60;

        System.out.println();
        System.out.println("----------- BILL -----------");
        System.out.println("Vehicle: " + b.vehicle.plate + " (" + b.vehicle.getTypeName()
                + (b.vehicle.isVip ? ", VIP" : "") + ")");
        System.out.println("Entry: " + b.vehicle.entryTime.format(timeFormat)
                + " | Exit: " + b.exitTime.format(timeFormat));
        System.out.println("Time: " + hrs + " hours " + mins + " min ("
                + b.chargedHours + " hours charged)");
        if (b.normalHours > 0) {
            System.out.println("Normal hours: " + b.normalHours + " x Rs." + money(b.normalRate)
                    + " = " + money(b.normalHours * b.normalRate));
        }
        if (b.peakHours > 0) {
            System.out.println("Peak hours:   " + b.peakHours + " x Rs." + money(b.peakRate)
                    + " = " + money(b.peakHours * b.peakRate));
        }
        System.out.println("Subtotal: Rs." + money(b.subtotal));
        if (b.vehicle.isVip) {
            System.out.println("VIP 20% discount: -Rs." + money(b.discount));
        }
        System.out.println("TOTAL: Rs." + money(b.total));
        System.out.println("----------------------------");
    }

    // ---------- 3. Live status ----------
    static void showLiveStatus() {
        System.out.println();
        System.out.println("=== LIVE PARKING STATUS ===");
        printSlots("Car:", 2);
        printSlots("Bike:", 1);
        int free = lot.countFree(1) + lot.countFree(2);
        System.out.println("Free: " + free + " | Occupied: " + lot.countParked());
    }

    static void printSlots(String title, int type) {
        System.out.println(title);
        System.out.print(" ");
        for (ParkingSlot s : lot.slots) {
            if (s.type == type) {
                if (s.isFree()) {
                    System.out.print("🟢 " + s.id + "   ");
                } else {
                    System.out.print("🔴 " + s.id + " (" + s.vehicle.plate + ")   ");
                }
            }
        }
        System.out.println();
    }

    // ---------- 4. Search ----------
    static void searchVehicle() {
        System.out.print("Number plate: ");
        String plate = readLine().toUpperCase();

        ParkingSlot slot = lot.findSlotByPlate(plate);
        if (slot == null) {
            System.out.println("This vehicle is not in the parking.");
            return;
        }
        Vehicle v = slot.vehicle;
        long minutes = Duration.between(v.entryTime, LocalDateTime.now()).toMinutes();
        System.out.println("Found! Slot: " + slot.id + " | " + v.getTypeName()
                + " | VIP: " + (v.isVip ? "Yes" : "No"));
        System.out.println("Entry: " + v.entryTime.format(timeFormat)
                + " | So far: " + (minutes / 60) + " hours " + (minutes % 60) + " min");
    }

    // ---------- 5. Earnings ----------
    static void showIncome() {
        System.out.println();
        System.out.println("=== TODAY'S EARNINGS ===");
        System.out.println("Vehicles left: " + lot.vehiclesServed);
        System.out.println("Currently parked: " + lot.countParked());
        System.out.println("Total earnings: Rs." + money(lot.todayIncome));
    }

    // ---------- Small helper methods ----------

    // Show whole numbers without .0 (152.0 -> 152)
    static String money(double amount) {
        if (amount == Math.floor(amount)) {
            return String.valueOf((long) amount);
        }
        return String.format("%.2f", amount);
    }

    // Read a line. If the input ends, save the data and stop the program
    static String readLine() {
        if (!sc.hasNextLine()) {
            FileManager.save(lot, FILE_PATH);
            System.exit(0);
        }
        return sc.nextLine().trim();
    }

    // Read a number. Ask again if the user types something else
    // (we use nextLine instead of nextInt to avoid the common newline bug)
    static int readInt(String message) {
        while (true) {
            System.out.print(message);
            String text = readLine();
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException e) {
                System.out.println("Please enter numbers only.");
            }
        }
    }

    static boolean readYesNo(String message) {
        while (true) {
            System.out.print(message);
            String text = readLine().toUpperCase();
            if (text.equals("Y")) {
                return true;
            }
            if (text.equals("N")) {
                return false;
            }
            System.out.println("Please enter Y or N only.");
        }
    }
}
