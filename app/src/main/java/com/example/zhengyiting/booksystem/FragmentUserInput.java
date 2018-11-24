package com.example.zhengyiting.booksystem;

import android.app.Activity;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.TextView;

public class FragmentUserInput extends Fragment {

    private static final String TAG = "FragmentUserInput";
    private MainActivity parent;
    private Handler myHandler;
    private TextView userinputtext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_input, container, false);

        final EditText useridEditText = (EditText) view.findViewById(R.id.user_id_input);
        final EditText usernameEditText = (EditText) view.findViewById(R.id.user_name_input);
        userinputtext = (TextView) view.findViewById(R.id.user_msg);
        Button userinputbutton = (Button) view.findViewById(R.id.user_input_button);
        myHandler = new Handler() {
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
                        String[] s1 = s.split("\\|");
                        if(s1[0].equals("a")) {
                            if(s1[1].equals("success")) {
                                userinputtext.setText("写入成功，用户ID：" + s1[2] + ", 用户名：" + s1[3] +", UID："+s1[4].replace(" ", "")+".");
                            }
                            if(s1[1].equals("fail")) {
                                userinputtext.setText("写入失败，失败原因："+ s1[2].replace(" ", "") + ".");
                            }
                        }

                        break;
                }

            }
        };

        userinputbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userinputtext.setText("");
                userinputtext.setText("请刷用户卡！\n");
                String userid = useridEditText.getText().toString();
                String username = usernameEditText.getText().toString();
                String send = "a" + "|" + userid + "|" + username;
                parent.btwrite(send);
                parent.mConnectedThread.setmHandler(myHandler);

            }
        });


        return view;
    }

    public static FragmentUserInput newInstance (MainActivity activity) {
        FragmentUserInput fragmentUserInput = new FragmentUserInput();
        fragmentUserInput.parent = activity;
        return fragmentUserInput;
    }
}
