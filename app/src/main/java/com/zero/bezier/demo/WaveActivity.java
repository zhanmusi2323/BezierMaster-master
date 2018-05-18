package com.zero.bezier.demo;

import android.app.Activity;
import android.os.Bundle;

import com.zero.bezier.R;
import com.zero.bezier.widget.wave.WaveView;
import com.zero.bezier.widget.wave.WaveViewTest;

/**
 * @author linzewu
 * @date 2016/6/5
 */
public class WaveActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave);

        WaveViewTest waveView = (WaveViewTest) findViewById(R.id.wave);
        waveView.setRunning();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
