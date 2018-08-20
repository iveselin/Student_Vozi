package hr.ferit.iveselin.studentvozi.results;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hr.ferit.iveselin.studentvozi.R;
import hr.ferit.iveselin.studentvozi.model.Ride;

public class RideAdapter extends RecyclerView.Adapter<RideViewHolder> {


    public interface OnItemClickListener {
        void onRideClick(View view, int position);
    }

    private List<Ride> rides = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public void setRidesList(List<Ride> ridesList) {
        rides.clear();
        rides.addAll(ridesList);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    @Override
    public RideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View carView = inflater.inflate(R.layout.item_ride, parent, false);

        return new RideViewHolder(carView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(RideViewHolder holder, int position) {
        if (rides.isEmpty() || rides.get(position) == null) {
            return;
        }
        Ride ride = rides.get(position);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ride.getTimeOfDeparture());
        String dateOfTravel = calendar.get(Calendar.DATE) + "." + calendar.get(Calendar.MONTH) + "." + calendar.get(Calendar.YEAR) + ".";

        Context holderContext = holder.itemView.getContext();
        holder.rideDeparture.setText(ride.getTravelingFrom());
        holder.rideDestination.setText(ride.getTravelingTo());
        holder.rideDate.setText(dateOfTravel);

    }

    @Override
    public int getItemCount() {
        return rides.size();
    }
}
