package hr.ferit.iveselin.studentvozi.results;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hr.ferit.iveselin.studentvozi.R;
import hr.ferit.iveselin.studentvozi.model.Ride;
import hr.ferit.iveselin.studentvozi.model.RideType;

public class PageFragment extends Fragment implements RideAdapter.OnItemClickListener {

    private static final String KEY_CAR_TYPE = "carTYPE";

    private List<Ride> rideList = new ArrayList<>();
    private RideType rideType;
    private RideAdapter rideAdapter;

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
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rideAdapter.setOnItemClickListener(this);

        RecyclerView carListRV = view.findViewById(R.id.ride_list);
        carListRV.addItemDecoration(itemDecoration);
        carListRV.setLayoutManager(layoutManager);
        carListRV.setAdapter(rideAdapter);
        setRides();
    }

    private void setRides() {
        rideList.add(new Ride(1, Calendar.getInstance(), RideType.REQUEST, "Osijek", "Pozega"));
        if (rideType.equals(RideType.OFFER)) {
            List<Ride> favourites = new ArrayList<>();
            favourites.add(new Ride(2, Calendar.getInstance(), RideType.OFFER, "Pozega", "Osijek"));
            rideList = favourites;
        }
        rideAdapter.setRidesList(rideList);
    }


    @Override
    public void onRideClick(View view, int position) {
        Toast.makeText(getActivity().getApplicationContext(), rideList.get(position).getRideType().getDisplayName(), Toast.LENGTH_SHORT).show();
    }
}
