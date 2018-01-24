package com.bqteam.appforlearn.function.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bqteam.appforlearn.R;

/**
 * @author charles
 * @date 2018/1/4
 */

public class EntranceAdapter extends RecyclerView.Adapter<EntranceAdapter.EntranceViewHolder> {
    private Context context;
    private String[] entranceList;

    public EntranceAdapter(Context context, String[] entranceList) {
        this.context = context;
        this.entranceList = entranceList;
    }

    @Override
    public EntranceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EntranceViewHolder(LayoutInflater.from(context).inflate(R.layout.item_entrance, parent, false));
    }

    @Override
    public void onBindViewHolder(EntranceViewHolder holder, int position) {
        final int p = position;
        holder.textView.setText(entranceList[position]);
        holder.textView.setOnClickListener(v -> itemClickListener.itemClickAt(p));
    }

    @Override
    public int getItemCount() {
        return entranceList == null ? 0 : entranceList.length;
    }

    class EntranceViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        EntranceViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.entrance_tv);
        }
    }

    /**
     * 设置点击事件
     */
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        /**
         * 监听点击事件
         *
         * @param position
         */
        void itemClickAt(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }
}
