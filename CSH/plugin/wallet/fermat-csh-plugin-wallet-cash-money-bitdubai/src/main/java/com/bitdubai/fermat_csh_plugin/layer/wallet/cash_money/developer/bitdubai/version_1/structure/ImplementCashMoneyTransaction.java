package com.bitdubai.fermat_csh_plugin.layer.wallet.cash_money.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_csh_api.all_definition.enums.BalanceType;
import com.bitdubai.fermat_csh_api.all_definition.enums.CashCurrencyType;
import com.bitdubai.fermat_csh_api.all_definition.enums.CashTransactionStatus;
import com.bitdubai.fermat_csh_api.all_definition.enums.TransactionType;
import com.bitdubai.fermat_csh_api.layer.csh_wallet.cash_money.interfaces.CashMoneyTransaction;

import java.util.UUID;

/**
 * Created by francisco on 21/10/15.
 */
public class ImplementCashMoneyTransaction implements CashMoneyTransaction {
    @Override
    public UUID getCashTransactionId() {
        return null;
    }

    @Override
    public String getPublicKeyActorFrom() {
        return null;
    }

    @Override
    public String getPublicKeyActorTo() {
        return null;
    }

    @Override
    public CashTransactionStatus getStatus() {
        return null;
    }

    @Override
    public BalanceType getBalanceType() {
        return null;
    }

    @Override
    public TransactionType getTransactionType() {
        return null;
    }

    @Override
    public double getAmount() {
        return 0;
    }

    @Override
    public CashCurrencyType getCashCurrencyType() {
        return null;
    }

    @Override
    public String getCashReference() {
        return null;
    }

    @Override
    public long getTimestamp() {
        return 0;
    }

    @Override
    public long getRunningBookBalance() {
        return 0;
    }

    @Override
    public long getRunningAvailableBalance() {
        return 0;
    }

    @Override
    public String getMemo() {
        return null;
    }
}
