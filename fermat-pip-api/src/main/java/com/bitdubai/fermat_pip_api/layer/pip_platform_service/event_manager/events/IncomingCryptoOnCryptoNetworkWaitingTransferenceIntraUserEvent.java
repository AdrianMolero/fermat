package com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.events;

import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.enums.EventType;

/**
 * Created by eze on 2015.09.02..
 */
public class IncomingCryptoOnCryptoNetworkWaitingTransferenceIntraUserEvent extends AbstractPlatformEvent {
    public IncomingCryptoOnCryptoNetworkWaitingTransferenceIntraUserEvent() {
        super(EventType.INCOMING_CRYPTO_ON_CRYPTO_NETWORK_WAITING_TRANSFERENCE_INTRA_USER);
    }
}
