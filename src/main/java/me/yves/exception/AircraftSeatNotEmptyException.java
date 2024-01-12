package me.yves.exception;

import me.yves.Customer;

import java.util.Objects;

@SuppressWarnings("unused")
public class AircraftSeatNotEmptyException extends RuntimeException {
    private final int row;
    private final int col;
    private final Customer customer;

    public AircraftSeatNotEmptyException(int row, int col, Customer customer) {
        super("Aircraft: the seat (%d,%d) is not empty (%s)".formatted(row, col, Objects.requireNonNull(customer)));

        this.row = row;
        this.col = col;
        this.customer = customer;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Customer getCustomer() {
        return customer;
    }
}
