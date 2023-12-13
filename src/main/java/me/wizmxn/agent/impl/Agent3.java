package me.wizmxn.agent.impl;

import me.wizmxn.Aircraft;
import me.wizmxn.agent.Agent;

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
