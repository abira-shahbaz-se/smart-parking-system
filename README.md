# Developered by: Abira Shahbaz
# Smart Parking Management System 

A console-based parking system written in Java. Vehicles enter and exit, a slot is assigned automatically, and the bill is calculated with peak-hour pricing and a VIP discount.

## Features
- Vehicle entry (Bike / Car) with automatic free-slot assignment
- **20% discount** for VIP members
- **Peak hour pricing:** Rs. 20 extra per hour between 5 PM and 9 PM
- **Live status:** 🟢 free slot, 🔴 occupied slot (with number plate)
- Search a vehicle
- Today's earnings
- Data is saved in `data/parking.txt`, so it stays after the program closes
- "Parking full" message when no slot is free

## Rates
| Type | Rate per hour | Peak (5-9 PM) |
|------|---------------|----------------|
| Bike | Rs. 20 | Rs. 40 |
| Car  | Rs. 50 | Rs. 70 |

Minimum charge is 1 hour, and a part of an hour counts as a full hour.
Rates can be changed in `BillCalculator.java` (constants at the top).

## How to run
```
javac -d out src/*.java
java -cp out ConsoleApp
```
(On Windows use `src\*.java`.)

> If 🟢/🔴 show up as `?` or boxes, it is a terminal encoding issue. In Windows CMD run `chcp 65001` first, or use the VS Code / IntelliJ terminal.

## Project structure
```
src/
├── Vehicle.java         -> vehicle data
├── ParkingSlot.java     -> one parking slot
├── ParkingLot.java      -> entry / exit logic
├── BillCalculator.java  -> bill (peak hours + VIP)
├── FileManager.java     -> save / load
└── ConsoleApp.java      -> menu (the program starts here)
```

## Future ideas
- Swing GUI (green/red slot boxes)
- Daily report file
- Admin password
