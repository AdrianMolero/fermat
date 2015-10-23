package com.bitdubai.fermat_core;

import com.bitdubai.fermat_api.layer.all_definition.common.abstract_classes.AbstractPlugin;
import com.bitdubai.fermat_api.layer.all_definition.common.exceptions.CantPausePluginException;
import com.bitdubai.fermat_api.layer.all_definition.common.exceptions.CantResumePluginException;
import com.bitdubai.fermat_api.layer.all_definition.common.exceptions.CantStartPluginException;
import com.bitdubai.fermat_api.layer.all_definition.common.exceptions.CantStopPluginException;
import com.bitdubai.fermat_api.layer.all_definition.common.exceptions.UnexpectedServiceStatusException;
import com.bitdubai.fermat_api.layer.all_definition.common.exceptions.VersionNotFoundException;
import com.bitdubai.fermat_api.layer.all_definition.common.utils.PluginVersionReference;

/**
 * The class <code>com.bitdubai.fermat_core.FermatPluginManager</code>
 * centralizes all start actions for the plugins of fermat.
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 23/10/2015.
 */
public class FermatPluginManager {

    private final FermatSystemContext systemContext;

    public FermatPluginManager(final FermatSystemContext systemContext) {

        this.systemContext = systemContext;
    }

    public final void startPlugin(final PluginVersionReference pluginVersionReference) throws CantStartPluginException,
                                                                                              VersionNotFoundException {

        AbstractPlugin abstractPlugin = systemContext.getPluginVersion(pluginVersionReference);

        startPlugin(abstractPlugin);

    }

    public final void startPlugin(final AbstractPlugin abstractPlugin) throws CantStartPluginException {

        if (abstractPlugin.isStarted())
            return;

        try {
            abstractPlugin.start();
        } catch (com.bitdubai.fermat_api.CantStartPluginException e) {

            throw new CantStartPluginException(
                    e,
                    abstractPlugin.getPluginVersionReference().toString(),
                    "There was a captured problem during the plugin start."
            );
        } catch (Exception e) {

            throw new CantStartPluginException(
                    e,
                    abstractPlugin.getPluginVersionReference().toString(),
                    "Unhandled exception trying to start the plugin."
            );
        }

    }

    public final void stopPlugin(final PluginVersionReference pluginVersionReference) throws CantStopPluginException          ,
                                                                                             VersionNotFoundException         ,
                                                                                             UnexpectedServiceStatusException {

        AbstractPlugin abstractPlugin = systemContext.getPluginVersion(pluginVersionReference);

        stopPlugin(abstractPlugin);

    }

    public final void stopPlugin(final AbstractPlugin abstractPlugin) throws CantStopPluginException          ,
                                                                             UnexpectedServiceStatusException {

        if (!abstractPlugin.isStarted()) {
            throw new UnexpectedServiceStatusException(
                    "Service Status: "+abstractPlugin.getStatus()+" || "+abstractPlugin.getPluginVersionReference().toString(),
                    "The plugin cannot be stopped because is not started."
            );
        }

        try {

            abstractPlugin.stop();

        } catch (Exception e) {
            throw new CantStopPluginException(e, abstractPlugin.toString(), "Unhandled exception trying to stop the plugin.");
        }
    }

    public final void pausePlugin(final PluginVersionReference pluginVersionReference) throws CantPausePluginException         ,
                                                                                              VersionNotFoundException         ,
                                                                                              UnexpectedServiceStatusException {

        AbstractPlugin abstractPlugin = systemContext.getPluginVersion(pluginVersionReference);

        pausePlugin(abstractPlugin);

    }

    public final void pausePlugin(final AbstractPlugin abstractPlugin) throws CantPausePluginException         ,
                                                                              UnexpectedServiceStatusException {

        if (!abstractPlugin.isStarted()) {
            throw new UnexpectedServiceStatusException(
                    "Service Status: "+abstractPlugin.getStatus()+" || "+abstractPlugin.getPluginVersionReference().toString(),
                    "The plugin cannot be paused because is not started."
            );
        }

        try {

            abstractPlugin.pause();

        } catch (Exception e) {
            throw new CantPausePluginException(e, abstractPlugin.toString(), "Unhandled exception trying to pause the plugin.");
        }
    }

    public final void resumePlugin(final PluginVersionReference pluginVersionReference) throws CantResumePluginException        ,
                                                                                               VersionNotFoundException         ,
                                                                                               UnexpectedServiceStatusException {

        AbstractPlugin abstractPlugin = systemContext.getPluginVersion(pluginVersionReference);

        resumePlugin(abstractPlugin);

    }

    public final void resumePlugin(final AbstractPlugin abstractPlugin) throws CantResumePluginException         ,
            UnexpectedServiceStatusException {

        if (!abstractPlugin.isPaused()) {
            throw new UnexpectedServiceStatusException(
                    "Service Status: "+abstractPlugin.getStatus()+" || "+abstractPlugin.getPluginVersionReference().toString(),
                    "The plugin cannot be resumed because is not paused."
            );
        }

        try {

            abstractPlugin.pause();

        } catch (Exception e) {
            throw new CantResumePluginException(e, abstractPlugin.toString(), "Unhandled exception trying to resume the plugin.");
        }
    }
}
