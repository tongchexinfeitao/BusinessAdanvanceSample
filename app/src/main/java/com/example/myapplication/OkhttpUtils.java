package com.example.myapplication;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkhttpUtils {
    private static OkhttpUtils mOkhttpUtils = null;

    private final OkHttpClient mOkHttpClient;
    private Handler mHandler = new Handler(Looper.myLooper());

    private OkhttpUtils() {
        //日志拦截器
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        //设置日志拦截器打印日志的级别
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //构造一个OkHttpClient对应
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }


    public static OkhttpUtils getInstance() {
        if (mOkhttpUtils == null) {
            synchronized (OkhttpUtils.class) {
                if (mOkhttpUtils == null) {
                    mOkhttpUtils = new OkhttpUtils();
                }
            }
        }
        return mOkhttpUtils;
    }


    public void doGet(String url, final IOkhttpCallBack iOkhttpCallBack) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        //根据请求Request构造一个http请求
        final Call call = mOkHttpClient.newCall(request);
        //执行异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (iOkhttpCallBack != null) {
                            iOkhttpCallBack.onFailure(e);
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    //通过获取响应体之后，通过string方法获取json字符串
                    final String json = response.body().string();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (iOkhttpCallBack != null) {
                                iOkhttpCallBack.onSuccess(json);
                            }
                        }
                    });
                    return;
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (iOkhttpCallBack != null) {
                            iOkhttpCallBack.onFailure(new Exception("服務器異常"));
                        }
                    }
                });

            }
        });
    }


    public void doPost(String url, Map<String, String> paramsMap, final IOkhttpCallBack iOkhttpCallBack) {
        //构造请求体
        FormBody.Builder builder = new FormBody.Builder();
        //循环map添加请求参数
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        FormBody formBody = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        //根据请求Request构造一个http请求
        final Call call = mOkHttpClient.newCall(request);
        //执行异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (iOkhttpCallBack != null) {
                            iOkhttpCallBack.onFailure(e);
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //只有response状态码为200，才是真正的成功
                if (response != null && response.isSuccessful()) {
                    //通过获取响应体之后，通过string方法获取json字符串
                    final String json = response.body().string();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (iOkhttpCallBack != null) {
                                iOkhttpCallBack.onSuccess(json);
                            }
                        }
                    });
                    return;
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (iOkhttpCallBack != null) {
                            iOkhttpCallBack.onFailure(new Exception("服務器異常"));
                        }
                    }
                });

            }
        });
    }

    public interface IOkhttpCallBack {
        void onSuccess(String json);

        void onFailure(Exception error);
    }
}
