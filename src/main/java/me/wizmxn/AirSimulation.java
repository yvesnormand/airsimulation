package me.wizmxn;
/* AirSimulation class
 *
 * TP of SE (version 2020)
 *
 * AM
 */

import me.wizmxn.agent.Agent;
import me.wizmxn.agent.Agent2;
import me.wizmxn.agent.factory.AgentFactory;
import me.wizmxn.agent.factory.ReflectionAgentFactory;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AirSimulation {
    public final static int AGENT_NUMBER = 4;
    private final Aircraft aircraft;
    private final List<Agent> agents;
    private final Map<Agent, Integer> agentWithCodeExecutionCounter;

    // Constructor
    public AirSimulation(AgentFactory agentFactory) {
        this.aircraft = new Aircraft();  // standard model
        this.agents = agentFactory.getAgent(aircraft);
        this.agentWithCodeExecutionCounter = agents.stream()
                .map(agent -> Map.entry(agent, 0))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // Reference to Aircraft
    public Aircraft getAircraftRef() {
        return this.aircraft;
    }

    public void executeAllAgentCode() {
        agents.forEach(Agent::executeCode);
    }

    public void executeAgentCode(int agentNumber) {
        if (agentNumber < 0 || agentNumber >= agents.size()) {
            throw new IllegalArgumentException("Agent number must be defined between 0 and %d (%d) !".formatted(agents.size(), agentNumber));
        }

        agents.get(agentNumber).executeCode();
    }

    @Override
    public String toString() {
        return "AirSimulation{" +
                "aircraft=" + aircraft +
                ", agentWithCodeExecutionCounter=" + agentWithCodeExecutionCounter +
                '}';
    }

}

