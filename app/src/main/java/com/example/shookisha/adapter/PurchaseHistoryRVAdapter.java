package com.example.shookisha.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shookisha.R;
import com.example.shookisha.entity.PurchaseHistory;
import com.example.shookisha.shared.Api;
import com.example.shookisha.utility.ToolsUtility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PurchaseHistoryRVAdapter extends RecyclerView.Adapter<PurchaseHistoryRVAdapter.MyViewHolder>{
    private ArrayList<PurchaseHistory> achat;
    private Context context;
    private Api api;
    private ToolsUtility toolsUtility ;
    private LayoutInflater inflater;

    public PurchaseHistoryRVAdapter(ArrayList<PurchaseHistory> achat, Context context) {
        this.achat = achat;
        this.context = context;
        api = new Api();
        toolsUtility = new ToolsUtility();
        inflater = LayoutInflater.from(context);
    }

    @Override
    public PurchaseHistoryRVAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = inflater.inflate(R.layout.achat_item, parent, false);
        MyViewHolder vholder = new MyViewHolder(view);

        return vholder;
    }

    @Override
    public void onBindViewHolder(PurchaseHistoryRVAdapter.MyViewHolder viewHolder, int position) {
        viewHolder.achat_couponSku.setText( context.getString( R.string.numero_codebarre_achat,achat.get(position).getCouponSku())  );
        viewHolder.achat_operationSelectionDate.setText( context.getString(R.string.date_achat, toolsUtility.dateToString(achat.get(position).getOperationSelectionDate()) )  );
        Picasso.get().load(api.getBaseUrlImg()+achat.get(position).getOfferImageLabel()).into(viewHolder.achat_offerImageLabel);
    }

    @Override
    public int getItemCount() {
        return achat.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView achat_operationSelectionDate, achat_couponSku;
        public ImageView achat_offerImageLabel;

        public MyViewHolder(View view) {
            super(view);
            achat_operationSelectionDate =  view.findViewById(R.id.achat_operationSelectionDate);
            achat_couponSku = view.findViewById(R.id.achat_couponSku);
            achat_offerImageLabel = view.findViewById(R.id.achat_offerImageLabel);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "You clicked coupons at  "+ getAdapterPosition(),
                    Toast.LENGTH_LONG).show();
        }
    }
}
