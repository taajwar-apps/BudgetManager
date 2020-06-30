package budgetmanager.tgs.com.budgetmanager;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;

import budgetmanager.tgs.com.budgetmanager.Model.CategoryGroup;

public class HeaderBudgetSummary implements ICatItem {

    private final String _title;
//    HEADER_TYPE _headerType;

    float _totalBudgetAllocated; // Allocated budget or expected income
    float _totalBudgetUsed; // Budget consumed or obtained income

    public HeaderBudgetSummary(String title, float totalBudgetAllocated, float totalBudgetUsed) {
        this._title = title;
        _totalBudgetAllocated = totalBudgetAllocated;
        _totalBudgetUsed = totalBudgetUsed;
//        this._headerType = headerType;
    }

//    enum HEADER_TYPE{EXPENSE,INCOME}

//    public HEADER_TYPE get_headerType() {
//        return _headerType;
//    }

//    public void set_headerType(HEADER_TYPE _headerType) {
//        this._headerType = _headerType;
//    }

    @Override
    public int getViewType() {
        return FragmentBudgetSummary.RowType.HEADER_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view = inflater.inflate(R.layout.item_group_budget_summary, null);
        TextView tvHeaderTitle = view.findViewById(R.id.tvBudgetGroupCat);
        TextView tvGroupBudgetConsumed = view.findViewById(R.id.tvGroupBudgetConsumed);
        TextView tvGroupBudgetAllocated = view.findViewById(R.id.tvGroupBudgetAllocated);
        TextView tvGroupBudgetRemaining = view.findViewById(R.id.tvGroupBudgetRemaining);

        tvHeaderTitle.setText(_title);
        tvHeaderTitle.setTextColor(Color.BLUE);

        tvGroupBudgetAllocated.setText("/"+_totalBudgetAllocated);
        tvGroupBudgetConsumed.setText(_totalBudgetUsed+"");
        float totalBudgetRemaining = _totalBudgetAllocated-_totalBudgetUsed;
        float percentageRemaining = (totalBudgetRemaining/_totalBudgetAllocated)*100;
        tvGroupBudgetRemaining.setText((int) percentageRemaining+"% Remaining Rs. "+ totalBudgetRemaining);
        ProgressBar progressBar = view.findViewById(R.id.budgetGroupProgress);
        progressBar.setProgress((int) (100-percentageRemaining));

        return view;
    }
    public String getName(){return _title;}
}
