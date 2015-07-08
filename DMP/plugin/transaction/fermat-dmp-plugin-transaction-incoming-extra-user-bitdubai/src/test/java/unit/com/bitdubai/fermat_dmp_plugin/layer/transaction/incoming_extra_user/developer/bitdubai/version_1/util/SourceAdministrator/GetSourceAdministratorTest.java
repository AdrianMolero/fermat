package unit.com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.util.SourceAdministrator;

import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.TransactionProtocolManager;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.crypto_transactions.CryptoTransaction;
import com.bitdubai.fermat_api.layer.pip_platform_service.event_manager.EventSource;
import com.bitdubai.fermat_cry_api.layer.crypto_router.incoming_crypto.IncomingCryptoManager;
import com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.structure.IncomingExtraUserRegistry;
import com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.util.SourceAdministrator;

import static org.fest.assertions.api.Assertions.*;
import static org.mockito.Mockito.*;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by jorgegonzalez on 2015.07.08..
 */
@RunWith(MockitoJUnitRunner.class)
public class GetSourceAdministratorTest {

    @Mock
    private IncomingCryptoManager mockIncomingCryptoManager;
    @Mock
    private TransactionProtocolManager<CryptoTransaction> mockManager;

    private SourceAdministrator testSourceAdministrator;

    @Test
    public void GetSourceAdministrator(){
        when(mockIncomingCryptoManager.getTransactionManager()).thenReturn(mockManager);

        testSourceAdministrator = new SourceAdministrator();
        testSourceAdministrator.setIncomingCryptoManager(mockIncomingCryptoManager);

        assertThat(testSourceAdministrator.getSourceAdministrator(EventSource.CRYPTO_VAULT)).isNotNull();
    }
}
