package com.example.dalek.hello;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int a = add(2, 3);
        String buf = String.format("%d(JNI)", a);
        buf = getMsgFromJni() + "__" + buf;

        ((TextView) findViewById(R.id.textView1)).setText(buf);

    }


    static {
        System.loadLibrary("hello-jni");
    }

    public native String getMsgFromJni();
    public native int add(int a, int b);

}
