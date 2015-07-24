package com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.interfaces;

import com.bitdubai.fermat_api.layer.all_definition.enums.Languages;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.Wallet;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Wallets;
import com.bitdubai.fermat_api.layer.all_definition.resources_structure.Language;
import com.bitdubai.fermat_api.layer.all_definition.resources_structure.Resource;
import com.bitdubai.fermat_api.layer.all_definition.resources_structure.Skin;
import com.bitdubai.fermat_api.layer.all_definition.resources_structure.enums.ResourceType;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.exceptions.CantAddLanguageStringException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.exceptions.CantAddResourceToSkinException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.exceptions.CantCopyWalletFactoryProjectLanguageException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.exceptions.CantCopyWalletFactoryProjectSkinException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.exceptions.CantCreateEmptyWalletFactoryProjectLanguageException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.exceptions.CantCreateEmptyWalletFactoryProjectSkinException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.exceptions.CantCreateWalletFactoryProjectException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.exceptions.CantCreateWalletFactoryProjectProposalException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.exceptions.CantDeleteLanguageStringException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.exceptions.CantDeleteResourceFromSkinException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.exceptions.CantDeleteWalletFactoryProjectLanguageException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.exceptions.CantDeleteWalletFactoryProjectProposalException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.exceptions.CantDeleteWalletFactoryProjectSkinException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.exceptions.CantGetLanguageException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.exceptions.CantGetWalletFactoryProjectException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.exceptions.CantGetWalletFactoryProjectLanguageException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.exceptions.CantGetWalletFactoryProjectLanguagesException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.exceptions.CantGetWalletFactoryProjectNavigationStructureException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.exceptions.CantGetWalletFactoryProjectProposalException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.exceptions.CantGetWalletFactoryProjectProposalsException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.exceptions.CantGetWalletFactoryProjectSkinException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.exceptions.CantGetWalletFactoryProjectSkinStructureException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.exceptions.CantGetWalletFactoryProjectSkinsException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.exceptions.CantGetWalletFactoryProjectsException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.exceptions.CantImportWalletFactoryProjectException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.exceptions.CantSetLanguageException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.exceptions.CantSetWalletFactoryProjectNavigationStructureException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.exceptions.CantSetWalletFactoryProjectSkinStructureException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.exceptions.CantUpdateResourceToSkinException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.exceptions.CantUpdateWalletFactoryProjectProposalException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.exceptions.LanguageNotFoundException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.exceptions.ProjectNotFoundException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.exceptions.ProposalNotFoundException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.exceptions.SkinNotFoundException;

import java.util.List;
import java.util.UUID;

/**
 * The Class <code>com.bitdubai.fermat_api.layer.middleware.wallet_factory.interfaces.WalletFactoryManager</code>
 * indicates the functionality of a WalletFactoryManager
 * <p/>
 *
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 09/07/15.
 * @version 1.0
 * @since Java JDK 1.7
 */
public interface WalletFactoryManager {

    WalletFactoryProject createEmptyWalletFactoryProject(String name, Wallets walletType) throws CantCreateWalletFactoryProjectException;

    void importWalletFactoryProjectFromDevice(String newName, UUID resourcesId, UUID navigationStructureId) throws CantImportWalletFactoryProjectException;

    void importWalletFactoryProjectFromRepository(String newName, String repository) throws CantImportWalletFactoryProjectException;

    /*******************************************************************
     * PROJECT METHODS
     *******************************************************************/
    List<WalletFactoryProject> getAllWalletFactoryProjects() throws CantGetWalletFactoryProjectsException;

    WalletFactoryProject getWalletFactoryProject(String name) throws CantGetWalletFactoryProjectException, ProjectNotFoundException;



    /*******************************************************************
     * PROPOSAL METHODS
     *******************************************************************/

    /**
     * Proposal search
     */
    List<WalletFactoryProjectProposal> getProposals(WalletFactoryProject walletFactoryProject) throws CantGetWalletFactoryProjectProposalsException;

    WalletFactoryProjectProposal getProposalByName(String proposal, WalletFactoryProject walletFactoryProject) throws CantGetWalletFactoryProjectProposalException, ProposalNotFoundException;

    WalletFactoryProjectProposal getProposalById(UUID id) throws CantGetWalletFactoryProjectProposalException, ProposalNotFoundException;

    /**
     * Proposal management
     */
    public WalletFactoryProjectProposal createProposal(String alias, Wallets walletType, WalletFactoryProject walletFactoryProject) throws CantCreateWalletFactoryProjectProposalException;

    WalletFactoryProjectProposal copyProposal(String newAlias, WalletFactoryProjectProposal walletFactoryProjectProposal) throws CantCreateWalletFactoryProjectProposalException;

    void updateProposal(WalletFactoryProjectProposal walletFactoryProjectProposal) throws CantUpdateWalletFactoryProjectProposalException, ProposalNotFoundException;

    void deleteProposal(WalletFactoryProjectProposal walletFactoryProjectProposal) throws CantDeleteWalletFactoryProjectProposalException, ProposalNotFoundException;


    /*******************************************************************
     * NAVIGATION STRUCTURE METHODS
     *******************************************************************/

    // brings the navigation structure of the project proposal
    Wallet getNavigationStructure(WalletFactoryProjectProposal walletFactoryProjectProposal) throws CantGetWalletFactoryProjectNavigationStructureException;

    // converts an xml file in a navigation structure
    Wallet getNavigationStructure(String navigationStructure) throws CantGetWalletFactoryProjectNavigationStructureException;

    // converts the given navigation structure in an xml file.
    String getNavigationStructureXml(Wallet wallet) throws CantGetWalletFactoryProjectNavigationStructureException;

    // converts the given navigation structure in an xml file.
    void setNavigationStructureXml(Wallet wallet, WalletFactoryProjectProposal walletFactoryProjectProposal) throws CantSetWalletFactoryProjectNavigationStructureException;



    /*******************************************************************
     * SKIN METHODS
     *******************************************************************/

    /**
     * Skin search
     */
    List<WalletFactoryProjectSkin> getSkins(WalletFactoryProjectProposal walletFactoryProjectProposal) throws CantGetWalletFactoryProjectSkinsException;

    WalletFactoryProjectSkin getSkinById(UUID id) throws CantGetWalletFactoryProjectSkinException, SkinNotFoundException;

    /**
     * Skin management
     */

    // create a new empty skin
    WalletFactoryProjectSkin createEmptySkin(String name, WalletFactoryProjectProposal walletFactoryProjectProposal) throws CantCreateEmptyWalletFactoryProjectSkinException;

    // copies an existent skin
    WalletFactoryProjectSkin copySkin(String newName, WalletFactoryProjectSkin walletFactoryProjectSkin) throws CantCopyWalletFactoryProjectSkinException, SkinNotFoundException;

    // delete an existent skin
    void deleteSkin(WalletFactoryProjectSkin walletFactoryProjectSkin) throws CantDeleteWalletFactoryProjectSkinException, SkinNotFoundException;

    // brings the skin structure of the project skin
    Skin getSkinStructure(WalletFactoryProjectSkin walletFactoryProjectSkin) throws CantGetWalletFactoryProjectSkinStructureException;

    // converts an xml file in a skin structure
    Skin getSkinStructure(String skinStructure) throws CantGetWalletFactoryProjectSkinStructureException;

    // converts the given skin structure in an xml file.
    String getSkinStructureXml(Skin skin) throws CantGetWalletFactoryProjectSkinStructureException;

    // converts the given skin structure in an xml file.
    void setSkinStructureXml(Skin skin, WalletFactoryProjectSkin walletFactoryProjectSkin) throws CantSetWalletFactoryProjectSkinStructureException;

    void addResourceToSkin(Resource resource, byte[] file, WalletFactoryProjectSkin walletFactoryProjectSkin) throws CantAddResourceToSkinException;

    void updateResourceToSkin(Resource resource, byte[] file, WalletFactoryProjectSkin walletFactoryProjectSkin) throws CantUpdateResourceToSkinException;

    void deleteResourceFromSkin(Resource resource, WalletFactoryProjectSkin walletFactoryProjectSkin) throws CantDeleteResourceFromSkinException;



    /*******************************************************************
     * LANGUAGE METHODS
     *******************************************************************/

    /**
     * Language search
     */
    List<WalletFactoryProjectLanguage> getLanguages(WalletFactoryProjectProposal walletFactoryProjectProposal) throws CantGetWalletFactoryProjectLanguagesException;

    WalletFactoryProjectLanguage getLanguageById(UUID id) throws CantGetWalletFactoryProjectLanguageException, LanguageNotFoundException;

    /**
     * Language management
     */

    // create a new empty language
    WalletFactoryProjectLanguage createEmptyLanguage(String name, Languages type, WalletFactoryProjectProposal walletFactoryProjectProposal) throws CantCreateEmptyWalletFactoryProjectLanguageException;

    // copies an existent language
    WalletFactoryProjectLanguage copyLanguage(String newName, WalletFactoryProjectLanguage walletFactoryProjectLanguage) throws CantCopyWalletFactoryProjectLanguageException, LanguageNotFoundException;

    // delete an existent language
    void deleteLanguage(WalletFactoryProjectLanguage walletFactoryProjectLanguage) throws CantDeleteWalletFactoryProjectLanguageException, LanguageNotFoundException;

    // brings the language of the project language
    Language getLanguage(WalletFactoryProjectLanguage walletFactoryProjectLanguage) throws CantGetLanguageException;

    // converts an xml file in a skin language
    Language getLanguage(String skinStructure) throws CantGetLanguageException;

    // converts the given language in an xml file.
    String getLanguageXml(Language language) throws CantGetLanguageException;

    // converts the given skin language in an xml file.
    void setLanguageXml(Language language, WalletFactoryProjectLanguage walletFactoryProjectLanguage) throws CantSetLanguageException;

    void addLanguageString(String name, String value, WalletFactoryProjectLanguage walletFactoryProjectLanguage) throws CantAddLanguageStringException;

    void deleteLanguageString(String name, WalletFactoryProjectLanguage walletFactoryProjectLanguage) throws CantDeleteLanguageStringException;
}