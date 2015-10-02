package com.bitdubai.reference_niche_wallet.bitcoin_wallet.common.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

import com.bitdubai.android_fermat_ccp_wallet_bitcoin.R;
import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.bitdubai.fermat_api.layer.dmp_basic_wallet.common.enums.BalanceType;
import com.bitdubai.fermat_api.layer.dmp_wallet_module.crypto_wallet.exceptions.CantGetActorTransactionHistoryException;
import com.bitdubai.fermat_api.layer.dmp_wallet_module.crypto_wallet.interfaces.ActorTransactionSummary;
import com.bitdubai.fermat_api.layer.dmp_wallet_module.crypto_wallet.interfaces.CryptoWallet;
import com.bitdubai.fermat_api.layer.dmp_wallet_module.crypto_wallet.interfaces.CryptoWalletTransaction;
import com.bitdubai.fermat_api.layer.dmp_wallet_module.crypto_wallet.interfaces.PaymentRequest;
import com.bitdubai.reference_niche_wallet.bitcoin_wallet.common.holders.PaymentHistoryItemViewHolder;
import com.bitdubai.reference_niche_wallet.bitcoin_wallet.common.holders.TransactionItemViewHolder;
import com.bitdubai.reference_niche_wallet.bitcoin_wallet.session.ReferenceWalletSession;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static com.bitdubai.reference_niche_wallet.bitcoin_wallet.common.utils.WalletUtils.formatBalanceString;

/**
 * Created by Matias Furszyfer on 2015.09.30..
 */
public class PaymentRequestHistoryAdapter  extends FermatAdapter<PaymentRequest, PaymentHistoryItemViewHolder> {

    CryptoWallet cryptoWallet;
    ReferenceWalletSession referenceWalletSession;

    protected PaymentRequestHistoryAdapter(Context context) {
        super(context);
    }

    public PaymentRequestHistoryAdapter(Context context, List<PaymentRequest> dataSet, CryptoWallet cryptoWallet, ReferenceWalletSession referenceWalletSession) {
        super(context, dataSet);
        this.cryptoWallet = cryptoWallet;
        this.referenceWalletSession =referenceWalletSession;
    }

    public void setOnClickListerAcceptButton(View.OnClickListener onClickListener){

    }

    public void setOnClickListerRefuseButton(View.OnClickListener onClickListener){

    }

    /**
     * Create a new holder instance
     *
     * @param itemView View object
     * @param type     int type
     * @return ViewHolder
     */
    @Override
    protected PaymentHistoryItemViewHolder createHolder(View itemView, int type) {
        return new PaymentHistoryItemViewHolder(itemView);
    }

    /**
     * Get custom layout to use it.
     *
     * @return int Layout Resource id: Example: R.layout.row_item
     */
    @Override
    protected int getCardViewResource() {
        return R.layout.history_request_row;
    }

    /**
     * Bind ViewHolder
     *
     * @param holder   ViewHolder object
     * @param data     Object data to render
     * @param position position to render
     */
    @Override
    protected void bindHolder(PaymentHistoryItemViewHolder holder, PaymentRequest data, int position) {

        holder.getContactIcon().setImageResource(R.drawable.mati_profile);



        holder.getTxt_amount().setText(formatBalanceString(data.getAmount(), referenceWalletSession.getTypeAmount()));
        holder.getTxt_amount().setTextColor(Color.BLACK);

        holder.getTxt_contactName().setText("Mati");//data.getContact().getActorName());
        holder.getTxt_contactName().setTextColor(Color.BLACK);

        holder.getTxt_notes().setText(data.getReason());
        holder.getTxt_notes().setTextColor(Color.BLACK);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        holder.getTxt_time().setText(data.getDate());
        holder.getTxt_time().setTextColor(Color.BLACK);

        holder.getTxt_state().setText(data.getState());//data.getState());
        holder.getTxt_state().setTextColor(Color.BLACK);
    }
}
