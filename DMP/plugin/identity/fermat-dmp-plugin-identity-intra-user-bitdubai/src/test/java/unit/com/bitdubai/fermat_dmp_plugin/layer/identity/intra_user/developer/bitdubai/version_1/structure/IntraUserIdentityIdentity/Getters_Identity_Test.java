package unit.com.bitdubai.fermat_dmp_plugin.layer.identity.intra_user.developer.bitdubai.version_1.structure.IntraUserIdentityIdentity;

import com.bitdubai.fermat_api.layer.dmp_identity.intra_user.exceptions.CantShowProfileImageException;
import com.bitdubai.fermat_dmp_plugin.layer.identity.intra_user.developer.bitdubai.version_1.structure.IntraUserIdentityIdentity;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;


/**
 * Created by angel on 13/8/15.
 */
public class Getters_Identity_Test{

    IntraUserIdentityIdentity identity_1;

    String alias_1;
    String publicKey_1;
    String privateKey_1;
    byte[] profileImage_1;

    @Before
    public void setUpVariable1(){

        alias_1 = "alias_1";
        publicKey_1 = "publicKey_1";
        privateKey_1 = "privateKey_1";
        profileImage_1 = new byte[10];

        identity_1 = new IntraUserIdentityIdentity(alias_1, publicKey_1, privateKey_1, profileImage_1);

    }

    @Test
    public void Get_Alias_AreEquals(){
        assertThat(identity_1.getAlias()).isEqualTo(alias_1);
    }

    @Test
    public void Get_PublicKey_AreEquals(){
        assertThat(identity_1.getPublicKey()).isEqualTo(publicKey_1);
    }

    @Test
    public void Get_ProfileImage_AreEquals() throws CantShowProfileImageException{
        assertThat(identity_1.getProfileImage()).isEqualTo(profileImage_1);
    }
}
