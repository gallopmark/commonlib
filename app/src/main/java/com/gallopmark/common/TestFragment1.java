package com.gallopmark.common;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.gallopmark.commom.CommonFragment;

import java.util.List;

public class TestFragment1 extends CommonFragment {

    @Override
    protected int bindLayoutId(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_test;
    }

    @Override
    protected void bindView(@NonNull View view) {
//        RecyclerView mRecyclerView = view.findViewById(R.id.mRecyclerView);
//        List<String> list = new ArrayList<>();
//        for (int i = 0; i < 50; i++) {
//            String[] array = {"XPopup", "BasePopup", "HouseLoading", "GifLoadingView"};
//            list.add(array[new Random().nextInt(array.length)]);
//        }
//        TestAdapter adapter = new TestAdapter(list);
//        mRecyclerView.setAdapter(adapter);
        view.findViewById(R.id.mTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(SecondActivity.class);
            }
        });
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
//                addChildFragment(R.id.mContainer, new TestFragment2());
            }
        },1500);
    }

    private static class TestAdapter extends RecyclerView.Adapter<TestAdapter.MyViewHolder> {
        private List<String> data;

        TestAdapter(List<String> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_test, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.textView.setText(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        static class MyViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            MyViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.mTextView);
            }
        }
    }
}
