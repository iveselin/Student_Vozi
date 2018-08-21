package hr.ferit.iveselin.studentvozi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import hr.ferit.iveselin.studentvozi.model.Ride;
import hr.ferit.iveselin.studentvozi.model.RideType;
import hr.ferit.iveselin.studentvozi.utils.LoginUtils;

public class UserPostsActivity extends AppCompatActivity {

    private static final String TAG = "UserPostsActivity";

    public static Intent getLaunchIntent(Context fromContext) {
        return new Intent(fromContext, UserPostsActivity.class);
    }

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String userId;

    private Ride userOfferRide;
    private Ride userRequestRide;


    @BindView(R.id.offer_list_applicants)
    ListView userOfferApplicants;
    @BindView(R.id.offer_ride_date)
    TextView userOfferDate;
    @BindView(R.id.offer_ride_time)
    TextView userOfferTime;
    @BindView(R.id.offer_ride_departure)
    TextView userOfferDeparture;
    @BindView(R.id.offer_ride_destination)
    TextView userOfferDestination;
    @BindView(R.id.offer_error)
    TextView userOfferError;
    @BindView(R.id.user_offer_view)
    LinearLayout userOfferLayout;

    @BindView(R.id.request_list_applicants)
    ListView userRequestApplicants;
    @BindView(R.id.request_ride_date)
    TextView userRequestDate;
    @BindView(R.id.request_ride_time)
    TextView userRequestTime;
    @BindView(R.id.request_ride_departure)
    TextView userRequestDeparture;
    @BindView(R.id.request_ride_destination)
    TextView userRequestDestination;
    @BindView(R.id.user_request_error)
    TextView userRequestError;
    @BindView(R.id.user_request_view)
    LinearLayout userRequestLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_posts);

        setUi();
        init();
    }

    private void setUi() {
        ButterKnife.bind(this);

    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("rides");
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!LoginUtils.checkLogin()) {
            Toast.makeText(getApplicationContext(), "You have to login", Toast.LENGTH_SHORT).show();
            startActivity(LoginActivity.getLaunchIntent(this));
            finish();
        } else {
            fetchUserData();
        }
    }

    private void fetchUserData() {
        userId = firebaseAuth.getCurrentUser().getUid();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userOfferRide = dataSnapshot.child(RideType.OFFER.name()).child(userId).getValue(Ride.class);
                userRequestRide = dataSnapshot.child(RideType.REQUEST.name()).child(userId).getValue(Ride.class);
                showUserData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void showUserData() {
        Calendar calendar = Calendar.getInstance();
        ArrayAdapter<String> simpleAdapter;
        String date;
        String time;

        if (userOfferRide != null) {
            calendar.setTimeInMillis(userOfferRide.getTimeOfDeparture());
            date = calendar.get(Calendar.DATE) + "." + calendar.get(Calendar.MONTH) + "." + calendar.get(Calendar.YEAR) + ".";
            time = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
            userOfferDate.setText(date);
            userOfferTime.setText(time);
            userOfferDeparture.setText(userOfferRide.getTravelingFrom());
            userOfferDestination.setText(userOfferRide.getTravelingTo());
            simpleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userOfferRide.getSingedUpUsersEmail());
            userOfferApplicants.setAdapter(simpleAdapter);
        } else {
            userOfferLayout.setVisibility(View.GONE);
            userOfferError.setVisibility(View.VISIBLE);
        }

        if (userRequestRide != null) {
            calendar.setTimeInMillis(userRequestRide.getTimeOfDeparture());
            date = calendar.get(Calendar.DATE) + "." + calendar.get(Calendar.MONTH) + "." + calendar.get(Calendar.YEAR) + ".";
            time = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
            userRequestDate.setText(date);
            userRequestTime.setText(time);
            userRequestDeparture.setText(userRequestRide.getTravelingFrom());
            userRequestDestination.setText(userRequestRide.getTravelingTo());
            simpleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userRequestRide.getSingedUpUsersEmail());
            userRequestApplicants.setAdapter(simpleAdapter);
        } else {
            userRequestLayout.setVisibility(View.GONE);
            userRequestError.setVisibility(View.VISIBLE);
        }

    }
}
