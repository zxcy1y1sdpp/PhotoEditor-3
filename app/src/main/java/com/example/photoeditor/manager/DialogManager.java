package com.example.photoeditor.manager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.photoeditor.MainActivity;
import com.example.photoeditor.abstracts.Constants;
import com.googlecode.mp4parser.authoring.Edit;

public class DialogManager {
    
    /* Interface */
    public interface DialogClickListener{
        void onDialogButtonClicked(DialogInterface dialog, int whichButton, String whichDialog, String input);
    }
    
    public void setListener(DialogClickListener listener){
        mListener = listener;
    }
    
    private DialogClickListener mListener;
    private static DialogManager mDialogInstance;
    
    public static DialogManager getInstance(){
        if(mDialogInstance == null)
            mDialogInstance = new DialogManager();
        
        return mDialogInstance;
    }
    
    private DialogManager(){}
    
    public void showAlertDialog(Context context, String title, String msg, String positiveButton){
    
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        
        builder.setTitle(title);
        builder.setMessage(msg);
        
        builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onDialogButtonClicked(dialog,AlertDialog.BUTTON_POSITIVE, Constants.ONE_BUTTON_DIALOG,null);
            }
        });
        
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    
    public void showInputAlertDialog(Context context, String title, String positiveButton, String negativeButton) {
    
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
    
        builder.setTitle(title);

        // Add Edit Text in dialog
        final EditText inputField = new EditText(context);
    
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        inputField.setLayoutParams(lp);

        builder.setView(inputField);
        
        builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                
                String text = inputField.getText().toString().trim();
                
                if(!text.isEmpty())
                    mListener.onDialogButtonClicked(dialog,AlertDialog.BUTTON_POSITIVE, Constants.TWO_BUTTON_DIALOG, text);
                else
                    inputField.setError("This Field is required.");
            }
        });
    
        builder.setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onDialogButtonClicked(dialog,AlertDialog.BUTTON_NEGATIVE, Constants.TWO_BUTTON_DIALOG, "");
            }
        });
    
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
