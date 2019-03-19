package budgetmanager.tgs.com.budgetmanager;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import budgetmanager.tgs.com.budgetmanager.Model.Category;
import budgetmanager.tgs.com.budgetmanager.Model.CategoryGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentCategories.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentCategories#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCategories extends Fragment implements AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 90;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Button _btnAddCategory;
    Button _btnSaveCategory;
    Button _btnUpdateCategory;
    Button _btnDeleteCategory;
    Button _btnCancel;
    Button _btnSaveCategoryGroup;
    Button _btnUpdateCategoryGroup;
    Button _btnDeleteCategoryGroup;
    Button _btnCategoryGroupCancel;

    ListView _listViewCategories;
    BaseAdapter _listAdapter;
    Category selectedCategory;
    CategoryGroup _selectedParentCategory;
//    List<Item> _listCategories = new ArrayList<Item>();
//    List<Item> _listCategoriesGroup = new ArrayList<Item>();;
    Item.ITEM_TYPE _selectedType = Item.ITEM_TYPE.EXPENSE;

//    boolean _categoriesLoaded = false;
//    boolean _categoriesGroupLoaded = false;
    ProgressDialog progressDialog;

    public FragmentCategories() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_categories, container, false);
        ((TextView)view.findViewById(R.id.custom_header)).setText("Categories");
        _listViewCategories = view.findViewById(R.id.list_view_categories);
        _btnAddCategory = view.findViewById(R.id.btnAddExpenseCategory);
        _btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              showAddCategoryPopupWindow();
//                sendSMS();
            }
        });
        _listAllCategories = new ArrayList<Item>();
        showList();
//        showLoader();
//        Common.setCustomActionBar((AppCompatActivity)getActivity(), container, inflater, "Categories",0);
        return view;//inflater.inflate(R.layout.fragment_categories, container, false);
    }

    void sendSMS(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getContext().checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.SEND_SMS)) {

                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
                }
            } else
                sendNow();
        } else
            sendNow();
    }

    void sendNow(){
        Toast.makeText(getContext(), "Sending SMS", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(),0,intent,0);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("+923327113477",null, "Hellow SMS", null,null);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Toast.makeText(getContext(), "Grant Results : "+grantResults.length, Toast.LENGTH_LONG).show();
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_LONG).show();
            sendNow();
        } else {

            Toast.makeText(getContext(), "Permission Denied",
                    Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    //=============================Category Section Start=================================
    PopupWindow _popupWindowAddCategory, popupWindowAddCategoryGroup;
    private void showAddCategoryPopupWindow(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.add_category_popup_window, null, false);
        _popupWindowAddCategory = new PopupWindow(popupView,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,true);
        _popupWindowAddCategory.showAtLocation(popupView, Gravity.CENTER,0,0);
        _btnSaveCategory = popupView.findViewById(R.id.btnSaveCategory);
        _btnUpdateCategory = popupView.findViewById(R.id.btnUpdateCategory);
        _btnDeleteCategory = popupView.findViewById(R.id.btnDeleteCategory);
        _btnCancel = popupView.findViewById(R.id.btnCancel);
        _btnSaveCategory.setVisibility(View.VISIBLE);
        _btnUpdateCategory.setVisibility(View.INVISIBLE);
        _btnDeleteCategory.setVisibility(View.INVISIBLE);
        _btnSaveCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCategory(popupView, _popupWindowAddCategory);
            }
        });
        _btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _popupWindowAddCategory.dismiss();
            }
        });
        Button btnAddParentCategory = popupView.findViewById(R.id.btnAddParentCategory);
        btnAddParentCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCategoryGroupPopupWindow();
            }
        });
        RadioGroup radioCategoryGroup = popupView.findViewById(R.id.radioCategoryGroup);
        radioCategoryGroup.setVisibility(View.VISIBLE);
        radioCategoryGroup.check(R.id.radioBtnExpense);
        _selectedType = Item.ITEM_TYPE.EXPENSE;
        radioCategoryGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radioBtnExpense){
                    _selectedType = Item.ITEM_TYPE.EXPENSE;
                    populateSpinner(popupView, _selectedType,"");
                }
                else if(checkedId == R.id.radioBtnIncome) {
                    _selectedType = Item.ITEM_TYPE.INCOME;
                    populateSpinner(popupView, _selectedType,"");
                }
            }
        });
        populateSpinner(popupView,_selectedType, "");
    }

    private void showUpdateCategoryPopupWindow(Category category){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.add_category_popup_window, null, false);
        final PopupWindow popupWindow = new PopupWindow(popupView,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER,0,0);
        selectedCategory = category;
        EditText editTextCatName = popupView.findViewById(R.id.editTextCategoryName);
        EditText editTextCatDescription = popupView.findViewById(R.id.editTextCategoryDescription);
        editTextCatName.setText(selectedCategory.get_categoryName());
        editTextCatDescription.setText(selectedCategory.get_description());
        final Item.ITEM_TYPE categoryType = selectedCategory.get_categoryType();
        RadioGroup radioCategoryGroup = popupView.findViewById(R.id.radioCategoryGroup);
        radioCategoryGroup.setVisibility(View.GONE);
        _btnSaveCategory = popupView.findViewById(R.id.btnSaveCategory);
        _btnUpdateCategory = popupView.findViewById(R.id.btnUpdateCategory);
        _btnDeleteCategory = popupView.findViewById(R.id.btnDeleteCategory);
        _btnCancel = popupView.findViewById(R.id.btnCancel);
        _btnSaveCategory.setVisibility(View.GONE);
        _btnUpdateCategory.setVisibility(View.VISIBLE);
        _btnDeleteCategory.setVisibility(View.VISIBLE);

        populateSpinner(popupView,categoryType, getCatGroupNameForID(selectedCategory.get_parentCategory()));

        _btnUpdateCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCategory(popupView, popupWindow, categoryType);
            }
        });
        _btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        _btnDeleteCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCategory.deleteCategory(new Callback(){

                    @Override
                    public void apply(String msg) {
                        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
                        popupWindow.dismiss();
                        _listAllCategories.remove(selectedCategory);
                        _listAdapter.notifyDataSetChanged();
                        
                    }
                });
            }
        });
    }

    private void saveCategory(View popupView, final PopupWindow popupWindow){
        String categoryName = ((EditText)popupView.findViewById(R.id.editTextCategoryName)).getText().toString();
        int selectedItemPosition = ((Spinner)popupView.findViewById(R.id.spinnerParentCategory)).getSelectedItemPosition();
        final CategoryGroup catGroup = _categoryGroupsInSpinner.get(selectedItemPosition);
        String catGroupID = catGroup.get_catID();//(_categoryGroupsInSpinner.get(selectedItemPosition)).get_catID();
        String categoryDescription = ((EditText)popupView.findViewById(R.id.editTextCategoryDescription)).getText().toString();
        final Category category = new Category(categoryName, catGroupID, categoryDescription);
        RadioGroup radioCategoryGroup = popupView.findViewById(R.id.radioCategoryGroup);
        int selectedRadioBtnID = radioCategoryGroup.getCheckedRadioButtonId();
        switch (selectedRadioBtnID){
            case R.id.radioBtnExpense:
                _selectedType = Item.ITEM_TYPE.EXPENSE;
                break;
            case R.id.radioBtnIncome:
                _selectedType = Item.ITEM_TYPE.INCOME;
                break;
                default:
                    ShowToast("Please Select Category Type");
                    return;
        }
        category.saveCategory(_selectedType, new Callback() {
            @Override
            public void apply(String msg) {
                Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                populateListView();
            }
        });
        popupWindow.dismiss();
    }

    private void updateCategory(View popupView, final PopupWindow popupWindow, Item.ITEM_TYPE categoryType){
        String categoryName = ((EditText)popupView.findViewById(R.id.editTextCategoryName)).getText().toString();
        int selectedItemPosition = ((Spinner)popupView.findViewById(R.id.spinnerParentCategory)).getSelectedItemPosition();
        final CategoryGroup catGroup = _categoryGroupsInSpinner.get(selectedItemPosition);
        String catGroupID = catGroup.get_catID();//(_categoryGroupsInSpinner.get(selectedItemPosition)).get_catID();
        String categoryDescription = ((EditText)popupView.findViewById(R.id.editTextCategoryDescription)).getText().toString();
        selectedCategory.set_categoryName(categoryName);
        selectedCategory.set_parentCategory(catGroupID);
        selectedCategory.set_description(categoryDescription);
        selectedCategory.updateCategory(categoryType, new Callback() {
            @Override
            public void apply(String msg) {
                Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                populateListView();
            }
        });
    }
    //=============================Category Section End==================================

    //=============================Parent Category Section Start=================================
    private void showAddCategoryGroupPopupWindow(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.add_category_group_popup_window, null, false);
        popupWindowAddCategoryGroup = new PopupWindow(popupView,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,true);
        popupWindowAddCategoryGroup.showAtLocation(popupView, Gravity.CENTER,0,0);
        _btnSaveCategoryGroup = popupView.findViewById(R.id.btnSaveParentCategory);
        _btnUpdateCategoryGroup = popupView.findViewById(R.id.btnUpdateParentCategory);
        _btnDeleteCategoryGroup = popupView.findViewById(R.id.btnDeleteParentCategory);
        _btnCategoryGroupCancel = popupView.findViewById(R.id.btnCancel);
        _btnSaveCategoryGroup.setVisibility(View.VISIBLE);
        _btnUpdateCategoryGroup.setVisibility(View.INVISIBLE);
        _btnDeleteCategoryGroup.setVisibility(View.INVISIBLE);

        _btnSaveCategoryGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCategoryGroup(popupView, popupWindowAddCategoryGroup);
            }
        });
        _btnCategoryGroupCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowAddCategoryGroup.dismiss();
                _popupWindowAddCategory.getContentView().setVisibility(View.VISIBLE);
            }
        });
        _popupWindowAddCategory.getContentView().setVisibility(View.GONE);
    }

    private void showUpdateCategoryGroupPopupWindow(CategoryGroup categoryGroup){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.add_category_group_popup_window, null, false);
        final PopupWindow popupWindow = new PopupWindow(popupView,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER,0,0);
        _selectedParentCategory = categoryGroup;
        EditText editTextCatName = (EditText) popupView.findViewById(R.id.editTextParentCategoryName);
        editTextCatName.setText(_selectedParentCategory.get_categoryName());
        _btnSaveCategoryGroup = popupView.findViewById(R.id.btnSaveParentCategory);
        _btnUpdateCategoryGroup = popupView.findViewById(R.id.btnUpdateParentCategory);
        _btnDeleteCategoryGroup = popupView.findViewById(R.id.btnDeleteParentCategory);
        _btnCategoryGroupCancel = popupView.findViewById(R.id.btnCancel);
        _btnSaveCategoryGroup.setVisibility(View.INVISIBLE);
        _btnUpdateCategoryGroup.setVisibility(View.VISIBLE);
        _btnDeleteCategoryGroup.setVisibility(View.VISIBLE);

        _btnUpdateCategoryGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCategoryGroup(popupView, popupWindow);
            }
        });
        _btnCategoryGroupCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        _btnDeleteCategoryGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _selectedParentCategory.deleteCategory(new Callback(){

                    @Override
                    public void apply(String msg) {
                        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
                        popupWindow.dismiss();
                    }
                });
            }
        });
    }

    private void saveCategoryGroup(final View popupView, final PopupWindow popupWindow){
        String categoryName = ((EditText)popupView.findViewById(R.id.editTextParentCategoryName)).getText().toString();
        final CategoryGroup categoryGroup = new CategoryGroup(categoryName, _selectedType);

        categoryGroup.saveCategory(new Callback() {
            @Override
            public void apply(String msg) {
                Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                _popupWindowAddCategory.getContentView().setVisibility(View.VISIBLE);
                populateSpinner(_popupWindowAddCategory.getContentView(), _selectedType, categoryGroup.get_categoryName());
            }
        });
        popupWindow.dismiss();
    }

    private void updateCategoryGroup(View popupView, final PopupWindow popupWindow){
        String categoryName = ((EditText)popupView.findViewById(R.id.editTextParentCategoryName)).getText().toString();
        _selectedParentCategory.set_categoryName(categoryName);
        _selectedParentCategory.updateCategory( new Callback() {
            @Override
            public void apply(String msg) {
                Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            }
        });
    }

    //=============================Parent Category Section End==================================


    void showList(){
        populateListView();
    }

    public enum RowType {
        LIST_ITEM, HEADER_ITEM
    }


    List<Item> _listAllCategories;// = new ArrayList<Item>();
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
                public Item getItem(int position) {
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

                    view = getItem(position).getView(inflater, convertView);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                                showUpdateCategoryPopupWindow(position);
                            Item item = getItem(position);
                            if (item.getClass().equals(Category.class))
                                showUpdateCategoryPopupWindow((Category) item);
                            else if (item.getClass().equals(CategoryGroup.class))
                                showUpdateCategoryGroupPopupWindow((CategoryGroup) item);
//                                    Toast.makeText(getContext(),""+((Category)item).get_categoryName(), Toast.LENGTH_LONG).show();
                        }
                    });
                    return view;
                }
            };
        }
        _listAllCategories.clear();
        _listAllCategories.add(new Header("Expense Categories"));
        for (Item parentCategory:Common._listCategoriesGroup){
            List<Item> categories = new ArrayList<Item>();
            if(((CategoryGroup)parentCategory).get_categoryType().equals(Item.ITEM_TYPE.EXPENSE)) {
                for (Item category : Common._listCategories) {
                    if (((Category) category).get_categoryType().equals(Item.ITEM_TYPE.EXPENSE))
                        if (((Category) category).get_parentCategory().equals(((CategoryGroup) parentCategory).get_catID()))
                            categories.add(category);
                }
            }
            if(!categories.isEmpty()){
                _listAllCategories.add(parentCategory);
                _listAllCategories.addAll(categories);
            }
        }
        _listAllCategories.add(new Header("Income Categories"));
        for (Item parentCategory:Common._listCategoriesGroup){
            List<Item> categories = new ArrayList<Item>();
            if(((CategoryGroup)parentCategory).get_categoryType().equals(Item.ITEM_TYPE.INCOME)) {
                for (Item category : Common._listCategories) {
                    if (((Category) category).get_categoryType().equals(Item.ITEM_TYPE.INCOME))
                        if (((Category) category).get_parentCategory().equals(((CategoryGroup) parentCategory).get_catID()))
                            categories.add(category);
                }
            }
            if(!categories.isEmpty()){
                _listAllCategories.add(parentCategory);
                _listAllCategories.addAll(categories);
            }
        }

//        Common._listCategories.add(new Header("Expense Categories"));
//                Common._listCategories.addAll(listExpenseCategories);
//                Common._listCategories.add(new Header("Income Categories"));
//                Common._listCategories.addAll(listIncomeCategories);



//                _listAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1) {
//                    @Override
//                    public boolean areAllItemsEnabled() {
//                        return true;
//                    }
//
//                    @Override
//                    public boolean isEnabled(int position) {
//                        return false;
//                    }
//
//                    @Override
//                    public void registerDataSetObserver(DataSetObserver observer) { }
//
//                    @Override
//                    public void unregisterDataSetObserver(DataSetObserver observer) { }
//
//                    @Override
//                    public int getCount() {
//                        return _listAllCategories.size();
//                    }
//
//                    @Override
//                    public Object getItem(int position) {
//                        return _listAllCategories.get(position);
//                    }
//
//                    @Override
//                    public long getItemId(int position) {
//                        return position;
//                    }
//
//                    @Override
//                    public boolean hasStableIds() {
//                        return false;
//                    }
//
//                    @Override
//                    public View getView(final int position, View convertView, ViewGroup parent) {
//                        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                        View view = ((Item)getItem(position)).getView(inflater, convertView);
//                        view.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
////                                showUpdateCategoryPopupWindow(position);
//                                Item item = ((Item)getItem(position));
//                                if(item.getClass().equals(Category.class))
//                                    showUpdateCategoryPopupWindow((Category) item);
//                                else if (item.getClass().equals(CategoryGroup.class))
//                                    showUpdateCategoryGroupPopupWindow((CategoryGroup)item);
////                                    Toast.makeText(getContext(),""+((Category)item).get_categoryName(), Toast.LENGTH_LONG).show();
//                            }
//                        });
//                        return view;//((Item)getItem(position)).getView(inflater, convertView);
//                    }
//
//                    @Override
//                    public int getItemViewType(int position) {
//                        Item item = (Item)getItem(position);
//
//                        return item.getViewType();
//                    }
//
//                    @Override
//                    public int getViewTypeCount() {
//                        return 2;
//                    }
//
//                    @Override
//                    public boolean isEmpty() {
//                        return false;
//                    }
//                };

                if(updateAdapter)
                    _listAdapter.notifyDataSetChanged();
                else
                    _listViewCategories.setAdapter(_listAdapter);
    }

    List<CategoryGroup> _categoryGroupsInSpinner = new ArrayList<CategoryGroup>();
    void populateSpinner(final View view, Item.ITEM_TYPE item_type, String selectionValue){
        final Spinner spinner = view.findViewById(R.id.spinnerParentCategory);

        // Spinner click listener
        spinner.setOnItemSelectedListener(FragmentCategories.this);


        // Creating adapter for spinner
        List<String> spinnerAdapter = new ArrayList<String>();
        _categoryGroupsInSpinner.clear();
        for(Item categoryGroup:Common._listCategoriesGroup) {
            if (((CategoryGroup)categoryGroup).get_categoryType().equals(item_type)) {
                spinnerAdapter.add(((CategoryGroup) categoryGroup).get_categoryName());
                _categoryGroupsInSpinner.add(((CategoryGroup)categoryGroup));
            }
        }
        spinnerAdapter.add("Button");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerAdapter){
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                if (getItem(position) == "Button"){
                    Button b = new Button(getActivity());
                    b.setTextColor(Color.BLACK);
                    TextView tv = new TextView(getActivity());
                    tv.setText("Add New Category Group");
                    tv.setTextColor(Color.CYAN);
                    tv.setPadding(50,0,0,0);
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showAddCategoryGroupPopupWindow();
                            try {
                                Method method = Spinner.class.getDeclaredMethod("onDetachedFromWindow");
                                method.setAccessible(true);
                                method.invoke(spinner);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    return tv;
                }
//                if(convertView == null){
//                    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item, parent, false);
//                View row = inflater.inflate(R.layout.list_item, parent,false);
//                }
                TextView lblCategory = (TextView) row.findViewById(R.id.tv_spinnervalue);
                lblCategory.setText(getItem(position).toString());
                return row;
//                return LayoutInflater.from(this.getContext()).inflate(
//                        android.R.layout.simple_spinner_dropdown_item, parent,
//                        false);
            }
        };

        // Drop down layout style - list view with radio button  |  simple_spinner_item
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(dataAdapter.getPosition(selectionValue));
        spinner.setPrompt("Select Category Group");
    }

    String getCatGroupNameForID(String catGroupID){

        for (Item categoryGroup:Common._listCategoriesGroup){
            if(((CategoryGroup)categoryGroup).get_catID().equals(catGroupID))
                return ((CategoryGroup)categoryGroup).get_categoryName();
        }

//        for (int i=0; i<spinner.getCount();i++){
//            if(spinner.getChildAt(i).equals(value)){
//                return i;
//            }
//        }
        return ((CategoryGroup)Common._listCategoriesGroup.get(0)).get_categoryName();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        long parentCategoryID = ((Spinner)parent.findViewById(R.id.spinnerParentCategory)).getSelectedItemPosition();
//        Toast.makeText(getContext(),"ID : "+parentCategoryID,Toast.LENGTH_LONG).show();
//        Toast.makeText(getContext(),"Name : "+((CategoryGroup)Common._listCategoriesGroup.get(position)).get_categoryName()+" , ID : "+((CategoryGroup)Common._listCategoriesGroup.get(position)).get_catID(), Toast.LENGTH_LONG).show();
//        Toast.makeText(getContext(),"Name : "+parent.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
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
