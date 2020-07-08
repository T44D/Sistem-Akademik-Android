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
import project.thebest.sistemakademik.data.NilaiPTS;

public class NilaiPTSAdapter extends RecyclerView.Adapter<NilaiPTSAdapter.NilaiViewHolder> {
    private Context mContext;
    private ArrayList<NilaiPTS> mNilaiList;
    private OnItemClickListener mListener;

    public NilaiPTSAdapter(Context mContext, ArrayList<NilaiPTS> mNilaiList) {
        this.mContext = mContext;
        this.mNilaiList = mNilaiList;
    }

    @NonNull
    @Override
    public NilaiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_nilai, parent, false);
        return new NilaiViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NilaiViewHolder holder, int position) {
        NilaiPTS currentItem = mNilaiList.get(position);
        String mapel = currentItem.getMapel();
        holder.mapel.setText(mapel);
    }

    @Override
    public int getItemCount() {
        return mNilaiList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    class NilaiViewHolder extends RecyclerView.ViewHolder {
        public TextView mapel;
        public NilaiViewHolder(View itemView) {
            super(itemView);
            mapel = itemView.findViewById(R.id.text_mata_pelajaran);
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
