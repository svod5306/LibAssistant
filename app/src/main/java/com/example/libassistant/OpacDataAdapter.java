package com.example.libassistant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.libassistant.OpacData;
import com.example.libassistant.R;

import java.util.List;

public class OpacDataAdapter extends RecyclerView.Adapter<OpacDataAdapter.ViewHolder> {
    private List<OpacData> mOpacData;

    private View view;
    public OpacDataAdapter(List<OpacData> mOpacData) {
        this.mOpacData = mOpacData;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.opac_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OpacDataAdapter.ViewHolder holder, int position) {
        OpacData data=(OpacData)mOpacData.get(position);
        holder.title.setText(data.getTitle());
        holder.author.setText(data.getAuthor()+"编写");
        holder.isbn.setText("ISBN号码："+data.getIsbn());
        holder.ssh.setText("索书号："+data.getSsh());
        holder.cbd.setText(data.getCbd()+"出版");
        holder.gcd.setText(data.getGcd());
        holder.gch.setText("馆藏号："+data.getGch());
        Glide.with(view)
                .load(data.getBookimage())
                .into(holder.bookimage);
    }

    @Override
    public int getItemCount() {
        return mOpacData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView author;
        TextView isbn;
        TextView ssh;
        TextView cbd;
        TextView gcd;
        TextView gch;
        ImageView bookimage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.textViewTitle);
            author=itemView.findViewById(R.id.textViewAuthor);
            isbn=itemView.findViewById(R.id.textViewIsbn);
            ssh=itemView.findViewById(R.id.textViewSsh);
            cbd=itemView.findViewById(R.id.tetxViewCbd);
            gcd=itemView.findViewById(R.id.textViewGcd);
            gch=itemView.findViewById(R.id.textViewGch);
            bookimage=itemView.findViewById(R.id.imgbook_opac);
        }
    }
}
