package io.github.ghostwriternr.selene;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.ncapdevi.fragnav.FragNavController;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.util.ArrayList;
import java.util.List;

import layout.FriendsFragment;
import layout.HotFragment;
import layout.UserFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, UserFragment.OnFragmentInteractionListener, FriendsFragment.OnFragmentInteractionListener, HotFragment.OnFragmentInteractionListener {

    private BottomBar mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.MainFrame, new PlaceholderFragment()).commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String ggauth = "http://10.117.11.116/api/v1/googleDATA?";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("io.github.ghostwriternr", Context.MODE_PRIVATE);
        String gid = sharedPref.getString(getString(R.string.google), null);
        String fid = sharedPref.getString(getString(R.string.facebook), null);
        ggauth += fid + "&" + gid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ggauth,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("LoginActivity", response);
//                        info.setText("Response is: "+ response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("LoginActivity", "That didn't work out!");
//                info.setText("That didn't work out!");
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        List<Fragment> fragments = new ArrayList<>(3);
        fragments.add(HotFragment.newInstance("Hello", "World"));
        fragments.add(FriendsFragment.newInstance("Yolo", "People!"));
        fragments.add(UserFragment.newInstance("Heyo", "guys!"));

        final FragNavController fragNavController = new FragNavController(getSupportFragmentManager(), R.id.MainFrame, fragments);
//        mBottomBar = BottomBar.attachShy((CoordinatorLayout) findViewById(R.id.myCoordinator),
//                findViewById(R.id.myScrollingContent), savedInstanceState);
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.noTopOffset();
        mBottomBar.setActiveTabColor("#D32F2F");
        mBottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bb_menu_friends) {
                    // The user selected item number one.
                    getSupportActionBar().setTitle("Hot");
                    fragNavController.switchTab(FragNavController.TAB1);
                    fragNavController.clearStack();
                } else if (menuItemId == R.id.bb_menu_favorites) {
                    getSupportActionBar().setTitle("Favorites");
                    fragNavController.switchTab(FragNavController.TAB2);
                    fragNavController.clearStack();
                } else {
                    getSupportActionBar().setTitle("User");
                    fragNavController.switchTab(FragNavController.TAB3);
                    fragNavController.clearStack();
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                fragNavController.clearStack();
                Log.v("MainActivity", "Reselected!");
                if (menuItemId == R.id.bb_menu_friends) {
                    //The user reselected item number one, scroll your content to top
                    ListView listView = (ListView) findViewById(R.id.TestDetailsView);
                    assert listView != null;
                    listView.smoothScrollToPosition(0);
                } else if (menuItemId == R.id.bb_menu_favorites) {
                    ListView listView = (ListView) findViewById(R.id.TestDetailsView);
                    assert listView != null;
                    listView.smoothScrollToPosition(0);
                } else {

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }


    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {

        }
    }

    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }
}
