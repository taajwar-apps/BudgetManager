package budgetmanager.tgs.com.budgetmanager;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

import budgetmanager.tgs.com.budgetmanager.Model.BudgetAllocation;
import budgetmanager.tgs.com.budgetmanager.Model.BudgetSummary;
import budgetmanager.tgs.com.budgetmanager.Model.Category;
import budgetmanager.tgs.com.budgetmanager.Model.CategoryGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentBudgetSummary extends Fragment implements AdapterView.OnItemSelectedListener{

    private FragmentBudgetAllocation.OnFragmentInteractionListener mListener;

    ListView _listViewCategories;
    BaseAdapter _listAdapter;
    BudgetSummary selectedCategory;
//    BudgetAllocationGroup _selectedParentCategory;

    ICatItem.ITEM_TYPE _selectedType = ICatItem.ITEM_TYPE.EXPENSE;
    ProgressDialog progressDialog;

    public enum RowType {
        LIST_ITEM, HEADER_ITEM
    }

    public FragmentBudgetSummary() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_budget_summary, container, false);
        _listViewCategories = view.findViewById(R.id.list_view_budget_summary);
        _listAllCategories = new ArrayList<ICatItem>();
        loadBudget();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void loadBudget(){
        DatabaseReference budgetAllocationRootRef = Common._firebaseDB.getReference(BudgetSummary._dbRoot);
        DatabaseReference userRef = budgetAllocationRootRef.child(Common._firebaseAuth.getCurrentUser().getUid());
        DatabaseReference budgetMonthRef = userRef.child(Utils.getMonth());
        budgetMonthRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("DATA SNAPSHOT",dataSnapshot.toString());
                if (dataSnapshot.exists())
                    loadCategories();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //=============================Category Section Start=================================

    static List<ICatItem> _listBudgetSummary = new ArrayList<ICatItem>();

    private void loadCategories(){
        DatabaseReference budgetAllocationDbRef = FirebaseDatabase.getInstance().getReference(BudgetSummary._dbRoot);
        String currentUserId = Common._firebaseAuth.getCurrentUser().getUid();
        DatabaseReference userDbRef = budgetAllocationDbRef.child(currentUserId);
        DatabaseReference budgetAllocationMonthRef = userDbRef.child(Utils.getMonth());

        budgetAllocationMonthRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                _listBudgetSummary.clear();
                for(DataSnapshot expenseCategorySnapshot : dataSnapshot.getChildren()){
                    BudgetSummary budgetAllocation = expenseCategorySnapshot.getValue(BudgetSummary.class);
                    _listBudgetSummary.add(budgetAllocation);
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
                        view = inflater.inflate(R.layout.item_group_budget_summary, null);
                        _parentView = view;
                        TextView tvBudgetAllocationGroupName = view.findViewById(R.id.tvBudgetGroupCat);
                        TextView tvGroupBudgetConsumed = view.findViewById(R.id.tvGroupBudgetConsumed);
                        TextView tvGroupBudgetAllocated = view.findViewById(R.id.tvGroupBudgetAllocated);
                        TextView tvGroupBudgetRemaining = view.findViewById(R.id.tvGroupBudgetRemaining);
                        tvBudgetAllocationGroupName.setText(((CategoryGroup)item).get_categoryName());
                        HashMap<String, Float> budgetDetail = getSumAmountForCatGroup(((CategoryGroup)item).get_catID(),tvGroupBudgetAllocated, "FragmentBudgetAllocation");
                        tvGroupBudgetAllocated.setText("/"+budgetDetail.get("Allocated").toString());
                        tvGroupBudgetConsumed.setText(budgetDetail.get("Consumed").toString());
                        float percentageRemaining = budgetDetail.get("Percentage");
                        tvGroupBudgetRemaining.setText((int) percentageRemaining+"% Remaining Rs. "+ budgetDetail.get("Remaining").toString());
                        ProgressBar progressBar = view.findViewById(R.id.budgetGroupProgress);
                        progressBar.setProgress((int) (100-percentageRemaining));
//                        getSumAmountForCatGroup(((CategoryGroup)item).get_catID(),tvBudgetAllocationGroupAmount, "FragmentBudgetAllocation2");
                    } else if (item.getClass().equals(HeaderBudgetSummary.class)){
//                        ((HeaderBudgetSummary) item).set_ParentView(_parentView);
                        view = item.getView(inflater, convertView);
                    } else {
                        ((BudgetSummary) item).set_ParentView(_parentView);
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

        _listAllCategories.clear();
        HashMap<String, Float> totalExpenseDetail = calculateTotalExpense();
        _listAllCategories.add(new HeaderBudgetSummary("EXPENSE", totalExpenseDetail.get("Allocated"), totalExpenseDetail.get("Used")));
        for (ICatItem parentCategory:Common._listCategoriesGroup){
            List<ICatItem> categories = new ArrayList<ICatItem>();
            if(((CategoryGroup)parentCategory).get_categoryType().equals(ICatItem.ITEM_TYPE.EXPENSE)) {
                for (ICatItem category : _listBudgetSummary) {
                    if (((BudgetSummary) category).get_budgetType().equals(ICatItem.ITEM_TYPE.EXPENSE))
                        if (((BudgetSummary) category).get_budgetCatParentID().equals(((CategoryGroup) parentCategory).get_catID())) {
                            categories.add(category);
                        }
                }
            }
            if(!categories.isEmpty()){
                _listAllCategories.add(parentCategory);
                _listAllCategories.addAll(categories);
            }
        }

        HashMap<String, Float> totalIncomeDetail = calculateTotalIncome();
        _listAllCategories.add(new HeaderBudgetSummary("INCOME", totalIncomeDetail.get("Allocated"), totalIncomeDetail.get("Used")));
        for (ICatItem parentCategory:Common._listCategoriesGroup){
            List<ICatItem> categories2 = new ArrayList<ICatItem>();
            if(((CategoryGroup)parentCategory).get_categoryType().equals(ICatItem.ITEM_TYPE.INCOME)) {
                for (ICatItem category : _listBudgetSummary) {
                    if (((BudgetSummary) category).get_budgetType().equals(ICatItem.ITEM_TYPE.INCOME))
                        if (((BudgetSummary) category).get_budgetCatParentID().equals(((CategoryGroup) parentCategory).get_catID()))
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

    public HashMap<String, Float> calculateTotalExpense(){
        float totalExpenseAllocated = 0;
        float totalExpenseUsed = 0;
        for (ICatItem category : _listBudgetSummary) {
            if(((BudgetSummary) category).get_budgetType().equals(BudgetSummary.ITEM_TYPE.EXPENSE)) {
                totalExpenseAllocated += ((BudgetSummary) category).get_budgetAllocated();
                totalExpenseUsed += ((BudgetSummary) category).get_budgetConsumed();
            }
        }

        HashMap<String, Float> totalBudgetDetail = new HashMap<String, Float>();
        totalBudgetDetail.put("Allocated", totalExpenseAllocated);
        totalBudgetDetail.put("Used", totalExpenseUsed);
        return totalBudgetDetail;
    }

    public HashMap<String, Float> calculateTotalIncome(){
        float totalIncomeAllocated = 0;
        float totalIncomeObtained = 0;
        for (ICatItem category : _listBudgetSummary) {
            if(((BudgetSummary) category).get_budgetType().equals(BudgetSummary.ITEM_TYPE.EXPENSE)) {
                totalIncomeAllocated += ((BudgetSummary) category).get_budgetAllocated();
                totalIncomeObtained += ((BudgetSummary) category).get_budgetConsumed();
            }
        }

        HashMap<String, Float> totalBudgetDetail = new HashMap<String, Float>();
        totalBudgetDetail.put("Allocated", totalIncomeAllocated);
        totalBudgetDetail.put("Used", totalIncomeObtained);
        return totalBudgetDetail;
    }

    public static HashMap<String, Float> getSumAmountForCatGroup(String catGroupID, TextView textView, String calledFrom){
        float sumOfBudgetAllocatedAllCategories = 0;//sum of all categories under same parent category
        float sumOfBudgetConsumedAllCategories = 0;
        float sumOfBudgetRemainingAllCategories = 0;
        float percentageRemaining = 0;
        for (ICatItem category : _listBudgetSummary) {
            if (((BudgetSummary) category).get_budgetCatParentID().equals(catGroupID)) {
                if(((BudgetSummary) category).is_isActiveForAllocation()) {
                    sumOfBudgetAllocatedAllCategories += ((BudgetSummary) category).get_budgetAllocated();
                    sumOfBudgetConsumedAllCategories += ((BudgetSummary) category).get_budgetConsumed();
                    sumOfBudgetRemainingAllCategories += ((BudgetSummary) category).get_budgetRemaining();
                }
            }
        }
        percentageRemaining = (sumOfBudgetRemainingAllCategories/sumOfBudgetAllocatedAllCategories)*100;
        HashMap<String, Float> budgetDetail = new HashMap<String, Float>();
        budgetDetail.put("Allocated", sumOfBudgetAllocatedAllCategories);
        budgetDetail.put("Consumed", sumOfBudgetConsumedAllCategories);
        budgetDetail.put("Remaining", sumOfBudgetRemainingAllCategories);
        budgetDetail.put("Percentage", percentageRemaining);

//        textView.setText(sumOfBudgetAllocatedAllCategories+"");
        return budgetDetail;
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
