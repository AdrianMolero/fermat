package com.bitdubai.fermat_cbp_plugin.layer.wallet.crypto_broker.developer.bitdubai.version_1.database;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.AsymmetricCryptography;
import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.interfaces.KeyPair;
import com.bitdubai.fermat_api.layer.all_definition.enums.DeviceDirectory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantInsertRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FileLifeSpan;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FilePrivacy;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginTextFile;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantCreateFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantLoadFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantPersistFileException;
import com.bitdubai.fermat_api.layer.pip_Identity.developer.exceptions.CantGetUserDeveloperIdentitiesException;
import com.bitdubai.fermat_cbp_api.all_definition.enums.BalanceType;
import com.bitdubai.fermat_cbp_api.all_definition.enums.CurrencyType;
import com.bitdubai.fermat_cbp_api.all_definition.enums.TransactionType;
import com.bitdubai.fermat_cbp_api.all_definition.exceptions.InvalidParameterException;
//import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_cbp_api.layer.cbp_wallet.crypto_broker.exceptions.CantCalculateBalanceException;
import com.bitdubai.fermat_cbp_api.layer.cbp_wallet.crypto_broker.interfaces.CryptoBrokerTransactionRecord;
import com.bitdubai.fermat_cbp_api.layer.cbp_wallet.crypto_broker.exceptions.CantRegisterDebitException;
//import com.bitdubai.fermat_cbp_api.layer.cbp_wallet.crypto_broker.interfaces.CryptoBrokerTotalBalanceRecord;
import com.bitdubai.fermat_cbp_plugin.layer.wallet.crypto_broker.developer.bitdubai.version_1.CryptoBrokerWalletPluginRoot;
import com.bitdubai.fermat_cbp_plugin.layer.wallet.crypto_broker.developer.bitdubai.version_1.exceptions.CantPersistPrivateKeyException;
import com.bitdubai.fermat_cbp_plugin.layer.wallet.crypto_broker.developer.bitdubai.version_1.exceptions.CantInitializeCryptoBrokerWalletDatabaseException;
import com.bitdubai.fermat_cbp_plugin.layer.wallet.crypto_broker.developer.bitdubai.version_1.exceptions.CantGetBalanceRecordException;
import com.bitdubai.fermat_cbp_plugin.layer.wallet.crypto_broker.developer.bitdubai.version_1.exceptions.CantExecuteCryptoBrokerTransactionException;
import com.bitdubai.fermat_cbp_plugin.layer.wallet.crypto_broker.developer.bitdubai.version_1.exceptions.CantGetCryptoBrokerWalletPrivateKeyException;
import com.bitdubai.fermat_cbp_plugin.layer.wallet.crypto_broker.developer.bitdubai.version_1.exceptions.CantListCryptoBrokerWalletTransactionException;
import com.bitdubai.fermat_cbp_plugin.layer.wallet.crypto_broker.developer.bitdubai.version_1.structure.CryptoBrokerWalletImpl;
import com.bitdubai.fermat_pip_api.layer.pip_user.device_user.interfaces.DeviceUser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Yordin Alayn on 17.10.15.
 */
public class CryptoBrokerWalletDatabaseDao {

    private PluginDatabaseSystem pluginDatabaseSystem;

    private PluginFileSystem pluginFileSystem;

    private UUID pluginId;

    public CryptoBrokerWalletDatabaseDao(PluginDatabaseSystem pluginDatabaseSystem, PluginFileSystem pluginFileSystem, UUID pluginId) {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.pluginFileSystem = pluginFileSystem;
        this.pluginId = pluginId;
    }

    Database database;

    /*INITIALIZE DATABASE*/
    public void initialize() throws CantInitializeCryptoBrokerWalletDatabaseException {
        try {
            database = this.pluginDatabaseSystem.openDatabase(this.pluginId, this.pluginId.toString());
        } catch (DatabaseNotFoundException e) {
            try {
                CryptoBrokerWalletDatabaseFactory databaseFactory = new CryptoBrokerWalletDatabaseFactory(pluginDatabaseSystem);
                database = databaseFactory.createDatabase(pluginId, pluginId.toString());
            } catch (CantCreateDatabaseException f) {
                throw new CantInitializeCryptoBrokerWalletDatabaseException(CantCreateDatabaseException.DEFAULT_MESSAGE, f, "", "There is a problem and i cannot create the database.");
            } catch (Exception z) {
                throw new CantInitializeCryptoBrokerWalletDatabaseException(CantOpenDatabaseException.DEFAULT_MESSAGE, z, "", "Generic Exception.");
            }
        } catch (CantOpenDatabaseException e) {
            throw new CantInitializeCryptoBrokerWalletDatabaseException(CantOpenDatabaseException.DEFAULT_MESSAGE, e, "", "Exception not handled by the plugin, there is a problem and i cannot open the database.");
        } catch (Exception e) {
            throw new CantInitializeCryptoBrokerWalletDatabaseException(CantOpenDatabaseException.DEFAULT_MESSAGE, e, "", "Generic Exception.");
        }
    }

    /*## CRYPTO BROKER ##*/
    /*GET BALANCE BOOKED*/
    public float getBookBalance() throws CantCalculateBalanceException {
        try {
            return getCurrentBalance(BalanceType.BOOK);
        } catch (CantGetBalanceRecordException exception) {
            throw new CantCalculateBalanceException(CantCalculateBalanceException.DEFAULT_MESSAGE, exception, null, "Check the cause");
        } catch (Exception exception) {
            throw new CantCalculateBalanceException(CantCalculateBalanceException.DEFAULT_MESSAGE, FermatException.wrapException(exception), null, "Check the cause");

        }
    }

    /*GET BALANCE AVAILABLE*/
    public float getAvailableBalance() throws CantCalculateBalanceException {
        try{
            return getCurrentBalance(BalanceType.AVAILABLE);
        } catch (CantGetBalanceRecordException exception){
            throw new CantCalculateBalanceException(CantCalculateBalanceException.DEFAULT_MESSAGE, exception, null, "Check the cause");
        } catch (Exception exception){
            throw new CantCalculateBalanceException(CantCalculateBalanceException.DEFAULT_MESSAGE, FermatException.wrapException(exception), null, "Check the cause");
        }
    }

    /*LIST TRANSACTION*/
    public List<CryptoBrokerTransactionRecord> getTransactionsList (DeviceUser deviceUser) throws CantListCryptoBrokerWalletTransactionException {
        List<CryptoBrokerTransactionRecord> list = new ArrayList<CryptoBrokerTransactionRecord>();
        DatabaseTable cryptoBrokerTable;
        try {
            cryptoBrokerTable = this.database.getTable(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_TABLE_NAME);
            if (cryptoBrokerTable == null) {
                throw new CantGetUserDeveloperIdentitiesException("Cant get crypto broker identity list, table not found.", "Crypto Broker Identity", "Cant get Crypto Broker identity list, table not found.");
            }
            cryptoBrokerTable.setStringFilter(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_PUBLIC_KEY_WALLET_COLUMN_NAME, deviceUser.getPublicKey(), DatabaseFilterType.EQUAL);
            cryptoBrokerTable.loadToMemory();

            for (DatabaseTableRecord record : cryptoBrokerTable.getRecords ()) {
                list.add(getCryptoBrokerFromRecord(record));
            }
        } catch (CantLoadTableToMemoryException e) {
            throw new CantListCryptoBrokerWalletTransactionException(e.getMessage(), e, "Crypto Broker Wallet", "Cant load " + CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_TABLE_NAME + " table in memory.");
        } catch (CantGetCryptoBrokerWalletPrivateKeyException e) {
            throw new CantListCryptoBrokerWalletTransactionException(e.getMessage(), e, "Crypto Broker Wallet", "Can't get private key.");
        } catch (Exception e) {
            throw new CantListCryptoBrokerWalletTransactionException(e.getMessage(), FermatException.wrapException(e), "Crypto Broker Wallet", "Cant get Crypto Broker Wallet list, unknown failure.");
        }
        return list;
    }

    /*TRANSACTION SUMMARY*/

    /*## END CRYPTO BROKER ##*/
    /*## CRYPTO BROKER BALANCE ##*/

    /*ADD CREDIT*/
    public void addDebit(final CryptoBrokerTransactionRecord cryptoBrokerTransactionRecord, final BalanceType balanceType, final String privateKeyWallet) throws CantRegisterDebitException{
        try {
            if (isTransactionInTable(cryptoBrokerTransactionRecord.getTransactionId(), TransactionType.DEBIT, balanceType))
                throw new CantRegisterDebitException(CantRegisterDebitException.DEFAULT_MESSAGE, null, null, "The transaction is already in the database");

            float availableAmount = balanceType.equals(BalanceType.AVAILABLE) ? cryptoBrokerTransactionRecord.getAmount() : 0L;
            float bookAmount = balanceType.equals(BalanceType.BOOK) ? cryptoBrokerTransactionRecord.getAmount() : 0L;
            float runningBookBalance = calculateBookRunningBalanceByAsset(-bookAmount, cryptoBrokerTransactionRecord.getPublicKeyWallet());
            float runningAvailableBalance = calculateAvailableRunningBalanceByAsset(-availableAmount, cryptoBrokerTransactionRecord.getPublicKeyWallet());

            executeTransaction(cryptoBrokerTransactionRecord,TransactionType.DEBIT ,balanceType, runningBookBalance, runningAvailableBalance, privateKeyWallet);
        }catch(CantGetBalanceRecordException | CantLoadTableToMemoryException | CantExecuteCryptoBrokerTransactionException exception){
            throw new CantRegisterDebitException(CantRegisterDebitException.DEFAULT_MESSAGE, exception, null, "Check the cause");
        } catch (Exception exception){
            throw new CantRegisterDebitException(CantRegisterDebitException.DEFAULT_MESSAGE, FermatException.wrapException(exception), null, "Check the cause");
        }
    }

    /*ADD DEBIT*/
    public void addCredit(final CryptoBrokerTransactionRecord cryptoBrokerTransactionRecord, final BalanceType balanceType, final String privateKeyWallet) throws CantRegisterDebitException{
        try {
            if (isTransactionInTable(cryptoBrokerTransactionRecord.getTransactionId(), TransactionType.CREDIT, balanceType))
                throw new CantRegisterDebitException(CantRegisterDebitException.DEFAULT_MESSAGE, null, null, "The transaction is already in the database");

            float availableAmount = balanceType.equals(BalanceType.AVAILABLE) ? cryptoBrokerTransactionRecord.getAmount() : 0L;
            float bookAmount = balanceType.equals(BalanceType.BOOK) ? cryptoBrokerTransactionRecord.getAmount() : 0L;
            float runningBookBalance = calculateBookRunningBalanceByAsset(-bookAmount, cryptoBrokerTransactionRecord.getPublicKeyWallet());
            float runningAvailableBalance = calculateAvailableRunningBalanceByAsset(-availableAmount, cryptoBrokerTransactionRecord.getPublicKeyWallet());

            executeTransaction(cryptoBrokerTransactionRecord,TransactionType.CREDIT ,balanceType, runningBookBalance, runningAvailableBalance, privateKeyWallet);
        }catch(CantGetBalanceRecordException | CantLoadTableToMemoryException | CantExecuteCryptoBrokerTransactionException exception){
            throw new CantRegisterDebitException(CantRegisterDebitException.DEFAULT_MESSAGE, exception, null, "Check the cause");
        } catch (Exception exception){
            throw new CantRegisterDebitException(CantRegisterDebitException.DEFAULT_MESSAGE, FermatException.wrapException(exception), null, "Check the cause");
        }
    }
    
    /*## END CRYPTO BROKER BALANCE ##*/
    /*## CRYPTO BROKER MANAGER ##*/
    
    /*LOAD WALLETS*/


    /*CREATE NEW WALLET*/

    /*## END CRYPTO BROKER MANAGER ##*/
    /*## PRIVATE ##*/
    private long getCurrentBalance(final BalanceType balanceType) throws CantGetBalanceRecordException {
        long balanceAmount = 0;
        if (balanceType == BalanceType.AVAILABLE){
            for (DatabaseTableRecord record : getBalancesRecord()){
                balanceAmount += record.getLongValue(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_TOTAL_BALANCES_AVAILABLE_BALANCE_COLUMN_NAME);
            }
        } else {
            for (DatabaseTableRecord record : getBalancesRecord()){
                balanceAmount += record.getLongValue(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_TOTAL_BALANCES_BOOK_BALANCE_COLUMN_NAME);
            }
        }
        return balanceAmount;
    }

    private List<DatabaseTableRecord> getBalancesRecord() throws CantGetBalanceRecordException{
        try {
            DatabaseTable totalBalancesTable = this.database.getTable(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_TOTAL_BALANCES_TABLE_NAME);
            totalBalancesTable.loadToMemory();
            return totalBalancesTable.getRecords();
        } catch (CantLoadTableToMemoryException exception) {
            throw new CantGetBalanceRecordException("Error to get balances record",exception,"Can't load balance table" , "");
        }
    }

    private boolean isTransactionInTable(final UUID transactionId, final TransactionType transactionType, final BalanceType balanceType) throws CantLoadTableToMemoryException {
        DatabaseTable cryptoBrokerTable = this.database.getTable(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_TOTAL_BALANCES_TABLE_NAME);
        cryptoBrokerTable.setUUIDFilter(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_TRANSACTION_ID_COLUMN_NAME, transactionId, DatabaseFilterType.EQUAL);
        cryptoBrokerTable.setStringFilter(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_TRANSACTION_TYPE_COLUMN_NAME, transactionType.getCode(), DatabaseFilterType.EQUAL);
        cryptoBrokerTable.setStringFilter(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_BALANCE_TYPE_COLUMN_NAME, balanceType.getCode(), DatabaseFilterType.EQUAL);
        cryptoBrokerTable.loadToMemory();
        return !cryptoBrokerTable.getRecords().isEmpty();
    }


    private float calculateBookRunningBalanceByAsset(final float transactionAmount, String publicKeyWallet) throws CantGetBalanceRecordException{
        return getCurrentBalance(BalanceType.BOOK) + transactionAmount;
    }

    private float calculateAvailableRunningBalanceByAsset(final float transactionAmount, String publicKeyWallet) throws CantGetBalanceRecordException{
        return getCurrentBalance(BalanceType.AVAILABLE) + transactionAmount;
    }

    private float getCurrentBalance(BalanceType balanceType, String publicKeyWallet)
    {
        try {
            long balanceAmount = 0;
            if (balanceType == BalanceType.AVAILABLE) {
                balanceAmount = getBalancesTotalBalances(publicKeyWallet).getLongValue(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_TOTAL_BALANCES_AVAILABLE_BALANCE_COLUMN_NAME);
            } else {
                balanceAmount = getBalancesTotalBalances(publicKeyWallet).getLongValue(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_TOTAL_BALANCES_BOOK_BALANCE_COLUMN_NAME);
            }
            return balanceAmount;
        }
        catch (Exception exception){
            return 0;
        }
    }

    private DatabaseTableRecord getBalancesTotalBalances(String publicKeyWallet) throws CantGetBalanceRecordException{
        try {
            DatabaseTable totalBalancesTable = database.getTable(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_TOTAL_BALANCES_TABLE_NAME);;
            totalBalancesTable.setStringFilter(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_TOTAL_BALANCES_PUBLIC_KEY_WALLET_COLUMN_NAM, publicKeyWallet, DatabaseFilterType.EQUAL);
            totalBalancesTable.loadToMemory();
            if (!totalBalancesTable.getRecords().isEmpty() ) {
                return totalBalancesTable.getRecords().get(0);
            } else {
                return totalBalancesTable.getEmptyRecord();
            }
        } catch (CantLoadTableToMemoryException exception) {
            throw new CantGetBalanceRecordException("Error to get balances record",exception,"Can't load balance table" , "");
        }
    }

    private void executeTransaction(final CryptoBrokerTransactionRecord cryptoBrokerTransactionRecord, final TransactionType transactionType, final BalanceType balanceType, final float runningBookBalance, final float runningAvailableBalance, final String privateKeyWallet) throws CantExecuteCryptoBrokerTransactionException {
        try {
            DatabaseTable cryptoBrokerTable = this.database.getTable(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_TABLE_NAME);
            DatabaseTableRecord cryptoBrokerRecord = cryptoBrokerTable.getEmptyRecord();
            loadCryptoBrokersRecordAsNew(cryptoBrokerRecord, cryptoBrokerTransactionRecord, transactionType, balanceType, runningBookBalance, runningAvailableBalance);
            cryptoBrokerTable.insertRecord(cryptoBrokerRecord);

            DatabaseTable totalBalancesTable = this.database.getTable(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_TOTAL_BALANCES_TABLE_NAME);
            DatabaseTableRecord totalBalancesRecord = totalBalancesTable.getEmptyRecord();

            totalBalancesTable.setStringFilter(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_TOTAL_BALANCES_TABLE_NAME, cryptoBrokerTransactionRecord.getPublicKeyWallet(), DatabaseFilterType.EQUAL );
            totalBalancesTable.loadToMemory();
            String description = "TRANSACTION: " +transactionType.getCode();
            loadtotalBalancesRecordAsNew(totalBalancesRecord, cryptoBrokerTransactionRecord, runningBookBalance, runningAvailableBalance, description);
            if (totalBalancesTable.getRecords().isEmpty()){
                totalBalancesTable.insertRecord(totalBalancesRecord);
                persistNewCryptoBrokerWalletPrivateKeysFile(cryptoBrokerTransactionRecord.getPublicKeyWallet(), privateKeyWallet);
            }else{
                totalBalancesTable.updateRecord(totalBalancesRecord);
            }
        } catch (CantInsertRecordException e){
            throw new CantExecuteCryptoBrokerTransactionException(e.getMessage(), e, "Crypto Broker Wallet", "Cant Add new Transaction in Crypto Broker Wallet, insert database problems.");
        } catch (CantPersistPrivateKeyException e){
            throw new CantExecuteCryptoBrokerTransactionException (e.getMessage(), e, "Crypto Broker Wallet", "Cant Add new Transaction in Crypto Broker Wallet, persist private key error.");
        } catch (Exception e) {
            throw new CantExecuteCryptoBrokerTransactionException (e.getMessage(), FermatException.wrapException(e), "Crypto Broker Wallet", "Cant Add new Transaction in Crypto Broker Wallet, unknown failure.");
        }
    }

    private void  persistNewCryptoBrokerWalletPrivateKeysFile(String publicKey,String privateKey) throws CantPersistPrivateKeyException {
        try {
            PluginTextFile file = this.pluginFileSystem.createTextFile(pluginId,
                    DeviceDirectory.LOCAL_USERS.getName(),
                    CryptoBrokerWalletPluginRoot.CRYPTO_BROKER_PRIVATE_KEYS_WALLET_FILE_NAME + "_" + publicKey,
                    FilePrivacy.PRIVATE,
                    FileLifeSpan.PERMANENT
            );
            file.setContent(privateKey);
            file.persistToMedia();
        } catch (CantPersistFileException e) {
            throw new CantPersistPrivateKeyException("CAN'T PERSIST PRIVATE KEY ", e, "Error persist file.", null);
        } catch (CantCreateFileException e) {
            throw new CantPersistPrivateKeyException("CAN'T PERSIST PRIVATE KEY ", e, "Error creating file.", null);
        } catch (Exception e) {
            throw  new CantPersistPrivateKeyException("CAN'T PERSIST PRIVATE KEY ",FermatException.wrapException(e),"", "");
        }
    }

    private void loadCryptoBrokersRecordAsNew(
            DatabaseTableRecord databaseTableRecord,
            CryptoBrokerTransactionRecord cryptoBrokerTransactionRecord,
            TransactionType transactionType,
            BalanceType balanceType,
            float runningBookBalance,
            float runningAvailableBalance
    ) {
        UUID transactionId = UUID.randomUUID();
        databaseTableRecord.setUUIDValue(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_TRANSACTION_ID_COLUMN_NAME, transactionId);
        databaseTableRecord.setStringValue(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_PUBLIC_KEY_WALLET_COLUMN_NAME, cryptoBrokerTransactionRecord.getPublicKeyWallet());
        databaseTableRecord.setStringValue(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_PUBLIC_KEY_BROKER_COLUMN_NAME, cryptoBrokerTransactionRecord.getPublicKeyBroker());
        databaseTableRecord.setStringValue(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_BALANCE_TYPE_COLUMN_NAME, balanceType.getCode());
        databaseTableRecord.setStringValue(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_TRANSACTION_TYPE_COLUMN_NAME, transactionType.getCode());
        databaseTableRecord.setFloatValue(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_AMOUNT_COLUMN_NAME, cryptoBrokerTransactionRecord.getAmount());
        databaseTableRecord.setStringValue(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_CURRENCY_TYPE_COLUMN_NAME, cryptoBrokerTransactionRecord.getCurrencyType().getCode());
        databaseTableRecord.setFloatValue(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_RUNNING_BOOK_BALANCE_COLUMN_NAME, runningBookBalance);
        databaseTableRecord.setFloatValue(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_RUNNING_AVAILABLE_BALANCE_COLUMN_NAME, runningAvailableBalance);
        databaseTableRecord.setLongValue(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_TIMESTAMP_COLUMN_NAME, cryptoBrokerTransactionRecord.getTimestamp());
        databaseTableRecord.setStringValue(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_MEMO_COLUMN_NAME, cryptoBrokerTransactionRecord.getMemo());

    }

    private void loadtotalBalancesRecordAsNew(
            DatabaseTableRecord databaseTableRecord,
            CryptoBrokerTransactionRecord cryptoBrokerTransactionRecord,
            float runningBookBalance,
            float runningAvailableBalance,
            String description
    ) {
        databaseTableRecord.setStringValue(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_TOTAL_BALANCES_PUBLIC_KEY_WALLET_COLUMN_NAM, cryptoBrokerTransactionRecord.getPublicKeyWallet());
        databaseTableRecord.setStringValue(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_TOTAL_BALANCES_CURRENCY_TYPE_COLUMN_NAME, cryptoBrokerTransactionRecord.getCurrencyType().getCode());
        databaseTableRecord.setStringValue(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_TOTAL_BALANCES_DESCRIPTION_COLUMN_NAME, description);
        databaseTableRecord.setFloatValue(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_TOTAL_BALANCES_BOOK_BALANCE_COLUMN_NAME, runningBookBalance);
        databaseTableRecord.setFloatValue(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_TOTAL_BALANCES_AVAILABLE_BALANCE_COLUMN_NAME, runningAvailableBalance);

    }

    private CryptoBrokerTransactionRecord getCryptoBrokerFromRecord(final DatabaseTableRecord record) throws CantGetCryptoBrokerWalletPrivateKeyException, InvalidParameterException{
        UUID  transactionId                 = record.getUUIDValue(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_TRANSACTION_ID_COLUMN_NAME);
        String  publickeyWalle              = getCryptoBrokerPrivateKeyWallet(record.getStringValue(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_PUBLIC_KEY_WALLET_COLUMN_NAME));
        String  publickeybroker             = getCryptoBrokerPrivateKeyBroker(record.getStringValue(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_PUBLIC_KEY_BROKER_COLUMN_NAME));
        BalanceType  balanceType            = BalanceType.getByCode(record.getStringValue(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_BALANCE_TYPE_COLUMN_NAME));
        TransactionType  transactionType    = TransactionType.getByCode(record.getStringValue(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_TRANSACTION_ID_COLUMN_NAME));
        CurrencyType currencyType           = CurrencyType.getByCode(record.getStringValue(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_CURRENCY_TYPE_COLUMN_NAME));
        float   amount                      = record.getFloatValue(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_AMOUNT_COLUMN_NAME);;
        float   runningBookBalance          = record.getFloatValue(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_RUNNING_BOOK_BALANCE_COLUMN_NAME);
        float   runningAvailableBalance     = record.getFloatValue(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_RUNNING_AVAILABLE_BALANCE_COLUMN_NAME);
        long    timeStamp                   = record.getLongValue(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_TIMESTAMP_COLUMN_NAME);
        String  memo                        = record.getStringValue(CryptoBrokerWalletDatabaseConstants.CRYPTO_BROKER_MEMO_COLUMN_NAME);
        KeyPair keyPairWallet               = AsymmetricCryptography.createKeyPair(publickeyWalle);
        KeyPair keyPairBroker               = AsymmetricCryptography.createKeyPair(publickeybroker);
        return new CryptoBrokerWalletImpl(
                transactionId,
                keyPairWallet,
                keyPairBroker,
                balanceType,
                transactionType,
                currencyType,
                amount,
                runningBookBalance,
                runningAvailableBalance,
                timeStamp,
                memo
        );
    }

    private String getCryptoBrokerPrivateKeyWallet(String publicKey) throws CantGetCryptoBrokerWalletPrivateKeyException {
        String privateKey = "";
        try {
            PluginTextFile file = this.pluginFileSystem.getTextFile(pluginId,
                    DeviceDirectory.LOCAL_USERS.getName(),
                    CryptoBrokerWalletPluginRoot.CRYPTO_BROKER_PRIVATE_KEYS_WALLET_FILE_NAME + "_" + publicKey,
                    FilePrivacy.PRIVATE,
                    FileLifeSpan.PERMANENT
            );
            file.loadFromMedia();
            privateKey = file.getContent();
        } catch (CantLoadFileException e) {
            throw new CantGetCryptoBrokerWalletPrivateKeyException("CAN'T GET PRIVATE KEY WALLER", e, "Error loaded file.", null);
        } catch (CantCreateFileException e) {
            throw new CantGetCryptoBrokerWalletPrivateKeyException("CAN'T GET PRIVATE KEY WALLER", e, "Error getting developer wallet private keys file.", null);
        } catch (Exception e) {
            throw  new CantGetCryptoBrokerWalletPrivateKeyException("CAN'T GET PRIVATE KEY WALLER",FermatException.wrapException(e),"", "");
        }
        return privateKey;
    }

    private String getCryptoBrokerPrivateKeyBroker(String publicKey) throws CantGetCryptoBrokerWalletPrivateKeyException {
        String privateKey = "";
        try {
            PluginTextFile file = this.pluginFileSystem.getTextFile(pluginId,
                    DeviceDirectory.LOCAL_USERS.getName(),
                    CryptoBrokerWalletPluginRoot.CRYPTO_BROKER_PRIVATE_KEYS_BROKER_FILE_NAME + "_" + publicKey,
                    FilePrivacy.PRIVATE,
                    FileLifeSpan.PERMANENT
            );
            file.loadFromMedia();
            privateKey = file.getContent();
        } catch (CantLoadFileException e) {
            throw new CantGetCryptoBrokerWalletPrivateKeyException("CAN'T GET PRIVATE KEY WALLER", e, "Error loaded file.", null);
        } catch (CantCreateFileException e) {
            throw new CantGetCryptoBrokerWalletPrivateKeyException("CAN'T GET PRIVATE KEY WALLER", e, "Error getting developer wallet private keys file.", null);
        } catch (Exception e) {
            throw  new CantGetCryptoBrokerWalletPrivateKeyException("CAN'T GET PRIVATE KEY WALLER",FermatException.wrapException(e),"", "");
        }
        return privateKey;
    }

    /*##END PRIVATE##*/
}