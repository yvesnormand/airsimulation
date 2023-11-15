package me.wizmxn;
/* AirSimulation class
 *
 * TP of SE (version 2020)
 *
 * AM
 */

import me.wizmxn.agent.Agent;
import me.wizmxn.agent.factory.AgentFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AirSimulation {
    public final static int AGENT_NUMBER = 4;
    private final Aircraft aircraft;
    private final List<Agent> agents;

    // Constructor
    public AirSimulation(AgentFactory agentFactory) {
        this.aircraft = Aircraft.classicalAircraft();  // standard model
        this.agents = agentFactory.getAgent(aircraft);

    }

    // Reference to Aircraft
    public Aircraft getAirCraft() {
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
               ", agentWithCodeExecutionCounter=" + getAgentWithCodeExecutionCounter() +
               '}';
    }

    private Map<Agent, Integer> getAgentWithCodeExecutionCounter() {
        return agents.stream()
                .map(agent -> Map.entry(agent, agent.getNumberOfExecution()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}

