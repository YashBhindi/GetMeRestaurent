package com.example.yash.getmerestaurent;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yash.getmerestaurent.R;
import com.example.yash.getmerestaurent.RestaurentMenuAddItemBean;

import java.util.List;

/**
 * Created by yash on 3/4/18.
 */

class RestaurentMenuAdapter extends RecyclerView.Adapter<RestaurentMenuAdapter.RestaurentMenuViewHolder>
{

    private Context ctx;
    private List<RestaurentMenuAddItemBean> itemBeanList;

    public RestaurentMenuAdapter(Context ctx, List<RestaurentMenuAddItemBean> itemBeanList) {
        this.ctx = ctx;
        this.itemBeanList = itemBeanList;
    }

    @Override
    public RestaurentMenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.menu_item_list,null);
        RestaurentMenuViewHolder holder = new RestaurentMenuViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RestaurentMenuViewHolder holder, int position) {
        RestaurentMenuAddItemBean menuAddItemBean = itemBeanList.get(position);
        holder.name.setText("Item:"+menuAddItemBean.getName());
        double p=menuAddItemBean.getPrice();

        holder.price.setText("Price:"+String.valueOf(p) );
        holder.category.setText("Category:"+menuAddItemBean.getCategory());
    }

    @Override
    public int getItemCount() {
        return itemBeanList.size();
    }

    class RestaurentMenuViewHolder extends RecyclerView.ViewHolder{

        TextView name,price,category;
        public RestaurentMenuViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            category = itemView.findViewById(R.id.category);
        }
    }
}