package com.bitdubai.fermat_cry_plugin.layer.crypto_vault.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.events.EventSource;
import com.bitdubai.fermat_cry_plugin.layer.crypto_vault.developer.bitdubai.version_1.exceptions.UnexpectedCryptoStatusException;
import com.bitdubai.fermat_cry_api.layer.definition.enums.EventType;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.crypto_transactions.CryptoStatus;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogManager;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.interfaces.EventManager;

import com.bitdubai.fermat_cry_plugin.layer.crypto_vault.developer.bitdubai.version_1.BitcoinCryptoVaultPluginRoot;
import com.bitdubai.fermat_cry_plugin.layer.crypto_vault.developer.bitdubai.version_1.exceptions.CantCalculateTransactionConfidenceException;
import com.bitdubai.fermat_cry_plugin.layer.crypto_vault.developer.bitdubai.version_1.exceptions.CantExecuteQueryException;

import org.bitcoinj.core.AbstractWalletEventListener;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.Wallet;

import java.util.UUID;

/**
 * Created by rodrigo on 11/06/15.
 * Modified by lnacosta (laion.cj91@gmail.com) on 15/10/2015.
 */
public class VaultEventListeners extends AbstractWalletEventListener {

    private final ErrorManager               errorManager;
    private final EventManager               eventManager;
    private final LogManager                 logManager  ;
    private final CryptoVaultDatabaseActions dbActions   ;

    /**
     * Constructor with final params...
     */
    public VaultEventListeners(final Database     database    ,
                               final ErrorManager errorManager,
                               final EventManager eventManager,
                               final LogManager   logManager  ) {

        this.errorManager = errorManager;
        this.eventManager = eventManager;
        this.logManager   = logManager  ;

        this.dbActions = new CryptoVaultDatabaseActions(
                database,
                eventManager
        );
    }

    @Override
    public void onCoinsReceived(Wallet wallet, Transaction tx, Coin prevBalance, Coin newBalance) {
        logManager.log(BitcoinCryptoVaultPluginRoot.getLogLevelByClass(this.getClass().getName()), "CryptoVault information: Ney money received!!! New balance: " + newBalance.getValue(), null, null);
        /**
         * I save this transaction in the database
         */
        try {

            dbActions.saveIncomingTransaction(UUID.randomUUID(), tx.getHashAsString());
        } catch (Exception e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_BITCOIN_CRYPTO_VAULT, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
        }
    }

    @Override
    public void onCoinsSent(Wallet wallet, Transaction tx, Coin prevBalance, Coin newBalance) {
        try {
            logManager.log(BitcoinCryptoVaultPluginRoot.getLogLevelByClass(this.getClass().getName()), "Money sent.", "Prev Balance: " + prevBalance.getValue() + " New Balance:" + newBalance.getValue(), "Transaction: " + tx.toString());
        } catch (Exception e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_BITCOIN_CRYPTO_VAULT, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
        }
    }

    @Override
    public void onTransactionConfidenceChanged(Wallet wallet, Transaction tx) {
        logManager.log(BitcoinCryptoVaultPluginRoot.getLogLevelByClass(this.getClass().getName()), "Transaction confidence change detected!", "Transaction confidence changed. Transaction: " + tx, "Transaction confidence changed. Transaction: " + tx);

        System.out.println("\nTransaction HASH: "+tx.getHashAsString());

        TransactionConfidenceCalculator transactionConfidenceCalculator = new TransactionConfidenceCalculator(tx, wallet);
        CryptoStatus cryptoStatus;
        try {
            cryptoStatus = transactionConfidenceCalculator.getCryptoStatus();
        } catch (CantCalculateTransactionConfidenceException e) {
            cryptoStatus = CryptoStatus.ON_CRYPTO_NETWORK;
        }
        System.out.println("\nTransaction ACTUAL CRYPTOSTATUS: "+cryptoStatus);
        try {
            switch (cryptoStatus){
                case ON_CRYPTO_NETWORK:
                    raiseTransactionEvent(EventType.INCOMING_CRYPTO_ON_CRYPTO_NETWORK);
                    break;
                case REVERSED_ON_CRYPTO_NETWORK:
                    insertNewTransactionAndRaiseEvent(tx.getHashAsString(), cryptoStatus, EventType.INCOMING_CRYPTO_REVERSED_ON_CRYPTO_NETWORK);
                    break;
                case ON_BLOCKCHAIN:
                    insertNewTransactionAndRaiseEvent(tx.getHashAsString(), cryptoStatus, EventType.INCOMING_CRYPTO_ON_BLOCKCHAIN);
                    break;
                case REVERSED_ON_BLOCKCHAIN:
                    insertNewTransactionAndRaiseEvent(tx.getHashAsString(), cryptoStatus, EventType.INCOMING_CRYPTO_REVERSED_ON_BLOCKCHAIN);
                    break;
                case IRREVERSIBLE:
                    insertNewTransactionAndRaiseEvent(tx.getHashAsString(), cryptoStatus, EventType.INCOMING_CRYPTO_IRREVERSIBLE);
                    break;
                default:
                    throw new UnexpectedCryptoStatusException(
                            "cryptoStatus: "+cryptoStatus.name()+" - code: "+cryptoStatus.getCode(),
                            "Unexpected Crypto Status detected."
                    );
            }
        } catch (Exception e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_BITCOIN_CRYPTO_VAULT, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
        }
    }

    /**
     * Create the transaction in the DB and raise the event.
     *
     * @param hash         of the transaction.
     * @param cryptoStatus which we want to save.
     * @param eventType    type of event i want to raise.
     *
     * @throws CantExecuteQueryException if something goes wrong when i'm trying to insert the record.
     */
    private void insertNewTransactionAndRaiseEvent(final String       hash        ,
                                                   final CryptoStatus cryptoStatus,
                                                   final EventType    eventType   ) throws CantExecuteQueryException {

        dbActions.insertNewTransactionWithNewConfidence(hash, cryptoStatus, dbActions.calculateTransactionType(hash));
        raiseTransactionEvent(eventType);
    }

    private void raiseTransactionEvent(final EventType eventType) {

        FermatEvent transactionEvent = eventManager.getNewEvent(eventType);
        transactionEvent.setSource(EventSource.CRYPTO_VAULT);
        eventManager.raiseEvent(transactionEvent);
    }
}
