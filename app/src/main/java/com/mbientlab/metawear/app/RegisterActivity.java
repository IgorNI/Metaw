package com.mbientlab.metawear.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by nilif on 2016/6/14.
 */
public class RegisterActivity extends Activity{
    private Button confirmBtn;
    private EditText patientName;
    private EditText userName;
    private EditText phoneNum;
    private EditText password;
    private DatePicker datePicker;
    private int year;
    private int month;
    private int day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        confirmBtn = (Button) findViewById(R.id.confirm_btn);
        patientName = (EditText) findViewById(R.id.et_patientName);
        userName = (EditText) findViewById(R.id.et_userName);
        phoneNum = (EditText) findViewById(R.id.et_mobile);
        password = (EditText) findViewById(R.id.et_password);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        //获取日期
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                RegisterActivity.this.year = year;
                RegisterActivity.this.month = monthOfYear+1;
                RegisterActivity.this.day = dayOfMonth;
                Toast.makeText(RegisterActivity.this, "时间是"+year+"年"+(monthOfYear+1)+"月"+dayOfMonth+"日", Toast.LENGTH_SHORT).show();
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2016/6/14 将所有的字段保存？
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this,MyInforActivity.class);
                startActivity(intent);
            }
        });

    }
}
