package com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.enums;

/**
 * The Class <code>com.bitdubai.fermat_api.layer.middleware.wallet_factory.enums.FactoryProjectState</code>
 * enumerates type of Resources.
 * <p/>
 *
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 15/07/15.
 * @version 1.0
 * @since Java JDK 1.7
 */
public enum FactoryProjectState {
    IN_PROGRESS("In_Progress"),//draft
    CLOSED("Closed"),//versioned
    PUBLISHED("Published"),
    DELETED("Deleted");//dismissed

    public static FactoryProjectState getByCode(String key) {
        switch(key) {
            case"In_Progress":
                return IN_PROGRESS;
            case"Closed":
                return CLOSED;
            case"Published":
                return PUBLISHED;
            case"Deleted":
                return DELETED;
        }
        //throw new InvalidParameterException(key);
        return null;
    }

    //Modified by Manuel perez on 05/08/2015
    private String code;

    FactoryProjectState(String code) {
        this.code = code;
    }

    public String value() { return this.code; }
}
