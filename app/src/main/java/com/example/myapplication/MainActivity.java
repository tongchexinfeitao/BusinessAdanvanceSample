package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.edtTxt_main_inputContent)
    EditText mEdtTxtMainInputContent;
    @BindView(R.id.iv_main_QRCode)
    ImageView mIvMainQRCode;

    private Unbinder unbinder;

    //多个Disposable容器，可以使用他集体释放
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
    }


    @OnClick({R.id.btn_main_scan, R.id.btn_main_openImg, R.id.btn_main_creatQRCode, R.id.btn_main_creatQRCodeWithIcon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_scan:
                //拿到单例
                RetrofitManager.getInstance()
                        //构造一个IApi对象
                        .create(IApi.class)
                        //登录
                        .login("849616168@qq.com", "uNzol1eD+fxXRt/ALcYcdw==")
                        //设置联网请求的线程状态
                        .subscribeOn(Schedulers.io())
                        .filter(new Predicate<LoginBean>() {
                            @Override
                            public boolean test(LoginBean loginBean) throws Exception {
                                return "0000".equals(loginBean.getStatus());
                            }
                        })
                        .map(new Function<LoginBean, LoginBean.ResultBean>() {
                            @Override
                            public LoginBean.ResultBean apply(LoginBean loginBean) throws Exception {
                                return loginBean.getResult();
                            }
                        })
                        .flatMap(new Function<LoginBean.ResultBean, ObservableSource<UploadPhotoBean>>() {
                            @Override
                            public ObservableSource<UploadPhotoBean> apply(LoginBean.ResultBean resultBean) throws Exception {
                                MediaType mediaType = MediaType.parse("image/*");
                                File file = getFile();
                                String fileName = file.getName();
                                RequestBody requestBody = RequestBody.create(mediaType, file);
                                MultipartBody.Part part = MultipartBody.Part.createFormData("image", fileName, requestBody);
                                return RetrofitManager.getInstance()
                                        .create(IApi.class)
                                        .uploadPhoto("http://172.17.8.100/movieApi/user/v1/verify/uploadHeadPic",
                                                "13415",
                                                "156516614084813415", part);
                            }
                        })
                        //切换接受回调结果的线程状态
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<UploadPhotoBean>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                mCompositeDisposable.add(d);
                                Log.e("TAG", "onSubscribe");
                            }

                            @Override
                            public void onNext(UploadPhotoBean uploadPhotoBean) {
                                Log.e("TAG", "onNext" + uploadPhotoBean.toString());
                                Toast.makeText(MainActivity.this, "onNext", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("TAG", "onError " + e.toString());
                            }

                            @Override
                            public void onComplete() {
                                Log.e("TAG", "onComplete");
                            }
                        });

                break;
            case R.id.btn_main_openImg:
                //create方法构造一个Api接口对象

//                Observable.interval(2, 2, TimeUnit.SECONDS)
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Observer<Long>() {
//                            @Override
//                            public void onSubscribe(Disposable d) {
//                                mCompositeDisposable.add(d);
//                                Log.e("TAG", "onSubscribe");
//                            }
//
//                            @Override
//                            public void onNext(Long tem) {
//                                Log.e("TAG", "onNext" + tem);
//                                Toast.makeText(MainActivity.this, "onNext", Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                Log.e("TAG", "onError " + e.toString());
//                            }
//
//                            @Override
//                            public void onComplete() {
//                                Log.e("TAG", "onComplete");
//                            }
//                        });

                Observable.range(5, 4)
                        .take(1)
                        .subscribe(new Observer<Integer>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Integer integer) {
                                Log.e("TAG", "onNext" + integer.intValue());
                                Toast.makeText(MainActivity.this, "onNext" + integer.intValue(), Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });

                break;
            case R.id.btn_main_creatQRCode:
                Bitmap mBitmap = CodeUtils.createImage("我是二维码", 200, 200, null);
                mIvMainQRCode.setImageBitmap(mBitmap);
                break;
            case R.id.btn_main_creatQRCodeWithIcon:

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        //释放所有已经添加的Disposable
        mCompositeDisposable.clear();
    }


    @OnLongClick(R.id.iv_main_QRCode)
    public boolean longClickRecognition(View view) {
        switch (view.getId()) {
            case R.id.iv_main_QRCode:
                ImageView imageView = (ImageView) view;
                BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                try {
                    File file = new File(getCacheDir(), "abc.jpg");
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
                    CodeUtils.analyzeBitmap(file.getAbsolutePath(), new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                            Log.i("TAG", result);
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            Log.i("TAG", "shibai");
                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }
        return true;

    }


    public File getFile() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        String defaultPath = getApplicationContext().getFilesDir()
                .getAbsolutePath() + "/defaultGoodInfo";
        File file = new File(defaultPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String defaultImgPath = defaultPath + "/messageImg.jpg";
        file = new File(defaultImgPath);
        try {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 20, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
}
