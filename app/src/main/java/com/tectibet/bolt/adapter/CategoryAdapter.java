package com.tectibet.bolt.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tectibet.bolt.ItemActivity;
import com.tectibet.bolt.ItemsActivity;
import com.tectibet.bolt.R;
import com.tectibet.bolt.domain.Category;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by kharag on 04-04-2020.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context context;
    private List<Category> mCategoryList;

    public CategoryAdapter(Context context, List<Category> mCategoryList) {
        this.context=context;
        this.mCategoryList=mCategoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_category_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Glide.with(context).load(mCategoryList.get(position).getImg_url()).into(holder.mTypeImg);
        holder.mTypeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ItemsActivity.class);
                intent.putExtra("type",mCategoryList.get(position).getType());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView mTypeImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTypeImg=itemView.findViewById(R.id.category_img);
        }
    }
}
