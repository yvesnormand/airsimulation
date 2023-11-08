package me.wizmxn.agent;

import me.wizmxn.Aircraft;
import me.wizmxn.Customer;

import java.util.ArrayList;

public class Agent2 extends Agent{
    public Agent2(Aircraft aircraft) {
        super(aircraft);
    }

    @Override
    public void executeCode() {
        boolean placed = false;
        ArrayList<Integer> emergRows = this.aircraft.getEmergencyRowList();

        // generating a new Customer
        Customer c = new Customer();

        // searching free seats on the seatMap
        int row = 0;
        while (!placed && !this.aircraft.isFlightFull() && row < this.aircraft.getNumberOfRows()) {
            int col = 0;
            while (!placed && col < this.aircraft.getSeatsPerRow()) {
                // verifying whether the seat is free
                if (this.aircraft.isSeatEmpty(row, col)) {
                    // if this is an emergency exit seat, and c needs assistence, then we skip
                    if (!emergRows.contains(row) || !c.isNeedingAssistance() || this.aircraft.numberOfFreeSeats() <= this.aircraft.getSeatsPerRow() * this.aircraft.getNumberEmergencyRows()) {
                        this.aircraft.add(c, row, col);
                        placed = true;
                    }
                }
                col++;
            }
            row++;
        }

        // updating counter
        if (placed) this.nAgent2++;
    }
}
