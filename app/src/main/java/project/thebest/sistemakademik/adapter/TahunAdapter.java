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
import project.thebest.sistemakademik.data.Tahun;

public class TahunAdapter extends RecyclerView.Adapter<TahunAdapter.TahunViewHolder> {
    private Context mContext;
    private ArrayList<Tahun> mTahun;
    private OnItemClickListener mListener;

    public TahunAdapter(Context mContext, ArrayList<Tahun> mTahun) {
        this.mContext = mContext;
        this.mTahun = mTahun;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public TahunViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_tahun, parent, false);
        return new TahunViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TahunViewHolder holder, int position) {
        Tahun currentItem = mTahun.get(position);
        String tahun = currentItem.getTahun();

        holder.tahun.setText(tahun);
    }

    @Override
    public int getItemCount() {
        return mTahun.size();
    }

    class TahunViewHolder extends RecyclerView.ViewHolder {
        public TextView tahun;

        public TahunViewHolder(View itemView) {
            super(itemView);
            tahun = itemView.findViewById(R.id.text_tahun);
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
