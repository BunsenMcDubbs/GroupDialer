package co.andrewdai.android.groupdialer;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.andrewdai.android.groupdialer.CalleeFragment.OnListFragmentInteractionListener;

import java.util.Iterator;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Callee} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyCalleeRecyclerViewAdapter extends RecyclerView.Adapter<MyCalleeRecyclerViewAdapter.ViewHolder> {

    private final List<Callee> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyCalleeRecyclerViewAdapter(List<Callee> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_callee, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.setCalleeInfo(mValues.get(position));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
        if (mValues.get(position).wasCalled()) {
            holder.mView.setBackgroundColor(Color.GRAY);
        } else {
            holder.mView.setBackgroundColor(Color.WHITE);
        }
    }

    public List<Callee> getCallees() {
        return mValues;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements Callee.CalleeStateListener{
        public final View mView;
        public final TextView mNameView;
        public final TextView mPhoneView;
        public Callee mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.callee_name);
            mPhoneView = (TextView) view.findViewById(R.id.callee_phone);
        }

        public void setCalleeInfo(Callee c) {
            mItem = c;
            mNameView.setText(c.fullname);
            mPhoneView.setText(c.phone);

            mItem.attach(this);
        }

        public void refresh() {
            System.out.println("Refresh!!");
            if (mItem.wasCalled()) {
                mView.setBackgroundColor(Color.GRAY);
            } else {
                mView.setBackgroundColor(Color.WHITE);
            }
        }

        @Override
        public String toString() {
            return super.toString() + mItem.toString();
        }

        @Override
        public void calleeCalledStateChanged(Callee c, boolean called) {
            refresh();
        }
    }
}
