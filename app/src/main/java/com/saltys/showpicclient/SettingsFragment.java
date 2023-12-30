package com.saltys.showpicclient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsFragment extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate( R.layout.dial_setting, container, false );
        LinearLayout getSecretKeyLayout = (LinearLayout) view.findViewById( R.id.getSecretKeyLayout );

        getSecretKeyLayout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).showSecret();
            }
        } );

        SwitchMaterial privacyToggle = view.findViewById(R.id.privacyToggle);
        privacyToggle.setChecked(false);
        privacyToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });


        LinearLayout logoutLayout = (LinearLayout) view.findViewById( R.id.logoutLayout );
        logoutLayout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).logoutUser();
            }
        } );

        LinearLayout exitLayout = (LinearLayout) view.findViewById( R.id.exitLayout );
        exitLayout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).exit();
            }
        } );


        return view;
    }

}

