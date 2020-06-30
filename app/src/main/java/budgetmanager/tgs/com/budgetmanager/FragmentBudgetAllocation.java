package budgetmanager.tgs.com.budgetmanager;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import budgetmanager.tgs.com.budgetmanager.Model.BudgetAllocation;
//import budgetmanager.tgs.com.budgetmanager.Model.BudgetAllocationGroup;
import budgetmanager.tgs.com.budgetmanager.Model.Category;
import budgetmanager.tgs.com.budgetmanager.Model.CategoryGroup;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class FragmentBudgetAllocation extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentBudgetAllocation.OnFragmentInteractionListener mListener;

    ListView _listViewCategories;
    BaseAdapter _listAdapter;
    BudgetAllocation selectedCategory;
//    BudgetAllocationGroup _selectedParentCategory;

    ICatItem.ITEM_TYPE _selectedType = ICatItem.ITEM_TYPE.EXPENSE;
    ProgressDialog progressDialog;

    public FragmentBudgetAllocation() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_budget_allocation, container, false);
//        ((TextView)view.findViewById(R.id.custom_header)).setText("Categories");
        _listViewCategories = view.findViewById(R.id.list_view_budget_allocation);
        _listAllCategories = new ArrayList<ICatItem>();
//        showList();

//        for (int i=1; i<=10; i++) {
//            BudgetAllocation category = new BudgetAllocation("BudgetCatID_"+i, "BudgetCatGroupID", "10");
//            category.saveCategoryBudget(_selectedType, new Callback() {
//                @Override
//                public void apply(String msg) {
//                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void returnResult(float result) {
//
//                }
//            });
//        }

//        for (ICatItem category:Common._listCategories){
//            Category catItem = (Category)category;
//            BudgetAllocation budgetAllocationCat = new BudgetAllocation(catItem.get_catID(), catItem.get_parentCategory(), 10, true, catItem.get_categoryType());
//            budgetAllocationCat.saveCategoryBudget(_selectedType, new Callback() {
//                @Override
//                public void apply(String msg) {
//                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void returnResult(float result) {
//
//                }
//            });
//        }

//        loadCategories();
        loadBudget();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void loadBudget(){
        DatabaseReference budgetAllocationRootRef = Common._firebaseDB.getReference(BudgetAllocation._dbRoot);
        DatabaseReference userRef = budgetAllocationRootRef.child(Common._firebaseAuth.getCurrentUser().getUid());
        DatabaseReference budgetMonthRef = userRef.child(Utils.getMonth());
        budgetMonthRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("DATA SNAPSHOT",dataSnapshot.toString());
                if (dataSnapshot.exists())
                    loadCategories();
                else
                    createBudget();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createBudget(){
        for (ICatItem category:Common._listCategories){
            Category catItem = (Category)category;
            BudgetAllocation budgetAllocationCat = new BudgetAllocation(catItem.get_catID(), catItem.get_parentCategory(), 10,5, true, catItem.get_categoryType());
            budgetAllocationCat.saveCategoryBudget(_selectedType, new Callback() {
                @Override
                public void apply(String msg) {
//                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    Log.d("BUDGET CREATED",msg);
                }

                @Override
                public void result(float result) {

                }
            });
        }
        loadCategories();
    }

    //=============================Category Section Start=================================

    private void saveBudgetAllocation(View popupView, final PopupWindow popupWindow){
//        String categoryName = ((EditText)popupView.findViewById(R.id.editTextCategoryName)).getText().toString();
//        int selectedItemPosition = ((Spinner)popupView.findViewById(R.id.spinnerParentCategory)).getSelectedItemPosition();
//        final CategoryGroup catGroup = _categoryGroupsInSpinner.get(selectedItemPosition);
//        String catGroupID = catGroup.get_catID();//(_categoryGroupsInSpinner.get(selectedItemPosition)).get_catID();
//        String categoryDescription = ((EditText)popupView.findViewById(R.id.editTextCategoryDescription)).getText().toString();
//        final BudgetAllocation category = new BudgetAllocation("categoryName", "catGroupID", 100, 20, false, ICatItem.ITEM_TYPE.EXPENSE);
//        RadioGroup radioCategoryGroup = popupView.findViewById(R.id.radioCategoryGroup);
//        int selectedRadioBtnID = radioCategoryGroup.getCheckedRadioButtonId();
//        switch (selectedRadioBtnID){
//            case R.id.radioBtnExpense:
//                _selectedType = ICatItem.ITEM_TYPE.EXPENSE;
//                break;
//            case R.id.radioBtnIncome:
//                _selectedType = ICatItem.ITEM_TYPE.INCOME;
//                break;
//            default:
//                ShowToast("Please Select Category Type");
//                return;
//        }
//        category.saveCategoryBudget(_selectedType, new Callback() {
//            @Override
//            public void apply(String msg) {
//                Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
//                popupWindow.dismiss();
//                populateListView();
//            }
//            @Override
//            public void result(float result) {
//
//            }
//        });
//        popupWindow.dismiss();
    }


    static List<ICatItem> _listBudgetAllocation = new ArrayList<ICatItem>();

    private void loadCategories(){
        DatabaseReference budgetAllocationDbRef = FirebaseDatabase.getInstance().getReference(BudgetAllocation._dbRoot);
        String currentUserId = Common._firebaseAuth.getCurrentUser().getUid();
        DatabaseReference userDbRef = budgetAllocationDbRef.child(currentUserId);
        DatabaseReference budgetAllocationMonthRef = userDbRef.child(Utils.getMonth());

        budgetAllocationMonthRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                _listBudgetAllocation.clear();
                for(DataSnapshot expenseCategorySnapshot : dataSnapshot.getChildren()){
                    BudgetAllocation budgetAllocation = expenseCategorySnapshot.getValue(BudgetAllocation.class);
                    _listBudgetAllocation.add(budgetAllocation);
                }
                populateListView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    List<ICatItem> _listAllCategories;// = new ArrayList<ICatItem>();
    View _parentView = null;
    void populateListView(){
        boolean updateAdapter = false;
        if(_listAllCategories.size() > 0)
            updateAdapter = true;
        else {
            _listAdapter = new BaseAdapter() {
                @Override
                public int getCount() {
                    return _listAllCategories.size();
                }

                @Override
                public ICatItem getItem(int position) {
                    return _listAllCategories.get(position);
                }

                @Override
                public long getItemId(int position) {
                    return position;
                }

                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    View view = null;
                    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    ICatItem item = getItem(position);
                    if(item.getClass().equals(CategoryGroup.class)){
                        view = inflater.inflate(R.layout.list_item_budget_allocation_group, null);
                        _parentView = view;
                        TextView tvBudgetAllocationGroupName = view.findViewById(R.id.tv_budget_allocation_cat_group_name);
                        TextView tvBudgetAllocationGroupAmount = view.findViewById(R.id.tv_budget_allocation_group_amount);
                        tvBudgetAllocationGroupName.setText(((CategoryGroup)item).get_categoryName());
                        getSumAmountForCatGroup(((CategoryGroup)item).get_catID(),tvBudgetAllocationGroupAmount, "FragmentBudgetAllocation");
                        getSumAmountForCatGroup(((CategoryGroup)item).get_catID(),tvBudgetAllocationGroupAmount, "FragmentBudgetAllocation2");
                    } else {
                        ((BudgetAllocation) item).set_ParentView(_parentView);
                        view = item.getView(inflater, convertView);
                    }
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
                    return view;
                }
            };
        }

//        CategoryGroup cGroup = new CategoryGroup("BudgetCatGroupID",ICatItem.ITEM_TYPE.EXPENSE);
//        cGroup.set_catID("BudgetCatGroupID");
//        Common._listCategoriesGroup.add(cGroup);
//        CategoryGroup cGroup2 = new CategoryGroup("BudgetCatGroupID_1",ICatItem.ITEM_TYPE.EXPENSE);
//        cGroup2.set_catID("BudgetCatGroupID_1");
//        Common._listCategoriesGroup.add(cGroup2);
//        CategoryGroup cGroup3 = new CategoryGroup("BudgetCatGroupID_2",ICatItem.ITEM_TYPE.INCOME);
//        cGroup3.set_catID("BudgetCatGroupID_2");
//        Common._listCategoriesGroup.add(cGroup3);


        _listAllCategories.clear();
//        _listAllCategories.add(new Header("Expense Categories"));
        for (ICatItem parentCategory:Common._listCategoriesGroup){
            List<ICatItem> categories = new ArrayList<ICatItem>();
            if(((CategoryGroup)parentCategory).get_categoryType().equals(ICatItem.ITEM_TYPE.EXPENSE)) {
                for (ICatItem category : _listBudgetAllocation) {
                    if (((BudgetAllocation) category).get_budgetType().equals(ICatItem.ITEM_TYPE.EXPENSE))
                        if (((BudgetAllocation) category).get_budgetCatParentID().equals(((CategoryGroup) parentCategory).get_catID())) {
                            categories.add(category);
                        }
                }
            }
            if(!categories.isEmpty()){
                _listAllCategories.add(parentCategory);
                _listAllCategories.addAll(categories);
            }
        }
//        _listAllCategories.add(new Header("Income Categories"));
        for (ICatItem parentCategory:Common._listCategoriesGroup){
            List<ICatItem> categories2 = new ArrayList<ICatItem>();
            if(((CategoryGroup)parentCategory).get_categoryType().equals(ICatItem.ITEM_TYPE.INCOME)) {
                for (ICatItem category : _listBudgetAllocation) {
                    if (((BudgetAllocation) category).get_budgetType().equals(ICatItem.ITEM_TYPE.INCOME))
                        if (((BudgetAllocation) category).get_budgetCatParentID().equals(((CategoryGroup) parentCategory).get_catID()))
                            categories2.add(category);
                }
            }
            if(!categories2.isEmpty()){
                _listAllCategories.add(parentCategory);
                _listAllCategories.addAll(categories2);
            }
        }

        if(updateAdapter)
            _listAdapter.notifyDataSetChanged();
        else
            _listViewCategories.setAdapter(_listAdapter);
    }

    List<CategoryGroup> _categoryGroupsInSpinner = new ArrayList<CategoryGroup>();

    public static String getCatNameForID(String catID){

        for (ICatItem category:Common._listCategories){
            if(((Category)category).get_catID().equals(catID))
                return ((Category)category).get_categoryName();
        }
        return ((CategoryGroup)Common._listCategoriesGroup.get(0)).get_categoryName();
    }

    public static void getSumAmountForCatGroup(String catGroupID, TextView textView, String calledFrom){
        float amountSumOfAllCategories = 0;//sum of all categories under same parent category
        for (ICatItem category : _listBudgetAllocation) {
//            if (((BudgetAllocation) category).get_categoryType().equals(ICatItem.ITEM_TYPE.INCOME))
                if (((BudgetAllocation) category).get_budgetCatParentID().equals(catGroupID)) {
                    if(((BudgetAllocation) category).is_isActiveForAllocation())
                        amountSumOfAllCategories += ((BudgetAllocation) category).get_budgetAllocated();
                }
        }
        textView.setText(amountSumOfAllCategories+"");
//        return amountSumOfAllCategories;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void ShowToast(String msg){
        Toast.makeText(getContext(),msg, Toast.LENGTH_LONG).show();
    }

    private void showLoader(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
