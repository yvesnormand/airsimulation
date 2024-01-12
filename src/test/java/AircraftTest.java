import me.yves.Aircraft;
import me.yves.Customer;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

class AircraftTest {
    public static void main(String[] args) {
        AircraftTest aircraftTest = new AircraftTest();
        aircraftTest.execute();
    }

    void execute() {
        {
            System.out.println("Testing Aircraft class\n");
            Aircraft a = Aircraft.classicalAircraft();
            Customer mff = null;
            int mffl = -1;
            for (int i = 0; i < a.getNumberOfRows(); i++) {
                for (int j = 0; j < a.getSeatsPerRow(); j++) {
                    long seed = (long) (i + 1) * (j + 10);
                    Customer C = Customer.randomCustomer(seed);
                    if (mffl < C.getFlyerLevel()) {
                        mffl = C.getFlyerLevel();
                        mff = C;
                    }
                    a.placeCustomerToSeat(C, i, j);
                }
            }
            //a.toggleColorOff();  // uncomment to switch off colors
            System.out.println("Aircraft:\n" + a);

            // testing Aircraft methods
            System.out.println("Test 'getNumberOfRows' : " + (a.getNumberOfRows() == 32));
            System.out.println("Test 'getSeatsPerRow' : " + (a.getSeatsPerRow() == 6));
            Set<Integer> L = a.getEmergencyRows();
            System.out.println("Test 'getEmergencyRows' : " + (L.contains(0) && L.contains(12) & L.contains(31)));
            Customer C = Customer.randomCustomer(10);
            System.out.println("Test 'getCustomer' : " + (a.getCustomer(0, 0).map(C::equals).orElse(false)));
            System.out.println("Test 'getMostFrequentFlyer' : " + (Optional.ofNullable(mff).equals(a.getMostFrequentFlyer(10))));
            C = a.getCustomer(1, 1).orElse(null);
            System.out.println("Test 'getCustomerRowNumber' : " + (Optional.ofNullable(C).map(a::getCustomerRowNumber).map(op -> op.equals(OptionalInt.of(1)))));
            System.out.println("Test 'isSeatEmpty' : " + !a.isSeatEmpty(1, 1));
            a.freeSeat(1, 1);
            System.out.println("Test 'freeSeat' and 'isSeatEmpty' : " + a.isSeatEmpty(1, 1));
            a.freeSeat(2, 2);
            System.out.println("Test 'numberOfFreeSeats' : " + (a.numberOfFreeSeats() == 2));
            System.out.println("Test 'isFlightFull' : " + !a.isFlightFull());
            a.placeCustomerToSeat(Customer.randomCustomer(99), 1, 1);
            a.placeCustomerToSeat(Customer.randomCustomer(999), 2, 2);
            System.out.println("Test 'placeCustomerToSeat' and 'isFlightFull' : " + a.isFlightFull());
            a.clearAllCustomer();
            System.out.println("Test 'clearAllCustomer' : " + (!a.isFlightFull() && a.numberOfFreeSeats() == 32 * 6));
        }
    }
}
