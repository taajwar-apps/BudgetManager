package budgetmanager.tgs.com.budgetmanager;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import budgetmanager.tgs.com.budgetmanager.Model.Transaction;
import budgetmanager.tgs.com.budgetmanager.Model.TransactionGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTransaction extends Fragment {


    Button _btnAddTransaction;
    ExpandableListView listView;
    TransactionsListAdapter listAdapter;
    List<TransactionGroup> transactionGroupList;
    List<HashMap<String, String>> listDataHeader;
    HashMap<Integer, List<String>> listHashMap;

    ActionBar _toolbar;
    BottomNavigationView _navigationView;
    FragmentAddTransactions _fragmentAddTransactions;
    FragmentAddTransactionExpense _fragmentAddTransactionExpense;
    FragmentAddTransactionIncome _fragmentAddTransactionIncome;
    FragmentAddTransactionTransfer _fragmentAddTransactionTransfer;

    public FragmentTransaction() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_transaction, null);
        ((TextView)rootView.findViewById(R.id.custom_header)).setText("Transactions");
        listView = rootView.findViewById(R.id.expListView);
//        _toolbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
//        _toolbar.setTitle("Expense Transaction");
        _fragmentAddTransactions = new FragmentAddTransactions();
        _fragmentAddTransactionExpense = new FragmentAddTransactionExpense();
        _fragmentAddTransactionIncome = new FragmentAddTransactionIncome();
        _fragmentAddTransactionTransfer = new FragmentAddTransactionTransfer();
        initData();
        listAdapter = new TransactionsListAdapter(getContext(),listDataHeader, listHashMap, transactionGroupList);
        listView.setAdapter(listAdapter);

        _btnAddTransaction = rootView.findViewById(R.id.add_transaction);
        _btnAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showAddTransactionPopupWindow();
                setFragment(_fragmentAddTransactions);
                Common.hideNavigationView();
            }
        });

        //Calender Code
//        Button button = rootView.findViewById(R.id.button);
        final CalendarView view = rootView.findViewById(R.id.calendarView);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar calendar = Calendar.getInstance();
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//                try {
//                    Toast.makeText(getContext(),formatter.format(calendar.getTime()),Toast.LENGTH_LONG).show();
//                    Date date = formatter.parse("2018-12-15");
//                    calendar.setTime(date);
//                    calendar.add(Calendar.DATE, 5);
//
//                    Toast.makeText(getContext(),formatter.format(calendar.getTime()),Toast.LENGTH_LONG).show();
//
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
//                }
//            }
//        });
        view.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView arg0, int year, int month, int date) {
                Toast.makeText(getContext(),date+ "/"+month+"/"+year,Toast.LENGTH_LONG).show();
            }
        });
        //End Calender Code

        Toast.makeText(getContext(),"Day : "+getDayOfWeek(),Toast.LENGTH_LONG).show();
//        setHasOptionsMenu(true);
//        Common.setCustomActionBar((AppCompatActivity)getActivity(), container, inflater, "Transactions", 0);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
//        Common.setCustomActionBar((AppCompatActivity)getActivity(), (ViewGroup) getView(), getLayoutInflater(), "Transactions", 0);
//        Common.setCustomActionBar((AppCompatActivity)getActivity(), ((ViewGroup)_toolbar.getCustomView().getParent()), getActivity().getLayoutInflater(), "Transactions", 0);
    }

    @Override
    public void onResume() {
        super.onResume();
//        Common.setCustomActionBar((AppCompatActivity)getActivity(), (ViewGroup) getView(), getLayoutInflater(), "Transactions", 0);
    }

    private  void setFragment(Fragment fragment){
        android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_transactions,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    String getDayOfWeek(){
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_WEEK);
        String dayName = null;
        switch (day){
            case Calendar.SUNDAY:
                dayName = "Sun";
                break;
            case Calendar.MONDAY:
                dayName = "Mon";
                break;
            case Calendar.TUESDAY:
                dayName = "Tue";
                break;
            case Calendar.WEDNESDAY:
                dayName = "Wed";
                break;
            case Calendar.THURSDAY:
                dayName = "Thi";
                break;
            case Calendar.FRIDAY:
                dayName = "Fri";
                break;
            case Calendar.SATURDAY:
                dayName = "Sat";
                break;
        }

        return dayName;
    }

    private void initData() {
        listDataHeader = new ArrayList<HashMap<String, String>>();
        listHashMap = new HashMap<Integer, List<String>>();
        transactionGroupList = new ArrayList<TransactionGroup>();

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Transactions");
//        String id = dbRef.push().getKey();
//        dbRef.child(id).setValue("SSSSSSS");

        for (int i =1; i<= 5; i++) {
//            HashMap<String, String> hash = new HashMap<String, String>();
//            hash.put("Date", "24 "+i);
//            hash.put("Day", "Monday "+i);
//            hash.put("Month", "December 2018 "+i);
//            hash.put("Amount", "2400 "+i);
//            listDataHeader.add(hash);
            dbRef = FirebaseDatabase.getInstance().getReference("Transactions");
            TransactionGroup transactionGroup = new TransactionGroup();
            transactionGroup.setDate("25 "+i);
            transactionGroup.setDay("Tuesday "+i);
            transactionGroup.setMonth("December "+i);
            transactionGroup.setDay("2018 "+i);
//            String id = dbRef.push().getKey();
//            dbRef.child(id).setValue(transactionGroup);
//            dbRef = FirebaseDatabase.getInstance().getReference("Transactions").child(id);

            DatabaseReference childRef = dbRef.child(transactionGroup.getDate());
            childRef.setValue(transactionGroup);
//            List<Transaction> transactionsList = new ArrayList<Transaction>();
            for (int j=1; j<=i; j++){
                Transaction transaction = new Transaction();
                transaction.setTransactionCategory("Fuel");
                transaction.setTransactionAccount("Cash");
                transaction.setTransactionAmount(200);
                transaction.setTransactionType(Transaction.TRANSACTION_TYPE.EXPENSE);
                transactionGroup.getTransactionsList().add(transaction);
                String idt = childRef.push().getKey();
                childRef.child(idt).setValue(transaction);
//                childRef.setValue(transaction);
            }

            transactionGroupList.add(transactionGroup);
        }
        Calendar c = Calendar.getInstance();

//        listDataHeader.add("Accounts");
//        listDataHeader.add("Categories");
//        listDataHeader.add("Budget");
        List<String> transactions = new ArrayList<>();
        transactions.add("Transactions 1");
        transactions.add("Transactions 2");
        List<String> accounts = new ArrayList<>();
        accounts.add("Accounts 1");
        accounts.add("Accounts 2");
        accounts.add("Accounts 3");
        List<String> categories = new ArrayList<>();
        categories.add("Categories 1");
        categories.add("Categories 2");
        categories.add("Categories 3");
        categories.add("Categories 4");
        List<String> budget = new ArrayList<>();
        budget.add("Budget 1");
        budget.add("Budget 2");
        budget.add("Budget 3");
        budget.add("Budget 4");
        budget.add("Budget 5");
        listHashMap.put(0, transactions);
        listHashMap.put(1, accounts);
        listHashMap.put(2, categories);
        listHashMap.put(3, budget);

//        View listGroupView = listAdapter.getGroupView()

    }

}
