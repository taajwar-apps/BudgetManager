package budgetmanager.tgs.com.budgetmanager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import budgetmanager.tgs.com.budgetmanager.Model.Account;
import budgetmanager.tgs.com.budgetmanager.Model.Category;
import budgetmanager.tgs.com.budgetmanager.Model.CategoryGroup;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    FragmentTransaction _fragmentTransactions;
    FragmentAccounts _fragmentAccounts;
    FragmentCategories _fragmentCategories;
    FragmentBudgetSummary _fragmentBudgetSummary;
    ProgressDialog progressDialog;
//    public static BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_transactions:
//                    mTextMessage.setText(R.string.title_transactions);
                    setFragment(_fragmentTransactions);
                    return true;
                case R.id.navigation_accounts:
//                    mTextMessage.setText(R.string.title_accounts);
                    setFragment(_fragmentAccounts);
                    return true;
                case R.id.navigation_categories:
//                    mTextMessage.setText(R.string.title_categories);
                    setFragment(_fragmentCategories);
                    return true;
                case R.id.navigation_budget:
//                    mTextMessage.setText(R.string.title_budget);
                    setFragment(_fragmentBudgetSummary);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
//        getSupportActionBar().hide();

        loadCategoriesGroup();
        loadCategories();
        loadAccounts();
        showLoader();

//        mTextMessage = (TextView) findViewById(R.id.message);
        Common.navigation = findViewById(R.id.navigationBottom);
        Common.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        _fragmentTransactions = new FragmentTransaction();
        _fragmentAccounts = new FragmentAccounts();
        _fragmentCategories = new FragmentCategories();
        _fragmentBudgetSummary = new FragmentBudgetSummary();

        setFragment(_fragmentTransactions);
    }


    private  void setFragment(Fragment fragment){
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

    boolean _categoriesLoaded = false;
    boolean _categoriesGroupLoaded = false;
    boolean _accountsLoaded = false;

    private void loadCategoriesGroup(){
        DatabaseReference categoriesDbRef = FirebaseDatabase.getInstance().getReference("ParentCategories");
        String currentUserId = Common._firebaseAuth.getCurrentUser().getUid();
        DatabaseReference userDbRef = categoriesDbRef.child(currentUserId);

        userDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Common._listCategoriesGroup.clear();
                for(DataSnapshot parentCategorySnapshot : dataSnapshot.getChildren()){
                    CategoryGroup parentCategory = parentCategorySnapshot.getValue(CategoryGroup.class);
                    Common._listCategoriesGroup.add(parentCategory);
                }
                if (_categoriesGroupLoaded && _categoriesLoaded && _accountsLoaded) {
                    return;
                }else {
                    _categoriesGroupLoaded = true;
                    if (_categoriesLoaded && _accountsLoaded) {
                        progressDialog.dismiss();
    //                    showList();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadCategories(){
//        String currentUserId = Common._firebaseAuth.getCurrentUser().getUid();
//        Query query = FirebaseDatabase.getInstance().getReference("Categories").child(currentUserId).orderByChild("ParentCategoryID").equalTo(parentCatID);
//
//        FirebaseListOptions<Category> options = new FirebaseListOptions.Builder<Category>().setLayout(R.layout.category_item_layout).setQuery(query, Category.class).setLifecycleOwner(this).build();
//
//        FirebaseListAdapter<Category> adapter = new FirebaseListAdapter<Category>(options) {
//            @Override
//            protected void populateView(View v, Category model, int position) {
//                TextView textName = (TextView) v.findViewById(R.id.category_name);
//                TextView textDescription = (TextView) v.findViewById(R.id.category_description);
//                textName.setText(model.get_categoryName());
//                textDescription.setText(model.get_description());
//            }
//        };
        DatabaseReference categoriesDbRef = FirebaseDatabase.getInstance().getReference("Categories");
        String currentUserId = Common._firebaseAuth.getCurrentUser().getUid();
        DatabaseReference userDbRef = categoriesDbRef.child(currentUserId);

        userDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Common._listCategories.clear();
                for(DataSnapshot expenseCategorySnapshot : dataSnapshot.getChildren()){
                    Category category = expenseCategorySnapshot.getValue(Category.class);
                    Common._listCategories.add(category);
                }

                if (_categoriesGroupLoaded && _categoriesLoaded  && _accountsLoaded) {
//                    _listAdapter.notifyDataSetChanged();
                    return;
                }else {
                    _categoriesLoaded = true;
                    if (_categoriesGroupLoaded  && _accountsLoaded) {
                        progressDialog.dismiss();
//                        showList();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadAccounts(){
        DatabaseReference accountsDbRef = FirebaseDatabase.getInstance().getReference("Accounts");
        String currentUserId = Common._firebaseAuth.getCurrentUser().getUid();
        DatabaseReference userDbRef = accountsDbRef.child(currentUserId);

        userDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Common._listAccounts.clear();
                for(DataSnapshot accountsSnapshot : dataSnapshot.getChildren()){
                    Account account = accountsSnapshot.getValue(Account.class);
                    Common._listAccounts.add(account);
                }

                if (_categoriesGroupLoaded && _categoriesLoaded && _accountsLoaded) {
//                    _listAdapter.notifyDataSetChanged();
                    return;
                }else {
                    _accountsLoaded = true;
                    if (_categoriesGroupLoaded && _categoriesLoaded) {
                        progressDialog.dismiss();
//                        showList();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showLoader(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();
    }

}
