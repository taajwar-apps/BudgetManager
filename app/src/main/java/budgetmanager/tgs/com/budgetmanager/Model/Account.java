package budgetmanager.tgs.com.budgetmanager.Model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import budgetmanager.tgs.com.budgetmanager.Common;
import budgetmanager.tgs.com.budgetmanager.Callback;
import budgetmanager.tgs.com.budgetmanager.FragmentCategories;
import budgetmanager.tgs.com.budgetmanager.ICatItem;
import budgetmanager.tgs.com.budgetmanager.R;

public class Account implements ICatItem {

    private String _accountTitle = "";
//    private String _parentCategory = "";
    private String _accountBalance = "";
    private String _accountID = "";

//    private CATEGORY_TYPE _categoryType;


    public Account(){
//        _categoryType = CATEGORY_TYPE.EXPENSE;
    }

//    enum CATEGORY_TYPE{EXPENSE,INCOME}

    public Account(String accountTitle, String accountBalance){
        _accountTitle = accountTitle;
//        _parentCategory = parentCategory;
        _accountBalance = accountBalance;
    }

    public String get_catID() {
        return _accountID;
    }

    public void set_catID(String accountID) {
        this._accountID = accountID;
    }

    public String get_accountTitle() {
        return _accountTitle;
    }

    public void set_accountTitle(String accountTitle) {
        this._accountTitle = accountTitle;
    }

//    public String get_parentCategory() {
//        return _parentCategory;
//    }
//
//    public void set_parentCategory(String _parentCategory) {
//        this._parentCategory = _parentCategory;
//    }

    public String get_accountBalance() {
        return _accountBalance;
    }

    public void set_accountBalance(String _description) {
        this._accountBalance = _description;
    }

//    public CATEGORY_TYPE get_categoryType() {
//        return _categoryType;
//    }
//
//    public void set_categoryType(CATEGORY_TYPE _categoryType) {
//        this._categoryType = _categoryType;
//    }


    public void saveAccount(final Callback callback){
        if(Common._firebaseAuth.getCurrentUser() != null) {

            //Get the reference to the loggedin user
            DatabaseReference categoriesRef = Common._firebaseDB.getReference("Accounts");
//            DatabaseReference userRef = Common._firebaseDB.getReference(Common._firebaseAuth.getCurrentUser().getUid());
            DatabaseReference userRef = categoriesRef.child(Common._firebaseAuth.getCurrentUser().getUid());
//            DatabaseReference categoryRef = null;
//            if(categoryType.equals(CATEGORY_TYPE.EXPENSE)) {
//                categoryRef = userRef.child("CategoriesExpense");
//                this._categoryType = CATEGORY_TYPE.EXPENSE;
//            } else if (categoryType.equals(CATEGORY_TYPE.INCOME)) {
//                categoryRef = userRef.child("CategoriesIncome");
//                this._categoryType = CATEGORY_TYPE.INCOME;
//            }

            String accountID = userRef.push().getKey();
            _accountID = accountID;
            DatabaseReference childRef = userRef.child(accountID);
            //send current object to database to save
            childRef.setValue(this, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if (databaseError != null) callback.apply(databaseError.getMessage());
                    else callback.apply("Account Saved Successfully...");
                }
            });
        }
    }

    public void updateAccount(final Callback callback){
        if(Common._firebaseAuth.getCurrentUser() != null) {
            //Get the reference to the loggedin user
//            DatabaseReference userRef = Common._firebaseDB.getReference(Common._firebaseAuth.getCurrentUser().getUid());
//            DatabaseReference categoryRef = null;

            //Get the reference to the loggedin user
            DatabaseReference categoriesRef = Common._firebaseDB.getReference("Accounts");
            DatabaseReference userRef = categoriesRef.child(Common._firebaseAuth.getCurrentUser().getUid());
//            DatabaseReference categoryRef = null;
//            if(categoryType.equals(CATEGORY_TYPE.EXPENSE)) {
//                categoryRef = userRef.child("CategoriesExpense");
//                this._categoryType = CATEGORY_TYPE.EXPENSE;
//            } else if (categoryType.equals(CATEGORY_TYPE.INCOME)) {
//                categoryRef = userRef.child("CategoriesIncome");
//                this._categoryType = CATEGORY_TYPE.INCOME;
//            }
//            String expenseCategoryID = expenseCategoryRef.push().getKey();
//            _catID = expenseCategoryID;
            DatabaseReference childRef = userRef.child(_accountID);
//            DatabaseReference childRef = expenseCategoryRef.child(expenseCategoryID);
            //send current object to database to save
            childRef.setValue(this, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if (databaseError != null) callback.apply(databaseError.getMessage());
                    else callback.apply("Account Updated Successfully...");
                }
            });
        }
    }

    public void deleteAccount(final Callback callback){
        if(Common._firebaseAuth.getCurrentUser() != null) {
            //Get the reference to the loggedin user
//            DatabaseReference userRef = Common._firebaseDB.getReference(Common._firebaseAuth.getCurrentUser().getUid());
//            DatabaseReference categoryRef = null;

            DatabaseReference categoriesRef = Common._firebaseDB.getReference("Accounts");
            DatabaseReference userRef = categoriesRef.child(Common._firebaseAuth.getCurrentUser().getUid());

//            if(_categoryType.equals(CATEGORY_TYPE.EXPENSE)) {
//                categoryRef = userRef.child("CategoriesExpense");
//            } else if (_categoryType.equals(CATEGORY_TYPE.INCOME)) {
//                categoryRef = userRef.child("CategoriesIncome");
//            }
            DatabaseReference catRef = userRef.child(_accountID);
//            DatabaseReference childRef = expenseCategoryRef.child(expenseCategoryID);
            //send current object to database to save
            catRef.removeValue(new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
//                    Toast.makeText(context,"", Toast.LENGTH_LONG).show();
                    if (databaseError != null) callback.apply(databaseError.getMessage());
                    else callback.apply("Account Deleted Successfully...");
                }
            });
        }
    }

    @Override
    public int getViewType() {
        return FragmentCategories.RowType.LIST_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = inflater.inflate(R.layout.account_item_layout, null);
            // Do some initialization
        } else {
            view = convertView;
        }

        TextView textAccountTitle = view.findViewById(R.id.textViewAccountTitle);
        TextView textAccountBalance = view.findViewById(R.id.textViewAccountBalance);
        textAccountTitle.setText(_accountTitle);
        textAccountBalance.setText(_accountBalance);

        return view;
    }
}
