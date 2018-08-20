package hr.ferit.iveselin.studentvozi.results;

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

        RecyclerView ridesRecylcerView = view.findViewById(R.id.ride_list);
        ridesRecylcerView.addItemDecoration(itemDecoration);
        ridesRecylcerView.setLayoutManager(layoutManager);
        ridesRecylcerView.setAdapter(rideAdapter);

        setRides();
    }

    private void setRides() {
        Log.d(TAG, "setRides: trying to set rides");
        rideAdapter.setRidesList(rideList);
    }


    @Override
    public void onRideClick(View view, int position) {

    }
}
