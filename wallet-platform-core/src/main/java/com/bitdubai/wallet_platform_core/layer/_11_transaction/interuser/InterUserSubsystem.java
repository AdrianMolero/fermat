package com.bitdubai.wallet_platform_core.layer._11_transaction.interuser;

import com.bitdubai.wallet_platform_api.Plugin;
import com.bitdubai.wallet_platform_api.layer._11_transaction.CantStartSubsystemException;
import com.bitdubai.wallet_platform_api.layer._11_transaction.TransactionSubsystem;
import com.bitdubai.wallet_platform_core.layer._11_transaction.interuser.developer.bitdubai.DeveloperBitDubai;

/**
 * Created by loui on 16/02/15.
 */
public class InterUserSubsystem implements TransactionSubsystem {

    Plugin plugin;

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void start() throws CantStartSubsystemException {
        /**
         * I will choose from the different Developers available which implementation to use. Right now there is only
         * one, so it is not difficult to choose.
         */

        try {
            DeveloperBitDubai developerBitDubai = new DeveloperBitDubai();
            plugin = developerBitDubai.getPlugin();
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            throw new CantStartSubsystemException();
        }
    }
}