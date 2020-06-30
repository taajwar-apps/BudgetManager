package budgetmanager.tgs.com.budgetmanager;

import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    public static String getMonth(){
        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.MONTH, -2);
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter = new SimpleDateFormat("MM-yyyy");

//        try {
//                Date date = formatter.parse("2018-12-15");
//                calendar.setTime(date);
//                calendar.add(Calendar.DATE, 5);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
        return formatter.format(calendar.getTime()).toString();
    }
}
