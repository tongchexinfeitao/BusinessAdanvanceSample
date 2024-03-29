package com.example.myapplication.cart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CartActivity extends AppCompatActivity {

    @BindView(R.id.el_cart)
    ExpandableListView mElCart;
    @BindView(R.id.cb_cart_all_select)
    CheckBox mCbCartAllSelect;
    @BindView(R.id.tv_cart_total_price)
    TextView mTvCartTotalPrice;
    @BindView(R.id.btn_cart_pay)
    Button mBtnCartPay;
    //购物车接口的bean类
    private CartInfo mCartInfo;
    private CartAdapter mCartAdapter;
    //全选按钮的选中状态
    private boolean mIsAllSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);
        //联网请求，拿到json，进行解析
        mCartInfo = new Gson().fromJson(jsonData, CartInfo.class);
        if (mCartInfo != null && "0".equals(mCartInfo.getCode())) {
            List<CartInfo.DataBean> dataBeanList = mCartInfo.getData();
            mCartAdapter = new CartAdapter(dataBeanList);
            mCartAdapter.setOnCartStatusChangeListener(new CartAdapter.OnCartStatusChangeListener() {
                @Override
                public void onCartStatusChange(boolean isAllSelected, float totalPrice, int totalNum) {
                    refreshBottomStatus(isAllSelected, totalPrice, totalNum);
                }
            });
            mElCart.setAdapter(mCartAdapter);
            //刷新底部状态栏
            mCartAdapter.resetCartAllStatus();
            //展开商家子列表
            for (int i = 0; i < dataBeanList.size(); i++) {
                mElCart.expandGroup(i);
            }
        }

    }

    @OnClick({R.id.cb_cart_all_select, R.id.btn_cart_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cb_cart_all_select:
                //  2019/8/9 去获取之前的选中状态
                //  2019/8/9 改变所有的商品的状态
                // 2019/8/9 重新刷新底部状态栏
                mIsAllSelected = !mIsAllSelected;
                mCartAdapter.changeAllProductSelected(mIsAllSelected);
                mCartAdapter.resetCartAllStatus();
                break;
            case R.id.btn_cart_pay:
                Toast.makeText(CartActivity.this, "土豪", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //刷新底部状态栏
    private void refreshBottomStatus(boolean isAllSelected, float totalPrice, int totalNum) {
        mIsAllSelected = isAllSelected;
        mCbCartAllSelect.setChecked(mIsAllSelected);
        mTvCartTotalPrice.setText("总价￥ " + totalPrice);
        mBtnCartPay.setText("去结算(" + totalNum + ")");
    }

    private String jsonData = "{\n" +
            "\t\"msg\": \"请求成功\",\n" +
            "\t\"code\": 0,\n" +
            "\t\"data\": [{\n" +
            "\t\t\"list\": [{\n" +
            "\t\t\t\"bargainPrice\": 99,\n" +
            "\t\t\t\"createtime\": \"2017-10-14T21:38:26\",\n" +
            "\t\t\t\"detailUrl\": \"https://item.m.jd.com/product/4345173.html?utm#_source=androidapp&utm#_medium=appshare&utm#_campaign=t#_335139774&utm#_term=QQfriends\",\n" +
            "\t\t\t\"images\": \"https://m.360buyimg.com/n0/jfs/t6037/35/2944615848/95178/6cd6cff0/594a3a10Na4ec7f39.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t6607/258/1025744923/75738/da120a2d/594a3a12Ne3e6bc56.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t6370/292/1057025420/64655/f87644e3/594a3a12N5b900606.jpg!q70.jpg\",\n" +
            "\t\t\t\"num\": 1,\n" +
            "\t\t\t\"pid\": 45,\n" +
            "\t\t\t\"price\": 2999,\n" +
            "\t\t\t\"pscid\": 39,\n" +
            "\t\t\t\"selected\": 0,\n" +
            "\t\t\t\"sellerid\": 1,\n" +
            "\t\t\t\"subhead\": \"高清双摄，就是清晰！2000+1600万高清摄像头，6GB大内存+高通骁龙835处理器，性能怪兽！\",\n" +
            "\t\t\t\"title\": \"一加手机5 (A5000) 6GB+64GB 月岩灰 全网通 双卡双待 移动联通电信4G手机\"\n" +
            "\t\t}],\n" +
            "\t\t\"sellerName\": \"商家1\",\n" +
            "\t\t\"sellerid\": \"1\"\n" +
            "\t}, {\n" +
            "\t\t\"list\": [{\n" +
            "\t\t\t\"bargainPrice\": 6666,\n" +
            "\t\t\t\"createtime\": \"2017-10-10T16:01:31\",\n" +
            "\t\t\t\"detailUrl\": \"https://item.m.jd.com/product/5089273.html?utm#_source=androidapp&utm#_medium=appshare&utm#_campaign=t#_335139774&utm#_term=QQfriends\",\n" +
            "\t\t\t\"images\": \"https://m.360buyimg.com/n0/jfs/t8284/363/1326459580/71585/6d3e8013/59b857f2N6ca75622.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t9346/182/1406837243/282106/68af5b54/59b8480aNe8af7f5c.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t8434/54/1359766007/56140/579509d9/59b85801Nfea207db.jpg!q70.jpg\",\n" +
            "\t\t\t\"num\": 1,\n" +
            "\t\t\t\"pid\": 46,\n" +
            "\t\t\t\"price\": 234,\n" +
            "\t\t\t\"pscid\": 39,\n" +
            "\t\t\t\"selected\": 0,\n" +

            "\t\t\t\"sellerid\": 2,\n" +
            "\t\t\t\"subhead\": \"【iPhone新品上市】新一代iPhone，让智能看起来更不一样\",\n" +
            "\t\t\t\"title\": \"Apple iPhone 8 Plus (A1864) 64GB 金色 移动联通电信4G手机\"\n" +
            "\t\t}],\n" +
            "\t\t\"sellerName\": \"商家2\",\n" +
            "\t\t\"sellerid\": \"2\"\n" +
            "\t}, {\n" +
            "\t\t\"list\": [{\n" +
            "\t\t\t\"bargainPrice\": 399,\n" +
            "\t\t\t\"createtime\": \"2017-10-14T21:38:26\",\n" +
            "\t\t\t\"detailUrl\": \"https://item.m.jd.com/product/1439822107.html?utm_source=androidapp&utm_medium=appshare&utm_campaign=t_335139774&utm_term=QQfriends\",\n" +
            "\t\t\t\"images\": \"https://m.360buyimg.com/n0/jfs/t5887/201/859509257/69994/6bde9bf6/59224c24Ne854e14c.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t5641/233/853609022/57374/5c73d281/59224c24N3324d5f4.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t5641/233/853609022/57374/5c73d281/59224c24N3324d5f4.jpg!q70.jpg\",\n" +
            "\t\t\t\"num\": 1,\n" +
            "\t\t\t\"pid\": 82,\n" +
            "\t\t\t\"price\": 333,\n" +
            "\t\t\t\"pscid\": 85,\n" +
            "\t\t\t\"selected\": 0,\n" +
            "\t\t\t\"sellerid\": 3,\n" +
            "\t\t\t\"subhead\": \"满2件，总价打6.50折\",\n" +
            "\t\t\t\"title\": \"Gap男装 休闲舒适简约水洗五袋直筒长裤紧身牛仔裤941825 深灰色 33/32(175/84A)\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"bargainPrice\": 1599,\n" +
            "\t\t\t\"createtime\": \"2017-10-14T21:48:08\",\n" +
            "\t\t\t\"detailUrl\": \"https://item.m.jd.com/product/1993026402.html?utm#_source=androidapp&utm#_medium=appshare&utm#_campaign=t#_335139774&utm#_term=QQfriends\",\n" +
            "\t\t\t\"images\": \"https://m.360buyimg.com/n0/jfs/t5863/302/8961270302/97126/41feade1/5981c81cNc1b1fbef.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t7003/250/1488538438/195825/53bf31ba/5981c57eN51e95176.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t5665/100/8954482513/43454/418611a9/5981c57eNd5fc97ba.jpg!q70.jpg\",\n" +
            "\t\t\t\"num\": 1,\n" +
            "\t\t\t\"pid\": 47,\n" +
            "\t\t\t\"price\": 111,\n" +
            "\t\t\t\"pscid\": 39,\n" +
            "\t\t\t\"selected\": 0,\n" +
            "\t\t\t\"sellerid\": 3,\n" +
            "\t\t\t\"subhead\": \"碳黑色 32GB 全网通 官方标配   1件\",\n" +
            "\t\t\t\"title\": \"锤子 坚果Pro 特别版 巧克力色 酒红色 全网通 移动联通电信4G手机 双卡双待 碳黑色 32GB 全网通\"\n" +
            "\t\t}],\n" +
            "\t\t\"sellerName\": \"商家3\",\n" +
            "\t\t\"sellerid\": \"3\"\n" +
            "\t}, {\n" +
            "\t\t\"list\": [{\n" +
            "\t\t\t\"bargainPrice\": 399,\n" +
            "\t\t\t\"createtime\": \"2017-10-14T21:38:26\",\n" +
            "\t\t\t\"detailUrl\": \"https://item.m.jd.com/product/1439822107.html?utm_source=androidapp&utm_medium=appshare&utm_campaign=t_335139774&utm_term=QQfriends\",\n" +
            "\t\t\t\"images\": \"https://m.360buyimg.com/n0/jfs/t5887/201/859509257/69994/6bde9bf6/59224c24Ne854e14c.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t5641/233/853609022/57374/5c73d281/59224c24N3324d5f4.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t5641/233/853609022/57374/5c73d281/59224c24N3324d5f4.jpg!q70.jpg\",\n" +
            "\t\t\t\"num\": 1,\n" +
            "\t\t\t\"pid\": 83,\n" +
            "\t\t\t\"price\": 444,\n" +
            "\t\t\t\"pscid\": 85,\n" +
            "\t\t\t\"selected\": 0,\n" +
            "\t\t\t\"sellerid\": 4,\n" +
            "\t\t\t\"subhead\": \"满2件，总价打6.50折\",\n" +
            "\t\t\t\"title\": \"Gap男装 休闲舒适简约水洗五袋直筒长裤紧身牛仔裤941825 深灰色 33/32(175/84A)\"\n" +
            "\t\t}],\n" +
            "\t\t\"sellerName\": \"商家4\",\n" +
            "\t\t\"sellerid\": \"4\"\n" +
            "\t}, {\n" +
            "\t\t\"list\": [{\n" +
            "\t\t\t\"bargainPrice\": 399,\n" +
            "\t\t\t\"createtime\": \"2017-10-03T23:53:28\",\n" +
            "\t\t\t\"detailUrl\": \"https://item.m.jd.com/product/1439822107.html?utm_source=androidapp&utm_medium=appshare&utm_campaign=t_335139774&utm_term=QQfriends\",\n" +
            "\t\t\t\"images\": \"https://m.360buyimg.com/n0/jfs/t5887/201/859509257/69994/6bde9bf6/59224c24Ne854e14c.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t5641/233/853609022/57374/5c73d281/59224c24N3324d5f4.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t5641/233/853609022/57374/5c73d281/59224c24N3324d5f4.jpg!q70.jpg\",\n" +
            "\t\t\t\"num\": 1,\n" +
            "\t\t\t\"pid\": 84,\n" +
            "\t\t\t\"price\": 555,\n" +
            "\t\t\t\"pscid\": 85,\n" +
            "\t\t\t\"selected\": 0,\n" +
            "\t\t\t\"sellerid\": 5,\n" +
            "\t\t\t\"subhead\": \"满2件，总价打6.50折\",\n" +
            "\t\t\t\"title\": \"Gap男装 休闲舒适简约水洗五袋直筒长裤紧身牛仔裤941825 深灰色 33/32(175/84A)\"\n" +
            "\t\t}],\n" +
            "\t\t\"sellerName\": \"商家5\",\n" +
            "\t\t\"sellerid\": \"5\"\n" +
            "\t}, {\n" +
            "\t\t\"list\": [{\n" +
            "\t\t\t\"bargainPrice\": 399,\n" +
            "\t\t\t\"createtime\": \"2017-10-14T21:38:26\",\n" +
            "\t\t\t\"detailUrl\": \"https://item.m.jd.com/product/1439822107.html?utm_source=androidapp&utm_medium=appshare&utm_campaign=t_335139774&utm_term=QQfriends\",\n" +
            "\t\t\t\"images\": \"https://m.360buyimg.com/n0/jfs/t5887/201/859509257/69994/6bde9bf6/59224c24Ne854e14c.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t5641/233/853609022/57374/5c73d281/59224c24N3324d5f4.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t5641/233/853609022/57374/5c73d281/59224c24N3324d5f4.jpg!q70.jpg\",\n" +
            "\t\t\t\"num\": 1,\n" +
            "\t\t\t\"pid\": 85,\n" +
            "\t\t\t\"price\": 666,\n" +
            "\t\t\t\"pscid\": 85,\n" +
            "\t\t\t\"selected\": 0,\n" +
            "\t\t\t\"sellerid\": 6,\n" +
            "\t\t\t\"subhead\": \"满2件，总价打6.50折\",\n" +
            "\t\t\t\"title\": \"Gap男装 休闲舒适简约水洗五袋直筒长裤紧身牛仔裤941825 深灰色 33/32(175/84A)\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"bargainPrice\": 11800,\n" +
            "\t\t\t\"createtime\": \"2017-10-03T23:53:28\",\n" +
            "\t\t\t\"detailUrl\": \"https://mitem.jd.hk/ware/view.action?wareId=1988853309&cachekey=1acb07a701ece8d2434a6ae7fa6870a1\",\n" +
            "\t\t\t\"images\": \"https://m.360buyimg.com/n0/jfs/t6130/97/1370670410/180682/1109582a/593276b1Nd81fe723.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t5698/110/2617517836/202970/c9388feb/593276b7Nbd94ef1f.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t5698/110/2617517836/202970/c9388feb/593276b7Nbd94ef1f.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t5815/178/2614671118/51656/7f52d137/593276c7N107b725a.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t5878/60/2557817477/30873/4502b606/593276caN5a7d6357.jpg!q70.jpg\",\n" +
            "\t\t\t\"num\": 1,\n" +
            "\t\t\t\"pid\": 62,\n" +
            "\t\t\t\"price\": 15999,\n" +
            "\t\t\t\"pscid\": 40,\n" +
            "\t\t\t\"selected\": 0,\n" +
            "\t\t\t\"sellerid\": 6,\n" +
            "\t\t\t\"subhead\": \"购买电脑办公部分商品满1元返火车票5元优惠券（返完即止）\",\n" +
            "\t\t\t\"title\": \"全球购 新款Apple MacBook Pro 苹果笔记本电脑 银色VP2新13英寸Bar i5/8G/256G\"\n" +
            "\t\t}],\n" +
            "\t\t\"sellerName\": \"商家6\",\n" +
            "\t\t\"sellerid\": \"6\"\n" +
            "\t}, {\n" +
            "\t\t\"list\": [{\n" +
            "\t\t\t\"bargainPrice\": 399,\n" +
            "\t\t\t\"createtime\": \"2017-10-03T23:53:28\",\n" +
            "\t\t\t\"detailUrl\": \"https://item.m.jd.com/product/1439822107.html?utm_source=androidapp&utm_medium=appshare&utm_campaign=t_335139774&utm_term=QQfriends\",\n" +
            "\t\t\t\"images\": \"https://m.360buyimg.com/n0/jfs/t5887/201/859509257/69994/6bde9bf6/59224c24Ne854e14c.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t5641/233/853609022/57374/5c73d281/59224c24N3324d5f4.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t5641/233/853609022/57374/5c73d281/59224c24N3324d5f4.jpg!q70.jpg\",\n" +
            "\t\t\t\"num\": 1,\n" +
            "\t\t\t\"pid\": 86,\n" +
            "\t\t\t\"price\": 777,\n" +
            "\t\t\t\"pscid\": 85,\n" +
            "\t\t\t\"selected\": 0,\n" +
            "\t\t\t\"sellerid\": 7,\n" +
            "\t\t\t\"subhead\": \"满2件，总价打6.50折\",\n" +
            "\t\t\t\"title\": \"Gap男装 休闲舒适简约水洗五袋直筒长裤紧身牛仔裤941825 深灰色 33/32(175/84A)\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"bargainPrice\": 22.9,\n" +
            "\t\t\t\"createtime\": \"2017-10-14T21:38:26\",\n" +
            "\t\t\t\"detailUrl\": \"https://item.m.jd.com/product/2542855.html?utm_source=androidapp&utm_medium=appshare&utm_campaign=t_335139774&utm_term=QQfriends\",\n" +
            "\t\t\t\"images\": \"https://m.360buyimg.com/n0/jfs/t1930/284/2865629620/390243/e3ade9c4/56f0a08fNbd3a1235.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t2137/336/2802996626/155915/e5e90d7a/56f0a09cN33e01bd0.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t1882/31/2772215910/389956/c8dbf370/56f0a0a2Na0c86ea6.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t2620/166/2703833710/312660/531aa913/57709035N33857877.jpg!q70.jpg\",\n" +
            "\t\t\t\"num\": 1,\n" +
            "\t\t\t\"pid\": 30,\n" +
            "\t\t\t\"price\": 688,\n" +
            "\t\t\t\"pscid\": 2,\n" +
            "\t\t\t\"selected\": 0,\n" +
            "\t\t\t\"sellerid\": 7,\n" +
            "\t\t\t\"subhead\": \"三只松鼠零食特惠，专区满99减50，满199减100，火速抢购》\",\n" +
            "\t\t\t\"title\": \"三只松鼠 坚果炒货 零食奶油味 碧根果225g/袋\"\n" +
            "\t\t}],\n" +
            "\t\t\"sellerName\": \"商家7\",\n" +
            "\t\t\"sellerid\": \"7\"\n" +
            "\t}, {\n" +
            "\t\t\"list\": [{\n" +
            "\t\t\t\"bargainPrice\": 399,\n" +
            "\t\t\t\"createtime\": \"2017-10-03T23:53:28\",\n" +
            "\t\t\t\"detailUrl\": \"https://item.m.jd.com/product/1439822107.html?utm_source=androidapp&utm_medium=appshare&utm_campaign=t_335139774&utm_term=QQfriends\",\n" +
            "\t\t\t\"images\": \"https://m.360buyimg.com/n0/jfs/t5887/201/859509257/69994/6bde9bf6/59224c24Ne854e14c.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t5641/233/853609022/57374/5c73d281/59224c24N3324d5f4.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t5641/233/853609022/57374/5c73d281/59224c24N3324d5f4.jpg!q70.jpg\",\n" +
            "\t\t\t\"num\": 1,\n" +
            "\t\t\t\"pid\": 87,\n" +
            "\t\t\t\"price\": 888,\n" +
            "\t\t\t\"pscid\": 85,\n" +
            "\t\t\t\"selected\": 0,\n" +
            "\t\t\t\"sellerid\": 8,\n" +
            "\t\t\t\"subhead\": \"满2件，总价打6.50折\",\n" +
            "\t\t\t\"title\": \"Gap男装 休闲舒适简约水洗五袋直筒长裤紧身牛仔裤941825 深灰色 33/32(175/84A)\"\n" +
            "\t\t}],\n" +
            "\t\t\"sellerName\": \"商家8\",\n" +
            "\t\t\"sellerid\": \"8\"\n" +
            "\t}, {\n" +
            "\t\t\"list\": [{\n" +
            "\t\t\t\"bargainPrice\": 399,\n" +
            "\t\t\t\"createtime\": \"2017-10-14T21:38:26\",\n" +
            "\t\t\t\"detailUrl\": \"https://item.m.jd.com/product/1439822107.html?utm_source=androidapp&utm_medium=appshare&utm_campaign=t_335139774&utm_term=QQfriends\",\n" +
            "\t\t\t\"images\": \"https://m.360buyimg.com/n0/jfs/t5887/201/859509257/69994/6bde9bf6/59224c24Ne854e14c.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t5641/233/853609022/57374/5c73d281/59224c24N3324d5f4.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t5641/233/853609022/57374/5c73d281/59224c24N3324d5f4.jpg!q70.jpg\",\n" +
            "\t\t\t\"num\": 2,\n" +
            "\t\t\t\"pid\": 88,\n" +
            "\t\t\t\"price\": 999,\n" +
            "\t\t\t\"pscid\": 85,\n" +
            "\t\t\t\"selected\": 0,\n" +
            "\t\t\t\"sellerid\": 9,\n" +
            "\t\t\t\"subhead\": \"满2件，总价打6.50折\",\n" +
            "\t\t\t\"title\": \"Gap男装 休闲舒适简约水洗五袋直筒长裤紧身牛仔裤941825 深灰色 33/32(175/84A)\"\n" +
            "\t\t}],\n" +
            "\t\t\"sellerName\": \"商家9\",\n" +
            "\t\t\"sellerid\": \"9\"\n" +
            "\t}, {\n" +
            "\t\t\"list\": [{\n" +
            "\t\t\t\"bargainPrice\": 22.9,\n" +
            "\t\t\t\"createtime\": \"2017-10-03T23:43:53\",\n" +
            "\t\t\t\"detailUrl\": \"https://item.m.jd.com/product/2542855.html?utm_source=androidapp&utm_medium=appshare&utm_campaign=t_335139774&utm_term=QQfriends\",\n" +
            "\t\t\t\"images\": \"https://m.360buyimg.com/n0/jfs/t1930/284/2865629620/390243/e3ade9c4/56f0a08fNbd3a1235.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t2137/336/2802996626/155915/e5e90d7a/56f0a09cN33e01bd0.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t1882/31/2772215910/389956/c8dbf370/56f0a0a2Na0c86ea6.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t2620/166/2703833710/312660/531aa913/57709035N33857877.jpg!q70.jpg\",\n" +
            "\t\t\t\"num\": 1,\n" +
            "\t\t\t\"pid\": 33,\n" +
            "\t\t\t\"price\": 988,\n" +
            "\t\t\t\"pscid\": 2,\n" +
            "\t\t\t\"selected\": 0,\n" +
            "\t\t\t\"sellerid\": 10,\n" +
            "\t\t\t\"subhead\": \"三只松鼠零食特惠，专区满99减50，满199减100，火速抢购》\",\n" +
            "\t\t\t\"title\": \"三只松鼠 坚果炒货 零食奶油味 碧根果225g/袋\"\n" +
            "\t\t}],\n" +
            "\t\t\"sellerName\": \"商家10\",\n" +
            "\t\t\"sellerid\": \"10\"\n" +
            "\t}, {\n" +
            "\t\t\"list\": [{\n" +
            "\t\t\t\"bargainPrice\": 3455,\n" +
            "\t\t\t\"createtime\": \"2017-10-14T21:38:26\",\n" +
            "\t\t\t\"detailUrl\": \"https://item.m.jd.com/product/12224420750.html?utm_source=androidapp&utm_medium=appshare&utm_campaign=t_335139774&utm_term=QQfriends\",\n" +
            "\t\t\t\"images\": \"https://m.360buyimg.com/n0/jfs/t9106/106/1785172479/537280/253bc0ab/59bf78a7N057e5ff7.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t9106/106/1785172479/537280/253bc0ab/59bf78a7N057e5ff7.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t8461/5/1492479653/68388/7255e013/59ba5e84N91091843.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t8461/5/1492479653/68388/7255e013/59ba5e84N91091843.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t8803/356/1478945529/489755/2a163ace/59ba5e84N7bb9a666.jpg!q70.jpg\",\n" +
            "\t\t\t\"num\": 10,\n" +
            "\t\t\t\"pid\": 56,\n" +
            "\t\t\t\"price\": 99,\n" +
            "\t\t\t\"pscid\": 39,\n" +
            "\t\t\t\"selected\": 0,\n" +
            "\t\t\t\"sellerid\": 12,\n" +
            "\t\t\t\"subhead\": \"【现货新品抢购】全面屏2.0震撼来袭，骁龙835处理器，四曲面陶瓷机\",\n" +
            "\t\t\t\"title\": \"小米（MI） 小米MIX2 手机 黑色 全网通 (6GB+64GB)【标配版】\"\n" +
            "\t\t}],\n" +
            "\t\t\"sellerName\": \"商家12\",\n" +
            "\t\t\"sellerid\": \"12\"\n" +
            "\t}, {\n" +
            "\t\t\"list\": [{\n" +
            "\t\t\t\"bargainPrice\": 111.99,\n" +
            "\t\t\t\"createtime\": \"2017-10-14T21:39:05\",\n" +
            "\t\t\t\"detailUrl\": \"https://item.m.jd.com/product/4719303.html?utm_source=androidapp&utm_medium=appshare&utm_campaign=t_335139774&utm_term=QQfriends\",\n" +
            "\t\t\t\"images\": \"https://m.360buyimg.com/n0/jfs/t9004/210/1160833155/647627/ad6be059/59b4f4e1N9a2b1532.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t7504/338/63721388/491286/f5957f53/598e95f1N7f2adb87.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t7441/10/64242474/419246/adb30a7d/598e95fbNd989ba0a.jpg!q70.jpg\",\n" +
            "\t\t\t\"num\": 1,\n" +
            "\t\t\t\"pid\": 1,\n" +
            "\t\t\t\"price\": 118,\n" +
            "\t\t\t\"pscid\": 1,\n" +
            "\t\t\t\"selected\": 0,\n" +
            "\t\t\t\"sellerid\": 17,\n" +
            "\t\t\t\"subhead\": \"每个中秋都不能简单，无论身在何处，你总需要一块饼让生活更圆满，京东月饼让爱更圆满京东自营，闪电配送，更多惊喜，快用手指戳一下\",\n" +
            "\t\t\t\"title\": \"北京稻香村 稻香村中秋节月饼 老北京月饼礼盒655g\"\n" +
            "\t\t}],\n" +
            "\t\t\"sellerName\": \"商家17\",\n" +
            "\t\t\"sellerid\": \"17\"\n" +
            "\t}, {\n" +
            "\t\t\"list\": [{\n" +
            "\t\t\t\"bargainPrice\": 111.99,\n" +
            "\t\t\t\"createtime\": \"2017-10-14T21:39:05\",\n" +
            "\t\t\t\"detailUrl\": \"https://item.m.jd.com/product/4719303.html?utm_source=androidapp&utm_medium=appshare&utm_campaign=t_335139774&utm_term=QQfriends\",\n" +
            "\t\t\t\"images\": \"https://m.360buyimg.com/n0/jfs/t9004/210/1160833155/647627/ad6be059/59b4f4e1N9a2b1532.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t7504/338/63721388/491286/f5957f53/598e95f1N7f2adb87.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t7441/10/64242474/419246/adb30a7d/598e95fbNd989ba0a.jpg!q70.jpg\",\n" +
            "\t\t\t\"num\": 4,\n" +
            "\t\t\t\"pid\": 2,\n" +
            "\t\t\t\"price\": 299,\n" +
            "\t\t\t\"pscid\": 1,\n" +
            "\t\t\t\"selected\": 0,\n" +
            "\t\t\t\"sellerid\": 18,\n" +
            "\t\t\t\"subhead\": \"每个中秋都不能简单，无论身在何处，你总需要一块饼让生活更圆满，京东月饼让爱更圆满京东自营，闪电配送，更多惊喜，快用手指戳一下\",\n" +
            "\t\t\t\"title\": \"北京稻香村 稻香村中秋节月饼 老北京月饼礼盒655g\"\n" +
            "\t\t}],\n" +
            "\t\t\"sellerName\": \"商家18\",\n" +
            "\t\t\"sellerid\": \"18\"\n" +
            "\t}, {\n" +
            "\t\t\"list\": [{\n" +
            "\t\t\t\"bargainPrice\": 111.99,\n" +
            "\t\t\t\"createtime\": \"2017-10-03T23:53:28\",\n" +
            "\t\t\t\"detailUrl\": \"https://item.m.jd.com/product/4719303.html?utm_source=androidapp&utm_medium=appshare&utm_campaign=t_335139774&utm_term=QQfriends\",\n" +
            "\t\t\t\"images\": \"https://m.360buyimg.com/n0/jfs/t9004/210/1160833155/647627/ad6be059/59b4f4e1N9a2b1532.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t7504/338/63721388/491286/f5957f53/598e95f1N7f2adb87.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t7441/10/64242474/419246/adb30a7d/598e95fbNd989ba0a.jpg!q70.jpg\",\n" +
            "\t\t\t\"num\": 1,\n" +
            "\t\t\t\"pid\": 3,\n" +
            "\t\t\t\"price\": 198,\n" +
            "\t\t\t\"pscid\": 1,\n" +
            "\t\t\t\"selected\": 0,\n" +
            "\t\t\t\"sellerid\": 19,\n" +
            "\t\t\t\"subhead\": \"每个中秋都不能简单，无论身在何处，你总需要一块饼让生活更圆满，京东月饼让爱更圆满京东自营，闪电配送，更多惊喜，快用手指戳一下\",\n" +
            "\t\t\t\"title\": \"北京稻香村 稻香村中秋节月饼 老北京月饼礼盒655g\"\n" +
            "\t\t}],\n" +
            "\t\t\"sellerName\": \"商家19\",\n" +
            "\t\t\"sellerid\": \"19\"\n" +
            "\t}, {\n" +
            "\t\t\"list\": [{\n" +
            "\t\t\t\"bargainPrice\": 111.99,\n" +
            "\t\t\t\"createtime\": \"2017-10-03T23:53:28\",\n" +
            "\t\t\t\"detailUrl\": \"https://item.m.jd.com/product/4719303.html?utm_source=androidapp&utm_medium=appshare&utm_campaign=t_335139774&utm_term=QQfriends\",\n" +
            "\t\t\t\"images\": \"https://m.360buyimg.com/n0/jfs/t9004/210/1160833155/647627/ad6be059/59b4f4e1N9a2b1532.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t7504/338/63721388/491286/f5957f53/598e95f1N7f2adb87.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t7441/10/64242474/419246/adb30a7d/598e95fbNd989ba0a.jpg!q70.jpg\",\n" +
            "\t\t\t\"num\": 1,\n" +
            "\t\t\t\"pid\": 6,\n" +
            "\t\t\t\"price\": 7.99,\n" +
            "\t\t\t\"pscid\": 1,\n" +
            "\t\t\t\"selected\": 1,\n" +
            "\t\t\t\"sellerid\": 22,\n" +
            "\t\t\t\"subhead\": \"每个中秋都不能简单，无论身在何处，你总需要一块饼让生活更圆满，京东月饼让爱更圆满京东自营，闪电配送，更多惊喜，快用手指戳一下\",\n" +
            "\t\t\t\"title\": \"北京稻香村 稻香村中秋节月饼 老北京月饼礼盒655g\"\n" +
            "\t\t}],\n" +
            "\t\t\"sellerName\": \"商家22\",\n" +
            "\t\t\"sellerid\": \"22\"\n" +
            "\t}]\n" +
            "}";

}
