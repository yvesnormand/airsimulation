package me.wizmxn.agent.factory;

import jakarta.annotation.Nonnull;
import me.wizmxn.Aircraft;
import me.wizmxn.agent.Agent;

import java.util.List;

public interface AgentFactory {
    @Nonnull
    List<Agent> getAgent(@Nonnull Aircraft aircraft);
}
