package me.wizmxn.agent;

import jakarta.annotation.Nonnull;
import me.wizmxn.Aircraft;

public abstract class Agent {
    @Nonnull
    protected final Aircraft aircraft;

    public Agent(@Nonnull Aircraft aircraft) {
        this.aircraft = aircraft;
    }
    public abstract void executeCode();
}
