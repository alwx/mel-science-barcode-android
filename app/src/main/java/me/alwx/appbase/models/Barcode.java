package me.alwx.appbase.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author alwx (http://alwx.me)
 * @version 1.0
 */
public class Barcode extends RealmObject {
    @PrimaryKey
    @SerializedName("value")
    private long value;

    @SerializedName("created_at")
    private long createdAt;

    public Barcode() {

    }

    public Barcode(long value, long createdAt) {
        this.value = value;
        this.createdAt = createdAt;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
