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
import com.example.shookisha.entity.Bascket;
import com.example.shookisha.shared.Api;
import com.example.shookisha.utility.ToolsUtility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class PanierRecyclerViewAdapter extends RecyclerView.Adapter<PanierRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<Bascket> panier;
    private Context context;
    private Api api;
    private ToolsUtility toolsUtility ;
    private LayoutInflater inflater;

    public PanierRecyclerViewAdapter(ArrayList<Bascket> panier, Context context) {
        this.panier = panier;
        this.context = context;
        api = new Api();
        toolsUtility = new ToolsUtility();
        inflater = LayoutInflater.from(context);
    }


    @Override
    public PanierRecyclerViewAdapter.MyViewHolder onCreateViewHolder( ViewGroup parent, int i) {
        View view = inflater.inflate(R.layout.panier_item, parent, false);
        MyViewHolder vholder = new MyViewHolder(view);

        return vholder;
    }

    @Override
    public void onBindViewHolder(PanierRecyclerViewAdapter.MyViewHolder viewHolder, int position) {
        viewHolder.couponSku.setText(panier.get(position).getCouponSku() );
        viewHolder.operationSelectionDate.setText( context.getString(R.string.date_panier, toolsUtility.dateToString(panier.get(position).getOperationSelectionDate())) );
        Picasso.get().load(api.getBaseUrlCoupon()+panier.get(position).getCouponImageSku()).into(viewHolder.couponImageSku);
        Picasso.get().load(api.getBaseUrlImg()+panier.get(position).getOfferImageLabel()).into(viewHolder.offerImageLabel);
    }

    @Override
    public int getItemCount() {
        return panier.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView operationSelectionDate, couponSku;
        public ImageView offerImageLabel, couponImageSku;

        public MyViewHolder(View view) {
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
