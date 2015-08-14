package com.bitdubai.fermat_dmp_plugin.layer.identity.intra_user.developer.bitdubai.version_1;


import com.bitdubai.fermat_api.CantStartPluginException;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.Plugin;
import com.bitdubai.fermat_api.Service;
import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.ECCKeyPair;
import com.bitdubai.fermat_api.layer.all_definition.developer.DatabaseManagerForDevelopers;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabase;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTable;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTableRecord;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperObjectFactory;
import com.bitdubai.fermat_api.layer.all_definition.developer.LogManagerForDevelopers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer.dmp_identity.intra_user.exceptions.CantCreateNewIntraUserException;
import com.bitdubai.fermat_api.layer.dmp_identity.intra_user.exceptions.CantGetUserIntraUserIdentitiesException;
import com.bitdubai.fermat_api.layer.dmp_identity.intra_user.interfaces.IntraUserIdentity;
import com.bitdubai.fermat_api.layer.dmp_identity.intra_user.interfaces.IntraUserIdentityManager;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DealsWithPluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.DealsWithPluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogLevel;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogManager;
import com.bitdubai.fermat_dmp_plugin.layer.identity.intra_user.developer.bitdubai.version_1.database.IntraUserIdentityDao;
import com.bitdubai.fermat_dmp_plugin.layer.identity.intra_user.developer.bitdubai.version_1.database.IntraUserIdentityDatabaseConstants;
import com.bitdubai.fermat_dmp_plugin.layer.identity.intra_user.developer.bitdubai.version_1.database.IntraUserIdentityDeveloperDatabaseFactory;
import com.bitdubai.fermat_dmp_plugin.layer.identity.intra_user.developer.bitdubai.version_1.exceptions.CantDeliverDatabaseException;
import com.bitdubai.fermat_dmp_plugin.layer.identity.intra_user.developer.bitdubai.version_1.exceptions.CantGetIntraUserIdentitiesException;
import com.bitdubai.fermat_dmp_plugin.layer.identity.intra_user.developer.bitdubai.version_1.exceptions.CantInitializeIntraUserIdentityDatabaseException;
import com.bitdubai.fermat_dmp_plugin.layer.identity.intra_user.developer.bitdubai.version_1.structure.IntraUserIdentityIdentity;
import com.bitdubai.fermat_pip_api.layer.pip_identity.developer.exceptions.CantCreateNewDeveloperException;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.DealsWithErrors;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.pip_user.device_user.exceptions.CantGetLoggedInDeviceUserException;
import com.bitdubai.fermat_pip_api.layer.pip_user.device_user.interfaces.DealsWithDeviceUser;
import com.bitdubai.fermat_pip_api.layer.pip_user.device_user.interfaces.DeviceUser;
import com.bitdubai.fermat_pip_api.layer.pip_user.device_user.interfaces.DeviceUserManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * An Intra-User identity is used to "authenticate" in a wallet or some sub-apps.
 * The management is done through this plugin.
 * The User can create, list or set a new profile picture for an identity.
 *
 * The "authentication" is managed in each wallet or sub-app in which is used.
 *
 * Created by loui on 22/02/15.
 * Modified by Leon Acosta - (laion.cj91@gmail.com) on 07/08/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */


public class IntraUserIdentityPluginRoot implements DatabaseManagerForDevelopers, DealsWithDeviceUser, DealsWithErrors, DealsWithPluginDatabaseSystem,DealsWithPluginFileSystem, IntraUserIdentityManager, LogManagerForDevelopers,Service, Plugin {

    private IntraUserIdentityDao intraUserIdentityDao;
    /**
     * DealsWithDeviceUsers Interface member variables.
     */

    DeviceUserManager deviceUserManager;

    /**
     * DealsWithErrors Interface member variables.
     */
    ErrorManager errorManager;

    // DealsWithPluginDatabaseSystem Interface member variables.
    PluginDatabaseSystem pluginDatabaseSystem;

    /**
     * FileSystem Interface member variables.
     */
    PluginFileSystem pluginFileSystem;


    /**
     * DealsWithPluginIdentity Interface member variables.
     */
    UUID pluginId;

    /**
     * Service Interface member variables.
     */
    ServiceStatus serviceStatus = ServiceStatus.CREATED;

    // DealsWithlogManager interface member variable.
    private LogManager logManager = null;

    private static Map<String, LogLevel> newLoggingLevel = new HashMap<String, LogLevel>();


    /**
     * List Intra Users linked to current Device User
     *
     * The Intra-Users are linked to the device with the publicKey (it can be acquired by deviceUserManager.getLoggedInDeviceUser()
     *
     * IntraUserIdentityDao is used to database access (select).
     *
     * @return a list of instances of the class IntraUserIdentityIdentity found in structure package of the plugin
     * @throws CantGetUserIntraUserIdentitiesException
     */
    @Override
    public List<IntraUserIdentity> getIntraUsersFromCurrentDeviceUser() throws CantGetUserIntraUserIdentitiesException {

        try
        {

           DeviceUser loggedUser = deviceUserManager.getLoggedInDeviceUser();
            return   intraUserIdentityDao.getIntraUserFromCurrentDeviceUser(loggedUser);
        }
        catch (CantGetLoggedInDeviceUserException e) {
            throw new CantGetUserIntraUserIdentitiesException("CAN'T GET INTRA USER IDENTITYS",e,"Error get logged user device","");
       }
         catch (CantGetIntraUserIdentitiesException e) {
             throw new CantGetUserIntraUserIdentitiesException("CAN'T GET INTRA USER IDENTITYS",e,"","");
        }
        catch (Exception e) {
            throw new CantGetUserIntraUserIdentitiesException ("CAN'T GET INTRA USER IDENTITYS", FermatException.wrapException(e), "", "");
        }
    }

    /**
     * Create an Identity Intra-User
     *
     * When an User decides to create a new identity, that is achieved throw this method.
     * The new Intra-User is linked to the current device user (it can be acquired by deviceUserManager.getLoggedInDeviceUser()
     * The key-pair is created with a new ECCKeyPair()
     *
     * The user must have an alias and a profileImage for the identification, if not, give an Exception.
     *
     * The privateKey must be saved in a file with "$publicKey" like name.
     * The profileImage must be saved in a file with "$publicKey_profileImage" like name.
     *
     * IntraUserIdentityDao is used to database access (insert).
     *
     * @param alias        the alias that the user choose as intra user identity
     * @param profileImage the profile image to identify this identity
     * @return an instance of the class IntraUserIdentityIdentity found in structure package of the plugin
     * @throws CantCreateNewIntraUserException
     */
    @Override
    public IntraUserIdentity createNewIntraUser(String alias, byte[] profileImage) throws CantCreateNewIntraUserException {
        try{
            DeviceUser loggedUser = deviceUserManager.getLoggedInDeviceUser();

            ECCKeyPair keyPair = new ECCKeyPair();
            String publicKey = keyPair.getPublicKey();
            String privateKey = keyPair.getPrivateKey();

            intraUserIdentityDao.createNewUser(alias,publicKey,privateKey,loggedUser,profileImage);

            return new IntraUserIdentityIdentity(alias,publicKey,privateKey,profileImage);
        }
        catch(CantGetLoggedInDeviceUserException e)
        {
            throw new CantCreateNewIntraUserException("CAN'T CREATE NEW INTRA USER IDENTITY",e,"Error get logged user device","");
        }
        catch(CantCreateNewDeveloperException e)
        {
            throw new CantCreateNewIntraUserException("CAN'T CREATE NEW INTRA USER IDENTITY",e,"Error save user on database","");
       }
        catch (Exception e) {
            throw new CantCreateNewIntraUserException ("CAN'T CREATE NEW INTRA USER IDENTITY", FermatException.wrapException(e), "", "");
         }

    }

    /**
     * Service Interface implementation.
     */
    @Override
    public void start() throws CantStartPluginException{

        /**
         * I created instance of IntraUserIdentityDao
         */


        try {

            this.intraUserIdentityDao = new IntraUserIdentityDao(pluginDatabaseSystem,this.pluginFileSystem, this.pluginId);

            this.intraUserIdentityDao.initializeDatabase();

        } catch (CantInitializeIntraUserIdentityDatabaseException e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INTRA_USER_IDENTITY, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, e);
            throw new CantStartPluginException(e, Plugins.BITDUBAI_INTRA_USER_IDENTITY);
        }



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
        return serviceStatus;
    }

    /**
     * DatabaseManagerForDevelopers Interface implementation.
     */

    @Override
    public List<DeveloperDatabase> getDatabaseList(DeveloperObjectFactory developerObjectFactory) {


        IntraUserIdentityDeveloperDatabaseFactory dbFactory = new IntraUserIdentityDeveloperDatabaseFactory(this.pluginDatabaseSystem,this.pluginId);
        return dbFactory.getDatabaseList(developerObjectFactory);


    }

    @Override
    public List<DeveloperDatabaseTable> getDatabaseTableList(DeveloperObjectFactory developerObjectFactory, DeveloperDatabase developerDatabase) {
        IntraUserIdentityDeveloperDatabaseFactory dbFactory = new IntraUserIdentityDeveloperDatabaseFactory(this.pluginDatabaseSystem,this.pluginId);
        return dbFactory.getDatabaseTableList(developerObjectFactory);
    }

    @Override
    public List<DeveloperDatabaseTableRecord> getDatabaseTableContent(DeveloperObjectFactory developerObjectFactory, DeveloperDatabase developerDatabase, DeveloperDatabaseTable developerDatabaseTable) {
        Database database;
        try {
            IntraUserIdentityDeveloperDatabaseFactory dbFactory = new IntraUserIdentityDeveloperDatabaseFactory(this.pluginDatabaseSystem,this.pluginId);

            database = this.pluginDatabaseSystem.openDatabase(pluginId, IntraUserIdentityDatabaseConstants.INTRA_USER_DATABASE_NAME);
            return dbFactory.getDatabaseTableContent(developerObjectFactory,  developerDatabaseTable);
        }catch (CantOpenDatabaseException cantOpenDatabaseException){
            /**
             * The database exists but cannot be open. I can not handle this situation.
             */
            FermatException e = new CantDeliverDatabaseException("I can't open database",cantOpenDatabaseException,"WalletId: " + developerDatabase.getName(),"");
            this.errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INTRA_USER_IDENTITY, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,e);
        }
        catch (DatabaseNotFoundException databaseNotFoundException) {
            FermatException e = new CantDeliverDatabaseException("Database does not exists",databaseNotFoundException,"WalletId: " + developerDatabase.getName(),"");
            this.errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INTRA_USER_IDENTITY,UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,e);
        }
        // If we are here the database could not be opened, so we return an empry list
        return new ArrayList<>();
    }

    /**
     * DealWithDeviceUser Interface implementation.
     */

    @Override
    public void setDeviceUserManager(DeviceUserManager deviceUserManager) {
        this.deviceUserManager = deviceUserManager;
    }

    /**
     * DealWithErrors Interface implementation.
     */

    @Override
    public void setErrorManager(ErrorManager errorManager) {
        this.errorManager = errorManager;
    }

    /**
     * DealsWithPluginDatabaseSystem interface implementation.
     */
    @Override
    public void setPluginDatabaseSystem(PluginDatabaseSystem pluginDatabaseSystem) {
        this.pluginDatabaseSystem = pluginDatabaseSystem;

    }

    /**
     * DealWithPluginFileSystem Interface implementation.
     */
    @Override
    public void setPluginFileSystem(PluginFileSystem pluginFileSystem) {
        this.pluginFileSystem  = pluginFileSystem;

    }




    /**
     * LogManagerForDevelopers Interface implementation.
     */
    @Override
    public List<String> getClassesFullPath() {
        List<String> returnedClasses = new ArrayList<String>();
        returnedClasses.add("com.bitdubai.fermat_dmp_plugin.layer.identity.intra_user.developer.bitdubai.version_1IntraUserIdentityPluginRoot");
        returnedClasses.add("com.bitdubai.fermat_dmp_plugin.layer.identity.intra_user.developer.bitdubai.version_1.structure.IntraUserIdentityIdentity");
        returnedClasses.add("com.bitdubai.fermat_dmp_plugin.layer.identity.intra_user.developer.bitdubai.version_1.database.IntraUserIdentityDao");
        returnedClasses.add("com.bitdubai.fermat_dmp_plugin.layer.identity.intra_user.developer.bitdubai.version_1.database.IntraUserIdentityDatabaseConstants");
        returnedClasses.add("com.bitdubai.fermat_dmp_plugin.layer.identity.intra_user.developer.bitdubai.version_1.database.IntraUserIdentityDatabaseFactory");


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
            if (IntraUserIdentityPluginRoot.newLoggingLevel.containsKey(pluginPair.getKey())) {
                IntraUserIdentityPluginRoot.newLoggingLevel.remove(pluginPair.getKey());
                IntraUserIdentityPluginRoot.newLoggingLevel.put(pluginPair.getKey(), pluginPair.getValue());
            } else {
                IntraUserIdentityPluginRoot.newLoggingLevel.put(pluginPair.getKey(), pluginPair.getValue());
            }
        }
    }

    /**
     * PlugIn Interface implementation.
     */
    @Override
    public void setId(UUID pluginId) {
     this.pluginId = pluginId;
    }
}
