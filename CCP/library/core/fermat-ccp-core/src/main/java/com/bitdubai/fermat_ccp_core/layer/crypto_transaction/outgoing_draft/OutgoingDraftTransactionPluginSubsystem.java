package com.bitdubai.fermat_ccp_core.layer.crypto_transaction.outgoing_draft;

import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
//import com.bitdubai.fermat_ccp_plugin.layer.crypto_transaction.outgoing_draft.developer.bitdubai.DeveloperBitDubai;
import com.bitdubai.fermat_core_api.layer.all_definition.system.abstract_classes.AbstractPluginSubsystem;
import com.bitdubai.fermat_core_api.layer.all_definition.system.exceptions.CantStartSubsystemException;

/**
 * Created by mati on 2016.02.25..
 */
public class OutgoingDraftTransactionPluginSubsystem extends AbstractPluginSubsystem {

    public OutgoingDraftTransactionPluginSubsystem() {
        super(new PluginReference(Plugins.CCP_OUTGOING_DRAFT_TRANSACTION));
    }
    @Override
    public void start() throws CantStartSubsystemException {
        try {
            //registerDeveloper(new DeveloperBitDubai());
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            throw new CantStartSubsystemException(e, null, null);
        }
    }

}
