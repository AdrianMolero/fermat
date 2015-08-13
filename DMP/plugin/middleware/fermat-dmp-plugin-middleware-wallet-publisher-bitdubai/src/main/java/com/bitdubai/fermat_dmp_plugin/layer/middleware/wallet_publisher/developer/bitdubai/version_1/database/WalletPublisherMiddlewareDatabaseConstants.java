/*
 * @#WalletPublisherMiddlewareDatabaseConstants.java - 2015
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package com.bitdubai.fermat_dmp_plugin.layer.middleware.wallet_publisher.developer.bitdubai.version_1.database;

/**
 * The Class <code>com.bitdubai.fermat_dmp_plugin.layer.middleware.wallet_publisher.developer.bitdubai.version_1.database.WalletPublisherMiddlewareDatabaseConstants</code>
 * keeps constants the column names of the database.<p/>
 * <p/>
 *
 * Created by Roberto Requena - (rart3001@gmail.com) on 04/08/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class WalletPublisherMiddlewareDatabaseConstants {

    /**
     * Represent the name of the data base
     */
    public static final String DATA_BASE_NAME  = "component_publisher_data_base";

    /**
     * COMPONENT PUBLISHED INFORMATION database table definition.
     */
    static final String COMPONENT_PUBLISHED_INFORMATION_TABLE_NAME = "component_published_information";

    static final String COMPONENT_PUBLISHED_INFORMATION_ID_COLUMN_NAME = "id";
    static final String COMPONENT_PUBLISHED_INFORMATION_WFP_ID_COLUMN_NAME = "wfp_id";
    static final String COMPONENT_PUBLISHED_INFORMATION_WFP_NAME_COLUMN_NAME = "wfp_name";
    static final String COMPONENT_PUBLISHED_INFORMATION_WALLET_ID_COLUMN_NAME = "wallet_id";
    static final String COMPONENT_PUBLISHED_INFORMATION_CATALOG_ID_COLUMN_NAME = "catalog_id";
    static final String COMPONENT_PUBLISHED_INFORMATION_STORE_ID_COLUMN_NAME = "store_id";
    static final String COMPONENT_PUBLISHED_INFORMATION_SCREEN_SIZE_COLUMN_NAME = "screen_size";
    static final String COMPONENT_PUBLISHED_INFORMATION_INITIAL_WALLET_VERSION_COLUMN_NAME = "initial_wallet_version";
    static final String COMPONENT_PUBLISHED_INFORMATION_FINAL_WALLET_VERSION_COLUMN_NAME = "final_wallet_version";
    static final String COMPONENT_PUBLISHED_INFORMATION_INITIAL_PLATFORM_VERSION_COLUMN_NAME = "initial_platform_version";
    static final String COMPONENT_PUBLISHED_INFORMATION_FINAL_PLATFORM_VERSION_COLUMN_NAME = "final_platform_version";
    static final String COMPONENT_PUBLISHED_INFORMATION_COMPONENT_TYPE_COLUMN_NAME = "component_type";
    static final String COMPONENT_PUBLISHED_INFORMATION_VERSION_COLUMN_NAME = "version";
    static final String COMPONENT_PUBLISHED_INFORMATION_VERSION_TIMESTAMP_COLUMN_NAME = "version_timestamp";
    static final String COMPONENT_PUBLISHED_INFORMATION_STATUS_COLUMN_NAME = "status";
    static final String COMPONENT_PUBLISHED_INFORMATION_STATUS_TIMESTAMP_COLUMN_NAME = "status_timestamp";
    static final String COMPONENT_PUBLISHED_INFORMATION_PUBLICATION_TIMESTAMP_COLUMN_NAME = "publication_timestamp";
    static final String COMPONENT_PUBLISHED_INFORMATION_PUBLISHER_ID_COLUMN_NAME = "publisher_id";

    static final String COMPONENT_PUBLISHED_INFORMATION_FIRST_KEY_COLUMN = "id";

}