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
import project.thebest.sistemakademik.data.Information;

public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.InformationViewHolder> {
    private Context mContext;
    private ArrayList<Information> mInformationList;
    private OnItemClickListener mListener;

    public InformationAdapter(Context mContext, ArrayList<Information> mInformationList) {
        this.mContext = mContext;
        this.mInformationList = mInformationList;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public InformationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_information, parent, false);
        return new InformationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull InformationViewHolder holder, int position) {
        Information currentItem = mInformationList.get(position);
        String judul_info = currentItem.getJudul_info();
        String deskripsi_info = currentItem.getDeskripsi_info();

        if (deskripsi_info.length() > 100) {
            deskripsi_info = deskripsi_info.substring(0,100)+"...";
        }
        holder.judul.setText(judul_info);
        holder.deskripsi.setText(deskripsi_info);
    }

    @Override
    public int getItemCount() {
        return mInformationList.size();
    }

    class InformationViewHolder extends RecyclerView.ViewHolder {
        public TextView judul;
        public TextView deskripsi;
        public InformationViewHolder(View itemView) {
            super(itemView);
            judul = itemView.findViewById(R.id.text_judul_informasi);
            deskripsi = itemView.findViewById(R.id.text_isi_informasi);
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
}
