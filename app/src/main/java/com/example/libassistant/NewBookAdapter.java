package com.example.libassistant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class NewBookAdapter extends RecyclerView.Adapter<NewBookAdapter.ViewHolder> {
    private static List<BookInfo> mBooks;
    private View view;

    public NewBookAdapter(List<BookInfo> books){
        mBooks=books;
    }
    @NonNull
    @Override
    public NewBookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.borrowed_books_item,parent,false);
        NewBookAdapter.ViewHolder viewHolder=new NewBookAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewBookAdapter.ViewHolder holder, int position) {
        BookInfo bookInfo = (BookInfo) mBooks.get(position);
        holder.textViewTitle.setText("书名：" + bookInfo.getTitle());
        holder.textViewGch.setText("馆藏号：" + bookInfo.getGch());
        holder.textViewGcd.setText("馆藏地点：" + bookInfo.getGcd());
        holder.textViewBDate.setVisibility(View.GONE);
        holder.textViewRDate.setVisibility(View.GONE);
        holder.textViewCqqk.setVisibility(View.GONE);


        Glide.with(view)
                .load(bookInfo.getBookPhoto())
                .into(holder.imageViewBookPhoto);
        holder.imageXujie.setVisibility(View.GONE);
        holder.imageZhuanjie.setVisibility(View.GONE);
        holder.blank_line.setVisibility(View.VISIBLE);
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
        }
    }
}
