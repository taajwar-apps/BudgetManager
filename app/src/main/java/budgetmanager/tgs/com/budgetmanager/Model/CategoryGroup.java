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
import budgetmanager.tgs.com.budgetmanager.Item;
import budgetmanager.tgs.com.budgetmanager.R;

public class CategoryGroup implements Item {

    private String _categoryName = "";
//    private String _description = "";
    private String _catID = "";

    private ITEM_TYPE _categoryType;


    public CategoryGroup(){
        _categoryType = ITEM_TYPE.EXPENSE;
    }

//    public enum CATEGORY_TYPE{EXPENSE,INCOME}

    public CategoryGroup(String categoryName, ITEM_TYPE categoryType){
        _categoryName = categoryName;
//        _description = description;
        this._categoryType = categoryType;
    }

    public String get_catID() {
        return _catID;
    }

    public void set_catID(String _catID) {
        this._catID = _catID;
    }

    public String get_categoryName() {
        return _categoryName;
    }

    public void set_categoryName(String _categoryName) {
        this._categoryName = _categoryName;
    }

//    public String get_parentCategory() {
//        return _parentCategory;
//    }
//
//    public void set_parentCategory(String _parentCategory) {
//        this._parentCategory = _parentCategory;
//    }

//    public String get_description() {
//        return _description;
//    }
//
//    public void set_description(String _description) {
//        this._description = _description;
//    }

    public ITEM_TYPE get_categoryType() {
        return _categoryType;
    }

    public void set_categoryType(ITEM_TYPE _categoryType) {
        this._categoryType = _categoryType;
    }


    public void saveCategory(final Callback callback){
        if(Common._firebaseAuth.getCurrentUser() != null) {

            //Get the reference to the loggedin user
            DatabaseReference categoriesRef = Common._firebaseDB.getReference("ParentCategories");
//            DatabaseReference userRef = Common._firebaseDB.getReference(Common._firebaseAuth.getCurrentUser().getUid());
            DatabaseReference userRef = categoriesRef.child(Common._firebaseAuth.getCurrentUser().getUid());
//            DatabaseReference categoryRef = null;
//            if(categoryType.equals(ITEM_TYPE.EXPENSE)) {
////                categoryRef = userRef.child("CategoriesExpense");
//                this._categoryType = ITEM_TYPE.EXPENSE;
//            } else if (categoryType.equals(ITEM_TYPE.INCOME)) {
////                categoryRef = userRef.child("CategoriesIncome");
//                this._categoryType = ITEM_TYPE.INCOME;
//            }

            String categoryID = userRef.push().getKey();
            _catID = categoryID;
            DatabaseReference childRef = userRef.child(categoryID);
            //send current object to database to save
            childRef.setValue(this, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if (databaseError != null) callback.apply(databaseError.getMessage());
                    else callback.apply("Parent Category Saved Successfully...");
                }
            });
        }
    }

    public void updateCategory( final Callback callback){
        if(Common._firebaseAuth.getCurrentUser() != null) {
            //Get the reference to the loggedin user
//            DatabaseReference userRef = Common._firebaseDB.getReference(Common._firebaseAuth.getCurrentUser().getUid());
//            DatabaseReference categoryRef = null;

            //Get the reference to the loggedin user
            DatabaseReference categoriesRef = Common._firebaseDB.getReference("ParentCategories");
            DatabaseReference userRef = categoriesRef.child(Common._firebaseAuth.getCurrentUser().getUid());
//            DatabaseReference categoryRef = null;
//            if(categoryType.equals(ITEM_TYPE.EXPENSE)) {
////                categoryRef = userRef.child("CategoriesExpense");
//                this._categoryType = ITEM_TYPE.EXPENSE;
//            } else if (categoryType.equals(ITEM_TYPE.INCOME)) {
////                categoryRef = userRef.child("CategoriesIncome");
//                this._categoryType = ITEM_TYPE.INCOME;
//            }
//            String expenseCategoryID = expenseCategoryRef.push().getKey();
//            _catID = expenseCategoryID;
            DatabaseReference childRef = userRef.child(_catID);
//            DatabaseReference childRef = expenseCategoryRef.child(expenseCategoryID);
            //send current object to database to save
            childRef.setValue(this, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if (databaseError != null) callback.apply(databaseError.getMessage());
                    else callback.apply("Parent Category Updated Successfully...");
                }
            });
        }
    }

    public void deleteCategory(final Callback callback){
        if(Common._firebaseAuth.getCurrentUser() != null) {
            //Get the reference to the loggedin user
//            DatabaseReference userRef = Common._firebaseDB.getReference(Common._firebaseAuth.getCurrentUser().getUid());
//            DatabaseReference categoryRef = null;

            DatabaseReference categoriesRef = Common._firebaseDB.getReference("ParentCategories");
            DatabaseReference userRef = categoriesRef.child(Common._firebaseAuth.getCurrentUser().getUid());

//            if(_categoryType.equals(CATEGORY_TYPE.EXPENSE)) {
//                categoryRef = userRef.child("CategoriesExpense");
//            } else if (_categoryType.equals(CATEGORY_TYPE.INCOME)) {
//                categoryRef = userRef.child("CategoriesIncome");
//            }
            DatabaseReference catRef = userRef.child(_catID);
//            DatabaseReference childRef = expenseCategoryRef.child(expenseCategoryID);
            //send current object to database to save
            catRef.removeValue(new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
//                    Toast.makeText(context,"", Toast.LENGTH_LONG).show();
                    if (databaseError != null) callback.apply(databaseError.getMessage());
                    else callback.apply("Parent Category Deleted Successfully...");
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
//        if (convertView == null) {
            view = inflater.inflate(R.layout.category_group_item_layout, null);
            // Do some initialization
//        } else {
//            view = convertView;
//        }

        TextView textName = view.findViewById(R.id.category_name);
//        TextView textDescription = view.findViewById(R.id.category_description);
        textName.setText(_categoryName);
//        textDescription.setText(_description);

        return view;
    }
}
