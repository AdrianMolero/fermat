package com.bitdubai.fermat_api.layer.dmp_module.wallet_store.interfaces;

import com.bitdubai.fermat_api.layer.all_definition.enums.NicheWallet;
import com.bitdubai.fermat_api.layer.all_definition.enums.WalletCategory;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_language.exceptions.CantGetWalletLanguageException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_store.enums.InstallationStatus;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_store.exceptions.CantGetItemInformationException;
import com.bitdubai.fermat_api.layer.dmp_network_service.wallet_store.exceptions.CantGetDeveloperException;
import com.bitdubai.fermat_api.layer.dmp_network_service.wallet_store.exceptions.CantGetSkinException;
import com.bitdubai.fermat_api.layer.dmp_network_service.wallet_store.interfaces.DetailedCatalogItem;
import com.bitdubai.fermat_api.layer.dmp_network_service.wallet_store.interfaces.Language;

import java.util.List;
import java.util.UUID;

/**
 * Created by eze on 2015.07.17..
 */
public interface WalletStoreDetailedCatalogItem extends DetailedCatalogItem{

    public String getDeveloperName() throws CantGetDeveloperException;
    public String getDeveloperPublicKey() throws CantGetDeveloperException;
    public WalletStoreSkin getSkin(UUID skinId) throws CantGetSkinException, CantGetItemInformationException;
    public WalletStoreLanguage getLanguage (UUID languageId) throws CantGetWalletLanguageException, CantGetItemInformationException;


}
