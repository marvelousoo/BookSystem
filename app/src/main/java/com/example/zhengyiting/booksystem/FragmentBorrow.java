package com.example.zhengyiting.booksystem;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class FragmentBorrow extends Fragment {

    private static final String TAG = "FragmentBorrow";
    private MainActivity parent;
    private Button borrowStart;
    private TextView borrowTextView;
    private Button borrowContinue;
    private Button borrowFinish;
    private Button borrowHistoryQuery;
    private TextView borrowHistoryTextView;
    private Handler mHandleruser;
    private Handler mHandlerbook;
    private Handler mHandlerborrow;
    private Handler mHandlerquery;
    private String userId;
    private String userName;
    private String userUid;
    private String bookId;
    private String bookName;
    private String bookUid;
    private String temp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_borrow,container,false);
        borrowStart = view.findViewById(R.id.borrow_start_button);
        borrowTextView = view.findViewById(R.id.borrow_msg);
        borrowContinue = view.findViewById(R.id.borrow_cont_button);
        borrowFinish = view.findViewById(R.id.borrow_fin_button);
        borrowHistoryQuery = view.findViewById(R.id.borrow_hist_button);
        borrowHistoryTextView = view.findViewById(R.id.borrow_hist_msg);
        mHandleruser = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case ConnectedThread.MESSAGE_READ:
                        byte[] buffer = (byte[]) msg.obj;

                        String s = new String(buffer);
                        //textView.setText(s);
                        //receive = s;
                        //Log.d(TAG, "handleMessage: "+ s +".");
                        String[] responese = s.split("\\|");
                        //responese = s1;
                        if(responese[0].equals("c")) {
                            if(responese[1].equals("success")) {
                                userId = responese[2];
                                userName = responese[3];
                                userUid = responese[4].replace(" ", "");
                                borrowTextView.setText("用户卡读取成功，用户ID：" + userId + ", 用户名：" + userName +", 用户UID：" + userUid +".");
                                try{
                                    TimeUnit.SECONDS.sleep(3);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                parent.mConnectedThread.setmHandler(mHandlerbook);
                                rfidBookScan();
                            }
                            if(responese[1].equals("fail")){
                                borrowTextView.setText("刷卡失败，请重新刷用户卡，失败原因：" + responese[2].replace(" ", "") + ".");
                                try{
                                    TimeUnit.SECONDS.sleep(3);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                parent.mConnectedThread.setmHandler(mHandleruser);
                                rfidUserScan();

                            }
                        }
                        break;
                }

            }
        };
        mHandlerbook = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case ConnectedThread.MESSAGE_READ:
                        byte[] buffer = (byte[]) msg.obj;
                        try {
                            String s = new String(buffer);
                            String[] responese = s.split("\\|");
                            if(responese[0].equals("d")) {
                                if (responese[1].equals("success")) {
                                    bookId = responese[2];
                                    bookName = responese[3];
                                    bookUid = responese[4].replace(" ", "");
                                    Log.d(TAG, "handleMessage: " + bookUid + ".");
                                    borrowTextView.setText("图书卡读取成功，图书ID：" + bookId + ", 图书名：" + bookName + ", 图书UID：" + bookUid + ".");
                                    if(userId == null){
                                        borrowTextView.setText("用户数据为空，请点击开始借阅，重新开始");
                                        break;
                                    }
                                    parent.mConnectedThread.setmHandler(mHandlerborrow);
                                    try{
                                        TimeUnit.SECONDS.sleep(2);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    borrowStart();
                                }
                                if (responese[1].equals("fail")) {
                                    borrowTextView.setText("刷卡失败，请重新刷图书卡，失败原因：" + responese[2].replace(" ", "") + ".");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        //textView.setText(s);
                        //receive = s;
                        //Log.d(TAG, "handleMessage: "+ s +".");
                        //String[] responese = s.split("\\|");
                        //responese = s1;

                        break;
                }

            }
        };

        mHandlerborrow = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case ConnectedThread.MESSAGE_READ:
                        byte[] buffer = (byte[]) msg.obj;

                        String s = new String(buffer);
                        //textView.setText(s);
                        //receive = s;
                        //Log.d(TAG, "handleMessage: "+ s +".");
                        String[] responese = s.split("\\|");
                        //responese = s1;
                        if(responese[0].equals("e")){
                            if(responese[1].equals("success")){
                                borrowTextView.setText("借阅成功");
                            }
                            if(responese[1].equals("fail")){
                                borrowTextView.setText("借书失败，失败原因：" + responese[2]);
                            }
                        }
                        break;
                }

            }
        };

        mHandlerquery = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case ConnectedThread.MESSAGE_READ:
                        byte[] buffer = (byte[]) msg.obj;

                        String s = new String(buffer);
                        //textView.setText(s);
                        //receive = s;
                        //Log.d(TAG, "handleMessage: "+ s +".");
                        String[] responese = s.split("\\|");
                        //responese = s1;
                        if(responese[0].equals("g")){
                            if(responese[1].equals("success")){
                                borrowHistoryTextView.setText("查询成功，查询结果：" + responese[2]);
                            }
                            if(responese[1].equals("fail")){
                                borrowHistoryTextView.setText("借书失败，失败原因：" + responese[2]);
                            }
                        }
                        break;
                }

            }
        };

        borrowStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrowTextView.setText("请扫描用户卡");
                parent.mConnectedThread.setmHandler(mHandleruser);
                rfidUserScan();

            }
        });

        borrowContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrowTextView.setText("请扫描图书");
                parent.mConnectedThread.setmHandler(mHandlerbook);
                rfidBookScan();
            }
        });

        borrowFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userId = null;
                userName = null;
                bookId = null;
                bookName = null;
                borrowTextView.setText("借阅完成，请下一位同学点击开始借阅");
            }
        });

        borrowHistoryQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.mConnectedThread.setmHandler(mHandlerquery);
                borrowQuery("borrowlists");
            }
        });


        return view;
    }

    public static FragmentBorrow newInstance (MainActivity activity) {
        FragmentBorrow fragmentBorrow = new FragmentBorrow();
        fragmentBorrow.parent = activity;
        return fragmentBorrow;
    }

    public void rfidUserScan(){
        parent.btwrite("c");
    }

    public void rfidBookScan(){
        borrowTextView.setText("请扫描图书");
        parent.btwrite("d");
    }

    public void borrowStart(){
        String borrowStr = "e|" + userUid + "|" + userName + "|" + bookUid + "|" + bookName;
        parent.btwrite(borrowStr);
    }

    public void borrowQuery(String tablename){
        String select = "select * from " + tablename + ";";
        String selectStr = "g|"+ tablename + "|" + select + "|";
        parent.btwrite(selectStr);
    }
}

