package com.gz.mozixing.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gz.mozixing.R;
import com.gz.mozixing.interfaces.MyOnClickListener;
import com.gz.mozixing.network.model.MinuteModel;

import java.util.ArrayList;

/**
 * @author alex
 * @since 2017/11/27
 */

public class MinuteAdapter extends RecyclerView.Adapter<MinuteAdapter.VoucherHodler> {

    private Activity activity;
    private ArrayList<MinuteModel> list;
    private MyOnClickListener listener;

    public MinuteAdapter(Activity activity, ArrayList<MinuteModel> list, MyOnClickListener listener) {
        this.activity = activity;
        this.list = list;
        this.listener = listener;
    }

    @Override
    public VoucherHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VoucherHodler(LayoutInflater.from(activity).inflate(R.layout.item_minute, parent, false));
    }

    @Override
    public void onBindViewHolder(VoucherHodler holder, final int position) {
        holder.minute_tv.setText(list.get(position).getMinute());
        if (list.get(position).isSelect()) {
            holder.minute_tv.setTextColor(activity.getResources().getColor(R.color.colorWhite));
            holder.minute_tv.setBackgroundResource(R.drawable.five_blue_shape);
        } else {
            holder.minute_tv.setTextColor(activity.getResources().getColor(R.color.color_666666));
            holder.minute_tv.setBackgroundResource(R.drawable.five_white_shape);
        }
        holder.minute_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class VoucherHodler extends RecyclerView.ViewHolder {
        TextView minute_tv;

        public VoucherHodler(View itemView) {
            super(itemView);
            minute_tv = (TextView) itemView.findViewById(R.id.minute_tv);
        }
    }

}
