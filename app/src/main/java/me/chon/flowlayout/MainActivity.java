package me.chon.flowlayout;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import me.chon.flowlayout.view.FlowLayout;

public class MainActivity extends AppCompatActivity {

    private String[] str = {"Hello","Android Studio","java",
            "FlowLayout","material design","AppCompat","Ryoka",
            "chon","layoutinflater","Xcode","SmartTabLayout",
            "MaterialKit","XferMode"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FlowLayout flowLayout1 = (FlowLayout) findViewById(R.id.flowlayout1);
        FlowLayout flowLayout2 = (FlowLayout) findViewById(R.id.flowlayout2);
        FlowLayout flowLayout3 = (FlowLayout) findViewById(R.id.flowlayout3);
        FlowLayout flowLayout4 = (FlowLayout) findViewById(R.id.flowlayout4);
        FlowLayout flowLayout5 = (FlowLayout) findViewById(R.id.flowlayout5);

        addChildren(flowLayout1);
        addChildren(flowLayout2);
        addChildren(flowLayout3);
        addChildren(flowLayout4);
        addChildren(flowLayout5);
    }


    private void addChildren(FlowLayout mFlowlayout){
        for (int i = 0; i < str.length; i++) {
            LayoutInflater inflater = LayoutInflater.from(this);
            TextView tv = (TextView) inflater.inflate(R.layout.layout_text,mFlowlayout,false);
            tv.setText(str[i]);

            final int position = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this,str[position],Toast.LENGTH_SHORT).show();
                }
            });

            mFlowlayout.addView(tv);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
