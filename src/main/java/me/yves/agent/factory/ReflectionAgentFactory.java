package me.yves.agent.factory;

import jakarta.annotation.Nonnull;
import me.yves.Aircraft;
import me.yves.agent.Agent;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class ReflectionAgentFactory implements AgentFactory {
    private static final Logger LOGGER = Logger.getLogger(ReflectionAgentFactory.class.getName());

    @Nonnull
    @Override
    public List<Agent> getAgent(@Nonnull Aircraft aircraft) {
        Set<Class<? extends Agent>> allClassesUsingClassLoader = findAllClassesUsingReflectionsLibrary("me.yves.agent.impl");
        List<Agent> agentList = allClassesUsingClassLoader.stream()
                .map(aClass -> {
                    try {
                        return (Agent) aClass.getConstructor(Aircraft.class).newInstance(aircraft);
                    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                             InvocationTargetException e) {
                        LOGGER.severe("Couldn't load agents !");
                        throw new RuntimeException(e);

                    }
                }).toList();
        LOGGER.info(() -> "Loaded %d agents, %s".formatted(agentList.size(), agentList));
        return agentList;
    }

    public Set<Class<? extends Agent>> findAllClassesUsingReflectionsLibrary(String packageName) {
        Reflections reflections = new Reflections(packageName, new SubTypesScanner(false));
        return new HashSet<>(reflections.getSubTypesOf(Agent.class));
    }
}
