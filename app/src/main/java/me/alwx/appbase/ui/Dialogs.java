package me.alwx.appbase.ui;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import me.alwx.appbase.R;

/**
 * @author alwx (http://alwx.me)
 * @version 1.0
 */
public class Dialogs {

    private Dialogs() {
        throw new AssertionError();
    }

    public static void showMessage(@NonNull Context context,
                                   String message) {
        new MaterialDialog.Builder(context)
                .content(message)
                .positiveText(R.string.ok)
                .show();
    }

    public static void showConfirmation(@NonNull Context context,
                                        String message,
                                        MaterialDialog.SingleButtonCallback positiveCallback) {
        new MaterialDialog.Builder(context)
                .content(message)
                .positiveText(R.string.yes)
                .negativeText(R.string.no)
                .onPositive(positiveCallback)
                .show();
    }
}
