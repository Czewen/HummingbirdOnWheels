package hummingbird.android.mobile_app.views.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import hummingbird.android.mobile_app.R;
import test.MyFragmentPageAdapter;

public class TestFragment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tablayouttest);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        MyFragmentPageAdapter pagerUpdater = new MyFragmentPageAdapter(getSupportFragmentManager(), TestFragment.this);
        viewPager.setAdapter(pagerUpdater);

        TabLayout tablayout = (TabLayout) findViewById(R.id.tablayouttest);
        tablayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
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
