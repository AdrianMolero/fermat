package com.bitdubai.fermat_bch_plugin.layer.asset_vault.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.all_definition.enums.BlockchainNetworkType;
import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrency;
import com.bitdubai.fermat_api.layer.all_definition.money.CryptoAddress;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.BitcoinNetworkSelector;
import com.bitdubai.fermat_bch_api.layer.crypto_vault.asset_vault.exceptions.GetNewCryptoAddressException;
import com.bitdubai.fermat_bch_plugin.layer.asset_vault.developer.bitdubai.version_1.database.AssetsOverBitcoinCryptoVaultDao;
import com.bitdubai.fermat_bch_plugin.layer.asset_vault.developer.bitdubai.version_1.exceptions.CantExecuteDatabaseOperationException;
import com.bitdubai.fermat_bch_plugin.layer.asset_vault.developer.bitdubai.version_1.exceptions.CantInitializeAssetsOverBitcoinCryptoVaultDatabaseException;

import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicHierarchy;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The Class <code>com.bitdubai.fermat_bch_plugin.layer.cryptovault.assetsoverbitcoin.developer.bitdubai.version_1.structure.VaultKeyHierarchy</code>
 * Defines the internal Hierarchy object used on the Crypto Vault. The hierarchy is created from a root key each time the platform
 * is initiated. The Hierarchy is in charge of generating new bitcoin addresses when request from the public Keys derived for each account.
 * <p/>
 *
 * Created by Rodrigo Acosta - (acosta_rodrigo@hotmail.com) on 06/10/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
class VaultKeyHierarchy extends DeterministicHierarchy {

    /**
     * Holds the list of Accounts and master keys of the hierarchy
     */
    private Map<Integer, DeterministicKey> accountsMasterKeys;

    /**
     * Holds the DAO object to access the database
     */
    AssetsOverBitcoinCryptoVaultDao dao;

    /**
     * Platform variables
     */
    PluginDatabaseSystem pluginDatabaseSystem;
    UUID pluginId;

    /**
     * Constructor
     * @param rootKey (m) key
     */
    public VaultKeyHierarchy(DeterministicKey rootKey, PluginDatabaseSystem pluginDatabaseSystem, UUID pluginId) {
        super(rootKey);
        accountsMasterKeys = new HashMap<>();
        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.pluginId = pluginId;
    }

    /**
     * Generates a master Deterministic Key for the given account on the account path
     * These are the m/n paths...for example m/0 , m/1, ... m m/n
     * @param account
     */
    public void addVaultAccount(HierarchyAccount account){
        DeterministicKey accountMasterKey = this.deriveChild(account.getAccountPath(), false, true, ChildNumber.ZERO);
        accountsMasterKeys.put(account.getId(), accountMasterKey);
    }

    /**
     * Returns a public Key only from the specified account used to generate bitcoin addresses     *
     * @param hierarchyAccount
     * @return the fist key of the path m/HierarchyAccount/0. Example: m/0/0
     */
    private DeterministicKey getAddressPublicKeyFromAccount(HierarchyAccount hierarchyAccount){
        /**
         * gets the masterKey for this account
         */
        DeterministicKey masterKey = accountsMasterKeys.get(hierarchyAccount.getId());

        /**
         * Serialize the pubkey of the master key
         */
        byte[] pubKeyBytes = masterKey.getPubKey();
        byte[] chainCode = masterKey.getChainCode();

        return HDKeyDerivation.createMasterPubKeyFromBytes(pubKeyBytes, chainCode);
    }

    /**
     * Generates a new hierarchy on the path m/account/0 with only public keys
     * @param hierarchyAccount
     * @return a new hierarchy used to generate bitcoin addresses
     */
    public DeterministicHierarchy getAddressPublicHierarchyFromAccount(HierarchyAccount hierarchyAccount){
        DeterministicHierarchy deterministicHierarchy = new DeterministicHierarchy(getAddressPublicKeyFromAccount(hierarchyAccount));
        return deterministicHierarchy;
    }

    /**
     * Generates a Bitcoin Address from the specified networkType and Account.
     * It wil use the next available publicKey from the hierarchy for that account.
     * @param blockchainNetworkType
     * @return the crypto address
     */
    public CryptoAddress getBitcoinAddress(BlockchainNetworkType blockchainNetworkType, HierarchyAccount hierarchyAccount) throws GetNewCryptoAddressException {
        /**
         * The depth of the next available public key for this account
         */
        int pubKeyDepth = 0;
        try {
            pubKeyDepth = getNextAvailablePublicKeyDepth(hierarchyAccount);
        } catch (CantExecuteDatabaseOperationException e) {
            throw new GetNewCryptoAddressException(GetNewCryptoAddressException.DEFAULT_MESSAGE, e, "there was a problem getting the key depth from the database.", "database issue");
        }

        /**
         * I will derive a new public Key from this account
         */
        DeterministicHierarchy pubKeyHierarchy = getAddressPublicHierarchyFromAccount(hierarchyAccount);
        DeterministicKey pubKey = pubKeyHierarchy.deriveChild(pubKeyHierarchy.getRootKey().getPath(), true, true, new ChildNumber(pubKeyDepth, false));

        /**
         * I will create the CryptoAddress
         */
        String address = pubKey.toAddress(BitcoinNetworkSelector.getNetworkParameter(blockchainNetworkType)).toString();
        CryptoAddress cryptoAddress = new CryptoAddress(address, CryptoCurrency.BITCOIN);

        /**
         * I need to make the network that I used to generate the address active, if it is different than the default network.
         * BlockchainNetworkType has MainNet, RegTest and TestNet. The default value is the one used for the platform.
         * If the address generated is for a network different than default, I need to update the database so we start monitoring this network
         */
        if (blockchainNetworkType != BlockchainNetworkType.DEFAULT){
            setActiveNetwork(blockchainNetworkType);
        }

        return cryptoAddress;
    }

    /**
     * Updates the database to active a new network
     * @param blockchainNetworkType
     */
    private void setActiveNetwork(BlockchainNetworkType blockchainNetworkType) {
        //todo update table active_Networks and add (if missing) this blockchainNetworkType
    }

    /**
     * Get the net keyDepth that is available to generate a new key.
     * It sets this value in the database.
     * @param hierarchyAccount
     * @return
     */
    private int getNextAvailablePublicKeyDepth(HierarchyAccount hierarchyAccount) throws CantExecuteDatabaseOperationException {
        int returnValue = 0;
        int currentUsedKey = getDao().getCurrentUsedKeys(hierarchyAccount.getId());
        /**
         * I set the new value of the key depth
         */
        returnValue = currentUsedKey +1;

        /**
         * and Update this value in the database
         */
        getDao().setNewCurrentUsedKeyValue(hierarchyAccount.getId(), returnValue);

        return returnValue;
    }

    /**
     * gets the dao instance to access database operations
     * @return
     */
    private AssetsOverBitcoinCryptoVaultDao getDao(){
        if (dao == null){
            try {
                dao = new AssetsOverBitcoinCryptoVaultDao(pluginDatabaseSystem, pluginId);
            } catch (CantInitializeAssetsOverBitcoinCryptoVaultDatabaseException e) {
                e.printStackTrace();
            }
        }

        return dao;
    }
}
