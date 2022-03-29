package com.stuffer.stuffers.fragments.finance_fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.OptionSelectListener;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;

public class BankChildFragment extends Fragment {
    private OptionSelectListener mListenerSelect;
    private View mView;
    private LinearLayout layoutOpen, layoutRequest,layoutExists;
    private MyTextView tvText1, tvText2, tvText3, tvText4, tvText5;
    private MyButton btnProceed;
    private MyTextViewBold tvHeadingOpenAccount;

    public BankChildFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_bank_child, container, false);
        tvHeadingOpenAccount = (MyTextViewBold)mView.findViewById(R.id.tvHeadingOpenAccount);
        layoutOpen = (LinearLayout) mView.findViewById(R.id.layoutOpen);
        btnProceed = (MyButton) mView.findViewById(R.id.btnProceed);

        //layoutRequest = (LinearLayout) mView.findViewById(R.id.layoutRequest);
        //layoutExists = (LinearLayout) mView.findViewById(R.id.layoutExists);
        tvHeadingOpenAccount.setText(getString(R.string.info_open_account1));

        tvText1 = (MyTextView) mView.findViewById(R.id.tvText1);
        tvText2 = (MyTextView) mView.findViewById(R.id.tvText2);
        tvText3 = (MyTextView) mView.findViewById(R.id.tvText3);
        tvText4 = (MyTextView) mView.findViewById(R.id.tvText4);
        tvText5 = (MyTextView) mView.findViewById(R.id.tvText5);
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListenerSelect.onSelectConfirm(AppoConstants.OPEN_ACCOUNT, null);
            }
        });




        /**
         * android:text="1.	Copia de cédula o pasaporte (el pasaporte se le debe sacar copia de la página(S) donde aparezca la fotografía, firma y generales de la persona y la pagina donde se encuentre estampado y sello de ingreso al país)"
         * android:text="2.	Completar y firmar Declaración Jurada de Ingresos."
         * android:text="3.	Copia de un recibo de utilidad."
         * android:text="4.	Completar formularios bancarios."
         * android:text="**Nota: El cliente podrá mantener hasta un monto de $5,000.00 en su cuenta. Los depósitos y retiros mensuales acumulados no podrán exceder los mil quinientos balboas (B/. 1,500.00)."
         */

        String text1 = "<b><font color='#000000'>" + "1." + "</font></b>" + getString(R.string.info_open_account2);
        String text2 = "<b><font color='#000000'>" + "2." + "</font></b>" + getString(R.string.info_open_account3);
        String text3 = "<b><font color='#000000'>" + "3." + "</font></b>" + getString(R.string.info_open_account4);
        String text4 = "<b><font color='#000000'>" + "4." + "</font></b>" + getString(R.string.info_open_account5);
        String text5 = "<b><font color='#F61C46'>" + "**Nota:  " + "</font></b>" + getString(R.string.info_open_account_note1)+" "+"<b><font color='#F61C46'>"+ getString(R.string.info_open_account_min_amount)+" "+"</font></b>" +getString(R.string.info_open_account_note2)+"<b><font color='#F61C46'>"+getString(R.string.info_open_account_max_amount)+"</font></b>"+").";

        tvText1.setText(Html.fromHtml(text1));

        tvText2.setText(Html.fromHtml(text2));
        tvText3.setText(Html.fromHtml(text3));
        tvText4.setText(Html.fromHtml(text4));
        tvText5.setText(Html.fromHtml(text5));


        return mView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListenerSelect = (OptionSelectListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("parent should implement OptionSelectListener Thank you!");
        }
    }
}