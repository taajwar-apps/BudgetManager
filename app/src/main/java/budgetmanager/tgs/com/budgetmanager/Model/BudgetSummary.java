package budgetmanager.tgs.com.budgetmanager.Model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import budgetmanager.tgs.com.budgetmanager.Calculator;
import budgetmanager.tgs.com.budgetmanager.Callback;
import budgetmanager.tgs.com.budgetmanager.Common;
import budgetmanager.tgs.com.budgetmanager.FragmentBudgetAllocation;
import budgetmanager.tgs.com.budgetmanager.FragmentCategories;
import budgetmanager.tgs.com.budgetmanager.ICatItem;
import budgetmanager.tgs.com.budgetmanager.R;
import budgetmanager.tgs.com.budgetmanager.Utils;

public class BudgetSummary implements ICatItem {

    private String _budgetCatID = "";
    private String _budgetCatParentID = "";
    private float _budgetAllocated = 0; // Allocated budget or expected income
    private float _budgetConsumed = 0; // Budget consumed or obtained income
    private boolean _isActiveForAllocation = false;
//    private String _catID = "";
    public static String _dbRoot = "Budget";

    private View _parentView;

    private ITEM_TYPE _budgetType;


    public BudgetSummary(){

//        _budgetType = ITEM_TYPE.EXPENSE;
    }

//    public enum CATEGORY_TYPE{EXPENSE,INCOME}

    public BudgetSummary(String categoryName, String parentCategory, float budgetAllocated, float budgetConsumed, boolean isActiveForAllocation, ITEM_TYPE item_type){
        _budgetCatID = categoryName;
        _budgetCatParentID = parentCategory;
        _budgetAllocated = budgetAllocated;
        _budgetConsumed = budgetConsumed;
        _isActiveForAllocation = isActiveForAllocation;
        _budgetType = item_type;
    }

    public String get_budgetCatID() {
        return _budgetCatID;
    }

    public void set_budgetCatID(String _budgetCatID) {
        this._budgetCatID = _budgetCatID;
    }

    public String get_budgetCatParentID() {
        return _budgetCatParentID;
    }

    public void set_budgetCatParentID(String _budgetCatParentID) {
        this._budgetCatParentID = _budgetCatParentID;
    }

    public float get_budgetAllocated() {
        return _budgetAllocated;
    }

    public void set_budgetAllocated(float _budgetAmount) {
        this._budgetAllocated = _budgetAmount;
    }

    public float get_budgetConsumed() {
        return _budgetConsumed;
    }

    public void set_budgetConsumed(float budgetAllocated) {
        this._budgetConsumed = budgetAllocated;
    }

    public float get_budgetRemaining() {
        return _budgetAllocated-_budgetConsumed;
    }


    public boolean is_isActiveForAllocation() {
        return _isActiveForAllocation;
    }

    public void set_isActiveForAllocation(boolean _isActiveForAllocation) {
        this._isActiveForAllocation = _isActiveForAllocation;
    }

    public void set_ParentView(View parentView){
        _parentView = parentView;
    }

    public ITEM_TYPE get_budgetType() {
        return _budgetType;
    }

    public void set_budgetType(ITEM_TYPE budgetType) {
        this._budgetType = budgetType;
    }

    public float getPercentRemaining(){
        return (get_budgetRemaining()/_budgetAllocated)*100;
    }

    public void saveCategoryBudget(ITEM_TYPE categoryType, final Callback callback){
        if(Common._firebaseAuth.getCurrentUser() != null) {

            //Get the reference to the loggedin user
            DatabaseReference categoriesRef = Common._firebaseDB.getReference(_dbRoot);
            DatabaseReference userRef = categoriesRef.child(Common._firebaseAuth.getCurrentUser().getUid());
            DatabaseReference monthRef = userRef.child(Utils.getMonth());
            DatabaseReference childRef = monthRef.child(_budgetCatID);
            //send current object to database to save
            childRef.setValue(this, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if (databaseError != null) callback.apply(databaseError.getMessage());
                    else callback.apply("Budget Allocated Successfully...");
                }
            });
        }
    }

    public void updateCategoryBudget(){
        if(Common._firebaseAuth.getCurrentUser() != null) {

            //Get the reference to the loggedin user
            DatabaseReference categoriesRef = Common._firebaseDB.getReference(_dbRoot);
            DatabaseReference userRef = categoriesRef.child(Common._firebaseAuth.getCurrentUser().getUid());
            DatabaseReference monthRef = userRef.child(Utils.getMonth());
            DatabaseReference childRef = monthRef.child(_budgetCatID);
//            DatabaseReference budgetAmountNodeRef = childRef.child("_budgetAmount");
            //send current object to database to save
            childRef.setValue(this, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    TextView tvBudgetGroup = _parentView.findViewById(R.id.tv_budget_allocation_group_amount);
                    FragmentBudgetAllocation.getSumAmountForCatGroup(_budgetCatParentID,tvBudgetGroup, "BudgetAllocation");
                }

            });
        }
    }

    @Override
    public int getViewType() {
        return FragmentCategories.RowType.LIST_ITEM.ordinal();
    }
    @Override
    public View getView(final LayoutInflater inflater, final View convertView) {
        final View view;
        view = inflater.inflate(R.layout.item_budget_summary, null);

        TextView textName = view.findViewById(R.id.tvBudgetCategory);
        final TextView textBudgetConsumed = view.findViewById(R.id.tvCatBudgetConsumed);
        TextView textBudgetAllocated = view.findViewById(R.id.tvCatBudgetAllocated);
        TextView textBudgetRemaining = view.findViewById(R.id.tvCatBudgetRemaining);
        textName.setText(FragmentBudgetAllocation.getCatNameForID(_budgetCatID));
        textBudgetAllocated.setText("/"+_budgetAllocated);
        textBudgetConsumed.setText(""+_budgetConsumed);
        textBudgetRemaining.setText((int)getPercentRemaining()+"% Remaining Rs. "+get_budgetRemaining());
        ProgressBar progressBar = view.findViewById(R.id.budgetProgress);
        progressBar.setProgress((int) (100-getPercentRemaining()));

        ViewGroup container = (ViewGroup) view.findViewById(R.id.container);

        if (!_isActiveForAllocation) {
//            textBudgetAllocated.setEnabled(false);//.setVisibility(View.INVISIBLE);
//            textBudgetRemaining.setEnabled(false);//.setVisibility(View.INVISIBLE);
//            progressBar.setEnabled(false);//.setVisibility(View.INVISIBLE);
            container.removeView(textBudgetRemaining);
            container.removeView(progressBar);
            container = (ViewGroup) view.findViewById(R.id.container2);
            container.removeView(textBudgetAllocated);
        }
//        final CheckBox chkBox = view.findViewById(R.id.chkbox_budget_allocation);
//        chkBox.setChecked(_isActiveForAllocation);

//        if(_isActiveForAllocation)
//            textAmount.setVisibility(View.VISIBLE);
//        else
//            textAmount.setVisibility(View.INVISIBLE);

//        textAmount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                textAmount.setVisibility(View.VISIBLE);
//                Calculator calculator = new Calculator(convertView.getContext());
//                calculator.show(new Callback() {
//                    @Override
//                    public void apply(String msg) {
//
//                    }
//
//                    @Override
//                    public void result(float result) {
////                        TextView tvBudgetGroup = _parentView.findViewById(R.id.tv_budget_allocation_group_amount);
//                        _budgetAllocated = result;
//                        textAmount.setText(""+_budgetAllocated);
//                        updateCategoryBudget();
//                        textAmount.setVisibility(View.VISIBLE);
////                            FragmentBudgetAllocation.getSumAmountForCatGroup(_budgetCatParentID,tvBudgetGroup, "BudgetAllocation");
//
//                    }
//                });
//            }
//        });

        return view;
    }
}
