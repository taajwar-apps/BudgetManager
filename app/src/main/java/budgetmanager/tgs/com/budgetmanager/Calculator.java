package budgetmanager.tgs.com.budgetmanager;

import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.*;
import java.util.List;

public class Calculator extends PopupWindow {


    Context _context;
    View view;

    TextView tvNumber, tvTotalNumber;
    float _currentNumber, _lastNumber;
    String _currentValue = "";
    String _lastValue = "";
    ArrayList<String> _listValues = new ArrayList<String>();
    ArrayList<String> _listTemp ;//= new ArrayList<String>();

    public Calculator(Context context) {
        super(context);
        _context = context;
        View popupView = LayoutInflater.from(context).inflate(R.layout.calculator_popup, null);
        view = popupView;
        setContentView(popupView);

        tvNumber = (TextView) popupView.findViewById(R.id.textView1);
        tvTotalNumber = (TextView) popupView.findViewById(R.id.textView2);
        numbersButtonClick((Button) popupView.findViewById(R.id.btn_0));
        numbersButtonClick((Button) popupView.findViewById(R.id.btn_1));
        numbersButtonClick((Button) popupView.findViewById(R.id.btn_2));
        numbersButtonClick((Button) popupView.findViewById(R.id.btn_3));
        numbersButtonClick((Button) popupView.findViewById(R.id.btn_4));
        numbersButtonClick((Button) popupView.findViewById(R.id.btn_5));
        numbersButtonClick((Button) popupView.findViewById(R.id.btn_6));
        numbersButtonClick((Button) popupView.findViewById(R.id.btn_7));
        numbersButtonClick((Button) popupView.findViewById(R.id.btn_8));
        numbersButtonClick((Button) popupView.findViewById(R.id.btn_9));
        numbersButtonClick((Button) popupView.findViewById(R.id.btn_dot));
        actionsButtonClick((Button) popupView.findViewById(R.id.btn_plus));
        actionsButtonClick((Button) popupView.findViewById(R.id.btn_minus));
        actionsButtonClick((Button) popupView.findViewById(R.id.btn_multiply));
        actionsButtonClick((Button) popupView.findViewById(R.id.btn_division));

        ((Button) popupView.findViewById(R.id.btn_clear)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvNumber.setText(null);
                tvTotalNumber.setText(null);
                _currentValue = "";
            }
        });
        ((Button) popupView.findViewById(R.id.btn_OK)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(_callback != null) {
                    float val = 0;
                    if(!tvTotalNumber.getText().toString().isEmpty())
                        val = Float.parseFloat(tvTotalNumber.getText().toString());
                    _callback.result(val);
                }
            }
        });
    }

    Callback _callback;
    public void show(Callback callback){
        _callback = callback;
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(getContentView().getResources().getDrawable(R.drawable.calculator_popup_bg));
        showAtLocation(view, Gravity.CENTER,0,0);
        getContentView().setElevation(20);

    }

    public void numbersButtonClick(final Button button){

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typedValue = (String) button.getText();
                String value = "";
                switch(typedValue){
                    case  "0":
                        value = _currentValue+"0";
                        break;
                    case  "1":
                        value = _currentValue+"1";
                        break;
                    case  "2":
                        value = _currentValue+"2";
                        break;
                    case  "3":
                        value = _currentValue+"3";
                        break;
                    case  "4":
                        value = _currentValue+"4";
                        break;
                    case  "5":
                        value = _currentValue+"5";
                        break;
                    case  "6":
                        value = _currentValue+"6";
                        break;
                    case  "7":
                        value = _currentValue+"7";
                        break;
                    case  "8":
                        value = _currentValue+"8";
                        break;
                    case  "9":
                        value = _currentValue+"9";
                        break;
                    case  ".":
                        value = _currentValue+".";
                        break;
                }

                    if(_currentValue.equals("")) {
                        _listValues.add(value);
//                        duplicateList();
                        _listTemp = new ArrayList<>(_listValues);
//                        tvTotalNumber.setText(calculateDivisions(_listValues));
//                        tvTotalNumber.setText(calculateMultiplies(_listValues));
//                        tvTotalNumber.setText(calculatePluses(_listValues));
//                        tvTotalNumber.setText(calculateMinuses(_listValues));

                        calculateDivisions(_listTemp);
                        calculateMultiplies(_listTemp);
                        calculateMinuses(_listTemp);
                        calculatePluses(_listTemp);
                        tvTotalNumber.setText(_listTemp.get(0));
                    } else {
                        _listValues.set(_listValues.size()-1, value);
//                        duplicateList();
                        _listTemp = new ArrayList<>(_listValues);
//                        tvTotalNumber.setText(calculateDivisions(_listValues));
//                        tvTotalNumber.setText(calculateMultiplies(_listValues));
//                        tvTotalNumber.setText(calculatePluses(_listValues));
//                        tvTotalNumber.setText(calculateMinuses(_listValues));

                        calculateDivisions(_listTemp);
                        calculateMultiplies(_listTemp);
                        calculateMinuses(_listTemp);
                        calculatePluses(_listTemp);
                        tvTotalNumber.setText(_listTemp.get(0));
                    }

                _currentValue = value;
                _currentNumber = Float.parseFloat(_currentValue);
                tvNumber.setText(_lastValue+_currentValue);

            }
        });
    }

    void duplicateList(){
        _listTemp = new ArrayList<>(_listValues);
        for (String val : _listValues){
            _listTemp.add(val);
        }
    }

    String _lastSymbol = "";
    public void actionsButtonClick(final Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _lastSymbol = button.getText().toString();
                _currentNumber = 0;
                _lastValue = _lastValue+_currentValue+_lastSymbol;
                tvNumber.setText(_lastValue);
                _listValues.add(_lastSymbol);
                _currentValue = "";
            }
        });
    }

    //=============================================================

    void calculateDivisions(ArrayList<String> list){
//        Log.d("MLog", "Size : "+_listValues.size());
        if(list.contains("÷")){
            int index = list.indexOf("÷");
            float firstNumber = Float.parseFloat(list.get(index - 1));
            float lastNumber = Float.parseFloat(list.get(index + 1));
            float resultNumber = firstNumber / lastNumber;
            list.set(index - 1, String.valueOf(resultNumber));
            list.subList(index, index+2).clear();
//            return calculateDivisions(list);
            calculateDivisions(list);
        }
//        return list.get(0);
    }

    void calculateMultiplies(ArrayList<String> list){
//        Log.d("MLog", "Multiply : "+_listValues.size());
        if(list.contains("x")){
            int index = list.indexOf("x");
            float firstNumber = Float.parseFloat(list.get(index - 1));
            float lastNumber = Float.parseFloat(list.get(index + 1));
            float resultNumber = firstNumber * lastNumber;
            list.set(index - 1, String.valueOf(resultNumber));
            list.subList(index, index+2).clear();
            calculateMultiplies(list);
        }
//        return list.get(0);
    }

    void calculatePluses(ArrayList<String> list){
//        Log.d("MLog", "Plus : "+_listValues.size());
        if(list.contains("+")){
            int index = list.indexOf("+");
            float firstNumber = Float.parseFloat(list.get(index - 1));
            float lastNumber = Float.parseFloat(list.get(index + 1));
            float resultNumber = firstNumber + lastNumber;
            list.set(index - 1, String.valueOf(resultNumber));
            list.subList(index, index+2).clear();
//            return calculatePluses(list);
            calculatePluses(list);
        }
//        return list.get(0);
    }

    void calculateMinuses(ArrayList<String> list){
//        Log.d("MLog", "Minus : "+_listValues.size());
        if(list.contains("-")){
            int index = list.indexOf("-");
            float firstNumber = Float.parseFloat(list.get(index - 1));
            float lastNumber = Float.parseFloat(list.get(index + 1));
            float resultNumber = firstNumber - lastNumber;
            list.set(index - 1, String.valueOf(resultNumber));
            list.subList(index, index+2).clear();
             calculateMinuses(list);
        }
//        return list.get(0);
    }

    //============================================================
}
