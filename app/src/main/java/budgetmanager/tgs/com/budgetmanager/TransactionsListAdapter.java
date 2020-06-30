package budgetmanager.tgs.com.budgetmanager;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import budgetmanager.tgs.com.budgetmanager.Model.Transaction;
import budgetmanager.tgs.com.budgetmanager.Model.TransactionGroup;

public class TransactionsListAdapter extends BaseExpandableListAdapter {


    Context context;
    List<HashMap<String,String>> listGroupData;
    HashMap<Integer, List<String>> listHashMap;
    List<TransactionGroup> transactionGroupList;



    public TransactionsListAdapter(Context context, List<HashMap<String,String>> listDataHeader, HashMap<Integer, List<String>> listHashMap, List<TransactionGroup> transactionGroupList){
        this.context = context;
        this.listGroupData = listDataHeader;
        this.listHashMap = listHashMap;
        this.transactionGroupList = transactionGroupList;
    }

    @Override
    public int getGroupCount() {
//        return listGroupData.size();
        return transactionGroupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
//        return listHashMap.get(listDataHeader.get(groupPosition)).size();
//        return listHashMap.get(groupPosition).size();
        return transactionGroupList.get(groupPosition).getTransactionsList().size();
    }


    @Override
    public Object getGroup(int groupPosition) {
//        return listGroupData.get(groupPosition);
        return transactionGroupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
//        return listHashMap.get(listDataHeader.get(groupPosition)).get(childPosition);
//        return listHashMap.get(groupPosition).get(childPosition);
        return transactionGroupList.get(groupPosition).getTransactionsList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
//        HashMap<String, String> groupData = (HashMap) getGroup(groupPosition);
        TransactionGroup groupData = (TransactionGroup) getGroup(groupPosition);
        String date = groupData.getDate();//groupData.get("Date");
        String day = groupData.getDay();//groupData.get("Day");
        String month = groupData.getMonth();//groupData.get("Month");
        String amount = groupData.getYear();//groupData.get("Amount");

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group_transaction, null);
        }

        TextView lblGroupDate = convertView.findViewById(R.id.lblListGroupDate);
        lblGroupDate.setTypeface(null,Typeface.BOLD);
        lblGroupDate.setText(date);
        TextView lblGroupDay = convertView.findViewById(R.id.lblListGroupDay);
        lblGroupDay.setText(day);
        TextView lblGroupMonth = convertView.findViewById(R.id.lblListGroupMonth);
        lblGroupMonth.setText(month);
        TextView lblGroupAmount = convertView.findViewById(R.id.lblListGroupAmount);
        lblGroupAmount.setText(amount);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String listItemTitle = (String) getChild(groupPosition,childPosition);
        Transaction transaction = (Transaction) getChild(groupPosition, childPosition);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        TextView lblCategory = convertView.findViewById(R.id.lblListItemTransactionCategory);
        lblCategory.setText(transaction.getTransactionCategory());
        TextView lblAccount = convertView.findViewById(R.id.lblListItemTransactionAccount);
        lblAccount.setText(transaction.getTransactionAccount());
        TextView lblAmount = convertView.findViewById(R.id.lblListItemTransactionAmount);
        lblAmount.setText(""+transaction.getTransactionAmount());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
