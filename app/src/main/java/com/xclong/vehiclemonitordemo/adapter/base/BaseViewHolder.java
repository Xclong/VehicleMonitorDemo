package com.xclong.vehiclemonitordemo.adapter.base;

import android.content.Context;
import android.graphics.Typeface;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by xclong on 2016/9/1.
 */

public class BaseViewHolder {
    private SparseArray<View> views;
    private View mConvertView;
    private int mPosition;

    public BaseViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.mPosition = position;
        this.mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        this.views = new SparseArray<>();
        mConvertView.setTag(this);
    }

    public static BaseViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new BaseViewHolder(context, parent, layoutId, position);
        } else {
            BaseViewHolder holder = (BaseViewHolder) convertView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }

    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    public View getmConvertView() {
        return this.mConvertView;
    }

    public void setText(int viewId, String text) {
        ((TextView) getView(viewId)).setText(text);
    }

    public void setTypeface(int viewId, Typeface typeface) {
        ((TextView) getView(viewId)).setTypeface(typeface);
    }

    public void setVisibility(int viewId, int i) {
        getView(viewId).setVisibility(i);
    }
}
