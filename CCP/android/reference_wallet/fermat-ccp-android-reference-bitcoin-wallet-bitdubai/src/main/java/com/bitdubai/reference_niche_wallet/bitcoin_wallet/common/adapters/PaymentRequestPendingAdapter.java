package com.bitdubai.reference_niche_wallet.bitcoin_wallet.common.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bitdubai.android_fermat_ccp_wallet_bitcoin.R;
import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapterNew;
import com.bitdubai.fermat_android_api.ui.inflater.ViewInflater;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_store.enums.InstallationStatus;
import com.bitdubai.fermat_api.layer.dmp_wallet_module.crypto_wallet.interfaces.PaymentRequest;
import com.bitdubai.fermat_api.layer.pip_engine.interfaces.ResourceProviderManager;
import com.bitdubai.reference_niche_wallet.bitcoin_wallet.common.holders.PaymentRequestItemViewHolder;
import com.bitdubai.reference_niche_wallet.bitcoin_wallet.session.ReferenceWalletSession;

import java.util.ArrayList;
import java.util.List;

import static com.bitdubai.reference_niche_wallet.bitcoin_wallet.common.utils.WalletUtils.formatBalanceString;

/**
 * Created on 22/08/15.
 * Adapter para el RecliclerView del MainFragment que muestra el catalogo de Wallets disponibles en el store
 *
 * @author Matias Furszyfer
 */
public class PaymentRequestPendingAdapter extends FermatAdapterNew<PaymentRequest, PaymentRequestItemViewHolder> {

    //private WalletStoreItemPopupMenuListener menuClickListener;

    ViewInflater viewInflater;
    ReferenceWalletSession referenceWalletSession;

    public PaymentRequestPendingAdapter(Context context, List<PaymentRequest> dataSet,ViewInflater viewInflater,ReferenceWalletSession referenceWalletSession) {
        super(context, dataSet,viewInflater,referenceWalletSession.getWalletResourcesProviderManager());
        this.referenceWalletSession = referenceWalletSession;
        this.viewInflater = viewInflater;
    }

    @Override
    protected PaymentRequestItemViewHolder createHolder(View itemView, int type) {
        return new PaymentRequestItemViewHolder(itemView,viewInflater);
    }

    @Override
    protected String getCardViewResourceName() {
        return "pending_request_row2";//R.layout.wallet_store_catalog_item;
    }

    @Override
    protected void bindHolder(final PaymentRequestItemViewHolder holder, final PaymentRequest data, final int position) {

        holder.getContactIcon().setImageResource(R.drawable.mati_profile);

        holder.getTxt_amount().setText(formatBalanceString(data.getAmount(), referenceWalletSession.getTypeAmount()));

        holder.getTxt_contactName().setText("Juan");//data.getContact().getActorName());

        holder.getTxt_notes().setText(data.getReason());

        holder.getTxt_time().setText(data.getDate());



//        holder.getWalletName().setText(data.getWalletName());
//        holder.getWalletIcon().setImageBitmap(data.getWalletIcon());
//        holder.getWalletPublisherName().setText("Publisher Name");
//
//        InstallationStatus installStatus = data.getInstallationStatus();
//        int resId = UtilsFuncs.INSTANCE.getInstallationStatusStringResource(installStatus);
//        holder.getInstallStatus().setText(resId);
//
//        final ImageView menu = holder.getMenu();
//        if (menuClickListener != null) {
//            menu.setVisibility(View.VISIBLE);
//            menu.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    menuClickListener.onMenuItemClickListener(menu, data, position);
//                }
//            });
//        } else
//            menu.setVisibility(View.INVISIBLE);
    }

    public void setOnClickListerAcceptButton(View.OnClickListener onClickListener){

    }

    public void setOnClickListerRefuseButton(View.OnClickListener onClickListener){

    }

}
