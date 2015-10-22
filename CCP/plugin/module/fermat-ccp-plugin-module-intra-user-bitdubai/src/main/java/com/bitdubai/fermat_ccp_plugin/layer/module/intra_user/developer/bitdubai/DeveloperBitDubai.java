package com.bitdubai.fermat_ccp_plugin.layer.module.intra_user.developer.bitdubai;

import com.bitdubai.fermat_api.Plugin;
import com.bitdubai.fermat_api.PluginDeveloper;
import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrency;
import com.bitdubai.fermat_api.layer.all_definition.enums.TimeFrequency;
import com.bitdubai.fermat_api.layer.all_definition.license.PluginLicensor;
import com.bitdubai.fermat_ccp_plugin.layer.module.intra_user.developer.bitdubai.version_1.IntraWalletUserModulePluginRoot;

/**
 * Created by loui on 22/02/15.
 */
public class DeveloperBitDubai implements PluginDeveloper, PluginLicensor {

    Plugin plugin;

    public DeveloperBitDubai () {
        plugin = new IntraWalletUserModulePluginRoot();
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public int getAmountToPay() {
        return 100;
    }

    @Override
    public CryptoCurrency getCryptoCurrency() {
        return CryptoCurrency.BITCOIN;
    }

    @Override
    public String getAddress() {
        return "13gpMizSNvQCbJzAPyGCUnfUGqFD8ryzcv";
    }

    @Override
    public TimeFrequency getTimePeriod() {
        return TimeFrequency.MONTHLY;
    }
}
