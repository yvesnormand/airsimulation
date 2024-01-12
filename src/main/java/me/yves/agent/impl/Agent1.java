package me.yves.agent.impl;

import jakarta.annotation.Nonnull;
import me.yves.Aircraft;
import me.yves.Customer;
import me.yves.agent.Agent;

import java.util.Random;

@SuppressWarnings("unused")
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
        Customer customer = Customer.randomCustomer(); // create a random customer

        // randomly pick a seat
        do {
            int row = random.nextInt(aircraft.getNumberOfRows());
            int col = random.nextInt(aircraft.getSeatsPerRow());

            // verifying whether the seat is free, if not continue to seek for an empty seat
            if (!aircraft.isSeatEmpty(row, col)) {
                continue;
            }
            // if this is an emergency exit seat, and customer is over60, then we skip
            if (aircraft.isEmergencyRow(row) &&
                customer.isOver60() &&
                aircraft.numberOfFreeSeats() > aircraft.getSeatsPerRow() * aircraft.getNumberEmergencyRows()) {
                continue;
            }

            aircraft.placeCustomerToSeat(customer, row, col);

            placed = true;
        } while (!placed && !aircraft.isFlightFull());

        // updating counter
        if (placed) {
            numberOfExecution++;
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

}
