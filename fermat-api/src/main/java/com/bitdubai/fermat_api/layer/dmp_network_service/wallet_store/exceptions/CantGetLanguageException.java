package com.bitdubai.fermat_api.layer.dmp_network_service.wallet_store.exceptions;

import com.bitdubai.fermat_api.FermatException;

/**
 * Created by rodrigo on 7/21/15.
 */
public class CantGetLanguageException extends FermatException {
    public CantGetLanguageException(String message, Exception cause, String context, String possibleReason) {
        super(message, cause, context, possibleReason);
    }
}