package com.bitdubai.fermat_dmp_plugin.layer.engine.wallet_runtime.developer.bitdubai.version_1.event_handlers;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.Service;
import com.bitdubai.fermat_api.layer.dmp_engine.wallet_runtime.exceptions.CantRecordClosedWalletException;
import com.bitdubai.fermat_api.layer.dmp_engine.wallet_runtime.WalletRuntimeManager;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer.all_definition.event.PlatformEvent;

import com.bitdubai.fermat_api.layer.dmp_engine.wallet_runtime.exceptions.CantRemoveWalletNavigationStructureException;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.EventHandler;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.events.WalletClosedEvent;

import java.util.UUID;

/**
 * Created by loui on 05/02/15.
 */
public class WalletClosedEventHandler implements EventHandler {
    WalletRuntimeManager walletRuntimeManager;

    public void setWalletRuntimeManager(WalletRuntimeManager walletRuntimeManager) {
        this.walletRuntimeManager = walletRuntimeManager;
    }


    @Override
    public void handleEvent(PlatformEvent platformEvent) throws FermatException {
        String publicKey = ((WalletClosedEvent)platformEvent).getPublicKey();


        if (((Service) this.walletRuntimeManager).getStatus() == ServiceStatus.STARTED) {

            try
            {
                this.walletRuntimeManager.removeNavigationStructure(publicKey);
            }
            catch (CantRemoveWalletNavigationStructureException cantRecordClosedWalletException)
            {
                /**
                 * The main module could not handle this exception. Me neither. Will throw it again.
                 */

                throw cantRecordClosedWalletException;

            }

        }
    }
}
