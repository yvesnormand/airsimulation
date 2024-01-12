package me.yves.agent.impl;

import me.yves.Aircraft;
import me.yves.agent.Agent;

public class Agent3 extends Agent {
    private static final int AGENT_NUMBER = 3;

    public Agent3(Aircraft aircraft) {
        super(aircraft, AGENT_NUMBER);
    }

    @Override
    public int executeCodeImpl() {
        //todo
        logExecuteCodeEnding();
        return numberOfExecution;
    }
}
