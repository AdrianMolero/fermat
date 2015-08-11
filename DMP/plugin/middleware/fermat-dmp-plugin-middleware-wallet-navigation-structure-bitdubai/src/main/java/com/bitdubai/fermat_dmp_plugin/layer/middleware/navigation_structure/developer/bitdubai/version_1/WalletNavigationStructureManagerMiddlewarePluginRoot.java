package com.bitdubai.fermat_dmp_plugin.layer.middleware.navigation_structure.developer.bitdubai.version_1;

import com.bitdubai.fermat_api.CantStartPluginException;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.Plugin;
import com.bitdubai.fermat_api.Service;
import com.bitdubai.fermat_api.layer.all_definition.developer.DatabaseManagerForDevelopers;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabase;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTable;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTableRecord;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperObjectFactory;
import com.bitdubai.fermat_api.layer.all_definition.developer.LogManagerForDevelopers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;

import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_manager.interfaces.InstalledWallet;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_navigation_structure.interfaces.WalletNavigationStructureManager;
import com.bitdubai.fermat_api.layer.dmp_network_service.wallet_resources.WalletResourcesInstalationManager;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DealsWithPluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.DealsWithLogger;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogLevel;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogManager;
import com.bitdubai.fermat_dmp_plugin.layer.middleware.navigation_structure.developer.bitdubai.version_1.developerUtils.WalletnavigationStructureMiddlewareDeveloperDatabaseFactory;
import com.bitdubai.fermat_dmp_plugin.layer.middleware.navigation_structure.developer.bitdubai.version_1.exceptions.CantDeliverDatabaseException;
import com.bitdubai.fermat_dmp_plugin.layer.middleware.navigation_structure.developer.bitdubai.version_1.database.WalletNavigationStructureMiddlewareDatabaseConstants;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.DealsWithErrors;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.DealsWithEvents;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.EventManager;
import com.bitdubai.fermat_pip_api.layer.pip_user.device_user.interfaces.DeviceUserManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * That plugin manages the navigation structure of Wallets .
 * Keep track of the resources found in the device and its relationship with the wallets .
 * Serves the WalletFactory for creating and modifying Wallets .
 *
 * Created by Natalia on 07/08/2015
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class WalletNavigationStructureManagerMiddlewarePluginRoot implements DatabaseManagerForDevelopers,DealsWithErrors,DealsWithEvents,DealsWithLogger,DealsWithPluginDatabaseSystem,LogManagerForDevelopers, Plugin, Service, WalletNavigationStructureManager {

    /**
     * DealsWithDeviceUser member variables
     */
    DeviceUserManager deviceUserManager;


    /**
     * WalletManagerMiddlewarePluginRoot member variables
     */
    Database database;

    private List<InstalledWallet> installedWallets = null;


    /**
     * DealsWithErrors Interface member variables.
     */
    ErrorManager errorManager;


    /**
     * DealsWithLogger interface member variable
     */
    LogManager logManager;
    static Map<String, LogLevel> newLoggingLevel = new HashMap<String, LogLevel>();

    /**
     * DealsWithEvents interface member variable
     */
    EventManager eventManager;


    /**
     * DealsWithWalletResources interface member variable
     */
    WalletResourcesInstalationManager walletResources;


    /**
     * DealsWithPluginDatabaseSystem Interface member variables.
     */

    PluginDatabaseSystem pluginDatabaseSystem;

    /**
     * Plugin Interface member variables.
     */
    UUID pluginId;




    /*
     * DatabaseManagerForDevelopers interface methods implementation
     */

    @Override
    public List<DeveloperDatabase> getDatabaseList(DeveloperObjectFactory developerObjectFactory) {

        WalletnavigationStructureMiddlewareDeveloperDatabaseFactory dbFactory = new WalletnavigationStructureMiddlewareDeveloperDatabaseFactory(pluginId.toString());
        return dbFactory.getDatabaseList(developerObjectFactory);
    }

    @Override
    public List<DeveloperDatabaseTable> getDatabaseTableList(DeveloperObjectFactory developerObjectFactory, DeveloperDatabase developerDatabase) {
        return WalletnavigationStructureMiddlewareDeveloperDatabaseFactory.getDatabaseTableList(developerObjectFactory);
    }

    @Override
    public List<DeveloperDatabaseTableRecord> getDatabaseTableContent(DeveloperObjectFactory developerObjectFactory, DeveloperDatabase developerDatabase, DeveloperDatabaseTable developerDatabaseTable) {
        Database database;
        try {
            database = this.pluginDatabaseSystem.openDatabase(pluginId, WalletNavigationStructureMiddlewareDatabaseConstants.WALLET_NAVIGATION_STRUCTURE_WALLETS_DATABASE);
            return WalletnavigationStructureMiddlewareDeveloperDatabaseFactory.getDatabaseTableContent(developerObjectFactory, database, developerDatabaseTable);
        }catch (CantOpenDatabaseException cantOpenDatabaseException){
            /**
             * The database exists but cannot be open. I can not handle this situation.
             */
            FermatException e = new CantDeliverDatabaseException("I can't open database",cantOpenDatabaseException,"WalletId: " + developerDatabase.getName(),"");
            this.errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_WALLET_NAVIGATION_STRUCTURE_MIDDLEWARE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,e);
        }
        catch (DatabaseNotFoundException databaseNotFoundException) {
            FermatException e = new CantDeliverDatabaseException("Database does not exists",databaseNotFoundException,"WalletId: " + developerDatabase.getName(),"");
            this.errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_WALLET_NAVIGATION_STRUCTURE_MIDDLEWARE,UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,e);
        }
        // If we are here the database could not be opened, so we return an empry list
        return new ArrayList<>();
    }

    /**
     * Service Interface member variables.
     */
    ServiceStatus serviceStatus = ServiceStatus.CREATED;


    @Override
    public void start() throws CantStartPluginException {



        this.serviceStatus = ServiceStatus.STARTED;

    }
    @Override
    public void pause(){
        this.serviceStatus = ServiceStatus.PAUSED;
    }

    @Override
    public void resume(){
        this.serviceStatus = ServiceStatus.STARTED;
    }

    @Override
    public void stop(){
        this.serviceStatus = ServiceStatus.STOPPED;
    }

    @Override
    public ServiceStatus getStatus() {
        return this.serviceStatus;
    }


    /**
     * DealWithErrors Interface implementation. 
     */
    @Override
    public void setErrorManager(ErrorManager errorManager) {
        this.errorManager = errorManager;
    }

    /**
     * Plugin methods implementation.
     */
    @Override
    public void setId(UUID pluginId) {
        this.pluginId = pluginId;
    }




    /**
     * DealsWithLogger Interface implementation.
     */

    @Override
    public void setLogManager(LogManager logManager) {
        this.logManager = logManager;
    }

    /**
     * LogManagerForDevelopers Interface implementation.
     */

    @Override
    public List<String> getClassesFullPath() {
        List<String> returnedClasses = new ArrayList<String>();
        returnedClasses.add("com.bitdubai.fermat_dmp_plugin.layer.middleware.navigation_structure.developer.bitdubai.version_1.WalletNavigationStructureManagerMiddlewarePluginRoot");
        returnedClasses.add("com.bitdubai.fermat_dmp_plugin.layer.middleware.navigation_structure.developer.bitdubai.version_1.structure.WalletNavigationStructureMiddleware");

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
            if (WalletNavigationStructureManagerMiddlewarePluginRoot.newLoggingLevel.containsKey(pluginPair.getKey())) {
                WalletNavigationStructureManagerMiddlewarePluginRoot.newLoggingLevel.remove(pluginPair.getKey());
                WalletNavigationStructureManagerMiddlewarePluginRoot.newLoggingLevel.put(pluginPair.getKey(), pluginPair.getValue());
            } else {
                WalletNavigationStructureManagerMiddlewarePluginRoot.newLoggingLevel.put(pluginPair.getKey(), pluginPair.getValue());
            }
        }

    }


    /*
     * DealsWithPluginDatabaseSystem interface methods implementation
     */

    @Override
    public void setPluginDatabaseSystem(PluginDatabaseSystem pluginDatabaseSystem) {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
    }

    /*
     * DealsWithEvents interface methods implementation
     */

    @Override
    public void setEventManager(EventManager eventManager) {
        this.eventManager=eventManager;
    }





}
