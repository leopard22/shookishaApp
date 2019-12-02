package com.example.shookisha.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shookisha.R;
import com.example.shookisha.entity.Infos;
import com.example.shookisha.shared.Api;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class InfosRecyclerViewAdapter extends RecyclerView.Adapter<InfosRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<Infos> infosList;
    private Context context;
    private Api api;
    private ItemClickListener mClickListener;
    private LayoutInflater inflater;

    public InfosRecyclerViewAdapter(ArrayList<Infos> infosLst, Context ctx){
        this.infosList = infosLst;
        this.context = ctx;
        api = new Api();
        inflater = LayoutInflater.from(ctx);
    }

    @Override
    public InfosRecyclerViewAdapter.MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.infos, parent, false);
        MyViewHolder myholder = new MyViewHolder(view);
        return myholder;
    }

    @Override
    public void onBindViewHolder(InfosRecyclerViewAdapter.MyViewHolder holder, int poition) {

        holder.labelInfosTV.setText(infosList.get(poition).getLabelInfos());
       // Picasso.get().load(api.getBaseUrlImg()+dataModelList.get(position).getImgOffer()[0].getOfferImageLabel()).into(holder.imageOffer);
        Picasso.get().load(api.getBaseUrlImg()+infosList.get(poition).getImageInfos()).into(holder.imageInfosIV);
    }

    @Override
    public int getItemCount() {
        return infosList.size();
    }

    public Infos getItem(int position) {
        System.out.println("adpter ::  method getItem infos(objet)");
        return infosList.get(position);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView imageInfosIV;
        public TextView labelInfosTV;

        public MyViewHolder(View view){
            super(view);

            imageInfosIV = view.findViewById(R.id.imageInfosIV);
            labelInfosTV = view.findViewById(R.id.labelInfosTV);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView infosRecyclerView) {
        super.onAttachedToRecyclerView(infosRecyclerView);
    }
}
