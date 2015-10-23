package com.bitdubai.reference_wallet.crypto_broker_wallet.common.models;

import com.bitdubai.fermat_cbp_api.layer.cbp_wallet_module.crypto_broker.interfaces.NegotiationBasicInformation;

import java.util.Random;
import java.util.UUID;

/**
 * Custom child list item.
 * <p/>
 * This is for demo purposes, although it is recommended having a separate
 * child list item from your parent list item.
 *
 * @author Ryan Brooks
 * @version 1.0
 * @since 5/27/2015
 */
public class NegotiationBasicInformationImpl implements NegotiationBasicInformation {

    private String customerAlias;
    private byte[] imageBytes;
    private UUID negotiationId;
    private float amount;
    private String merchandise;
    private String typeOfPayment;
    private float exchangeRateAmount;
    private String paymentCurrency;


    public NegotiationBasicInformationImpl(String customerAlias, String merchandise, String typeOfPayment, String paymentCurrency) {
        this.customerAlias = customerAlias;
        this.merchandise = merchandise;
        this.typeOfPayment = typeOfPayment;
        this.paymentCurrency = paymentCurrency;

        Random random = new Random(321515131);
        amount = random.nextFloat();
        exchangeRateAmount = random.nextFloat();

        imageBytes = new byte[0];
        negotiationId = UUID.randomUUID();
    }

    @Override
    public String getCryptoCustomerAlias() {
        return customerAlias;
    }

    @Override
    public byte[] getCryptoCustomerImage() {

        return imageBytes;
    }

    @Override
    public UUID getNegotiationId() {
        return negotiationId;
    }

    @Override
    public float getAmount() {
        return amount;
    }

    @Override
    public String getMerchandise() {
        return merchandise;
    }

    @Override
    public String getTypeOfPayment() {
        return typeOfPayment;
    }

    @Override
    public float getExchangeRateAmount() {
        return exchangeRateAmount;
    }

    @Override
    public String getPaymentCurrency() {
        return paymentCurrency;
    }
}