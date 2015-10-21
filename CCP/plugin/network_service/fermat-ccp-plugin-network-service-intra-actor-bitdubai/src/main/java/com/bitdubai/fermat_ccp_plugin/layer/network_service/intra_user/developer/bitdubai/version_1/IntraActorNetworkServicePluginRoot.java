/*
 * @#TemplateNetworkServicePluginRoot.java - 2015
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package com.bitdubai.fermat_ccp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1;

import com.bitdubai.fermat_api.CantStartAgentException;
import com.bitdubai.fermat_api.CantStartPluginException;
import com.bitdubai.fermat_api.Plugin;
import com.bitdubai.fermat_api.Service;
import com.bitdubai.fermat_api.layer.all_definition.components.enums.PlatformComponentType;
import com.bitdubai.fermat_api.layer.all_definition.components.interfaces.DiscoveryQueryParameters;
import com.bitdubai.fermat_api.layer.all_definition.components.interfaces.PlatformComponentProfile;
import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.ECCKeyPair;
import com.bitdubai.fermat_api.layer.all_definition.developer.DatabaseManagerForDevelopers;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabase;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTable;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTableRecord;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperObjectFactory;
import com.bitdubai.fermat_api.layer.all_definition.developer.LogManagerForDevelopers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer.all_definition.events.EventSource;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEventListener;
import com.bitdubai.fermat_api.layer.all_definition.network_service.enums.NetworkServiceType;
import com.bitdubai.fermat_api.layer.all_definition.network_service.interfaces.NetworkService;
import com.bitdubai.fermat_api.layer.all_definition.network_service.interfaces.NetworkServiceConnectionManager;
import com.bitdubai.fermat_ccp_api.layer.actor.Actor;

import com.bitdubai.fermat_ccp_api.layer.identity.intra_wallet_user.exceptions.CantListIntraWalletUsersException;
import com.bitdubai.fermat_ccp_api.layer.network_service.intra_actor.enums.IntraUserNotificationDescriptor;
import com.bitdubai.fermat_ccp_api.layer.network_service.intra_actor.exceptions.ErrorAcceptIntraUserException;
import com.bitdubai.fermat_ccp_api.layer.network_service.intra_actor.exceptions.ErrorAskIntraUserForAcceptanceException;
import com.bitdubai.fermat_ccp_api.layer.network_service.intra_actor.exceptions.ErrorCancellingIntraUserException;
import com.bitdubai.fermat_ccp_api.layer.network_service.intra_actor.exceptions.ErrorConfirmNotificationsIntraUserException;
import com.bitdubai.fermat_ccp_api.layer.network_service.intra_actor.exceptions.ErrorDenyConnectingIntraUserException;
import com.bitdubai.fermat_ccp_api.layer.network_service.intra_actor.exceptions.ErrorDisconnectingIntraUserException;
import com.bitdubai.fermat_ccp_api.layer.network_service.intra_actor.exceptions.ErrorGetNotificationsIntraUserException;
import com.bitdubai.fermat_ccp_api.layer.network_service.intra_actor.exceptions.ErrorInIntraUserSearchException;
import com.bitdubai.fermat_ccp_api.layer.network_service.intra_actor.exceptions.ErrorSearchingSuggestionsException;
import com.bitdubai.fermat_ccp_api.layer.network_service.intra_actor.interfaces.IntraUserManager;
import com.bitdubai.fermat_ccp_api.layer.network_service.intra_actor.interfaces.IntraUserNotification;
import com.bitdubai.fermat_ccp_api.layer.module.intra_user.interfaces.IntraUserInformation;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DealsWithPluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.DealsWithPluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.location_system.Location;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.DealsWithLogger;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogLevel;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogManager;
import com.bitdubai.fermat_ccp_api.layer.network_service.intra_actor.enums.ActorProtocolState;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.communications.CommunicationNetworkServiceConnectionManager;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.communications.CommunicationRegistrationProcessNetworkServiceAgent;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.database.communications.CommunicationNetworkServiceDatabaseConstants;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.database.communications.CommunicationNetworkServiceDatabaseFactory;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.database.communications.CommunicationNetworkServiceDeveloperDatabaseFactory;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.database.communications.IncomingNotificationDao;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.database.communications.OutgoingNotificationDao;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.event_handlers.communication.CompleteComponentConnectionRequestNotificationEventHandler;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.event_handlers.communication.CompleteComponentRegistrationNotificationEventHandler;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.event_handlers.communication.CompleteRequestListComponentRegisteredNotificationEventHandler;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.event_handlers.communication.FailureComponentConnectionRequestNotificationEventHandler;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.event_handlers.communication.NewReceiveMessagesNotificationEventHandler;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.exceptions.CantInitializeTemplateNetworkServiceDatabaseException;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.exceptions.CantReadRecordDataBaseException;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.exceptions.CantUpdateRecordDataBaseException;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.structure.ActorNetworkServiceRecord;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.structure.ActorNetworkServiceRecordedAgent;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.structure.Identity;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.structure.IntraUserNSInformation;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.structure.IntraUserNetworkServiceNotification;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.structure.MessageType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.contents.FermatMessageCommunication;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.contents.FermatMessageCommunicationFactory;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.P2pEventType;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.DealsWithWsCommunicationsCloudClientManager;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.MessagesStatus;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.WsCommunicationsCloudClientManager;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.client.CommunicationsClientConnection;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.contents.FermatMessage;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.enums.FermatMessageContentType;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.enums.FermatMessagesStatus;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.exceptions.CantRegisterComponentException;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.exceptions.CantRequestListException;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.DealsWithErrors;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.enums.EventType;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.events.IntraUserActorConnectionCancelledEvent;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.interfaces.DealsWithEvents;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.interfaces.EventManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;


/**
 * The Class <code>com.bitdubai.fermat_dmp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.TemplateNetworkServicePluginRoot</code> is
 * the responsible to initialize all component to work together, and hold all resources they needed.
 * <p/>
 *
 * Created by Roberto Requena - (rrequena) on 21/07/15.
 *
 * @version 1.0
 */
public class IntraActorNetworkServicePluginRoot implements IntraUserManager, Service, NetworkService, DealsWithWsCommunicationsCloudClientManager, DealsWithPluginFileSystem,DealsWithPluginDatabaseSystem, DealsWithEvents, DealsWithErrors, DealsWithLogger, LogManagerForDevelopers, Plugin, DatabaseManagerForDevelopers {

    /******************************************************************
     * IMPORTANT: CHANGE THE EVENT_SOURCE TO THE NEW PLUGIN TO IMPLEMENT
     ******************************************************************/
    /**
     * Represent the EVENT_SOURCE
     */
    public final static EventSource EVENT_SOURCE = EventSource.NETWORK_SERVICE_INTRA_ACTOR;

    /**
     * Represent the logManager
     */
    private LogManager logManager;

    /**
     * Represent the newLoggingLevel
     */
    static Map<String, LogLevel> newLoggingLevel = new HashMap<>();

    /**
     * Represent the platformComponentProfile
     */
    private PlatformComponentProfile platformComponentProfile;

    /**
     * Represent the platformComponentType
     */
    private PlatformComponentType platformComponentType;

    /**
     * Represent the networkServiceType
     */
    private NetworkServiceType networkServiceType;

    /**
     * Represent the name
     */
    private String name;

    /**
     * Represent the alias
     */
    private String alias;

    /**
     * Represent the extraData
     */
    private String extraData;

    /**
     * Represent the wsCommunicationsCloudClientManager
     */
    private WsCommunicationsCloudClientManager wsCommunicationsCloudClientManager;

    /**
     * Represent the status of the network service
     */
    private ServiceStatus serviceStatus;

    /**
     * DealWithEvents Interface member variables.
     */
    private EventManager eventManager;

    /**
     * DealsWithErrors Interface member variables.
     */
    private ErrorManager errorManager;

    /**
     * DealsWithPluginDatabaseSystem Interface member variable
     */
    private PluginDatabaseSystem pluginDatabaseSystem;


    /**
     * DealsWithPluginFileSystem Interface member variable
     */
    private PluginFileSystem pluginFileSystem;


    /**
     * DealsWithPluginIdentity Interface member variables.
     */
    private UUID pluginId;

    /**
     * Hold the listeners references
     */
    private List<FermatEventListener> listenersAdded;

    /**
     * Represent the communicationNetworkServiceConnectionManager
     */
    private CommunicationNetworkServiceConnectionManager communicationNetworkServiceConnectionManager;

    /**
     * Represent the dataBase
     */
    private Database dataBase;

    /**
     * Represent the identity
     */
    private ECCKeyPair identity;

    /**
     * Represent the register
     */
    private boolean register;

    /**
     * Represent the communicationRegistrationProcessNetworkServiceAgent
     */
    private CommunicationRegistrationProcessNetworkServiceAgent communicationRegistrationProcessNetworkServiceAgent;

    /**
     *  Represent the remoteNetworkServicesRegisteredList
     */
    private CopyOnWriteArrayList<PlatformComponentProfile> remoteNetworkServicesRegisteredList;

    /**
     *   Represent the communicationNetworkServiceDeveloperDatabaseFactory
     */
    private CommunicationNetworkServiceDeveloperDatabaseFactory communicationNetworkServiceDeveloperDatabaseFactory;

    /**
     * Agent
     */
    private ActorNetworkServiceRecordedAgent actorNetworkServiceRecordedAgent;

    /**
     *  cacha identities to register
     */
    private List<PlatformComponentProfile> actorsToRegisterCache;

    /**
     * Connections arrived
     */
    private AtomicBoolean connectionArrived;

    /**
     * DAO
     */
    private IncomingNotificationDao incomingNotificationsDao;
    private OutgoingNotificationDao outgoingNotificationDao;

    /**
     * Constructor
     */
    public IntraActorNetworkServicePluginRoot() {
        super();
        this.listenersAdded = new ArrayList<>();
        this.platformComponentType = PlatformComponentType.NETWORK_SERVICE;
        this.networkServiceType    = NetworkServiceType.INTRA_USER;
        this.name                  = "Intra actor Network Service";
        this.alias                 = "IntraActorNetworkService";
        this.extraData             = null;
    }


    /**
     * This method validate is all required resource are injected into
     * the plugin root by the platform
     *
     * @throws CantStartPluginException
     */
    private void validateInjectedResources() throws CantStartPluginException {

         /*
         * If all resources are inject
         */
        if (wsCommunicationsCloudClientManager == null ||
                pluginDatabaseSystem  == null ||
                    errorManager      == null ||
                        eventManager  == null) {


            StringBuffer contextBuffer = new StringBuffer();
            contextBuffer.append("Plugin ID: " + pluginId);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("wsCommunicationsCloudClientManager: " + wsCommunicationsCloudClientManager);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("pluginDatabaseSystem: " + pluginDatabaseSystem);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("errorManager: " + errorManager);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("eventManager: " + eventManager);

            String context = contextBuffer.toString();
            String possibleCause = "No all required resource are injected";
            CantStartPluginException pluginStartException = new CantStartPluginException(CantStartPluginException.DEFAULT_MESSAGE, null, context, possibleCause);

            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_TEMPLATE_NETWORK_SERVICE,UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, pluginStartException);
            throw pluginStartException;



        }

    }

    /**
     * This method initialize the communicationNetworkServiceConnectionManager.
     * IMPORTANT: Call this method only in the CommunicationRegistrationProcessNetworkServiceAgent, when execute the registration process
     * because at this moment, is create the platformComponentProfile for this component
     */
    public void initializeCommunicationNetworkServiceConnectionManager(){
        this.communicationNetworkServiceConnectionManager = new CommunicationNetworkServiceConnectionManager(platformComponentProfile, identity, wsCommunicationsCloudClientManager.getCommunicationsCloudClientConnection(), dataBase, errorManager, eventManager);
    }

    /**
     * Initialize the event listener and configure
     */
    private void initializeListener(){

         /*
         * Listen and handle Complete Component Registration Notification Event
         */
        FermatEventListener fermatEventListener = eventManager.getNewListener(P2pEventType.COMPLETE_COMPONENT_REGISTRATION_NOTIFICATION);
        fermatEventListener.setEventHandler(new CompleteComponentRegistrationNotificationEventHandler(this));
        eventManager.addListener(fermatEventListener);
        listenersAdded.add(fermatEventListener);

         /*
         * Listen and handle Complete Request List Component Registered Notification Event
         */
        fermatEventListener = eventManager.getNewListener(P2pEventType.COMPLETE_REQUEST_LIST_COMPONENT_REGISTERED_NOTIFICATION);
        fermatEventListener.setEventHandler(new CompleteRequestListComponentRegisteredNotificationEventHandler(this));
        eventManager.addListener(fermatEventListener);
        listenersAdded.add(fermatEventListener);

        /*
         * Listen and handle Complete Request List Component Registered Notification Event
         */
        fermatEventListener = eventManager.getNewListener(P2pEventType.COMPLETE_COMPONENT_CONNECTION_REQUEST_NOTIFICATION);
        fermatEventListener.setEventHandler(new CompleteComponentConnectionRequestNotificationEventHandler(this));
        eventManager.addListener(fermatEventListener);
        listenersAdded.add(fermatEventListener);

        /**
         *  failure connection
         */

        fermatEventListener = eventManager.getNewListener(P2pEventType.FAILURE_COMPONENT_CONNECTION_REQUEST_NOTIFICATION);
        fermatEventListener.setEventHandler(new FailureComponentConnectionRequestNotificationEventHandler(this));
        eventManager.addListener(fermatEventListener);
        listenersAdded.add(fermatEventListener);
    }


    /**
     * Messages listeners
     */
    private void initializeMessagesListeners(){
        /*
         * Listen and handle Complete Request List Component Registered Notification Event
         */

//        FermatEventListener fermatEventListener = eventManager.getNewListener(P2pEventType.NEW_NETWORK_SERVICE_MESSAGE_SENT_NOTIFICATION);
//        fermatEventListener.setEventHandler(new New(cryptoTransmissionAgent));
//        eventManager.addListener(fermatEventListener);
//        listenersAdded.add(fermatEventListener);

        /**
         *
         */
        FermatEventListener fermatEventListener = eventManager.getNewListener(P2pEventType.NEW_NETWORK_SERVICE_MESSAGE_RECEIVE_NOTIFICATION);
        fermatEventListener.setEventHandler(new NewReceiveMessagesNotificationEventHandler(actorNetworkServiceRecordedAgent));
        eventManager.addListener(fermatEventListener);
        listenersAdded.add(fermatEventListener);
    }



    /**
     * (non-Javadoc)
     * @see Service#start()
     */
    @Override
    public void start() throws CantStartPluginException {

        logManager.log(IntraActorNetworkServicePluginRoot.getLogLevelByClass(this.getClass().getName()), "TemplateNetworkServicePluginRoot - Starting", "TemplateNetworkServicePluginRoot - Starting", "TemplateNetworkServicePluginRoot - Starting");

        /*
         * Validate required resources
         */
        validateInjectedResources();

        try {

            /*
             * Create a new key pair for this execution
             */
            identity = new ECCKeyPair();

            /*
             * Initialize the data base
             */
            initializeDb();

            /*
             * Initialize Developer Database Factory
             */
            communicationNetworkServiceDeveloperDatabaseFactory = new CommunicationNetworkServiceDeveloperDatabaseFactory(pluginDatabaseSystem, pluginId);
            communicationNetworkServiceDeveloperDatabaseFactory.initializeDatabase();

            /*
             * Initialize listeners
             */
            initializeListener();


            /*
             * Verify if the communication cloud client is active
             */
            if (!wsCommunicationsCloudClientManager.isDisable()){

                /*
                 * Initialize the agent and start
                 */
                communicationRegistrationProcessNetworkServiceAgent = new CommunicationRegistrationProcessNetworkServiceAgent(this, wsCommunicationsCloudClientManager.getCommunicationsCloudClientConnection());
                communicationRegistrationProcessNetworkServiceAgent.start();
            }

            //DAO
            incomingNotificationsDao = new IncomingNotificationDao(dataBase);

            outgoingNotificationDao = new OutgoingNotificationDao(dataBase);


            actorsToRegisterCache = new ArrayList<>();

            remoteNetworkServicesRegisteredList = new CopyOnWriteArrayList<PlatformComponentProfile>();

            connectionArrived = new AtomicBoolean(false);

            /*
             * Its all ok, set the new status
            */
            this.serviceStatus = ServiceStatus.STARTED;


       } catch (CantInitializeTemplateNetworkServiceDatabaseException exception) {

            StringBuffer contextBuffer = new StringBuffer();
            contextBuffer.append("Plugin ID: " + pluginId);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("Database Name: " + CommunicationNetworkServiceDatabaseConstants.DATA_BASE_NAME);

            String context = contextBuffer.toString();
            String possibleCause = "The Template Database triggered an unexpected problem that wasn't able to solve by itself";
            CantStartPluginException pluginStartException = new CantStartPluginException(CantStartPluginException.DEFAULT_MESSAGE, exception, context, possibleCause);

            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_TEMPLATE_NETWORK_SERVICE,UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, pluginStartException);
            throw pluginStartException;
        }

    }

    /**
     * (non-Javadoc)
     * @see Service#pause()
     */
    @Override
    public void pause() {

        /*
         * Pause
         */
        communicationNetworkServiceConnectionManager.pause();

        /*
         * Set the new status
         */
        this.serviceStatus = ServiceStatus.PAUSED;

    }

    /**
     * (non-Javadoc)
     * @see Service#resume()
     */
    @Override
    public void resume() {

        /*
         * resume the managers
         */
        communicationNetworkServiceConnectionManager.resume();

        /*
         * Set the new status
         */
        this.serviceStatus = ServiceStatus.STARTED;

    }

    /**
     * (non-Javadoc)
     * @see Service#stop()
     */
    @Override
    public void stop() {

        //Clear all references of the event listeners registered with the event manager.
        listenersAdded.clear();

        /*
         * Stop all connection on the managers
         */
        communicationNetworkServiceConnectionManager.closeAllConnection();

        //set to not register
        register = Boolean.FALSE;

        /*
         * Set the new status
         */
        this.serviceStatus = ServiceStatus.STOPPED;

    }

    /**
     * (non-Javadoc)
     * @see Service#getStatus()
     */
    @Override
    public ServiceStatus getStatus() {
        return serviceStatus;
    }

    /**
     * (non-Javadoc)
     * @see DealsWithWsCommunicationsCloudClientManager#setWsCommunicationsCloudClientConnectionManager(WsCommunicationsCloudClientManager)
     */
    @Override
    public void setWsCommunicationsCloudClientConnectionManager(WsCommunicationsCloudClientManager wsCommunicationsCloudClientManager) {
        this.wsCommunicationsCloudClientManager = wsCommunicationsCloudClientManager;
    }

    /**
     * (non-Javadoc)
     * @see DealsWithErrors#setErrorManager(ErrorManager)
     */
    @Override
    public void setErrorManager(ErrorManager errorManager) {
        this.errorManager = errorManager;
    }

    /**
     * (non-Javadoc)
     * @see DealsWithEvents#setEventManager(EventManager) )
     */
    @Override
    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    /* (non-Javadoc)
    * @see DealsWithPluginDatabaseSystem#setPluginDatabaseSystem()
    */
    @Override
    public void setPluginDatabaseSystem(PluginDatabaseSystem pluginDatabaseSystem) {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
    }


    @Override
    public void setPluginFileSystem(PluginFileSystem pluginFileSystem) {
        this.pluginFileSystem = pluginFileSystem;
    }


    /**
     * (non-Javadoc)
     * @see NetworkService#getId()
     */
    @Override
    public UUID getId() {
        return this.pluginId;
    }

    /**
     * (non-Javadoc)
     * @see Plugin#setId(UUID)
     */
    @Override
    public void setId(UUID pluginId) {
        this.pluginId = pluginId;
    }


    private void initializeIntraActorAgent(){

        try {
            actorNetworkServiceRecordedAgent = new ActorNetworkServiceRecordedAgent(
                    communicationNetworkServiceConnectionManager,
                    this,
                    errorManager,
                    eventManager,
                    pluginFileSystem,
                    wsCommunicationsCloudClientManager);



            actorNetworkServiceRecordedAgent.start();

        } catch (CantStartAgentException e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INTRAUSER_NETWORK_SERVICE,UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN,e);
        }
    }

    /**
     * (non-Javadoc)
     * @see NetworkService#handleCompleteComponentRegistrationNotificationEvent(PlatformComponentProfile)
     */
    public void handleCompleteComponentRegistrationNotificationEvent(PlatformComponentProfile platformComponentProfileRegistered){

        System.out.println(" CommunicationNetworkServiceConnectionManager - Starting method handleCompleteComponentRegistrationNotificationEvent");

        if(platformComponentProfileRegistered.getPlatformComponentType()  == PlatformComponentType.ACTOR_INTRA_USER){
            System.out.print("-----------------------\n" +
                    "ACTOR REGISTRADO!! -----------------------\n" +
                    "-----------------------\n A: " +platformComponentProfileRegistered.getAlias());
        }


        /*
         * If the component registered have my profile and my identity public key
         */
        if (platformComponentProfileRegistered.getPlatformComponentType()  == PlatformComponentType.NETWORK_SERVICE &&
                platformComponentProfileRegistered.getNetworkServiceType()  == NetworkServiceType.INTRA_USER &&
                   platformComponentProfileRegistered.getIdentityPublicKey().equals(identity.getPublicKey())){

            /*
             * Mark as register
             */
             this.register = Boolean.TRUE;

            initializeIntraActorAgent();

            initializeMessagesListeners();


            try {

                /**
                 * Register identities
                 */

                CommunicationsClientConnection communicationsClientConnection = wsCommunicationsCloudClientManager.getCommunicationsCloudClientConnection();



                for(PlatformComponentProfile platformComponentProfile : actorsToRegisterCache){

                    communicationsClientConnection.registerComponentForCommunication(platformComponentProfile);

                    System.out.print("-----------------------\n" +
                            "INTENTANDO REGISTRAR ACTOR  -----------------------\n" +
                            "-----------------------\n A: " + platformComponentProfile.getAlias());


                }


                /**
                 * Actors cache
                 */


                 /*-------------------------------------------------------------------------------------------------
             * This is for test and example of how to use
             * Construct the filter
             */
                DiscoveryQueryParameters discoveryQueryParameters = constructDiscoveryQueryParamsFactory(PlatformComponentType.ACTOR_INTRA_USER, //PlatformComponentType you want to find
                        NetworkServiceType.UNDEFINED,     //NetworkServiceType you want to find
                        null,                     // alias
                        null,                     // identityPublicKey
                        null,                     // location
                        null,                     // distance
                        null,                     // name
                        null,                     // extraData
                        null,                     // offset
                        null,                     // max
                        null,                     // fromOtherPlatformComponentType, when use this filter apply the identityPublicKey
                        null);                    // fromOtherNetworkServiceType,    when use this filter apply the identityPublicKey



                requestRemoteNetworkServicesRegisteredList(discoveryQueryParameters);




            } catch (CantRegisterComponentException e) {
                e.printStackTrace();
            }


        }

    }

    @Override
    public void handleFailureComponentRegistrationNotificationEvent(PlatformComponentProfile networkServiceApplicant, PlatformComponentProfile remoteParticipant) {
        System.out.println("----------------------------------\n" +
                "FAILED CONNECTION WITH "+remoteParticipant.getAlias()+"\n" +
                "--------------------------------------------------------");
        actorNetworkServiceRecordedAgent.connectionFailure(remoteParticipant.getIdentityPublicKey());

    }

    /**
     * (non-Javadoc)
     * @see NetworkService#
     */
    public void handleCompleteRequestListComponentRegisteredNotificationEvent(List<PlatformComponentProfile> platformComponentProfileRegisteredList){

        System.out.println(" CommunicationNetworkServiceConnectionManager - Starting method handleCompleteRequestListComponentRegisteredNotificationEvent");

        /*
         * save into the cache
         */

        remoteNetworkServicesRegisteredList.addAllAbsent(platformComponentProfileRegisteredList);


        System.out.println("--------------------------------------\n" +
                "REGISTRO DE USUARIOS INTRA USER CONECTADOS");
        for (PlatformComponentProfile platformComponentProfile : platformComponentProfileRegisteredList){
            System.out.println(platformComponentProfile.getAlias() + "\n");
        }
        System.out.println("--------------------------------------\n" +
                "FIN DE REGISTRO DE USUARIOS INTRA USER CONECTADOS");



        //save register actors in the cache
        //TODO: Ver si me conviene guardar los actores en una base de datos nueva de cache





//        if(!remoteNetworkServicesRegisteredList.isEmpty()){
//            connectionArrived.set(true);
//        }

        /* -----------------------------------------------------------------------
         * This is for test and example of how to use
         */
        if(getRemoteNetworkServicesRegisteredList() != null && !getRemoteNetworkServicesRegisteredList().isEmpty()){


        }

    }
    private void saveActorsInMemoryCache(List<PlatformComponentProfile> platformComponentProfileRegisteredList){

//        for (PlatformComponentProfile platformComponentProfile : platformComponentProfileRegisteredList){
//            incomingNotificationsDao.createNotification(UUID.randomUUID(),platformComponentProfile.);
//        }

    }


    /**
     * (non-Javadoc)
     * @see NetworkService#handleCompleteComponentConnectionRequestNotificationEvent(PlatformComponentProfile, PlatformComponentProfile)
     */
    public void handleCompleteComponentConnectionRequestNotificationEvent(PlatformComponentProfile applicantComponentProfile, PlatformComponentProfile remoteComponentProfile){

        System.out.println(" TemplateNetworkServiceRoot - Starting method handleCompleteComponentConnectionRequestNotificationEvent");

        /*
         * Tell the manager to handler the new connection stablished
         */

        System.out.println("LEONNNN MIRAME QUE CAPO ;) \n publickey" + remoteComponentProfile.getIdentityPublicKey() + "\n -------------------- nombre: " + remoteComponentProfile.getAlias());

        communicationNetworkServiceConnectionManager.handleEstablishedRequestedNetworkServiceConnection(remoteComponentProfile);


        //NetworkServiceLocal communicationNetworkServiceLocal = communicationNetworkServiceConnectionManager.getNetworkServiceLocalInstance(remoteComponentProfile.getIdentityPublicKey());

        //communicationNetworkServiceLocal.sendMessage(platformComponentProfile.getIdentityPublicKey(), remoteComponentProfile.getIdentityPublicKey(), "Leon la puta que te pario");

        if (remoteNetworkServicesRegisteredList != null && !remoteNetworkServicesRegisteredList.isEmpty()){

            /* -------------------------------------------------------------------------------------------------
             * This is for test and example of how to use
             * Get the local representation of the remote network service
             */
            //CommunicationNetworkServiceLocal communicationNetworkServiceLocal = communicationNetworkServiceConnectionManager.getNetworkServiceLocalInstance(remoteComponentProfile.getIdentityPublicKey());

            /*
             * Get a remote network service registered from the list requested
             */
            //PlatformComponentProfile remoteNetworkServiceToConnect = remoteNetworkServicesRegisteredList.get(0);

            /**
             * Create the message content
             * RECOMMENDATION: the content have to be a json string
             */
//            String messageContent = "*********************************************************************************\n " +
//                                    "* HELLO TEAM...  This message was sent from the device of ROBERTO REQUENA... :) *\n" +
//                                    "*********************************************************************************";
//
//            /*
//             * Send a message using the local representation
//             */
//            communicationNetworkServiceLocal.sendMessage(identity.getPublicKey(),null, messageContent);

        }

    }

    /**
     * Get the IdentityPublicKey
     *
     * @return String
     */
    public String getIdentityPublicKey(){
        return this.identity.getPublicKey();
    }

    /**
     * (non-Javadoc)
     * @see NetworkService#getPlatformComponentProfile()
     */
    public PlatformComponentProfile getPlatformComponentProfile() {
        return platformComponentProfile;
    }

    /**
     * (non-Javadoc)
     * @see NetworkService#getPlatformComponentType()
     */
    @Override
    public PlatformComponentType getPlatformComponentType() {
        return platformComponentType;
    }

    /**
     * (non-Javadoc)
     * @see NetworkService#getNetworkServiceType()
     */
    @Override
    public NetworkServiceType getNetworkServiceType() {
        return networkServiceType;
    }

    /**
     * Set the PlatformComponentProfile
     *
     * @param platformComponentProfile
     */
    public void setPlatformComponentProfile(PlatformComponentProfile platformComponentProfile) {
        this.platformComponentProfile = platformComponentProfile;
    }

    /**
     * Get is Register
     * @return boolean
     */
    public boolean isRegister() {
        return register;
    }


    /**
     * (non-javadoc)
     * @see NetworkService#getNetworkServiceConnectionManager()
     */
    public NetworkServiceConnectionManager getNetworkServiceConnectionManager() {
        return communicationNetworkServiceConnectionManager;
    }

    /**
     * (non-javadoc)
     * @see NetworkService#constructDiscoveryQueryParamsFactory(PlatformComponentType, NetworkServiceType, String, String, Location, Double, String, String, Integer, Integer, PlatformComponentType, NetworkServiceType)
     */
    @Override
    public DiscoveryQueryParameters constructDiscoveryQueryParamsFactory(PlatformComponentType platformComponentType, NetworkServiceType networkServiceType, String alias, String identityPublicKey, Location location, Double distance, String name, String extraData, Integer firstRecord, Integer numRegister, PlatformComponentType fromOtherPlatformComponentType, NetworkServiceType fromOtherNetworkServiceType) {
        return wsCommunicationsCloudClientManager.getCommunicationsCloudClientConnection().constructDiscoveryQueryParamsFactory(platformComponentType, networkServiceType, alias, identityPublicKey, location, distance, name, extraData, firstRecord, numRegister, fromOtherPlatformComponentType, fromOtherNetworkServiceType);
    }


    /**
     * (non-javadoc)
     * @see NetworkService#getRemoteNetworkServicesRegisteredList()
     */
    public List<PlatformComponentProfile> getRemoteNetworkServicesRegisteredList() {
        return remoteNetworkServicesRegisteredList;
    }

    /**
     * (non-javadoc)
     * @see NetworkService#requestRemoteNetworkServicesRegisteredList(DiscoveryQueryParameters)
     */
    public void requestRemoteNetworkServicesRegisteredList(DiscoveryQueryParameters discoveryQueryParameters){

        System.out.println(" TemplateNetworkServiceRoot - requestRemoteNetworkServicesRegisteredList");

         /*
         * Request the list of component registers
         */
        try {

            wsCommunicationsCloudClientManager.getCommunicationsCloudClientConnection().requestListComponentRegistered(platformComponentProfile, discoveryQueryParameters);

        } catch (CantRequestListException e) {

            StringBuffer contextBuffer = new StringBuffer();
            contextBuffer.append("Plugin ID: " + pluginId);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("wsCommunicationsCloudClientManager: " + wsCommunicationsCloudClientManager);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("pluginDatabaseSystem: " + pluginDatabaseSystem);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("errorManager: " + errorManager);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("eventManager: " + eventManager);

            String context = contextBuffer.toString();
            String possibleCause = "Plugin was not registered";

            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INTRAUSER_NETWORK_SERVICE, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, e);

        }

    }

    /**
     * Get the Name
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Get the Alias
     * @return String
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Get the ExtraData
     * @return String
     */
    public String getExtraData() {
        return extraData;
    }

    /**
     * Get the New Received Message List
     *
     * @return List<FermatMessage>
     */
    public List<FermatMessage> getNewReceivedMessageList() throws CantReadRecordDataBaseException {

        Map<String, Object> filters = new HashMap<>();
        filters.put(CommunicationNetworkServiceDatabaseConstants.INCOMING_MESSAGES_FIRST_KEY_COLUMN, MessagesStatus.NEW_RECEIVED.getCode());

        return communicationNetworkServiceConnectionManager.getIncomingMessageDao().findAll(filters);
    }

    /**
     * Mark the message as read
     * @param fermatMessage
     */
    public void markAsRead(FermatMessage fermatMessage) throws CantUpdateRecordDataBaseException {

        ((FermatMessageCommunication)fermatMessage).setFermatMessagesStatus(FermatMessagesStatus.READ);
        communicationNetworkServiceConnectionManager.getIncomingMessageDao().update(fermatMessage);
    }

    //TODO harcodeado
    /*
     * IntraUserManager Interface method implementation
     */

    @Override
    public List<IntraUserInformation> searchIntraUserByName(String intraUserAlias) throws ErrorInIntraUserSearchException {

        List<IntraUserInformation> intraUserList = new ArrayList<IntraUserInformation>();


        return intraUserList;
    }

    @Override
    public List<IntraUserInformation> getIntraUsersSuggestions(int max, int offset) throws ErrorSearchingSuggestionsException {

        List<IntraUserInformation> lstIntraUser = new ArrayList<>();

        try {
                if(remoteNetworkServicesRegisteredList!=null) {
                    byte[] image = null;
                        for (PlatformComponentProfile platformComponentProfile : remoteNetworkServicesRegisteredList) {
                            if (platformComponentProfile.getExtraData() != null) {
                                JsonObject jsonObject = new JsonObject();
                                jsonObject =new JsonParser().parse(platformComponentProfile.getExtraData()).getAsJsonObject();
                                image = jsonObject.get(com.bitdubai.fermat_ccp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.structure.JsonObjectConstants.PROFILE_IMAGE).getAsString().getBytes();
                            }


                            IntraUserInformation intraUser = new IntraUserNSInformation(platformComponentProfile.getAlias(), platformComponentProfile.getIdentityPublicKey(), image);

                            lstIntraUser.add(intraUser);
                        }
                }

            /* This is for test and example of how to use
                    * Construct the filter
            */
            DiscoveryQueryParameters discoveryQueryParameters = constructDiscoveryQueryParamsFactory(PlatformComponentType.ACTOR_INTRA_USER, //PlatformComponentType you want to find
                    NetworkServiceType.UNDEFINED,     //NetworkServiceType you want to find
                    null,                     // alias
                    null,                     // identityPublicKey
                    null,                     // location
                    null,                     // distance
                    null,                     // name
                    null,                     // extraData
                    null,                     // offset
                    null,                     // max
                    null,                     // fromOtherPlatformComponentType, when use this filter apply the identityPublicKey
                    null);                    // fromOtherNetworkServiceType,    when use this filter apply the identityPublicKey



            requestRemoteNetworkServicesRegisteredList(discoveryQueryParameters);

        }catch (Exception e){
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INTRAUSER_NETWORK_SERVICE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);

        }


        return lstIntraUser;
    }

    @Override
    public void askIntraUserForAcceptance(String intraUserLoggedInPublicKey,String intraUserLoggedName,Actors senderType, String intraUserToAddName, String intraUserToAddPublicKey,Actors destinationType, byte[] myProfileImage) throws ErrorAskIntraUserForAcceptanceException {

        try {
        //TODO: guardar mensaje en base de datos

            /**
             * Guardo en mensaje en la base de datos
             */

            ActorNetworkServiceRecord actorNetworkServiceRecord = outgoingNotificationDao.createNotification(
                    UUID.randomUUID(),
                    intraUserLoggedInPublicKey,
                    senderType,
                    intraUserToAddPublicKey,
                    intraUserLoggedName,
                    myProfileImage,
                    destinationType,
                    IntraUserNotificationDescriptor.ASKFORACCEPTANCE,
                    System.currentTimeMillis(),
                    ActorProtocolState.PROCESSING_SEND,
                    false);



//            JsonObject obj=new JsonObject();
//            obj.addProperty(JsonObjectConstants.SENDER_PUBLIC_HEY, intraUserLoggedInPublicKey);
//            obj.addProperty(JsonObjectConstants.SENDER_NAME,intraUserLoggedName);
//            obj.addProperty(JsonObjectConstants.DESTINATION_NAME, intraUserToAddName);
//            obj.addProperty(JsonObjectConstants.DESTINATION_PUBLIC_KEY, intraUserToAddPublicKey);
//            obj.addProperty(JsonObjectConstants.PROFILE_IMAGE, String.valueOf(myProfileImage));
//            obj.addProperty(JsonObjectConstants.MESSAGE_TYPE, IntraUserNotificationDescriptor.ASKFORACCEPTANCE.getCode());

//            Gson gson = new Gson();
//
//
//
//
//            FermatMessage fermatMessage  = FermatMessageCommunicationFactory.constructFermatMessage(intraUserLoggedInPublicKey,  //Sender NetworkService
//                    intraUserToAddPublicKey,   //Receiver
//                    gson.toJson(actorNetworkServiceRecord).toString(),                //Message Content
//                    FermatMessageContentType.TEXT);//Type
//
//            /*
//             * Configure the correct status
//             */
//            ((FermatMessageCommunication)fermatMessage).setFermatMessagesStatus(FermatMessagesStatus.PENDING_TO_SEND);
//
//            /*
//             * Save to the data base table
//             */
//            communicationNetworkServiceConnectionManager.getOutgoingMessageDao().create(fermatMessage);



            //connectToBetweenActors(intraUserLoggedInPublicKey, PlatformComponentType.ACTOR_INTRA_USER, intraUserToAddPublicKey, PlatformComponentType.ACTOR_INTRA_USER);


           /* communicationNetworkServiceConnectionManager.getIncomingMessageDao().create(new FermatMessageCommunication(String content,
                    Timestamp deliveryTimestamp,
                    FermatMessageContentType.TEXT, FermatMessagesStatus.DELIVERED, String receiver, String sender,
                    Timestamp shippingTimestamp, String signature));*/




        } catch (Exception e) {
            throw new ErrorAskIntraUserForAcceptanceException("ERROR ASK INTRAUSER FOR ACCEPTANCE",e, "", "Generic Exception");
        }

    }

    @Override
    public void acceptIntraUser(String intraUserLoggedInPublicKey, String intraUserToAddPublicKey) throws ErrorAcceptIntraUserException {

        try {
        //TODO: guardar en la base de datos el cambio de estado a aceptado y responder


            //incomingNotificationsDao.c;

            /**
             * Guardo en mensaje en la base de datos
             */

            JsonObject obj=new JsonObject();
            obj.addProperty(com.bitdubai.fermat_ccp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.structure.JsonObjectConstants.SENDER_PUBLIC_HEY, intraUserLoggedInPublicKey);
            obj.addProperty(com.bitdubai.fermat_ccp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.structure.JsonObjectConstants.DESTINATION_PUBLIC_KEY, intraUserToAddPublicKey);
            obj.addProperty(com.bitdubai.fermat_ccp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.structure.JsonObjectConstants.MESSAGE_TYPE, IntraUserNotificationDescriptor.ACCEPTED.getCode());


            FermatMessage fermatMessage  = FermatMessageCommunicationFactory.constructFermatMessage(intraUserLoggedInPublicKey,  //Sender NetworkService
                    intraUserToAddPublicKey,   //Receiver
                    obj.toString(),                //Message Content
                    FermatMessageContentType.TEXT);//Type

            /*
             * Configure the correct status
             */
            ((FermatMessageCommunication)fermatMessage).setFermatMessagesStatus(FermatMessagesStatus.PENDING_TO_SEND);

            /*
             * Save to the data base table
             */
            communicationNetworkServiceConnectionManager.getOutgoingMessageDao().create(fermatMessage);


            /**
             * Connect
             */

            connectToBetweenActors(intraUserLoggedInPublicKey, PlatformComponentType.ACTOR_INTRA_USER, intraUserToAddPublicKey, PlatformComponentType.ACTOR_INTRA_USER);

//        NetworkServiceLocal communicationNetworkServiceLocal = communicationNetworkServiceConnectionManager.getNetworkServiceLocalInstance(remoteComponentProfile.getIdentityPublicKey());
//
//        communicationNetworkServiceLocal.sendMessage(platformComponentProfile.getIdentityPublicKey(),remoteComponentProfile.getIdentityPublicKey(),"Leon la puta que te pario");



        } catch (Exception e){
            throw new ErrorAcceptIntraUserException("ERROR ACCEPTED CONNECTION TO INTRAUSER",e, "", "Generic Exception");
        }
    }

    @Override
    public void denyConnection(String intraUserLoggedInPublicKey, String intraUserToRejectPublicKey) throws ErrorDenyConnectingIntraUserException {

        //TODO: guardar en la base de datos el cambio de estado a denegado y responder

        try {

            /**
             * Guardo en mensaje en la base de datos
             */

            JsonObject obj=new JsonObject();
            obj.addProperty(com.bitdubai.fermat_ccp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.structure.JsonObjectConstants.SENDER_PUBLIC_HEY, intraUserLoggedInPublicKey);
            obj.addProperty(com.bitdubai.fermat_ccp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.structure.JsonObjectConstants.DESTINATION_PUBLIC_KEY, intraUserToRejectPublicKey);
            obj.addProperty(com.bitdubai.fermat_ccp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.structure.JsonObjectConstants.MESSAGE_REASON, IntraUserNotificationDescriptor.DENIED.getCode());
            obj.addProperty(com.bitdubai.fermat_ccp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.structure.JsonObjectConstants.MESSAGE_TYPE, MessageType.REQUEST.getCode());


            FermatMessage fermatMessage  = FermatMessageCommunicationFactory.constructFermatMessage(intraUserLoggedInPublicKey,  //Sender NetworkService
                    intraUserToRejectPublicKey,   //Receiver
                    obj.toString(),                //Message Content
                    FermatMessageContentType.TEXT);//Type

            /*
             * Configure the correct status
             */
            ((FermatMessageCommunication)fermatMessage).setFermatMessagesStatus(FermatMessagesStatus.PENDING_TO_SEND);

            /*
             * Save to the data base table
             */
            communicationNetworkServiceConnectionManager.getOutgoingMessageDao().create(fermatMessage);


            //conect

            connectToBetweenActors(intraUserLoggedInPublicKey,PlatformComponentType.ACTOR_INTRA_USER,intraUserToRejectPublicKey,PlatformComponentType.ACTOR_INTRA_USER);


        } catch (Exception e) {
            throw new ErrorDenyConnectingIntraUserException("ERROR DENY CONNECTION TO INTRAUSER",e, "", "Generic Exception");
        }

    }

    @Override
    public void disconnectIntraUSer(String intraUserLoggedInPublicKey, String intraUserToDisconnectPublicKey) throws ErrorDisconnectingIntraUserException {

        //TODO: cambiar el estado de un intra user a desconectado que seria un bloqueado
        //TODO FALTA HACERLO
        try {


//            //Save request fire event to intra user
//            UUID requestId = UUID.randomUUID();
//            getIntraUserNetworkServiceDao().saveRequestCache(requestId, intraUserLoggedInPublicKey, "", intraUserToDisconnectPublicKey, IntraUserNotificationDescriptor.DISCONNECTED,new byte[0]);
//
//
//            FermatEvent fermatEvent = eventManager.getNewEvent(EventType.INTRA_USER_DISCONNECTION_REQUEST_RECEIVED);
//            IntraUserActorConnectionCancelledEvent intraUserActorConnectionCancelledEvent = (IntraUserActorConnectionCancelledEvent) fermatEvent;
//
//            intraUserActorConnectionCancelledEvent.setIntraUserLoggedInPublicKey(intraUserLoggedInPublicKey);
//            intraUserActorConnectionCancelledEvent.setIntraUserToAddPublicKey(intraUserToDisconnectPublicKey);


            //eventManager.raiseEvent(intraUserActorConnectionCancelledEvent);
//        }
//        catch (CantExecuteDatabaseOperationException e) {
//            throw new ErrorDisconnectingIntraUserException("ERROR DISCONNECTING INTRAUSER ",e,"","Error to save record on database");
//        }
        }catch (Exception e) {
            throw new ErrorDisconnectingIntraUserException("ERROR DISCONNECTING INTRAUSER ",e, "", "Generic Exception");
        }

    }

    @Override
    public void cancelIntraUSer(String intraUserLoggedInPublicKey, String intraUserToCancelPublicKey) throws ErrorCancellingIntraUserException {

        //TODO: guardar en la base de datos que es cancelada y responder

        //FermatMessage fermatMessage

        //communicationNetworkServiceConnectionManager.getOutgoingMessageDao().create();
        try {
            //Save request fire event to intra user
//            UUID requestId = UUID.randomUUID();
//            getIntraUserNetworkServiceDao().saveRequestCache(requestId, intraUserLoggedInPublicKey, "",intraUserToCancelPublicKey, IntraUserNotificationDescriptor.CANCEL,new byte[0]);


            FermatEvent fermatEvent = eventManager.getNewEvent(EventType.INTRA_USER_DISCONNECTION_REQUEST_RECEIVED);
            IntraUserActorConnectionCancelledEvent intraUserActorConnectionCancelledEvent = (IntraUserActorConnectionCancelledEvent) fermatEvent;

            intraUserActorConnectionCancelledEvent.setIntraUserLoggedInPublicKey(intraUserLoggedInPublicKey);
            intraUserActorConnectionCancelledEvent.setIntraUserToAddPublicKey(intraUserToCancelPublicKey);


            eventManager.raiseEvent(intraUserActorConnectionCancelledEvent);

        }
//        catch (CantExecuteDatabaseOperationException e)
//        {
//            throw new ErrorCancellingIntraUserException("ERROR CANCEL CONNECTION TO INTRAUSER ",e,"","Error to save record on database");
//        }
        catch (Exception e)
        {
            throw new ErrorCancellingIntraUserException("ERROR CANCEL CONNECTION TO INTRAUSER ",e, "", "Generic Exception");
        }

    }

    @Override
    public List<IntraUserNotification> getPendingNotifications() throws ErrorGetNotificationsIntraUserException {

     //   try {
      //     return  getIntraUserNetworkServiceDao().getAllRequestCacheRecord();
      //  }
      // catch (CantExecuteDatabaseOperationException e) {
      //      throw new ErrorGetNotificationsIntraUserException("ERROR GETING NOTIFICATIONS ",e,"","Error list records to database");
       // }
      //  catch (Exception e)
       // {
       //     throw new ErrorGetNotificationsIntraUserException("ERROR GETING NOTIFICATIONS  ",e, "", "Generic Exception");
       // }

        List<IntraUserNotification> lstIntraUserNotifications = new ArrayList<>();
        try {
            lstIntraUserNotifications .addAll(incomingNotificationsDao.listNotificationsUnreaded());
        } catch (CantListIntraWalletUsersException e) {
            e.printStackTrace();
        }

        return lstIntraUserNotifications;
    }

    @Override
    public void confirmNotification(String intraUserLogedInPublicKey, String intraUserInvolvedPublicKey) throws ErrorConfirmNotificationsIntraUserException {
//        incomingNotificationsDao.markReadedNotification();
    }

    @Override
    public void confirmNotification(UUID notificationID) throws ErrorConfirmNotificationsIntraUserException {
        incomingNotificationsDao.markReadedNotification(notificationID);
    }

    @Override
    public void registrateActors(List<Actor> actors) {

        //TODO: deberia cambiaresto para que venga el tipo de actor a registrar



        CommunicationsClientConnection communicationsClientConnection = wsCommunicationsCloudClientManager.getCommunicationsCloudClientConnection();


        for(Actor actor:actors){



        /*
         * Construct  profile and register
         */

            //build jsonObject Photo
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(com.bitdubai.fermat_ccp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.structure.JsonObjectConstants.PROFILE_IMAGE, actor.getPhoto().toString());

            PlatformComponentProfile platformComponentProfile =  communicationsClientConnection.constructPlatformComponentProfileFactory(actor.getActorPublicKey(),
                    (actor.getName().toLowerCase()),
                    (actor.getName().toLowerCase() + "_" + this.getName()),
                    NetworkServiceType.UNDEFINED, // aca iria UNDEFIND
                    PlatformComponentType.ACTOR_INTRA_USER, // actor.INTRA_USER
                    jsonObject.toString());


            if(!actorsToRegisterCache.contains(platformComponentProfile)) {
                actorsToRegisterCache.add(platformComponentProfile);

                if (register) {
                    try {
                        communicationsClientConnection.registerComponentForCommunication(platformComponentProfile);

                    } catch (CantRegisterComponentException e) {
                        e.printStackTrace();
                    }
                }
            }



        }
    }

    @Override
    public Actor contructIdentity(String publicKey, String alias,Actors actors ,byte[] profileImage) {
        return new Identity(publicKey,alias,actors,profileImage);
    }


    public void connectToBetweenActors(String senderPK,PlatformComponentType senderType,String receiverPK,PlatformComponentType receiverType){
        PlatformComponentProfile applicantParticipant = wsCommunicationsCloudClientManager.getCommunicationsCloudClientConnection().constructBasicPlatformComponentProfileFactory(
                senderPK,
                NetworkServiceType.UNDEFINED,
                senderType
        );

        PlatformComponentProfile remoteParticipant = wsCommunicationsCloudClientManager.getCommunicationsCloudClientConnection().constructBasicPlatformComponentProfileFactory(
                receiverPK,
                NetworkServiceType.UNDEFINED,
                receiverType
        );

        communicationNetworkServiceConnectionManager.connectTo(applicantParticipant, platformComponentProfile, remoteParticipant);
    }


    /**
     * Request list
     */


    /**
     * DAO and DATABASE
     */

    /**
     *
     * @return
     */
    public Database getDataBase(){
        return dataBase;
    }

    public IncomingNotificationDao getIncomingNotificationsDao(){
        return incomingNotificationsDao;
    }
    public OutgoingNotificationDao getOutgoingNotificationDao(){
        return outgoingNotificationDao;
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
            String[] correctedClass = className.split((Pattern.quote("$")));
            return IntraActorNetworkServicePluginRoot.newLoggingLevel.get(correctedClass[0]);
        } catch (Exception e){
            /**
             * If I couldn't get the correct loggin level, then I will set it to minimal.
             */
            return DEFAULT_LOG_LEVEL;
        }
    }

    /**
     * (non-Javadoc)
     * @see DealsWithLogger#setLogManager(LogManager)
     */
    @Override
    public void setLogManager(LogManager logManager) {
        this.logManager = logManager;
    }

    /**
     * (non-Javadoc)
     * @see LogManagerForDevelopers#getClassesFullPath()
     */
    @Override
    public List<String> getClassesFullPath() {
        List<String> returnedClasses = new ArrayList<String>();
        returnedClasses.add("com.bitdubai.fermat_dmp_plugin.layer.network_service.template.developer.bitdubai.version_1.TemplateNetworkServicePluginRoot");
        returnedClasses.add("com.bitdubai.fermat_dmp_plugin.layer.network_service.template.developer.bitdubai.version_1.structure.IncomingTemplateNetworkServiceMessage");
        returnedClasses.add("com.bitdubai.fermat_dmp_plugin.layer.network_service.template.developer.bitdubai.version_1.structure.OutgoingTemplateNetworkServiceMessage");
        returnedClasses.add("com.bitdubai.fermat_dmp_plugin.layer.network_service.template.developer.bitdubai.version_1.communications.CommunicationRegistrationProcessNetworkServiceAgent");
        returnedClasses.add("com.bitdubai.fermat_dmp_plugin.layer.network_service.template.developer.bitdubai.version_1.communications.CommunicationNetworkServiceLocal");
        returnedClasses.add("com.bitdubai.fermat_dmp_plugin.layer.network_service.template.developer.bitdubai.version_1.communications.CommunicationNetworkServiceConnectionManager");
        returnedClasses.add("com.bitdubai.fermat_dmp_plugin.layer.network_service.template.developer.bitdubai.version_1.communications.CommunicationNetworkServiceRemoteAgent");
        return returnedClasses;
    }

    /**
     * (non-Javadoc)
     * @see LogManagerForDevelopers#setLoggingLevelPerClass(Map<String, LogLevel>)
     */
    @Override
    public void setLoggingLevelPerClass(Map<String, LogLevel> newLoggingLevel) {

        /*
         * I will check the current values and update the LogLevel in those which is different
         */
        for (Map.Entry<String, LogLevel> pluginPair : newLoggingLevel.entrySet()) {

            /*
             * if this path already exists in the Root.bewLoggingLevel I'll update the value, else, I will put as new
             */
            if (IntraActorNetworkServicePluginRoot.newLoggingLevel.containsKey(pluginPair.getKey())) {
                IntraActorNetworkServicePluginRoot.newLoggingLevel.remove(pluginPair.getKey());
                IntraActorNetworkServicePluginRoot.newLoggingLevel.put(pluginPair.getKey(), pluginPair.getValue());
            } else {
                IntraActorNetworkServicePluginRoot.newLoggingLevel.put(pluginPair.getKey(), pluginPair.getValue());
            }
        }

    }


    /**
     * (non-Javadoc)
     * @see DatabaseManagerForDevelopers#getDatabaseList(DeveloperObjectFactory)
     */
    @Override
    public List<DeveloperDatabase> getDatabaseList(DeveloperObjectFactory developerObjectFactory) {
        return communicationNetworkServiceDeveloperDatabaseFactory.getDatabaseList(developerObjectFactory);
    }

    /**
     * (non-Javadoc)
     * @see DatabaseManagerForDevelopers#getDatabaseTableList(DeveloperObjectFactory, DeveloperDatabase)
     */
    @Override
    public List<DeveloperDatabaseTable> getDatabaseTableList(DeveloperObjectFactory developerObjectFactory, DeveloperDatabase developerDatabase) {
        return communicationNetworkServiceDeveloperDatabaseFactory.getDatabaseTableList(developerObjectFactory);
    }

    /**
     * (non-Javadoc)
     * @see DatabaseManagerForDevelopers#getDatabaseTableContent(DeveloperObjectFactory, DeveloperDatabase, DeveloperDatabaseTable)
     */
    @Override
    public List<DeveloperDatabaseTableRecord> getDatabaseTableContent(DeveloperObjectFactory developerObjectFactory, DeveloperDatabase developerDatabase, DeveloperDatabaseTable developerDatabaseTable) {
        return communicationNetworkServiceDeveloperDatabaseFactory.getDatabaseTableContent(developerObjectFactory, developerDatabaseTable);
    }


    /**
     * This method initialize the database
     *
     * @throws CantInitializeTemplateNetworkServiceDatabaseException
     */
    private void initializeDb() throws CantInitializeTemplateNetworkServiceDatabaseException {

        try {
            /*
             * Open new database connection
             */
            this.dataBase = this.pluginDatabaseSystem.openDatabase(pluginId, CommunicationNetworkServiceDatabaseConstants.DATA_BASE_NAME);

        } catch (CantOpenDatabaseException cantOpenDatabaseException) {

            /*
             * The database exists but cannot be open. I can not handle this situation.
             */
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_TEMPLATE_NETWORK_SERVICE, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, cantOpenDatabaseException);
            throw new CantInitializeTemplateNetworkServiceDatabaseException(cantOpenDatabaseException.getLocalizedMessage());

        } catch (DatabaseNotFoundException e) {

            /*
             * The database no exist may be the first time the plugin is running on this device,
             * We need to create the new database
             */
            CommunicationNetworkServiceDatabaseFactory communicationNetworkServiceDatabaseFactory = new CommunicationNetworkServiceDatabaseFactory(pluginDatabaseSystem);

            try {

                /*
                 * We create the new database
                 */
                this.dataBase = communicationNetworkServiceDatabaseFactory.createDatabase(pluginId, CommunicationNetworkServiceDatabaseConstants.DATA_BASE_NAME);

            } catch (CantCreateDatabaseException cantOpenDatabaseException) {

                /*
                 * The database cannot be created. I can not handle this situation.
                 */
                errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_TEMPLATE_NETWORK_SERVICE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantOpenDatabaseException);
                throw new CantInitializeTemplateNetworkServiceDatabaseException(cantOpenDatabaseException.getLocalizedMessage());

            }
        }

    }


}