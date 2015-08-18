package com.bitdubai.fermat_dmp_plugin.layer.middleware.wallet_factory.developer.bitdubai.version_1.database;

/**
 * Created by rodrigo on 8/17/15.
 */
public class WalletFactoryMiddlewareDatabaseConstants {

    static final String DATABASE_NAME = "Factory";

    /**
     * Project database table definition.
     */
    static final String PROJECT_TABLE_NAME = "project";

    static final String PROJECT_PUBLICKEY_COLUMN_NAME = "publickey";
    static final String PROJECT_NAME_COLUMN_NAME = "name";
    static final String PROJECT_STATE_COLUMN_NAME = "state";
    static final String PROJECT_WALLETTYPE_COLUMN_NAME = "wallettype";
    static final String PROJECT_CREATION_TIMESTAMP_COLUMN_NAME = "creation_timestamp";
    static final String PROJECT_MODIFICATION_TIMESTAMP_COLUMN_NAME = "modification_timestamp";

    static final String PROJECT_FIRST_KEY_COLUMN = "publickey";

    /**
     * skin database table definition.
     */
    static final String SKIN_TABLE_NAME = "skin";

    static final String SKIN_PROJECT_PUBLICKEY_COLUMN_NAME = "project_publickey";
    static final String SKIN_SKIN_ID_COLUMN_NAME = "skin_id";
    static final String SKIN_DEFAULT_COLUMN_NAME = "default";

    static final String SKIN_FIRST_KEY_COLUMN = "project_publickey";

    /**
     * language database table definition.
     */
    static final String LANGUAGE_TABLE_NAME = "language";

    static final String LANGUAGE_PROJECT_PUBLICKEY_COLUMN_NAME = "project_publickey";
    static final String LANGUAGE_LANGUAGE_ID_COLUMN_NAME = "language_id";
    static final String LANGUAGE_DEFAULT_COLUMN_NAME = "default";

    static final String LANGUAGE_FIRST_KEY_COLUMN = "project_publickey";

    /**
     * navigation_structure database table definition.
     */
    static final String NAVIGATION_STRUCTURE_TABLE_NAME = "navigation_structure";

    static final String NAVIGATION_STRUCTURE_PROJECT_PUBLICKEY_COLUMN_NAME = "project_publickey";
    static final String NAVIGATION_STRUCTURE_LANGUAGE_ID_COLUMN_NAME = "language_id";

    static final String NAVIGATION_STRUCTURE_FIRST_KEY_COLUMN = "project_publickey";
}
