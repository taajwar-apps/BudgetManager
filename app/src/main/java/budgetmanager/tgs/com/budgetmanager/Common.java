package budgetmanager.tgs.com.budgetmanager;

import android.support.design.widget.BottomNavigationView;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import budgetmanager.tgs.com.budgetmanager.Model.Account;

public class Common {
//=====================================================================================
    public static final FirebaseDatabase _firebaseDB  = FirebaseDatabase.getInstance();
    public static final FirebaseAuth _firebaseAuth = FirebaseAuth.getInstance();
    public static final List<ICatItem> _listCategories = new ArrayList<ICatItem>();
    public static final List<ICatItem> _listCategoriesGroup = new ArrayList<ICatItem>();
    public static final List<Account> _listAccounts = new ArrayList<Account>();
    public static BottomNavigationView navigation;


//    public static void setCustomActionBar(AppCompatActivity activity, ViewGroup viewGroup, LayoutInflater inflater, String title, int layout){
//        activity.getSupportActionBar().setDisplayShowHomeEnabled(false);
//        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
//        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
//        View header = null;
//        if(layout == 0) {
//            header = inflater.inflate(R.layout.activities_header_layout, viewGroup, false);
//            TextView txtHeader = header.findViewById(R.id.custom_header);
//            txtHeader.setText(title);
//        } else
//            header = inflater.inflate(layout, viewGroup,false);
//
//        activity.getSupportActionBar().setCustomView(header);//.setTitle("Accounts");
//        Toolbar parent = (Toolbar) header.getParent();
//        parent.setContentInsetsAbsolute(0, 0);
//    }

    public static void showNavigationView(){
        navigation.setVisibility(View.VISIBLE);
    }
    public static void hideNavigationView(){
        navigation.setVisibility(View.INVISIBLE);
    }

}
