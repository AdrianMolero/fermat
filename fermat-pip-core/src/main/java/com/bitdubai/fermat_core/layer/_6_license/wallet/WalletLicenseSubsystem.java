package com.bitdubai.fermat_core.layer._6_license.wallet;

import com.bitdubai.fermat_api.Addon;
import com.bitdubai.fermat_api.layer._6_license.CantStartSubsystemException;
import com.bitdubai.fermat_api.layer._6_license.LicenseSubsystem;
import com.bitdubai.fermat_core.layer._6_license.wallet.developer.DeveloperBitDubai;

/**
 * Created by ciencias on 21.01.15.
 */
public class WalletLicenseSubsystem implements LicenseSubsystem {

    private Addon addon;





    @Override
    public Addon getAddon() {
        return addon;
    }





    @Override
    public void start() throws CantStartSubsystemException {
        /**
         * I will choose from the different versions available of this functionality.
         */

        try {
            DeveloperBitDubai developerBitDubai = new DeveloperBitDubai();
            addon = developerBitDubai.getAddon();
        }
        catch (Exception e)
        {
            System.err.println("Exception: " + e.getMessage());
            throw new CantStartSubsystemException();
        }
    }


}
