package me.wizmxn.agent.impl;

import me.wizmxn.Aircraft;
import me.wizmxn.Customer;
import me.wizmxn.agent.Agent;

import java.util.Set;

public class Agent2 extends Agent {
    private static final int AGENT_NUMBER = 2;

    public Agent2(Aircraft aircraft) {
        super(aircraft, AGENT_NUMBER);
    }

    @Override
    public int executeCodeImpl() {
        boolean placed = false;
        Set<Integer> emergencyRowList = this.aircraft.getEmergencyRows();

        // generating a new random Customer
        Customer customer = Customer.randomCustomer();

        // searching free seats on the seatMap
        int rowIndex = 0;
        while (!placed && !this.aircraft.isFlightFull() && rowIndex < this.aircraft.getNumberOfRows()) {
            int columnIndex = 0;
            while (!placed && columnIndex < this.aircraft.getSeatsPerRow()) {
                // verifying whether the seat is free
                if (!aircraft.isSeatEmpty(rowIndex, columnIndex)) {
                    continue;
                }
                // if this is an emergency exit seat, and customer needs assistence, then we skip
                if (aircraft.isEmergencyRow(rowIndex) &&
                    customer.specialAssistance() &&
                    this.aircraft.numberOfFreeSeats() > this.aircraft.getSeatsPerRow() * this.aircraft.getNumberEmergencyRows()) {
                    continue;
                }
                this.aircraft.placeCustomerToSeat(customer, rowIndex, columnIndex);
                placed = true;
                columnIndex++;
            }
            rowIndex++;
        }

        // updating counter
        if (placed) {
            this.numberOfExecution++;
        }

        logExecuteCodeEnding();
        return numberOfExecution;
    }
}
