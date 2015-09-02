package com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.events;

import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.interfaces.PlatformEvent;
import com.bitdubai.fermat_api.layer.all_definition.event.EventSource;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.enums.EventType;

/**
 * Created by loui on 05/02/15.
 */
public class WalletOpenedEvent extends AbstractPlatformEvent {
    private String publicKey;

    public void setWalletPublicKey (String publicKey){
        this.publicKey = publicKey;
    }
    
    public String getWalletPublicKey() {
        return this.publicKey;
    }
    
    public WalletOpenedEvent (EventType eventType){
        super(eventType);
    }
}
