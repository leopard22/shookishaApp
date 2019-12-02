package com.example.shookisha.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shookisha.R;
import com.example.shookisha.entity.Bascket;
import com.example.shookisha.shared.Api;
import com.example.shookisha.utility.ToolsUtility;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PanierRecyclerViewAdapter extends RecyclerView.Adapter<PanierRecyclerViewAdapter.ViewHolder> {
    private List<Bascket> panier;
    private Context context;
    private Api api;
    private ToolsUtility toolsUtility ;

    public PanierRecyclerViewAdapter(List<Bascket> panier, Context context) {
        this.panier = panier;
        this.context = context;
        api = new Api();
        toolsUtility = new ToolsUtility();
    }


    @Override
    public PanierRecyclerViewAdapter.ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.panier_item, viewGroup, false);
        PanierRecyclerViewAdapter.ViewHolder vholder = new PanierRecyclerViewAdapter.ViewHolder(view);

        return vholder;
    }

    @Override
    public void onBindViewHolder(PanierRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        viewHolder.couponSku.setText(panier.get(position).getCouponSku() );
        viewHolder.operationSelectionDate.setText( toolsUtility.dateToString(panier.get(position).getOperationSelectionDate()));
        Picasso.get().load(api.getBaseUrlCoupon()+panier.get(position).getCouponImageSku()).into(viewHolder.couponImageSku);
        Picasso.get().load(api.getBaseUrlImg()+panier.get(position).getOfferImageLabel()).into(viewHolder.offerImageLabel);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView operationSelectionDate, couponSku;
        public ImageView offerImageLabel, couponImageSku;

        public ViewHolder(View view) {
            super(view);
            operationSelectionDate =  view.findViewById(R.id.operationSelectionDate);
            couponSku = view.findViewById(R.id.couponSku);
            offerImageLabel = view.findViewById(R.id.offerImageLabel);
            couponImageSku = view.findViewById(R.id.couponImageSku);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "You clicked coupons at  "+ getAdapterPosition(),
                    Toast.LENGTH_LONG).show();
        }
    }
}
