package me.wizmxn.agent;

import me.wizmxn.Aircraft;

public abstract class Agent {
    protected final Aircraft aircraft;

    public Agent(Aircraft aircraft) {
        this.aircraft = aircraft;
    }
    public abstract void executeCode();

}
