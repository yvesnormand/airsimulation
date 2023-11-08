package me.wizmxn.agent.factory;

import jakarta.annotation.Nonnull;
import me.wizmxn.Aircraft;
import me.wizmxn.agent.Agent;

import java.util.Collections;
import java.util.List;

public class ReflectionAgentFactory implements AgentFactory {
    @Nonnull
    @Override
    public List<Agent> getAgent(@Nonnull Aircraft aircraft) {
        //Todo load agent trought reflection
        return Collections.emptyList();
    }
}
