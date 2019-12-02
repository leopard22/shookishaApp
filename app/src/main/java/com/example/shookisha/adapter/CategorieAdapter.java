package com.example.shookisha.adapter;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.shookisha.R;
import com.example.shookisha.entity.Categorie;
import com.example.shookisha.entity.ResultFilter;

import java.util.ArrayList;
import java.util.List;

public class CategorieAdapter extends BaseAdapter implements SpinnerAdapter {
   // private List<Categorie> categorieList;
    private ArrayList<ResultFilter> resultFilters;
    private Context context;

    public CategorieAdapter(Context context, ArrayList<ResultFilter> resultFilters) {
        this.context = context;
        this.resultFilters = resultFilters;
    }

    @Override
    public int getCount() {
        return resultFilters.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public ResultFilter getItem(int position) {
        return resultFilters.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =  View.inflate(context, R.layout.categorie_offre_main, null);
        TextView textView = view.findViewById(R.id.categoriOffreMain);
        textView.setText(resultFilters.get(position).getCatLabel());
        return textView;
    }

    @Override
    public View getDropDownView(int position,View convertView, ViewGroup parent) {

        View view =  View.inflate(context, R.layout.categorie_offre, null);
        TextView nameCategorie = view.findViewById(R.id.nameCategorie);
        nameCategorie.setText(resultFilters.get(position).getCatLabel());
        return view;
    }

}
