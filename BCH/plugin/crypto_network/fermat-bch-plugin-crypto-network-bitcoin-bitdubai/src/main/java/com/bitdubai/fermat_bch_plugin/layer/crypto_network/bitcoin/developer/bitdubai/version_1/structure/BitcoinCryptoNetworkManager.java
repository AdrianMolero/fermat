package com.bitdubai.fermat_bch_plugin.layer.crypto_network.bitcoin.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.all_definition.enums.BlockchainNetworkType;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.Specialist;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.TransactionProtocolManager;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.crypto_transactions.CryptoTransaction;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.exceptions.CantConfirmTransactionException;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.exceptions.CantDeliverPendingTransactionsException;
import com.bitdubai.fermat_api.layer.dmp_world.wallet.exceptions.CantStartAgentException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.BitcoinNetworkSelector;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.exceptions.CantBroadcastTransactionException;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.exceptions.CantGetGenesisTransactionException;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.interfaces.BitcoinNetworkConfiguration;
import com.bitdubai.fermat_bch_api.layer.crypto_vault.CryptoVaults;
import com.bitdubai.fermat_bch_plugin.layer.crypto_network.bitcoin.developer.bitdubai.version_1.database.BitcoinCryptoNetworkDatabaseDao;
import com.bitdubai.fermat_bch_plugin.layer.crypto_network.bitcoin.developer.bitdubai.version_1.exceptions.BlockchainException;
import com.bitdubai.fermat_bch_plugin.layer.crypto_network.bitcoin.developer.bitdubai.version_1.exceptions.CantExecuteDatabaseOperationException;
import com.bitdubai.fermat_bch_plugin.layer.crypto_network.bitcoin.developer.bitdubai.version_1.util.TransactionProtocolData;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.interfaces.EventManager;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.core.UTXO;
import org.bitcoinj.core.UTXOProvider;
import org.bitcoinj.core.UTXOProviderException;
import org.bitcoinj.core.Wallet;
import org.bitcoinj.params.RegTestParams;
import org.bitcoinj.store.UnreadableWalletException;
import org.bitcoinj.wallet.WalletTransaction;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * The Class <code>com.bitdubai.fermat_bch_plugin.layer.cryptonetwork.bitcoin.developer.bitdubai.version_1.structure.BitcoinCryptoNetworkManager</code>
 * Starts the monitoring agent that will listen to transactions. Based on the passed public Keys from the network type
 * it will activate a different agent to listen to that network.
 * <p/>
 *
 * Created by Rodrigo Acosta - (acosta_rodrigo@hotmail.com) on 13/10/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class BitcoinCryptoNetworkManager implements TransactionProtocolManager, UTXOProvider {

    /**
     * BitcoinJ wallet where I'm storing the public keys and transactions
     */
    Wallet wallet=null;
    private final String WALLET_FILENAME = "/data/data/com.bitdubai.fermat/files/wallet_";

    /**
     * UTXO Provider interface variables
     */
    BlockchainNetworkType utxoProviderNetworkParameter;

    /**
     * class variables
     */
    BitcoinCryptoNetworkMonitor bitcoinCryptoNetworkMonitor;
    File walletFile;

    /**
     * List of running agents per network
     */
    HashMap<BlockchainNetworkType, BitcoinCryptoNetworkMonitor> runningAgents;

    /**
     * Platform variables
     */
    EventManager eventManager;
    PluginDatabaseSystem pluginDatabaseSystem;
    BitcoinCryptoNetworkDatabaseDao bitcoinCryptoNetworkDatabaseDao;
    UUID pluginId;

    /**
     * Constructor
     * @param eventManager
     * @param pluginDatabaseSystem
     */
    public BitcoinCryptoNetworkManager(EventManager eventManager, PluginDatabaseSystem pluginDatabaseSystem, UUID pluginId) {
        this.eventManager = eventManager;
        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.pluginId = pluginId;

        runningAgents = new HashMap<>();
    }

    /**
     * Monitor the bitcoin network with the passes Key Lists.
     * @param blockchainNetworkTypes
     * @param keyList
     */
    public void monitorNetworkFromKeyList(CryptoVaults cryptoVault, List<BlockchainNetworkType> blockchainNetworkTypes, List<ECKey> keyList) throws CantStartAgentException {
        /**
         * This method will be called from agents from the Vaults. New keys may be added on each call or not.
         */
        try {
            getDao().updateCryptoVaultsStatistics(cryptoVault, keyList.size());
        } catch (CantExecuteDatabaseOperationException e) {
            //If stats where not updated, I will just continue.
            e.printStackTrace();
        }

        /**
         * For each network that is active to be monitored I will...
         */
        for (BlockchainNetworkType blockchainNetworkType : blockchainNetworkTypes){
            /**
             * load (if any) existing wallet.
             */
            wallet = getWallet(blockchainNetworkType);

            /**
             * add new keys (if any).
             */
            boolean isWalletReset = false;
            if (areNewKeysAdded(wallet, keyList)){
                wallet.importKeys(keyList);
                try {
                    wallet.saveToFile(walletFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /**
                 * I do not need to reset the wallet because I will
                 * always be importing fresh (unused) keys.
                 */
                //wallet.reset();
                isWalletReset = true;
            }

            /**
             * If the agent for this network is already running...
             */
            if (isAgentRunning(blockchainNetworkType)){
                /**
                 * and the wallet was reseted because new keys were added
                 */
                if (isWalletReset){
                    BitcoinCryptoNetworkMonitor bitcoinCryptoNetworkMonitor = runningAgents.get(blockchainNetworkType);
                    bitcoinCryptoNetworkMonitor.stop();
                    runningAgents.remove(blockchainNetworkType);
                    bitcoinCryptoNetworkMonitor.setWallet(wallet);

                    bitcoinCryptoNetworkMonitor.start();
                    runningAgents.put(blockchainNetworkType,bitcoinCryptoNetworkMonitor);
                }
            } else {
                /**
                 * If the agent for the network is not running, I will start a new one.
                 */
                BitcoinCryptoNetworkMonitor bitcoinCryptoNetworkMonitor = new BitcoinCryptoNetworkMonitor(this.pluginDatabaseSystem, pluginId, wallet);
                runningAgents.put(blockchainNetworkType, bitcoinCryptoNetworkMonitor);

                bitcoinCryptoNetworkMonitor.start();
            }
        }
    }

    /**
     * Will create the wallet Object. I will try to load it from disk from a previous execution
     * by forming the name wallet_[NETWORK]. If it doesn't exists, then I will create a new object for this network.
     * @return
     */
    private Wallet getWallet(BlockchainNetworkType blockchainNetworkType){
        Wallet wallet;
        String fileName = WALLET_FILENAME + blockchainNetworkType.getCode();
        walletFile = new File(fileName);
        try {
            wallet  =Wallet.loadFromFile(walletFile);
        } catch (UnreadableWalletException e) {
            wallet = new Wallet(BitcoinNetworkSelector.getNetworkParameter(blockchainNetworkType));
        }

        /**
         * Will set the autosave information
         */
        wallet.autosaveToFile(walletFile, 1, TimeUnit.SECONDS, null);

        return wallet;
    }

    /**
     * Will compare the keys already saved in the wallet loaded from disk against the list passed by the vault.
     * If there are additions, it will return true.
     * If they are no new keys, will return false.
     * @param wallet
     * @param keys
     * @return
     */
    private boolean areNewKeysAdded(Wallet wallet, List<ECKey> keys){
        List<ECKey> walletKeys = wallet.getImportedKeys();
        /**
         * I remove from the passed list, everything is already saved in the wallet-
         */
        keys.removeAll(walletKeys);

        /**
         * If there are still keys, then we have new ones.
         */
        if (keys.size() >0)
            return true;
        else
            return false;
    }

    /**
     * Verifies if for the passed network type, an Agent is already running.
     * @param blockchainNetworkType
     * @return
     */
    private boolean isAgentRunning(BlockchainNetworkType blockchainNetworkType){
        if (runningAgents.get(blockchainNetworkType) == null)
            return false;
        else
            return true;
    }

    /**
     * instantiate if needed the dao object to access the database
     * @return
     */
    private BitcoinCryptoNetworkDatabaseDao getDao(){
        if (bitcoinCryptoNetworkDatabaseDao == null)
            bitcoinCryptoNetworkDatabaseDao = new BitcoinCryptoNetworkDatabaseDao(this.pluginId, this.pluginDatabaseSystem);
        return bitcoinCryptoNetworkDatabaseDao;
    }

    /**
     * TransactionProtocolManager interface implementations
     */

    /**
     * Confirms the reception of a transaction.
     * This will change the ProtocolStatus of a transaction from ToBeNotified to NoActionRequired
     * @param transactionID
     * @throws CantConfirmTransactionException
     */
    @Override
    public void confirmReception(UUID transactionID) throws CantConfirmTransactionException {
        try {
            getDao().confirmReception(transactionID);
        } catch (CantExecuteDatabaseOperationException e) {
            throw new CantConfirmTransactionException(CantConfirmTransactionException.DEFAULT_MESSAGE, e, "Crypto Network issue confirming transaction.", "database issue");
        }
    }

    /**
     * Gets the list of pending transactions, which are marked as Pending_NOTIFIED
     * @param specialist
     * @return
     * @throws CantDeliverPendingTransactionsException
     */
    @Override
    public List<com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.Transaction> getPendingTransactions(Specialist specialist) throws CantDeliverPendingTransactionsException {
        /**
         * the list to return
         */
        List<com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.Transaction> transactionList = new ArrayList<>();

        /**
         * Will get all the pendingCryptoTransactions data
         */
        try {
            for (TransactionProtocolData transactionProtocolData : getPendingTransactionProtocolData()){
                com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.Transaction transaction;
                /**
                 * I create the transaction protocol object and fill it with the data
                 */
                transaction = new com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.Transaction(
                        transactionProtocolData.getTransactionId(),
                        transactionProtocolData.getCryptoTransaction(),
                        transactionProtocolData.getAction(),
                        transactionProtocolData.getTimestamp());
                /**
                 * and Add it to the list
                 */
                transactionList.add(transaction);
            }
        } catch (CantExecuteDatabaseOperationException e) {
            throw new CantDeliverPendingTransactionsException(CantDeliverPendingTransactionsException.DEFAULT_MESSAGE, e, "database error getting the pending transactions.", "database issue");
        }

        return transactionList;
    }

    /**
     * Gets the pending transaction data
     * @return
     */
    private List<TransactionProtocolData> getPendingTransactionProtocolData() throws CantExecuteDatabaseOperationException {
        return getDao().getPendingTransactionProtocolData();
    }

    /**
     * Gets the CryptoTransaction list that matches the specified hash
     * @param txHash
     * @return
     * @throws CantGetGenesisTransactionException
     */
    public List<CryptoTransaction> getGenesisTransaction(String txHash) throws CantGetGenesisTransactionException {
        try {
            return getDao().getIncomingCryptoTransaction(txHash);
        } catch (CantExecuteDatabaseOperationException e) {
            throw new CantGetGenesisTransactionException(CantGetGenesisTransactionException.DEFAULT_MESSAGE, e, "database operation issue.", "database error");
        }
    }

    /**
     * Broadcast a well formed, commited and signed transaction into the specified network
     * @param blockchainNetworkType
     * @param tx
     * @throws CantBroadcastTransactionException
     */
    public void broadcastTransaction(BlockchainNetworkType blockchainNetworkType, Transaction tx) throws CantBroadcastTransactionException {
        runningAgents.get(blockchainNetworkType).broadcastTransaction(tx);
    }


    /**
     * Gets the UTXO provider from the CryptoNetwork on the specified Network
     * @param blockchainNetworkType
     * @return
     */
    public UTXOProvider getUTXOProvider(BlockchainNetworkType blockchainNetworkType) {
        this.utxoProviderNetworkParameter = blockchainNetworkType;
        return this;
    }

    /**
     * Implementation of UTXOProvider interface. Calculates all the UTXO available which outputs are send to the specified address
     * @param addresses
     * @return
     * @throws UTXOProviderException
     */
    @Override
    public List<UTXO> getOpenTransactionOutputs(List<Address> addresses) throws UTXOProviderException {
        /**
         * load the wallet from the passed network. The network type was defined when the UTXO provider was set.
         */
        Wallet wallet = this.getWallet(utxoProviderNetworkParameter);
        List<UTXO> utxoList = new ArrayList<>();

        /**
         * I will get all the outputs that are mine to spent.
         */
        for (TransactionOutput output : wallet.calculateAllSpendCandidates()){
            for (Address address : addresses){
                /**
                 * and if one of them matches the passed address, then I will convert it to an UTXO and add it to the list.
                 */
                if (output.getAddressFromP2PKHScript(RegTestParams.get()) == address){
                    UTXO utxo = new UTXO(   output.getHash(),
                                            output.getIndex(),
                                            output.getValue(),
                                            output.getParentTransactionDepthInBlocks(),
                                            output.getParentTransaction().isCoinBase(),
                                            output.getScriptPubKey(),
                                            address.toString());
                    utxoList.add(utxo);
                }
            }
        }
        return utxoList;
    }


    /**
     * Access the store blockchain and get its height
     * @return
     * @throws UTXOProviderException
     */
    @Override
    public int getChainHeadHeight() throws UTXOProviderException {
        try {
            /**
             * instantiates a blockchain that will load it from file.
             */
            BitcoinCryptoNetworkBlockChain blockChain = new BitcoinCryptoNetworkBlockChain(BitcoinNetworkSelector.getNetworkParameter(utxoProviderNetworkParameter));
            /**
             * get its height.
             */
            return blockChain.getBlockChain().getBestChainHeight();
        } catch (BlockchainException e) {
            throw new UTXOProviderException("There was an error loading the blockchain.", e);
        }
    }

    /**
     * returns the nertwork parameter defined for this UTXO provider
     * @return
     */
    @Override
    public NetworkParameters getParams() {
        return BitcoinNetworkSelector.getNetworkParameter(utxoProviderNetworkParameter);
    }

    /**
     * Gets the specified bitcoin transaction
     * @param transactionHash
     * @return
     */
    public Transaction getBitcoinTransaction(BlockchainNetworkType blockchainNetworkType, String transactionHash) {
        Wallet wallet = getWallet(blockchainNetworkType);
        return wallet.getTransaction(Sha256Hash.of(transactionHash.getBytes()));
    }
}
