package com.bitdubai.fermat_core.layer.ccp_module.notification;

import com.bitdubai.fermat_api.Plugin;
import com.bitdubai.fermat_api.PluginDeveloper;
import com.bitdubai.fermat_api.layer.ccp_module.CantStartSubsystemException;
import com.bitdubai.fermat_api.layer.ccp_module.ModuleSubsystem;
import com.bitdubai.fermat_pip_plugin.layer.module.notification.developer.bitdubai.DeveloperBitDubai;


/**
 * Created by Matias Furszyfer on 2015.09.03..
 */
public class NotificationSubSystem implements ModuleSubsystem {

    Plugin plugin;

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void start() throws CantStartSubsystemException {

        /**
         * I will choose from the different versions available of this functionality.
         */

        try {
            PluginDeveloper developer = new DeveloperBitDubai();
            plugin =  developer.getPlugin();

        }
        catch (Exception e)
        {
            System.err.println("Exception: " + e.getMessage());
            throw new CantStartSubsystemException();
        }
    }
}
