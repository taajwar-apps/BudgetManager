package budgetmanager.tgs.com.budgetmanager;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import budgetmanager.tgs.com.budgetmanager.Model.Account;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAddTransactions extends Fragment {


    Button _btnAddAccount;
    Button _btnSaveAccount;
    Button _btnUpdateAccount;
    Button _btnDeleteAccount;
    Button _btnCancel;

    //    ListView _listViewExpenseCategory;
    ListView _listViewAccounts;
    BaseAdapter _listAdapter;

    //    List<Category> _listExpenseCategories;
    List<Item> _listAccounts;
    Account _selectedAccount;

    BottomNavigationView _navigationView;
    FragmentAddTransactionExpense _fragmentAddTransactionExpense;
    FragmentAddTransactionIncome _fragmentAddTransactionIncome;
    FragmentAddTransactionTransfer _fragmentAddTransactionTransfer;

    public FragmentAddTransactions() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_add_transactions, container, false);
        _listViewAccounts = view.findViewById(R.id.list_view_accounts);
        _listAccounts = new ArrayList<Item>();
        _btnAddAccount = view.findViewById(R.id.btnAddAccount);

        _fragmentAddTransactionExpense = new FragmentAddTransactionExpense();
        _fragmentAddTransactionIncome = new FragmentAddTransactionIncome();
        _fragmentAddTransactionTransfer = new FragmentAddTransactionTransfer();

        _navigationView = (BottomNavigationView) view.findViewById(R.id.navigation_view_transactions);
        _navigationView.setSelectedItemId(R.id.navigation_expense);
        _navigationView.setOnNavigationItemSelectedListener(_navigationItemSelectedListener);

        setFragment(_fragmentAddTransactionExpense);
        return view;//inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    BottomNavigationView.OnNavigationItemSelectedListener _navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener(){
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.navigation_expense:
                    setFragment(_fragmentAddTransactionExpense);
                    return true;
                case R.id.navigation_income:
                    setFragment(_fragmentAddTransactionIncome);
                    return true;
                case R.id.navigation_transfer:
                    setFragment(_fragmentAddTransactionTransfer);
                    return true;
            }
            return false;
        }
    };

    private  void setFragment(Fragment fragment){
        android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_add_transaction,fragment);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
