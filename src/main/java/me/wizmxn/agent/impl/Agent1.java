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
        final Set<Integer> emergencyRows = this.aircraft.getEmergencyRows();

        Customer customer = Customer.randomCustomer(); // create a random customer

        // randomly pick a seat
        do {
            int rowIndex = random.nextInt(aircraft.getNumberOfRows());
            int columnIndex = random.nextInt(aircraft.getSeatsPerRow());

            // verifying whether the seat is free, if not continue to seek for an empty seat
            if (!aircraft.isSeatEmpty(rowIndex, columnIndex)) {
                continue;
            }
            // if this is an emergency exit seat, and customer is over60, then we skip
            if (aircraft.isEmergencyRow(rowIndex) &&
                customer.isOver60() &&
                aircraft.numberOfFreeSeats() > aircraft.getSeatsPerRow() * aircraft.getNumberEmergencyRows()) {
                continue;
            }

            aircraft.placeCustomerToSeat(customer, rowIndex, columnIndex);
            placed = true;
        } while (!placed && !this.aircraft.isFlightFull());

        // updating counter
        if (placed) {
            this.numberOfExecution++;
        }

        logExecuteCodeEnding();
        return numberOfExecution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Agent1 agent1 = (Agent1) o;
        return random.equals(agent1.random);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + random.hashCode();
        return result;
    }

    @Override
    public String toString() {
        // ignoring the random
        return super.toString();
    }
}
