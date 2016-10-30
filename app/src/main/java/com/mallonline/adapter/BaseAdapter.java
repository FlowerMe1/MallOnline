package com.mallonline.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.mallonline.R;
import com.mallonline.databinding.EmptyRecyclerViewBinding;

import java.util.List;

public class BaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected final int TYPE_EMPTY = 0;
    protected final int TYPE_ITEM = 1;
    protected List items;
    protected LayoutInflater layoutInflater;

    public BaseAdapter(List items){
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());

        RecyclerView.ViewHolder holder = null;

        if (viewType == TYPE_EMPTY) {
            final EmptyRecyclerViewBinding binding =
                     DataBindingUtil.inflate(layoutInflater, R.layout.empty_recycler_view, parent, false);

            holder = new ViewHolder(binding);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return items.size() > 0 ? items.size() : 1;
    }

    public List getItems(){
        return items;
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        if (items == null || items.size() == 0) {
            return TYPE_EMPTY;
        }
        return TYPE_ITEM;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final EmptyRecyclerViewBinding binding;

        ViewHolder(final EmptyRecyclerViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void addItems(List itemsList) {
        this.items.addAll(itemsList);
        notifyDataSetChanged();
    }

    public void setItems(List items){
        this.items = items;
        notifyDataSetChanged();
    }

    public void clearItems() {
        this.items.clear();
    }

    public boolean isEmpty(){
        return (items == null || items.size() <=1);
    }

    protected void removeItem(int position){
        items.remove(position);
        notifyDataSetChanged();
    }
}
