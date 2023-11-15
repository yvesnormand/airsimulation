package me.wizmxn.agent.impl;

import me.wizmxn.Aircraft;
import me.wizmxn.agent.Agent;

public class Agent4 extends Agent {
    private static final int AGENT_NUMBER = 4;

    public Agent4(Aircraft aircraft) {
        super(aircraft, AGENT_NUMBER);
    }

    @Override
    public int executeCodeImpl() {
        //todo

        return numberOfExecution;
    }
}
