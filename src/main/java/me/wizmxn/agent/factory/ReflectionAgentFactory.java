package me.wizmxn.agent.factory;

import me.wizmxn.Aircraft;
import me.wizmxn.agent.Agent;

import java.util.Collections;
import java.util.List;

public class ReflectionAgentFactory implements AgentFactory {
    @Override
    public List<Agent> getAgent(Aircraft aircraft) {
        //Todo load agent trought reflection
        return Collections.emptyList();
    }
}
