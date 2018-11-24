package com.example.zhengyiting.booksystem;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FragmentBookInput extends Fragment {

    private MainActivity parent;
    private Handler myHandler;
    private TextView bookinputtext;
    private static final String TAG = "FragmentBookInput";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_input, container, false);

        final EditText bookidEditText = (EditText) view.findViewById(R.id.book_id_input);
        final EditText booknameEditText = (EditText) view.findViewById(R.id.book_name_input);
        bookinputtext = (TextView) view.findViewById(R.id.book_msg);
        Button bookinputbutton = (Button) view.findViewById(R.id.book_input_button);
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
                        if(s1[0].equals("b")) {
                            if(s1[1].equals("success")) {
                                bookinputtext.setText("写入成功，图书ID：" + s1[2] + ", 图书名：" + s1[3] +", UID："+s1[4].replace(" ", "")+".");
                            }
                            if(s1[1].equals("fail")) {
                                bookinputtext.setText("写入失败，失败原因："+ s1[2].replace(" ", "") + ".");
                            }
                        }

                        break;
                }

            }
        };

        bookinputbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookinputtext.setText("");
                bookinputtext.setText("请刷图书！\n");
                String bookid = bookidEditText.getText().toString();
                String bookname = booknameEditText.getText().toString();
                String send = "b" + "|" + bookid + "|" + bookname;
                parent.btwrite(send);
                parent.mConnectedThread.setmHandler(myHandler);

            }
        });



        return view;
    }

    public static FragmentBookInput newInstance (MainActivity activity) {
        FragmentBookInput fragmentBookInput = new FragmentBookInput();
        fragmentBookInput.parent = activity;
        return fragmentBookInput;
    }
}
