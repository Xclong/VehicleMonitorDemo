package com.xclong.vehiclemonitordemo.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xclong.vehiclemonitordemo.R;
import com.xclong.vehiclemonitordemo.constant.AbnormalInfo;

import java.util.List;

/**
 * Created by xcl02 on 2016/8/1.
 */
public class FaultAdapter extends RecyclerView.Adapter<FaultAdapter.ViewHolder> {

    private Typeface mTypeFaceLight, mTypeFaceThin, mTypeFaceRegular;

    private String TAG = FaultAdapter.class.getSimpleName();

    private List<AbnormalInfo> datas;

    public FaultAdapter(Context context, List<AbnormalInfo> datas) {

        mTypeFaceLight = Typeface.createFromAsset(context.getAssets(), "NotoSansHans-Light.ttf");
        mTypeFaceThin = Typeface.createFromAsset(context.getAssets(), "NotoSansHans-Thin.ttf");
        mTypeFaceRegular = Typeface.createFromAsset(context.getAssets(), "NotoSansHans-Regular.ttf");
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_fault_item, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fault_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.e(TAG, datas.get(position).getMessage());
//        holder.tv_item_title.setText(datas.get(position).getPlate_number());
        holder.tv_item_message.setText(datas.get(position).getMessage());
        holder.tv_item_date.setText(datas.get(position).getDate());
        holder.tv_item_time.setText(datas.get(position).getTime());

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View view;

        private final TextView /*tv_item_title, */tv_item_message, tv_item_date, tv_item_time;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
//            tv_item_title = (TextView) itemView.findViewById(R.id.tv_item_title);
            tv_item_message = (TextView) itemView.findViewById(R.id.tv_item_message);
            tv_item_date = (TextView) itemView.findViewById(R.id.tv_item_date);
            tv_item_time = (TextView) itemView.findViewById(R.id.tv_item_time);

//            tv_item_title.setTypeface(mTypeFaceRegular);
//            tv_item_message.setTypeface(mTypeFaceLight);
//            tv_item_date.setTypeface(mTypeFaceThin);
//            tv_item_time.setTypeface(mTypeFaceThin);
        }

        public View getView() {
            return view;
        }
    }
}
