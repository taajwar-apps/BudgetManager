package budgetmanager.tgs.com.budgetmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class Header implements Item{

    private final String         name;
//    HEADER_TYPE _headerType;

    public Header(String name) {
        this.name = name;
//        this._headerType = headerType;
    }

//    enum HEADER_TYPE{EXPENSE,INCOME}

//    public HEADER_TYPE get_headerType() {
//        return _headerType;
//    }

//    public void set_headerType(HEADER_TYPE _headerType) {
//        this._headerType = _headerType;
//    }

    @Override
    public int getViewType() {
        return FragmentCategories.RowType.HEADER_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
//        if (convertView == null) {
            view = inflater.inflate(R.layout.category_header_layout, null);
//            // Do some initialization
//        } else {
//            view = convertView;
//        }

        TextView text = view.findViewById(R.id.catHeaderTitle);
        text.setText(name);

        return view;
    }
    public String getName(){return name;}
}
