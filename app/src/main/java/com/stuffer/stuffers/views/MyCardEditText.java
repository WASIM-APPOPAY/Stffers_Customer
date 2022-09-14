package com.stuffer.stuffers.views;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import java.util.ArrayList;


public class MyCardEditText  extends AppCompatEditText {
    ArrayList<String> listOfPattern;
    public MyCardEditText(@NonNull Context context) {
        super(context);
        initialize(context);
    }

    public MyCardEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public MyCardEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context) {
        setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        //setKeyListener(null);
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        setSingleLine();

        addCardType();
    }

    private void addCardType() {
        listOfPattern = new ArrayList<String>();

        String ptVisa = "^4[0-9]{6,}$";
        listOfPattern.add(ptVisa);
        String ptMasterCard = "^5[1-5][0-9]{5,}$";
        listOfPattern.add(ptMasterCard);
        String ptAmeExp = "^3[47][0-9]{5,}$";
        listOfPattern.add(ptAmeExp);

        //below comment for testing purpose

        String ptDinClb = "^3(?:0[0-5]|[68][0-9])[0-9]{4,}$";
        listOfPattern.add(ptDinClb);
        String ptDiscover = "^6(?:011|5[0-9]{2})[0-9]{3,}$";
        listOfPattern.add(ptDiscover);
        String ptJcb = "^(?:2131|1800|35[0-9]{3})[0-9]{3,}$";
        listOfPattern.add(ptJcb);
    }


    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        String cardNumber = text.toString();
        cardNumber = cardNumber.replaceAll(" ", "");
        cardNumber = formatCardNumber(cardNumber);
        if (!cardNumber.equals(text.toString())) {
            setText(cardNumber);
            setSelection(length());
        }
    }

    private String formatCardNumber(String cardNumber) {
        StringBuilder formatted = new StringBuilder(cardNumber);

        for (int i = 1; i <= Math.floor(cardNumber.length() / 4f); i++) {
            formatted.insert(i * 5 - 1, " ");
        }
        return formatted.toString().trim();
    }

    public String getCardNumber() {
        return getText().toString().replaceAll(" ", "");
    }

    public int getCardType(String cardCCNumber) {
        int pos = -1;
        int counter = -1;
        for (String p : listOfPattern) {
            counter = 0;
            if (cardCCNumber.matches(p)) {
                pos = counter;
                break;
            }
            counter = counter + 1;
        }
        return pos;
    }


}
