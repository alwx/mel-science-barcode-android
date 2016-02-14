package me.alwx.appbase.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.alwx.appbase.R;

/**
 * @author alwx (http://alwx.me)
 * @version 1.0
 */
public class SimpleRecyclerAdapter<T> extends RecyclerView.Adapter<SimpleRecyclerAdapter.Holder> {
    private List<T> mObjectList;
    private OnClickListener<T> mOnClickListener;
    private int mLayoutRes;
    private HolderInjector<T> mInjector;

    public SimpleRecyclerAdapter(@NonNull List<T> objectList,
                                 int layoutRes,
                                 @NonNull HolderInjector<T> injector) {
        mObjectList = objectList;
        mLayoutRes = layoutRes;
        mInjector = injector;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(mLayoutRes, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final T obj = mObjectList.get(position);

        mInjector.inject(obj, holder);

        if (mOnClickListener != null) {
            holder.mRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickListener.onClick(obj);
                }
            });
            holder.mRoot.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnClickListener.onLongClick(obj);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mObjectList.size();
    }

    public T getItem(int position) {
        return mObjectList.get(position);
    }

    public List<T> getItems() {
        return mObjectList;
    }

    public void setItems(List<T> objectList) {
        mObjectList = objectList;
        notifyDataSetChanged();
    }

    public void setOnClickListener(OnClickListener<T> onClickListener) {
        mOnClickListener = onClickListener;
    }

    public static class Holder extends RecyclerView.ViewHolder {
        @Bind(R.id.text)
        public TextView mText;

        @Bind(R.id.description)
        public TextView mDescription;

        public View mRoot;

        public Holder(View view) {
            super(view);
            mRoot = view;
            ButterKnife.bind(this, view);
        }
    }

    public interface OnClickListener<T> {
        void onClick(T object);

        void onLongClick(T object);
    }

    public interface HolderInjector<T> {
        void inject(T object, Holder holder);
    }
}