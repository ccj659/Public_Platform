package com.efly.platform;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.efly.platform.api.APIService;
import com.efly.platform.api.JsonUtils;
import com.efly.platform.base.Constants;
import com.efly.platform.bean.User;
import com.efly.platform.utils.InputMethodUtil;
import com.efly.platform.utils.SharedPreferenceUtil;
import com.efly.platform.utils.ToastUtils;
import com.efly.platform.utils.VolleyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/2.
 */
public class LoginIndexActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "LoginIndexActivity";
    private EditText tv_phone, tv_password;
    private Button btn_login, btn_register;
    private User currentUser;
    private CheckBox cb_remember_pass;
    private TextView tv_forget_pass;
    private ImageView iv_cancel;
    private SharedPreferenceUtil sharedPreferenceUtil;
    private Boolean isCheckeds = false;
    private AlertDialog.Builder progressDialog;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_index);
        initView();
    }

    private void initView() {
        sharedPreferenceUtil = SharedPreferenceUtil.getInstance();
        tv_phone = (EditText) findViewById(R.id.tv_phone);
        tv_password = (EditText) findViewById(R.id.tv_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);
        cb_remember_pass = (CheckBox) findViewById(R.id.cb_remember_pass);
        tv_forget_pass = (TextView) findViewById(R.id.tv_forget_pass);
        iv_cancel = (ImageView) findViewById(R.id.iv_cancel);

        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        iv_cancel.setOnClickListener(this);
        tv_forget_pass.setOnClickListener(this);
        checkAutoLogin(); //检查是否已经选择了自动登录
        cb_remember_pass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    sharedPreferenceUtil.putAutoLogin(true);
                    isCheckeds = true;
                } else {
                    sharedPreferenceUtil.putAutoLogin(false);
                    isCheckeds = false;
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                InputMethodUtil.hideInput(btn_login);
                User user = new User();
                user.UserName = tv_phone.getText().toString().trim().replace(" ", "");
                user.PassWord = tv_password.getText().toString().trim().replace(" ", "");
                if (validateData(user)) {
                    loadData(user);
                }
                break;
            case R.id.btn_register:
                //Intent intent =new Intent(LoginIndexActivity.this,RegisterActivity.class);
                //startActivity(intent);
                break;
            case R.id.iv_cancel:
                 finish();
                break;
            case R.id.tv_forget_pass:

                break;
        }

    }

    private void loadData(User user) {
            progressDialog = new AlertDialog.Builder(this);
            dialog = progressDialog.create();
            final View view = View.inflate(LoginIndexActivity.this, R.layout.fragment_loading, null);
            dialog.setView(view);
            dialog.show();
            JSONObject params = new JSONObject();
            user.ID = 1 + "";

            try {
                params.put("UserName", user.UserName);
                params.put("PassWord", user.PassWord);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e(TAG, params.toString());
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, APIService.URL_LOGIN, params,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e(TAG, response.toString());
                            String code = "";
                            String msg = "";
                            try {
                                code = response.getString("code");
                                msg = response.getString("msg");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (code.equals("200")) {
                                currentUser = JsonUtils.parseUser(response.toString());
                                Log.e("currentUser",currentUser.toString());
                                SharedPreferenceUtil.getInstance().putUser(currentUser);//保存用户信息
                                SharedPreferenceUtil.getInstance().setIsLogin(true);//已经登陆
                                Toast.makeText(LoginIndexActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                                setResult(Constants.RESULT_FOR_LOGIN);
                                finish();
                            } else {
                                Toast.makeText(LoginIndexActivity.this, "请求失败:" + msg, Toast.LENGTH_LONG).show();
                            }
                            dialog.dismiss();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e(TAG, "Error: " + error.getMessage());
                    ToastUtils.ToastShort(LoginIndexActivity.this, "网络错误");
                    dialog.dismiss();
                }
            }) {
                /**
                 * Passing some request headers
                 * */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }

            };
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleyUtils.getInstance().addToRequestQueue(jsonObjReq, "jsonObjReq");
        
    }


    public void checkAutoLogin() {//
        isCheckeds = sharedPreferenceUtil.getAutoLogin();
        if (isCheckeds == true) {
            cb_remember_pass.setChecked(true);
            Log.i("user", "in");
            User user = sharedPreferenceUtil.getUser();
            if (user == null) {
                return;
            }

        } else {
            cb_remember_pass.setChecked(false);
        }
    }


    private boolean validateData(User user) {

        if (TextUtils.isEmpty(user.UserName)) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(user.PassWord)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        if (user.PassWord.length() < 6) {
            Toast.makeText(this, "密码长度不得少于6位", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }


}
