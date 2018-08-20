package hr.ferit.iveselin.studentvozi.results;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import hr.ferit.iveselin.studentvozi.R;

public class RideViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private RideAdapter.OnItemClickListener listener;

    TextView rideDestination;
    TextView rideDeparture;

    public RideViewHolder(View itemView, RideAdapter.OnItemClickListener listener) {
        super(itemView);


        this.rideDestination = itemView.findViewById(R.id.ride_destination);
        this.rideDeparture = itemView.findViewById(R.id.ride_departure);
        this.listener = listener;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onRideClick(view, getAdapterPosition());
        }
    }
}
