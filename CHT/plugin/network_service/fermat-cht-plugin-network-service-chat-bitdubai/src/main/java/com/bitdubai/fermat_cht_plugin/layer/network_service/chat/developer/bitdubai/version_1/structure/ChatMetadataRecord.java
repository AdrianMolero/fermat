package com.bitdubai.fermat_cht_plugin.layer.network_service.chat.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.all_definition.components.enums.PlatformComponentType;
import com.bitdubai.fermat_api.layer.all_definition.util.XMLParser;
import com.bitdubai.fermat_cht_api.all_definition.enums.MessageStatus;
import com.bitdubai.fermat_cht_api.layer.network_service.chat.enums.ChatMessageStatus;
import com.bitdubai.fermat_cht_api.layer.network_service.chat.enums.ChatProtocolState;
import com.bitdubai.fermat_cht_api.layer.network_service.chat.enums.DistributionStatus;
import com.bitdubai.fermat_cht_api.layer.network_service.chat.interfaces.ChatMetadata;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by Gabriel Araujo on 06/01/16.
 */
public class ChatMetadataRecord implements ChatMetadata{

    private UUID transactionId;

    private String transactionHash;

    private UUID chatId;

    private UUID objectId;

    private PlatformComponentType localActorType;

    private String localActorPublicKey;

    private PlatformComponentType remoteActorType;

    private String remoteActorPublicKey;

    private String chatName;

    private ChatMessageStatus chatMessageStatus;

    private MessageStatus messageStatus;

    private Timestamp date;

    private UUID messageId;

    private String message;

    private DistributionStatus distributionStatus;

    private ChatProtocolState chatProtocolState;

    private Timestamp sentDate;

    private boolean flagReadead;

    private int sentCount;

    private UUID responseToNotificationId;

    public ChatProtocolState getChatProtocolState() {
        return chatProtocolState;
    }

    public void changeState(ChatProtocolState chatProtocolState) {
        this.chatProtocolState = chatProtocolState;
    }

    /**
     * Represent the value of processed
     */
    private String processed;

    public Timestamp getSentDate() {
        return sentDate;
    }

    public void setSentDate(Timestamp sentDate) {
        this.sentDate = sentDate;
    }

    public boolean isFlagReadead() {
        return flagReadead;
    }

    public void setFlagReadead(boolean flagReadead) {
        this.flagReadead = flagReadead;
    }

    public int getSentCount() {
        return sentCount;
    }

    public void setSentCount(int sentCount) {
        this.sentCount = sentCount;
    }

    public UUID getResponseToNotificationId() {
        return responseToNotificationId;
    }

    public void setResponseToNotificationId(UUID responseToNotificationId) {
        this.responseToNotificationId = responseToNotificationId;
    }

    @Override
    public String toString() {
        ChatMetadata chatMetadata = this;
        return XMLParser.parseObject(chatMetadata);
    }

    /**
     * This method checks if the ChatMetadataRecord if completed filled before sending.
     * @return
     */
    public boolean isFilled(){
        if(this.chatId == null)
            return false;
        if(this.transactionId == null)
            return false;
        if(this.transactionHash == null || this.transactionHash.isEmpty())
            return false;
        if(this.objectId == null)
            return false;
        if(this.localActorType == null)
            return false;
        if(this.localActorPublicKey == null || this.localActorPublicKey.isEmpty())
            return false;
        if(this.remoteActorType == null)
            return false;
        if(this.remoteActorPublicKey == null || this.remoteActorPublicKey.isEmpty())
            return false;
        if(this.chatName == null || this.chatName.isEmpty())
            return false;
        if(this.chatMessageStatus == null)
            return false;
        if(this.messageStatus == null)
            return false;
        if(this.date == null)
            return false;
        if(this.messageId == null)
            return false;
        if(this.message == null || message.isEmpty())
            return false;
        if(this.distributionStatus == null)
            return false;
//        if(this.processed == null || processed.isEmpty())
//            return false;
//        if(this.chatProtocolState == null)
//            return false;
//        if(this.sentDate == null)
//            return false;
//        if(this.responseToNotificationId == null)
//            return false;
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(!(obj instanceof ChatMetadataRecord))
            return false;
        if(this.chatId != ((ChatMetadataRecord) obj).getChatId())
            return false;
        if(this.transactionId != ((ChatMetadataRecord) obj).getTransactionId())
            return false;
        if(!Objects.equals(this.transactionHash, ((ChatMetadataRecord) obj).getTransactionHash()))
            return false;
        if(this.objectId != ((ChatMetadataRecord) obj).getObjectId())
            return false;
        if(this.localActorType != ((ChatMetadataRecord) obj).getLocalActorType())
            return false;
        if(!Objects.equals(this.localActorPublicKey, ((ChatMetadataRecord) obj).getLocalActorPublicKey()))
            return false;
        if(this.remoteActorType != ((ChatMetadataRecord) obj).getRemoteActorType())
            return false;
        if(!Objects.equals(this.remoteActorPublicKey, ((ChatMetadataRecord) obj).getRemoteActorPublicKey()))
            return false;
        if(!Objects.equals(this.chatName, ((ChatMetadataRecord) obj).getChatName()))
            return false;
        if(this.chatMessageStatus != ((ChatMetadataRecord) obj).getChatMessageStatus())
            return false;
        if(this.messageStatus != ((ChatMetadataRecord) obj).getMessageStatus())
            return false;
        if(this.date != ((ChatMetadataRecord) obj).getDate())
            return false;
        if(this.messageId != ((ChatMetadataRecord) obj).getMessageId())
            return false;
        if(!Objects.equals(this.message, ((ChatMetadataRecord) obj).getMessage()))
            return false;
        if(this.distributionStatus != ((ChatMetadataRecord) obj).getDistributionStatus())
            return false;

        if(!Objects.equals(this.processed, ((ChatMetadataRecord) obj).getProcessed()))
            return false;
        return true;
 }

    /**
     * Represent the value of PROCESSED
     */
    public final static String PROCESSED = "Y";

    /**
     * Represent the value of NO_PROCESSED
     */
    public final static String NO_PROCESSED = "N";

    /**
     * Get the Processed
     * @return String
     */
    public String getProcessed() {
        return processed;
    }

    /**
     * Set the Processed
     * @param processed
     */
    public void setProcessed(String processed) {
        this.processed = processed;
    }

    /**
     *
     * @return
     */
    public String getTransactionHash() {
        return transactionHash;
    }

    /**
     *
     * @param transactionHash
     */
    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    /**
     *
     * @return
     */
    public UUID getTransactionId() {
        return transactionId;
    }

    /**
     *
     * @param transactionId
     */
    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    /**
     *
     * @return
     */
    @Override
    public UUID getChatId() {
        return chatId;
    }

    /**
     *
     * @param chatId
     */
    public void setChatId(UUID chatId) {
        this.chatId = chatId;
    }

    /**
     *
     * @return
     */
    @Override
    public MessageStatus getMessageStatus() {
        return messageStatus;
    }

    /**
     *
     * @param messageStatus
     */
    public void setMessageStatus(MessageStatus messageStatus) {
        this.messageStatus = messageStatus;
    }

    /**
     *
     * @return
     */
    @Override
    public DistributionStatus getDistributionStatus() {
        return distributionStatus;
    }

    /**
     *
     * @param distributionStatus
     */
    public void setDistributionStatus(DistributionStatus distributionStatus) {
        this.distributionStatus = distributionStatus;
    }

    /**
     *
     * @return
     */
    @Override
    public PlatformComponentType getLocalActorType() {
        return localActorType;
    }

    /**
     *
     * @param localActorType
     */
    public void setLocalActorType(PlatformComponentType localActorType) {
        this.localActorType = localActorType;
    }

    /**
     *
     * @return
     */
    @Override
    public String getLocalActorPublicKey() {
        return localActorPublicKey;
    }

    /**
     *
     * @param localActorPublicKey
     */
    public void setLocalActorPublicKey(String localActorPublicKey) {
        this.localActorPublicKey = localActorPublicKey;
    }

    /**
     *
     * @return
     */
    @Override
    public PlatformComponentType getRemoteActorType() {
        return remoteActorType;
    }

    /**
     *
     * @param remoteActorType
     */
    public void setRemoteActorType(PlatformComponentType remoteActorType) {
        this.remoteActorType = remoteActorType;
    }

    /**
     *
     * @return
     */
    @Override
    public String getRemoteActorPublicKey() {
        return remoteActorPublicKey;
    }

    /**
     *
     * @param remoteActorPublicKey
     */
    public void setRemoteActorPublicKey(String remoteActorPublicKey) {
        this.remoteActorPublicKey = remoteActorPublicKey;
    }

    /**
     *
     * @return
     */
    @Override
    public String getChatName() {
        return chatName;
    }

    /**
     *
     * @param chatName
     */
    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    /**
     *
     * @return
     */
    @Override
    public ChatMessageStatus getChatMessageStatus() {
        return chatMessageStatus;
    }

    /**
     *
     * @param chatMessageStatus
     */
    public void setChatMessageStatus(ChatMessageStatus chatMessageStatus) {
        this.chatMessageStatus = chatMessageStatus;
    }

    /**
     *
     * @return
     */
    @Override
    public Timestamp getDate() {
        return date;
    }

    /**
     *
     * @param date
     */
    public void setDate(Timestamp date) {
        this.date = date;
    }

    /**
     *
     * @return
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }


    /**
     *
     * @return
     */
    @Override
    public UUID getObjectId() {
        return objectId;
    }

    /**
     *
     * @param objectId
     */
    public void setObjectId(UUID objectId) {
        this.objectId = objectId;
    }

    /**
     *
     * @return
     */
    @Override
    public UUID getMessageId() {
        return messageId;
    }

    /**
     *
     * @param messageId
     */
    public void setMessageId(UUID messageId) {
        this.messageId = messageId;
    }

}
