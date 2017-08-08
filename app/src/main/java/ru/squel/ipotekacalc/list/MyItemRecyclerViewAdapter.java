package ru.squel.ipotekacalc.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.squel.ipotekacalc.R;
import ru.squel.ipotekacalc.data.MonthlyData;
import ru.squel.ipotekacalc.list.ItemFragment.OnListFragmentInteractionListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MonthlyData} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
  */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<MonthlyData> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context ctx;

    public MyItemRecyclerViewAdapter(Context ctx, List<MonthlyData> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        this.ctx = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mItem = mValues.get(position);

        holder.mIdView.setText(String.valueOf(position + 1));
        holder.mMonthPay.setText(ctx.getString(R.string.monthlypay_title, mValues.get(position).getMonthlyPay()));
        holder.mDebtView.setText(ctx.getString(R.string.monthlydebt_title, mValues.get(position).getMonthlyDebt()));
        holder.mPercenttView.setText(ctx.getString(R.string.monthlypercent_title, mValues.get(position).getMonthlyPercent()));
        holder.mDebtStatView.setText(mValues.get(position).getCurrentDebtSize() + "/" + mValues.get(position).getTotalDebtSize());
        holder.mPercentStatView.setText(mValues.get(position).getCurrentPercentSize() + "/" + mValues.get(position).getTotalPercentSize());

        holder.mIdView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mMonthPay;
        public final TextView mDebtView;
        public final TextView mPercenttView;
        public final TextView mDebtStatView;
        public final TextView mPercentStatView;
        public MonthlyData mItem;

        public ViewHolder(View view) {
            super(view);
            mIdView = (TextView) view.findViewById(R.id.id);
            mMonthPay = (TextView) view.findViewById(R.id.monthly_pay);
            mDebtView = (TextView) view.findViewById(R.id.monthly_debt);
            mPercenttView = (TextView) view.findViewById(R.id.monthly_percent);
            mDebtStatView = (TextView) view.findViewById(R.id.monthly_debt_stat);
            mPercentStatView = (TextView) view.findViewById(R.id.monthly_percent_stat);
        }
    }
}
