package com.example.dass21;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<Integer> mNumeros;
    private Context mContext;
    private OnNoteListenner mOneOnNoteListenner;

    private List<Pergunta> mPerguntas;

    public RecyclerViewAdapter(Context context, ArrayList<Integer> numeros, OnNoteListenner onNoteListenner, List<Pergunta> perguntas) {
        mNumeros = numeros;
        mContext = context;
        mPerguntas = perguntas;

        this.mOneOnNoteListenner = onNoteListenner;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        return new ViewHolder(view, mOneOnNoteListenner);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(mContext)
                .asBitmap()
                .load(mNumeros.get(position));

        //CONSERTAR A PORRA DO POSITION PRA PINTAR OS BAGULHO
        holder.rvText.setText(String.valueOf(mNumeros.get(position)));
        /*if(mPerguntas.get(position).getRes() != 4){
            holder.rvText.setBackgroundColor(Color.RED);
        }*/
    }

    @Override
    public int getItemCount() {
        return mNumeros.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView rvText;
        OnNoteListenner onNoteListenner;

        public ViewHolder(View itemView, OnNoteListenner onNoteListenner) {
            super(itemView);
            rvText = itemView.findViewById(R.id.rvText);
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
