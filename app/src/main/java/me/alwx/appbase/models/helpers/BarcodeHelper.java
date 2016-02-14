package me.alwx.appbase.models.helpers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import me.alwx.appbase.models.Barcode;

/**
 * @author alwx (http://alwx.me)
 * @version 1.0
 */
public class BarcodeHelper {
    public static void insertBarcode(@NonNull Realm realm, Barcode barcode) {
        realm.beginTransaction();
        realm.copyToRealm(barcode);
        realm.commitTransaction();
    }

    @NonNull
    public static List<Barcode> getBarcodes(@NonNull Realm realm) {
        return realm.allObjectsSorted(Barcode.class, "createdAt", false);
    }

    @Nullable
    public static Barcode getBarcodeByValue(@NonNull Realm realm, long value) {
        return realm.where(Barcode.class).equalTo("value", value).findFirst();
    }

    public static void removeBarcodeByValue(@NonNull Realm realm, long value) {
        realm.beginTransaction();
        Barcode barcode = getBarcodeByValue(realm, value);
        if (barcode != null) {
            barcode.removeFromRealm();
        }
        realm.commitTransaction();
    }

    public static void clearBarcodes(@NonNull Realm realm) {
        realm.beginTransaction();
        realm.clear(Barcode.class);
        realm.commitTransaction();
    }
}
