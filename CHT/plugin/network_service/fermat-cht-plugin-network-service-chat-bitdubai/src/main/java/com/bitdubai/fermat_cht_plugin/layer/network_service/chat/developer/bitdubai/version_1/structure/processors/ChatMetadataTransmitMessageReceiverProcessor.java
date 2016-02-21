/*
 * @#DigitalAssetMetadataTransmitMessageReceiverProcessor.java - 2015
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package com.bitdubai.fermat_cht_plugin.layer.network_service.chat.developer.bitdubai.version_1.structure.processors;

import com.bitdubai.fermat_api.layer.all_definition.crypto.util.CryptoHasher;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.util.XMLParser;
import com.bitdubai.fermat_cht_api.all_definition.enums.MessageStatus;
import com.bitdubai.fermat_cht_api.layer.network_service.chat.enums.ChatMessageStatus;
import com.bitdubai.fermat_cht_api.layer.network_service.chat.enums.ChatMessageTransactionType;
import com.bitdubai.fermat_cht_api.layer.network_service.chat.events.IncomingChat;
import com.bitdubai.fermat_cht_plugin.layer.network_service.chat.developer.bitdubai.version_1.ChatNetworkServicePluginRoot;
import com.bitdubai.fermat_cht_plugin.layer.network_service.chat.developer.bitdubai.version_1.communications.CommunicationNetworkServiceConnectionManager;
import com.bitdubai.fermat_cht_plugin.layer.network_service.chat.developer.bitdubai.version_1.structure.ChatMetadataTransactionRecord;
import com.bitdubai.fermat_cht_plugin.layer.network_service.chat.developer.bitdubai.version_1.structure.ChatTransmissionJsonAttNames;
import com.bitdubai.fermat_cht_api.layer.network_service.chat.enums.DistributionStatus;
import com.bitdubai.fermat_cht_api.all_definition.events.enums.EventType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.contents.FermatMessageCommunication;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.contents.FermatMessage;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.enums.FermatMessagesStatus;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.google.gson.JsonObject;

import java.util.UUID;

/**
 * The Class <code>com.bitdubai.fermat_cht_plugin.layer.network_service.chat.developer.bitdubai.version_1.structure.processors.ChatMetadataTransmitMessageReceiverProcessor</code> is
 *
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 12/10/15.
 * Implemented by Gabriel Araujo to CHT
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class ChatMetadataTransmitMessageReceiverProcessor extends FermatMessageProcessor {

    /**
     * Constructor with parameters
     * @param chatNetworkServicePluginRoot
     */
    public ChatMetadataTransmitMessageReceiverProcessor(ChatNetworkServicePluginRoot chatNetworkServicePluginRoot) {
        super(chatNetworkServicePluginRoot);
    }

    /**
     * (non-javadoc)
     *
     * @see FermatMessageProcessor#processingMessage(FermatMessage, JsonObject)
     */
    @Override
    public void processingMessage(FermatMessage fermatMessage, JsonObject jsonMsjContent) {

        try {

            /*
             * Get the XML representation of the Chat Message MetaData
             */
            String chatMetadataXml                  = jsonMsjContent.get(ChatTransmissionJsonAttNames.CHAT_METADATA).getAsString();
            /*
             * Convert the xml to object
             */
            ChatMetadataTransactionRecord chatMetadataTransactionRecord = (ChatMetadataTransactionRecord) XMLParser.parseXML(chatMetadataXml, new ChatMetadataTransactionRecord());


            /*
             * Construct a new digitalAssetMetadataTransaction
            */

            /*
             * Save into data base for audit control
             */
            //get the transactions an UUID
            chatMetadataTransactionRecord.setTransactionId(getChatNetworkServicePluginRoot().getChatMetaDataDao().getNewUUID(UUID.randomUUID().toString()));
            String transactionHash = CryptoHasher.performSha256(chatMetadataTransactionRecord.getChatId().toString() + chatMetadataTransactionRecord.getMessageId().toString());
            chatMetadataTransactionRecord.setTransactionHash(transactionHash);
            chatMetadataTransactionRecord.setChatMessageStatus(ChatMessageStatus.CREATED_CHAT);
            chatMetadataTransactionRecord.setMessageStatus(MessageStatus.CREATED);
            chatMetadataTransactionRecord.setDistributionStatus(DistributionStatus.DELIVERING);
            chatMetadataTransactionRecord.setProcessed(ChatMetadataTransactionRecord.NO_PROCESSED);
            getChatNetworkServicePluginRoot().getChatMetaDataDao().create(chatMetadataTransactionRecord);

            /*
             * Mark the message as read
             */
            ((FermatMessageCommunication)fermatMessage).setFermatMessagesStatus(FermatMessagesStatus.READ);
            ((CommunicationNetworkServiceConnectionManager) getChatNetworkServicePluginRoot().getNetworkServiceConnectionManager()).getIncomingMessageDao().update(fermatMessage);

            /*
             * Notify to the interested
             */
            IncomingChat event = (IncomingChat) getChatNetworkServicePluginRoot().getEventManager().getNewEvent(EventType.INCOMING_CHAT);
            event.setChatId(chatMetadataTransactionRecord.getChatId());
            event.setSource(ChatNetworkServicePluginRoot.EVENT_SOURCE);
            getChatNetworkServicePluginRoot().getEventManager().raiseEvent(event);
            //System.out.println("ChatNetworkServicePluginRoot - Incoming Chat fired!");

        } catch (Exception e) {
            getChatNetworkServicePluginRoot().getErrorManager().reportUnexpectedPluginException(Plugins.CHAT_NETWORK_SERVICE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
        }

    }

    @Override
    public ChatMessageTransactionType getChatMessageTransactionType() {
        return ChatMessageTransactionType.CHAT_METADATA_TRASMIT;
    }


}
