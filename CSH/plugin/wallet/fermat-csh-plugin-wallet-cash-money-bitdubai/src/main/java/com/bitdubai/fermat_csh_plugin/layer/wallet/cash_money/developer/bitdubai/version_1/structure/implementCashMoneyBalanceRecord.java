package com.bitdubai.fermat_csh_plugin.layer.wallet.cash_money.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_csh_api.all_definition.enums.BalanceType;
import com.bitdubai.fermat_csh_api.all_definition.enums.CashCurrencyType;
import com.bitdubai.fermat_csh_api.all_definition.enums.CashTransactionStatus;
import com.bitdubai.fermat_csh_api.all_definition.enums.TransactionType;
import com.bitdubai.fermat_csh_api.layer.csh_wallet.cash_money.interfaces.CashMoneyBalanceRecord;
import com.bitdubai.fermat_csh_plugin.layer.wallet.cash_money.developer.bitdubai.version_1.database.CashMoneyWalletDao;
import com.bitdubai.fermat_csh_plugin.layer.wallet.cash_money.developer.bitdubai.version_1.exceptions.CantGetCashMoneyBalance;

import java.util.UUID;

/**
 * Created by francisco on 17/10/15.
 */
public class implementCashMoneyBalanceRecord implements CashMoneyBalanceRecord {

    PluginDatabaseSystem pluginDatabaseSystem;

    private double amountBalance;

    CashMoneyWalletDao cashMoneyWalletDao = new CashMoneyWalletDao(pluginDatabaseSystem);
    CashMoney cashMoney = new CashMoney();

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
        try {
            if (cashMoneyWalletDao.getCashMoneyBalance().size() !=0 ){
                cashMoneyWalletDao.getCashMoneyBalance();
                amountBalance = Double.parseDouble(cashMoney.getGetAmount());
            }
            else  {
                amountBalance=0;
            }
        } catch (CantGetCashMoneyBalance cantGetCashMoneyBalance) {
            cantGetCashMoneyBalance.printStackTrace();
        }

        return amountBalance;
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
    public String getMemo() {
        return null;
    }
}
