package com.bitdubai.fermat_cbp_plugin.layer.wallet_module.crypto_broker.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_cbp_api.all_definition.enums.ClauseStatus;
import com.bitdubai.fermat_cbp_api.all_definition.enums.ClauseType;
import com.bitdubai.fermat_cbp_api.all_definition.enums.NegotiationStatus;
import com.bitdubai.fermat_cbp_api.all_definition.negotiation.Clause;
import com.bitdubai.fermat_cbp_api.layer.negotiation.customer_broker_sale.interfaces.CustomerBrokerSaleNegotiation;
import com.bitdubai.fermat_cbp_api.layer.negotiation.exceptions.CantGetListClauseException;
import com.bitdubai.fermat_cbp_api.layer.wallet_module.common.interfaces.ClauseInformation;
import com.bitdubai.fermat_cbp_api.layer.wallet_module.common.interfaces.CustomerBrokerNegotiationInformation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * Created by franklin on 05/01/16.
 */
public class CustomerBrokerSaleNegotiationImpl implements CustomerBrokerSaleNegotiation {
    long startDate;
    long negotiationUpdateDatetime;
    long expirationDatetime;
    boolean nearExpirationDatetime;
    String cancelReason;
    String memo;
    String customerPublicKey;
    String brokerPublicKey;
    UUID negotiationId;
    NegotiationStatus status;
    Collection<Clause> clauses;


    public CustomerBrokerSaleNegotiationImpl(UUID negotiationId) {
        this.negotiationId = negotiationId;
        clauses = new ArrayList<>();
        status = null;
    }

    public CustomerBrokerSaleNegotiationImpl(CustomerBrokerSaleNegotiation negotiationInfo) {
        startDate = negotiationInfo.getStartDate();
        negotiationUpdateDatetime = negotiationInfo.getLastNegotiationUpdateDate();
        expirationDatetime = negotiationInfo.getNegotiationExpirationDate();
        nearExpirationDatetime = negotiationInfo.getNearExpirationDatetime();
        cancelReason = negotiationInfo.getCancelReason();
        memo = negotiationInfo.getMemo();
        customerPublicKey = negotiationInfo.getCustomerPublicKey();
        brokerPublicKey = negotiationInfo.getBrokerPublicKey();
        negotiationId = negotiationInfo.getNegotiationId();
        status = negotiationInfo.getStatus();
        try {
            clauses = negotiationInfo.getClauses();
        } catch (CantGetListClauseException e) {
            clauses = new ArrayList<>();
        }
    }

    /**
     * @return the broker public key
     */
    @Override
    public String getCustomerPublicKey() {
        return customerPublicKey;
    }

    /**
     * @return the broker public key
     */
    @Override
    public String getBrokerPublicKey() {
        return brokerPublicKey;
    }

    /**
     * @return the Negotiation ID
     */
    @Override
    public UUID getNegotiationId() {
        return negotiationId;
    }

    /**
     * @return a long representation of the Datetime the negotiation started
     */
    @Override
    public Long getStartDate() {
        return startDate;
    }

    /**
     * @return a long representation of the last Datetime the negotiation was updated
     */
    @Override
    public Long getLastNegotiationUpdateDate() {
        return negotiationUpdateDatetime;
    }

    /**
     * @param lastNegotiationUpdateDate the last negotiation update datetime y millis
     */
    @Override
    public void setLastNegotiationUpdateDate(Long lastNegotiationUpdateDate) {
        negotiationUpdateDatetime = lastNegotiationUpdateDate;
    }

    /**
     * @return a long representation of the Datetime the negotiation is going to be available (this is set by the broker)
     */
    @Override
    public Long getNegotiationExpirationDate() {
        return expirationDatetime;
    }

    /**
     * @return the negotiation Status
     */
    @Override
    public NegotiationStatus getStatus() {
        return status;
    }

    /**
     * @return a Boolean with NearExpirationDatetime
     */
    @Override
    public Boolean getNearExpirationDatetime() {
        return nearExpirationDatetime;
    }

    /**
     * @return the clauses that conform this negotiation
     * @throws CantGetListClauseException
     */
    @Override
    public Collection<Clause> getClauses() throws CantGetListClauseException {
        return clauses;
    }

    /**
     * set a string representing the reason why the negotiation was cancelled
     *
     * @param cancelReason text whit the reason
     */
    @Override
    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    /**
     * @return string representing the reason why the negotiation was cancelled
     */
    @Override
    public String getCancelReason() {
        return cancelReason;
    }

    /**
     * set a string representing a note or memo (free text) to put more info about the negotiation,
     * this can be set by the customer or the broker
     *
     * @param memo the note or memo
     */
    @Override
    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * @return the string representing a note or memo (free text) to put more info about the negotiation,
     * this can be set by the customer or the broker
     */
    @Override
    public String getMemo() {
        return memo;
    }

    public void changeInfo(CustomerBrokerNegotiationInformation negotiationInfo, NegotiationStatus status) {
        negotiationUpdateDatetime = negotiationInfo.getLastNegotiationUpdateDate();
        expirationDatetime = negotiationInfo.getNegotiationExpirationDate();
        cancelReason = negotiationInfo.getCancelReason();
        memo = negotiationInfo.getMemo();
        this.status = status;

        Collection<ClauseInformation> values = negotiationInfo.getClauses().values();
        clauses = new ArrayList<>();
        for (final ClauseInformation value : values)
            clauses.add(new ClauseImpl(value, brokerPublicKey));
    }

    @Override
    public String toString() {
        return "long startDate: " + startDate + "\n" +
                "long negotiationUpdateDatetime: " + negotiationUpdateDatetime + "\n" +
                "long expirationDatetime: " + expirationDatetime + "\n" +
                "boolean nearExpirationDatetime: " + nearExpirationDatetime + "\n" +
                "String cancelReason: " + cancelReason + "\n" +
                "String memo: " + memo + "\n" +
                "String customerPublicKey: " + customerPublicKey + "\n" +
                "String brokerPublicKey: " + brokerPublicKey + "\n" +
                "UUID negotiationId: " + negotiationId + "\n" +
                "NegotiationStatus status:" + status + " \n" +
                "Collection<Clause> clauses: " + clauses;
    }
}
