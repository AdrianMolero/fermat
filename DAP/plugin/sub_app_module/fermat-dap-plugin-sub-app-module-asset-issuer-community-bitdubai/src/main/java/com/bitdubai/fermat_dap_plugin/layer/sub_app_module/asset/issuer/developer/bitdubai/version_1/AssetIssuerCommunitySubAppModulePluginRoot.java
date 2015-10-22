package com.bitdubai.fermat_dap_plugin.layer.sub_app_module.asset.issuer.developer.bitdubai.version_1;

import com.bitdubai.fermat_api.CantStartPluginException;
import com.bitdubai.fermat_api.Plugin;
import com.bitdubai.fermat_api.Service;
import com.bitdubai.fermat_api.layer.all_definition.developer.LogManagerForDevelopers;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEventListener;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.DealsWithLogger;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogLevel;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogManager;
import com.bitdubai.fermat_dap_api.layer.all_definition.exceptions.CantSetObjectException;
import com.bitdubai.fermat_dap_api.layer.dap_actor.asset_issuer.exceptions.CantGetAssetIssuerActorsException;
import com.bitdubai.fermat_dap_api.layer.dap_actor.asset_issuer.interfaces.ActorAssetIssuer;
import com.bitdubai.fermat_dap_api.layer.dap_actor.asset_issuer.interfaces.ActorAssetIssuerManager;
import com.bitdubai.fermat_dap_api.layer.dap_actor.asset_issuer.interfaces.DealsWithActorAssetIssuer;
import com.bitdubai.fermat_dap_api.layer.dap_sub_app_module.asset_issuer_community.interfaces.AssetIssuerCommunitySubAppModuleManager;
import com.bitdubai.fermat_dap_plugin.layer.sub_app_module.asset.issuer.developer.bitdubai.version_1.structure.AssetIssuerCommunitySupAppModuleManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Nerio on 13/10/15.
 */
public class AssetIssuerCommunitySubAppModulePluginRoot implements AssetIssuerCommunitySubAppModuleManager, DealsWithActorAssetIssuer, DealsWithLogger, LogManagerForDevelopers, Plugin, Service {

    UUID pluginId;

    /**
     * DealsWithLogger interface member variable
     */
    LogManager logManager;

    static Map<String, LogLevel> newLoggingLevel = new HashMap<String, LogLevel>();

    /**
     * Service Interface member variables.
     */
    ServiceStatus serviceStatus = ServiceStatus.CREATED;
    List<FermatEventListener> listenersAdded = new ArrayList<>();

    AssetIssuerCommunitySupAppModuleManager assetIssuerCommunitySubAppModuleManager;

    ActorAssetIssuerManager actorAssetIssuerManager;

    @Override
    public void setId(UUID pluginId) {
        this.pluginId = pluginId;
    }

    @Override
    public void start() throws CantStartPluginException {
        assetIssuerCommunitySubAppModuleManager = new AssetIssuerCommunitySupAppModuleManager(actorAssetIssuerManager);

        this.serviceStatus = ServiceStatus.STARTED;
    }

    @Override
    public void setActorAssetIssuerManager(ActorAssetIssuerManager actorAssetIssuerManager) throws CantSetObjectException {
        this.actorAssetIssuerManager = actorAssetIssuerManager;
    }

    @Override
    public void pause() {
        this.serviceStatus = ServiceStatus.PAUSED;
    }

    @Override
    public void resume() {
        this.serviceStatus = ServiceStatus.STARTED;
    }

    @Override
    public void stop() {
        this.serviceStatus = ServiceStatus.STOPPED;
    }

    @Override
    public ServiceStatus getStatus() {
        return this.serviceStatus;
    }

    @Override
    public void setLogManager(LogManager logManager) {
        this.logManager = logManager;
    }

    @Override
    public List<String> getClassesFullPath() {
        List<String> returnedClasses = new ArrayList<String>();
        returnedClasses.add("com.bitdubai.fermat_dap_plugin.layer.sub_app_module.asset.issuer.developer.bitdubai.version_1.AssetIssuerCommunitySubAppModulePluginRoot");
        /**
         * I return the values.
         */
        return returnedClasses;
    }

    @Override
    public void setLoggingLevelPerClass(Map<String, LogLevel> newLoggingLevel) {
        /**
         * I will check the current values and update the LogLevel in those which is different
         */
        for (Map.Entry<String, LogLevel> pluginPair : newLoggingLevel.entrySet()) {
            /**
             * if this path already exists in the Root.bewLoggingLevel I'll update the value, else, I will put as new
             */
            if (AssetIssuerCommunitySubAppModulePluginRoot.newLoggingLevel.containsKey(pluginPair.getKey())) {
                AssetIssuerCommunitySubAppModulePluginRoot.newLoggingLevel.remove(pluginPair.getKey());
                AssetIssuerCommunitySubAppModulePluginRoot.newLoggingLevel.put(pluginPair.getKey(), pluginPair.getValue());
            } else {
                AssetIssuerCommunitySubAppModulePluginRoot.newLoggingLevel.put(pluginPair.getKey(), pluginPair.getValue());
            }
        }
    }

    @Override
    public List<ActorAssetIssuer> getAllActorAssetIssuerRegistered() throws CantGetAssetIssuerActorsException {
        List<ActorAssetIssuer> actorAssetList = new ArrayList<>();

//        Location location = new DeviceLocation(00.00, 00.00, 12345678910L, 00.00, LocationProvider.NETWORK);

//        actorAssetList.add(new AssetIssuerActorRecord("Rodrigo Acosta",UUID.randomUUID().toString(),new byte[0],location));
//        actorAssetList.add(new AssetIssuerActorRecord("Franklin Marcano",UUID.randomUUID().toString(),new byte[0],location));
//        actorAssetList.add(new AssetIssuerActorRecord("Manuel Colmenares",UUID.randomUUID().toString(),new byte[0],location));
//        actorAssetList.add(new AssetIssuerActorRecord("Nerio Indriago",UUID.randomUUID().toString(),new byte[0],location));
        try {
            actorAssetList = actorAssetIssuerManager.getAllAssetIssuerActorRegistered();
        } catch (CantGetAssetIssuerActorsException e) {
            e.printStackTrace();
        }

        return actorAssetList;
    }
}
