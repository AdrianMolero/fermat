package com.bitdubai.smartwallet.platform.layer._10_agent;


/**
 * Created by ciencias on 21.01.15.
 */
public interface AgentSubsystem {
    public void start () throws CantStartSubsystemException;
    public AIAgent getAIAgent();
}
