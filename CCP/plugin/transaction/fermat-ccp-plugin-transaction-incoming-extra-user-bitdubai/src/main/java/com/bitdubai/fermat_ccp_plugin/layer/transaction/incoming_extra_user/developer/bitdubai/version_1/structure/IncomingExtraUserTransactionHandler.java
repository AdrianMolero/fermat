package com.bitdubai.fermat_ccp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.all_definition.enums.ReferenceWallet;
import com.bitdubai.fermat_api.layer.all_definition.events.EventSource;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.Transaction;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.crypto_transactions.CryptoTransaction;
import com.bitdubai.fermat_api.layer.dmp_basic_wallet.common.exceptions.CantRegisterDebitException;
import com.bitdubai.fermat_api.layer.dmp_basic_wallet.common.exceptions.CantLoadWalletException;
import com.bitdubai.fermat_api.layer.dmp_basic_wallet.common.exceptions.CantRegisterCreditException;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.bitcoin_wallet.interfaces.BitcoinWalletManager;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.bitcoin_wallet.interfaces.DealsWithBitcoinWallet;
import com.bitdubai.fermat_cry_api.layer.crypto_module.crypto_address_book.exceptions.CantGetCryptoAddressBookRecordException;
import com.bitdubai.fermat_cry_api.layer.crypto_module.crypto_address_book.exceptions.CryptoAddressBookRecordNotFoundException;
import com.bitdubai.fermat_cry_api.layer.crypto_module.crypto_address_book.interfaces.DealsWithCryptoAddressBook;
import com.bitdubai.fermat_cry_api.layer.crypto_module.crypto_address_book.interfaces.CryptoAddressBookManager;
import com.bitdubai.fermat_cry_api.layer.crypto_module.crypto_address_book.interfaces.CryptoAddressBookRecord;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.enums.EventType;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.events.IncomingMoneyNotificationEvent;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.interfaces.DealsWithEvents;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.interfaces.EventManager;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;

/**
 * Created by eze on 2015.06.22.
 *
 */
public class IncomingExtraUserTransactionHandler implements DealsWithEvents,DealsWithBitcoinWallet, DealsWithCryptoAddressBook {

    /*
     * DealsWithBitcoinWallet Interface member variables
    */
    private BitcoinWalletManager bitcoinWalletManager;

    /*
    * DealsWithCryptoAddressBook member variables
    */
    private CryptoAddressBookManager cryptoAddressBookManager;

    /**
     * DealsWithEventManager
     */
    private EventManager eventManager;

    /*
     * DealsWithBitcoinWallet Interface method implementation
    */
    @Override
    public void setBitcoinWalletManager(BitcoinWalletManager bitcoinWalletManager) {
        this.bitcoinWalletManager = bitcoinWalletManager;
    }

    /*
    * DealsWithCryptoAddressBook method implementation
    */
    @Override
    public void setCryptoAddressBookManager(CryptoAddressBookManager cryptoAddressBookManager) {
        this.cryptoAddressBookManager = cryptoAddressBookManager;
    }

    public void handleTransaction(Transaction<CryptoTransaction> transaction) throws CantGetCryptoAddressBookRecordException, CantLoadWalletException, CantRegisterCreditException, CantRegisterDebitException, com.bitdubai.fermat_ccp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.exceptions.UnexpectedTransactionException {
        try {
            try {
                CryptoAddressBookRecord cryptoAddressBookRecord = this.cryptoAddressBookManager.getCryptoAddressBookRecordByCryptoAddress(transaction.getInformation().getAddressTo());
                ReferenceWallet         referenceWallet         = cryptoAddressBookRecord.getWalletType();
                String                  walletPublicKey         = cryptoAddressBookRecord.getWalletPublicKey();

                com.bitdubai.fermat_ccp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.util.TransactionExecutorFactory executorFactory = new com.bitdubai.fermat_ccp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.util.TransactionExecutorFactory(this.bitcoinWalletManager, this.cryptoAddressBookManager);
                com.bitdubai.fermat_ccp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.interfaces.TransactionExecutor executor        = executorFactory.newTransactionExecutor(referenceWallet, walletPublicKey);

                executor.executeTransaction(transaction);

                launchIncomingMoneyNotificationEvent(cryptoAddressBookRecord,transaction);

            } catch (CryptoAddressBookRecordNotFoundException exception) {
                //TODO LUIS we should define what is going to happen in this case, in the meantime we throw an exception
                throw new CantGetCryptoAddressBookRecordException(CantGetCryptoAddressBookRecordException.DEFAULT_MESSAGE, exception, "", "Check the cause to see what happened");
            }
        } catch (CantGetCryptoAddressBookRecordException | CantLoadWalletException | CantRegisterCreditException | CantRegisterDebitException | com.bitdubai.fermat_ccp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.exceptions.UnexpectedTransactionException e){
            throw e;
        } catch (Exception exception) {
            //TODO LUIS we should define what is going to happen in this case, in the meantime we throw an exception
            throw new CantGetCryptoAddressBookRecordException(CantGetCryptoAddressBookRecordException.DEFAULT_MESSAGE, exception, "", "Check the cause to see what happened");
        }
    }

    private void launchIncomingMoneyNotificationEvent(CryptoAddressBookRecord cryptoAddressBookRecord,Transaction<CryptoTransaction> transaction) {
        FermatEvent platformEvent = eventManager.getNewEvent(EventType.INCOMING_MONEY_NOTIFICATION);
        IncomingMoneyNotificationEvent incomingMoneyNotificationEvent =  (IncomingMoneyNotificationEvent) platformEvent;
        incomingMoneyNotificationEvent.setSource(EventSource.INCOMING_EXTRA_USER);
        incomingMoneyNotificationEvent.setActorId(cryptoAddressBookRecord.getDeliveredToActorPublicKey());
        //incomingMoneyNotificationEvent.setActorType(Actors.getByCode(cryptoAddressBookRecord.getDeliveredToActorPublicKey()));
        incomingMoneyNotificationEvent.setAmount(transaction.getInformation().getCryptoAmount());
        incomingMoneyNotificationEvent.setCryptoCurrency(transaction.getInformation().getCryptoCurrency());
        incomingMoneyNotificationEvent.setWalletPublicKey(cryptoAddressBookRecord.getWalletPublicKey());
        eventManager.raiseEvent(platformEvent);
    }

    @Override
    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }
}
