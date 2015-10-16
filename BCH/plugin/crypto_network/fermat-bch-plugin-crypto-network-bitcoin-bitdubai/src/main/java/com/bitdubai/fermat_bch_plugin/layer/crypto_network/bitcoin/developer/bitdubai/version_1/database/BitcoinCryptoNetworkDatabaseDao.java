package com.bitdubai.fermat_bch_plugin.layer.crypto_network.bitcoin.developer.bitdubai.version_1.database;

import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrency;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.all_definition.money.CryptoAddress;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.Action;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.ProtocolStatus;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.crypto_transactions.CryptoStatus;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.crypto_transactions.CryptoTransaction;
import com.bitdubai.fermat_api.layer.all_definition.util.XMLParser;

import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterOperator;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterOrder;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFilter;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFilterGroup;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantInsertRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantUpdateRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_bch_api.layer.crypto_network.enums.TransactionTypes;
import com.bitdubai.fermat_bch_api.layer.crypto_vault.CryptoVaults;
import com.bitdubai.fermat_bch_plugin.layer.crypto_network.bitcoin.developer.bitdubai.version_1.exceptions.CantExecuteDatabaseOperationException;
import com.bitdubai.fermat_bch_plugin.layer.crypto_network.bitcoin.developer.bitdubai.version_1.exceptions.CantInitializeBitcoinCryptoNetworkDatabaseException;
import com.bitdubai.fermat_bch_plugin.layer.crypto_network.bitcoin.developer.bitdubai.version_1.util.TransactionProtocolData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by rodrigo on 10/9/15.
 */
public class BitcoinCryptoNetworkDatabaseDao {
    /**
     * Class variables
     */
    Database database;

    /**
     * platform variables
     */
    UUID pluginId;
    PluginDatabaseSystem pluginDatabaseSystem;

    /**
     * Constructor
     */
    public BitcoinCryptoNetworkDatabaseDao(UUID pluginId, PluginDatabaseSystem pluginDatabaseSystem) {
        this.pluginId = pluginId;
        this.pluginDatabaseSystem = pluginDatabaseSystem;


        try {
            initializeDatabase();
        } catch (CantInitializeBitcoinCryptoNetworkDatabaseException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method open or creates the database i'll be working with
     *
     * @throws CantInitializeBitcoinCryptoNetworkDatabaseException
     */
    public void initializeDatabase() throws CantInitializeBitcoinCryptoNetworkDatabaseException {
        try {

             /*
              * Open new database connection
              */
            database = this.pluginDatabaseSystem.openDatabase(pluginId, pluginId.toString());

        } catch (CantOpenDatabaseException cantOpenDatabaseException) {

             /*
              * The database exists but cannot be open. I can not handle this situation.
              */
            throw new CantInitializeBitcoinCryptoNetworkDatabaseException(cantOpenDatabaseException.getMessage());

        } catch (DatabaseNotFoundException e) {

             /*
              * The database no exist may be the first time the plugin is running on this device,
              * We need to create the new database
              */
            BitcoinCryptoNetworkDatabaseFactory bitcoinCryptoNetworkDatabaseFactory = new BitcoinCryptoNetworkDatabaseFactory(pluginDatabaseSystem);

            try {
                  /*
                   * We create the new database
                   */
                database = bitcoinCryptoNetworkDatabaseFactory.createDatabase(pluginId, pluginId.toString());
            } catch (CantCreateDatabaseException cantCreateDatabaseException) {
                  /*
                   * The database cannot be created. I can not handle this situation.
                   */
                throw new CantInitializeBitcoinCryptoNetworkDatabaseException(cantCreateDatabaseException.getMessage());
            }
        }
    }

    /**
     * Update the crypto vault statistics when called to start monitoring the network.
     * @param cryptoVault
     * @param monitoredPublicKeys
     * @throws CantExecuteDatabaseOperationException
     */
    public void updateCryptoVaultsStatistics(CryptoVaults cryptoVault, int monitoredPublicKeys) throws CantExecuteDatabaseOperationException {
        DatabaseTable databaseTable = database.getTable(BitcoinCryptoNetworkDatabaseConstants.CRYPTOVAULTS_STATS_TABLE_NAME);
        databaseTable.setStringFilter(BitcoinCryptoNetworkDatabaseConstants.CRYPTOVAULTS_STATS_CRYPTO_VAULT_COLUMN_NAME, cryptoVault.getCode(), DatabaseFilterType.EQUAL);
        /**
         * I will check if I have the record to update it
         */
        try {
            databaseTable.loadToMemory();
        } catch (CantLoadTableToMemoryException e) {
            throw new CantExecuteDatabaseOperationException(
                    CantExecuteDatabaseOperationException.DEFAULT_MESSAGE,
                    e,
                    "trying to load table " + databaseTable.getTableName() + " into memory",
                    null);
        }

        DatabaseTableRecord record = null;
        try{
            if (databaseTable.getRecords().size() == 0){
                /**
                 * I will insert a new record
                 */
                record = databaseTable.getEmptyRecord();
                record.setStringValue(BitcoinCryptoNetworkDatabaseConstants.CRYPTOVAULTS_STATS_CRYPTO_VAULT_COLUMN_NAME, cryptoVault.getCode());
                record.setStringValue(BitcoinCryptoNetworkDatabaseConstants.CRYPTOVAULTS_STATS_LAST_CONNECTION_REQUEST_COLUMN_NAME, getCurrentDateTime());
                record.setIntegerValue(BitcoinCryptoNetworkDatabaseConstants.CRYPTOVAULTS_STATS_MONITORED_PUBLICKEYS_COLUMN_NAME, monitoredPublicKeys);

                databaseTable.insertRecord(record);
            } else {
                /**
                 * I will update an existing record.
                 */
                record = databaseTable.getRecords().get(0);
                record.setStringValue(BitcoinCryptoNetworkDatabaseConstants.CRYPTOVAULTS_STATS_LAST_CONNECTION_REQUEST_COLUMN_NAME, getCurrentDateTime());
                record.setIntegerValue(BitcoinCryptoNetworkDatabaseConstants.CRYPTOVAULTS_STATS_MONITORED_PUBLICKEYS_COLUMN_NAME, monitoredPublicKeys);

                databaseTable.updateRecord(record);
            }
        } catch (CantInsertRecordException | CantUpdateRecordException e) {
            StringBuilder outputMessage = new StringBuilder("There was an error inserting or updating a record in table ");
            outputMessage.append(databaseTable.getTableName());
            outputMessage.append("Record is:");
            outputMessage.append(System.lineSeparator());
            outputMessage.append(XMLParser.parseObject(record));

            throw new CantExecuteDatabaseOperationException(CantExecuteDatabaseOperationException.DEFAULT_MESSAGE, e, outputMessage.toString(), "database issue.");
        }

    }

    /**
     * saves a new Crypto transaction into database
     * @param hash
     * @param cryptoStatus
     * @param blockDepth
     * @param addressTo
     * @param addressFrom
     * @param value
     * @param fee
     * @param protocolStatus
     * @throws CantExecuteDatabaseOperationException
     */
    public void saveNewIncomingTransaction(String hash, CryptoStatus cryptoStatus, int blockDepth, CryptoAddress addressTo, CryptoAddress addressFrom, long value, long fee, ProtocolStatus protocolStatus) throws CantExecuteDatabaseOperationException{
        DatabaseTable databaseTable = database.getTable(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_TABLE_NAME);
        DatabaseTableRecord record = databaseTable.getEmptyRecord();

        /**
         * generates the trx_id
         */
        UUID trxId = UUID.randomUUID();

        record.setUUIDValue(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_TRX_ID_COLUMN_NAME, trxId);
        record.setStringValue(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_HASH_COLUMN_NAME, hash);
        record.setStringValue(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_CRYPTO_STATUS_COLUMN_NAME, cryptoStatus.getCode());
        record.setIntegerValue(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_BLOCK_DEPTH_COLUMN_NAME, blockDepth);
        record.setStringValue(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_ADDRESS_TO_COLUMN_NAME, addressTo.getAddress());
        record.setStringValue(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_ADDRESS_FROM_COLUMN_NAME, addressFrom.getAddress());
        record.setDoubleValue(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_VALUE_COLUMN_NAME, value);
        record.setDoubleValue(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_FEE_COLUMN_NAME, fee);
        record.setStringValue(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_PROTOCOL_STATUS_COLUMN_NAME, protocolStatus.getCode());
        record.setStringValue(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_LAST_UPDATE_COLUMN_NAME, getCurrentDateTime());
        try {
            databaseTable.insertRecord(record);
        } catch (CantInsertRecordException e) {
            StringBuilder outputMessage = new StringBuilder("There was an error inserting a new transaction. Transaction record is:");
            outputMessage.append(System.lineSeparator());
            outputMessage.append(XMLParser.parseObject(record));

            throw new CantExecuteDatabaseOperationException (CantExecuteDatabaseOperationException.DEFAULT_MESSAGE, e, outputMessage.toString(), "database issue.");
        }
    }

    public CryptoStatus getTransactionCryptoStatus(String txHash) throws CantExecuteDatabaseOperationException{

        return null;
    }

    /**
     * gets the current date and time to save in a database
     * @return
     */
    private String getCurrentDateTime(){
        String date = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()).toString();
        return date;
    }

    public void saveNewOutgoingTransaction(String hash, CryptoStatus cryptoStatus, int blockDepth, CryptoAddress addressTo, CryptoAddress addressFrom, long value, long fee, ProtocolStatus protocolStatus) throws CantExecuteDatabaseOperationException{
        DatabaseTable databaseTable = database.getTable(BitcoinCryptoNetworkDatabaseConstants.OUTGOING_TRANSACTIONS_TABLE_NAME);
        DatabaseTableRecord record = databaseTable.getEmptyRecord();

        /**
         * generates the trx_id
         */
        UUID trxId = UUID.randomUUID();

        record.setUUIDValue(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_TRX_ID_COLUMN_NAME, trxId);
        record.setStringValue(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_HASH_COLUMN_NAME, hash);
        record.setStringValue(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_CRYPTO_STATUS_COLUMN_NAME, cryptoStatus.getCode());
        record.setIntegerValue(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_BLOCK_DEPTH_COLUMN_NAME, blockDepth);
        record.setStringValue(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_ADDRESS_TO_COLUMN_NAME, addressTo.getAddress());
        record.setStringValue(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_ADDRESS_FROM_COLUMN_NAME, addressFrom.getAddress());
        record.setDoubleValue(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_VALUE_COLUMN_NAME, value);
        record.setDoubleValue(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_FEE_COLUMN_NAME, fee);
        record.setStringValue(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_PROTOCOL_STATUS_COLUMN_NAME, protocolStatus.getCode());
        record.setStringValue(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_LAST_UPDATE_COLUMN_NAME, getCurrentDateTime());
        try {
            databaseTable.insertRecord(record);
        } catch (CantInsertRecordException e) {
            StringBuilder outputMessage = new StringBuilder("There was an error inserting a new transaction in the Outgoing Transactions Table. Transaction record is:");
            outputMessage.append(System.lineSeparator());
            outputMessage.append(XMLParser.parseObject(record));

            throw new CantExecuteDatabaseOperationException (CantExecuteDatabaseOperationException.DEFAULT_MESSAGE, e, outputMessage.toString(), "database issue.");
        }
    }

    /**
     * If the transaction exists in the IncomingTransactions table it will return true, otherwise it will return false
     * @param txHash
     * @return
     */
    public boolean isIncomingTransaction(String txHash) throws CantExecuteDatabaseOperationException {
        DatabaseTable databaseTable = database.getTable(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_TABLE_NAME);
        databaseTable.setStringFilter(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_HASH_COLUMN_NAME, txHash, DatabaseFilterType.EQUAL);

        try {
            databaseTable.loadToMemory();
        } catch (CantLoadTableToMemoryException e) {
            throw new CantExecuteDatabaseOperationException(CantExecuteDatabaseOperationException.DEFAULT_MESSAGE, e, "Cant load IncomingTransaction table into memory", "database issue");
        }

        if (databaseTable.getRecords().size() > 0)
            return true;
        else
            return false;
    }

    /**
     * Will get the last current Crypto Status recorded from the given outgoing or incoming transaction
     * @param transactionType
     * @param txHash
     * @return
     */
    public CryptoStatus getStoredTransactionCryptoStatus(TransactionTypes transactionType, String txHash) throws CantExecuteDatabaseOperationException {
        /**
         * I will define the outgoing or incoming table, the filter and the sort order
         */
        DatabaseTable databaseTable = null;
        if (transactionType == TransactionTypes.OUTGOING){
            databaseTable = database.getTable(BitcoinCryptoNetworkDatabaseConstants.OUTGOING_TRANSACTIONS_TABLE_NAME);
            databaseTable.setStringFilter(BitcoinCryptoNetworkDatabaseConstants.OUTGOING_TRANSACTIONS_HASH_COLUMN_NAME, txHash, DatabaseFilterType.EQUAL);
            databaseTable.setFilterOrder(BitcoinCryptoNetworkDatabaseConstants.OUTGOING_TRANSACTIONS_LAST_UPDATE_COLUMN_NAME, DatabaseFilterOrder.DESCENDING);
        }
        else {
            databaseTable = database.getTable(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_TABLE_NAME);
            databaseTable.setStringFilter(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_HASH_COLUMN_NAME, txHash, DatabaseFilterType.EQUAL);
            databaseTable.setFilterOrder(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_LAST_UPDATE_COLUMN_NAME, DatabaseFilterOrder.DESCENDING);
        }

        /**
         * Wil load the table into memory
          */
        try {
            databaseTable.loadToMemory();
        } catch (CantLoadTableToMemoryException e) {
            throw new CantExecuteDatabaseOperationException (CantExecuteDatabaseOperationException.DEFAULT_MESSAGE, e, "Cant load transaction table into memory.", "database error");
        }

        /**
         * Since Im ordering by update date, I will only get the fist record retrieved to form the crypto status
         */
        DatabaseTableRecord record = databaseTable.getRecords().get(0);
       CryptoStatus cryptoStatus= null;

        /**
         * will get and form the crypto status
         */
        try{
            if (transactionType == TransactionTypes.OUTGOING)
                cryptoStatus = CryptoStatus.getByCode(record.getStringValue(BitcoinCryptoNetworkDatabaseConstants.OUTGOING_TRANSACTIONS_CRYPTO_STATUS_COLUMN_NAME));
            else
                cryptoStatus = CryptoStatus.getByCode(record.getStringValue(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_CRYPTO_STATUS_COLUMN_NAME));
        } catch (InvalidParameterException e) {
            throw new CantExecuteDatabaseOperationException (CantExecuteDatabaseOperationException.DEFAULT_MESSAGE, e, "Cant load the crypto status from the database.", "database error");
        }

        return cryptoStatus;
    }

    /**
     * Gets the amount of transaction in ProtocolStatus as Pending Notified from the specified table
     * @param transactionType
     * @return
     * @throws CantExecuteDatabaseOperationException
     */
    public int getPendingNotifiedTransactions(TransactionTypes transactionType) throws CantExecuteDatabaseOperationException{
        DatabaseTable databaseTable;
        if (transactionType == TransactionTypes.OUTGOING) {
            databaseTable = database.getTable(BitcoinCryptoNetworkDatabaseConstants.OUTGOING_TRANSACTIONS_TABLE_NAME);
            databaseTable.setStringFilter(BitcoinCryptoNetworkDatabaseConstants.OUTGOING_TRANSACTIONS_PROTOCOL_STATUS_COLUMN_NAME, ProtocolStatus.TO_BE_NOTIFIED.getCode(), DatabaseFilterType.EQUAL);
        } else {
            databaseTable = database.getTable(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_TABLE_NAME);
            databaseTable.setStringFilter(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_PROTOCOL_STATUS_COLUMN_NAME, ProtocolStatus.TO_BE_NOTIFIED.getCode(), DatabaseFilterType.EQUAL);
        }

        try {
            databaseTable.loadToMemory();
        } catch (CantLoadTableToMemoryException e) {
            throw new CantExecuteDatabaseOperationException(CantExecuteDatabaseOperationException.DEFAULT_MESSAGE, e, "there was an error loading the database table to get pending transactions", "database error.");
        }

        /**
         * I return the amount of records.
         */
        return databaseTable.getRecords().size();
    }

    /**
     * Updates the execution statistics of the event Agent
     * it will insert a new record if we have pending transactions, and update the existing value if we have zero transactions.
     * @param pendingIncoming
     * @param pendingOutgoing
     */
    public void updateEventAgentStats(int pendingIncoming, int pendingOutgoing) throws CantExecuteDatabaseOperationException{
        DatabaseTable databaseTable = database.getTable(BitcoinCryptoNetworkDatabaseConstants.EVENTAGENT_STATS_TABLE_NAME);

        /**
         * I will get the current execution number
         */
        int currentExecutionNumber;
        databaseTable.setFilterOrder(BitcoinCryptoNetworkDatabaseConstants.EVENTAGENT_STATS_EXECUTION_NUMBER_COLUMN_NAME, DatabaseFilterOrder.DESCENDING);
        try {
            databaseTable.loadToMemory();
        } catch (CantLoadTableToMemoryException e) {
            throw new CantExecuteDatabaseOperationException(CantExecuteDatabaseOperationException.DEFAULT_MESSAGE, e, "cant load EventAgent Stats table.", "database issue");
        }

        if (databaseTable.getRecords().size() != 0)
            currentExecutionNumber = databaseTable.getRecords().get(0).getIntegerValue(BitcoinCryptoNetworkDatabaseConstants.EVENTAGENT_STATS_EXECUTION_NUMBER_COLUMN_NAME);
        else
            currentExecutionNumber = 0;

        /**
         * if I have pending transactions, I will insert a new record, also if this is the first time I'm executing.
         */
        DatabaseTableRecord record = null;
        try{
            if (pendingIncoming != 0 || pendingOutgoing != 0 || currentExecutionNumber == 0){
                record = databaseTable.getEmptyRecord();
                record.setIntegerValue(BitcoinCryptoNetworkDatabaseConstants.EVENTAGENT_STATS_EXECUTION_NUMBER_COLUMN_NAME, currentExecutionNumber + 1);
                record.setStringValue(BitcoinCryptoNetworkDatabaseConstants.EVENTAGENT_STATS_LAST_EXECUTION_DATE_COLUMN_NAME, getCurrentDateTime());
                record.setIntegerValue(BitcoinCryptoNetworkDatabaseConstants.EVENTAGENT_STATS_PENDING_INCOMING_TRANSACTIONS_COLUMN_NAME, pendingIncoming);
                record.setIntegerValue(BitcoinCryptoNetworkDatabaseConstants.EVENTAGENT_STATS_PENDING_OUTGOING_TRANSACTIONS_COLUMN_NAME, pendingOutgoing);

                databaseTable.insertRecord(record);

            } else {
                /**
                 * I will update existing value with zero pending transactions
                 */
                DatabaseTableFilter filterIncoming = databaseTable.getEmptyTableFilter();
                filterIncoming.setColumn(BitcoinCryptoNetworkDatabaseConstants.EVENTAGENT_STATS_PENDING_INCOMING_TRANSACTIONS_COLUMN_NAME);
                filterIncoming.setValue("0");
                filterIncoming.setType(DatabaseFilterType.EQUAL);

                DatabaseTableFilter filterOutgoing = databaseTable.getEmptyTableFilter();
                filterOutgoing.setColumn(BitcoinCryptoNetworkDatabaseConstants.EVENTAGENT_STATS_PENDING_OUTGOING_TRANSACTIONS_COLUMN_NAME);
                filterOutgoing.setValue("0");
                filterOutgoing.setType(DatabaseFilterType.EQUAL);

                /**
                 * I create the two filters and add them to form a filter group.
                 */
                List<DatabaseTableFilter> filters = new ArrayList<>();
                filters.add(filterIncoming);
                filters.add(filterOutgoing);

                List<DatabaseTableFilterGroup> filterGroups = new ArrayList<>();
                databaseTable.setFilterGroup(databaseTable.getNewFilterGroup(filters, filterGroups, DatabaseFilterOperator.AND));
                databaseTable.loadToMemory();

                record = databaseTable.getRecords().get(0);
                record.setIntegerValue(BitcoinCryptoNetworkDatabaseConstants.EVENTAGENT_STATS_EXECUTION_NUMBER_COLUMN_NAME, currentExecutionNumber + 1);
                record.setStringValue(BitcoinCryptoNetworkDatabaseConstants.EVENTAGENT_STATS_LAST_EXECUTION_DATE_COLUMN_NAME, getCurrentDateTime());

                databaseTable.updateRecord(record);
            }
        } catch (CantLoadTableToMemoryException e) {
            throw new CantExecuteDatabaseOperationException(CantExecuteDatabaseOperationException.DEFAULT_MESSAGE, e, "cant load EventAgent Stats table.", "database issue");
        } catch (CantInsertRecordException | CantUpdateRecordException e) {
            StringBuilder outputMessage = new StringBuilder("There was an error inserting or modifying a record. The record is:");
            outputMessage.append(System.lineSeparator());
            outputMessage.append(XMLParser.parseObject(record));

            throw new CantExecuteDatabaseOperationException(CantExecuteDatabaseOperationException.DEFAULT_MESSAGE, e, outputMessage.toString(), "database issue");
        }
    }

    /**
     * Gets the crypto Status list that are in pending status from the specified table.
     * @param transactionType
     * @return
     */
    public Set<CryptoStatus> getPendingCryptoStatus(TransactionTypes transactionType) throws CantExecuteDatabaseOperationException {
        DatabaseTable databaseTable;
        String cryptoStatusColumnName;
        Set<CryptoStatus> cryptoStatuses = new HashSet<>();

        /**
         * Will set up filters and column names depending on the transaction type.
         */
        if (transactionType == TransactionTypes.OUTGOING){
            databaseTable = database.getTable(BitcoinCryptoNetworkDatabaseConstants.OUTGOING_TRANSACTIONS_TABLE_NAME);
            databaseTable.setStringFilter(BitcoinCryptoNetworkDatabaseConstants.OUTGOING_TRANSACTIONS_PROTOCOL_STATUS_COLUMN_NAME, ProtocolStatus.TO_BE_NOTIFIED.getCode(), DatabaseFilterType.EQUAL);
            cryptoStatusColumnName = BitcoinCryptoNetworkDatabaseConstants.OUTGOING_TRANSACTIONS_CRYPTO_STATUS_COLUMN_NAME;
        } else{
            databaseTable = database.getTable(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_TABLE_NAME);
            databaseTable.setStringFilter(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_PROTOCOL_STATUS_COLUMN_NAME, ProtocolStatus.TO_BE_NOTIFIED.getCode(), DatabaseFilterType.EQUAL);
            cryptoStatusColumnName = BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_CRYPTO_STATUS_COLUMN_NAME;
        }

        try {
            databaseTable.loadToMemory();
        } catch (CantLoadTableToMemoryException e) {
            throw new CantExecuteDatabaseOperationException(CantExecuteDatabaseOperationException.DEFAULT_MESSAGE, e, "error loading table into memory", "database error");
        }

        /**
         * get all the CryptoStatus and remove duplicates as Im storing them in a set.
         */
        for (DatabaseTableRecord record : databaseTable.getRecords()){
            try {
                CryptoStatus cryptoStatus = CryptoStatus.getByCode(record.getStringValue(cryptoStatusColumnName));
                cryptoStatuses.add(cryptoStatus);
            } catch (InvalidParameterException e) {
                e.printStackTrace();
            }
        }

        return cryptoStatuses;
    }

    /**
     * Confirms a transaction by marking it to No Action Required in is Protocol Status.
     * I will have to search this transaction in both incoming and outgoing tables
     * @param transactionID
     * @throws CantExecuteDatabaseOperationException
     */
    public void confirmReception(UUID transactionID) throws CantExecuteDatabaseOperationException{
        confirmIncomingTransactionReception(transactionID);
        confirmOutgoingTransactionReception(transactionID);
    }


    /**
     * Marks to RECEPTION_NOTIFIED the protocol status of this Incoming transaction if found.
     * @param transactionID
     */
    private void confirmIncomingTransactionReception(UUID transactionID) throws CantExecuteDatabaseOperationException {
        DatabaseTable databaseTable = database.getTable(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_TABLE_NAME);
        databaseTable.setStringFilter(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_TRX_ID_COLUMN_NAME, transactionID.toString(), DatabaseFilterType.EQUAL);

        try {
            databaseTable.loadToMemory();
        } catch (CantLoadTableToMemoryException e) {
            throw new CantExecuteDatabaseOperationException(CantExecuteDatabaseOperationException.DEFAULT_MESSAGE, e, "error loading table into memory", "database error");
        }

        /**
         * set the value to RECEPTION_NOTIFIED
         */
        if (databaseTable.getRecords().size() > 0){
            DatabaseTableRecord record = databaseTable.getRecords().get(0);
            record.setStringValue(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_PROTOCOL_STATUS_COLUMN_NAME, ProtocolStatus.RECEPTION_NOTIFIED.getCode());
            record.setStringValue(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_LAST_UPDATE_COLUMN_NAME, getCurrentDateTime());

            try {
                databaseTable.updateRecord(record);
            } catch (CantUpdateRecordException e) {
                StringBuilder errorOutput = new StringBuilder("There was a problem setting to RECEPTION_NOTIFIED the following transaction:");
                errorOutput.append(System.lineSeparator());
                errorOutput.append(XMLParser.parseObject(record));
                throw  new CantExecuteDatabaseOperationException(CantExecuteDatabaseOperationException.DEFAULT_MESSAGE, e, errorOutput.toString(), "database issue");
            }
        }

    }

    /**
     * Marks to RECEPTION_NOTIFIED the protocol status of this Outgoing transaction if found.
     * @param transactionID
     */
    private void confirmOutgoingTransactionReception(UUID transactionID) throws CantExecuteDatabaseOperationException {
        DatabaseTable databaseTable = database.getTable(BitcoinCryptoNetworkDatabaseConstants.OUTGOING_TRANSACTIONS_TABLE_NAME);
        databaseTable.setStringFilter(BitcoinCryptoNetworkDatabaseConstants.OUTGOING_TRANSACTIONS_TRX_ID_COLUMN_NAME, transactionID.toString(), DatabaseFilterType.EQUAL);

        try {
            databaseTable.loadToMemory();
        } catch (CantLoadTableToMemoryException e) {
            throw new CantExecuteDatabaseOperationException(CantExecuteDatabaseOperationException.DEFAULT_MESSAGE, e, "error loading table into memory", "database error");
        }

        /**
         * set the value to RECEPTION_NOTIFIED and the Last_update_time to now
         */
        if (databaseTable.getRecords().size() > 0){
            DatabaseTableRecord record = databaseTable.getRecords().get(0);
            record.setStringValue(BitcoinCryptoNetworkDatabaseConstants.OUTGOING_TRANSACTIONS_PROTOCOL_STATUS_COLUMN_NAME, ProtocolStatus.RECEPTION_NOTIFIED.getCode());
            record.setStringValue(BitcoinCryptoNetworkDatabaseConstants.OUTGOING_TRANSACTIONS_LAST_UPDATE_COLUMN_NAME, getCurrentDateTime());

            try {
                databaseTable.updateRecord(record);
            } catch (CantUpdateRecordException e) {
                StringBuilder errorOutput = new StringBuilder("There was a problem setting to RECEPTION_NOTIFIED the following transaction:");
                errorOutput.append(System.lineSeparator());
                errorOutput.append(XMLParser.parseObject(record));
                throw  new CantExecuteDatabaseOperationException(CantExecuteDatabaseOperationException.DEFAULT_MESSAGE, e, errorOutput.toString(), "database issue");
            }
        }

    }

    /**
     * Gets the list of Pending trasactions and forms the TransactionProtocolData object to pass to the Incoming Crypto Router
     * Gets the data from both incoming and outgoing transactions.
     * @return
     */
    public List<TransactionProtocolData> getPendingTransactionProtocolData() throws CantExecuteDatabaseOperationException {
        List<TransactionProtocolData> transactionProtocolDataList = getPendingTransactionProtocolData(TransactionTypes.INCOMING);
        transactionProtocolDataList.addAll(getPendingTransactionProtocolData(TransactionTypes.OUTGOING));
        return transactionProtocolDataList;
    }

    /**
     * Gets the pending transaction data from the specified table
     * @param transactionType
     * @return
     */
    private List<TransactionProtocolData> getPendingTransactionProtocolData(TransactionTypes transactionType) throws CantExecuteDatabaseOperationException {
        DatabaseTable databaseTable;
        String transactionIdColumnName;

        List<TransactionProtocolData> transactionProtocolDataList = new ArrayList<>();

        if (transactionType == TransactionTypes.INCOMING){
            databaseTable = database.getTable(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_TABLE_NAME);
            databaseTable.setStringFilter(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_PROTOCOL_STATUS_COLUMN_NAME, ProtocolStatus.TO_BE_NOTIFIED.getCode(), DatabaseFilterType.EQUAL);
            transactionIdColumnName = BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_TRX_ID_COLUMN_NAME;
        } else{
            databaseTable = database.getTable(BitcoinCryptoNetworkDatabaseConstants.OUTGOING_TRANSACTIONS_TABLE_NAME);
            databaseTable.setStringFilter(BitcoinCryptoNetworkDatabaseConstants.OUTGOING_TRANSACTIONS_PROTOCOL_STATUS_COLUMN_NAME, ProtocolStatus.TO_BE_NOTIFIED.getCode(), DatabaseFilterType.EQUAL);
            transactionIdColumnName = BitcoinCryptoNetworkDatabaseConstants.OUTGOING_TRANSACTIONS_TRX_ID_COLUMN_NAME;
        }

        try {
            databaseTable.loadToMemory();
        } catch (CantLoadTableToMemoryException e) {
            throw new CantExecuteDatabaseOperationException(CantExecuteDatabaseOperationException.DEFAULT_MESSAGE,e, "cant load table to get pending transactions.", "database error");
        }

        /**
         * Will form the TransactionProtocolData object from the records
         */
        for (DatabaseTableRecord record : databaseTable.getRecords()){
            TransactionProtocolData transactionProtocolData = new TransactionProtocolData();
            transactionProtocolData.setTransactionId(UUID.fromString(record.getStringValue(transactionIdColumnName)));
            transactionProtocolData.setCryptoTransaction(getCryptoTransactionFromRecord(transactionType, record));
            transactionProtocolData.setAction(Action.APPLY);
            transactionProtocolData.setTimestamp(System.currentTimeMillis() / 1000L); //todo I need to convert the stored saved date to long

            transactionProtocolDataList.add(transactionProtocolData);
        }

        return transactionProtocolDataList;

    }

    /**
     * Creates a incoming or outgoing CryptoTransaction object from a database record
     * @param transactionType
     * @param record
     * @return
     */
    private CryptoTransaction getCryptoTransactionFromRecord(TransactionTypes transactionType, DatabaseTableRecord record) {
        String addressFromColumnName, addressToColumnName, transactionHashColumnName, valueColumnName, cryptoStatusColumnName;
        if (transactionType == TransactionTypes.INCOMING){
            transactionHashColumnName = BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_HASH_COLUMN_NAME;
            addressFromColumnName = BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_ADDRESS_FROM_COLUMN_NAME;
            addressToColumnName = BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_ADDRESS_TO_COLUMN_NAME;
            valueColumnName = BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_VALUE_COLUMN_NAME;
            cryptoStatusColumnName = BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_CRYPTO_STATUS_COLUMN_NAME;
        } else{
            transactionHashColumnName = BitcoinCryptoNetworkDatabaseConstants.OUTGOING_TRANSACTIONS_HASH_COLUMN_NAME;
            addressFromColumnName = BitcoinCryptoNetworkDatabaseConstants.OUTGOING_TRANSACTIONS_ADDRESS_FROM_COLUMN_NAME;
            addressToColumnName = BitcoinCryptoNetworkDatabaseConstants.OUTGOING_TRANSACTIONS_ADDRESS_TO_COLUMN_NAME;
            valueColumnName = BitcoinCryptoNetworkDatabaseConstants.OUTGOING_TRANSACTIONS_VALUE_COLUMN_NAME;
            cryptoStatusColumnName = BitcoinCryptoNetworkDatabaseConstants.OUTGOING_TRANSACTIONS_CRYPTO_STATUS_COLUMN_NAME;
        }

       CryptoTransaction cryptoTransaction = new CryptoTransaction();
        cryptoTransaction.setTransactionHash(record.getStringValue(transactionHashColumnName));
        cryptoTransaction.setCryptoCurrency(CryptoCurrency.BITCOIN);
        try {
            cryptoTransaction.setCryptoStatus(CryptoStatus.getByCode(record.getStringValue(cryptoStatusColumnName)));
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }
        cryptoTransaction.setCryptoAmount(record.getLongValue(valueColumnName));
        cryptoTransaction.setAddressFrom(new CryptoAddress(record.getStringValue(addressFromColumnName), CryptoCurrency.BITCOIN));
        cryptoTransaction.setAddressTo(new CryptoAddress(record.getStringValue(addressToColumnName), CryptoCurrency.BITCOIN));

        //todo define how to get the Op_Return value
        return cryptoTransaction;
    }

    /**
     * Gets both incoming and outgoing transactions hash stored in the database
     * @return
     * @throws CantExecuteDatabaseOperationException
     */
    public Set<String> getStoredStransactionsHash () throws CantExecuteDatabaseOperationException{
        Set<String> transactionsSet = new HashSet<>();

        /**
         * Loads and puts in the transactionsSet  the list of stored Hashes
         */
        DatabaseTable databaseTable = database.getTable(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_TABLE_NAME);
        try {
            databaseTable.loadToMemory();
        } catch (CantLoadTableToMemoryException e) {
            throw new CantExecuteDatabaseOperationException(CantExecuteDatabaseOperationException.DEFAULT_MESSAGE, e, "error loading table in memory", "database issue");
        }

        for (DatabaseTableRecord record : databaseTable.getRecords()){
            transactionsSet.add(record.getStringValue(BitcoinCryptoNetworkDatabaseConstants.INCOMING_TRANSACTIONS_HASH_COLUMN_NAME));
        }

        /**
         * Loads and puts in the transactionsSet  the list of stored Hashes
         */
        databaseTable = database.getTable(BitcoinCryptoNetworkDatabaseConstants.OUTGOING_TRANSACTIONS_TABLE_NAME);
        try {
            databaseTable.loadToMemory();
        } catch (CantLoadTableToMemoryException e) {
            throw new CantExecuteDatabaseOperationException(CantExecuteDatabaseOperationException.DEFAULT_MESSAGE, e, "error loading table in memory", "database issue");
        }

        for (DatabaseTableRecord record : databaseTable.getRecords()){
            transactionsSet.add(record.getStringValue(BitcoinCryptoNetworkDatabaseConstants.OUTGOING_TRANSACTIONS_HASH_COLUMN_NAME));
        }

        return transactionsSet;
    }
}
