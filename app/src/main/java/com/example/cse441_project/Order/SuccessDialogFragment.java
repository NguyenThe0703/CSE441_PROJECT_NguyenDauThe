package com.example.cse441_project.Order;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.cse441_project.R;

public class SuccessDialogFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_success_dialog, container, false);

        // Thiết lập nội dung cho dialog
        TextView messageTextView = view.findViewById(R.id.messageTextView);
        messageTextView.setText("In thành công");

        // Nút OK để đóng dialog
        Button okButton = view.findViewById(R.id.okButton);
        okButton.setOnClickListener(v -> dismiss());

        return view;
    }
}
