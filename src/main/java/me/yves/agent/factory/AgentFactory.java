package me.yves.agent.factory;

import jakarta.annotation.Nonnull;
import me.yves.Aircraft;
import me.yves.agent.Agent;

import java.util.List;

public interface AgentFactory {
    @Nonnull
    List<Agent> getAgent(@Nonnull Aircraft aircraft);
}
