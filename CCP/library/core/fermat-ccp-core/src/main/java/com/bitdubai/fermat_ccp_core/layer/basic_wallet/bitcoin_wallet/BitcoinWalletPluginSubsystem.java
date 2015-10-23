package com.bitdubai.fermat_ccp_core.layer.basic_wallet.bitcoin_wallet;

import com.bitdubai.fermat_api.PluginDeveloper;
import com.bitdubai.fermat_api.layer.all_definition.common.abstract_classes.AbstractPluginSubsystem;
import com.bitdubai.fermat_api.layer.all_definition.common.exceptions.CantStartSubsystemException;
import com.bitdubai.fermat_api.layer.all_definition.common.utils.PluginReference;
import com.bitdubai.fermat_ccp_api.all_definition.enums.CCPPlugins;
import com.bitdubai.fermat_ccp_plugin.layer.basic_wallet.bitcoin_wallet.developer.bitdubai.DeveloperBitDubai;

/**
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 20/10/2015.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class BitcoinWalletPluginSubsystem extends AbstractPluginSubsystem {

    public BitcoinWalletPluginSubsystem() {
        super(new PluginReference(CCPPlugins.BITDUBAI_BITCOIN_WALLET_BASIC_WALLET));
    }

    @Override
    public void start() throws CantStartSubsystemException {
        try {
            PluginDeveloper developer = new DeveloperBitDubai();
            plugin = developer.getPlugin();
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            throw new CantStartSubsystemException(e, null, null);
        }
    }
}
