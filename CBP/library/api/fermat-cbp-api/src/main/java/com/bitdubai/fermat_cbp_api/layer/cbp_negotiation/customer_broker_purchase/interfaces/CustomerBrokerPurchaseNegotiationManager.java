package com.bitdubai.fermat_cbp_api.layer.cbp_negotiation.customer_broker_purchase.interfaces;

import com.bitdubai.fermat_cbp_api.all_definition.enums.NegotiationStatus;
import com.bitdubai.fermat_cbp_api.all_definition.identity.ActorIdentity;
import com.bitdubai.fermat_cbp_api.layer.cbp_negotiation.customer_broker_purchase.exceptions.CantCreateCustomerBrokerPurchaseNegotiationException;
import com.bitdubai.fermat_cbp_api.layer.cbp_negotiation.customer_broker_purchase.exceptions.CantListPurchaseNegotianionsException;

import java.util.Collection;

/**
 * Created by jorge on 10-10-2015.
 */
public interface CustomerBrokerPurchaseNegotiationManager {

    CustomerBrokerPurchaseNegotiation createCustomerBrokerPurchaseNegotiation(String publicKeyCustomer, String publicKeyBroker) throws CantCreateCustomerBrokerPurchaseNegotiationException;
    void cancelNegotiation(CustomerBrokerPurchase negotiation);
    void closeNegotiation(CustomerBrokerPurchase negotiation);

    Collection<CustomerBrokerPurchaseNegotiation> getNegotiations() throws CantListPurchaseNegotianionsException;
    Collection<CustomerBrokerPurchaseNegotiation> getNegotiations(NegotiationStatus status) throws CantListPurchaseNegotianionsException;
    Collection<CustomerBrokerPurchaseNegotiation> getNegotiationsByCustomer(ActorIdentity customer) throws CantListPurchaseNegotianionsException;
    Collection<CustomerBrokerPurchaseNegotiation> getNegotiationsByBroker(ActorIdentity broker) throws CantListPurchaseNegotianionsException;
}
