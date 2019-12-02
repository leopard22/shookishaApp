package com.example.shookisha;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shookisha.entity.Categorie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListPreferenceAdapter extends BaseAdapter {
    private Context context;
    public static ArrayList<Categorie> categorieArrayList;
    private String baseUrl ="http://www.worldcorpgroup.fr/shookisha/picture/preferences/xhdpi/";

    public ListPreferenceAdapter(Context context, ArrayList<Categorie> categorieArrayList){
        this.context = context;
        this.categorieArrayList = categorieArrayList;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }
    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getCount() {
        return categorieArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return categorieArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_categorie, null, true);

            holder.checkBoxCat = convertView.findViewById(R.id.checkBoxCat);
            holder.imgCat = convertView.findViewById(R.id.imgCat);
            holder.labelCat = convertView.findViewById(R.id.labelCat);
            holder.totaloffersCat = convertView.findViewById(R.id.totaloffersCat);

            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }

        //int id = context.getResources().getIdentifier("com.example.shookisha:drawable/"+categorieArrayList.get(position).getImg(), null, null);
       // holder.checkBoxCat.setText("Checkbox "+position);
       // System.out.println(categorieArrayList.get(position).getImg()+" "+categorieArrayList.get(position).getImg());
        Picasso.get().load(baseUrl+categorieArrayList.get(position).getImg()).into(holder.imgCat);
       // holder.imgCat.setImageResource(id);
        holder.labelCat.setText(categorieArrayList.get(position).getLabel());
        holder.totaloffersCat.setTextColor( context.getResources().getColor(R.color.colorText) );
        holder.totaloffersCat.setText("("+categorieArrayList.get(position).getTotalOffers()+")");
        holder.checkBoxCat.setChecked(Boolean.valueOf(categorieArrayList.get(position).getChecked()));

        holder.checkBoxCat.setTag(R.integer.btnplusview, convertView);
        holder.checkBoxCat.setTag( position);
        holder.checkBoxCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View tempview = (View) holder.checkBoxCat.getTag(R.integer.btnplusview);
                TextView labelCat = (TextView) tempview.findViewById(R.id.labelCat);
                ImageView imgCat =  tempview.findViewById(R.id.imgCat);
                TextView totaloffersCat = (TextView) tempview.findViewById(R.id.totaloffersCat);
                Integer pos = (Integer)  holder.checkBoxCat.getTag();
                //Toast.makeText(context, "Checkbox "+pos+" clicked!", Toast.LENGTH_SHORT).show();
System.out.println(labelCat.getText() +" checkbox cliké checkbox position :: "+pos);
                if(Boolean.valueOf(categorieArrayList.get(pos).getChecked())){
                    categorieArrayList.get(pos).setChecked("false");
                }else {
                    categorieArrayList.get(pos).setChecked("true");
                }

            }
        });

        return convertView;
    }


    private class ViewHolder {

        protected CheckBox checkBoxCat;
        private ImageView imgCat;
        private TextView labelCat;
        private TextView totaloffersCat;
    }

}