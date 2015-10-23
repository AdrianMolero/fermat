package com.bitdubai.fermat_ccp_plugin.layer.wallet_module.crypto_wallet.developer.bitdubai.version_1;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.Plugin;
import com.bitdubai.fermat_api.Service;
import com.bitdubai.fermat_api.layer.all_definition.common.abstract_classes.AbstractPlugin;
import com.bitdubai.fermat_api.layer.all_definition.common.utils.AddonReference;
import com.bitdubai.fermat_api.layer.all_definition.common.utils.PluginReference;
import com.bitdubai.fermat_api.layer.all_definition.developer.LogManagerForDevelopers;


import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer.all_definition.enums.WalletModule;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.DealsWithLogger;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogLevel;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogManager;
import com.bitdubai.fermat_ccp_api.layer.identity.intra_wallet_user.interfaces.IntraWalletUserManager;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.bitcoin_wallet.interfaces.BitcoinWalletManager;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.bitcoin_wallet.interfaces.DealsWithBitcoinWallet;
import com.bitdubai.fermat_ccp_api.layer.identity.intra_wallet_user.interfaces.DealsWithCCPIntraWalletUser;
import com.bitdubai.fermat_ccp_api.layer.middleware.wallet_contacts.interfaces.DealsWithWalletContacts;
import com.bitdubai.fermat_ccp_api.layer.middleware.wallet_contacts.interfaces.WalletContactsManager;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_addresses.interfaces.CryptoAddressesManager;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_addresses.interfaces.DealsWithCryptoAddressesNetworkService;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_transmission.interfaces.CryptoTransmissionNetworkServiceManager;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_transmission.interfaces.DealsWithCryptoTransmissionNetworkService;

import com.bitdubai.fermat_ccp_api.layer.request.crypto_payment.interfaces.CryptoPaymentManager;
import com.bitdubai.fermat_ccp_api.layer.request.crypto_payment.interfaces.DealsWithCryptoPayment;
import com.bitdubai.fermat_ccp_api.layer.transaction.outgoing_extra_user.DealsWithOutgoingExtraUser;
import com.bitdubai.fermat_ccp_api.layer.transaction.outgoing_extra_user.OutgoingExtraUserManager;
import com.bitdubai.fermat_ccp_api.layer.transaction.outgoing_intra_actor.interfaces.DealsWithOutgoingIntraActor;
import com.bitdubai.fermat_ccp_api.layer.transaction.outgoing_intra_actor.interfaces.OutgoingIntraActorManager;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.crypto_wallet.exceptions.CantGetCryptoWalletException;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.crypto_wallet.interfaces.CryptoWallet;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.crypto_wallet.interfaces.CryptoWalletManager;
import com.bitdubai.fermat_ccp_plugin.layer.wallet_module.crypto_wallet.developer.bitdubai.version_1.structure.CryptoWalletWalletModuleManager;
import com.bitdubai.fermat_cry_api.layer.crypto_module.crypto_address_book.interfaces.CryptoAddressBookManager;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.DealsWithErrors;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_ccp_api.layer.actor.extra_user.interfaces.DealsWithExtraUsers;
import com.bitdubai.fermat_ccp_api.layer.actor.extra_user.interfaces.ExtraUserManager;
import com.bitdubai.fermat_cry_api.layer.crypto_module.crypto_address_book.interfaces.DealsWithCryptoAddressBook;
import com.bitdubai.fermat_cry_api.layer.crypto_vault.CryptoVaultManager;
import com.bitdubai.fermat_cry_api.layer.crypto_vault.DealsWithCryptoVault;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Created by loui on 27/05/15.a
 */
public class CryptoWalletCryptoModulePluginRoot extends AbstractPlugin implements DealsWithCryptoTransmissionNetworkService,DealsWithCryptoAddressesNetworkService,CryptoWalletManager,DealsWithCCPIntraWalletUser, DealsWithBitcoinWallet, DealsWithCryptoVault, DealsWithLogger, LogManagerForDevelopers, DealsWithErrors, DealsWithExtraUsers,DealsWithOutgoingExtraUser, DealsWithOutgoingIntraActor,DealsWithWalletContacts, DealsWithCryptoAddressBook,DealsWithCryptoPayment, Plugin, Service {

    @Override
    public List<AddonReference> getNeededAddonReferences() {
        return new ArrayList<>();
    }

    @Override
    public List<PluginReference> getNeededPluginReferences() {
        return new ArrayList<>();
    }

    /**
     * Service Interface member variables.
     */
    private ServiceStatus serviceStatus = ServiceStatus.CREATED;

    /**
     * DealsWithBitcoinWallet Interface member variables.
     */
    private BitcoinWalletManager bitcoinWalletManager;

    /**
     * DealsWithCryptoVault Interface member variables.
     */
    private CryptoVaultManager cryptoVaultManager;

    /**
     * DealsWithLogger interface member variable
     */
    private LogManager logManager;
    private static Map<String, LogLevel> newLoggingLevel = new HashMap<String, LogLevel>();

    /**
     * DealsWithErrors Interface member variables.
     */
    private ErrorManager errorManager;

    /**
     * DealsWithExtraUsers Interface member variables.
     */
    private ExtraUserManager extraUserManager;

    /**
     * DealsWithCryptoPayment Interface member variable
     */
    private CryptoPaymentManager cryptoPaymentManager;


    /**
     * DealsWithOutgoingIntraActor Interface member variable
     */

    private OutgoingIntraActorManager outgoingIntraActorManager;

    /**
     * DealsWithIntraUsersActor Interface member variables.
     */

//    private ActorIntraUserManager intraUserManager;


    /**
     * DealsWithCryptoAddressesNetworkService Interface member variables
     */
    private CryptoAddressesManager cryptoAddressesNSManager;

    /**
     * DealsWithCCPIntraWalletUser Interface member variables.
     */

    private IntraWalletUserManager intraWalletUserManager;

    /**
     * DealsWithOutgoingExtraUser Interface member variables.
     */
    private OutgoingExtraUserManager outgoingExtraUserManager;

    /**
     * DealsWithCryptoAddressBook Interface member variables.
     */
    private CryptoAddressBookManager cryptoAddressBookManager;

    /**
     * DealsWithWalletContacts Interface member variables.
     */
    private WalletContactsManager walletContactsManager;
    private CryptoTransmissionNetworkServiceManager cryptoTransmissionNetworkServiceManager;

    /**
     * Service Interface implementation.
     */
    @Override
    public void start() {
        this.serviceStatus = ServiceStatus.STARTED;
    }

    @Override
    public void pause() {
        this.serviceStatus = ServiceStatus.PAUSED;
    }

    @Override
    public void resume() {
        this.serviceStatus = ServiceStatus.STARTED;
    }

    @Override
    public void stop() {
        this.serviceStatus = ServiceStatus.STOPPED;
    }

    @Override
    public ServiceStatus getStatus() {
        return this.serviceStatus;
    }


    @Override
    public CryptoWallet getCryptoWallet() throws CantGetCryptoWalletException {
        try {

            logManager.log(CryptoWalletCryptoModulePluginRoot.getLogLevelByClass(this.getClass().getName()), "CryptoWallet instantiation started...", null, null);

            CryptoWalletWalletModuleManager walletModuleCryptoWallet = new CryptoWalletWalletModuleManager();

            walletModuleCryptoWallet.setBitcoinWalletManager(bitcoinWalletManager);
            walletModuleCryptoWallet.setCryptoVaultManager(cryptoVaultManager);
            walletModuleCryptoWallet.setExtraUserManager(extraUserManager);
            walletModuleCryptoWallet.setErrorManager(errorManager);
            walletModuleCryptoWallet.setOutgoingExtraUserManager(outgoingExtraUserManager);
            walletModuleCryptoWallet.setCryptoAddressBookManager(cryptoAddressBookManager);
            walletModuleCryptoWallet.setWalletContactsManager(walletContactsManager);
            walletModuleCryptoWallet.setIntraUserManager(intraWalletUserManager);
            walletModuleCryptoWallet.setCryptoTransmissionNetworkService(cryptoTransmissionNetworkServiceManager);
//            walletModuleCryptoWallet.setActorIntraUserManager(this.intraUserManager);
            walletModuleCryptoWallet.setCryptoPaymentManager(this.cryptoPaymentManager);
            walletModuleCryptoWallet.setOutgoingIntraActorManager(this.outgoingIntraActorManager);
            walletModuleCryptoWallet.setCryptoAddressesManager(this.cryptoAddressesNSManager);
            walletModuleCryptoWallet.initialize();

            logManager.log(CryptoWalletCryptoModulePluginRoot.getLogLevelByClass(this.getClass().getName()), "CryptoWallet instantiation finished successfully.", null, null);

            return walletModuleCryptoWallet;
        } catch (Exception e) {
            throw new CantGetCryptoWalletException(CantGetCryptoWalletException.DEFAULT_MESSAGE, FermatException.wrapException(e));
        }
    }

    @Override
    public void setLogManager(LogManager logManager) {
        this.logManager = logManager;
    }

    @Override
    public List<String> getClassesFullPath() {
        List<String> returnedClasses = new ArrayList<>();
        returnedClasses.add("CryptoWalletCryptoModulePluginRoot");
        returnedClasses.add("CryptoWalletWalletModuleManager");

        /**
         * I return the values.
         */
        return returnedClasses;
    }

    @Override
    public void setLoggingLevelPerClass(Map<String, LogLevel> newLoggingLevel) {
        /**
         * I will check the current values and update the LogLevel in those which is different
         */

        for (Map.Entry<String, LogLevel> pluginPair : newLoggingLevel.entrySet()) {
            /**
             * if this path already exists in the Root.bewLoggingLevel I'll update the value, else, I will put as new
             */
            if (CryptoWalletCryptoModulePluginRoot.newLoggingLevel.containsKey(pluginPair.getKey())) {
                CryptoWalletCryptoModulePluginRoot.newLoggingLevel.remove(pluginPair.getKey());
                CryptoWalletCryptoModulePluginRoot.newLoggingLevel.put(pluginPair.getKey(), pluginPair.getValue());
            } else {
                CryptoWalletCryptoModulePluginRoot.newLoggingLevel.put(pluginPair.getKey(), pluginPair.getValue());
            }
        }
    }

    /**
     * Static method to get the logging level from any class under root.
     * @param className
     * @return
     */
    public static LogLevel getLogLevelByClass(String className){
        try{
            /**
             * sometimes the classname may be passed dinamically with an $moretext
             * I need to ignore whats after this.
             */
            String[] correctedClass = className.split(Pattern.quote("$"));
            return CryptoWalletCryptoModulePluginRoot.newLoggingLevel.get(correctedClass[0]);
        } catch (Exception e){
            /**
             * If I couldn't get the correct loggin level, then I will set it to minimal.
             */
            return DEFAULT_LOG_LEVEL;
        }
    }


    @Override
    public void setBitcoinWalletManager(BitcoinWalletManager bitcoinWalletManager) {
        this.bitcoinWalletManager = bitcoinWalletManager;
    }

    @Override
    public void setCryptoVaultManager(CryptoVaultManager cryptoVaultManager) {
        this.cryptoVaultManager = cryptoVaultManager;
    }

    @Override
    public void setErrorManager(ErrorManager errorManager) {
        this.errorManager = errorManager;
    }

    @Override
    public void setExtraUserManager(ExtraUserManager extraUserManager) {
        this.extraUserManager = extraUserManager;
    }

    @Override
    public void setOutgoingExtraUserManager(OutgoingExtraUserManager outgoingExtraUserManager) {
        this.outgoingExtraUserManager = outgoingExtraUserManager;
    }

    @Override
    public void setCryptoAddressBookManager(CryptoAddressBookManager cryptoAddressBookManager) {
        this.cryptoAddressBookManager = cryptoAddressBookManager;
    }

    @Override
    public void setWalletContactsManager(WalletContactsManager walletContactsManager) {
        this.walletContactsManager = walletContactsManager;
    }

    @Override
    public void setId(UUID pluginId) {
    }


    @Override
    public void setIntraUserManager(IntraWalletUserManager intraWalletUserManager) {
        this.intraWalletUserManager = intraWalletUserManager;
    }

    @Override
    public void setCryptoTransmissionNetworkService(CryptoTransmissionNetworkServiceManager cryptoTransmissionNetworkServiceManager) {
        this.cryptoTransmissionNetworkServiceManager = cryptoTransmissionNetworkServiceManager;
    }

    @Override
    public void setCryptoPaymentManager(CryptoPaymentManager cryptoPaymentManager) {
        this.cryptoPaymentManager = cryptoPaymentManager;
    }

    @Override
    public void setOutgoingIntraActorManager(OutgoingIntraActorManager outgoingIntraActorManager) {
        this.outgoingIntraActorManager = outgoingIntraActorManager;
    }

    @Override
    public void setCryptoAddressesManager(CryptoAddressesManager cryptoAddressesNetworkServiceManager) {
        this.cryptoAddressesNSManager = cryptoAddressesNetworkServiceManager;
    }

//    @Override
//    public void setActorIntraUserManager(ActorIntraUserManager actorIntraUserManager) {
//        this.intraUserManager = actorIntraUserManager;
//    }
}
