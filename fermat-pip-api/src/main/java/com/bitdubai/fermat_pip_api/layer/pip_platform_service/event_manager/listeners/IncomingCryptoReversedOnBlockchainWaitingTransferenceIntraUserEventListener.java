package com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.listeners;

import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.enums.EventType;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.interfaces.FermatEventMonitor;

/**
 * Created by eze on 2015.09.02..
 */
public class IncomingCryptoReversedOnBlockchainWaitingTransferenceIntraUserEventListener extends GenericEventListener {
    public IncomingCryptoReversedOnBlockchainWaitingTransferenceIntraUserEventListener(FermatEventMonitor fermatEventMonitor) {
        super(EventType.INCOMING_CRYPTO_REVERSED_ON_BLOCKCHAIN_WAITING_TRANSFERENCE_INTRA_USER, fermatEventMonitor);
    }
}
