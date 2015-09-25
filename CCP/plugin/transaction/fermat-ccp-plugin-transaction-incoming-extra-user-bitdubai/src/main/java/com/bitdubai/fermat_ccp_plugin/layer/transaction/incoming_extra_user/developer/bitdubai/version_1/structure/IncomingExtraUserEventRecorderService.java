package com.bitdubai.fermat_ccp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;

import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.enums.EventType;
import com.bitdubai.fermat_ccp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.event_handlers.IncomingCryptoOnBlockchainNetworkWaitingTransferenceExtraUserEventHandler;
import com.bitdubai.fermat_ccp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.event_handlers.IncomingCryptoOnCryptoNetworkWaitingTransferenceExtraUserEventHandler;
import com.bitdubai.fermat_ccp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.event_handlers.IncomingCryptoReversedOnBlockchainWaitingTransferenceExtraUserEventHandler;
import com.bitdubai.fermat_ccp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.event_handlers.IncomingCryptoReversedOnCryptoNetworkWaitingTransferenceExtraUserEventHandler;
import com.bitdubai.fermat_ccp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.exceptions.CantSaveEventException;
import com.bitdubai.fermat_ccp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.exceptions.CantStartServiceException;
import com.bitdubai.fermat_ccp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.interfaces.DealsWithRegistry;
import com.bitdubai.fermat_ccp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.interfaces.TransactionService;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEventListener;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ciencias on 3/30/15.
 * Modified by Arturo Vallone 25/04/2015
 */

/**
 * Esta es la clase que escucha los eventos relacionados a Incoming Crypto disparados por cualquier plugin que reciba
 * crypto currencies. 
 *
 * Cuando detecta un evento lo escribe en su tabla de base de datos.
 *
 *
 *
 * * * * * * * *
 */

public class IncomingExtraUserEventRecorderService implements com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.interfaces.DealsWithEvents, DealsWithRegistry, TransactionService {

    /**
     * DealsWithEvents Interface member variables.
     */
    private com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.interfaces.EventManager eventManager;
    private List<FermatEventListener> listenersAdded = new ArrayList<>();

    /*
     * DealsWithRegistry Interface member variables.
     */
    private IncomingExtraUserRegistry registry;


    /**
     * TransactionService Interface member variables.
     */
    private ServiceStatus serviceStatus = ServiceStatus.CREATED;


    public IncomingExtraUserEventRecorderService(final com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.interfaces.EventManager eventManager, final IncomingExtraUserRegistry registry){
        this.eventManager = eventManager;
        this.registry = registry;
    }


    /**
     * DealWithEvents Interface implementation.
     */
    @Override
    public void setEventManager(com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.interfaces.EventManager eventManager) {
        this.eventManager = eventManager;
    }

    /**
     * DealWithEvents Interface implementation.
     */
    @Override
    public void setRegistry(IncomingExtraUserRegistry registry) {
        this.registry = registry;
    }

    public void saveEvent(FermatEvent event) throws CantSaveEventException {
        if(!serviceStatus.equals(ServiceStatus.STARTED))
            throw new CantSaveEventException(CantSaveEventException.DEFAULT_MESSAGE, null, null, "You must start the service first");

        this.registry.saveNewEvent(event.getEventType().getCode(), event.getSource().getCode());
    }


    /**
     * TransactionService interface implementation.
     */
    @Override
    public void start() throws CantStartServiceException {

        /**
         * I will initialize the handling of com.bitdubai.platform events.
         */
        try{
            FermatEventListener onBlockchainFermatEventListener = eventManager.getNewListener(EventType.INCOMING_CRYPTO_ON_BLOCKCHAIN_WAITING_TRANSFERENCE_EXTRA_USER);
            FermatEventListener onCryptoNetworkFermatEventListener = eventManager.getNewListener(EventType.INCOMING_CRYPTO_ON_CRYPTO_NETWORK_WAITING_TRANSFERENCE_EXTRA_USER);
            FermatEventListener reversedOnBlockchainFermatEventListener = eventManager.getNewListener(EventType.INCOMING_CRYPTO_REVERSED_ON_BLOCKCHAIN_WAITING_TRANSFERENCE_EXTRA_USER);
            FermatEventListener reversedOnCryptoNetworkFermatEventListener = eventManager.getNewListener(EventType.INCOMING_CRYPTO_REVERSED_ON_CRYPTO_NETWORK_WAITING_TRANSFERENCE_EXTRA_USER);

            onBlockchainFermatEventListener.setEventHandler(new IncomingCryptoOnBlockchainNetworkWaitingTransferenceExtraUserEventHandler(this));
            onCryptoNetworkFermatEventListener.setEventHandler(new IncomingCryptoOnCryptoNetworkWaitingTransferenceExtraUserEventHandler(this));
            reversedOnBlockchainFermatEventListener.setEventHandler(new IncomingCryptoReversedOnBlockchainWaitingTransferenceExtraUserEventHandler(this));
            reversedOnCryptoNetworkFermatEventListener.setEventHandler(new IncomingCryptoReversedOnCryptoNetworkWaitingTransferenceExtraUserEventHandler(this));

            eventManager.addListener(onBlockchainFermatEventListener);
            eventManager.addListener(onCryptoNetworkFermatEventListener);
            eventManager.addListener(reversedOnBlockchainFermatEventListener);
            eventManager.addListener(reversedOnCryptoNetworkFermatEventListener);

            listenersAdded.add(onBlockchainFermatEventListener);
            listenersAdded.add(onCryptoNetworkFermatEventListener);
            listenersAdded.add(reversedOnBlockchainFermatEventListener);
            listenersAdded.add(reversedOnCryptoNetworkFermatEventListener);

            this.serviceStatus = ServiceStatus.STARTED;
        }catch (Exception e){
            throw new CantStartServiceException(CantStartServiceException.DEFAULT_MESSAGE, FermatException.wrapException(e),null,"Check the cause of this error");
        }
        
    }

    @Override
    public void stop() {

        /**
         * I will remove all the event listeners registered with the event manager.
         */

        for (FermatEventListener fermatEventListener : listenersAdded) {
            eventManager.removeListener(fermatEventListener);
        }

        listenersAdded.clear();
        
        this.serviceStatus = ServiceStatus.STOPPED;
        
    }

    @Override
    public ServiceStatus getStatus() {
        return this.serviceStatus;
    }

}
