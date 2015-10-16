package com.bitdubai.reference_niche_wallet.bitcoin_wallet.fragments.wallet_final_version;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bitdubai.android_fermat_ccp_wallet_bitcoin.R;
import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.bitdubai.fermat_android_api.ui.enums.FermatRefreshTypes;
import com.bitdubai.fermat_android_api.ui.fragments.FermatWalletListFragment;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatListItemListeners;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Wallets;
import com.bitdubai.fermat_api.layer.dmp_basic_wallet.common.enums.BalanceType;
import com.bitdubai.fermat_api.layer.dmp_basic_wallet.common.enums.TransactionType;
import com.bitdubai.fermat_api.layer.dmp_wallet_module.crypto_wallet.exceptions.CantFindWalletContactException;
import com.bitdubai.fermat_api.layer.dmp_wallet_module.crypto_wallet.exceptions.CantGetAllWalletContactsException;
import com.bitdubai.fermat_api.layer.dmp_wallet_module.crypto_wallet.exceptions.CantGetBalanceException;
import com.bitdubai.fermat_api.layer.dmp_wallet_module.crypto_wallet.exceptions.CantListTransactionsException;
import com.bitdubai.fermat_api.layer.dmp_wallet_module.crypto_wallet.exceptions.WalletContactNotFoundException;
import com.bitdubai.fermat_api.layer.dmp_wallet_module.crypto_wallet.interfaces.CryptoWallet;
import com.bitdubai.fermat_api.layer.dmp_wallet_module.crypto_wallet.interfaces.CryptoWalletTransaction;
import com.bitdubai.fermat_api.layer.dmp_wallet_module.crypto_wallet.interfaces.CryptoWalletWalletContact;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.UnexpectedUIExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.UnexpectedWalletExceptionSeverity;

import com.bitdubai.reference_niche_wallet.bitcoin_wallet.common.adapters.TransactionNewAdapter;
import com.bitdubai.reference_niche_wallet.bitcoin_wallet.common.contacts_list_adapter.WalletContact;
import com.bitdubai.reference_niche_wallet.bitcoin_wallet.common.contacts_list_adapter.WalletContactListAdapter;
import com.bitdubai.reference_niche_wallet.bitcoin_wallet.common.custom_anim.Fx;
import com.bitdubai.reference_niche_wallet.bitcoin_wallet.common.enums.ShowMoneyType;
import com.bitdubai.reference_niche_wallet.bitcoin_wallet.common.popup.CreateContactFragmentDialog;
import com.bitdubai.reference_niche_wallet.bitcoin_wallet.common.popup.ReceiveFragmentDialog;
import com.bitdubai.reference_niche_wallet.bitcoin_wallet.common.utils.WalletUtils;
import com.bitdubai.reference_niche_wallet.bitcoin_wallet.session.ReferenceWalletSession;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;
import static com.bitdubai.reference_niche_wallet.bitcoin_wallet.common.utils.WalletUtils.formatBalanceString;
import static com.bitdubai.reference_niche_wallet.bitcoin_wallet.common.utils.WalletUtils.showMessage;

/**
 * Created by Matias Furszyfer on 2015.09.28..
 */
public class ReceiveTransactionsFragment extends FermatWalletListFragment<CryptoWalletTransaction> implements FermatListItemListeners<CryptoWalletTransaction>, DialogInterface.OnDismissListener, Thread.UncaughtExceptionHandler {



    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_LOAD_IMAGE = 2;
    public static final int CONTEXT_MENU_CAMERA = 1;
    public static final int CONTEXT_MENU_GALLERY = 2;
    public static final int CONTEXT_MENU_DELETE = 3;
    private static final int CONTEXT_MENU_NO_PHOTO = 4;

    // TODO: preguntar de donde saco el user id
    String user_id = UUID.fromString("afd0647a-87de-4c56-9bc9-be736e0c5059").toString();

    /**
     * MANAGERS
     */
    private CryptoWallet cryptoWallet;

    /**
     * Session
     */
    ReferenceWalletSession referenceWalletSession;

    /**
     * DATA
     */
    private List<CryptoWalletTransaction> lstCryptoWalletTransactionsAvailable;
    private List<CryptoWalletTransaction> lstCryptoWalletTransactionsBook;

    private CryptoWalletTransaction selectedItem;

    /**
     * Executor Service
     */
    private ExecutorService executor;

    private int MAX_TRANSACTIONS = 20;

    private int available_offset = 0;
    private int book_offset = 0;

    View rootView;
    private LinearLayout linear_layout_receive_form;
    private AutoCompleteTextView autocompleteContacts;
    private WalletContactListAdapter contactsAdapter;
    private TextView txt_type_balance;
    private TextView txt_balance_amount;
    private WalletContact walletContact;
    private String user_address_wallet="";

    private long bookBalance;
    private long balanceAvailable;


    private Bitmap contactImageBitmap;
    private Button btn_give_address;
    private LinearLayout empty;


    private boolean contactSelectedFlag=false;

    private AtomicBoolean start;

    /**
     * Create a new instance of this fragment
     *
     * @return InstalledFragment instance object
     */
    public static ReceiveTransactionsFragment newInstance() {
        ReceiveTransactionsFragment requestPaymentFragment = new ReceiveTransactionsFragment();
        return new ReceiveTransactionsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        referenceWalletSession = (ReferenceWalletSession)walletSession;

        lstCryptoWalletTransactionsBook = new ArrayList<CryptoWalletTransaction>();
        lstCryptoWalletTransactionsAvailable = new ArrayList<CryptoWalletTransaction>();

        start= new AtomicBoolean(false);

        try {
            cryptoWallet = referenceWalletSession.getCryptoWalletManager().getCryptoWallet();

            balanceAvailable = loadBalance(BalanceType.AVAILABLE);
            bookBalance = loadBalance(BalanceType.BOOK);

            updateTransactions();



            //lstCryptoWalletTransactions.addAll(getMoreDataAsync(FermatRefreshTypes.NEW, 0)); // get init data
        } catch (Exception ex) {
            ex.printStackTrace();
            //CommonLogger.exception(TAG, ex.getMessage(), ex);
        }
    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        try {

            rootView = super.onCreateView(inflater, container, savedInstanceState);
//
            start.set(true);

            setUp(inflater);
            //container_header_balance.invalidate();

            //((PaintActivtyFeactures)getActivity()).invalidate();

            //rootView    = inflater.inflate(R.layout.receive_button, container, false);


            linear_layout_receive_form = (LinearLayout) rootView.findViewById(R.id.receive_form);

            ((com.melnykov.fab.FloatingActionButton) rootView.findViewById(R.id.fab_action)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean isShow = linear_layout_receive_form.isShown();
                    //linear_layout_send_form.setVisibility(isShow?View.GONE:View.VISIBLE);
                    if (isShow) {
                        Fx.slide_up(getActivity(), linear_layout_receive_form);
                        linear_layout_receive_form.setVisibility(View.GONE);
                    } else {
                        linear_layout_receive_form.setVisibility(View.VISIBLE);
                        Fx.slide_down(getActivity(), linear_layout_receive_form);
                    }

                }
            });

            autocompleteContacts = (AutoCompleteTextView) rootView.findViewById(R.id.contact_name);

            contactsAdapter = new WalletContactListAdapter(getActivity(), R.layout.wallets_bitcoin_fragment_contacts_list_item, getWalletContactList());

            autocompleteContacts.setAdapter(contactsAdapter);
            //autocompleteContacts.setTypeface(tf);
            autocompleteContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    walletContact = (WalletContact) arg0.getItemAtPosition(position);
                    contactSelectedFlag = true;
                    //add.setText(walletContact.address);
                }
            });

            autocompleteContacts.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (contactSelectedFlag == true) {
                        walletContact = null;
                        contactSelectedFlag = false;
                    }
                }
            });


            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView_new_contact);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    walletContact = new WalletContact();
                    walletContact.setName(autocompleteContacts.getText().toString());
                    getActivity().openContextMenu(autocompleteContacts);
                }
            });



            btn_give_address = (Button) rootView.findViewById(R.id.btn_address);
            btn_give_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (walletContact != null) {
                        try {
                            CryptoWalletWalletContact walletContactDatabase = cryptoWallet.findWalletContactById(walletContact.contactId);

                            if (walletContact.actorPublicKey.equals(walletContactDatabase.getActorPublicKey())) {
                                ReceiveFragmentDialog receiveFragmentDialog = new ReceiveFragmentDialog(getActivity(), cryptoWallet, referenceWalletSession.getErrorManager(), walletContact, user_id, referenceWalletSession.getWalletSessionType().getWalletPublicKey());
                                receiveFragmentDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {
                                        boolean isShow = linear_layout_receive_form.isShown();
                                        //linear_layout_send_form.setVisibility(isShow?View.GONE:View.VISIBLE);
                                        if (isShow) {
                                            Fx.slide_up(getActivity(), linear_layout_receive_form);
                                            linear_layout_receive_form.setVisibility(View.GONE);
                                        }
                                    }
                                });
                                receiveFragmentDialog.show();
                            } else {
                                Toast.makeText(getActivity(), "no se que hacer", Toast.LENGTH_SHORT).show();
                                //                            registerForContextMenu(btn_give_address);
                                //                            getActivity().openContextMenu(btn_give_address);
                            }
                        } catch (CantFindWalletContactException e) {
                            e.printStackTrace();
                        } catch (WalletContactNotFoundException e) {
                            e.printStackTrace();
                        }
                        //                        walletContact.contactId
                        //                        walletContact.
                    } else {
                                                                    //                        walletContact = new WalletContact();
                                                                //                        walletContact.setName(autocompleteContacts.getText().toString());
//                        registerForContextMenu(btn_give_address);
//                        getActivity().openContextMenu(btn_give_address);
                        Toast.makeText(getActivity(), "Sorry, action not posible\nadd contact first", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            registerForContextMenu(btn_give_address);

            empty=(LinearLayout) rootView.findViewById(R.id.empty);

            if (lstCryptoWalletTransactionsBook.isEmpty() && BalanceType.getByCode(referenceWalletSession.getBalanceTypeSelected()).equals(BalanceType.BOOK)) {
                empty.setVisibility(View.VISIBLE);
            }else if (lstCryptoWalletTransactionsAvailable.isEmpty() && BalanceType.getByCode(referenceWalletSession.getBalanceTypeSelected()).equals(BalanceType.AVAILABLE)){
                empty.setVisibility(View.VISIBLE);
            }


            return rootView;

        }catch (Exception e){
            makeText(getActivity(), "Oooops! recovering from system error",
                    LENGTH_LONG).show();
            referenceWalletSession.getErrorManager().reportUnexpectedUIException(UISource.VIEW, UnexpectedUIExceptionSeverity.CRASH, e);
        }
        return null;
    }
    private void setUp(LayoutInflater inflater){
        setUpHeader(inflater);
    }

    private void setUpHeader(LayoutInflater inflater){
        RelativeLayout container_header_balance = getActivityHeader();


        inflater =
                (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        container_header_balance.setVisibility(View.VISIBLE);

        View balance_header = inflater.inflate(R.layout.balance_header, container_header_balance, true);

        txt_type_balance = (TextView) balance_header.findViewById(R.id.txt_type_balance);
        txt_type_balance.setTextColor(Color.parseColor("#a8a5ff"));


        LinearLayout linear_type_container = (LinearLayout) balance_header.findViewById(R.id.linear_type_container);
        linear_type_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(),"balance cambiado",Toast.LENGTH_SHORT).show();
                //txt_type_balance.setText(referenceWalletSession.getBalanceTypeSelected());
                changeBalanceType(txt_type_balance, txt_balance_amount);
            }
        });

        LinearLayout linear_amount_container = (LinearLayout) balance_header.findViewById(R.id.linear_amount_container);
        linear_amount_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(),"balance cambiado",Toast.LENGTH_SHORT).show();
                //txt_type_balance.setText(referenceWalletSession.getBalanceTypeSelected());
                changeAmountType(txt_balance_amount);
            }
        });

        txt_balance_amount = (TextView) balance_header.findViewById(R.id.txt_balance_amount);
        txt_balance_amount.setTextColor(Color.WHITE);


        try {
            long balance = cryptoWallet.getBalance(BalanceType.getByCode(referenceWalletSession.getBalanceTypeSelected()), referenceWalletSession.getWalletSessionType().getWalletPublicKey());
            txt_balance_amount.setText(formatBalanceString(balance, referenceWalletSession.getTypeAmount()));
        } catch (CantGetBalanceException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected boolean hasMenu() {
        return false;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.transaciotn_main_fragment_receive;
    }

    @Override
    protected int getSwipeRefreshLayoutId() {
        return R.id.swipe_refresh;
    }

    @Override
    protected int getRecyclerLayoutId() {
        return R.id.transactions_recycler_view;
    }

    @Override
    protected boolean recyclerHasFixedSize() {
        return true;
    }


    @Override
    @SuppressWarnings("unchecked")
    public FermatAdapter getAdapter() {
        if (adapter == null) {
            //WalletStoreItemPopupMenuListener listener = getWalletStoreItemPopupMenuListener();
            adapter = new TransactionNewAdapter(getActivity(), BalanceType.getByCode(referenceWalletSession.getBalanceTypeSelected()).equals(BalanceType.AVAILABLE) ? lstCryptoWalletTransactionsAvailable : lstCryptoWalletTransactionsBook,cryptoWallet,referenceWalletSession);
            adapter.setFermatListEventListener(this); // setting up event listeners
        }
        return adapter;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        if (layoutManager == null) {
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        }
        return layoutManager;
    }

    @Override
    public List<CryptoWalletTransaction> getMoreDataAsync(FermatRefreshTypes refreshType, int pos) {
//        List<CryptoWalletTransaction> lstTransactions  = new ArrayList<CryptoWalletTransaction>();
//
//
//        try {
//            lstTransactions = cryptoWallet.listLastActorTransactionsByTransactionType(BalanceType.getByCode(referenceWalletSession.getBalanceTypeSelected()), TransactionTypes.CREDIT,referenceWalletSession.getWalletSessionType().getWalletPublicKey(),MAX_TRANSACTIONS,offset);
//            offset+=lstTransactions.size();
//        }
//        catch (Exception e) {
//            referenceWalletSession.getErrorManager().reportUnexpectedWalletException(Wallets.CWP_WALLET_RUNTIME_WALLET_BITCOIN_WALLET_ALL_BITDUBAI,
//                    UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
//            e.printStackTrace();
//            // data = RequestPaymentListItem.getTestData(getResources());
//        }

        updateTransactions();


        return null;
    }

    @Override
    public void onItemClickListener(CryptoWalletTransaction item, int position) {
        selectedItem = item;
        //showDetailsActivityFragment(selectedItem);
    }

    /**
     * On Long item Click Listener
     *
     * @param data
     * @param position
     */
    @Override
    public void onLongItemClickListener(CryptoWalletTransaction data, int position) {

    }

    private void updateTransactions(){

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                try {

                    lstCryptoWalletTransactionsAvailable.addAll(cryptoWallet.listLastActorTransactionsByTransactionType(BalanceType.AVAILABLE, TransactionType.CREDIT, referenceWalletSession.getWalletSessionType().getWalletPublicKey(), MAX_TRANSACTIONS, available_offset));

                    available_offset = lstCryptoWalletTransactionsAvailable.size();

                    lstCryptoWalletTransactionsBook.addAll(cryptoWallet.listLastActorTransactionsByTransactionType(BalanceType.BOOK, TransactionType.CREDIT, referenceWalletSession.getWalletSessionType().getWalletPublicKey(), MAX_TRANSACTIONS, book_offset));

                    book_offset = lstCryptoWalletTransactionsBook.size();



                } catch (CantListTransactionsException e) {
                    e.printStackTrace();
                }

            }
        };

        Thread thread = new Thread(runnable);

        thread.start();
    }

    @Override
    public void onPostExecute(Object... result) {
        isRefreshing = false;
        if (isAttached) {
            swipeRefreshLayout.setRefreshing(false);
            //if (result != null && result.length > 0) {
                //lstCryptoWalletTransactions.addAll((ArrayList) result[0]);
                if (adapter != null)
                    adapter.changeDataSet(BalanceType.getByCode(referenceWalletSession.getBalanceTypeSelected()).equals(BalanceType.AVAILABLE) ? lstCryptoWalletTransactionsAvailable : lstCryptoWalletTransactionsBook);


            if(start.get()) {

                if (!lstCryptoWalletTransactionsBook.isEmpty() && BalanceType.getByCode(referenceWalletSession.getBalanceTypeSelected()).equals(BalanceType.BOOK)) {
                    empty.setVisibility(View.GONE);
                } else if (!lstCryptoWalletTransactionsAvailable.isEmpty() && BalanceType.getByCode(referenceWalletSession.getBalanceTypeSelected()).equals(BalanceType.AVAILABLE)) {
                    empty.setVisibility(View.GONE);
                } else {
                    empty.setVisibility(View.VISIBLE);
                }
            }
            //}
        }
    }

    @Override
    public void onErrorOccurred(Exception ex) {
        isRefreshing = false;
        if (isAttached) {
            swipeRefreshLayout.setRefreshing(false);
            referenceWalletSession.getErrorManager().reportUnexpectedPluginException(Plugins.BITDUBAI_CRYPTO_WALLET_WALLET_MODULE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,ex);
            //CommonLogger.exception(TAG, ex.getMessage(), ex);
        }
    }



    public void setReferenceWalletSession(ReferenceWalletSession referenceWalletSession) {
        this.referenceWalletSession = referenceWalletSession;
    }

    /**
     * Obtain the wallet contacts from the cryptoWallet
     *
     * @return
     */
    private List<WalletContact> getWalletContactList() {
        List<WalletContact> contacts = new ArrayList<>();

//        new FermatWorker(getActivity(), new FermatWorkerCallBack() {
//            @SuppressWarnings("unchecked")
//            @Override
//            public void onPostExecute(Object... result) {
//                if (isAttached) {
//                    if (adapter != null) {
//                        intraUserItemList = (ArrayList<IntraUserConnectionListItem>) result[0];
//                        adapter.changeDataSet(intraUserItemList);
//                        isStartList = true;
//
//                    }
//                    showEmpty();
//                }
//            }
//
//            @Override
//            public void onErrorOccurred(Exception ex) {
//                if (isAttached) {
//                    dialog.dismiss();
//                    dialog = null;
//                    Toast.makeText(getActivity(), "Some Error Occurred: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
//                    showEmpty();
//                }
//            }
//        }) {
//
//            @Override
//            protected Object doInBackground() throws Exception {
//
//                return getMoreDataAsync(FermatRefreshTypes.NEW, 0); // get init data
//
//            }
//        }.execute();
        try {
            List<CryptoWalletWalletContact> walletContactRecords = cryptoWallet.listWalletContacts("reference_wallet"/*referenceWalletSession.getWalletSessionType().getWalletPublicKey()*/);
            for (CryptoWalletWalletContact wcr : walletContactRecords) {
                contacts.add(new WalletContact(wcr.getContactId(), wcr.getActorPublicKey(), wcr.getActorName(), wcr.getReceivedCryptoAddress().get(0).getAddress()));
            }
        } catch (CantGetAllWalletContactsException e) {
            referenceWalletSession.getErrorManager().reportUnexpectedWalletException(Wallets.CWP_WALLET_RUNTIME_WALLET_BITCOIN_WALLET_ALL_BITDUBAI, UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
            showMessage(getActivity(), "CantGetAllWalletContactsException- " + e.getMessage());
        }
        return contacts;
    }


    private void changeAmountType(TextView txt_balance_amount){
        referenceWalletSession.setTypeAmount((referenceWalletSession.getTypeAmount()==ShowMoneyType.BITCOIN.getCode()) ? ShowMoneyType.BITS : ShowMoneyType.BITCOIN);
        updateBalances();
    }



    /**
     * Method to change the balance type
     */
    private void changeBalanceType(TextView txt_type_balance,TextView txt_balance_amount) {

        try {
            if (referenceWalletSession.getBalanceTypeSelected().equals(BalanceType.AVAILABLE.getCode())) {
                balanceAvailable = loadBalance(BalanceType.AVAILABLE);
                txt_balance_amount.setText(formatBalanceString(bookBalance, referenceWalletSession.getTypeAmount()));
                txt_type_balance.setText(R.string.book_balance);
                referenceWalletSession.setBalanceTypeSelected(BalanceType.BOOK);
            } else if (referenceWalletSession.getBalanceTypeSelected().equals(BalanceType.BOOK.getCode())) {
                bookBalance = loadBalance(BalanceType.BOOK);
                txt_balance_amount.setText(formatBalanceString(balanceAvailable, referenceWalletSession.getTypeAmount()));
                txt_type_balance.setText(R.string.available_balance);
                referenceWalletSession.setBalanceTypeSelected(BalanceType.AVAILABLE);
            }
        } catch (Exception e) {
            referenceWalletSession.getErrorManager().reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.CRASH, FermatException.wrapException(e));
            Toast.makeText(getActivity().getApplicationContext(), "Oooops! recovering from system error", Toast.LENGTH_SHORT).show();
        }

    }

    private long loadBalance(BalanceType balanceType){
        long balance = 0;
        try {
            balance = cryptoWallet.getBalance(balanceType,referenceWalletSession.getWalletSessionType().getWalletPublicKey());
        } catch (CantGetBalanceException e) {
            e.printStackTrace();
        }
        return balance;
    }


    private void updateBalances(){
        bookBalance = loadBalance(BalanceType.BOOK);
        balanceAvailable = loadBalance(BalanceType.AVAILABLE);
        txt_balance_amount.setText(
                WalletUtils.formatBalanceString(
                        (referenceWalletSession.getBalanceTypeSelected() == BalanceType.AVAILABLE.getCode())
                                ? balanceAvailable : bookBalance,
                        referenceWalletSession.getTypeAmount())
        );
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            contactImageBitmap = null;
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    Bundle extras = data.getExtras();
                    contactImageBitmap = (Bitmap) extras.get("data");
                    break;
                case REQUEST_LOAD_IMAGE:
                    Uri selectedImage = data.getData();
                    try {
                        contactImageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);

                        //imageBitmap = Bitmap.createScaledBitmap(imageBitmap,take_picture_btn.getWidth(),take_picture_btn.getHeight(),true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity().getApplicationContext(), "Error cargando la imagen", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            //take_picture_btn.setBackground(new RoundedDrawable(imageBitmap, take_picture_btn));
            //take_picture_btn.setImageDrawable(null);
            //contactPicture = imageBitmap;
            this.lauchCreateContactDialog(true);

        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select contact picture");
        menu.setHeaderIcon(getActivity().getResources().getDrawable(R.drawable.ic_camera_green));
        menu.add(Menu.NONE, CONTEXT_MENU_CAMERA, Menu.NONE, "Camera");
        menu.add(Menu.NONE, CONTEXT_MENU_GALLERY, Menu.NONE, "Gallery");
        menu.add(Menu.NONE, CONTEXT_MENU_NO_PHOTO, Menu.NONE, "No photo");
//        if(contactImageBitmap!=null)
//            menu.add(Menu.NONE, CONTEXT_MENU_DELETE, Menu.NONE, "Delete");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case CONTEXT_MENU_CAMERA:
                dispatchTakePictureIntent();
                break;
            case CONTEXT_MENU_GALLERY:
                loadImageFromGallery();
                break;
            case CONTEXT_MENU_NO_PHOTO:

//                takePictureButton.setBackground(getActivity().getResources().
//                        getDrawable(R.drawable.rounded_button_green_selector));
//                takePictureButton.setImageResource(R.drawable.ic_camera_green);
//                contactPicture = null;
                this.lauchCreateContactDialog(false);
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void loadImageFromGallery() {
        Intent intentLoad = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intentLoad, REQUEST_LOAD_IMAGE);
    }

    private void lauchCreateContactDialog(boolean withImage){
        CreateContactFragmentDialog dialog = new CreateContactFragmentDialog(
                getActivity(),
                referenceWalletSession,
                walletContact,
                user_id,
                ((withImage) ? contactImageBitmap : null));
        dialog.setOnDismissListener(this);
        dialog.show();
    }


    @Override
    public void onDismiss(DialogInterface dialogInterface) {


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                contactsAdapter.clear();
                contactsAdapter.addAll(getWalletContactList());

                contactsAdapter.notifyDataSetChanged();
            }
        };
        Thread thread = new Thread(runnable);

        thread.setUncaughtExceptionHandler(this);

        thread.start();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        referenceWalletSession.getErrorManager().reportUnexpectedPluginException(Plugins.BITDUBAI_BANK_NOTES_WALLET_WALLET_MODULE,UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,new Exception());
        Toast.makeText(getActivity(),"oooopps",Toast.LENGTH_SHORT).show();
    }
}
