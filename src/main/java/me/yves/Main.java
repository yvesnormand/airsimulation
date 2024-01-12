package me.yves;

import me.yves.agent.factory.ReflectionAgentFactory;

import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    /**
     * Logging level of the application, feel free to lower it for more details!
     */
    private static final Level LOGGER_LEVEL = Level.INFO;

    static {
        setLoggerLevel(); // ignore this, just setting at initialization LOGGER_LEVEL for the application logging level
    }

    public static void main(String[] args) throws InterruptedException {
        AirSimulation airSimulation = new AirSimulation(new ReflectionAgentFactory());
        Aircraft airCraft = airSimulation.getAirCraft();
        LOGGER.fine(airSimulation::toString);

        LOGGER.fine("\n** Sequential execution **\n");
        EXECUTION_MODE executionMode = getExecutionMode();
        // please note that normally to benchmark you should avoid using techniques like currentMs but rather use JMH
        long startCurrentMs = System.currentTimeMillis();
        long sleepTimeMs = 0;
        while (!airCraft.isFlightFull()) {
            airSimulation.executeAllAgentCode();
            if (executionMode == EXECUTION_MODE.ANIMATION) {
                LOGGER.info(airCraft::toString);
                //noinspection BusyWait
                Thread.sleep(100);
                sleepTimeMs++;
            }
        }
        long timeElapsedMs = System.currentTimeMillis() - startCurrentMs;
        long finalSleepTimeMs = sleepTimeMs;
        LOGGER.info(() -> "Running took %d ms (%f seconds), ".formatted(timeElapsedMs, timeElapsedMs * 1f / 1_000) +
                          "code running time took effectively %d ms (%f seconds)".formatted(timeElapsedMs - finalSleepTimeMs, (timeElapsedMs - finalSleepTimeMs) * 1f / 1_000));
        LOGGER.info(airSimulation::toString);
    }


    private static EXECUTION_MODE getExecutionMode() {
        final int rangeMax = EXECUTION_MODE.values().length;
        Scanner sc = new Scanner(System.in);
        LOGGER.info("Choose between : \n" + EXECUTION_MODE.getExecutionModeOfferList());
        int index = -1;
        do {
            try {
                index = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException ignored) {
                //do nothing
            }
        } while (index < 0 || index >= rangeMax);
        sc.close();
        return EXECUTION_MODE.values()[index];
    }

    private static void setLoggerLevel() {
        //you should avoid doing this in real application
        //
        Logger rootLog = Logger.getLogger("");
        rootLog.setLevel(LOGGER_LEVEL);
        rootLog.getHandlers()[0].setLevel(LOGGER_LEVEL);
        rootLog.setFilter(r -> r.getSourceClassName().startsWith("me.yves"));
    }


    private enum EXECUTION_MODE {
        NORMAL("Normal execution"),
        ANIMATION("Normal execution but with nicely printed aircraft");

        private final String detail;

        EXECUTION_MODE(String detail) {
            this.detail = detail;
        }

        public static String getExecutionModeOfferList() {
            return Arrays.stream(EXECUTION_MODE.values())
                    .map(executionMode -> "   * " + executionMode.name() + " " + executionMode.ordinal() + " (" + executionMode.getDetail() + ")")
                    .collect(Collectors.joining("\n", "{\n", "\n}"));
        }

        public String getDetail() {
            return detail;
        }
    }


    private static void setLoggerLevel() {
        //you should avoid doing this in real application
        //
        Logger rootLog = Logger.getLogger("");
        rootLog.setLevel(LOGGER_LEVEL);
        rootLog.getHandlers()[0].setLevel(LOGGER_LEVEL);
        rootLog.setFilter(r -> r.getSourceClassName().startsWith("me.yves"));
    }
}
