package budgetmanager.tgs.com.budgetmanager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import budgetmanager.tgs.com.budgetmanager.Model.Account;
import budgetmanager.tgs.com.budgetmanager.Model.BudgetAllocation;
import budgetmanager.tgs.com.budgetmanager.Model.BudgetAllocationGroup;
import budgetmanager.tgs.com.budgetmanager.Model.Category;
import budgetmanager.tgs.com.budgetmanager.Model.CategoryGroup;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentTransaction _fragmentTransactions;
    FragmentAccounts _fragmentAccounts;
    FragmentCategories _fragmentCategories;
    FragmentBudgetSummary _fragmentBudgetSummary;
    FragmentBudgetAllocation _fragmentBudgetAllocation;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //================
        Common.navigation = findViewById(R.id.navigationBottom);
        Common.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        _fragmentTransactions = new FragmentTransaction();
        _fragmentAccounts = new FragmentAccounts();
        _fragmentCategories = new FragmentCategories();
        _fragmentBudgetSummary = new FragmentBudgetSummary();
        _fragmentBudgetAllocation = new FragmentBudgetAllocation();

        setFragment(_fragmentTransactions, false);

        loadCategoriesGroup();
        loadCategories();
        loadAccounts();
        showLoader();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_transactions:
//                    mTextMessage.setText(R.string.title_transactions);
                    setFragment(_fragmentTransactions, false);
                    return true;
                case R.id.navigation_accounts:
//                    mTextMessage.setText(R.string.title_accounts);
                    setFragment(_fragmentAccounts, false);
                    return true;
                case R.id.navigation_categories:
//                    mTextMessage.setText(R.string.title_categories);
                    setFragment(_fragmentCategories, false);
                    return true;
                case R.id.navigation_budget:
//                    mTextMessage.setText(R.string.title_budget);
                    setFragment(_fragmentBudgetSummary,false);
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_budget_allocation) {
//            Common.hideNavigationView();
            setFragment(_fragmentBudgetAllocation, true);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private  void setFragment(Fragment fragment, boolean addToBackStack){
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        if(addToBackStack)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //======================= Data Loading ==============================
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
