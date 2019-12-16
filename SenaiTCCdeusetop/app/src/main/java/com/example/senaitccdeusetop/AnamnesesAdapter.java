package com.example.senaitccdeusetop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.senaitccdeusetop.Activitys.AnamnesesActivity;
import com.example.senaitccdeusetop.Vo.Anamnese;
import com.example.senaitccdeusetop.Vo.Pergunta;

import java.util.ArrayList;
import java.util.List;

public class AnamnesesAdapter extends RecyclerView.Adapter<AnamnesesAdapter.ViewHolder> {

    private List<Anamnese> mAnamneses;
    private Context mContext;
    private OnNoteListenner mOneOnNoteListenner;

    public AnamnesesAdapter(Context context, List<Anamnese> anamneses, OnNoteListenner onNoteListenner) {
        mAnamneses = anamneses;
        mContext = context;

        this.mOneOnNoteListenner = onNoteListenner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        return new ViewHolder(view, mOneOnNoteListenner);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvData.setText(String.valueOf(mAnamneses.get(position).getDataAnamneses()));
        holder.tvA.setText(String.valueOf(mAnamneses.get(position).getAnsiedade()));
        holder.tvD.setText(String.valueOf(mAnamneses.get(position).getDepressao()));
        holder.tvS.setText(String.valueOf(mAnamneses.get(position).getEstresse()));
    }


    @Override
    public int getItemCount() {
        return mAnamneses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvData, tvA, tvD, tvS;
        OnNoteListenner onNoteListenner;

        public ViewHolder(View itemView, OnNoteListenner onNoteListenner) {
            super(itemView);
            tvData = itemView.findViewById(R.id.tvData);
            tvA = itemView.findViewById(R.id.tvA);
            tvD = itemView.findViewById(R.id.tvD);
            tvS = itemView.findViewById(R.id.tvS);
            this.onNoteListenner = onNoteListenner;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onNoteListenner.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListenner{
        void onNoteClick(int position);
    }

}
