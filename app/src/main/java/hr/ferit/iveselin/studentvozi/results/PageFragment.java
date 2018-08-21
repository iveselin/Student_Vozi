package hr.ferit.iveselin.studentvozi.results;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hr.ferit.iveselin.studentvozi.R;
import hr.ferit.iveselin.studentvozi.model.Ride;
import hr.ferit.iveselin.studentvozi.model.RideType;

public class PageFragment extends Fragment implements RideAdapter.OnItemClickListener {

    private static final String TAG = "PageFragment";

    private static final String KEY_CAR_TYPE = "carTYPE";

    private List<Ride> rideList = new ArrayList<>();
    private RideType rideType;
    private RideAdapter rideAdapter;
    private Ride clickedRide;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public static PageFragment newInstance(RideType rideType) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_CAR_TYPE, rideType);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rideType = (RideType) getArguments().getSerializable(KEY_CAR_TYPE);

        setDb();
    }

    private void setDb() {
        Log.d(TAG, "setDb: setting the DB and fetching data");
        firebaseDatabase = FirebaseDatabase.getInstance();
        if (rideType == RideType.REQUEST) {
            databaseReference = firebaseDatabase.getReference().child("rides").child(RideType.REQUEST.name());
        } else if (rideType == RideType.OFFER) {
            databaseReference = firebaseDatabase.getReference().child("rides").child(RideType.OFFER.name());
        }

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rideList.clear();
                for (DataSnapshot childrenSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: fetching: " + childrenSnapshot.getValue(Ride.class));
                    rideList.add(childrenSnapshot.getValue(Ride.class));
                    setRides();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(valueEventListener);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUI(view);
    }

    private void setUI(View view) {
        rideAdapter = new RideAdapter();
        rideAdapter.setOnItemClickListener(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);

        RecyclerView ridesRecyclerView = view.findViewById(R.id.ride_list);
        ridesRecyclerView.addItemDecoration(itemDecoration);
        ridesRecyclerView.setLayoutManager(layoutManager);
        ridesRecyclerView.setAdapter(rideAdapter);

        setRides();
    }

    private void setRides() {
        Log.d(TAG, "setRides: trying to set rides");
        rideAdapter.setRidesList(rideList);
    }


    @Override
    public void onRideClick(View view, int position) {
        clickedRide = rideList.get(position);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.ride_dialog, null);

        TextView helperView = dialogView.findViewById(R.id.traveling_from);
        helperView.setText(clickedRide.getTravelingFrom());

        helperView = dialogView.findViewById(R.id.traveling_to);
        helperView.setText(clickedRide.getTravelingTo());

        helperView = dialogView.findViewById(R.id.time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(clickedRide.getTimeOfDeparture());
        helperView.setText(calendar.get(Calendar.DATE) + "." + calendar.get(Calendar.MONTH) + "." + calendar.get(Calendar.YEAR) + "." +
                "\t" + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE));

        helperView = dialogView.findViewById(R.id.num_places);
        helperView.setText("Mjesta: " + clickedRide.getNumOfPassengers());

        alertBuilder.setView(dialogView);
        alertBuilder.setMessage(clickedRide.getRideType().getDisplayName());

        alertBuilder.setPositiveButton(R.string.ride_dialog_positive_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                updateRide();
            }
        });

        alertBuilder.setNegativeButton(R.string.dialog_negative_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();

    }

    private void updateRide() {
        if (!clickedRide.getOwnerId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            clickedRide.addSignedUpUsersId(FirebaseAuth.getInstance().getCurrentUser().getUid());
            Log.d(TAG, "updateRide: updating ride " + clickedRide.getOwnerId());
            databaseReference.child(clickedRide.getOwnerId()).setValue(clickedRide);
        } else {
            Toast.makeText(getActivity(), "Ovo je vaša " + clickedRide.getRideType().getDisplayName(), Toast.LENGTH_SHORT).show();
        }
    }
}
