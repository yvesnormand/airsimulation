package me.yves;
/* Aircraft class
 *
 * TP of SE (version 2020)
 *
 * AM
 */

import jakarta.annotation.Nonnull;
import me.yves.exception.AircraftSeatNotEmptyException;
import me.yves.exception.IllegalSeatCoordinate;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class Aircraft {
    private static final Logger LOGGER = Logger.getLogger(Aircraft.class.getName());
    private static final int MINIMAL_EXIT_SECURITY_NUMBER = 2;
    private static final int MINIMAL_ROWS_NUMBER = 3;
    private static final int MINIMAL_NUMBER_OF_SEAT_PER_ROWS = 2;
    private static final int MINIMAL_AISLE_NUMBER = 1;
    private final int rowsNumber;
    private final int numberOfSeatsPerRow;
    private final int flightCapacity;
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
    private int freeSeatsNumber;
    private boolean color = true;

    /**
     *
     * @param rowsNumber the number of rows
     * @param numberOfSeatsPerRow the number of seats per row
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
        this.aisleSeats = Set.copyOf(aisleSeats); // immutable defensive copy

        checkEmergencyRowsValidity(emergencyRows);
        this.emergencyRows = Set.copyOf(emergencyRows); // immutable defensive copy

        this.seatMap = new Customer[rowsNumber][numberOfSeatsPerRow];
        this.flightCapacity = rowsNumber * numberOfSeatsPerRow;
        this.freeSeatsNumber = flightCapacity;

    }

    @Nonnull
    public static Aircraft classicalAircraft() {
        return new Aircraft(32, 6, Set.of(3, 4), Set.of(0, 12, 31));
    }

    @Nonnull
    // Looking for the Customer in the Aircraft with higher flyer frequency under a given flyerLevelLimit
    public Optional<Customer> getMostFrequentFlyer(int flyerLevelLimit) {
        int maxFlyerLevel = 0;
        Customer customer = null;

        for (int row = 0; row < rowsNumber; row++) {
            for (int col = 0; col < numberOfSeatsPerRow; col++) {
                if (isSeatEmpty(row, col)) {
                    continue;
                }
                int currentFlyerLevel = seatMap[row][col].getFlyerLevel();
                if (currentFlyerLevel <= flyerLevelLimit && maxFlyerLevel < currentFlyerLevel) {
                    customer = seatMap[row][col];
                    maxFlyerLevel = currentFlyerLevel;
                }
            }
        }

        return Optional.ofNullable(customer)
                .map(Customer::copyFromCustomer); // defensive copy
    }

    // Get the Row Number of a given Customer
    public OptionalInt getCustomerRowNumber(@Nonnull Customer c) {
        Objects.requireNonNull(c);
        final BiPredicate<Integer, Integer> isCustomerPlace =
                (row, col) -> !isSeatEmpty(row, col)
                              && seatMap[col][col].equals(c);

        for (int row = 0; row < rowsNumber; row++) {
            for (int col = 0; col < numberOfSeatsPerRow; col++) {
                if (isCustomerPlace.test(row, col)) {
                    return OptionalInt.of(row);
                }
            }
        }

        return OptionalInt.empty();
    }

    public void freeSeat(int row, int col) {
        checkOrThrowSeatCoordinate(row, col);
        if (isSeatEmpty(row, col)) {
            return;
        }
        synchronized (this) {
            this.freeSeatsNumber++;
        }
        seatMap[row][col] = null;
        LOGGER.fine(() -> "Seat emptied on (%d,%d)".formatted(row, col));
    }

    /**
     *
     * Placing a Customer in a given Aircraft seat
     * @return the number of freeSeat remaining
     * @throws AircraftSeatNotEmptyException if the seat we are trying to fill is not empty
     */
    @SuppressWarnings("UnusedReturnValue")
    public int placeCustomerToSeat(@Nonnull Customer customer, int row, int col) {
        Objects.requireNonNull(customer);
        checkOrThrowSeatCoordinate(row, col);
        if (!isSeatEmpty(row, col)) {
            throw new AircraftSeatNotEmptyException(row, col, customer);
        }
        seatMap[row][col] = Customer.copyFromCustomer(customer);
        LOGGER.fine(() -> "Customer %s placed on (%d,%d)".formatted(customer.simpleToString(), row, col));
        synchronized (this) {
            this.freeSeatsNumber--;
        }
        return freeSeatsNumber;
    }

    @SuppressWarnings("UnusedReturnValue")
    public Aircraft clearAllCustomer() {
        for (Customer[] customers : seatMap) {
            Arrays.fill(customers, null);
        }
        this.freeSeatsNumber = flightCapacity;
        return this;
    }

    // Get an optional Customer in a specific seat from the coordinates or optional empty
    @Nonnull
    public Optional<Customer> getCustomer(int row, int col) {
        checkOrThrowSeatCoordinate(row, col);
        return Optional.ofNullable(seatMap[row][col])
                .map(Customer::copyFromCustomer);
    }

    public int getNumberOfRows() {
        return this.rowsNumber;
    }

    public int getSeatsPerRow() {
        return this.numberOfSeatsPerRow;
    }

    public Set<Integer> getEmergencyRows() {
        return new HashSet<>(emergencyRows);
    }

    public int getNumberEmergencyRows() {
        return emergencyRows.size();
    }

    // Switch off colors in toString (for an incompatibility with the color system)
    public void toggleColorOff() {
        this.color = false;
    }

    public boolean isSeatEmpty(int row, int col) {
        checkOrThrowSeatCoordinate(row, col);
        return seatMap[row][col] == null;
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

    public boolean isEmergencyRow(int row) {
        if (!isRowNumberInBounds(row)) {
            throw new RuntimeException("Row out of bound %d".formatted(row));
        }
        return emergencyRows.contains(row);
    }

    // Verify that a row index is in the bounds
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean isRowNumberInBounds(int row) {
        return row >= 0 && row < rowsNumber;
    }

    // Verify that a column index is in the bounds
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean isSeatNumberInBounds(int col) {
        return col >= 0 && col < numberOfSeatsPerRow;
    }

    private void checkOrThrowSeatCoordinate(int row, int col) {
        if (!isRowNumberInBounds(row) || !isSeatNumberInBounds(col)) {
            throw new IllegalSeatCoordinate(getNumberOfRows(), getSeatsPerRow(), row, col);
        }
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

        int aisleNumber = aisleSeats.size();
        // check the minimal number of aisles (for 1 aisle, there are 2 corresponding seats)
        if (aisleNumber < MINIMAL_AISLE_NUMBER * 2) {
            throw new IllegalArgumentException("Aircraft: the aircraft needs to have at least %d aisle!".formatted(MINIMAL_AISLE_NUMBER));
        }

        final Predicate<Integer> isAisleSeatNextToWindows =
                aisleSeat -> aisleSeat == 1 || aisleSeat == aisleNumber - 1;
        if (aisleSeats.stream().anyMatch(isAisleSeatNextToWindows)) {
            throw new IllegalArgumentException("Aircraft: aisles cannot be located next to the windows!");
        }

        if (aisleSeats.stream().anyMatch(aisleSeat -> !isSeatNumberInBounds(aisleSeat))) {
            throw new IllegalArgumentException("Aircraft: the aircraft aisleSeats are invalid, they must all be between 0 and %d".formatted(numberOfSeatsPerRow));
        }
    }

    //     Printing
    // this code is old and haven't been fully refactored
    @Override
    public String toString() {
        final String whiteColor = "\033[1m";
        final String redColor = "\033[0m";

        final StringBuilder outerBorder = new StringBuilder();
        for (int row = 0; row < rowsNumber; row++) {
            outerBorder.append(isEmergencyRow(row) ? "-|" : "--");
        }

        final StringBuilder print = new StringBuilder("\n");


        print.append(whiteColor);
        print.append(outerBorder);
        print.append("-\n");

        for (int j = 0; j < numberOfSeatsPerRow; j++) {
            if (aisleSeats.contains(j) && aisleSeats.contains(j + 1)) {
                //print midline
                print.append(whiteColor)
                        .append("--".repeat(Math.max(0, rowsNumber)))
                        .append("-\n");
            }

            for (int i = 0; i < rowsNumber; i++) {
                Optional<Customer> customOpt = getCustomer(i, j);
                if (customOpt.isEmpty()) {
                    print.append(" ").append(whiteColor).append("x").append(redColor);
                    continue;
                }
                Customer customer = customOpt.get();
                if (!color) {
                    print.append(" ").append(customer.getFlyerLevel());
                    continue;
                }
                if (customer.specialAssistance()) {
                    print.append(" \033[31;1m").append(customer.getFlyerLevel()).append(redColor);
                } else if (customer.isOver60()) {
                    print.append(" \033[33;1m").append(customer.getFlyerLevel()).append(redColor);
                } else {
                    print.append(" \033[32;1m").append(customer.getFlyerLevel()).append(redColor);
                }

            }
            print.append("\n");
        }

        print.append(whiteColor);
        print.append(outerBorder);
        print.append("-\n");
        print.append(redColor);

        return print.toString();
    }


}

