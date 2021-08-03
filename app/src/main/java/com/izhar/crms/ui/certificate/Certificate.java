package com.izhar.crms.ui.certificate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.izhar.crms.R;

public class Certificate extends Fragment {

    View root;
    AutoCompleteTextView kebele;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_certificate, container, false);
        kebele = root.findViewById(R.id.kebele);

        ArrayAdapter<CharSequence> ti = ArrayAdapter.createFromResource(getContext(), R.array.kebele, R.layout.list_item);
        kebele.setAdapter(ti);
        return root;
    }
}