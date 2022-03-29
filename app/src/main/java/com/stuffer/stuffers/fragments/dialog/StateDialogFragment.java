package com.stuffer.stuffers.fragments.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.StateSelectListener;
import com.stuffer.stuffers.models.Country.State;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;

import java.util.ArrayList;

public class StateDialogFragment extends DialogFragment {
    MyEditText edSearch;
    RecyclerView rvState;
    MyButton btnClose;
    ArrayList<State> mState;
    private StateAdapter stateAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_state_dialog, container, false);
        Bundle arguments = this.getArguments();
        mState = arguments.getParcelableArrayList(AppoConstants.STATE);
        edSearch = view.findViewById(R.id.edSearch);
        rvState = view.findViewById(R.id.rvState);
        btnClose = view.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        rvState.setLayoutManager(new LinearLayoutManager(getContext()));
        stateAdapter = new StateAdapter(getActivity(), mState);
        rvState.setAdapter(stateAdapter);
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                //countryAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable charSequence) {
                stateAdapter.getFilter().filter(charSequence);
            }
        });

        return view;
    }

    public class StateAdapter extends RecyclerView.Adapter<StateAdapter.StateHolder> implements Filterable {
        ArrayList<State> mListState;
        ArrayList<State> mListStateFilter;
        Context mContext;
        StateSelectListener mListener;

        public StateAdapter(Context context, ArrayList<State> stateList) {
            this.mContext = context;
            this.mListState = stateList;
            this.mListStateFilter = stateList;
            try {
                mListener = (StateSelectListener) context;
            } catch (ClassCastException e) {
                throw new ClassCastException("Parent Activity must implement StateSelectListener");
            }
        }


        @NonNull
        @Override
        public StateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_state, parent, false);
            return new StateHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull StateHolder holder, int position) {
            holder.tvState.setText(mListStateFilter.get(position).getStatename());
        }

        @Override
        public int getItemCount() {
            return mListStateFilter.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        mListStateFilter = mListState;
                    } else {
                        ArrayList<State> tempFilterList = new ArrayList<>();
                        for (State mState : mListState) {
                            if (mState.getStatename().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                                tempFilterList.add(mState);
                            }
                        }
                        mListStateFilter = tempFilterList;
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = mListStateFilter;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    mListStateFilter = (ArrayList<State>) results.values;
                    notifyDataSetChanged();
                }
            };
        }

        public class StateHolder extends RecyclerView.ViewHolder {
            MyTextView tvState;

            public StateHolder(@NonNull View itemView) {
                super(itemView);
                tvState = itemView.findViewById(R.id.tvState);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onStateSelected(mListStateFilter.get(getAdapterPosition()).getStatename(), mListStateFilter.get(getAdapterPosition()).getId());
                        dismiss();
                    }
                });
            }



        }
    }
}
