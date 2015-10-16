package com.bitdubai.fermat_csh_plugin.layer.wallet.cash_money.developer.bitdubai.version_1.database;

import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantInsertRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_csh_api.layer.csh_wallet.cash_money.interfaces.CashMoneyBalanceRecord;
import com.bitdubai.fermat_csh_plugin.layer.wallet.cash_money.developer.bitdubai.version_1.exceptions.CantAddCashMoney;
import com.bitdubai.fermat_csh_plugin.layer.wallet.cash_money.developer.bitdubai.version_1.exceptions.CantAddCashMoneyBalance;
import com.bitdubai.fermat_csh_plugin.layer.wallet.cash_money.developer.bitdubai.version_1.exceptions.CantGetCashMoneyRecord;
import com.bitdubai.fermat_csh_plugin.layer.wallet.cash_money.developer.bitdubai.version_1.exceptions.CantInitializeCashMoneyWalletDatabaseException;
import com.bitdubai.fermat_csh_plugin.layer.wallet.cash_money.developer.bitdubai.version_1.structure.CashMoney;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by yordin on 01/10/15.
 */
public class CashMoneyWalletDao {
    /**
     *  Represent the Plugin Database.
     */
    private PluginDatabaseSystem pluginDatabaseSystem;

    /**
     * Database connection
     */
    Database database;

    public CashMoneyWalletDao (PluginDatabaseSystem pluginDatabaseSystem){
        this.pluginDatabaseSystem = pluginDatabaseSystem;
    }

    /**
     *
     * @param ownerId
     * @param databaseName
     * @throws CantInitializeCashMoneyWalletDatabaseException
     */
    public void initializeDatabase(UUID ownerId, String databaseName) throws CantInitializeCashMoneyWalletDatabaseException {
        try {
             /*
              * Open new database connection
              */
            database = this.pluginDatabaseSystem.openDatabase(ownerId, databaseName);
            database.closeDatabase();
        } catch (CantOpenDatabaseException cantOpenDatabaseException) {
            throw new CantInitializeCashMoneyWalletDatabaseException(
                    CantInitializeCashMoneyWalletDatabaseException.DEFAULT_MESSAGE,
                    cantOpenDatabaseException,
                    "initializeDatabase",
                    "Cant Initialize CashMoney WalletDatabase Exception - Cant Open Database Exception");
        } catch (DatabaseNotFoundException e) {
             /*
              * The database no exist may be the first time the plugin is running on this device,
              * We need to create the new database
              */
            CashMoneyWalletDatabaseFactory cashMoneyWalletDatabaseFactory = new CashMoneyWalletDatabaseFactory(pluginDatabaseSystem);
            try {
                  /*
                   * We create the new database
                   */
                database = cashMoneyWalletDatabaseFactory.createDatabase(ownerId, databaseName);
                database.closeDatabase();
            } catch (CantCreateDatabaseException cantCreateDatabaseException) {
                  /*
                   * The database cannot be created. I can not handle this situation.
                   */
                throw new CantInitializeCashMoneyWalletDatabaseException(
                        CantInitializeCashMoneyWalletDatabaseException.DEFAULT_MESSAGE,
                        cantCreateDatabaseException,
                        "initializeDatabase",
                        "Cant Initialize CashMoney WalletDatabase Exception - Cant Create Database Exception");
            }
        }
    }
    public void addCashMoney(  String cash_transaction_id,
                               String public_key_customer,
                                       String public_key_broker,
                                       String balance_type,
                                       String transaction_type,
                                       String amount,
                                       String cash_currency_type,
                                       String cash_reference,
                                       String running_book_balance,
                                       String running_available_balance,
                                       String status) throws CantAddCashMoney{
        try {
        DatabaseTable table = this.database.getTable(CashMoneyWalletDatabaseConstants.CASH_MONEY_TABLE_NAME);
        DatabaseTableRecord record =  table.getEmptyRecord();

        record.setStringValue(CashMoneyWalletDatabaseConstants.CASH_MONEY_CASH_TRANSACTION_ID_COLUMN_NAME, cash_transaction_id);
        record.setStringValue(CashMoneyWalletDatabaseConstants.CASH_MONEY_PUBLIC_KEY_CUSTOMER_COLUMN_NAME,public_key_customer);
        record.setStringValue(CashMoneyWalletDatabaseConstants.CASH_MONEY_PUBLIC_KEY_BROKER_COLUMN_NAME, public_key_broker);
        record.setStringValue(CashMoneyWalletDatabaseConstants.CASH_MONEY_BALANCE_TYPE_COLUMN_NAME, balance_type);
        record.setStringValue(CashMoneyWalletDatabaseConstants.CASH_MONEY_TRANSACTION_TYPE_COLUMN_NAME, transaction_type);
        record.setStringValue(CashMoneyWalletDatabaseConstants.CASH_MONEY_AMOUNT_COLUMN_NAME,amount);
        record.setStringValue(CashMoneyWalletDatabaseConstants.CASH_MONEY_CASH_CURRENCY_TYPE_COLUMN_NAME,cash_currency_type);
        record.setStringValue(CashMoneyWalletDatabaseConstants.CASH_MONEY_CASH_REFERENCE_COLUMN_NAME,cash_reference);
        record.setStringValue(CashMoneyWalletDatabaseConstants.CASH_MONEY_RUNNING_BOOK_BALANCE_COLUMN_NAME,running_book_balance);
        record.setStringValue(CashMoneyWalletDatabaseConstants.CASH_MONEY_RUNNING_AVAILABLE_BALANCE_COLUMN_NAME,running_available_balance);
        record.setStringValue(CashMoneyWalletDatabaseConstants.CASH_MONEY_STATUS_COLUMN_NAME,status);


            table.insertRecord(record);
            database.closeDatabase();
        } catch (CantInsertRecordException cantInsertRecordException) {
            throw new CantAddCashMoney(CantAddCashMoney.DEFAULT_MESSAGE,cantInsertRecordException,"Cant Add Cash Money","Cant Insert Record Exception");
        }
    }
    public void addCashMoneyBalance( String cash_transaction_id, String cashMoneyDebit, String cashMoneyCredit, String cashMoneyBalance) throws CantAddCashMoneyBalance{
    try{
        DatabaseTable table = this.database.getTable(CashMoneyWalletDatabaseConstants.CASH_MONEY_BALANCE_TABLE_NAME);
        DatabaseTableRecord record =  table.getEmptyRecord();

        record.setStringValue(CashMoneyWalletDatabaseConstants.CASH_MONEY_CASH_TRANSACTION_ID_COLUMN_NAME, cash_transaction_id);
        record.setStringValue(CashMoneyWalletDatabaseConstants.CASH_MONEY_BALANCE_DEBIT_COLUMN_NAME, cashMoneyDebit);
        record.setStringValue(CashMoneyWalletDatabaseConstants.CASH_MONEY_BALANCE_CREDIT_COLUMN_NAME,cashMoneyCredit);
        record.setStringValue(CashMoneyWalletDatabaseConstants.CASH_MONEY_BALANCE_BALANCE_COLUMN_NAME,cashMoneyBalance);

        table.insertRecord(record);
        database.closeDatabase();
    } catch (CantInsertRecordException cantInsertRecordException) {
        throw new CantAddCashMoneyBalance(CantAddCashMoneyBalance.DEFAULT_MESSAGE,cantInsertRecordException,"Cant Add Cash Money Balance","Cant Insert Record Exception");
    }
    }
    public List<CashMoneyBalanceRecord> getCashMoneyRecord() throws CantGetCashMoneyRecord {

        List<CashMoneyBalanceRecord> list = new ArrayList<>();
        DatabaseTable table;

            table=this.database.getTable(CashMoneyWalletDatabaseConstants.CASH_MONEY_TABLE_NAME);
            if (table==null){
                throw new CantGetCashMoneyRecord(CantGetCashMoneyRecord.DEFAULT_MESSAGE,null,"Cant GetCash Money Record","Table is null");
            }
        try {
            table.loadToMemory();

        for (DatabaseTableRecord record : table.getRecords()){
                list.add(new CashMoney(
                        record.getStringValue(CashMoneyWalletDatabaseConstants.CASH_MONEY_CASH_TRANSACTION_ID_COLUMN_NAME),
                        record.getStringValue(CashMoneyWalletDatabaseConstants.CASH_MONEY_PUBLIC_KEY_CUSTOMER_COLUMN_NAME),
                        record.getStringValue(CashMoneyWalletDatabaseConstants.CASH_MONEY_PUBLIC_KEY_BROKER_COLUMN_NAME),
                        record.getStringValue(CashMoneyWalletDatabaseConstants.CASH_MONEY_BALANCE_TYPE_COLUMN_NAME),
                        record.getStringValue(CashMoneyWalletDatabaseConstants.CASH_MONEY_TRANSACTION_TYPE_COLUMN_NAME),
                        record.getStringValue(CashMoneyWalletDatabaseConstants.CASH_MONEY_AMOUNT_COLUMN_NAME),
                        record.getStringValue(CashMoneyWalletDatabaseConstants.CASH_MONEY_CASH_CURRENCY_TYPE_COLUMN_NAME),
                        record.getStringValue(CashMoneyWalletDatabaseConstants.CASH_MONEY_CASH_REFERENCE_COLUMN_NAME),
                        record.getStringValue(CashMoneyWalletDatabaseConstants.CASH_MONEY_RUNNING_BOOK_BALANCE_COLUMN_NAME),
                        record.getStringValue(CashMoneyWalletDatabaseConstants.CASH_MONEY_RUNNING_AVAILABLE_BALANCE_COLUMN_NAME),
                        record.getStringValue(CashMoneyWalletDatabaseConstants.CASH_MONEY_STATUS_COLUMN_NAME)));
            }
            database.closeDatabase();
        } catch (CantLoadTableToMemoryException cantLoadTableToMemoryException) {
            throw new CantGetCashMoneyRecord(CantGetCashMoneyRecord.DEFAULT_MESSAGE, cantLoadTableToMemoryException, "Cant Get Cash Money Record","Cant Load Table To Memory Exception");
        }

        return list;
    }

}
