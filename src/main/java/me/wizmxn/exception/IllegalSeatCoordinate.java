package me.wizmxn.exception;

public class IllegalSeatCoordinate extends RuntimeException {
    private final int numberOfRows;
    private final int seatsPerRow;
    private final int row;
    private final int col;


    public IllegalSeatCoordinate(int numberOfRows, int seatsPerRow, int row, int col) {
        super(("Aircraft: impossible to verify whether the seat is empty or not: " +
               "seat coordinates out of bounds (bounds = %d,%d | coords = %d,%d)")
                .formatted(numberOfRows, seatsPerRow, row, col));

        this.numberOfRows = numberOfRows;
        this.seatsPerRow = seatsPerRow;
        this.row = row;
        this.col = col;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public int getSeatsPerRow() {
        return seatsPerRow;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
