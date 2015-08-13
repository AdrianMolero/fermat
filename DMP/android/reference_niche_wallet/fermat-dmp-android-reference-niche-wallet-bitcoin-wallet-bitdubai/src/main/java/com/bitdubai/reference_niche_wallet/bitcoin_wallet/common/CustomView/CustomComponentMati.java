package com.bitdubai.reference_niche_wallet.bitcoin_wallet.common.CustomView;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitdubai.android_fermat_dmp_wallet_bitcoin.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matias Furszyfer on 2015.08.12..
 */


public class CustomComponentMati extends LinearLayout {

    /**
     *  Header
     */
    TextView txtLastTransactions;
    TextView txtSeeAlltransactions;


    /**
     *  Body
     */
    TextView txtViewTitleTransaction;
    TextView txtViewDetailTransaction;

    ImageView imageView_transaction;


    /**
     *  Buttons
     */
    ImageView imageView_action_next;
    ImageView imageView_action_prev;

    /**
     * Data
     */
    private List<CustomComponentsObjects> lstData;

    /**
     * List position
     */
    private int listPosition;

    /**
     * Resources
     */
    private Resources resources;


    LinearLayout linearLayout_container;


    Animation animationNext;
    Animation animationPrev;
    /**
     *
     * @param context
     * @param attrst
     */
    public CustomComponentMati(Context context, AttributeSet attrst){
        super(context, attrst);
        initControl(context);
        initData();
    }

    private void initData() {
        lstData= new ArrayList<CustomComponentsObjects>();
    }

    /**
     * Load component XML layout
     */
    private void initControl(final Context context)
    {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.wallet_manager_transaction_view, this);

        // layout is inflated, assign local variables to components
        txtLastTransactions = (TextView) findViewById(R.id.txtLastTransactions);
        txtSeeAlltransactions = (TextView) findViewById(R.id.txtSeeAlltransactions);
        txtViewTitleTransaction = (TextView) findViewById(R.id.txtViewTitleTransaction);
        txtViewDetailTransaction = (TextView) findViewById(R.id.txtViewDetailTransaction);

        imageView_transaction = (ImageView)findViewById(R.id.imageView_transaction);
        imageView_action_next = (ImageView)findViewById(R.id.imageView_action_next);
        imageView_action_prev = (ImageView)findViewById(R.id.imageView_action_prev);

        imageView_action_next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listPosition++;

                if( lstData.size()>listPosition){
                    linearLayout_container.startAnimation(animationNext);
                    load(listPosition);
                }else {
                    listPosition--;
                }

            }
        });
        imageView_action_prev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listPosition--;


                if (listPosition > -1) {
                    linearLayout_container.startAnimation(animationPrev);
                    load(listPosition);
                } else {
                    listPosition++;
                }

            }
        });

        linearLayout_container = (LinearLayout) findViewById(R.id.linearLayout_container);
        animationNext =    AnimationUtils.loadAnimation(context, R.anim.slide_out_right);
        animationPrev = AnimationUtils.loadAnimation(context, R.anim.slide_in_left);

    }

    public void setDataList(List<CustomComponentsObjects> lstData){
        this.lstData=lstData;
        listPosition=0;
        load(listPosition);
    }
    public void setResources(Resources resources){
        this.resources=resources;
    }

    private void load(int position){
        if(!lstData.isEmpty() && lstData.size()>listPosition  && listPosition>-1) {

            CustomComponentsObjects customComponentsObjects = lstData.get(position);

            txtViewTitleTransaction.setText(customComponentsObjects.getTitle());
            txtViewDetailTransaction.setText(customComponentsObjects.getDetail());
            //imageView_transaction.setImageDrawable();
            imageView_transaction.setImageResource(
                    resources.getIdentifier(
                            "com.bitdubai.reference_niche_wallet.bitcoin_wallet:drawable/" + customComponentsObjects.getImageUrl()
                            , null, null));
        }

        invalidate();
    }


    public void setLastTransactionsEvent(OnClickListener onClickListener){
        this.txtLastTransactions.setOnClickListener(onClickListener);
    }
    public void setSeeAlltransactionsEvent(OnClickListener onClickListener){
        this.txtSeeAlltransactions.setOnClickListener(onClickListener);
    }


}
