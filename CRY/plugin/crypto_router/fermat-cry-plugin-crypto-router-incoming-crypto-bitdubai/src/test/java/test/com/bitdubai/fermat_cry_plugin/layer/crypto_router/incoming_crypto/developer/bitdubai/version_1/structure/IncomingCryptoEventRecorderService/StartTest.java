package test.com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.structure.IncomingCryptoEventRecorderService;

import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer.all_definition.event.EventType;
import com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.structure.IncomingCryptoEventRecorderService;
import com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.structure.IncomingCryptoRegistry;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.EventListener;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.EventManager;

import static org.fest.assertions.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.googlecode.catchexception.CatchException.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by Franklin Marcano 04/08/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class StartTest {
    @Mock
    private EventManager mockEventManager;
    @Mock
    private EventListener mockEventListener;

    @Mock
    private IncomingCryptoRegistry mockRegistry;

    private IncomingCryptoEventRecorderService testIncomingCryptoEventRecorderService;

    @Test
    public void Start_EventManagerSetWithValidEventListener_ServiceStarted() throws Exception{
        when(mockEventManager.getNewListener(any(EventType.class))).thenReturn(mockEventListener);
        mockRegistry = new IncomingCryptoRegistry();

        testIncomingCryptoEventRecorderService = new IncomingCryptoEventRecorderService();
        testIncomingCryptoEventRecorderService.setRegistry(mockRegistry);
        testIncomingCryptoEventRecorderService.setEventManager(mockEventManager);
        catchException(testIncomingCryptoEventRecorderService).start();
        assertThat(testIncomingCryptoEventRecorderService.getStatus()).isEqualTo(ServiceStatus.STARTED);
    }
}

