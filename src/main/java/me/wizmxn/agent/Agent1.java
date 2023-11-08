package me.wizmxn.agent;

import me.wizmxn.Aircraft;
import me.wizmxn.Customer;

import java.util.ArrayList;
import java.util.Random;

public class Agent1 extends Agent {
    private final Random random;

    public Agent1(Aircraft aircraft) {
        super(aircraft);
        random = new Random();
    }

    @Override
    public void executeCode() {
        boolean placed = false;
        ArrayList<Integer> emergRows = this.aircraft.getEmergencyRowList();
        Customer c = Customer.randomCustomer();

        // randomly pick a seat
        do {
            int row = random.nextInt(this.aircraft.getNumberOfRows());
            int col = random.nextInt(this.aircraft.getSeatsPerRow());

            // verifying whether the seat is free
            if (this.aircraft.isSeatEmpty(row, col)) {
                // if this is an emergency exit seat, and c is over60, then we skip
                if (!emergRows.contains(row) || !c.isOver60() || this.aircraft.numberOfFreeSeats() <= this.aircraft.getSeatsPerRow() * this.aircraft.getNumberEmergencyRows()) {
                    this.aircraft.add(c, row, col);
                    placed = true;
                }
            }
        }
        while (!placed && !this.aircraft.isFlightFull());

        // updating counter
        if (placed) this.nAgent1++;
    }
}
