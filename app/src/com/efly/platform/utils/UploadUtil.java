package com.efly.platform.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * Created by Administrator on 2016/3/17.
 */
public class UploadUtil {

    public static UploadUtil uploadUtil;
    private static final String TAG = "uploadFile-->";
    private static final int TIME_OUT = 10 * 1000; // 超时时间
    private static final String CHARSET = "utf-8"; // 设置编码
      int res = 0;
     String result ="null";
    public Context context;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -1:
                    Log.e(TAG, result);
                   /* Intent intent = new Intent("android.intent.action.MY_BROADCAST");
                    intent.putExtra("msg", "hello receiver.");
                    context.sendBroadcast(intent);*/

                    break;
                case 1:
                    Log.e(TAG, result);
                   /* Intent intent1 = new Intent("android.intent.action.MY_BROADCAST");
                    intent1.putExtra("msg", "hello receiver.");
                    context.sendBroadcast(intent1);*/
                    break;

            }


        }
    };

    public UploadUtil(Context context){
        this.context=context;
    }

    public static UploadUtil getInstance(Context context){
        if (uploadUtil==null){
            uploadUtil=new UploadUtil(context);
        }
        return uploadUtil;

    }


    /**
     * 上传文件到服务器
     *
     * @param RequestURL 请求的rul
     * @return 返回响应的内容
     */


    public  int uploadFile( String RequestURL, final Bitmap bitmap) {

        final String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        final String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型
        try {
            URL url = new URL(RequestURL);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", CHARSET); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);

            if (bitmap != null) {
                /**
                 * 当文件不为空时执行上传
                 */
                final boolean[] isRun = {true};
                Log.e(TAG, "isRun : " + isRun[0]);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (isRun[0]) {
                            DataOutputStream dos = null;
                            try {
                                dos = new DataOutputStream(conn.getOutputStream());
                                StringBuffer sb = new StringBuffer();
                                sb.append(PREFIX);
                                sb.append(BOUNDARY);
                                sb.append(LINE_END);
                                /**
                                 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                                 * filename是文件的名字，包含后缀名
                                 */
                                sb.append("Content-Disposition: form-data; name=\"file\"; filename=\""
                                        + bitmap.getByteCount() + "\"" + LINE_END);
                                sb.append("Content-Type: application/octet-stream; charset="
                                        + CHARSET + LINE_END);
                                sb.append(LINE_END);
                                dos.write(sb.toString().getBytes());
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();//图片变为stream
                                bitmap.compress(Bitmap.CompressFormat.PNG, 80, baos);
                                InputStream is = new ByteArrayInputStream(baos.toByteArray());

                                byte[] bytes = new byte[1024];
                                int len = 0;
                                while ((len = is.read(bytes)) != -1) {
                                    dos.write(bytes, 0, len);
                                }

                                is.close();
                                dos.write(LINE_END.getBytes());
                                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
                                        .getBytes();
                                dos.write(end_data);
                                dos.flush();
                                handler.sendEmptyMessage(-1);
                                /**
                                 * 获取响应码 200=成功 当响应成功，获取响应的流
                                 */
                                res = conn.getResponseCode();
                                Log.e(TAG, "response code:" + res);
                                if (res == 200) {
                                    InputStream input = conn.getInputStream();
                                    StringBuffer sb1 = new StringBuffer();
                                    int ss;
                                    while ((ss = input.read()) != -1) {
                                        sb1.append((char) ss);
                                    }
                                    result = sb1.toString();
                                    handler.sendEmptyMessage(1);
                                    Log.e(TAG, "result : " + result);
                                } else {
                                    handler.sendEmptyMessage(-1);
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                                isRun[0] = false;
                                handler.sendEmptyMessage(-1);
                            }
                            isRun[0] = false;
                        }
                    }
                }).start();
                isRun[0] = false;
                Log.e(TAG, "isRun : " + isRun[0]);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

}
