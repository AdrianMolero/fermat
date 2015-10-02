package unit.com.bitdubai.fermat_pip_plugin.layer.network_service.subapp_resources.developer.bitdubai.version_1.SubAppResourcesInstallationNetworkServicePluginRoot;

import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEventListener;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FileLifeSpan;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FilePrivacy;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginTextFile;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.enums.EventType;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.interfaces.EventManager;
import com.bitdubai.fermat_pip_plugin.layer.network_service.subapp_resources.developer.bitdubai.version_1.SubAppResourcesInstallationNetworkServicePluginRoot;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.UUID;

import static com.googlecode.catchexception.CatchException.caughtException;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by francisco on 30/09/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class GetLayoutResourceTest extends TestCase {

    /**
     * DealsWithErrors interface Mocked
     */
    @Mock
    ErrorManager errorManager;

    /**
     * UsesFileSystem Interface member variables.
     */
    @Mock
    PluginFileSystem pluginFileSystem;


    /**
     * DealWithEvents Iianterface member variables.
     */


    @Mock
    private FermatEventListener mockFermatEventListener;

    @Mock
    private EventManager mockEventManager;

    @Mock
    private Database mockDatabase;

    @Mock
    private PluginDatabaseSystem mockPluginDatabaseSystem;

    @Mock
    private PluginTextFile mockPluginTextFile;
    private String subAppType;

    SubAppResourcesInstallationNetworkServicePluginRoot subAppResourcesInstallationNetworkServicePluginRoot;

    @Before
    public void setUp() throws Exception {
        subAppResourcesInstallationNetworkServicePluginRoot = new SubAppResourcesInstallationNetworkServicePluginRoot();
        subAppResourcesInstallationNetworkServicePluginRoot.setPluginFileSystem(pluginFileSystem);

        subAppResourcesInstallationNetworkServicePluginRoot.setErrorManager(errorManager);
        subAppResourcesInstallationNetworkServicePluginRoot.setEventManager(mockEventManager);
        subAppResourcesInstallationNetworkServicePluginRoot.setPluginDatabaseSystem(mockPluginDatabaseSystem);

        when(mockEventManager.getNewListener(EventType.BEGUN_WALLET_INSTALLATION)).thenReturn(mockFermatEventListener);
        when(mockPluginDatabaseSystem.openDatabase(any(UUID.class), anyString())).thenReturn(mockDatabase);

        when(pluginFileSystem.getTextFile(any(UUID.class), anyString(), anyString(), any(FilePrivacy.class), any(FileLifeSpan.class))).thenReturn(mockPluginTextFile);

        when(mockPluginTextFile.getContent()).thenReturn("layoutContent");

        subAppResourcesInstallationNetworkServicePluginRoot.start();
    }

    @Test
    public void testgetImageResource_ReturnOk_ThrowsCantGetResourcesException() throws Exception {
        //catchException(subAppResourcesInstallationNetworkServicePluginRoot).getLayoutResource("wallets_kids_fragment_balance.txt", ScreenOrientation.LANDSCAPE, UUID.randomUUID(),subAppType);
        //assertThat(caughtException()).isNull();
    }

    @Ignore
    @Test
    public void testcheckResources_TheResourcesRepositoryNotExist_ThrowsCantGetResourcesException() throws Exception {


        //catchException(walletResourcePluginRoot).getLayoutResource("wallets_kids_fragment_balance.txt");
        //assertThat(caughtException()).isInstanceOf(CantGetResourcesException.class);
        caughtException().printStackTrace();
    }

    @Ignore
    @Test
    public void testcheckResources_fileNotFound_ThrowsCantGetResourcesException() throws Exception {

        //catchException(walletResourcePluginRoot).getLayoutResource("layout1.xml");
        //assertThat(caughtException()).isInstanceOf(CantGetResourcesException.class);
        caughtException().printStackTrace();
    }
}
