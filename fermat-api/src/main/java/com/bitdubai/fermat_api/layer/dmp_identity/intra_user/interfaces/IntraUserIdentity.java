package com.bitdubai.fermat_api.layer.dmp_identity.intra_user.interfaces;

import com.bitdubai.fermat_api.layer.dmp_identity.intra_user.exceptions.CantSetNewProfileImageException;
import com.bitdubai.fermat_api.layer.dmp_identity.intra_user.exceptions.CantShowProfileImageException;
import com.bitdubai.fermat_api.layer.dmp_identity.intra_user.exceptions.CantSingIntraUserMessageException;

/**
 * The interface <code>com.bitdubai.fermat_api.layer.dmp_identity.intra_user.interfaces.IntraUserIdentity</code>
 * defines the methods related to the extraction of the information of an intra user
 */
public interface IntraUserIdentity {

    /**
     * The method <code>getAlias</code> returns the alias of the represented intra user
     *
     * @return the alias of the represented intra user
     */
    String getAlias();

    /**
     * The method <code>getPublicKey</code> returns the public key of the represented intra user
     * @return the public key of the represented intra user
     */
    String getPublicKey();

    /**
     * The method <code>getProfileImage</code> gives us the profile image of the represented intra user
     * @return the profile image of the represented intra user
     * @throws CantShowProfileImageException
     */
    byte[] getProfileImage() throws CantShowProfileImageException;

    /**
     * This method let an intra user sign a message with his unique private key
     * @param message the message to sign
     * @return the signature
     * @throws CantSingIntraUserMessageException
     */
    String createMessageSignature(String message) throws CantSingIntraUserMessageException;
}
