import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

// Saves and loads the data using a text file
// File format (data on each line is separated by |):
//   DATE|2026-10-03
//   INCOME|152.0
//   SERVED|3
//   VEHICLE|C1|LEA-1234|2|true|2026-10-03T16:00:00
public class FileManager {

    public static void save(ParkingLot lot, String path) {
        try {
            File file = new File(path);
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();   // create the data folder if missing
            }
            PrintWriter out = new PrintWriter(file);
            out.println("DATE|" + LocalDate.now());
            out.println("INCOME|" + lot.todayIncome);
            out.println("SERVED|" + lot.vehiclesServed);
            for (ParkingSlot s : lot.slots) {
                if (!s.isFree()) {
                    Vehicle v = s.vehicle;
                    out.println("VEHICLE|" + s.id + "|" + v.plate + "|" + v.type
                            + "|" + v.isVip + "|" + v.entryTime);
                }
            }
            out.close();
        } catch (IOException e) {
            System.out.println("Could not save: " + e.getMessage());
        }
    }

    public static void load(ParkingLot lot, String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;     // first run, there is no file yet, that is fine
        }
        try {
            Scanner sc = new Scanner(file);
            String savedDate = "";
            double income = 0;
            int served = 0;

            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] p = line.split("\\|");

                if (p[0].equals("DATE")) {
                    savedDate = p[1];
                } else if (p[0].equals("INCOME")) {
                    income = Double.parseDouble(p[1]);
                } else if (p[0].equals("SERVED")) {
                    served = Integer.parseInt(p[1]);
                } else if (p[0].equals("VEHICLE")) {
                    ParkingSlot slot = lot.findSlotById(p[1]);
                    if (slot != null) {
                        slot.vehicle = new Vehicle(
                                p[2],
                                Integer.parseInt(p[3]),
                                Boolean.parseBoolean(p[4]),
                                LocalDateTime.parse(p[5]));
                    }
                }
            }
            sc.close();

            // Earnings are only for today. If the saved date is old, start from zero
            if (savedDate.equals(LocalDate.now().toString())) {
                lot.todayIncome = income;
                lot.vehiclesServed = served;
            }
        } catch (Exception e) {
            System.out.println("Could not load: " + e.getMessage());
        }
    }
}
