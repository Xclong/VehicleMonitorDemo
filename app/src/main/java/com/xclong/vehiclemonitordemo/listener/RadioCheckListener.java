package com.xclong.vehiclemonitordemo.listener;

import android.widget.RadioGroup;

import com.xclong.vehiclemonitordemo.fragment.CurveChartFragment;

/**
 * Created by xcl02 on 2016/5/11.
 */
public class RadioCheckListener implements RadioGroup.OnCheckedChangeListener {
    private boolean changedGroup = false;
    //    private RadioButton radioButton;
    private RadioGroup rg_1, rg_2;
    private CurveChartFragment fragment;

    public RadioCheckListener(CurveChartFragment fragment, RadioGroup rg_1, RadioGroup rg_2) {
        this.fragment = fragment;
        this.rg_1 = rg_1;
        this.rg_2 = rg_2;
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
       /* if (!changedGroup) {
            changedGroup = true;
            if (group == rg_1) {
                RadioButton radioButton = (RadioButton) fragment.getActivity().findViewById(rg_2.getCheckedRadioButtonId());
                if (radioButton != null) {
                    rg_2.check(-1);
                    radioButton.setChecked(false);
                }
                switch (checkedId) {
                    case R.id.rb_nox_f:
                        Message msg1 = Message.obtain();
                        msg1.what = 0;
                        fragment.myHandler.sendMessage(msg1);
                        break;
                    case R.id.rb_nox_r:
                        Message msg2 = Message.obtain();
                        msg2.what = 1;
                        fragment.myHandler.sendMessage(msg2);
                        break;
                    case R.id.rb_dnox_effi:
                        Message msg3 = Message.obtain();
                        msg3.what = 2;
                        fragment.myHandler.sendMessage(msg3);
                        break;
                    case R.id.rb_l19:
                        Message msg4 = Message.obtain();
                        msg4.what = 3;
                        fragment.myHandler.sendMessage(msg4);
                        break;
                }

            } else if (group == rg_2) {
                RadioButton radioButton = (RadioButton) fragment.getActivity().findViewById(rg_1.getCheckedRadioButtonId());
                if (radioButton != null) {
                    rg_1.check(-1);
                    radioButton.setChecked(false);
                }

                switch (checkedId) {
                    case R.id.rb_tr21:
                        Message msg4 = Message.obtain();
                        msg4.what = 4;
                        fragment.myHandler.sendMessage(msg4);
                        break;
                    case R.id.rb_tc90:
                        Message msg5 = Message.obtain();
                        msg5.what = 5;
                        fragment.myHandler.sendMessage(msg5);
                        break;
                    case R.id.rb_tc92:
                        Message msg6 = Message.obtain();
                        msg6.what = 6;
                        fragment.myHandler.sendMessage(msg6);
                        break;
                    case R.id.rb_freq_pump:
                        Message msg7 = Message.obtain();
                        msg7.what = 7;
                        fragment.myHandler.sendMessage(msg7);
                        break;
                }
            }
        }
        changedGroup = false;*/
    }
}
