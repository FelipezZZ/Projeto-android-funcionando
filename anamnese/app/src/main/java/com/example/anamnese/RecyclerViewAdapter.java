package com.example.anamnese;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    private ArrayList<Integer> mNumeros;
    private Context mContext;
    private OnNoteListenner mOneOnNoteListenner;

    private ArrayList<Integer> mAnsiedade;
    private ArrayList<Integer> mDepressao;
    private ArrayList<Integer> mStress;


    public RecyclerViewAdapter(Context context, ArrayList<Integer> numeros, OnNoteListenner onNoteListenner, ArrayList<Integer> ansiedade, ArrayList<Integer> depressao, ArrayList<Integer> stress) {
        mNumeros = numeros;
        mContext = context;

        mAnsiedade = ansiedade;
        mDepressao = depressao;
        mStress= stress;

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

        holder.rvText.setText(String.valueOf(mNumeros.get(position)));
        if(mStress.get(0) != 4){
            if (position == 0) {
                holder.rvText.setTextColor(Color.RED);
            }
        }
        if(mStress.get(1) != 4){
            if (position == 5) {
                holder.rvText.setTextColor(Color.RED);
            }
        }
        if(mStress.get(2) != 4){
            if (position == 7) {
                holder.rvText.setTextColor(Color.RED);
            }
        }
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
