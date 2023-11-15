package me.wizmxn;

import me.wizmxn.agent.factory.ReflectionAgentFactory;

public class Main {
    public static void main(String[] args) {
        AirSimulation airSimulation = new AirSimulation(new ReflectionAgentFactory());
        Aircraft airCraft = airSimulation.getAirCraft();
        System.out.println(airCraft);
    }
}
