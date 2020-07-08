package project.thebest.sistemakademik.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import project.thebest.sistemakademik.R;
import project.thebest.sistemakademik.data.Presensi;

public class PresensiAdapter extends RecyclerView.Adapter<PresensiAdapter.PresensiViewHolder>{
    private Context mContext;
    private ArrayList<Presensi> mPresensiList;
    private OnItemClickListener mListener;

    public PresensiAdapter(Context mContext, ArrayList<Presensi> mPresensiList) {
        this.mContext = mContext;
        this.mPresensiList = mPresensiList;
    }

    @NonNull
    @Override
    public PresensiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_presensi, parent, false);
        return new PresensiViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PresensiViewHolder holder, int position) {
        Presensi currentItem = mPresensiList.get(position);
        String text_mapel = currentItem.getMapel() + " " + currentItem.getKelas();
        String text_jadwal = namaHari(currentItem.getNama_hari()) + " " + currentItem.getJam();
        String text_ket = currentItem.getKeterangan();

        holder.mapel.setText(text_mapel);
        holder.jadwal.setText(text_jadwal);
        holder.ket.setText(text_ket);
    }

    @Override
    public int getItemCount() {
        return mPresensiList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    class PresensiViewHolder extends RecyclerView.ViewHolder {
        public TextView mapel;
        public TextView jadwal;
        public TextView ket;
        public PresensiViewHolder(View itemView) {
            super(itemView);
            mapel = itemView.findViewById(R.id.text_mapel);
            jadwal = itemView.findViewById(R.id.text_jadwal);
            ket = itemView.findViewById(R.id.text_keterangan);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    private String namaHari(String hari) {
        switch (hari) {
            case "Sunday":
                return "Minggu";
            case "Monday":
                return "Senin";
            case "Tuesday":
                return "Selasa";
            case "Wednesday":
                return "Rabu";
            case "Thursday":
                return "Kamis";
            case "Friday":
                return "Jumat";
            case "Saturday":
                return "Sabtu";
            default:
                return "hari tidak valid";
        }
    }
}
