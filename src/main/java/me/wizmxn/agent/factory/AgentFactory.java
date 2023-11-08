package me.wizmxn.agent.factory;

import me.wizmxn.Aircraft;
import me.wizmxn.agent.Agent;

import java.util.List;

public interface AgentFactory {
    List<Agent> getAgent(Aircraft aircraft);
}
