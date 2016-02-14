package me.alwx.appbase.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.alwx.appbase.R;
import me.alwx.appbase.ui.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        attachBarcodeListFragment();
        mToolbar.setTitle(R.string.app_name);
    }

    private void attachBarcodeListFragment() {
        MainFragment fragment = new MainFragment();
        fragment.setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction()
                .add(R.id.content_frame, fragment)
                .commit();
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }
}
