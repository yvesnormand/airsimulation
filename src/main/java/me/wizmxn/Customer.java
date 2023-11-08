package me.wizmxn;
/* Customer class
 *
 * TP of SE (version 2020)
 *
 * AM
 */

import java.util.Random;

public record Customer(int age, int frequentFlyer, int ticketNumber, int flightCost, boolean specialAssistance) {
    private static final double SPECIAL_ASSISTANCE_PROBABILITY = 0.2d;

    public static Customer randomCustomer(long seed) {
        return randomCustomer(new Random(seed));
    }

    public static Customer randomCustomer() {
        return randomCustomer(new Random());
    }

    public static Customer fromCustomer(Customer customer) {
        return new Customer(customer.age,
                customer.frequentFlyer,
                customer.ticketNumber,
                customer.flightCost,
                customer.specialAssistance);
    }

    private static Customer randomCustomer(Random random) {
        int age = random.nextInt(20, 80);
        int frequentFlyer = random.nextInt(10);
        int ticketNumber = random.nextInt(12345678, Integer.MAX_VALUE);
        int flightCost = random.nextInt(500, 1500);
        boolean specialAssistance = random.nextDouble() < SPECIAL_ASSISTANCE_PROBABILITY;
        return new Customer(age, frequentFlyer, ticketNumber, flightCost, specialAssistance);
    }

    // Checking whether the Customer is over 60
    public boolean isOver60() {
        return this.age > 60;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "age=" + age +
                ", frequentFlyer=" + frequentFlyer +
                ", ticketNumber=" + ticketNumber +
                ", flightCost=" + flightCost +
                ", specialAssistance=" + specialAssistance +
                '}';
    }

    public int getFlyerLevel() {
        return frequentFlyer;
    }
}

