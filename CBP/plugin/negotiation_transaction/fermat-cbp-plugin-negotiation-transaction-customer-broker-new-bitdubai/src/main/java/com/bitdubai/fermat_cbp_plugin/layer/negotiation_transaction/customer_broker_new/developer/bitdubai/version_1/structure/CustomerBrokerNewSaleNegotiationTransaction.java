package com.bitdubai.fermat_cbp_plugin.layer.negotiation_transaction.customer_broker_new.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_cbp_api.all_definition.enums.NegotiationTransactionStatus;
import com.bitdubai.fermat_cbp_api.all_definition.enums.NegotiationType;
import com.bitdubai.fermat_cbp_api.layer.negotiation.customer_broker_sale.exceptions.CantCreateCustomerBrokerSaleNegotiationException;
import com.bitdubai.fermat_cbp_api.layer.negotiation.customer_broker_sale.interfaces.CustomerBrokerSaleNegotiation;
import com.bitdubai.fermat_cbp_api.layer.negotiation.customer_broker_sale.interfaces.CustomerBrokerSaleNegotiationManager;
import com.bitdubai.fermat_cbp_plugin.layer.negotiation_transaction.customer_broker_new.developer.bitdubai.version_1.database.CustomerBrokerNewNegotiationTransactionDatabaseDao;
import com.bitdubai.fermat_cbp_plugin.layer.negotiation_transaction.customer_broker_new.developer.bitdubai.version_1.exceptions.CantNewSaleNegotiationTransactionException;
import com.bitdubai.fermat_cbp_plugin.layer.negotiation_transaction.customer_broker_new.developer.bitdubai.version_1.exceptions.CantRegisterCustomerBrokerNewNegotiationTransactionException;

import java.util.UUID;

/**
 * Created by Yordin Alayn on 08.12.15.
 */
public class CustomerBrokerNewSaleNegotiationTransaction {

    /*Represent the Negotiation Sale*/
    private CustomerBrokerSaleNegotiationManager                customerBrokerSaleNegotiationManager;

    /*Represent the Transaction database DAO */
    private CustomerBrokerNewNegotiationTransactionDatabaseDao  customerBrokerNewNegotiationTransactionDatabaseDao;

    public CustomerBrokerNewSaleNegotiationTransaction(
        CustomerBrokerSaleNegotiationManager                    customerBrokerSaleNegotiationManager,
        CustomerBrokerNewNegotiationTransactionDatabaseDao      customerBrokerNewNegotiationTransactionDatabaseDao
    ){
        this.customerBrokerSaleNegotiationManager               = customerBrokerSaleNegotiationManager;
        this.customerBrokerNewNegotiationTransactionDatabaseDao = customerBrokerNewNegotiationTransactionDatabaseDao;
    }

    public void receiveSaleNegotiationTranasction(UUID transactionId, CustomerBrokerSaleNegotiation customerBrokerSaleNegotiation)  throws CantNewSaleNegotiationTransactionException{
        try {


            System.out.print("\n\n**** 20) MOCK NEGOTIATION TRANSACTION - CUSTOMER BROKER NEW - SALE NEGOTIATION - CREATE SALE NEGOTIATION TRANSACTION  ****\n" +
                    "\n --- Negotiation Mock XML Date" +
                    "\n- NegotiationId = " + customerBrokerSaleNegotiation.getNegotiationId() +
                    "\n- CustomerPublicKey = " + customerBrokerSaleNegotiation.getCustomerPublicKey() +
                    "\n- BrokerPublicKey = " + customerBrokerSaleNegotiation.getCustomerPublicKey()+
                    "\n- Status: = " + customerBrokerSaleNegotiation.getStatus()
            );

            //CREATE NEGOTIATION
            this.customerBrokerSaleNegotiationManager.createCustomerBrokerSaleNegotiation(customerBrokerSaleNegotiation);
            this.customerBrokerSaleNegotiationManager.waitForBroker(customerBrokerSaleNegotiation);

            //CREATE NEGOTIATION TRANSATION
            this.customerBrokerNewNegotiationTransactionDatabaseDao.createCustomerBrokerNewNegotiationTransaction(
                    transactionId,
                    customerBrokerSaleNegotiation,
                    NegotiationType.SALE,
                    NegotiationTransactionStatus.PENDING_SUBMIT_CONFIRM
            );

        } catch (CantCreateCustomerBrokerSaleNegotiationException e) {
            throw new CantNewSaleNegotiationTransactionException(e.getMessage(),e, CantNewSaleNegotiationTransactionException.DEFAULT_MESSAGE, "ERROR CREATE CUSTOMER BROKER SALE NEGOTIATION, UNKNOWN FAILURE.");
        } catch (CantRegisterCustomerBrokerNewNegotiationTransactionException e) {
            throw new CantNewSaleNegotiationTransactionException(e.getMessage(),e, CantNewSaleNegotiationTransactionException.DEFAULT_MESSAGE, "ERROR REGISTER CUSTOMER BROKER SALE NEGOTIATION TRANSACTION, UNKNOWN FAILURE.");
        } catch (Exception e){
            throw new CantNewSaleNegotiationTransactionException(e.getMessage(), FermatException.wrapException(e), CantNewSaleNegotiationTransactionException.DEFAULT_MESSAGE, "ERROR PROCESS CUSTOMER BROKER SALE NEGOTIATION, UNKNOWN FAILURE.");
        }

    }
}
