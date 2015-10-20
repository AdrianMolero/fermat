package com.bitdubai.fermat_cbp_plugin.layer.negotiation.customer_broker_purchase.developer.bitdubai.version_1;

import com.bitdubai.fermat_api.CantStartPluginException;
import com.bitdubai.fermat_api.Plugin;
import com.bitdubai.fermat_api.Service;
import com.bitdubai.fermat_api.layer.all_definition.developer.LogManagerForDevelopers;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DealsWithPluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.DealsWithLogger;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogLevel;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogManager;
import com.bitdubai.fermat_cbp_api.all_definition.enums.NegotiationStatus;
import com.bitdubai.fermat_cbp_api.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_cbp_api.all_definition.identity.ActorIdentity;
import com.bitdubai.fermat_cbp_api.layer.cbp_negotiation.customer_broker_purchase.exceptions.CantCreateCustomerBrokerPurchaseException;
import com.bitdubai.fermat_cbp_api.layer.cbp_negotiation.customer_broker_purchase.exceptions.CantListPurchaseNegotianionsException;
import com.bitdubai.fermat_cbp_api.layer.cbp_negotiation.customer_broker_purchase.interfaces.CustomerBrokerPurchase;
import com.bitdubai.fermat_cbp_api.layer.cbp_negotiation.customer_broker_purchase.interfaces.CustomerBrokerPurchaseManager;
import com.bitdubai.fermat_cbp_plugin.layer.negotiation.customer_broker_purchase.developer.bitdubai.version_1.database.CustomerBrokerPurchaseNegotiationDao;
import com.bitdubai.fermat_cbp_plugin.layer.negotiation.customer_broker_purchase.developer.bitdubai.version_1.exceptions.CantInitializeCustomerBrokerPurchaseNegotiationDaoException;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.DealsWithErrors;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.ErrorManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by jorge on 12-10-2015.
 */
public class CustomerBrokerPurchasePluginRoot implements CustomerBrokerPurchaseManager, DealsWithErrors, DealsWithLogger, DealsWithPluginDatabaseSystem, LogManagerForDevelopers, Service, Plugin {

    private ErrorManager errorManager;
    private LogManager logManager;
    private PluginDatabaseSystem pluginDatabaseSystem;
    private UUID pluginId;
    private static Map<String, LogLevel> newLoggingLevel = new HashMap<>();
    private CustomerBrokerPurchaseNegotiationDao customerBrokerPurchaseNegotiationDao;
    private ServiceStatus serviceStatus = ServiceStatus.CREATED;


    @Override
    public void setId(UUID pluginId) {
        this.pluginId = pluginId;
    }

    @Override
    public void setErrorManager(ErrorManager errorManager) {
        this.errorManager = errorManager;
    }

    @Override
    public void setLogManager(LogManager logManager) {
        this.logManager = logManager;
    }

    @Override
    public List<String> getClassesFullPath() {
        List<String> returnedClasses = new ArrayList<String>();
        returnedClasses.add("com.bitdubai.fermat_cbp_plugin.layer.negotiation.customer_broker_purchase.developer.bitdubai.version_1.CryptoBrokerIdentityPluginRoot");
        returnedClasses.add("com.bitdubai.fermat_cbp_plugin.layer.negotiation.customer_broker_purchase.developer.bitdubai.version_1.structure.CustomerBrokerPurchaseNegotiation");
        returnedClasses.add("com.bitdubai.fermat_cbp_plugin.layer.negotiation.customer_broker_purchase.developer.bitdubai.version_1.database.CustomerBrokerPurchaseNegotiationDao");
        returnedClasses.add("com.bitdubai.fermat_cbp_plugin.layer.negotiation.customer_broker_purchase.developer.bitdubai.version_1.database.CustomerBrokerPurchaseNegotiationDatabaseFactory");
        returnedClasses.add("com.bitdubai.fermat_cbp_plugin.layer.negotiation.customer_broker_purchase.developer.bitdubai.version_1.database.CustomerBrokerPurchaseNegotiationDatabaseConstants");
        return returnedClasses;
    }

    @Override
    public void setLoggingLevelPerClass(Map<String, LogLevel> newLoggingLevel) {
        for (Map.Entry<String, LogLevel> pluginPair : newLoggingLevel.entrySet()) {
            if (CustomerBrokerPurchasePluginRoot.newLoggingLevel.containsKey(pluginPair.getKey())) {
                CustomerBrokerPurchasePluginRoot.newLoggingLevel.remove(pluginPair.getKey());
                CustomerBrokerPurchasePluginRoot.newLoggingLevel.put(pluginPair.getKey(), pluginPair.getValue());
            } else {
                CustomerBrokerPurchasePluginRoot.newLoggingLevel.put(pluginPair.getKey(), pluginPair.getValue());
            }
        }
    }

    @Override
    public void start() throws CantStartPluginException {
        this.serviceStatus = ServiceStatus.STARTED;
        try {
            this.customerBrokerPurchaseNegotiationDao = new CustomerBrokerPurchaseNegotiationDao(pluginDatabaseSystem);
            this.customerBrokerPurchaseNegotiationDao.initialize(pluginId);
        } catch (CantInitializeCustomerBrokerPurchaseNegotiationDaoException cantInitializeExtraUserRegistryException) {
            //errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DESIGNER_IDENTITY, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, cantInitializeExtraUserRegistryException);
            // throw new CantStartPluginException(cantInitializeExtraUserRegistryException, Plugins.BITDUBAI_ACTOR_DEVELOPER);
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
        return serviceStatus;
    }

    @Override
    public void setPluginDatabaseSystem(PluginDatabaseSystem pluginDatabaseSystem) {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
    }

    @Override
    public CustomerBrokerPurchase createCustomerBrokerPurchaseNegotiation(String publicKeyCustomer, String publicKeyBroker) throws CantCreateCustomerBrokerPurchaseException {
        return null;
    }

    @Override
    public void cancelNegotiation(CustomerBrokerPurchase negotiation) {

    }

    @Override
    public void closeNegotiation(CustomerBrokerPurchase negotiation) {

    }

    @Override
    public Collection<CustomerBrokerPurchase> getNegotiations() throws CantListPurchaseNegotianionsException {
        try {
            Collection<CustomerBrokerPurchase> negotiations = new ArrayList<CustomerBrokerPurchase>();
            negotiations = customerBrokerPurchaseNegotiationDao.getNegotiations();
            return negotiations;
        } catch (CantLoadTableToMemoryException e) {
            throw new CantListPurchaseNegotianionsException(CantListPurchaseNegotianionsException.DEFAULT_MESSAGE, e, "", "");
        } catch (InvalidParameterException e) {
            throw new CantListPurchaseNegotianionsException(CantListPurchaseNegotianionsException.DEFAULT_MESSAGE, e, "", "");
        }
    }

    @Override
    public Collection<CustomerBrokerPurchase> getNegotiations(NegotiationStatus status) throws CantListPurchaseNegotianionsException {
        try {
            Collection<CustomerBrokerPurchase> negotiations = new ArrayList<CustomerBrokerPurchase>();
            negotiations = customerBrokerPurchaseNegotiationDao.getNegotiations(status);
            return negotiations;
        } catch (CantLoadTableToMemoryException e) {
            throw new CantListPurchaseNegotianionsException(CantListPurchaseNegotianionsException.DEFAULT_MESSAGE, e, "", "");
        } catch (InvalidParameterException e) {
            throw new CantListPurchaseNegotianionsException(CantListPurchaseNegotianionsException.DEFAULT_MESSAGE, e, "", "");
        }
    }

    @Override
    public Collection<CustomerBrokerPurchase> getNegotiationsByCustomer(ActorIdentity customer) throws CantListPurchaseNegotianionsException {
        try {
            Collection<CustomerBrokerPurchase> negotiations = new ArrayList<CustomerBrokerPurchase>();
            negotiations = customerBrokerPurchaseNegotiationDao.getNegotiationsByCustomer(customer);
            return negotiations;
        } catch (CantLoadTableToMemoryException e) {
            throw new CantListPurchaseNegotianionsException(CantListPurchaseNegotianionsException.DEFAULT_MESSAGE, e, "", "");
        } catch (InvalidParameterException e) {
            throw new CantListPurchaseNegotianionsException(CantListPurchaseNegotianionsException.DEFAULT_MESSAGE, e, "", "");
        }
    }

    @Override
    public Collection<CustomerBrokerPurchase> getNegotiationsByBroker(ActorIdentity broker) throws CantListPurchaseNegotianionsException {
        try {
            Collection<CustomerBrokerPurchase> negotiations = new ArrayList<CustomerBrokerPurchase>();
            negotiations = customerBrokerPurchaseNegotiationDao.getNegotiationsByBroker(broker);
            return negotiations;
        } catch (CantLoadTableToMemoryException e) {
            throw new CantListPurchaseNegotianionsException(CantListPurchaseNegotianionsException.DEFAULT_MESSAGE, e, "", "");
        } catch (InvalidParameterException e) {
            throw new CantListPurchaseNegotianionsException(CantListPurchaseNegotianionsException.DEFAULT_MESSAGE, e, "", "");
        }
    }
}
