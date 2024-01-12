package me.yves.agent;

import jakarta.annotation.Nonnull;
import me.yves.Aircraft;

import java.util.Objects;
import java.util.logging.Logger;

public abstract class Agent {
    private static final Logger LOGGER = Logger.getLogger(Agent.class.getName());
    @Nonnull
    protected final Aircraft aircraft;
    protected final int agentNumber;
    /**
     * An int representing the number of time {@link #executeCode()} have been executed
     */
    protected int numberOfExecution;

    protected Agent(@Nonnull Aircraft aircraft, int agentNumber) {
        this.aircraft = Objects.requireNonNull(aircraft);
        this.agentNumber = agentNumber;
    }

    /**
     * Execute the agent code, then return the current numberOfExecution of the code
     * @return an int representing the number of code executions after the execution of this code
     */
    @SuppressWarnings("UnusedReturnValue")
    public final int executeCode() {
        LOGGER.fine(() -> "Agent%d started to execute his code (%d execution before)".formatted(agentNumber, numberOfExecution));
        return executeCodeImpl();
    }

    protected abstract int executeCodeImpl();

    protected final void logExecuteCodeEnding() {
        LOGGER.fine(() -> "Agent%d finished to execute his code (%d execution before)".formatted(agentNumber, numberOfExecution));
    }

    public final int getNumberOfExecution() {
        return numberOfExecution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Agent agent = (Agent) o;

        if (agentNumber != agent.agentNumber) return false;
        if (numberOfExecution != agent.numberOfExecution) return false;
        return aircraft.equals(agent.aircraft);
    }

    @Override
    public int hashCode() {
        int result = aircraft.hashCode();
        result = 31 * result + agentNumber;
        result = 31 * result + numberOfExecution;
        return result;
    }

    @Override
    public String toString() {
        return "Agent{" +
               ", agentNumber=" + agentNumber +
               ", numberOfExecution=" + numberOfExecution +
               '}';
    }
}
