package com.bitdubai.reference_niche_wallet.bitcoin_wallet.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitdubai.android_fermat_dmp_wallet_bitcoin.R;
import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.WalletSession;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Wallets;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_contacts.interfaces.WalletContactRecord;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_settings.interfaces.WalletSettingsManager;
import com.bitdubai.fermat_api.layer.dmp_niche_wallet_type.crypto_wallet.exceptions.CantGetAllWalletContactsException;
import com.bitdubai.fermat_api.layer.dmp_niche_wallet_type.crypto_wallet.exceptions.CantGetCryptoWalletException;
import com.bitdubai.fermat_api.layer.dmp_niche_wallet_type.crypto_wallet.interfaces.CryptoWallet;
import com.bitdubai.fermat_api.layer.dmp_niche_wallet_type.crypto_wallet.interfaces.CryptoWalletManager;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.UnexpectedWalletExceptionSeverity;
import com.bitdubai.reference_niche_wallet.bitcoin_wallet.common.contacts_list_adapter.WalletContact;
import com.bitdubai.reference_niche_wallet.bitcoin_wallet.common.popup.ReceiveFragmentDialog;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.bitdubai.reference_niche_wallet.bitcoin_wallet.common.utils.WalletUtils.showMessage;

/**
 * Contact Detail Fragment.
 *
 * @author Francisco Vásquez
 * @version 1.0
 */
public class ContactDetailFragment extends Fragment implements View.OnClickListener {

    /**
     * Root fragment view reference
     */
    private View mFragmentView;
    /**
     * Fragment UI controls
     */
    private TextView accountNameView;
    private TextView accountNumberView;
    private ImageView actionSendView;
    private ImageView actionReceiveView;
    private ImageView actionMoneyRequest;
    /**
     * Typeface Font
     */
    private Typeface typeface;
    /**
     * Platform
     */
    private UUID wallet_id = UUID.fromString("25428311-deb3-4064-93b2-69093e859871");
    private CryptoWallet cryptoWallet;
    private ErrorManager errorManager;
    private CryptoWalletManager cryptoWalletManager;
    private WalletSession walletSession;
    private WalletSettingsManager walletSettingsManager;

    /**
     * DATA
     */
    private String accountName;
    private WalletContact walletContact;


    public static ContactDetailFragment newInstance(WalletSession walletSession, String accountName) {
        if (accountName == null || accountName.isEmpty())
            return null;
        if (walletSession == null)
            return null;
        ContactDetailFragment f = new ContactDetailFragment();
        f.setWalletSession(walletSession);
        f.setAccountName(accountName);
        return f;
    }

    /**
     * Setting up the wallet contact
     *
     * @param accountName string contact
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    /**
     * Set Wallet Session
     *
     * @param walletSession session
     */
    public void setWalletSession(WalletSession walletSession) {
        this.walletSession = walletSession;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/CaviarDreams.ttf");
        cryptoWalletManager = walletSession.getCryptoWalletManager();
        errorManager = walletSession.getErrorManager();
        try {
            cryptoWallet = cryptoWalletManager.getCryptoWallet();
        } catch (CantGetCryptoWalletException e) {
            errorManager.reportUnexpectedWalletException(Wallets.CWP_WALLET_RUNTIME_WALLET_BITCOIN_WALLET_ALL_BITDUBAI, UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
            showMessage(getActivity(), "CantGetCryptoWalletException- " + e.getMessage());
        }

        try {
            List<WalletContactRecord> lst= cryptoWallet.listWalletContacts(wallet_id);

        } catch (CantGetAllWalletContactsException e) {
            e.printStackTrace();
        }
        /* Load Wallet Contact */
        walletContact = CollectionUtils.find(getWalletContactList(), new Predicate<WalletContact>() {
            @Override
            public boolean evaluate(WalletContact walletContact) {
                try {
                    return walletContact.name.equalsIgnoreCase(accountName);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return false;
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.wallets_bitcoin_fragment_contact_detail, container, false);
        setUp();
        setUpContact();
        return mFragmentView;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.action_send
                && walletContact != null) {
            SendFragment fragment = SendFragment.newInstance(walletSession, walletContact);
            fragment.fromContacts = true;
            getActivity().getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .add(R.id.fragment_container2, fragment)
                    .attach(fragment)
                    .show(fragment)
                    .commit();
        }else if(view.getId() == R.id.action_receive && walletContact != null){
//            ReceiveFragment fragment = ReceiveFragment.newInstance(0,walletContact,walletSession);
//            fragment.fromContacts = true;
//            getActivity().getSupportFragmentManager()
//                    .beginTransaction()
//                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
//                    .add(R.id.fragment_container2, fragment)
//                    .attach(fragment)
//                    .show(fragment)
//                    .commit();
            ReceiveFragmentDialog receiveFragmentDialog = new ReceiveFragmentDialog(getActivity(),cryptoWallet,errorManager,walletContact);
            receiveFragmentDialog.show();

            //CustomDialogClass cdd=new CustomDialogClass(getActivity(),item,item.pluginKey);
            //cdd.show();
        }else if(view.getId() == R.id.action_money_request && walletContact != null){
            MoneyRequestFragment fragment = MoneyRequestFragment.newInstance(0,walletContact,walletSettingsManager,walletSession);
            fragment.fromContacts = true;
            getActivity().getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .add(R.id.fragment_container2, fragment)
                    .attach(fragment)
                    .show(fragment)
                    .commit();
        }

    }

    /**
     * Setup UI references
     */
    private void setUp() {
        if (mFragmentView != null) {
            actionSendView = (ImageView) mFragmentView.findViewById(R.id.action_send);
            actionReceiveView = (ImageView) mFragmentView.findViewById(R.id.action_receive);
            actionMoneyRequest = (ImageView) mFragmentView.findViewById(R.id.action_money_request);
            accountNameView = (TextView) mFragmentView.findViewById(R.id.account_name);
            accountNumberView = (TextView) mFragmentView.findViewById(R.id.account_number);
            if (typeface != null) {
                accountNameView.setTypeface(typeface);
                accountNumberView.setTypeface(typeface);
                ((TextView) mFragmentView.findViewById(R.id.details_title)).setTypeface(typeface);
                ((TextView) mFragmentView.findViewById(R.id.view_more_title)).setTypeface(typeface);
            }

        }
    }

    /**
     * Setting up wallet contact value
     */
    private void setUpContact() {
        if (walletContact != null) {
            if (accountNameView != null)
                accountNameView.setText(walletContact.name);
            if (accountNumberView != null)
                accountNumberView.setText(walletContact.address);
            if (actionSendView != null) {
                actionSendView.setOnClickListener(this);
            }
            if (actionReceiveView != null) {
                actionReceiveView.setOnClickListener(this);
            }
            if (actionMoneyRequest != null) {
                actionMoneyRequest.setOnClickListener(this);
            }
        }
    }

    /**
     * Obtain the wallet contacts from the cryptoWallet
     *
     * @return
     */
    private List<WalletContact> getWalletContactList() {
        List<WalletContact> contacts = new ArrayList<>();
        try {
            List<WalletContactRecord> walletContactRecords = cryptoWallet.listWalletContacts(wallet_id);
            for (WalletContactRecord wcr : walletContactRecords) {
                contacts.add(new WalletContact(wcr.getActorName(), wcr.getReceivedCryptoAddress().getAddress(), wcr.getContactId()));
            }
        } catch (CantGetAllWalletContactsException e) {
            errorManager.reportUnexpectedWalletException(Wallets.CWP_WALLET_RUNTIME_WALLET_BITCOIN_WALLET_ALL_BITDUBAI, UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
            showMessage(getActivity(), "CantGetAllWalletContactsException- " + e.getMessage());
        }
        return contacts;
    }
}
