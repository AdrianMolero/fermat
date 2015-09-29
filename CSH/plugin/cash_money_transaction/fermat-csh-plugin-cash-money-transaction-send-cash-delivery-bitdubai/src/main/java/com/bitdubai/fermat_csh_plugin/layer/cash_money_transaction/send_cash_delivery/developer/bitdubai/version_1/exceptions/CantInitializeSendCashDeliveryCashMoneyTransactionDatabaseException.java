package com.bitdubai.fermat_csh_plugin.layer.cash_money_transaction.send_cash_delivery.developer.bitdubai.version_1.exceptions;

import com.bitdubai.fermat_api.FermatException;
/**
 * The Class <code>package com.bitdubai.fermat_csh_plugin.layer.cash_money_transaction.send_cash_delivery.developer.bitdubai.version_1.exceptions.CantInitializeSendCashDeliveryCashMoneyTransactionDatabaseException</code>
 * is thrown when an error occurs initializing database
 * <p/>
 *
 * Created by Yordin Alayn - (y.alayn@gmail.com) on 29/09/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class CantInitializeSendCashDeliveryCashMoneyTransactionDatabaseException extends FermatException {

    public static final String DEFAULT_MESSAGE = "CAN'T INTIALIZE SEND CASH DELIVERY CASH MONEY TRANSACTION DATABASE EXCEPTION";

    public CantInitializeSendCashDeliveryCashMoneyTransactionDatabaseException(final String message, final Exception cause, final String context, final String possibleReason) {
        super(message, cause, context, possibleReason);
    }

    public CantInitializeSendCashDeliveryCashMoneyTransactionDatabaseException(final String message, final Exception cause) {
        this(message, cause, "", "");
    }

    public CantInitializeSendCashDeliveryCashMoneyTransactionDatabaseException(final String message) {
        this(message, null);
    }

    public CantInitializeSendCashDeliveryCashMoneyTransactionDatabaseException() {
        this(DEFAULT_MESSAGE);
    }
}