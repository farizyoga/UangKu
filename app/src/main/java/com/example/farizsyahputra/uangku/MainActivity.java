package com.example.farizsyahputra.uangku;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.inputmethodservice.InputMethodService;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farizsyahputra.uangku.migration.AppMigration;
import com.example.farizsyahputra.uangku.model.MoneyTraffic;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    @BindView(android.R.id.content)
    View mContent;

    @BindView(R.id.containerInput)
    LinearLayout mContainerInput;

    @BindView(R.id.containerOutput)
    LinearLayout mContainerOutput;

    @BindView(R.id.containerDashboard)
    LinearLayout mContainerDashboard;

    @BindView(R.id.textPemasukkan)
    EditText mTextPemasukkan;

    @BindView(R.id.textPemasukkanDeskripsi)
    EditText mTextPemasukkanDeskripsi;

    @BindView(R.id.buttonSavePemasukkan)
    Button mButtonSavePemasukkan;

    @BindView(R.id.textPengeluaran)
    EditText mTextPengeluaran;

    @BindView(R.id.textPengeluaranDeskripsi)
    EditText mTextPengeluaranDeskripsi;

    @BindView(R.id.buttonSavePengeluaran)
    Button mButtonSavePengeluaran;

    @BindView(R.id.amountPemasukkan)
    TextView mAmountPemasukkan;

    @BindView(R.id.amountPengeluaran)
    TextView mAmountPengeluaran;

    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_input:
                    setContainerVisibility(mContainerInput);
                    return true;
                case R.id.navigation_dashboard:
                    setContainerVisibility(mContainerDashboard);
                    return true;
                case R.id.navigation_output:
                    setContainerVisibility(mContainerOutput);
                    return true;

            }
            return false;
        }
    };

    @OnClick(R.id.buttonSavePengeluaran)
    public void actionButtonSavePengeluaran() {
        String stringInput = mTextPengeluaran.getText().toString();
        final String stringDeskripsi = mTextPengeluaranDeskripsi.getText().toString();

        if (stringInput.matches("")) {
            return;
        }

        if (stringDeskripsi.matches("")) {
            return;
        }

        final Integer stringInputInteger;

        try {
            stringInputInteger = Integer.parseInt(stringInput);
        } catch (NumberFormatException e) {
            showToast("Input is not a number");
            return;
        }

        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Number lastInsertedID = realm.where(MoneyTraffic.class).max("ID");
                    Integer nextID;

                    if (null == lastInsertedID) {
                        nextID = 1;
                    } else {
                        nextID = lastInsertedID.intValue() + 1;
                    }

                    Long time = System.currentTimeMillis();
                    MoneyTraffic mt = new MoneyTraffic();

                    mt.setID(Long.parseLong(nextID.toString()));
                    mt.setAmount(stringInputInteger);
                    mt.setType("-");
                    mt.setDescription(stringDeskripsi);
                    mt.setCreatedAt(time);

                    realm.insert(mt);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(), "Input Success", Toast.LENGTH_SHORT).show();
                    reloadPengeluaranAmount();
                    hideSoftKeyboard();
                    mTextPengeluaran.setText("");
                    mTextPengeluaranDeskripsi.setText("");
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    @OnClick(R.id.buttonSavePemasukkan)
    public void actionButtonSavePemasukkan() {
        String stringInput = mTextPemasukkan.getText().toString();
        final String stringDeskripsi = mTextPemasukkanDeskripsi.getText().toString();

        if (stringInput.matches("")) {
            return;
        }

        if (stringDeskripsi.matches("")) {
            return;
        }

        final Integer stringInputInteger;

        try {
            stringInputInteger = Integer.parseInt(stringInput);
        } catch (NumberFormatException e) {
            showToast("Input is not a number");
            return;
        }

        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Number lastInsertedID = realm.where(MoneyTraffic.class).max("ID");
                    Integer nextID;

                    if (null == lastInsertedID) {
                        nextID = 1;
                    } else {
                        nextID = lastInsertedID.intValue() + 1;
                    }

                    Long time = System.currentTimeMillis();
                    MoneyTraffic mt = new MoneyTraffic();

                    mt.setID(Long.parseLong(nextID.toString()));
                    mt.setAmount(stringInputInteger);
                    mt.setType("+");
                    mt.setDescription(stringDeskripsi);
                    mt.setCreatedAt(time);

                    realm.insert(mt);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(), "Input Success", Toast.LENGTH_SHORT).show();
                    reloadPemasukkanAmount();
                    hideSoftKeyboard();
                    mTextPemasukkan.setText("");
                    mTextPemasukkanDeskripsi.setText("");
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    protected void reloadPengeluaranAmount() {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();

            RealmResults<MoneyTraffic> results = realm.where(MoneyTraffic.class).greaterThan("CreatedAt", getToday()).findAllAsync();
            results.addChangeListener(new RealmChangeListener<RealmResults<MoneyTraffic>>() {
                @Override
                public void onChange(RealmResults<MoneyTraffic> moneyTraffics) {
                    Integer amount = 0;

                    for (MoneyTraffic mt : moneyTraffics) {
                        if (mt.getType().equals("-")) {
                            amount = amount + mt.getAmount();
                        }
                    }

                    mAmountPengeluaran.setText(toCurrency(amount));
                }
            });
        } catch (Exception error) {
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    protected void reloadPemasukkanAmount() {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            Long test = getToday();
            RealmResults<MoneyTraffic> results = realm.where(MoneyTraffic.class).greaterThan("CreatedAt", getToday()).findAllAsync();
            results.addChangeListener(new RealmChangeListener<RealmResults<MoneyTraffic>>() {
                @Override
                public void onChange(RealmResults<MoneyTraffic> moneyTraffics) {
                    Integer amount = 0;

                    for (MoneyTraffic mt : moneyTraffics) {
                        if (mt.getType().equals("+")) {
                            amount = amount + mt.getAmount();
                        }
                    }

                    mAmountPemasukkan.setText(toCurrency(amount));
                }
            });
        } catch (Exception error) {
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    protected Long getToday() throws ParseException {
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse(today);

        return date.getTime();
    }

    protected String toCurrency(Integer amount) {
        DecimalFormat formatter = new DecimalFormat("Rp#,###,###");
        String formattedInteger = formatter.format(amount).toString();

        return formattedInteger;
    }

    protected void initRealm() {
        Realm.init(getApplicationContext());

        final RealmConfiguration configuration = new RealmConfiguration.Builder().name("uangku.db").schemaVersion(1).build();
        Realm.setDefaultConfiguration(configuration);
        Realm.getInstance(configuration);
    }

    protected void setContainerVisibility(LinearLayout container) {
        mContainerInput.setVisibility(View.GONE);
        mContainerDashboard.setVisibility(View.GONE);
        mContainerOutput.setVisibility(View.GONE);

        container.setVisibility(View.VISIBLE);
    }

    protected void hideSoftKeyboard() {
        View view = getCurrentFocus();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromInputMethod(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRealm();
        ButterKnife.bind(this);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_dashboard);
        reloadPemasukkanAmount();
        reloadPengeluaranAmount();
    }
}
