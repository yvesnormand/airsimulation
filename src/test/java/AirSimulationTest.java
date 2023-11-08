import me.wizmxn.AirSimulation;

public class AirSimulationTest {

    void main() {
        System.out.println("\n** Sequential execution **\n");
        if (args != null && args.length > 0 && args[0] != null && args[0].equals("animation")) {
            AirSimulation s = new AirSimulation();
            while (!s.aircraft.isFlightFull()) {
                s.agent1();
                s.agent2();
                s.agent3();
                s.agent4();
                System.out.println(s + s.aircraft.cleanString());
                Thread.sleep(100);
            }
            System.out.println(s);
        } else {
            AirSimulation s = new AirSimulation();
            while (!s.aircraft.isFlightFull()) {
                s.agent1();
                s.agent2();
                s.agent3();
                s.agent4();
            }
            System.out.println(s);
        }
    }
}
