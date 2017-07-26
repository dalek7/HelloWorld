package com.example.seung.hellotf;

// Written based on
// https://omid.al/posts/2017-02-20-Tutorial-Build-Your-First-Tensorflow-Android-App.html
// https://raw.githubusercontent.com/omimo/TFDroid/master/app/src/main/java/al/omid/tfdroid/MainActivity.java

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

public class MainActivity extends AppCompatActivity {

    private Button mBtn;

    private static final String MODEL_FILE = "file:///android_asset/optimized_tfdroid.pb";
    private static final String INPUT_NODE = "I";
    private static final String OUTPUT_NODE = "O";

    private static final int[] INPUT_SIZE = {1,3};

    private TensorFlowInferenceInterface inferenceInterface;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("tensorflow_inference");
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtn = (Button) findViewById(R.id.button1);

        inferenceInterface = new TensorFlowInferenceInterface();
        inferenceInterface.initializeTensorFlow(getAssets(), MODEL_FILE);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.textView1);
        tv.setText(stringFromJNI());

        int a = add(2, 3);
        String buf = String.format("%d from JNI NDK r12b", a);
        tv.setText(buf);


        mBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("info", "clicked");

                final EditText editNum1 = (EditText) findViewById(R.id.editText1);
                final EditText editNum2 = (EditText) findViewById(R.id.editText2);
                final EditText editNum3 = (EditText) findViewById(R.id.editText3);

                float num1 = Float.parseFloat(editNum1.getText().toString());
                float num2 = Float.parseFloat(editNum2.getText().toString());
                float num3 = Float.parseFloat(editNum3.getText().toString());


                float[] inputFloats = {num1, num2, num3};

                inferenceInterface.fillNodeFloat(INPUT_NODE, INPUT_SIZE, inputFloats);

                inferenceInterface.runInference(new String[] {OUTPUT_NODE});

                float[] resu = {0, 0};
                inferenceInterface.readNodeFloat(OUTPUT_NODE, resu);

                final TextView textViewR = (TextView) findViewById(R.id.textView1);
                String str1 = String.format("%.1f, %.1f (Tensorflow)", resu[0], resu[1]);
                textViewR.setText(str1);

                // Manual calc.
                // sess.run(tf.assign(W, [[1, 2],[4,5],[7,8]]))
                // sess.run(tf.assign(b, [1,1]))
                float v1 = num1*1 + num2*4 + num3*7 + 1;
                float v2 = num1*2 + num2*5 + num3*8 + 1;

                final TextView textViewManual = (TextView) findViewById(R.id.textView2);
                String str2 = String.format("%.1f, %.1f (manual)", v1, v2);
                textViewManual.setText(str2);

            }
        });
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public native int add(int a, int b);

}
