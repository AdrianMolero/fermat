package com.bitdubai.fermat_api.layer.dmp_middleware.wallet_manager.interfaces;

import java.util.List;
import java.util.UUID;

/**
 * The Class <code>com.bitdubai.fermat_api.layer.middleware.wallet_factory.interfaces.WalletManagerManager</code>
 * indicates the functionality of a WalletManagerManager
 * <p/>
 *
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 09/07/15.
 * @version 1.0
 * @since Java JDK 1.7
 */
public interface WalletManagerManager {
    // TODO: Discutir con Matías los parámetros más convenientes a utilizar
    public List<InstalledWallet> getInstalledWallets();
    public void openWallet(UUID walletIdInThisDevice);
    public void uninstallWallet(UUID walletIdInThisDevice);
    public void installWallet(WalletInstallationInformation walletInstallationInformation);
}