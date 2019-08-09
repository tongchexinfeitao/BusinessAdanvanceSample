package com.example.myapplication.cart;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyAddSubView extends LinearLayout {
    @BindView(R.id.sub_tv)
    TextView mSubTv;
    @BindView(R.id.product_number_tv)
    TextView mProductNumberTv;
    @BindView(R.id.add_tv)
    TextView mAddTv;
    private int mNumber = 1;


    public MyAddSubView(Context context) {
        super(context);
    }

    public MyAddSubView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View inflate = inflate(context, R.layout.add_remove_view_layout, this);
        ButterKnife.bind(this, inflate);
    }

    public void setNumber(int num) {
        mNumber = num;
        mProductNumberTv.setText("" + mNumber);
    }

    public int getNumber() {
        return mNumber;
    }

    @OnClick({R.id.sub_tv, R.id.add_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sub_tv:
                if (mNumber < 2) {
                    Toast.makeText(getContext(), "不能再少了", Toast.LENGTH_SHORT).show();
                } else {
                    mNumber--;
                    if (onNumberChangeListener != null) {
                        onNumberChangeListener.onNumberChange(mNumber);
                    }
                }
                break;
            case R.id.add_tv:
                mNumber++;
                if (onNumberChangeListener != null) {
                    onNumberChangeListener.onNumberChange(mNumber);
                }
                break;
        }
    }


    private OnNumberChangeListener onNumberChangeListener;

    public void setOnNumberChangeListener(OnNumberChangeListener onNumberChangeListener) {
        this.onNumberChangeListener = onNumberChangeListener;
    }

    interface OnNumberChangeListener {
        void onNumberChange(int num);
    }

}
