package com.example.project007;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class RvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<UserEntity> mlist;
    private final Context context;
    public RvAdapter(List<UserEntity> list, Context context) {
        this.mlist = list;
        this.context = context;
    }

    //第一步 定义接口
    public interface OnItemClickListener {
        void onClick(int position);
    }

    private OnItemClickListener listener;

    //第二步， 写一个公共的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }




    public interface OnItemLongClickListener {
        void onClick(int position);
    }

    private OnItemLongClickListener longClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }


    public void  addAll(List<UserEntity> list) {
        this.mlist.clear();
        this.mlist.addAll(list) ;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_recyc, null);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final int positionf = position;
        MyViewHolder holder1 = (MyViewHolder) holder;
        holder1.itemName.setText("UserName  :  "+mlist.get(positionf).getUsername());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(positionf);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (longClickListener != null) {
                    longClickListener.onClick(positionf);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView itemName;
        public MyViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
        }
    }
}