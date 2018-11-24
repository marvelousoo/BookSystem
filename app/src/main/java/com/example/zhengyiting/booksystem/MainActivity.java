package com.example.zhengyiting.booksystem;


import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private static final String TAG = "MainActivity";
    private BluetoothAdapter mBluetoothAdapter;
    ConnectedThread mConnectedThread;
    private static final int REQUEST_ENABLE_BT = 1;
    private Handler handler;
    private FragmentUserInput fragmentUserInput = FragmentUserInput.newInstance(this);
    private FragmentBookInput fragmentBookInput = FragmentBookInput.newInstance(this);
    private FragmentBorrow fragmentBorrow = FragmentBorrow.newInstance(this);
    private FragmentReturn fragmentReturn = FragmentReturn.newInstance(this);
    private FragmentRef fragmentRef = new FragmentRef();
    private String receive = "";
    private TextView textView;
    private Button test;
    private ImageView mainPic;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.user_msg);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        mainPic = (ImageView) findViewById(R.id.main_pic);
        Glide.with(this).load(R.drawable.book_shelves_1024).into(mainPic);


        navView.setCheckedItem(R.id.nav_userinput);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    default:
                        break;
                    case R.id.nav_userinput:
                        fragmentTransaction.replace(R.id.fragment_framelayout, fragmentUserInput);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_bookinput:
                        fragmentTransaction.replace(R.id.fragment_framelayout, fragmentBookInput);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_borrowbook:
                        fragmentTransaction.replace(R.id.fragment_framelayout, fragmentBorrow);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_return:
                        fragmentTransaction.replace(R.id.fragment_framelayout, fragmentReturn);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_ref:
                        fragmentTransaction.replace(R.id.fragment_framelayout, fragmentRef);
                        mDrawerLayout.closeDrawers();
                        break;

                }
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                return true;
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Log.d(TAG, "onOptionsItemSelected: home");
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.backup:
                //Toast.makeText(this, "You clicked Settings", Toast.LENGTH_SHORT).show();
                //Log.d(TAG, "onOptionsItemSelected: in setting");

                // 获取蓝牙适配器
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null) {
                    Toast.makeText(getApplicationContext(), "该设备不支持蓝牙", Toast.LENGTH_SHORT).show();
                }

                //请求开启蓝牙
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }

                //进入蓝牙设备连接界面
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), DevicesListActivity.class);
                startActivity(intent);

                break;
            default:
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (BluetoothUtils.getBluetoothSocket() == null || mConnectedThread != null) {
            //txtIsConnected.setText("未连接");
            return;
        }


        //启动蓝牙数据收发线程
        mConnectedThread = new ConnectedThread(BluetoothUtils.getBluetoothSocket());
        mConnectedThread.start();
    }

    public void btwrite(String sendStr) {
        byte[] bytes = sendStr.getBytes();
        mConnectedThread.write(bytes);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: running");
        btwrite("h");
        btwrite("xxx");
    }
}

