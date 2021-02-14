package com.example.libassistant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {

    private List<Msg> msgList;
    public String userpic;
    public MsgAdapter(List<Msg> msgList) {
        this.msgList = msgList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.assist_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String u=userpic;
        Msg msg=msgList.get(position);
        switch (msg.getType()){
            case Msg.TYPE_RECEIVE:
                Glide.with(holder.left)
                        .load(R.drawable.xiaozhi)
                        .dontAnimate()
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(holder.img_xiaozhi);
                holder.txtReceive.setText(msg.getContent());
                holder.right.setVisibility(View.GONE);
                break;
            case Msg.TYPE_SEND:
                Glide.with(holder.right)
                        .load(userpic)
                        .dontAnimate()
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(holder.img_me);
                holder.txtSend.setText(msg.getContent());
                holder.left.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtReceive;
        private TextView txtSend;
        private View left;
        private View right;
        private ImageView img_xiaozhi;
        private ImageView img_me;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtReceive=itemView.findViewById(R.id.textViewLeft);
            txtSend=itemView.findViewById(R.id.textViewRight);
            left=itemView.findViewById(R.id.assist_left);
            right=itemView.findViewById(R.id.assist_right);
            img_xiaozhi=itemView.findViewById(R.id.xiaozhi);
            img_me=itemView.findViewById(R.id.me);
        }
    }
}
