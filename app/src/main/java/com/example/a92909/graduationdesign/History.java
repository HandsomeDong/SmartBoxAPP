package com.example.a92909.graduationdesign;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class History extends AppCompatActivity {
    public TextView history;
    public TextView time;
    String id = null;
    String medicine = null;
    String finish = null;
    String create = null;
    JSONObject medicineJson;

    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        history = (TextView) findViewById(R.id.text_history);
        time = (TextView) findViewById(R.id.time_history);

        Intent intent = getIntent();
        String medicineData = intent.getStringExtra("medicineData");
        try {
            medicineJson = new JSONObject(medicineData);
            id = medicineJson.getString("id");
            finish = medicineJson.getString("finishTime");
            create = medicineJson.getString("createTime");
            medicine = medicineJson.getString("medicine");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        history.setText("订单号：" + id + "\n药品：" + medicine);
        finish = Medicine.timedate(finish);
        create = Medicine.timedate(create);
        this.time.setText("取药时间：" + finish + "\n订单创建时间：" + create);
    }
}
