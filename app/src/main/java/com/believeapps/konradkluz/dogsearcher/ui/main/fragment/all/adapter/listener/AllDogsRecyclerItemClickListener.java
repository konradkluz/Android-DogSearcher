package com.believeapps.konradkluz.dogsearcher.ui.main.fragment.all.adapter.listener;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by konradkluz on 12/09/2017.
 */

public class AllDogsRecyclerItemClickListener extends RecyclerView.SimpleOnItemTouchListener {

    private static final String TAG = "RecyclerItemClickListen";

    public interface OnRecyclerClickListener {
        void onItemClick(View view, int position);

        void onLongItemClick(View view, int position);
    }

    private final OnRecyclerClickListener mOnRecyclerClickListener;
    private final GestureDetectorCompat mGestureDetector;

    public AllDogsRecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnRecyclerClickListener onRecyclerClickListener) {
        mOnRecyclerClickListener = onRecyclerClickListener;
        mGestureDetector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                View childView = getChildView(recyclerView, e);
                if (childView != null && mOnRecyclerClickListener != null) {
                    Log.d(TAG, "onSingleTapUp: calling listener.onItemClick");
                    mOnRecyclerClickListener.onItemClick(childView, recyclerView.getChildAdapterPosition(childView));
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Log.d(TAG, "onLongPress: started");
                View childView = getChildView(recyclerView, e);
                if (childView != null && mOnRecyclerClickListener != null) {
                    Log.d(TAG, "onLongPress: calling listener.onItemLongClick");
                    mOnRecyclerClickListener.onLongItemClick(childView, recyclerView.getChildAdapterPosition(childView));
                }
            }
        });
    }

    private View getChildView(RecyclerView recyclerView, MotionEvent e){
        return recyclerView.findChildViewUnder(e.getX(), e.getY());
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        Log.d(TAG, "onInterceptTouchEvent: started");
        if (mGestureDetector != null) {
            boolean result = mGestureDetector.onTouchEvent(e);
            Log.d(TAG, "onInterceptTouchEvent: returned" + result);
            return result;
        }
        return false;
    }
}
