package com.example.libassistant;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class BookInfoAdapter extends RecyclerView.Adapter<BookInfoAdapter.ViewHolder> {
    private static List<BookInfo> mBooks;
    private View view;
    private static OnItemClickListener onItemClickListener;
    public BookInfoAdapter(List<BookInfo> books){
        mBooks=books;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.borrowed_books_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookInfo bookInfo = (BookInfo) mBooks.get(position);
        holder.textViewTitle.setText("书名：" + bookInfo.getTitle());
        holder.textViewGch.setText("馆藏号：" + bookInfo.getGch());
        holder.textViewGcd.setText("馆藏地点：" + bookInfo.getGcd());
        holder.textViewBDate.setText("借书日期：" + bookInfo.getBdate());
        holder.textViewRDate.setText("还书日期：" + bookInfo.getRdate());
        holder.textViewCqqk.setText("超期情况：" + bookInfo.getCqqk());


        Glide.with(view)
                .load(bookInfo.getBookPhoto())
                .into(holder.imageViewBookPhoto);
        if (bookInfo.getRdate().equals("") & bookInfo.getCqqk().equals("")) {
            holder.imageXujie.setVisibility(View.VISIBLE);
            holder.imageZhuanjie.setVisibility(View.VISIBLE);
        } else{
            holder.imageXujie.setVisibility(View.GONE);
            holder.imageZhuanjie.setVisibility(View.GONE);
            holder.blank_line.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewBookPhoto;
        TextView textViewTitle;
        TextView textViewGch;
        TextView textViewGcd;
        TextView textViewBDate;
        TextView textViewRDate;
        TextView textViewCqqk;
        ImageView imageXujie;
        ImageView imageZhuanjie;
        View blank_line;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewBookPhoto= itemView.findViewById(R.id.imgbook);
            textViewTitle=itemView.findViewById(R.id.txttitle);
            textViewGch=itemView.findViewById(R.id.txtgch);
            textViewGcd=itemView.findViewById(R.id.txtgcd);
            textViewBDate=itemView.findViewById(R.id.txtjssj);
            textViewRDate=itemView.findViewById(R.id.txthssj);
            textViewCqqk=itemView.findViewById(R.id.txtcqqk);
            imageZhuanjie=itemView.findViewById(R.id.btn_zhuanjie);
            imageXujie=itemView.findViewById(R.id.btn_xujie);
            blank_line=itemView.findViewById(R.id.blank_line);
            imageXujie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null){
                        onItemClickListener.OnItemClick(v, mBooks.get(getLayoutPosition()));
                    }
                }
            });
            imageZhuanjie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null){
                        onItemClickListener.OnItemClick(v, mBooks.get(getLayoutPosition()));
                    }
                }
            });
        }
    }
    public interface OnItemClickListener{
        public void OnItemClick(View view, BookInfo data);
        }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
