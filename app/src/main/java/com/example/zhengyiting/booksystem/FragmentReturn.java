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

public class FragmentReturn extends Fragment {

    private static final String TAG = "FragmentReturn";
    private MainActivity parent;
    private Button returnStart;
    private TextView returnTextView;
    private Button returnContinue;
    private Button returnFinish;
    private Button returnHistoryQuery;
    private TextView returnHistoryTextView;
    private Handler mHandleruser;
    private Handler mHandlerbook;
    private Handler mHandlerreturn;
    private Handler mHandlerquery;
    private String userId;
    private String userName;
    private String userUid;
    private String bookId;
    private String bookName;
    private String bookUid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_return,container,false);
        returnStart = view.findViewById(R.id.return_start_button);
        returnTextView = view.findViewById(R.id.return_msg);
        returnContinue = view.findViewById(R.id.return_cont_button);
        returnFinish = view.findViewById(R.id.return_fin_button);
        returnHistoryQuery = view.findViewById(R.id.return_hist_button);
        returnHistoryTextView = view.findViewById(R.id.return_hist_msg);
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
                                returnTextView.setText("用户卡读取成功，用户ID：" + userId + ", 用户名：" + userName +", 用户UID：" + userUid +".");
                                try{
                                    TimeUnit.SECONDS.sleep(3);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                parent.mConnectedThread.setmHandler(mHandlerbook);
                                rfidBookScan();
                            }
                            if(responese[1].equals("fail")){
                                returnTextView.setText("刷卡失败，请重新刷用户卡，失败原因：" + responese[2].replace(" ", "") + ".");
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
                                    returnTextView.setText("图书卡读取成功，图书ID：" + bookId + ", 图书名：" + bookName + ", 图书UID：" + bookUid + ".");
                                    if(userId == null){
                                        returnTextView.setText("用户数据为空，请点击开始归还，重新开始");
                                        break;
                                    }
                                    parent.mConnectedThread.setmHandler(mHandlerreturn);
                                    try{
                                        TimeUnit.SECONDS.sleep(1);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    returnStart();
                                }
                                if (responese[1].equals("fail")) {
                                    returnTextView.setText("刷卡失败，请重新刷图书卡，失败原因：" + responese[2].replace(" ", "") + ".");
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

        mHandlerreturn = new Handler(){
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
                        if(responese[0].equals("f")){
                            if(responese[1].equals("success")){
                                returnTextView.setText("归还成功");
                            }
                            if(responese[1].equals("fail")){
                                returnTextView.setText("还书失败，失败原因：" + responese[2]);
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
                                returnHistoryTextView.setText("查询成功，查询结果：" + responese[2]);
                            }
                            if(responese[1].equals("fail")){
                                returnHistoryTextView.setText("还书失败，失败原因：" + responese[2]);
                            }
                        }
                        break;
                }

            }
        };

        returnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnTextView.setText("请扫描用户卡");
                parent.mConnectedThread.setmHandler(mHandleruser);
                rfidUserScan();

            }
        });

        returnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnTextView.setText("请扫描图书");
                parent.mConnectedThread.setmHandler(mHandlerbook);
                rfidBookScan();
            }
        });

        returnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userId = null;
                userName = null;
                bookId = null;
                bookName = null;
                returnTextView.setText("还书完成，请下一位同学点击开始归还");
            }
        });

        returnHistoryQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.mConnectedThread.setmHandler(mHandlerquery);
                returnQuery("borrowlists");
            }
        });


        return view;
    }

    public static FragmentReturn newInstance (MainActivity activity) {
        FragmentReturn fragmentreturn = new FragmentReturn();
        fragmentreturn.parent = activity;
        return fragmentreturn;
    }

    public void rfidUserScan(){
        parent.btwrite("c");
    }

    public void rfidBookScan(){
        returnTextView.setText("请扫描图书");
        parent.btwrite("d");
    }

    public void returnStart(){
        String returnStr = "f|" + userUid + "|" + bookUid;
        parent.btwrite(returnStr);
    }

    public void returnQuery(String tablename){
        String select = "select * from " + tablename + ";";
        String selectStr = "g|"+ tablename + "|" + select + "|";
        parent.btwrite(selectStr);
    }
}

