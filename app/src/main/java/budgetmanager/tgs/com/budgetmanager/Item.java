package budgetmanager.tgs.com.budgetmanager;

import android.view.LayoutInflater;
import android.view.View;

public interface Item {
    enum ITEM_TYPE{EXPENSE,INCOME}
    int getViewType();
    View getView(LayoutInflater inflater, View convertView);
}
