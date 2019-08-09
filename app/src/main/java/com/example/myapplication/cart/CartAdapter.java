package com.example.myapplication.cart;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartAdapter extends BaseExpandableListAdapter {


    private List<CartInfo.DataBean> mDataBeanList;

    public CartAdapter(List<CartInfo.DataBean> dataBeanList) {
        this.mDataBeanList = dataBeanList;
    }

    //商家的数量
    @Override
    public int getGroupCount() {
        return mDataBeanList == null ? 0 : mDataBeanList.size();
    }

    //第groupPosition个商家中的商品数量
    @Override
    public int getChildrenCount(int groupPosition) {
        return mDataBeanList.get(groupPosition).getList() == null ? 0 : mDataBeanList.get(groupPosition).getList().size();
    }


    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ParentViewHolder parentViewHolder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_cart_parent, null);
            parentViewHolder = new ParentViewHolder(convertView);
            convertView.setTag(parentViewHolder);
        } else {
            parentViewHolder = (ParentViewHolder) convertView.getTag();
        }
        //当前商家的bean类
        CartInfo.DataBean dataBean = mDataBeanList.get(groupPosition);
        //设置商家名字
        parentViewHolder.mSellerNameTv.setText(dataBean.getSellerName());

        //计算当前商家下的商品是否全部是选中状态
        final boolean currentSellerAllProductSelected = isCurrentSellerAllProductSelected(groupPosition);
        //设置商家checkbox的状态
        parentViewHolder.mSellerCb.setChecked(currentSellerAllProductSelected);
        //商家的checkbox
        parentViewHolder.mSellerCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentSellerAllProductStatus(groupPosition, !currentSellerAllProductSelected);
                notifyDataSetChanged();
                resetCartAllStatus();
                // 2019/8/9  修改当前商家下的所有商品的选中状态
                //  2019/8/9  刷新适配器 == 修改商家自己的状态
                // 2019/8/9 刷新底部状态栏

            }
        });
        return convertView;
    }

    //设置当前商家所有商品的选中状态
    private void setCurrentSellerAllProductStatus(int groupPosition, boolean selected) {
        //当前商家的bean类
        CartInfo.DataBean dataBean = mDataBeanList.get(groupPosition);
        //遍历当前商家所有的商品
        for (int i = 0; i < dataBean.getList().size(); i++) {
            CartInfo.DataBean.ListBean listBean = dataBean.getList().get(i);
            //设置当前商家所有商品的状态
            listBean.setSelected(selected ? 1 : 0);
        }
    }

    //设置当前商家所有商品的选中状态
    private void setCurrentProductNumber(int groupPosition, int childPosition, int num) {
        //当前商家的bean类
        CartInfo.DataBean dataBean = mDataBeanList.get(groupPosition);
        //设置当前商品的数量
        dataBean.getList().get(childPosition).setNum(num);
    }


    //商品的布局
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_cart_child, null);
            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        //商品的bean类
        final CartInfo.DataBean.ListBean listBean = mDataBeanList.get(groupPosition).getList().get(childPosition);
        //商品的标题
        childViewHolder.mProductTitleNameTv.setText(listBean.getTitle());
        //商品的单价
        childViewHolder.mProductPriceTv.setText("" + listBean.getBargainPrice());
        //设置加减器的数量
        childViewHolder.mAddRemoveView.setNumber(listBean.getNum());
        //设置加减器的数量改变监听
        childViewHolder.mAddRemoveView.setOnNumberChangeListener(new MyAddSubView.OnNumberChangeListener() {
            @Override
            public void onNumberChange(int num) {
                setCurrentProductNumber(groupPosition, childPosition, num);
                notifyDataSetChanged();
                resetCartAllStatus();
                //  2019/8/9 改变当前商品bean类的数量
                // 2019/8/9 刷新适配器
                //  2019/8/9 刷新底部状态栏
            }
        });
        // TODO: 2019/8/9  设置加减器的数量
        childViewHolder.mChildCb.setChecked(listBean.getSelected() == 1);
        childViewHolder.mChildCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listBean.setSelected(listBean.getSelected() == 1 ? 0 : 1);
                notifyDataSetChanged();
                resetCartAllStatus();
                // T2019/8/9 刷新底部状态栏
            }
        });
        return convertView;
    }

    //计算当前商家下的所有商品的选中状态，只要有一个商品未选中，那么商家状态就是未选中
    private boolean isCurrentSellerAllProductSelected(int position) {
        //拿到当前商家的数据
        CartInfo.DataBean dataBean = mDataBeanList.get(position);
        //拿到当前商家中所有商品的数据
        List<CartInfo.DataBean.ListBean> dataBeanList = dataBean.getList();
        for (int i = 0; i < dataBeanList.size(); i++) {
            //只要有一个商品是未选中状态，那么直接商家就是未选中状态
            //1是true是选中状态， 0是false是未选中状态
            if (dataBeanList.get(i).getSelected() == 0) {
                return false;
            }
        }
        //默认是返回true
        return true;
    }


    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    //设置所有的商品的选中状态
    public void changeAllProductSelected(boolean mIsAllSelected) {
        //所有的商家
        for (int i = 0; i < mDataBeanList.size(); i++) {
            CartInfo.DataBean dataBean = mDataBeanList.get(i);
            //所有的商品
            List<CartInfo.DataBean.ListBean> dataBeanList = dataBean.getList();
            //遍历所有的商品
            for (int j = 0; j < dataBeanList.size(); j++) {
                CartInfo.DataBean.ListBean listBean = dataBeanList.get(j);
                //设置当前商品的选中状态
                listBean.setSelected(mIsAllSelected ? 1 : 0);
            }
        }
        notifyDataSetChanged();

        // 2019/8/9 遍历所有商家
        //  2019/8/9 遍历商家下的所有商品
        //  2019/8/9 修改当前商家的选中状态
        // 2019/8/9 刷新适配器

    }

    static class ParentViewHolder {
        @BindView(R.id.seller_cb)
        CheckBox mSellerCb;
        @BindView(R.id.seller_name_tv)
        TextView mSellerNameTv;

        ParentViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static
    class ChildViewHolder {
        @BindView(R.id.child_cb)
        CheckBox mChildCb;
        @BindView(R.id.product_icon_iv)
        ImageView mProductIconIv;
        @BindView(R.id.product_title_name_tv)
        TextView mProductTitleNameTv;
        @BindView(R.id.product_price_tv)
        TextView mProductPriceTv;
        @BindView(R.id.add_remove_view)
        MyAddSubView mAddRemoveView;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    //重新计算购物车的选中状态和总价和总数量
    public void resetCartAllStatus() {
        boolean isAllSelected = true;
        float totalPrice = 0;
        int totalNum = 0;
        //所有的商家
        for (int i = 0; i < mDataBeanList.size(); i++) {
            CartInfo.DataBean dataBean = mDataBeanList.get(i);
            //所有的商品
            List<CartInfo.DataBean.ListBean> dataBeanList = dataBean.getList();
            //遍历所有的商品
            for (int j = 0; j < dataBeanList.size(); j++) {
                CartInfo.DataBean.ListBean listBean = dataBeanList.get(j);
                //如果有未选中的
                if (listBean.getSelected() == 0) {
                    isAllSelected = false;
                } else {
                    totalPrice += listBean.getBargainPrice() * listBean.getNum();
                    totalNum += listBean.getNum();
                }

            }
        }
        if (onCartStatusChangeListener != null) {
            onCartStatusChangeListener.onCartStatusChange(isAllSelected, totalPrice, totalNum);
        }
        //  2019/8/9 计算是否是全选状态
        //  2019/8/9 计算总价
        //  2019/8/9 计算总数量
        //  2019/8/9 通知外界刷新底部状态栏
    }

    OnCartStatusChangeListener onCartStatusChangeListener;

    public void setOnCartStatusChangeListener(OnCartStatusChangeListener onCartStatusChangeListener) {
        this.onCartStatusChangeListener = onCartStatusChangeListener;
    }

    interface OnCartStatusChangeListener {
        void onCartStatusChange(boolean isAllSelected, float totalPrice, int totalNum);
    }

}
