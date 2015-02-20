package com.bitdubai.wallet_platform_api.layer._2_platform_service.event_manager.events;

import com.bitdubai.wallet_platform_api.layer._1_definition.event.PlatformEvent;
import com.bitdubai.wallet_platform_api.layer._2_platform_service.event_manager.EventSource;
import com.bitdubai.wallet_platform_api.layer._2_platform_service.event_manager.EventType;

/**
 * Created by loui on 20/02/15.
 */
public class MoneyReceivedEvent implements PlatformEvent {
    private EventType eventType;
    private EventSource eventSource;

    public MoneyReceivedEvent(EventType eventType){
        this.eventType = eventType;
    }

    @Override
    public EventType getEventType() {
        return this.eventType;
    }

    @Override
    public void setSource(EventSource eventSource) {
        this.eventSource = eventSource;
    }

    @Override
    public EventSource getSource() {
        return this.eventSource;
    }
}
