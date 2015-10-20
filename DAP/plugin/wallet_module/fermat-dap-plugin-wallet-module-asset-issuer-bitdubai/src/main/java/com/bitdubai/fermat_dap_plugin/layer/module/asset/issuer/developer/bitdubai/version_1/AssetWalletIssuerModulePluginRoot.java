package com.bitdubai.fermat_dap_plugin.layer.module.asset.issuer.developer.bitdubai.version_1;

import com.bitdubai.fermat_api.CantStartPluginException;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.Plugin;
import com.bitdubai.fermat_api.Service;
import com.bitdubai.fermat_api.layer.all_definition.developer.LogManagerForDevelopers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantCreateFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.FileNotFoundException;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.DealsWithLogger;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogLevel;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogManager;
import com.bitdubai.fermat_dap_api.layer.dap_actor.asset_user.interfaces.ActorAssetUser;
import com.bitdubai.fermat_dap_api.layer.dap_module.wallet_asset_issuer.interfaces.AssetIssuerWalletSupAppModuleManager;
import com.bitdubai.fermat_dap_api.layer.dap_module.wallet_asset_issuer.interfaces.DealsWithAssetIssuerWalletSubAppModule;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.asset_distribution.exceptions.CantDistributeDigitalAssetsException;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.asset_issuer_wallet.interfaces.AssetIssuerWallet;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.asset_issuer_wallet.interfaces.AssetIssuerWalletBalance;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.asset_issuer_wallet.interfaces.AssetIssuerWalletList;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.asset_issuer_wallet.interfaces.AssetIssuerWalletManager;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.asset_issuer_wallet.interfaces.DealsWithAssetIssuerWallet;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.common.exceptions.CantGetTransactionsException;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.common.exceptions.CantLoadWalletException;
import com.bitdubai.fermat_dap_plugin.layer.module.asset.issuer.developer.bitdubai.version_1.structure.AssetIssuerWalletModuleManager;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.DealsWithErrors;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.UnexpectedPluginExceptionSeverity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Franklin on 07/09/15.
 */
public class AssetWalletIssuerModulePluginRoot implements Plugin, DealsWithAssetIssuerWallet, Service, DealsWithLogger, LogManagerForDevelopers, DealsWithErrors, AssetIssuerWalletSupAppModuleManager {
    //AssetIssuerWalletModuleManager assetIssuerWalletModuleManager;
    AssetIssuerWalletManager assetIssuerWalletManager;
    //AssetIssuerWalletModuleManager assetIssuerWalletModuleManager;
    AssetIssuerWalletModuleManager assetIssuerWalletModuleManager;

    UUID pluginId;


    /**
     * DealsWithErros interface member variable
     */
    ErrorManager errorManager;

    /**
     * DealsWithLogger interface member variable
     */
    LogManager logManager;

    /**
     * Service Interface member variables.
     */
    ServiceStatus serviceStatus = ServiceStatus.CREATED;

    static Map<String, LogLevel> newLoggingLevel = new HashMap<String, LogLevel>();

    @Override
    public void setId(UUID pluginId) {
        this.pluginId = pluginId;
    }


    /**
     * (non-Javadoc)
     * @see Service#start()
     */
    @Override
    public void start() throws CantStartPluginException {
        try {
            assetIssuerWalletModuleManager = new AssetIssuerWalletModuleManager(assetIssuerWalletManager);
            System.out.println("******* Asset Issuer Wallet Module Init ******");
            this.serviceStatus = ServiceStatus.STARTED;
        }catch (Exception exception) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_WALLET_MODULE, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, exception);
            throw exception;
        }
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
    public void setAssetIssuerManager(AssetIssuerWalletManager assetIssuerWalletManager) {
        this.assetIssuerWalletManager = assetIssuerWalletManager;
    }

    @Override
    public void setLogManager(LogManager logManager) {
        this.logManager = logManager;
    }

    @Override
    public List<String> getClassesFullPath() {
        List<String> returnedClasses = new ArrayList<String>();

        returnedClasses.add("com.bitdubai.fermat_dap_plugin.layer.module.asset.issuer.developer.bitdubai.version_1.AssetWalletIssuerModulePluginRoot");
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
            if (AssetWalletIssuerModulePluginRoot.newLoggingLevel.containsKey(pluginPair.getKey())) {
                AssetWalletIssuerModulePluginRoot.newLoggingLevel.remove(pluginPair.getKey());
                AssetWalletIssuerModulePluginRoot.newLoggingLevel.put(pluginPair.getKey(), pluginPair.getValue());
            } else {
                AssetWalletIssuerModulePluginRoot.newLoggingLevel.put(pluginPair.getKey(), pluginPair.getValue());
            }
        }
    }

    @Override
    public void setErrorManager(ErrorManager errorManager) {
        this.errorManager = errorManager;
    }

    @Override
    public List<AssetIssuerWalletList> getAssetIssuerWalletBalancesAvailable(String publicKey) throws CantLoadWalletException {
        return assetIssuerWalletModuleManager.getAssetIssuerWalletBalancesAvailable(publicKey);
    }

    @Override
    public List<AssetIssuerWalletList> getAssetIssuerWalletBalancesBook(String publicKey) throws CantLoadWalletException {
        return assetIssuerWalletModuleManager.getAssetIssuerWalletBalancesBook(publicKey);
    }

    @Override
    public void distributionAssets(String assetPublicKey, String walletPublicKey, ActorAssetUser actorAssetUser) throws CantDistributeDigitalAssetsException, CantGetTransactionsException, CantCreateFileException, FileNotFoundException, CantLoadWalletException {
        assetIssuerWalletModuleManager.distributionAssets(assetPublicKey, walletPublicKey, actorAssetUser);
    }
}
