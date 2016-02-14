package me.alwx.appbase.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.okhttp.Response;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import me.alwx.appbase.DateFormatter;
import me.alwx.appbase.R;
import me.alwx.appbase.api.API;
import me.alwx.appbase.models.Barcode;
import me.alwx.appbase.models.helpers.BarcodeHelper;
import me.alwx.appbase.ui.Dialogs;
import me.alwx.appbase.ui.activity.MainActivity;
import me.alwx.appbase.ui.activity.ScannerActivity;
import me.alwx.appbase.ui.adapter.SimpleRecyclerAdapter;
import me.alwx.appbase.ui.decoration.DefaultListSpacingItemDecoration;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

/**
 * @author alwx (http://alwx.me)
 * @version 1.0
 */
public class MainFragment extends Fragment {
    private static final int REQUEST_CODE_SCAN = 1001;

    @Bind(R.id.list)
    RecyclerView mList;

    @Bind(R.id.fab)
    FloatingActionButton mFloatingActionButton;

    @Bind(R.id.placeholder)
    TextView mPlaceholder;

    private SimpleRecyclerAdapter<Barcode> mAdapter;
    private Realm mRealm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        View contentView = inflater.inflate(R.layout.fragment_main, null);
        ButterKnife.bind(this, contentView);

        mRealm = Realm.getInstance(getContext());

        initFloatingActionButton();
        initList();
        updateListData();

        return contentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SCAN && resultCode == Activity.RESULT_OK) {
            String result = data.getStringExtra(ScannerActivity.INTENT_RESULT);
            try {
                long barcodeValue = Long.valueOf(result);

                if (BarcodeHelper.getBarcodeByValue(mRealm, barcodeValue) == null) {
                    Barcode barcode = new Barcode();
                    barcode.setValue(Long.valueOf(result));
                    barcode.setCreatedAt(System.currentTimeMillis());
                    BarcodeHelper.insertBarcode(mRealm, barcode);
                    updateListData();
                } else {
                    Dialogs.showMessage(
                            getContext(), getString(R.string.main_barcode_exists, barcodeValue)
                    );
                }
            } catch (NumberFormatException e) {
                Dialogs.showMessage(
                        getContext(), getString(R.string.main_barcode_incorrect)
                );
            }
        }
    }

    private void initFloatingActionButton() {
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ScannerActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
            }
        });
    }

    private void initList() {
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(getContext()));
        mList.addItemDecoration(
                new DefaultListSpacingItemDecoration(
                        (int) getResources().getDimension(R.dimen.list_spacing)
                )
        );
    }

    private void updateListData() {
        List<Barcode> barcodes = BarcodeHelper.getBarcodes(Realm.getInstance(getContext()));
        updateTitle(barcodes.size());
        if (barcodes.size() == 0) {
            mPlaceholder.setVisibility(View.VISIBLE);
            mList.setVisibility(View.GONE);
            clearMenu();
        } else {
            mPlaceholder.setVisibility(View.GONE);
            mList.setVisibility(View.VISIBLE);
            showMenu();

            mAdapter = new SimpleRecyclerAdapter<>(
                    barcodes,
                    R.layout.item_two_line,
                    new SimpleRecyclerAdapter.HolderInjector<Barcode>() {
                        @Override
                        public void inject(Barcode barcode, SimpleRecyclerAdapter.Holder holder) {
                            holder.mText.setText(String.valueOf(barcode.getValue()));
                            holder.mDescription.setText(
                                    DateFormatter.formatDate(
                                            DateFormatter.Format.FULL,
                                            barcode.getCreatedAt()
                                    )
                            );
                        }
                    }
            );
            mAdapter.setOnClickListener(new SimpleRecyclerAdapter.OnClickListener<Barcode>() {
                @Override
                public void onClick(Barcode object) {
                    // don't do anything
                }

                @Override
                public void onLongClick(final Barcode object) {
                    Dialogs.showConfirmation(
                            getContext(),
                            getString(R.string.main_delete_confirmation, object.getValue()),
                            new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog,
                                                    @NonNull DialogAction action) {
                                    BarcodeHelper.removeBarcodeByValue(mRealm, object.getValue());
                                    updateListData();
                                }
                            }
                    );
                }
            });
            mList.setAdapter(mAdapter);
        }
    }

    private void updateTitle(int count) {
        if (getActivity() instanceof MainActivity) {
            String title = getString(R.string.main_title, count);
            ((MainActivity) getActivity()).getToolbar().setTitle(title);
        }
    }

    private void clearMenu() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).getToolbar().getMenu().clear();
        }
    }

    private void showMenu() {
        if (getActivity() instanceof MainActivity) {
            Toolbar toolbar = ((MainActivity) getActivity()).getToolbar();

            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.main);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.upload:
                            Dialogs.showConfirmation(
                                    getContext(),
                                    getString(R.string.main_upload_confirmation),
                                    new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog,
                                                            @NonNull DialogAction action) {
                                            uploadBarcodes();
                                        }
                                    });
                            return true;
                    }
                    return false;
                }
            });
        }
    }

    private void uploadBarcodes() {
        Call<Response> response = API.getBarcodeService().postBarcode(mAdapter.getItems());
        response.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(retrofit.Response<Response> response, Retrofit retrofit) {
                Dialogs.showMessage(getContext(), getString(R.string.main_upload_success));
                BarcodeHelper.clearBarcodes(mRealm);
                updateListData();
            }

            @Override
            public void onFailure(Throwable t) {
                Dialogs.showMessage(getContext(), getString(R.string.main_upload_failure));
            }
        });
    }
}
