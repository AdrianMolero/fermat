package com.bitdubai.fermat_ccp_plugin.layer.network_service.crypto_addresses.developer.bitdubai.version_1.communication.exceptions;

import com.bitdubai.fermat_api.FermatException;

/**
 * The exception <code>com.bitdubai.fermat_ccp_plugin.layer.network_service.crypto_payment_request.developer.bitdubai.version_1.network_service.exceptions.CantDeleteRecordDataBaseException</code>
 * is thrown when there is an error trying to delete a record from database.
 * <p>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 06/10/2015.
 */
public class CantDeleteRecordDataBaseException extends FermatException {

    private static final String DEFAULT_MESSAGE = "CAN'T DELETE RECORD IN DATABASE EXCEPTION";

    public CantDeleteRecordDataBaseException(String message, Exception cause, String context, String possibleReason) {
        super(message, cause, context, possibleReason);
    }

    public CantDeleteRecordDataBaseException(Exception cause, String context, String possibleReason) {
        this(DEFAULT_MESSAGE, cause, context, possibleReason);
    }

}
