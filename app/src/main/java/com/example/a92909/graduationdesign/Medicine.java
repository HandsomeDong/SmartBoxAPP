package com.example.a92909.graduationdesign;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by 92909 on 2018/11/2.
 */

public class Medicine extends AppCompatActivity {
    public TextView process;
    public TextView time;
    int status = 0;
    int boxId = 0;
    int verification = 0;
    String medicine = null;
    String update = null;
    String create = null;
    JSONObject medicineJson;

    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicine);
        process = (TextView) findViewById(R.id.process);
        time = (TextView) findViewById(R.id.time);

        Intent intent = getIntent();
        String medicineData = intent.getStringExtra("medicineData");
        try {
            medicineJson = new JSONObject(medicineData);
            status = medicineJson.getInt("status");
            update = medicineJson.getString("updateTime");
            create = medicineJson.getString("createTime");
            boxId = medicineJson.getInt("bid");
            verification = medicineJson.getInt("verification");
            medicine = medicineJson.getString("medicine");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        switch (status) {
            case 1:
                process.setText("正在配药中，请耐心等候……");
                create = timedate(create);
                this.time.setText("订单创建时间：\n" + create);
                break;
            case 2:
                process.setText("正在运送到" + boxId + "号柜子，请耐心等候……");
                update = timedate(update);
                create = timedate(create);
                this.time.setText("最后更新时间：\n" + update + "\n订单创建时间：\n" + create);
                break;
            case 3:
                process.setText("您的药品已放到" + boxId + "号柜子中，验证码为" + verification + "。\n\n药方: " + medicine);
                update = timedate(update);
                create = timedate(create);
                this.time.setText("最后更新时间：\n" + update + "\n订单创建时间：\n" + create);
                break;
            default:
                process.setText(" 您目前没有药品！");
        }
    }

    public static String timedate(String time) {
        time = time.substring(0, time.length() - 3);
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }
}
