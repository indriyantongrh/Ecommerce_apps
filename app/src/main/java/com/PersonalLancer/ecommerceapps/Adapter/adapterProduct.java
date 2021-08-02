package com.PersonalLancer.ecommerceapps.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.PersonalLancer.ecommerceapps.Model.ListproductItem;
import com.PersonalLancer.ecommerceapps.R;
import com.PersonalLancer.ecommerceapps.detailProduct;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Create by Indriyantongrh on 8/1/21
 */
public class adapterProduct extends  RecyclerView.Adapter<adapterProduct.ViewHolder> {

    private ArrayList<ListproductItem> mArrayList;
    private ArrayList<ListproductItem> mFilteredList;
    private Context context;
    Context mContext;

    public adapterProduct (Context context, ArrayList<ListproductItem> arrayList) {
        this.context = context;
        this.mArrayList = arrayList;
        this.mFilteredList = arrayList;

    }

    @NonNull
    @Override
    public adapterProduct.ViewHolder  onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_product, viewGroup, false);
        return new adapterProduct.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapterProduct.ViewHolder viewHolder, int position) {
        viewHolder.txt_id.setText(mFilteredList.get(position).getId());
        viewHolder.txtDescription.setText(mFilteredList.get(position).getDeskripsi());
        viewHolder.txtStock.setText(mFilteredList.get(position).getStock());
        viewHolder.txtCondition.setText(mFilteredList.get(position).getKondisi());
        viewHolder.txtWeight.setText(mFilteredList.get(position).getBerat());
        viewHolder.txtNameproduct.setText(mFilteredList.get(position).getNamaProduct());
        viewHolder.txtPrice.setText(mFilteredList.get(position).getHarga());
        viewHolder.txtNamemerchant.setText(mFilteredList.get(position).getMerchant());
        viewHolder.txtImage.setText(mFilteredList.get(position).getGambar());
        Glide.with(context)
                .load(mFilteredList.get(position).getGambar())
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(viewHolder.image);

    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txt_id, txtDescription,txtStock,txtCondition, txtWeight, txtNamemerchant,txtPrice, txtImage,txtNameproduct;
        ImageView image;
        CardView btnclick;

        public ViewHolder(View view) {
            super(view);
            txt_id = (TextView) view.findViewById(R.id.txt_id);
            txtDescription = (TextView) view.findViewById(R.id.txtDescription);
            txtStock = (TextView) view.findViewById(R.id.txtStock);
            txtCondition = (TextView) view.findViewById(R.id.txtCondition);
            txtWeight = (TextView) view.findViewById(R.id.txtWeight);
            txtNamemerchant = (TextView) view.findViewById(R.id.txtNamemerchant);
            txtNameproduct = (TextView) view.findViewById(R.id.txtNameproduct);
            txtPrice = (TextView) view.findViewById(R.id.txtPrice);
            txtImage = (TextView) view.findViewById(R.id.txtImage);
            image = (ImageView) view.findViewById(R.id.image);
            btnclick = (CardView) view.findViewById(R.id.btnclick);

            btnclick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent detail = new Intent(view.getContext(), detailProduct.class);

                    detail.putExtra("id", txt_id.getText());
                    detail.putExtra("deskripsi", txtDescription.getText());
                    detail.putExtra("stock", txtStock.getText());
                    detail.putExtra("kondisi", txtCondition.getText());
                    detail.putExtra("berat", txtWeight.getText());
                    detail.putExtra("merchant", txtNamemerchant.getText());
                    detail.putExtra("namaProduct", txtNameproduct.getText());
                    detail.putExtra("harga", txtPrice.getText());
                    detail.putExtra("gambar", txtImage.getText());
                    view.getContext().startActivity(detail);
                }
            });


        }

        @Override
        public void onClick(View view) {
            Intent detail = new Intent(view.getContext(), detailProduct.class);

            detail.putExtra("id", txt_id.getText());
            detail.putExtra("deskripsi", txtDescription.getText());
            detail.putExtra("stock", txtStock.getText());
            detail.putExtra("kondisi", txtCondition.getText());
            detail.putExtra("berat", txtWeight.getText());
            detail.putExtra("merchant", txtNamemerchant.getText());
            detail.putExtra("namaProduct", txtNameproduct.getText());
            detail.putExtra("harga", txtPrice.getText());
            detail.putExtra("gambar", txtImage.getText());
            view.getContext().startActivity(detail);
        }
    }
}
