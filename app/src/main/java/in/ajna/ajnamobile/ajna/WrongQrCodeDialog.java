package in.ajna.ajnamobile.ajna;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatDialogFragment;

public class WrongQrCodeDialog extends AppCompatDialogFragment {

    private ExitListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (ExitListener) context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_wrong_qr_code, null);

        builder.setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                        listener.exitAndStartScanner();
                    }
                })
                .setCancelable(false);

        return builder.create();
    }

//    @Override
//    public void onStart() {
//
//        super.onStart();
//        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x00ffffff));
//    }

    public interface ExitListener {
        void exitAndStartScanner();

    }
}
