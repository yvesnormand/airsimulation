package me.wizmxn;
/* Aircraft class
 *
 * TP of SE (version 2020)
 *
 * AM
 */

import jakarta.annotation.Nonnull;

import java.util.*;
import java.util.function.Predicate;

public class Aircraft {
    private static final int MINIMAL_EXIT_SECURITY_NUMBER = 2;
    private static final int MINIMAL_ROWS_NUMBER = 3;
    private static final int MINIMAL_NUMBER_OF_SEAT_PER_ROWS = 2;
    private static final int MINIMAL_AISLE_NUMBER = 1;
    private final int rowsNumber;
    private final int numberOfSeatsPerRow;
    private final int flightCapacity;
    private int freeSeatsNumber;
    /**
     * a set of seat marked as aisleSeat
     */
    @Nonnull
    private final Set<Integer> aisleSeats;
    /**
     * a set of rows marked as emergency
     */
    @Nonnull
    private final Set<Integer> emergencyRows;
    private final @Nonnull Customer[][] seatMap;
    private boolean color = true;

    /**
     *
     * @param rowsNumber
     * @param numberOfSeatsPerRow
     * @param aisleSeats a set of seat marked as aisleSeat
     * @param emergencyRows a set of rows marked as emergency
     */
    public Aircraft(int rowsNumber,
                    int numberOfSeatsPerRow,
                    @Nonnull Set<Integer> aisleSeats,
                    @Nonnull Set<Integer> emergencyRows) {
        if (rowsNumber < MINIMAL_ROWS_NUMBER) {
            throw new IllegalArgumentException("Aircraft: the aircraft needs at least %d rows!".formatted(MINIMAL_ROWS_NUMBER));
        }
        this.rowsNumber = rowsNumber;

        if (numberOfSeatsPerRow < MINIMAL_NUMBER_OF_SEAT_PER_ROWS) {
            throw new IllegalArgumentException("Aircraft: the aircraft needs to have at least %d seats per row!".formatted(MINIMAL_NUMBER_OF_SEAT_PER_ROWS));

        }
        this.numberOfSeatsPerRow = numberOfSeatsPerRow;

        checkAisleSeatValidity(aisleSeats);
        this.aisleSeats = new HashSet<>(aisleSeats); // defensive copy

        checkEmergencyRowsValidity(emergencyRows);
        this.emergencyRows = new HashSet<>(emergencyRows); // defensive copy

        this.seatMap = new Customer[rowsNumber][numberOfSeatsPerRow];
        this.flightCapacity = rowsNumber * numberOfSeatsPerRow;
        this.freeSeatsNumber = flightCapacity;

    }

    /**
     * Ensure all rows marked as emergency are valid rows, meaning between 0 and {@link #rowsNumber}
     * @param emergencyRows a set of rows marked as emergency
     */
    private void checkEmergencyRowsValidity(@Nonnull Set<Integer> emergencyRows) {
        Objects.requireNonNull(emergencyRows);
        if (emergencyRows.size() < MINIMAL_EXIT_SECURITY_NUMBER) {
            throw new IllegalArgumentException("Aircraft: the aircraft needs at least %d security exits!".formatted(MINIMAL_EXIT_SECURITY_NUMBER));
        }
        if (emergencyRows.stream().anyMatch(emergencyRow -> !isRowNumberInBounds(emergencyRow))) {
            throw new IllegalArgumentException("Aircraft: the aircraft emergencyRows are invalid, they must all be between 0 and %d".formatted(rowsNumber));
        }
    }

    /**
     * Ensure every aisleSeat is valid, meaning no aisleSeat is next to a windows and all are in numberOfSeatsPerRow bounds
     * @param aisleSeats a set of seat marked as aisleSeat
     */
    private void checkAisleSeatValidity(@Nonnull Set<Integer> aisleSeats) {
        Objects.requireNonNull(aisleSeats);

        int aislesNumber = aisleSeats.size();
        // check the minimal number of aisle (for 1 aisle, there are 2 corresponding seats)
        if (aislesNumber < MINIMAL_AISLE_NUMBER * 2) {
            throw new IllegalArgumentException("Aircraft: the aircraft needs to have at least %d aisle!".formatted(MINIMAL_AISLE_NUMBER));
        }

        final Predicate<Integer> isAisleSeatNextToWindows =
                aisleSeat -> aisleSeat == 1 || aisleSeat == aislesNumber - 1;
        if (aisleSeats.stream().anyMatch(isAisleSeatNextToWindows)) {
            throw new IllegalArgumentException("Aircraft: aisles cannot be located next to the windows!");
        }

        if (aisleSeats.stream().anyMatch(aisleSeat -> !isSeatNumberInBounds(aisleSeat))) {
            throw new IllegalArgumentException("Aircraft: the aircraft aisleSeats are invalid, they must all be between 0 and %d".formatted(numberOfSeatsPerRow));
        }
    }

    @Nonnull
    public static Aircraft classicalAircraft() {
        return new Aircraft(32, 6, Set.of(3, 4), Set.of(0, 12, 31));
    }

    public int getNumberOfRows() {
        return rowsNumber;
    }

    public int getSeatsPerRow() {
        return numberOfSeatsPerRow;
    }

    public Set<Integer> getEmergencyRows() {
        return new HashSet<>(emergencyRows);
    }

    public int getNumberEmergencyRows() {
        return emergencyRows.size();
    }

    // Verify that a row index is in the bounds
    private boolean isRowNumberInBounds(int row) {
        return row >= 0 && row < rowsNumber;
    }

    // Verify that a column index is in the bounds
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean isSeatNumberInBounds(int col) {
        return col >= 0 && col < numberOfSeatsPerRow;
    }

    // Get an optional Customer in a specific seat from the coordinates or optional empty
    @Nonnull
    public Optional<Customer> getCustomer(int rowIndex, int columnIndex) {
        checkOrThrowSeatCoordinate(rowIndex, columnIndex);
        return Optional.ofNullable(seatMap[rowIndex][columnIndex])
                .map(Customer::copyFromCustomer);
    }

    @Nonnull
    // Looking for the Customer in the Aircraft with higher flyer frequency under a given flyerLevelLimit
    public Optional<Customer> getMostFrequentFlyer(int flyerLevelLimit) {
        int maxFlyerLevel = 0;
        Customer customer = null;

        for (int i = 0; i < this.rowsNumber; i++) {
            for (int j = 0; j < this.numberOfSeatsPerRow; j++) {
                if (this.seatMap[i][j] == null) {
                    continue;
                }
                int currentFlyerLevel = this.seatMap[i][j].getFlyerLevel();
                if (currentFlyerLevel <= flyerLevelLimit && maxFlyerLevel < currentFlyerLevel) {
                    customer = Customer.copyFromCustomer(seatMap[i][j]);
                    maxFlyerLevel = currentFlyerLevel;
                }
            }
        }

        return Optional.ofNullable(customer);
    }

    // Get the Row Number of a given Customer
    public OptionalInt getCustomerRowNumber(@Nonnull Customer c) {
        //noinspection DuplicatedCode
        for (int i = 0; i < this.rowsNumber; i++) {
            for (int j = 0; j < this.numberOfSeatsPerRow; j++) {
                if (this.seatMap[i][j] != null
                    && this.seatMap[i][j].equals(c)) {
                    return OptionalInt.of(i);
                }
            }
        }

        return OptionalInt.empty();
    }

    // Get the Seat Number in the Row of a given Customer
    public OptionalInt getCustomerSeatPlaceInRowRange(@Nonnull Customer c) {
        //noinspection DuplicatedCode
        for (int i = 0; i < this.rowsNumber; i++) {
            for (int j = 0; j < this.numberOfSeatsPerRow; j++) {
                if (this.seatMap[i][j] != null
                    && this.seatMap[i][j].equals(c)) {
                    return OptionalInt.of(j);
                }
            }
        }
        return OptionalInt.empty();
    }

    public boolean isSeatEmpty(int row, int col) {
        checkOrThrowSeatCoordinate(row, col);
        return this.seatMap[row][col] == null;
    }

    public int numberOfFreeSeats() {
        return this.freeSeatsNumber;
    }

    public int getFlightCapacity() {
        return flightCapacity;
    }

    // Checking whether the flight is full
    public boolean isFlightFull() {
        return this.freeSeatsNumber == 0;
    }

    private void checkOrThrowSeatCoordinate(int row, int col) {
        if (!this.isRowNumberInBounds(row) || !this.isSeatNumberInBounds(col)) {
            throw new IllegalArgumentException("Aircraft: impossible to verify whether the seat is empty or not: seat coordinates out of bounds (bounds = %d,%d | coords = %d,%d)"
                    .formatted(getNumberOfRows(), getSeatsPerRow(), row, col));
        }
    }

    public void freeSeat(int row, int col) {
        checkOrThrowSeatCoordinate(row, col);
        if (this.seatMap[row][col] == null) {
            return;
        }

        synchronized (this) {
            this.freeSeatsNumber++;
        }
        this.seatMap[row][col] = null;
    }

    /**
     *
     * Placing a Customer in a given Aircraft seat
     * @return the number of freeSeat remaining
     */
    public int placeCustomerToSeat(@Nonnull Customer customer, int row, int col) {
        Objects.requireNonNull(customer);
        checkOrThrowSeatCoordinate(row, col);
        if (!isSeatEmpty(row, col)) {
            throw new RuntimeException("Aircraft: the seat (%d,%d) is not empty (%s)".formatted(row, col, customer));
        }
        this.seatMap[row][col] = Customer.copyFromCustomer(customer);
        synchronized (this) {
            this.freeSeatsNumber--;
        }
        return this.freeSeatsNumber;
    }

    public Aircraft clearAllCustomer() {
        for (Customer[] customers : seatMap) {
            Arrays.fill(customers, null);
        }
        this.freeSeatsNumber = flightCapacity;
        return this;
    }

    // Switch off colors in toString (for an incompatibility with the color system)
    public void toggleColorOff() {
        this.color = false;
    }

    // Clean screen
    // (creates a String that make the cursor point at the beginning of the last output from toString)
    public String cleanString() {
        final StringBuilder print = new StringBuilder();
        if (this.color) {
            print.append("\033[F".repeat(Math.max(0, 4 + this.getSeatsPerRow() + this.aisleSeats.size() / 2)));
            print.append("\r");
        }
        return print.toString();
    }

    //     Printing
    public String toString() {
        StringBuilder print = new StringBuilder();

        for (int rowNumber = 0; rowNumber < this.rowsNumber; rowNumber++) {
            if (emergencyRows.contains(rowNumber)) {
                print.append("-|");
            } else {
                print.append("--");
            }
        }
        print.append("-\n");

        for (int j = 0; j < this.numberOfSeatsPerRow; j++) {
            if (aisleSeats.contains(j) && aisleSeats.contains(j + 1)) {
                print.append("--".repeat(Math.max(0, this.rowsNumber)));
                print.append("-\n");
            }

            for (int i = 0; i < this.rowsNumber; i++) {
                if (this.seatMap[i][j] == null) {
                    print.append(" \033[1mx\033[0m");
                    continue;
                }
                if (!this.color) {
                    print.append(" ").append(this.seatMap[i][j].getFlyerLevel());
                    continue;
                }
                if (this.seatMap[i][j].specialAssistance()) {
                    print.append(" \033[31;1m").append(this.seatMap[i][j].getFlyerLevel()).append("\033[0m");
                } else if (this.seatMap[i][j].isOver60()) {
                    print.append(" \033[33;1m").append(this.seatMap[i][j].getFlyerLevel()).append("\033[0m");
                } else {
                    print.append(" \033[32;1m").append(this.seatMap[i][j].getFlyerLevel()).append("\033[0m");
                }

            }
            print.append("\n");
        }

        for (int rowNumber = 0; rowNumber < this.rowsNumber; rowNumber++) {
            if (emergencyRows.contains(rowNumber)) {
                print.append("-|");
            } else {
                print.append("--");
            }
        }
        print.append("-\n");

        return print.toString();
    }


}

