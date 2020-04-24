package com.example.test12;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * @author june
 */
public class MainActivity extends AppCompatActivity {

    private GridFragment mGridFragment;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        //网格
        mGridFragment = new GridFragment ( );
        getSupportFragmentManager ().beginTransaction ().replace (R.id.activity_main,mGridFragment).commit ();
    }
}
