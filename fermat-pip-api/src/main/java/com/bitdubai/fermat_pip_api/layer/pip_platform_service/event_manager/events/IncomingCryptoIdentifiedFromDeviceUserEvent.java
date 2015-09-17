package com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.events;

import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.interfaces.PlatformEvent;
import com.bitdubai.fermat_api.layer.all_definition.event.EventSource;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.enums.EventType;

/**
 * Created by loui on 20/02/15.
 */
public class IncomingCryptoIdentifiedFromDeviceUserEvent extends AbstractPlatformEvent {


    public IncomingCryptoIdentifiedFromDeviceUserEvent(EventType eventType) {
        super(eventType);
    }


}
