package mukesh.com.task9;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private Fragment grid_frag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Your Quote");

        grid_frag = new GridViewFragment();
        FragmentManager grid_fm = getSupportFragmentManager();
        FragmentTransaction grid_ft = grid_fm.beginTransaction();
        grid_ft.replace(R.id.ll_id, grid_frag);
        grid_ft.commit();
    }
}
