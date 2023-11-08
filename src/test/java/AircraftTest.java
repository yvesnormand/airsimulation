import me.wizmxn.Aircraft;
import me.wizmxn.Customer;

import java.util.ArrayList;

class AircraftTest {

    void main() {
        {
            System.out.println("Testing Aircraft class\n");
            Aircraft a = new Aircraft();
            Customer mff = null;
            int mffl = -1;
            for (int i = 0; i < a.getNumberOfRows(); i++) {
                for (int j = 0; j < a.getSeatsPerRow(); j++) {
                    Customer C = new Customer((i + 1) * (j + 10));
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
            ArrayList<Integer> L = a.getEmergencyRowList();
            System.out.println("Test 'getEmergencyRowList' : " + (L.contains(0) && L.contains(12) & L.contains(31)));
            Customer C = new Customer(10);
            System.out.println("Test 'getCustomer' : " + (C.equals(a.getCustomer(0, 0))));
            System.out.println("Test 'getMostFrequentFlyer' : " + (mff.equals(a.getMostFrequentFlyer(10))));
            C = a.getCustomer(1, 1);
            System.out.println("Test 'getCustomerRowNumber' : " + (a.getCustomerRowNumber(C) == 1));
            System.out.println("Test 'getCustomerSeatPlaceInRowRange' : " + (a.getCustomerSeatPlaceInRowRange(C) == 1));
            System.out.println("Test 'isSeatEmpty' : " + !a.isSeatEmpty(1, 1));
            a.freeSeat(1, 1);
            System.out.println("Test 'freeSeat' and 'isSeatEmpty' : " + a.isSeatEmpty(1, 1));
            a.freeSeat(2, 2);
            System.out.println("Test 'numberOfFreeSeats' : " + (a.numberOfFreeSeats() == 2));
            System.out.println("Test 'isFlightFull' : " + !a.isFlightFull());
            a.placeCustomerToSeat(new Customer(99), 1, 1);
            a.placeCustomerToSeat(new Customer(999), 2, 2);
            System.out.println("Test 'placeCustomerToSeat' and 'isFlightFull' : " + a.isFlightFull());
            a.clearAllCustomer();
            System.out.println("Test 'clearAllCustomer' : " + (!a.isFlightFull() && a.numberOfFreeSeats() == 32 * 6));
        }
    }
}
