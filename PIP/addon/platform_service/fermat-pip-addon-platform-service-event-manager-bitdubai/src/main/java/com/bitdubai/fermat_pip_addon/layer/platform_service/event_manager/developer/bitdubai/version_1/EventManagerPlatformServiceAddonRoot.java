package com.bitdubai.fermat_pip_addon.layer.platform_service.event_manager.developer.bitdubai.version_1;

import com.bitdubai.fermat_api.Addon;
import com.bitdubai.fermat_api.Service;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer.all_definition.enums.interfaces.FermatEnum;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.interfaces.DealsWithEventMonitor;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.interfaces.FermatEventMonitor;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.interfaces.FermatEvent;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.interfaces.FermatEventListener;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.interfaces.EventManager;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.enums.EventType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ciencias on 23.01.15.
 * Updated by Leon Acosta (laion.cj91@gmail.com) on 22-08-2015.
 */
public class EventManagerPlatformServiceAddonRoot implements Addon, EventManager, DealsWithEventMonitor, Service,Serializable {

    /**
     * Service Interface member variables.
     */
    ServiceStatus serviceStatus = ServiceStatus.CREATED;

    /**
     * DealsWithEventMonitor member variables
     */
    private FermatEventMonitor fermatEventMonitor;

    /**
     * EventManager Interface member variables.
     */
    Map<String, List<FermatEventListener>> listenersMap = new HashMap<>();

    /**
     * EventManager Interface implementation.
     */
    @Override
    public FermatEventListener getNewListener(EventType eventType) {
        return eventType.getListener(fermatEventMonitor);
    }

    @Override
    public FermatEvent getNewEvent(EventType eventType) {
        return eventType.getEvent();
    }

    @Override
    public void addListener(FermatEventListener listener) {

        String eventKey = buildMapKey(listener.getEventType());

        List<FermatEventListener> listenersList = listenersMap.get(eventKey);

        if (listenersList == null)
            listenersList = new ArrayList<>();

        listenersList.add(listener);

        listenersMap.put(eventKey, listenersList);
    }

    @Override
    public void removeListener(FermatEventListener listener) {

        String eventKey = buildMapKey(listener.getEventType());

        List<FermatEventListener> listenersList = listenersMap.get(eventKey);

        listenersList.remove(listener);

        listenersMap.put(eventKey, listenersList);

        listener.setEventHandler(null);

    }

    @Override
    public void raiseEvent(FermatEvent fermatEvent) {

        String eventKey = buildMapKey(fermatEvent.getEventType());

        List<FermatEventListener> listenersList = listenersMap.get(eventKey);

        if (listenersList != null) {
            for (FermatEventListener fermatEventListener : listenersList) {
                fermatEventListener.raiseEvent(fermatEvent);
            }
        }
    }

    private String buildMapKey(FermatEnum fermatEnum) {
        return fermatEnum.getPlatform().getCode()+fermatEnum.getCode();
    }

    /**
     * Service Interface implementation.
     */
    @Override
    public void start() {
        this.serviceStatus = ServiceStatus.STARTED;
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
        return serviceStatus;
    }


    /**
     * DealsWithEventMonitor interface implementation.
     */
    @Override
    public void setFermatEventMonitor(FermatEventMonitor fermatEventMonitor) {
        this.fermatEventMonitor = fermatEventMonitor;
    }
}
