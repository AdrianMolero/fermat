/*
 * @#AssetTransmissionMsjContentType.java - 2015
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package com.bitdubai.fermat_dap_plugin.layer.network.service.asset.transmission.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;

/**
 * The Class <code>com.bitdubai.fermat_dap_plugin.layer.network.service.asset.transmission.developer.bitdubai.version_1.structure.AssetTransmissionMsjContentType</code>
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 12/10/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public enum AssetTransmissionMsjContentType {

    /**
     * Definition types
     */
    META_DATA_TRANSMIT        ("MDT"),
    TRANSACTION_STATUS_UPDATE ("TSU");

    /**
     * Represent the code of the message status
     */
    private final String code;

    /**
     * Constructor whit parameter
     *
     * @param code the valid code
     */
    AssetTransmissionMsjContentType(String code) {
        this.code = code;
    }

    /**
     * Return a string code
     *
     * @return String that represent of the message status
     */
    public String getCode()   { return this.code ; }

    /**
     * Return the enum by the code
     *
     * @param code the valid code
     * @return MessagesStatus enum
     * @throws InvalidParameterException error with is no a valid code
     */
    public static AssetTransmissionMsjContentType getByCode(String code) throws InvalidParameterException {

        switch (code) {
            case "MDT":
                return AssetTransmissionMsjContentType.META_DATA_TRANSMIT;
            case "TSU":
                return AssetTransmissionMsjContentType.TRANSACTION_STATUS_UPDATE;
        }

        /**
         * If we try to convert am invalid string.
         */
        throw new InvalidParameterException(code);
    }

}
