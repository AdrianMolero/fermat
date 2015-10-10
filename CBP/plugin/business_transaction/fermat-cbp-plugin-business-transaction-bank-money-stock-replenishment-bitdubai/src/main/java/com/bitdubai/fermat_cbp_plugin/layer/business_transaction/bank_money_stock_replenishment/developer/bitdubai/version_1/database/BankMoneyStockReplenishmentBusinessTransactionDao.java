package com.bitdubai.fermat_cbp_plugin.layer.business_transaction.bank_money_stock_replenishment.developer.bitdubai.version_1.database;

import com.bitdubai.fermat_api.FermatException;
//import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_cbp_api.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantUpdateRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantInsertRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_cbp_api.all_definition.enums.BusinessTransactionStatus;
import com.bitdubai.fermat_cbp_api.all_definition.enums.BankCurrencyType;
import com.bitdubai.fermat_cbp_api.all_definition.enums.BankOperationType;
import com.bitdubai.fermat_cbp_api.all_definition.enums.CurrencyType;
import com.bitdubai.fermat_cbp_api.layer.cbp_business_transaction.bank_money_stock_replenishment.interfaces.BankMoneyStockReplenishment;
import com.bitdubai.fermat_cbp_plugin.layer.business_transaction.bank_money_stock_replenishment.developer.bitdubai.version_1.util.BankMoneyStockReplenishmentBusinessTransactionWrapper;
import com.bitdubai.fermat_cbp_plugin.layer.business_transaction.bank_money_stock_replenishment.developer.bitdubai.version_1.exceptions.CantInitializeBankMoneyStockReplenishmentBusinessTransactionDatabaseException;
import com.bitdubai.fermat_cbp_plugin.layer.business_transaction.bank_money_stock_replenishment.developer.bitdubai.version_1.exceptions.CantInsertRecordBankMoneyStockReplenishmentBusinessTransactionException;
import com.bitdubai.fermat_cbp_plugin.layer.business_transaction.bank_money_stock_replenishment.developer.bitdubai.version_1.exceptions.BankMoneyStockReplenishmentBusinessTransactionInconsistentTableStateException;
import com.bitdubai.fermat_cbp_plugin.layer.business_transaction.bank_money_stock_replenishment.developer.bitdubai.version_1.exceptions.CantUpdateStatusBankMoneyStockReplenishmentBusinessTransactionException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Yordin Alayn on 27.09.15.
 */
public class BankMoneyStockReplenishmentBusinessTransactionDao{
    /**
     * CryptoAddressBook Interface member variables.
     */
    private Database database;
    /**
     * DealsWithDatabaseSystem Interface member variables.
     */
    PluginDatabaseSystem pluginDatabaseSystem;
    /**
     * DealsWithPluginIdentity Interface member variables.
     */
    private UUID pluginId;
    /**
     * Constructor.
     */
    public BankMoneyStockReplenishmentBusinessTransactionDao(PluginDatabaseSystem pluginDatabaseSystem, UUID pluginId) {
        this.pluginId = pluginId;
        this.pluginDatabaseSystem = pluginDatabaseSystem;
    }

    /**
     * BankMoneyStockReplenishment Interface implementation.
     */
    public void initialize() throws CantInitializeBankMoneyStockReplenishmentBusinessTransactionDatabaseException {
        try {
            database = this.pluginDatabaseSystem.openDatabase(this.pluginId, this.pluginId.toString());
        } catch (DatabaseNotFoundException e) {
            try {
                BankMoneyStockReplenishmentBusinessTransactionDatabaseFactory databaseFactory = new BankMoneyStockReplenishmentBusinessTransactionDatabaseFactory(pluginDatabaseSystem);
                database = databaseFactory.createDatabase(pluginId, pluginId.toString());
            } catch (CantCreateDatabaseException f) {
                throw new CantInitializeBankMoneyStockReplenishmentBusinessTransactionDatabaseException(CantCreateDatabaseException.DEFAULT_MESSAGE, f, "", "There is a problem and i cannot create the database.");
            } catch (Exception z) {
                throw new CantInitializeBankMoneyStockReplenishmentBusinessTransactionDatabaseException(CantOpenDatabaseException.DEFAULT_MESSAGE, z, "", "Generic Exception.");
            }
        } catch (CantOpenDatabaseException e) {
            throw new CantInitializeBankMoneyStockReplenishmentBusinessTransactionDatabaseException(CantOpenDatabaseException.DEFAULT_MESSAGE, e, "", "Exception not handled by the plugin, there is a problem and i cannot open the database.");
        } catch (Exception e) {
            throw new CantInitializeBankMoneyStockReplenishmentBusinessTransactionDatabaseException(CantOpenDatabaseException.DEFAULT_MESSAGE, e, "", "Generic Exception.");
        }
    }

    public void createNewBankMoneyStockReplenishment(
            String publicKeyBroker,
            String merchandiseCurrency,
            String merchandiseAmount,
            String executionTransactionId,
            BankCurrencyType bankCurrencyType,
            BankOperationType bankOperationType
    ) throws CantInsertRecordBankMoneyStockReplenishmentBusinessTransactionException {
        try {
            DatabaseTable transactionTable = this.database.getTable(BankMoneyStockReplenishmentBusinessTransactionDatabaseConstants.BANK_MONEY_STOCK_REPLENISHMENT_TABLE_NAME);
            DatabaseTableRecord recordToInsert   = transactionTable.getEmptyRecord();
            BusinessTransactionStatus transactionStatus = BusinessTransactionStatus.PENDING_PAYMENT;
            loadRecordAsNew(recordToInsert, transactionStatus,publicKeyBroker, merchandiseCurrency, merchandiseAmount, executionTransactionId, bankCurrencyType, bankOperationType);
            transactionTable.insertRecord(recordToInsert);
        } catch (CantInsertRecordException e) {
            throw new CantInsertRecordBankMoneyStockReplenishmentBusinessTransactionException("An exception happened", e, "", "");
        } catch (Exception exception) {
            throw new CantInsertRecordBankMoneyStockReplenishmentBusinessTransactionException(CantInsertRecordBankMoneyStockReplenishmentBusinessTransactionException.DEFAULT_MESSAGE, FermatException.wrapException(exception), null, null);
        }
    }

    public void updateStatusBankMoneyStockReplenishment(BankMoneyStockReplenishmentBusinessTransactionWrapper businessTransaction, BusinessTransactionStatus transactionStatus) throws CantUpdateStatusBankMoneyStockReplenishmentBusinessTransactionException {
        try {
            setToState(businessTransaction, transactionStatus);
        } catch (CantUpdateRecordException | CantLoadTableToMemoryException exception) {
            throw new CantUpdateStatusBankMoneyStockReplenishmentBusinessTransactionException("An exception happened",exception,"","");
        } catch (Exception exception) {
            throw new CantUpdateStatusBankMoneyStockReplenishmentBusinessTransactionException("An unexpected exception happened", FermatException.wrapException(exception), null, null);
        }
    }

    public List<BankMoneyStockReplenishment> getAllBankMoneyStockReplenishmentFromCurrentDeviceUser() throws CantLoadTableToMemoryException, InvalidParameterException {
        DatabaseTable identityTable = this.database.getTable(BankMoneyStockReplenishmentBusinessTransactionDatabaseConstants.BANK_MONEY_STOCK_REPLENISHMENT_TABLE_NAME);
        identityTable.loadToMemory();

        List<DatabaseTableRecord> records = identityTable.getRecords();
        identityTable.clearAllFilters();

        List<BankMoneyStockReplenishment> bankMoneyStockReplenishment = new ArrayList<>();

        for (DatabaseTableRecord record : records) {
            bankMoneyStockReplenishment.add(constructBankMoneyStockReplenishmentFromRecord(record));
        }

        return bankMoneyStockReplenishment;
    }


    private void loadRecordAsNew(
        DatabaseTableRecord databaseTableRecord,
        BusinessTransactionStatus transactionStatus,
        String publicKeyBroker,
        String merchandiseCurrency,
        String merchandiseAmount,
        String executionTransactionId,
        BankCurrencyType bankCurrencyType,
        BankOperationType bankOperationType
    ) {
        UUID transactionId = UUID.randomUUID();

        databaseTableRecord.setUUIDValue(BankMoneyStockReplenishmentBusinessTransactionDatabaseConstants.BANK_MONEY_STOCK_REPLENISHMENT_TRANSACTION_ID_COLUMN_NAME, transactionId);
        databaseTableRecord.setStringValue(BankMoneyStockReplenishmentBusinessTransactionDatabaseConstants.BANK_MONEY_STOCK_REPLENISHMENT_STATUS_COLUMN_NAME, transactionStatus.getCode());
        databaseTableRecord.setStringValue(BankMoneyStockReplenishmentBusinessTransactionDatabaseConstants.BANK_MONEY_STOCK_REPLENISHMENT_PUBLIC_KEY_BROKER_COLUMN_NAME, publicKeyBroker);
        databaseTableRecord.setStringValue(BankMoneyStockReplenishmentBusinessTransactionDatabaseConstants.BANK_MONEY_STOCK_REPLENISHMENT_MERCHANDISE_CURRENCY_COLUMN_NAME, merchandiseCurrency);
        databaseTableRecord.setStringValue(BankMoneyStockReplenishmentBusinessTransactionDatabaseConstants.BANK_MONEY_STOCK_REPLENISHMENT_MERCHANDISE_CURRENCY_COLUMN_NAME, merchandiseAmount);
        databaseTableRecord.setStringValue(BankMoneyStockReplenishmentBusinessTransactionDatabaseConstants.BANK_MONEY_STOCK_REPLENISHMENT_EXECUTION_TRANSACTION_ID_COLUMN_NAME, executionTransactionId);
        databaseTableRecord.setStringValue(BankMoneyStockReplenishmentBusinessTransactionDatabaseConstants.BANK_MONEY_STOCK_REPLENISHMENT_BANK_CURRENCY_TYPE_COLUMN_NAME,bankCurrencyType.getCode());
        databaseTableRecord.setStringValue(BankMoneyStockReplenishmentBusinessTransactionDatabaseConstants.BANK_MONEY_STOCK_REPLENISHMENT_BANK_OPERATION_TYPE_COLUMN_NAME,bankOperationType.getCode());

    }

    private void setToState(BankMoneyStockReplenishmentBusinessTransactionWrapper businessTransaction, BusinessTransactionStatus status) throws CantUpdateRecordException, BankMoneyStockReplenishmentBusinessTransactionInconsistentTableStateException, CantLoadTableToMemoryException {
        DatabaseTable       transactionTable = this.database.getTable(BankMoneyStockReplenishmentBusinessTransactionDatabaseConstants.BANK_MONEY_STOCK_REPLENISHMENT_TABLE_NAME);
        DatabaseTableRecord recordToUpdate   = getByPrimaryKey(businessTransaction.getTransactionId());
        recordToUpdate.setStringValue(BankMoneyStockReplenishmentBusinessTransactionDatabaseConstants.BANK_MONEY_STOCK_REPLENISHMENT_STATUS_COLUMN_NAME, status.getCode());
        transactionTable.updateRecord(recordToUpdate);
    }

    private DatabaseTableRecord getByPrimaryKey(UUID id) throws CantLoadTableToMemoryException, BankMoneyStockReplenishmentBusinessTransactionInconsistentTableStateException {
        DatabaseTable transactionTable = this.database.getTable(BankMoneyStockReplenishmentBusinessTransactionDatabaseConstants.BANK_MONEY_STOCK_REPLENISHMENT_TABLE_NAME);
        List<DatabaseTableRecord> records;

        transactionTable.setStringFilter(BankMoneyStockReplenishmentBusinessTransactionDatabaseConstants.BANK_MONEY_STOCK_REPLENISHMENT_TRANSACTION_ID_COLUMN_NAME, id.toString(), DatabaseFilterType.EQUAL);
        transactionTable.loadToMemory();
        records = transactionTable.getRecords();

        if (records.size() != 1)
            throw new BankMoneyStockReplenishmentBusinessTransactionInconsistentTableStateException("The number of records with a primary key is different thatn one ", null, "The id is: " + id.toString(), "");

        return records.get(0);
    }

    private BankMoneyStockReplenishment constructBankMoneyStockReplenishmentFromRecord(DatabaseTableRecord record) throws InvalidParameterException {

        UUID                        transactionId           = record.getUUIDValue(BankMoneyStockReplenishmentBusinessTransactionDatabaseConstants.BANK_MONEY_STOCK_REPLENISHMENT_TRANSACTION_ID_COLUMN_NAME);
        String                      brokerPublicKey         = record.getStringValue(BankMoneyStockReplenishmentBusinessTransactionDatabaseConstants.BANK_MONEY_STOCK_REPLENISHMENT_PUBLIC_KEY_BROKER_COLUMN_NAME);
        CurrencyType                merchandiseCurrency     = CurrencyType.getByCode(record.getStringValue(BankMoneyStockReplenishmentBusinessTransactionDatabaseConstants.BANK_MONEY_STOCK_REPLENISHMENT_MERCHANDISE_CURRENCY_COLUMN_NAME));
        float                       merchandiseAmount       = record.getFloatValue(record.getStringValue(BankMoneyStockReplenishmentBusinessTransactionDatabaseConstants.BANK_MONEY_STOCK_REPLENISHMENT_MERCHANDISE_AMOUNT_COLUMN_NAME));
        UUID                        executionTransactionId  = record.getUUIDValue(BankMoneyStockReplenishmentBusinessTransactionDatabaseConstants.BANK_MONEY_STOCK_REPLENISHMENT_EXECUTION_TRANSACTION_ID_COLUMN_NAME);
        BankCurrencyType            bankCurrencyType        = BankCurrencyType.getByCode(record.getStringValue(BankMoneyStockReplenishmentBusinessTransactionDatabaseConstants.BANK_MONEY_STOCK_REPLENISHMENT_BANK_CURRENCY_TYPE_COLUMN_NAME));
        BankOperationType           bankOperationType       = BankOperationType.getByCode(record.getStringValue(BankMoneyStockReplenishmentBusinessTransactionDatabaseConstants.BANK_MONEY_STOCK_REPLENISHMENT_BANK_OPERATION_TYPE_COLUMN_NAME));
        BusinessTransactionStatus   status                  = BusinessTransactionStatus.getByCode(record.getStringValue(BankMoneyStockReplenishmentBusinessTransactionDatabaseConstants.BANK_MONEY_STOCK_REPLENISHMENT_STATUS_COLUMN_NAME));

        return new BankMoneyStockReplenishmentBusinessTransactionWrapper(
            transactionId,
            brokerPublicKey,
            merchandiseCurrency,
            merchandiseAmount,
            executionTransactionId,
            bankCurrencyType,
            bankOperationType,
            status
        );
    }

}
