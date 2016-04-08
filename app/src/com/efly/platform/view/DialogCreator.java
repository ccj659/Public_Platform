package com.efly.platform.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.efly.platform.R;


public class DialogCreator {
    /*public static Dialog createLoadingDialog(Context context, String msg) {
        Log.e("create-->","createLoadingDialog"+"");

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_view, null);
        RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.dialog_view);
        ImageView mLoadImg = (ImageView) v.findViewById(R.id.loading_img);
        TextView mLoadText = (TextView) v.findViewById(R.id.loading_txt);
        AnimationDrawable mDrawable = (AnimationDrawable) mLoadImg.getDrawable();
        mDrawable.start();
        mLoadText.setText(msg);
        final Dialog loadingDialog = new Dialog(context, R.style.LoadingDialog);
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        return loadingDialog;
    }*/

    public static Dialog createLoadingMin(Context context, String msg) {
        final Dialog loadingDialog = new Dialog(context, R.style.default_dialog_style);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loding_min, null);
        loadingDialog.setContentView(v);
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);
        return loadingDialog;
    }

    public static Dialog createSelectedDialog(Context context, String titleMsg,View.OnClickListener listener){
        Dialog dialog = new Dialog(context, R.style.default_dialog_style);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_base_with_button, null);
        dialog.setContentView(v);
        TextView title = (TextView) v.findViewById(R.id.title);
        title.setText(titleMsg);
        final Button cancel = (Button) v.findViewById(R.id.cancel_btn);
        final Button commit = (Button) v.findViewById(R.id.commit_btn);
        commit.setText("确定");
        cancel.setOnClickListener(listener);
        commit.setOnClickListener(listener);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }


    public static Dialog createListDialog(Context context, String titleMsg,View.OnClickListener listener){
        Dialog dialog =new Dialog(context, R.style.default_dialog_style);
       // View view =View.inflate(context,R.layout.)
        return dialog;
    }

}
