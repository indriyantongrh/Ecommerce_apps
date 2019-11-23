package com.PersonalLancer.ryudigitalprinting.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.PersonalLancer.ryudigitalprinting.KonfirmasiPesana.DetailKonfirmasi;
import com.PersonalLancer.ryudigitalprinting.Model.ModelStatusPesanan;
import com.PersonalLancer.ryudigitalprinting.R;

import java.util.ArrayList;

public class AdapterKonfirmasiPembayaran extends RecyclerView.Adapter<AdapterKonfirmasiPembayaran.ViewHolder> implements Filterable {

    private ArrayList<ModelStatusPesanan> mArrayList;
    private ArrayList<ModelStatusPesanan> mFilteredList;
    private Context context;


    public AdapterKonfirmasiPembayaran(Context context, ArrayList<ModelStatusPesanan> arrayList) {
        this.context = context;
        this.mArrayList = arrayList;
        this.mFilteredList = arrayList;

    }
    @Override
    public AdapterKonfirmasiPembayaran.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapterkonfirmasipembayaran, viewGroup, false);
        return new AdapterKonfirmasiPembayaran.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull AdapterKonfirmasiPembayaran.ViewHolder viewHolder, int i) {
        viewHolder.txt_id_transaksi.setText(mFilteredList.get(i).getId_transaksi());
        viewHolder.txt_namalengkap.setText(mFilteredList.get(i).getNamalengkap());
        viewHolder.txt_invoice.setText(mFilteredList.get(i).getNomor_invoice());
        viewHolder.txt_jeniscetak.setText(mFilteredList.get(i).getJenis_cetak());
       /// viewHolder.txt_statuspesanan.setText((mFilteredList.get(i).getStatus_pesanan()));
        viewHolder.txt_tanggalpesan.setText(mFilteredList.get(i).getTanggal_pesan());  //untuk mengirim url gambar ke berbagai activity
        viewHolder.txt_lebar.setText(mFilteredList.get(i).getLebar());
        viewHolder.txt_panjang.setText(mFilteredList.get(i).getPanjang());
        ///  viewHolder.txt_nama_perusahaan.setText(mFilteredList.get(i).getNama_perusahaan());
        viewHolder.txt_jumlahpesanan.setText(mFilteredList.get(i).getJumlah_pesanan());
        ///  viewHolder.txt_gambar.setText(mFilteredList.get(i).getGambar());
        viewHolder.txt_jumlahharga.setText(mFilteredList.get(i).getJumlah_harga());
        viewHolder.txt_statuspesanan.setText(mFilteredList.get(i).getStatus_transfer());
        viewHolder.txt_id_user.setText(mFilteredList.get(i).getId());

    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = mArrayList;
                } else {

                    ArrayList<ModelStatusPesanan> filteredList = new ArrayList<>();

                    for (ModelStatusPesanan androidVersion : mArrayList) {

                        if (androidVersion.getNomor_invoice().toLowerCase().contains(charString) || androidVersion.getJenis_cetak().toLowerCase().contains(charString) ) {
                            filteredList.add(androidVersion);
                        }
                    }
                    mFilteredList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<ModelStatusPesanan>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txt_invoice,txt_jeniscetak, txt_tanggalpesan, txt_lebar, txt_panjang,txt_namalengkap,
                txt_jumlahpesanan,txt_gambar, txt_jumlahharga,txt_statuspesanan,txt_statustransfer,txt_id_transaksi, txt_id_user;
        private ImageView image;

        public ViewHolder(View view) {
            super(view);

            txt_id_user = (TextView) view.findViewById(R.id.txt_id_user);
            txt_id_transaksi = (TextView) view.findViewById(R.id.txt_id_transaksi);
            txt_namalengkap = (TextView) view.findViewById(R.id.txt_namalengkap);
            txt_invoice = (TextView)view.findViewById(R.id.txt_invoice);
            txt_jeniscetak = (TextView)view.findViewById(R.id.txt_jeniscetak);
            txt_statuspesanan = (TextView)view.findViewById(R.id.txt_statuspesanan);
            txt_tanggalpesan = (TextView)view.findViewById(R.id.txt_tanggalpesan);
            ////image = (ImageView)view.findViewById(R.id.image);
            txt_lebar = (TextView)view.findViewById(R.id.txt_lebar);
            txt_panjang = (TextView)view.findViewById(R.id.txt_panjang);
            txt_jumlahpesanan = (TextView)view.findViewById(R.id.txt_jumlahpesanan);
            txt_jumlahharga = (TextView) view.findViewById(R.id.txt_jumlahharga);
            txt_statustransfer = (TextView) view.findViewById(R.id.txt_statustransfer);



            view.setOnClickListener(this);

            txt_invoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent detail = new Intent(view.getContext(), DetailKonfirmasi.class);

                    detail.putExtra("id_transaksi", txt_id_transaksi.getText());
                    detail.putExtra("id", txt_id_user.getText());
                    ///detail.putExtra("id_perusahaan", txt_id_perusahaan.getText());
                    detail.putExtra("namalengkap", txt_namalengkap.getText());
                    detail.putExtra("jenis_cetak", txt_jeniscetak.getText());
                    detail.putExtra("nomor_invoice", txt_invoice.getText());
                    detail.putExtra("lebar", txt_lebar.getText());
                    detail.putExtra("panjang", txt_panjang.getText());
                    detail.putExtra("jumlah_pesanan", txt_jumlahpesanan.getText());
                    detail.putExtra("jumlah_harga", txt_jumlahharga.getText());

/*                    detail.putExtra("id_pelamarmasuk", txt_id_pelamarmasuk.getText());
                    detail.putExtra("id_user", txt_id_user.getText());
                    detail.putExtra("id_perusahaan", txt_id_perusahaan.getText());
                    detail.putExtra("nama_perusahaan", txt_nama_perusahaan.getText());
                    detail.putExtra("id_lowongankerja", txt_id_lowongan.getText());
                    detail.putExtra("judul_lowongan", txt_judul_lowongan.getText());
                    detail.putExtra("deskripsi", txt_deskripsi.getText());
                    detail.putExtra("gaji", txt_gaji.getText());
                    detail.putExtra("deadline", txt_deadline.getText());
                    detail.putExtra("kategori", txt_kategori.getText());
                    detail.putExtra("status_pelamar", txt_statuspelamar.getText());
                    detail.putExtra("keterangan", txt_keterangan.getText());*/

                    view.getContext().startActivity(detail);
                }
            });
        }

        @Override
        public void onClick(View view) {
            Intent detail = new Intent(view.getContext(), DetailKonfirmasi.class);

            detail.putExtra("id_transaksi", txt_id_transaksi.getText());
            detail.putExtra("id", txt_id_user.getText());
            ///detail.putExtra("id_perusahaan", txt_id_perusahaan.getText());
            detail.putExtra("namalengkap", txt_namalengkap.getText());
            detail.putExtra("jenis_cetak", txt_jeniscetak.getText());
            detail.putExtra("nomor_invoice", txt_invoice.getText());
            detail.putExtra("lebar", txt_lebar.getText());
            detail.putExtra("panjang", txt_panjang.getText());
            detail.putExtra("jumlah_pesanan", txt_jumlahpesanan.getText());
            detail.putExtra("jumlah_harga", txt_jumlahharga.getText());
            ///detail.putExtra("kategori", txt_kategori.getText());
            ///detail.putExtra("status_pelamar", txt_statuspelamar.getText());
            ///detail.putExtra("keterangan", txt_keterangan.getText());

            view.getContext().startActivity(detail);

        }
    }
}
