package com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces;

import com.bitdubai.fermat_api.Plugin;
import com.bitdubai.fermat_api.layer.dmp_engine.sub_app_runtime.enums.SubApps;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.interfaces.WalletDescriptorFactoryProjectManager;
import com.bitdubai.fermat_api.layer.dmp_module.wallet_factory.interfaces.WalletFactoryManager;
import com.bitdubai.fermat_api.layer.dmp_module.wallet_publisher.interfaces.WalletPublisherManager;
import com.bitdubai.fermat_api.layer.dmp_module.wallet_store.interfaces.WalletStoreModuleManager;
import com.bitdubai.fermat_pip_api.layer.pip_module.developer.interfaces.ToolManager;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.ErrorManager;

import java.util.Map;

/**
 * Created by mati on 2015.07.20..
 */
public interface SubAppSessionManager {


    public SubAppsSession openSubAppSession(SubApps subApps, ErrorManager errorManager, WalletFactoryManager walletFactoryManager, ToolManager toolManager,WalletStoreModuleManager walletStoreModuleManager,WalletPublisherManager walletPublisherManager);
    public boolean closeSubAppSession(SubApps subApps);
    public Map<SubApps,SubAppsSession> listOpenSubApps();
    public boolean isSubAppOpen(SubApps subApps);
    public SubAppsSession getSubAppsSession(SubApps subAppType);

}
