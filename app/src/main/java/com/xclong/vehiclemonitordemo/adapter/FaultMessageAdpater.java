package com.xclong.vehiclemonitordemo.adapter;

import android.content.Context;

import com.xclong.vehiclemonitordemo.R;
import com.xclong.vehiclemonitordemo.adapter.base.BaseViewHolder;
import com.xclong.vehiclemonitordemo.adapter.base.CommonAdapter;
import com.xclong.vehiclemonitordemo.constant.AbnormalInfo;

import java.util.List;

/**
 * Created by xclong on 2016/9/1.
 */

public class FaultMessageAdpater extends CommonAdapter<AbnormalInfo> {
    public FaultMessageAdpater(Context mContext, int layoutId, List<AbnormalInfo> mData) {
        super(mContext, layoutId, mData);
    }

    @Override
    public void convert(BaseViewHolder holder, AbnormalInfo abnormalInfo) {
        holder.setText(R.id.tv_item_time, abnormalInfo.getTime());
        holder.setText(R.id.tv_item_date, abnormalInfo.getDate());
        holder.setText(R.id.tv_item_message, abnormalInfo.getMessage());
    }
}
