package com.stuffer.stuffers.fragments.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.AreaSelectListener;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyRadioButton;
import com.stuffer.stuffers.views.MyTextView;

import java.util.ArrayList;

public class AreaCodeDialog extends DialogFragment {
    private MyButton btnConfirm;
    private RecyclerView rvCarrier;
    private MyButton btnClose;
    private MyTextView tvTitle;
    private ArrayList<String> mListCarrier;
    private String mTitle;
    private boolean allow = false;
    private AreaSelectListener mListener;
    private int lastSelectedPosition = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_dialog_areacode, container, false);
        rvCarrier = (RecyclerView) view.findViewById(R.id.rvAreaCode);
        btnClose = (MyButton) view.findViewById(R.id.btnClose);
        tvTitle = (MyTextView) view.findViewById(R.id.tvTitle);
        btnConfirm = (MyButton) view.findViewById(R.id.btnConfirm);

        Bundle arguments = this.getArguments();
        mListCarrier = arguments.getStringArrayList(AppoConstants.INFO);
        mTitle = arguments.getString(AppoConstants.TITLE);
        tvTitle.setText(mTitle);
        rvCarrier.setLayoutManager(new LinearLayoutManager(getContext()));
        AreaAdapter adapter = new AreaAdapter(getContext(), mListCarrier);
        rvCarrier.setAdapter(adapter);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allow) {
                    mListener.onAreaSelected(lastSelectedPosition);
                } else {
                    Toast.makeText(getContext(), "Please select your Area Code ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.AreaHolder> {
        private Context mContext;
        private ArrayList<String> mList;

        private AreaSelectListener mListener;

        public AreaAdapter(Context mContext, ArrayList<String> mList) {
            this.mContext = mContext;
            this.mList = mList;
            try {
                this.mListener = (AreaSelectListener) mContext;
            } catch (ClassCastException e) {
                throw new ClassCastException("parent should implement AreaSelectListener");
            }
        }


        @NonNull
        @Override
        public AreaAdapter.AreaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_carrier_item, parent, false);
            return new AreaHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AreaAdapter.AreaHolder holder, int position) {
            holder.bindProcess();
        }


        @Override
        public int getItemCount() {
            return mList.size();
        }

        public class AreaHolder extends RecyclerView.ViewHolder {
            MyRadioButton rbtnCarrier;

            public AreaHolder(@NonNull View itemView) {
                super(itemView);
                rbtnCarrier = itemView.findViewById(R.id.rbtnCarrier);
                rbtnCarrier.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lastSelectedPosition = getAdapterPosition();
                        allow = true;
                        notifyDataSetChanged();


                    }
                });
            }

            public void bindProcess() {
                rbtnCarrier.setChecked(lastSelectedPosition == getAdapterPosition());
                rbtnCarrier.setText(mList.get(getAdapterPosition())+" Dominican Republic");
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            this.mListener = (AreaSelectListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("parent should implement AreaSelectListener");
        }

    }
}
