package com.massimo.location;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.massimo.lab2.R;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    LayoutInflater inflater;
    List<Location> locations;

    Adapter(Context context, List<Location> locations) {
        this.inflater = LayoutInflater.from(context);
        this.locations = locations;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView locationCardAddress, locationCardLatitude, locationCardLongitude;
        ImageButton locationCardDeleteButton;
        ConstraintLayout locationCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            locationCard = itemView.findViewById(R.id.locationCard);
            locationCardAddress = itemView.findViewById(R.id.locationCardAddress);
            locationCardLatitude = itemView.findViewById(R.id.locationCardLatitude);
            locationCardLongitude = itemView.findViewById(R.id.locationCardLongitude);
            locationCardDeleteButton = itemView.findViewById(R.id.locationDeleteButton);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.location_list_view, viewGroup, false);
        return new ViewHolder(view);
    }

    public void navigateToNote(View view) {
        Log.d("test", view.toString());

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Location location = locations.get(i);

        viewHolder.locationCardAddress.setText(location.address);
        viewHolder.locationCardLatitude.setText(String.valueOf(location.latitude));
        viewHolder.locationCardLongitude.setText(String.valueOf(location.longitude));
        viewHolder.locationCard.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, EditLocation.class);
            intent.putExtra("locationId", location.ID);
            context.startActivity(intent);
        });
        viewHolder.locationCardDeleteButton.setOnClickListener(v -> {
            Log.d("Delete", "dete " + location.ID);
            LocationDatabase db = new LocationDatabase(v.getContext());
            db.deleteLocation(locations.get(i));
            this.setData(db.getLocationList());
        });
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public void setData(List<Location> newData) {
        locations.clear();
        locations.addAll(newData);
        notifyDataSetChanged();
    }
}
