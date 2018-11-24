package com.example.zhengyiting.booksystem;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class FragmentRef extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ref, container,false);
        ImageView imageView = (ImageView) view.findViewById(R.id.ref_pic);
        Glide.with(this).load(R.drawable.book_shelves_1024).into(imageView);
        return view;
    }
}
