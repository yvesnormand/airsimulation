package me.wizmxn.agent.impl;

import jakarta.annotation.Nonnull;
import me.wizmxn.Aircraft;
import me.wizmxn.Customer;
import me.wizmxn.agent.Agent;

import java.util.Random;
import java.util.Set;

public class Agent1 extends Agent {
    private static final int AGENT_NUMBER = 1;
    @Nonnull
    private final Random random;

    public Agent1(@Nonnull Aircraft aircraft) {
        super(aircraft, AGENT_NUMBER);
        random = new Random();
    }

    @Override
    public int executeCodeImpl() {
        boolean placed = false;
        Set<Integer> emergencyRows = this.aircraft.getEmergencyRows();
        Customer customer = Customer.randomCustomer(); // create a random customer

        // randomly pick a seat
        do {
            int row = random.nextInt(this.aircraft.getNumberOfRows());
            int col = random.nextInt(this.aircraft.getSeatsPerRow());

            // verifying whether the seat is free, if not continue to seek for an empty seat
            if (aircraft.isSeatEmpty(row, col)) {
                continue;
            }
            // if this is an emergency exit seat, and customer is over60, then we skip
            if (emergencyRows.contains(row) && customer.isOver60() &&
                aircraft.numberOfFreeSeats() > aircraft.getSeatsPerRow() * aircraft.getNumberEmergencyRows()) {
                continue;
            }

            this.aircraft.placeCustomerToSeat(customer, row, col);
            placed = true;
        } while (!placed && !this.aircraft.isFlightFull());

        // updating counter
        if (placed) {
            this.numberOfExecution++;
        }
        return numberOfExecution;
    }

}
