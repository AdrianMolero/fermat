/*
 * @#WsCommunicationVPNClient.java - 2015
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package com.bitdubai.fermat_p2p_plugin.layer.ws.communications.cloud.client.developer.bitdubai.version_1.structure.vpn;

import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.AsymmectricCryptography;
import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.ECCKeyPair;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.contents.FermatMessageCommunication;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.contents.FermatPacketCommunicationFactory;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.contents.FermatPacketDecoder;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.contents.FermatPacketEncoder;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.client.CommunicationsCloudClientConnection;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.client.CommunicationsVPNConnection;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.components.PlatformComponentProfile;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.contents.FermatMessage;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.contents.FermatPacket;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.enums.FermatPacketType;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.ws.communications.cloud.client.developer.bitdubai.version_1.structure.vpn.WsCommunicationVPNClient</code>
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 12/09/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class WsCommunicationVPNClient extends WebSocketClient implements CommunicationsVPNConnection{

    /**
     * Represent the value of DEFAULT_CONNECTION_TIMEOUT
     */
    private static final int DEFAULT_CONNECTION_TIMEOUT = 10000;

    /**
     * Represent the vpnClientIdentity
     */
    private ECCKeyPair vpnClientIdentity;

    /**
     * Represent the participant of the vpn
     */
    private PlatformComponentProfile participant;

    /**
     * Represent the vpnServerIdentity
     */
    private String vpnServerIdentity;

    /**
     * Represent the pending incoming messages cache
     */
    private Set<FermatMessage> pendingIncomingMessages;

    /**
     * Represent the isActive
     */
    private boolean isActive;

    /**
     * Constructor with parameters
     * @param serverURI
     */
    public WsCommunicationVPNClient(ECCKeyPair vpnClientIdentity, URI serverURI, PlatformComponentProfile participant, String vpnServerIdentity, Map<String, String> headers) {
        super(serverURI , new Draft_17(), headers , WsCommunicationVPNClient.DEFAULT_CONNECTION_TIMEOUT);
        this.vpnClientIdentity = vpnClientIdentity;
        this.participant       = participant;
        this.vpnServerIdentity = vpnServerIdentity;
        this.pendingIncomingMessages = new ConcurrentSkipListSet<>();
    }

    /**
     * (non-javadoc)
     * @see WebSocketClient#onOpen(ServerHandshake)
     */
    @Override
    public void onOpen(ServerHandshake handsShakeData) {
        System.out.println(" --------------------------------------------------------------------- ");
        System.out.println(" WsCommunicationVPNClient - Starting method onOpen");
        System.out.println(" WsCommunicationVPNClient - VPN Server hand Shake Data = " + handsShakeData);
        isActive = Boolean.TRUE;
    }

    /**
     * (non-javadoc)
     * @see WebSocketClient#onClose(int, String, boolean)
     */
    @Override
    public void onMessage(String fermatPacketEncode) {

        System.out.println(" --------------------------------------------------------------------- ");
        System.out.println(" WsCommunicationVPNClient - Starting method onMessage(String)");
        System.out.println(" WsCommunicationVPNClient - encode fermatPacket " + fermatPacketEncode);

        /**
         * Decode the message with the client identity
         */
        FermatPacket fermatPacketReceive = FermatPacketDecoder.decode(fermatPacketEncode, vpnClientIdentity.getPrivateKey());
        System.out.println(" WsCommunicationsCloudClientChannel - decode fermatPacket " + FermatPacketDecoder.decode(fermatPacketEncode, vpnClientIdentity.getPrivateKey()));

        /*
         * Validate the signature
         */
        validateFermatPacketSignature(fermatPacketReceive);

        /*
         * Get the platformComponentProfile from the message content and decrypt
         */
        String messageContentJsonStringRepresentation = AsymmectricCryptography.decryptMessagePrivateKey(fermatPacketReceive.getMessageContent(), vpnClientIdentity.getPrivateKey());

        System.out.println("MessageTransmitPacketProcessor - messageContentJsonStringRepresentation = "+messageContentJsonStringRepresentation);

        /*
         * Get the message object
         */
        FermatMessage fermatMessage = new FermatMessageCommunication().fromJson(messageContentJsonStringRepresentation);

        System.out.println("MessageTransmitPacketProcessor - fermatMessage = "+fermatMessage);

        /*
         * Add to the list
         */
        pendingIncomingMessages.add(fermatMessage);

    }

    /**
     * (non-javadoc)
     * @see WebSocketClient#onClose(int, String, boolean)
     */
    @Override
    public void onClose(int code, String reason, boolean remote) {

        System.out.println(" --------------------------------------------------------------------- ");
        System.out.println(" WsCommunicationVPNClient - Starting method onClose");
        System.out.println(" WsCommunicationVPNClient -  code   = " + code + " reason = " + reason + " remote = " + remote);
        isActive = Boolean.FALSE;
    }

    /**
     * (non-javadoc)
     * @see WebSocketClient#onError(Exception)
     */
    @Override
    public void onError(Exception ex) {

        System.out.println(" --------------------------------------------------------------------- ");
        System.out.println(" WsCommunicationVPNClient - Starting method onError");
        ex.printStackTrace();
        getConnection().closeConnection(1000, ex.getLocalizedMessage());

    }

    /**
     * Validate the signature of the packet
     * @param fermatPacketReceive
     */
    private void validateFermatPacketSignature(FermatPacket fermatPacketReceive){

        System.out.println(" WsCommunicationsCloudClientChannel - validateFermatPacketSignature");

         /*
         * Validate the signature
         */
        boolean isValid = AsymmectricCryptography.verifyMessageSignature(fermatPacketReceive.getSignature(), fermatPacketReceive.getMessageContent(), vpnServerIdentity);

        System.out.println(" WsCommunicationsCloudClientChannel - isValid = " + isValid);

        /*
         * if not valid signature
         */
        if (!isValid){
            throw new RuntimeException("Fermat Packet received has not a valid signature, go to close this connection maybe is compromise");
        }

    }

    /**
     * (non-javadoc)
     * @see CommunicationsVPNConnection#sendMessage(FermatMessage)
     */
    @Override
    public void sendMessage(FermatMessage fermatMessage){

        System.out.println("WsCommunicationsCloudClientChannel - sendMessage");

        /*
         * Validate parameter
         */
        if (fermatMessage == null){
            throw new IllegalArgumentException("The fermatMessage is required, can not be null");
        }

         /*
         * Construct a fermat packet whit the message to transmit
         */
        FermatPacket fermatPacketRequest = FermatPacketCommunicationFactory.constructFermatPacketEncryptedAndSinged(vpnServerIdentity,                  //Destination
                                                                                                                    vpnClientIdentity.getPublicKey(),   //Sender
                                                                                                                    fermatMessage.toJson(),             //Message Content
                                                                                                                    FermatPacketType.MESSAGE_TRANSMIT,  //Packet type
                                                                                                                    vpnClientIdentity.getPrivateKey()); //Sender private key
        /*
         * Send the encode packet to the server
         */
        send(FermatPacketEncoder.encode(fermatPacketRequest));

    }

    /**
     * (non-Javadoc)
     *
     * @see CommunicationsVPNConnection#getUnreadMessagesCount()
     */
    public int getUnreadMessagesCount() {

        /*
         * Validate if not null
         */
        if (pendingIncomingMessages != null){

            /*
             * Return the size of the cache
             */
            return pendingIncomingMessages.size();

        }

        /*
         * Empty cache return 0
         */
        return 0;
    }

    /**
     * (non-Javadoc)
     *
     * @see CommunicationsVPNConnection#readNextMessage()
     */
    public FermatMessage readNextMessage() {

        /*
         * Validate no empty
         */
        if(!pendingIncomingMessages.isEmpty()) {

            /*
             * Return the next message
             */
            return pendingIncomingMessages.iterator().next();

        }else {

            //TODO: CREATE A APPROPRIATE EXCEPTION
            throw new RuntimeException();
        }

    }

    /**
     * (non-Javadoc)
     *
     * @see CommunicationsVPNConnection#removeMessageRead(FermatMessage)
     */
    @Override
    public void removeMessageRead(FermatMessage fermatMessage) {

        /*
         * Remove the message
         */
        pendingIncomingMessages.remove(fermatMessage);
    }

    /**
     * Get the VpnClientIdentityPublicKey
     * @return String
     */
    public String getVpnClientIdentityPublicKey(){
        return vpnClientIdentity.getPublicKey();
    }

    /**
     * Get the isActive
     * @return boolean
     */
    @Override
    public boolean isActive() {
        return isActive;
    }

    /**
     * Set the isActive
     * @param isActive
     */
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
