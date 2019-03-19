package budgetmanager.tgs.com.budgetmanager;


import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import budgetmanager.tgs.com.budgetmanager.Model.Account;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAddTransactionExpense extends Fragment {


    Button _btnAddExpense;
    Button _btnAddExpenseAndClose;
    Button _btnCancel;


    public FragmentAddTransactionExpense() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_add_transaction_expense, container, false);
//        Common.setCustomActionBar((AppCompatActivity)getActivity(), container, inflater, "", R.layout.layout_add_expense_action_bar);
//        _listViewExpenseCategory = (ListView) view.findViewById(R.id.list_view_expense_categories);
//        _listViewAccounts = view.findViewById(R.id.list_view_accounts);
//        _listExpenseCategories = new ArrayList<Category>();
//        _listAccounts = new ArrayList<Item>();
        _btnAddExpense = view.findViewById(R.id.btnAddExpense);
        _btnAddExpenseAndClose = view.findViewById(R.id.btnAddExpenseAndClose);

        _btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllFields();
            }
        });

        _btnAddExpenseAndClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                Common.showNavigationView();
            }
        });

        _btnCancel = view.findViewById(R.id.btnBack);
//        _btnCancel = getActivity().getActionBar().getCustomView().findViewById(R.id.btnBack);
        _btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), "HAHAHAHAH", Toast.LENGTH_SHORT).show();
                getFragmentManager().popBackStack();
                Common.showNavigationView();
//                Common.setCustomActionBar((AppCompatActivity)getActivity(), container, inflater, "Transactions", 0);
            }
        });

//        _listViewAccounts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                Toast.makeText(getContext(), "Touched Item : "+position, Toast.LENGTH_SHORT).show();
//                showUpdateCategoryPopupWindow(position);
//            }
//        });

//        showList();
//        LayoutInflater inflater = getLayoutInflater();
//        getActivity().setTitle("Accounts");
//        ActionBar actionBar = getActivity().getActionBar();
//        setCustomActionBar(inflater);
//        Common.setCustomActionBar((AppCompatActivity)getActivity(), container, inflater, "Accounts");
        // Inflate the layout for this fragment
//        setHasOptionsMenu(true);
//        ActionBar bar = getActivity().getActionBar();

//        Common.setCustomActionBar((AppCompatActivity)getActivity(), container, inflater, "Transactions", R.layout.layout_add_expense_action_bar);
        return view;//inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
//        Common.setCustomActionBar((AppCompatActivity)getActivity(), (ViewGroup) getView(), getLayoutInflater(), "Transactions", R.layout.layout_add_expense_action_bar);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        Common.setCustomActionBar((AppCompatActivity)getActivity(), (ViewGroup) getView(), getLayoutInflater(), "Transactions", R.layout.layout_add_expense_action_bar);
        super.onViewStateRestored(savedInstanceState);
    }

    void datePicker(){
//        eReminderDate.setOnClickListener(new OnClickListener() {

//            @Override
//            public void onClick(View v) {
        // TODO Auto-generated method stub
        //To show current date in the datepicker
        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker;
        mDatePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                // TODO Auto-generated method stub
                /*      Your code   to get date and time    */
                selectedmonth = selectedmonth + 1;
//                        eReminderDate.setText("" + selectedday + "/" + selectedmonth + "/" + selectedyear);
                Toast.makeText(getContext(),"" + selectedday + "/" + selectedmonth + "/" + selectedyear,Toast.LENGTH_LONG).show();
            }
        }, mYear, mMonth, mDay);
        mDatePicker.setTitle("Select Date");
        mDatePicker.show();
//            }
//        });
    }

    void timePicker(){
//        ReminderTime.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                        eReminderTime.setText( selectedHour + ":" + selectedMinute);
                Toast.makeText(getContext(),selectedHour + ":" + selectedMinute,Toast.LENGTH_LONG).show();
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

//            }
//        });
    }


    private void saveAccount(View popupView, final PopupWindow popupWindow){
        String accountTitle = ((EditText)popupView.findViewById(R.id.editTextAccountTitle)).getText().toString();
//        String parentCategory = ((EditText)popupView.findViewById(R.id.editTextParentCategory)).getText().toString();
        String accountBalance = ((EditText)popupView.findViewById(R.id.editTextAccountBalance)).getText().toString();
        Account account = new Account(accountTitle, /*parentCategory,*/ accountBalance);
//        RadioGroup radioCategoryGroup = (RadioGroup) popupView.findViewById(R.id.radioCategoryGroup);
//        int selectedRadioBtnID = radioCategoryGroup.getCheckedRadioButtonId();
//        Category.CATEGORY_TYPE categoryType = Category.CATEGORY_TYPE.EXPENSE;
//        switch (selectedRadioBtnID){
//            case R.id.radioBtnExpense:
//                categoryType = Category.CATEGORY_TYPE.EXPENSE;
//                break;
//            case R.id.radioBtnIncome:
//                categoryType = Category.CATEGORY_TYPE.INCOME;
//                break;
//            default:
//                ShowToast("Please Select Category Type");
//                return;
//        }
        account.saveAccount(new Callback() {
            @Override
            public void apply(String msg) {
                Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
//                refreshDataAdapter();
            }
        });
        popupWindow.dismiss();
    }

    private void updateAccount(View popupView, final PopupWindow popupWindow){
        String accountTitle = ((EditText)popupView.findViewById(R.id.editTextAccountTitle)).getText().toString();
//        String parentCategory = ((EditText)popupView.findViewById(R.id.editTextParentCategory)).getText().toString();
        String accountBalance = ((EditText)popupView.findViewById(R.id.editTextAccountBalance)).getText().toString();
//        _selectedAccount.set_accountTitle(accountTitle);
//        selectedCategory.set_parentCategory(parentCategory);
//        _selectedAccount.set_accountBalance(accountBalance);
//        RadioGroup radioCategoryGroup = (RadioGroup) popupView.findViewById(R.id.radioCategoryGroup);
//        int selectedRadioBtnID = radioCategoryGroup.getCheckedRadioButtonId();
//        Category.CATEGORY_TYPE categoryType = Category.CATEGORY_TYPE.EXPENSE;
//        switch (selectedRadioBtnID){
//            case R.id.radioBtnExpense:
//                categoryType = Category.CATEGORY_TYPE.EXPENSE;
//                break;
//            case R.id.radioBtnIncome:
//                categoryType = Category.CATEGORY_TYPE.INCOME;
//                break;
//            default:
//                ShowToast("Please Select Category Type");
//                return;
//        }
//        _selectedAccount.updateAccount(new Callback() {
//            @Override
//            public void apply(String msg) {
//                Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
//                popupWindow.dismiss();
////                refreshDataAdapter();
//            }
//        });
//        popupWindow.dismiss();
    }

    void clearAllFields(){

    }

    private void ShowToast(String msg){
        Toast.makeText(getContext(),msg, Toast.LENGTH_LONG).show();
    }


}
