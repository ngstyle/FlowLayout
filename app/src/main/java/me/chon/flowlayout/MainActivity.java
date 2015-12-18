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
import android.widget.Button;
import android.widget.LinearLayout;
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
                Snackbar.make(view, "hehe haha", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final FlowLayout flowLayout = (FlowLayout) findViewById(R.id.flowlayout);
        addChildren(flowLayout);

        final Button tv1 = (Button) findViewById(R.id.tv1);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flowLayout.removeAllViews();
                addChildren(flowLayout);

                flowLayout.setDistributedAndLastLineFill();
            }
        });

        final Button tv2 = (Button) findViewById(R.id.tv2);
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flowLayout.removeAllViews();
                addChildren(flowLayout);

                flowLayout.setDistributedAndLastLineNotFill();
            }
        });

        final Button tv3 = (Button) findViewById(R.id.tv3);
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flowLayout.removeAllViews();
                addChildren(flowLayout);

                flowLayout.setUnDistributedAndAlignmentType(FlowLayout.RIGHT);
            }
        });

        final Button tv4 = (Button) findViewById(R.id.tv4);
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flowLayout.removeAllViews();
                addChildren(flowLayout);

                flowLayout.setUnDistributedAndAlignmentType(FlowLayout.CENTER);
            }
        });

        final Button tv5 = (Button) findViewById(R.id.tv5);
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,tv5.getText(),Toast.LENGTH_SHORT).show();
                flowLayout.removeAllViews();
                addChildren(flowLayout);

                flowLayout.setUnDistributedAndAlignmentType(FlowLayout.LEFT);
            }
        });


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
