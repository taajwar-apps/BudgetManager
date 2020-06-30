package budgetmanager.tgs.com.budgetmanager;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
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
public class FragmentAddTransactionTransfer extends Fragment {


    Button _btnAddAccount;
    Button _btnSaveAccount;
    Button _btnUpdateAccount;
    Button _btnDeleteAccount;
    Button _btnCancel;

    //    ListView _listViewExpenseCategory;
    ListView _listViewAccounts;
    BaseAdapter _listAdapter;

    //    List<Category> _listExpenseCategories;
    List<ICatItem> _listAccounts;
    Account _selectedAccount;

    public FragmentAddTransactionTransfer() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_add_transaction_transfer, container, false);

//        _listViewExpenseCategory = (ListView) view.findViewById(R.id.list_view_expense_categories);
        _listViewAccounts = view.findViewById(R.id.list_view_accounts);
//        _listExpenseCategories = new ArrayList<Category>();
        _listAccounts = new ArrayList<ICatItem>();
        _btnAddAccount = view.findViewById(R.id.btnAddAccount);
//        _btnAddAccount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showAddAccountPopupWindow();
//            }
//        });

        _btnCancel = view.findViewById(R.id.btnBack);
        _btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "HAHAHAHAH", Toast.LENGTH_SHORT).show();
                getFragmentManager().popBackStack();
                Common.showNavigationView();
            }
        });

//        _listViewAccounts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                Toast.makeText(getContext(), "Touched ICatItem : "+position, Toast.LENGTH_SHORT).show();
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
        return view;//inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        showList();
    }

    private void showAddAccountPopupWindow(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.add_account_popup_window, null, false);
        final PopupWindow popupWindow = new PopupWindow(popupView,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,true);
//                pw.setContentView(popupView);
        popupWindow.showAtLocation(popupView, Gravity.CENTER,0,0);
        _btnSaveAccount = popupView.findViewById(R.id.btnSaveAccount);
        _btnUpdateAccount = popupView.findViewById(R.id.btnUpdateAccount);
        _btnDeleteAccount = popupView.findViewById(R.id.btnDeleteAccount);
        _btnCancel = popupView.findViewById(R.id.btnCancel);
        _btnSaveAccount.setVisibility(View.VISIBLE);
        _btnUpdateAccount.setVisibility(View.INVISIBLE);
        _btnDeleteAccount.setVisibility(View.INVISIBLE);
        _btnSaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAccount(popupView, popupWindow);
            }
        });
        _btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    private void showUpdateAccountPopupWindow(Account account){
//        if(_listAccounts.get(position).getClass().equals(Header.class))
//            return;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.add_account_popup_window, null, false);
        final PopupWindow popupWindow = new PopupWindow(popupView,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,true);
//                pw.setContentView(popupView);
        popupWindow.showAtLocation(popupView, Gravity.CENTER,0,0);
        _selectedAccount = account;//(Account)_listAccounts.get(position);
//        Toast.makeText(getContext(), "Category Type : "+selectedCategory.get_categoryType(), Toast.LENGTH_SHORT).show();
        EditText editTextAccountTitle = popupView.findViewById(R.id.editTextAccountTitle);
//        EditText editTextParentCat = (EditText)popupView.findViewById(R.id.editTextParentCategory);
        EditText editTextAccountBalance = popupView.findViewById(R.id.editTextAccountBalance);
        editTextAccountTitle.setText(_selectedAccount.get_accountTitle());
//        editTextParentCat.setText(selectedCategory.get_parentCategory());
        editTextAccountBalance.setText(_selectedAccount.get_accountBalance());
//        Category.CATEGORY_TYPE categoryType = _selectedAccount.get_categoryType();
//        RadioGroup radioCategoryGroup = (RadioGroup) popupView.findViewById(R.id.radioCategoryGroup);
//        if(categoryType.equals(Category.CATEGORY_TYPE.EXPENSE))
//            radioCategoryGroup.check(R.id.radioBtnExpense);
//        else if (categoryType.equals(Category.CATEGORY_TYPE.INCOME))
//            radioCategoryGroup.check(R.id.radioBtnIncome);


//        int selectedRadioBtnID = radioCategoryGroup.getCheckedRadioButtonId();
        _btnSaveAccount = popupView.findViewById(R.id.btnSaveAccount);
        _btnUpdateAccount = popupView.findViewById(R.id.btnUpdateAccount);
        _btnDeleteAccount = popupView.findViewById(R.id.btnDeleteAccount);
        _btnCancel = popupView.findViewById(R.id.btnCancel);
        _btnSaveAccount.setVisibility(View.INVISIBLE);
        _btnUpdateAccount.setVisibility(View.VISIBLE);
        _btnDeleteAccount.setVisibility(View.VISIBLE);

        _btnUpdateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAccount(popupView, popupWindow);
            }
        });
        _btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        _btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _selectedAccount.deleteAccount(new Callback(){

                    @Override
                    public void apply(String msg) {
                        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
                        popupWindow.dismiss();
                        refreshDataAdapter();
                    }
                    @Override
                    public void result(float result) {

                    }
                });
            }
        });
    }

//    private interface Callback{
//        void apply(String msg);
//    }

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
                refreshDataAdapter();
            }
            @Override
            public void result(float result) {

            }
        });
        popupWindow.dismiss();
    }

    private void updateAccount(View popupView, final PopupWindow popupWindow){
        String accountTitle = ((EditText)popupView.findViewById(R.id.editTextAccountTitle)).getText().toString();
//        String parentCategory = ((EditText)popupView.findViewById(R.id.editTextParentCategory)).getText().toString();
        String accountBalance = ((EditText)popupView.findViewById(R.id.editTextAccountBalance)).getText().toString();
        _selectedAccount.set_accountTitle(accountTitle);
//        selectedCategory.set_parentCategory(parentCategory);
        _selectedAccount.set_accountBalance(accountBalance);
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
        _selectedAccount.updateAccount(new Callback() {
            @Override
            public void apply(String msg) {
                Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                refreshDataAdapter();
            }
            @Override
            public void result(float result) {

            }
        });
//        popupWindow.dismiss();
    }

    void showList(){
//        loadAccountsList();
        populateListView();
//        _listAdapter = new ListViewAdapter(getContext(), listExpenseCategories, R.layout.category_item_layout);
//        _listViewExpenseCategory.setAdapter(_listAdapter);
    }

    void loadAccountsList(){
//        DatabaseReference accountsDbRef = FirebaseDatabase.getInstance().getReference("Accounts");
        String currentUserId = Common._firebaseAuth.getCurrentUser().getUid();
//        DatabaseReference userDbRef = accountsDbRef.child(currentUserId);

        Query query = FirebaseDatabase.getInstance().getReference("Accounts").child(currentUserId);
        FirebaseListOptions<Account> options = new FirebaseListOptions.Builder<Account>().setLayout(R.layout.account_item_layout).setQuery(query, Account.class).setLifecycleOwner(this).build();
        FirebaseListAdapter<Account> adapter = new FirebaseListAdapter<Account>(options) {
            @Override
            protected void populateView(View v, final Account model, final int position) {
                TextView textViewAccountTitle = v.findViewById(R.id.textViewAccountTitle);
                TextView textViewAccountBalance = v.findViewById(R.id.textViewAccountBalance);
                textViewAccountTitle.setText(model.get_accountTitle());
                textViewAccountBalance.setText(model.get_accountBalance());

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showUpdateAccountPopupWindow(model);
                    }
                });
            }
        };
//        LayoutInflater inflater = getLayoutInflater();
//        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.category_header_layout, _listViewAccounts,false);
//        _listViewAccounts.addHeaderView(header);
//        _listViewAccounts.setAdapter(adapter);


//        expenseCategoriesDbRef.addValueEventListener(new ValueEventListener() {
//        userDbRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                _listAccounts.clear();
//                List<ICatItem> listAccounts = new ArrayList<>();
////                List<ICatItem> listIncomeCategories = new ArrayList<>();
////                _listCategories.add(new Header("Expense Categories", Header.HEADER_TYPE.EXPENSE));
//                _listAccounts.add(new Header("Accounts"));
//                for(DataSnapshot accountSnapshot : dataSnapshot.getChildren()){
//                    Account account = accountSnapshot.getValue(Account.class);
//                    _listAccounts.add(account);
//                }
//
////                _listAdapter = new ListViewAdapter(getContext(), _listAccounts);
//                LayoutInflater inflater = getLayoutInflater();
//                ViewGroup header = (ViewGroup) inflater.inflate(R.layout.category_header_layout, _listViewAccounts,false);
//                _listViewAccounts.addHeaderView(header);
////                _listViewAccounts.setAdapter(_listAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    void populateListView(){
//        boolean updateAdapter = false;
//        if(Common._listAccounts.size() > 0)
//            updateAdapter = true;
//        else {
            _listAdapter = new BaseAdapter() {
                @Override
                public int getCount() {
                    return Common._listAccounts.size();
                }

                @Override
                public Account getItem(int position) {
                    return Common._listAccounts.get(position);
                }

                @Override
                public long getItemId(int position) {
                    return position;
                }

                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    View view = null;
                    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    view = getItem(position).getView(inflater, convertView);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                                showUpdateCategoryPopupWindow(position);
                            Account account = getItem(position);
                            showUpdateAccountPopupWindow(account);
                        }
                    });
                    return view;
                }
            };
//        }
//        _listAllCategories.clear();
//        _listAllCategories.add(new Header("Expense Categories"));
//        for (ICatItem parentCategory:Common._listCategoriesGroup){
//            List<ICatItem> categories = new ArrayList<ICatItem>();
//            if(((CategoryGroup)parentCategory).get_categoryType().equals(ICatItem.ITEM_TYPE.EXPENSE)) {
//                for (ICatItem category : Common._listCategories) {
//                    if (((Category) category).get_categoryType().equals(ICatItem.ITEM_TYPE.EXPENSE))
//                        if (((Category) category).get_parentCategory().equals(((CategoryGroup) parentCategory).get_catID()))
//                            categories.add(category);
//                }
//            }
//            if(!categories.isEmpty()){
//                _listAllCategories.add(parentCategory);
//                _listAllCategories.addAll(categories);
//            }
//        }
//        _listAllCategories.add(new Header("Income Categories"));
//        for (ICatItem parentCategory:Common._listCategoriesGroup){
//            List<ICatItem> categories = new ArrayList<ICatItem>();
//            if(((CategoryGroup)parentCategory).get_categoryType().equals(ICatItem.ITEM_TYPE.INCOME)) {
//                for (ICatItem category : Common._listCategories) {
//                    if (((Category) category).get_categoryType().equals(ICatItem.ITEM_TYPE.INCOME))
//                        if (((Category) category).get_parentCategory().equals(((CategoryGroup) parentCategory).get_catID()))
//                            categories.add(category);
//                }
//            }
//            if(!categories.isEmpty()){
//                _listAllCategories.add(parentCategory);
//                _listAllCategories.addAll(categories);
//            }
//        }

//        if(updateAdapter)
//            _listAdapter.notifyDataSetChanged();
//        else
//            _listViewAccounts.setAdapter(_listAdapter);
    }

    private void refreshDataAdapter(){
        _listAdapter.notifyDataSetChanged();
    }


    private void ShowToast(String msg){
        Toast.makeText(getContext(),msg, Toast.LENGTH_LONG).show();
    }


}
