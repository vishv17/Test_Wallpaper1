package com.hd.wallpaper.background.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.hd.wallpaper.background.R;


public class WallpaperDialog extends DialogFragment
{
    public interface WallpaperDialogListener {
        void onCancel();
    }

    private WallpaperDialogListener listener;
    private boolean downloadFinished = false;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_wallpaper, null, false);
//        ButterKnife.bind(this, view);

        /*return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.setting_wallpaper)
//                .setNegativeButton(R.string.cancel, null)
//                .setView(view)
                .setView(R.layout.dialog_wallpaper)
                .setCancelable(false)
                .create();*/

        AlertDialog dialog = new AlertDialog.Builder(getActivity()).setView(R.layout.dialog_wallpaper).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle(R.string.setting_wallpaper);
        return dialog;
        /*Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(R.string.setting_wallpaper);
        dialog.setContentView(R.layout.dialog_wallpaper);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;*/
    }

    /*@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.dialog_wallpaper,container,false);
        return view;
//        return super.onCreateView(inflater, container, savedInstanceState);
    }*/

    public void setListener(WallpaperDialogListener listener) {
        this.listener = listener;
    }

    public void setDownloadFinished(boolean downloadFinished) {
        this.downloadFinished = downloadFinished;
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        if (!downloadFinished && listener != null) {
            listener.onCancel();
        }
        downloadFinished = false;
    }
}
