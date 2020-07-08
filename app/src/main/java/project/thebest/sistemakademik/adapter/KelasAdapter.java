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
import project.thebest.sistemakademik.data.Kelas;

public class KelasAdapter extends RecyclerView.Adapter<KelasAdapter.KelasViewHolder> {
    private Context mContext;
    private ArrayList<Kelas> mKelas;
    private OnItemClickListener mListener;

    public KelasAdapter(Context mContext, ArrayList<Kelas> mKelas) {
        this.mContext = mContext;
        this.mKelas = mKelas;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public KelasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_kelas, parent, false);
        return new KelasViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull KelasViewHolder holder, int position) {
        Kelas currentItem = mKelas.get(position);
        String kelas = currentItem.getKelas();

        holder.kelas.setText(kelas);
    }

    @Override
    public int getItemCount() {
        return mKelas.size();
    }

    class KelasViewHolder extends RecyclerView.ViewHolder {
        public TextView kelas;

        public KelasViewHolder(View itemView) {
            super(itemView);
            kelas = itemView.findViewById(R.id.text_kelas);
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
