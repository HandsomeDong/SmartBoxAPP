package com.example.a92909.graduationdesign;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import okhttp3.Response;

public class Register extends AppCompatActivity {

    private EditText phoneEdit;
    private EditText verificationEdit;
    private EditText userNameEdit;
    private EditText passwordEdit;
    private Button sendVerificationBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        phoneEdit = (EditText) findViewById(R.id.input_phone);
        verificationEdit = (EditText) findViewById(R.id.input_verification);
        userNameEdit = (EditText) findViewById(R.id.input_username);
        passwordEdit = (EditText) findViewById(R.id.input_password);
        Button registerBtn = (Button) findViewById(R.id.register);
        sendVerificationBtn = (Button) findViewById(R.id.send_verification);

        sendVerificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(sendVerification).start();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(sendRegister).start();
            }
        });
    }

    Runnable sendVerification = new Runnable() {
        @Override
        public void run() {
            int status = 2;
            Message m1 = handler1.obtainMessage();
            String phoneNumber = phoneEdit.getText().toString();
            if (phoneNumber.length() == 11 && isInteger(phoneNumber)) {
                try {
                    String url = "http://119.29.247.25/smartbox/register/send";
                    MyHttpClient myHttpClient = new MyHttpClient(url, "GET");
                    Map<String, Object> params = new HashMap<>();
                    params.put("phoneNumber", phoneNumber);
                    myHttpClient.setParams(params);
                    Response response = myHttpClient.request();
                    String responseStr = response.body().string();
                    JSONObject resultJson = new JSONObject(responseStr);
                    status = resultJson.getInt("status");
                    if (status == 1)//发送成功
                    {
                        m1.what = 1;
                        handler1.sendMessage(m1);
                    } else if (status == 0) {//发送失败
                        m1.what = 2;
                        handler1.sendMessage(m1);
                    } else if (status == -1) {//发送账号已注册
                        m1.what = 3;
                        handler1.sendMessage(m1);
                    } else {//其他情况：联网失败，服务器异常等
                        m1.what = 4;
                        handler1.sendMessage(m1);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            } else {
                m1.what = 5;
                handler1.sendMessage(m1);
            }
        }
    };

    Runnable sendRegister = new Runnable() {
        @Override
        public void run() {
            int status = 2;
            Message m2 = handler2.obtainMessage();
            String phoneNumber = phoneEdit.getText().toString();
            String verification = verificationEdit.getText().toString();
            String userName = userNameEdit.getText().toString();
            String password = passwordEdit.getText().toString();
            if (phoneNumber.length() == 11 && isInteger(phoneNumber) && verification.length() > 0 && userName.length() > 0 && password.length() > 0) {
                try {
                    String url = "http://119.29.247.25/smartbox/register/register";
                    MyHttpClient myHttpClient = new MyHttpClient(url, "POST");
                    Map<String, Object> params = new HashMap<>();
                    params.put("id", phoneNumber);
                    params.put("password", password);
                    params.put("name", new String(userName.getBytes(), StandardCharsets.UTF_8));
                    params.put("verification", verification);

                    myHttpClient.setParams(params);
                    Response response = myHttpClient.request();
                    String responseStr = response.body().string();
                    JSONObject resultJson = new JSONObject(responseStr);
                    status = resultJson.getInt("status");
                    if (status == 1)//注册成功
                    {
                        m2.what = 1;
                        handler2.sendMessage(m2);
                    } else if (status == 0) {//注册失败
                        m2.what = 2;
                        handler2.sendMessage(m2);
                    } else if (status == -1) {//验证码错误
                        m2.what = 3;
                        handler2.sendMessage(m2);
                    } else {//其他情况：联网失败，服务器异常等
                        m2.what = 4;
                        handler2.sendMessage(m2);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            } else {
                m2.what = 5;
                handler2.sendMessage(m2);
            }
        }
    };

    @SuppressLint("HandlerLeak")
    public Handler handler1 = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Toast toast = Toast.makeText(Register.this, "验证码发送成功！", Toast.LENGTH_SHORT);
                sendVerificationBtn.setEnabled(false);
                toast.show();
            } else if (msg.what == 2) {
                Toast toast = Toast.makeText(Register.this, "验证码发送失败，请稍后再试！", Toast.LENGTH_SHORT);
                toast.show();
            } else if (msg.what == 3) {
                Toast toast = Toast.makeText(Register.this, "该手机号已注册，请直接登陆！", Toast.LENGTH_SHORT);
                toast.show();
            } else if (msg.what == 4) {
                Toast toast = Toast.makeText(Register.this, "网络错误，请稍后再试！", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Toast toast = Toast.makeText(Register.this, "请输入正确的手机号！", Toast.LENGTH_SHORT);
                toast.show();
            }
            super.handleMessage(msg);
        }
    };

    @SuppressLint("HandlerLeak")
    public Handler handler2 = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Intent result = new Intent(Register.this, Result.class);
                startActivity(result);
                Toast toast = Toast.makeText(Register.this, "注册成功！", Toast.LENGTH_SHORT);
                toast.show();
            } else if (msg.what == 2) {
                Toast toast = Toast.makeText(Register.this, "注册失败！请稍后再试！", Toast.LENGTH_SHORT);
                toast.show();
            } else if (msg.what == 3) {
                Toast toast = Toast.makeText(Register.this, "验证码错误！", Toast.LENGTH_SHORT);
                toast.show();
            } else if (msg.what == 4) {
                Toast toast = Toast.makeText(Register.this, "网络错误，请稍后重试！", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Toast toast = Toast.makeText(Register.this, "信息填写不完整！", Toast.LENGTH_SHORT);
                toast.show();
            }
            super.handleMessage(msg);
        }
    };

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
}
