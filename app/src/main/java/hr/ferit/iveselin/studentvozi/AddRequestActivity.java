package hr.ferit.iveselin.studentvozi;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.flags.impl.DataUtils;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.zxing.common.StringUtils;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hr.ferit.iveselin.studentvozi.base.BaseActivity;
import hr.ferit.iveselin.studentvozi.base.PlaceAutocompleteAdapter;
import hr.ferit.iveselin.studentvozi.model.Ride;
import hr.ferit.iveselin.studentvozi.model.RideType;
import hr.ferit.iveselin.studentvozi.results.ResultActivity;
import hr.ferit.iveselin.studentvozi.utils.LoginUtils;

public class AddRequestActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener,
        GoogleApiClient.OnConnectionFailedListener,
        RadioGroup.OnCheckedChangeListener {

    private static final String TAG = "AddRequestActivity";
    public static final int KEY_ADDRESS_FROM_MAP_DEPARTURE = 111;
    public static final int KEY_ADDRESS_FROM_MAP_DESTINATION = 222;
    private static final String KEY_PICKED_ADDRESS = "picked_address";
    private static final LatLngBounds CRO_BOUNDS = new LatLngBounds(new LatLng(13.6569755388, 42.47999136),
            new LatLng(19.3904757016, 46.5037509222));


    public static Intent getLaunchIntent(Context fromContext) {
        return new Intent(fromContext, AddRequestActivity.class);
    }

    public static Intent getResultsIntent(String pickedAddress) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_PICKED_ADDRESS, pickedAddress);
        return resultIntent;
    }


    private int year, month, day, hour, minute;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private GeoDataClient geoDataClient;
    private RideType rideType;
    private boolean dateSet = false;
    private boolean timeSet = false;


    @BindView(R.id.number_input_value)
    NumberPicker numberPicker;
    @BindView(R.id.add_date)
    Button dateInput;
    @BindView(R.id.add_time)
    Button timeInput;
    @BindView(R.id.ride_type_group)
    RadioGroup rideTypeGroup;
    @BindView(R.id.departure_location_input)
    AutoCompleteTextView departureInput;
    @BindView(R.id.destination_location_input)
    AutoCompleteTextView destinationInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);

        setUi();
        init();
    }

    private void setUi() {
        Log.d(TAG, "setUi: setting UI");
        ButterKnife.bind(this);

        numberPicker.setMaxValue(8);
        numberPicker.setMinValue(1);
    }

    private void init() {
        Log.d(TAG, "init: initializing AutoComplete");

        rideTypeGroup.setOnCheckedChangeListener(this);
        rideTypeGroup.check(R.id.ride_type_offer);

        geoDataClient = Places.getGeoDataClient(this, null);
        placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this, geoDataClient, CRO_BOUNDS, null);

        departureInput.setAdapter(placeAutocompleteAdapter);
        destinationInput.setAdapter(placeAutocompleteAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!LoginUtils.checkLogin()) {
            Toast.makeText(getApplicationContext(), "You have to login", Toast.LENGTH_SHORT).show();
            startActivity(LoginActivity.getLaunchIntent(this));
            finish();
        }
    }


    @OnClick(R.id.add_date)
    void onAddDateClicked() {
        final Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(this, this, currentYear, currentMonth, currentDay);
        datePicker.show();
    }

    @OnClick(R.id.add_time)
    void onAddTimeClicked() {
        final Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR);
        int currentMinute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePicker = new TimePickerDialog(this, this, currentHour, currentMinute, true);
        timePicker.show();
    }

    @OnClick(R.id.map_icon_departure)
    void onMapDepartureClicked() {
        String typedAddress = departureInput.getText().toString();
        startActivityForResult(LocationActivity.getLaunchIntent(this, typedAddress), KEY_ADDRESS_FROM_MAP_DEPARTURE);
    }

    @OnClick(R.id.map_icon_destination)
    void onMapDestinationClicked() {
        String typedAddress = destinationInput.getText().toString();
        startActivityForResult(LocationActivity.getLaunchIntent(this, typedAddress), KEY_ADDRESS_FROM_MAP_DESTINATION);
    }

    @OnClick(R.id.submit_request)
    void onSubmitClicked() {
        // TODO: 6.8.2018. check values, collect data and make a new db entry
        boolean error = false;
        if (TextUtils.isEmpty(destinationInput.getText().toString())) {
            destinationInput.setError(getString(R.string.empty_input_field_error));
            error = true;
        }
        if (TextUtils.isEmpty(departureInput.getText().toString())) {
            departureInput.setError(getString(R.string.empty_input_field_error));
            error = true;
        }
        if (!dateSet) {
            dateInput.setError(getString(R.string.empty_input_field_error));
            error = true;
        }
        if (!timeSet) {
            timeInput.setError(getString(R.string.empty_input_field_error));
            error = true;
        }
        if (error) {
            return;
        }

        int numOfPassengers = numberPicker.getValue();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);
        String departureAddress = departureInput.getText().toString();
        String destinationAddress = destinationInput.getText().toString();

        Ride rideToSubmit = new Ride(numOfPassengers, calendar, rideType, departureAddress, destinationAddress);
        Log.d(TAG, "onSubmitClicked: trying to save object: " + rideToSubmit.toString());

        startActivity(ResultActivity.getLaunchIntent(this));
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        this.year = year;
        this.month = month + 1;
        this.day = day;

        dateInput.setText(day + "." + month + "." + year + ".");
        dateInput.setError(null);
        dateSet = true;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        hour = i;
        minute = i1;

        timeInput.setText(hour + ":" + minute);
        timeInput.setError(null);
        timeSet = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            String pickedAddress = data.getStringExtra(KEY_PICKED_ADDRESS);
            switch (requestCode) {
                case KEY_ADDRESS_FROM_MAP_DEPARTURE:
                    departureInput.setText(pickedAddress);
                    break;
                case KEY_ADDRESS_FROM_MAP_DESTINATION:
                    destinationInput.setText(pickedAddress);
                    break;
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: failure: " + connectionResult.getErrorMessage());
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (rideTypeGroup.getCheckedRadioButtonId()) {
            case R.id.ride_type_offer:
                rideType = RideType.OFFER;
                break;
            case R.id.ride_type_request:
                rideType = RideType.REQUEST;
                break;
        }
    }
}
