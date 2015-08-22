package com.bitdubai.fermat_dmp_plugin.layer.actor.intra_user.developer.bitdubai.version_1.event_handlers;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.Service;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.interfaces.EventMonitor;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.interfaces.PlatformEvent;
import com.bitdubai.fermat_api.layer.dmp_actor.intra_user.exceptions.CantDenyConnectionException;
import com.bitdubai.fermat_api.layer.dmp_actor.intra_user.interfaces.ActorIntraUserManager;
import com.bitdubai.fermat_api.layer.dmp_network_service.intra_user.interfaces.IntraUserManager;
import com.bitdubai.fermat_api.layer.dmp_transaction.TransactionServiceNotStartedException;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.interfaces.EventHandler;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.events.IntraUserActorConnectionDeniedEvent;

/**
 * Created by natalia on 17/08/15.
 */
public class IntraUserDeniedConnectionEventHandlers implements EventHandler {
    /**
     * listener  INTRA_USER_CONNECTION_DENIED event
     * Change Actor status to DENIED
     */
    ActorIntraUserManager actorIntraUserManager;

    IntraUserManager intraUserNetworkServiceManager;

    EventMonitor eventMonitor;

    public void setEventManager(EventMonitor eventMonitor){
        this.eventMonitor = eventMonitor;

    }

    public void setActorIntraUserManager(ActorIntraUserManager actorIntraUserManager){
        this.actorIntraUserManager = actorIntraUserManager;

    }

    public void setIntraUserManager( IntraUserManager intraUserNetworkServiceManager){
        this.intraUserNetworkServiceManager = intraUserNetworkServiceManager;

    }

    @Override
    public void handleEvent(PlatformEvent platformEvent) throws FermatException {
        if (((Service) this.actorIntraUserManager).getStatus() == ServiceStatus.STARTED){

            try
            {
               IntraUserActorConnectionDeniedEvent intraUserActorConnectionDeniedEvent = (IntraUserActorConnectionDeniedEvent) platformEvent;
                this.actorIntraUserManager.denyConnection(intraUserActorConnectionDeniedEvent.getIntraUserLoggedInPublicKey(),
                        intraUserActorConnectionDeniedEvent.getIntraUserToAddPublicKey());

                /**
                 * Confirm Denied on Network services
                 */
                intraUserNetworkServiceManager.confirmNotification(intraUserActorConnectionDeniedEvent.getIntraUserLoggedInPublicKey(), intraUserActorConnectionDeniedEvent.getIntraUserToAddPublicKey());

            }
            catch(CantDenyConnectionException e)
            {
                this.eventMonitor.handleEventException(e,platformEvent);
            }

            catch(Exception e)
            {
                this.eventMonitor.handleEventException(e,platformEvent);
            }
        }
        else
        {
            throw new TransactionServiceNotStartedException();
        }
    }
}
