package budgetmanager.tgs.com.budgetmanager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
/////////
 */
public class FragmentBudget extends Fragment {


    public FragmentBudget() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_budget, container, false);
//        Common.setCustomActionBar((AppCompatActivity)getActivity(), container, inflater, "Budget", 0);
        ((TextView)rootView.findViewById(R.id.custom_header)).setText("Budget");
        return rootView;//inflater.inflate(R.layout.fragment_budget, container, false);
    }

}
