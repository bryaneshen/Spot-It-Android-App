package ca.cmpt276.finddamatch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

import ca.cmpt276.finddamatch.model.OptionsManager;

/**
 * Fragment used to display a dialog asking the user if they want to save their high scores when
 * they finish a game.
 */
// class inspired by https://www.youtube.com/watch?v=ARezg1D9Zd0
public class CongratulationsMessageFragment extends AppCompatDialogFragment {
    private EditText highScoreName;
    private MessageFragmentListener listener;
    private OptionsManager options = OptionsManager.getInstance();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.congratulations_dialog_layout, null);

        builder.setView(view)
                .setTitle(this.getString(R.string.dialog_title_congratulations))
                .setCancelable(false)
                .setNegativeButton(this.getString(R.string.dialog_cancel), (dialog, which) -> {
                    if (options.getIsExportSelected()) {
                        Toast.makeText(getContext(), getString(R.string.export_toast), Toast.LENGTH_SHORT).show();
                    }
                    getActivity().finish();
                })
                .setPositiveButton(this.getString(R.string.dialog_save), (dialog, which) -> {
                    String username = highScoreName.getText().toString();
                    listener.applyText(username);
                    if (options.getIsExportSelected()) {
                        Toast.makeText(getContext(), getString(R.string.export_toast), Toast.LENGTH_SHORT).show();
                    }
                    getActivity().finish();
                });

        builder.setCancelable(false);

        highScoreName = view.findViewById(R.id.editTextEnterUserName);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (MessageFragmentListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public interface MessageFragmentListener {
        void applyText(String username);
    }


}
