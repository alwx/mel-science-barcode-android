package me.alwx.appbase.api;

import com.squareup.okhttp.Response;

import java.util.List;

import me.alwx.appbase.models.Barcode;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * @author alwx (http://alwx.me)
 * @version 1.0
 */
public interface BarcodeService {
    @POST("/barcode")
    Call<Response> postBarcode(@Body List<Barcode> barcodeList);
}
