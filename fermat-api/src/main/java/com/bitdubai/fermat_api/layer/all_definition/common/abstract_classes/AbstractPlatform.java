package com.bitdubai.fermat_api.layer.all_definition.common.abstract_classes;

import com.bitdubai.fermat_api.layer.all_definition.common.exceptions.AddonNotFoundException;
import com.bitdubai.fermat_api.layer.all_definition.common.exceptions.CantRegisterLayerException;
import com.bitdubai.fermat_api.layer.all_definition.common.exceptions.CantStartLayerException;
import com.bitdubai.fermat_api.layer.all_definition.common.exceptions.CantStartPlatformException;
import com.bitdubai.fermat_api.layer.all_definition.common.exceptions.CantStartPluginIdsManagerException;
import com.bitdubai.fermat_api.layer.all_definition.common.exceptions.DeveloperNotFoundException;
import com.bitdubai.fermat_api.layer.all_definition.common.exceptions.LayerNotFoundException;
import com.bitdubai.fermat_api.layer.all_definition.common.exceptions.PluginNotFoundException;
import com.bitdubai.fermat_api.layer.all_definition.common.exceptions.VersionNotFoundException;
import com.bitdubai.fermat_api.layer.all_definition.common.utils.AddonDeveloperReference;
import com.bitdubai.fermat_api.layer.all_definition.common.utils.AddonReference;
import com.bitdubai.fermat_api.layer.all_definition.common.utils.AddonVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.common.utils.PluginDeveloperReference;
import com.bitdubai.fermat_api.layer.all_definition.common.utils.LayerReference;
import com.bitdubai.fermat_api.layer.all_definition.common.utils.PlatformReference;
import com.bitdubai.fermat_api.layer.all_definition.common.utils.PluginReference;
import com.bitdubai.fermat_api.layer.all_definition.common.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PlatformFileSystem;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The class <code>com.bitdubai.fermat_api.layer.all_definition.common.abstract_classes.AbstractPlatform</code>
 * contains all the basic functionality of a Platform class.
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 20/10/2015.
 */
public abstract class AbstractPlatform {

    private Map<LayerReference, AbstractLayer> layers;

    private final PlatformReference        platformReference;

    public AbstractPlatform(final PlatformReference platformReference) {

        this.layers            = new ConcurrentHashMap<>();
        this.platformReference = platformReference;
    }

    /**
     * Throw the method <code>registerLayer</code> you can add new layers to the platform.
     * Here we'll corroborate too that the layer is not added twice.
     *
     * @param abstractLayer  layer instance.
     *
     * @throws CantRegisterLayerException if something goes wrong.
     */
    protected final void registerLayer(final AbstractLayer abstractLayer) throws CantRegisterLayerException {

        LayerReference layerReference = abstractLayer.getLayerReference();
        layerReference.setPlatformReference(platformReference);

        try {

            if(layers.containsKey(layerReference))
                throw new CantRegisterLayerException("layer: " + layerReference.toString(), "Layer already exists in this platform.");

            abstractLayer.start();

            layers.put(
                    layerReference,
                    abstractLayer
            );

        } catch (final CantStartLayerException e) {

            throw new CantRegisterLayerException(e, "layer: " + layerReference.toString(), "Error trying to start the layer.");
        }
    }

    public final AbstractLayer getLayer(final LayerReference layerReference) throws LayerNotFoundException {
        if (layers.containsKey(layerReference))
            return layers.get(layerReference);
        else
            throw new LayerNotFoundException("layer: "+layerReference.getLayer(), "layer not found.");
    }

    /**
     * Throw the method <code>getAddonVersion</code> you can get a addon version instance passing like parameter a version reference instance.
     *
     * @param addonVersionReference addon version reference data.
     *
     * @return a addon version instance.
     *
     * @throws VersionNotFoundException   if we can't find a addon version with the given version reference parameters.
     */
    public final AbstractAddon getAddonVersion(final AddonVersionReference addonVersionReference) throws VersionNotFoundException {

        try {

            return getAddonDeveloper(addonVersionReference.getAddonDeveloperReference()).getAddonByVersion(addonVersionReference);

        } catch (DeveloperNotFoundException e) {

            throw new VersionNotFoundException(e, addonVersionReference.toString(), "addon version not found in the platform of the system context.");
        }
    }

    /**
     * Throw the method <code>getAddonDeveloper</code> you can get a addonDeveloper instance passing like parameter a developer reference instance.
     *
     * @param addonDeveloperReference addon developer reference data.
     *
     * @return a addon developer instance.
     *
     * @throws DeveloperNotFoundException   if we can't find a addon developer with the given developer reference parameters.
     */
    public final AbstractAddonDeveloper getAddonDeveloper(final AddonDeveloperReference addonDeveloperReference) throws DeveloperNotFoundException {

        try {

            return getAddonSubsystem(addonDeveloperReference.getAddonReference()).getDeveloperByReference(addonDeveloperReference);

        } catch (AddonNotFoundException e) {

            throw new DeveloperNotFoundException(e, addonDeveloperReference.toString(), "addon developer not found in the platform of the system context.");
        }
    }

    public final AbstractAddonSubsystem getAddonSubsystem(final AddonReference addonReference) throws AddonNotFoundException {

        try {

            return getLayer(addonReference.getLayerReference()).getAddon(addonReference);

        } catch (LayerNotFoundException e) {

            throw new AddonNotFoundException(e, "addon: "+addonReference.toString(), "layer not found for the specified addon.");
        }
    }

    /**
     * Throw the method <code>getPluginVersion</code> you can get a plugin version instance passing like parameter a version reference instance.
     *
     * @param pluginVersionReference plugin version reference data.
     *
     * @return a plugin version instance.
     *
     * @throws VersionNotFoundException   if we can't find a plugin version with the given version reference parameters.
     */
    public final AbstractPlugin getPluginVersion(final PluginVersionReference pluginVersionReference) throws VersionNotFoundException {

        try {

            return getPluginDeveloper(pluginVersionReference.getPluginDeveloperReference()).getPluginByVersion(pluginVersionReference);

        } catch (DeveloperNotFoundException e) {

            throw new VersionNotFoundException(e, pluginVersionReference.toString(), "plugin version not found in the platform of the system context.");
        }
    }

    /**
     * Throw the method <code>getPluginDeveloper</code> you can get a pluginDeveloper instance passing like parameter a developer reference instance.
     *
     * @param pluginDeveloperReference plugin developer reference data.
     *
     * @return a plugin developer instance.
     *
     * @throws DeveloperNotFoundException   if we can't find a plugin developer with the given developer reference parameters.
     */
    public final AbstractPluginDeveloper getPluginDeveloper(final PluginDeveloperReference pluginDeveloperReference) throws DeveloperNotFoundException {

        try {

            return getPluginSubsystem(pluginDeveloperReference.getPluginReference()).getDeveloperByReference(pluginDeveloperReference);

        } catch (PluginNotFoundException e) {

            throw new DeveloperNotFoundException(e, pluginDeveloperReference.toString(), "plugin developer not found in the platform of the system context.");
        }
    }

    public final AbstractPluginSubsystem getPluginSubsystem(final PluginReference pluginReference) throws PluginNotFoundException {

        try {

            return getLayer(pluginReference.getLayerReference()).getPlugin(pluginReference);

        } catch (LayerNotFoundException e) {

            throw new PluginNotFoundException(e, "plugin: "+pluginReference.toString(), "layer not found for the specified plugin.");
        }
    }

    public final PlatformReference getPlatformReference() {
        return platformReference;
    }

    public abstract void start() throws CantStartPlatformException;

}
