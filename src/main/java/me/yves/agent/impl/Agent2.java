package me.yves.agent.impl;

import me.yves.Aircraft;
import me.yves.Customer;
import me.yves.agent.Agent;

import java.util.function.IntPredicate;

@SuppressWarnings("unused")
public class Agent2 extends Agent {
    private static final int AGENT_NUMBER = 2;

    public Agent2(Aircraft aircraft) {
        super(aircraft, AGENT_NUMBER);
    }

    @Override
    public int executeCodeImpl() {
        boolean placed = false;

        // generating a new random Customer
        Customer customer = Customer.randomCustomer();

        final IntPredicate isRowFull = rowIndex ->
                !aircraft.isFlightFull() && rowIndex < aircraft.getNumberOfRows();
        // searching free seats on the seatMap
        for (int row = 0; !placed && isRowFull.test(row); row++) {
            for (int col = 0; !placed && col < aircraft.getSeatsPerRow(); col++) {
                // verifying whether the seat is free
                if (!aircraft.isSeatEmpty(row, col)) {
                    continue;
                }
                // if this is an emergency exit seat, and customer needs assistance, then we skip
                if (aircraft.isEmergencyRow(row) &&
                    customer.specialAssistance() &&
                    aircraft.numberOfFreeSeats() > aircraft.getSeatsPerRow() * aircraft.getNumberEmergencyRows()) {
                    continue;
                }
                aircraft.placeCustomerToSeat(customer, row, col);
                placed = true;
            }
        }

        // updating counter
        if (placed) {
            numberOfExecution++;
        }

        logExecuteCodeEnding();
        return numberOfExecution;
    }
}
