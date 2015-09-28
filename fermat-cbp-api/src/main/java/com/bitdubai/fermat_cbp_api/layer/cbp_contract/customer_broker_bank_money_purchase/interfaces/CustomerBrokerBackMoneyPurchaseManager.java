package com.bitdubai.fermat_cbp_api.layer.cbp_contract.customer_broker_bank_money_purchase.interfaces;


import com.bitdubai.fermat_cbp_api.layer.cbp_contract.customer_broker_bank_money_purchase.exceptions.CantCreateCustomerBrokerBankMoneyPurchaseException;
import com.bitdubai.fermat_cbp_api.layer.cbp_contract.customer_broker_bank_money_purchase.exceptions.CantupdateStatusCustomerBrokerBackMoneyPurchaseException;

import java.util.List;
import java.util.UUID;

/**
 * Created by angel on 16/9/15.
 */
public interface CustomerBrokerBackMoneyPurchaseManager {

    List<CustomerBrokerBankMoneyPurchase> getAllCustomerBrokerBackMoneyPurchaseFromCurrentDeviceUser();

    CustomerBrokerBankMoneyPurchase createCustomerBrokerBackMoneyPurchase(
                                        final String publicKeyCustomer,
                                        final String publicKeyBroker,
                                        final Float merchandiseAmount,
                                        final String merchandiseCurrency,
                                        final Float referencePrice,
                                        final String referenceCurrency,
                                        final Float paymentAmount,
                                        final String paymentCurrency,
                                        final long paymentExpirationDate,
                                        final long merchandiseDeliveryExpirationDate
                                    ) throws CantCreateCustomerBrokerBankMoneyPurchaseException;

    void updateStatusCustomerBrokerBackMoneyPurchase(final UUID ContractId) throws CantupdateStatusCustomerBrokerBackMoneyPurchaseException;

}