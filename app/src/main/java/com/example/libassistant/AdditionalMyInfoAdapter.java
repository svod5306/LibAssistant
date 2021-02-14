package com.example.libassistant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdditionalMyInfoAdapter extends RecyclerView.Adapter<AdditionalMyInfoAdapter.ViewHolder> {
    private List<AdditionalMyInfo> m_services;
    private ItemClickListener mItemClickListener ;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.addition_service_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    public AdditionalMyInfoAdapter(List<AdditionalMyInfo> m_services){
        this.m_services=m_services;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        AdditionalMyInfo myInfo=(AdditionalMyInfo)m_services.get(position);
        holder.viewserviceName.setText(myInfo.getServiceName());
        holder.viewServiceIcon.setImageResource(myInfo.getServiceIcon());
        if(mItemClickListener!=null){
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return m_services.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView viewserviceName;
        ImageView viewServiceIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
            viewserviceName= itemView.findViewById(R.id.service_Name);
            viewServiceIcon= itemView.findViewById(R.id.service_Icon);
        }
    }
    // 利用接口 -> 给RecyclerView设置点击事件

    public interface ItemClickListener{
        public void onItemClick(int position) ;
    }
    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.mItemClickListener = itemClickListener ;

    }
}
