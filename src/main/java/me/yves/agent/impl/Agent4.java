package me.yves.agent.impl;

import me.yves.Aircraft;
import me.yves.agent.Agent;

@SuppressWarnings("unused")
public class Agent4 extends Agent {
    private static final int AGENT_NUMBER = 4;

    public Agent4(Aircraft aircraft) {
        super(aircraft, AGENT_NUMBER);
    }

    @Override
    public int executeCodeImpl() {
        //todo
        logExecuteCodeEnding();
        return numberOfExecution;
    }
}
