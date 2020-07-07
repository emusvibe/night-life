package com.cheda.skysevents.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cheda.skysevents.R;
import com.cheda.skysevents.model.SkyEvent;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {
    public static final String ARG_PARAM0 = "event_id";
    public static final String ARG_PARAM1 = "event_name";
    public static final String ARG_PARAM2 = "event_date";
    public static final String ARG_PARAM3 = "event_venue";
    public static final String ARG_PARAM4 = "event_cost";
    public static final String ARG_PARAM5 = "event_poster";
    public static final String EXTRA_NAME = "event_name";
    TextView Date,Name, Venue,admission, moredata;
    ImageView poster;

    private Button get_Ticket;
    private String meventName;
    private String meventDate;
    private String mvenueName;
    private String meventCost;
    private String meventPoster;

    private SkyEvent mEvent;

    public DetailFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(SkyEvent event) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, event);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_PARAM1)) {
            meventName = getArguments().getString(ARG_PARAM1);
            meventDate = getArguments().getString(ARG_PARAM2);
            mvenueName = getArguments().getString(ARG_PARAM3);
            meventCost = getArguments().getString(ARG_PARAM4);
            //meventPoster = getArguments().getString(ARG_PARAM5);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout)
                    activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(meventName);
            }

        }

        if (getArguments() != null ||getArguments().containsKey(ARG_PARAM1)) {
//            Toast.makeText(getContext(), "GetParcel Recieved Data", Toast.LENGTH_SHORT).show();
            mEvent = getArguments().getParcelable(ARG_PARAM1);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
// initialize variables
        Date = (TextView)v.findViewById(R.id.event_date);
        Name = (TextView)v.findViewById(R.id.event_name);
        Venue = (TextView)v.findViewById(R.id.event_venue);
        admission = (TextView)v.findViewById(R.id.event_fee);
        moredata = (TextView)v.findViewById(R.id.more_data);
        poster = (ImageView) v.findViewById(R.id.event_poster);

// set text and poster to layout with a condition on landscape or portrait
        if (mEvent != null) {
//            Toast.makeText(getContext(), "mEvent not null SetText now Happening", Toast.LENGTH_SHORT).show();
            Date.setText(mEvent.getDate());
            Venue.setText(mEvent.getVenue());
            Name.setText(mEvent.getName());
            admission.setText(mEvent.getAdmission());
            Picasso.with(getContext())
                    .load(mEvent.getPoster())
                    .placeholder(R.drawable.bckcover)
                    .into(poster);
        }
        else{

            Date.setText(meventDate);
            Venue.setText(mvenueName);
            Name.setText(meventName);
            admission.setText(meventCost);
        }

        get_Ticket = (Button)v.findViewById(R.id.get_Ticket);
        get_Ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Tickets Are only available at the Entrance for Now", Snackbar.LENGTH_LONG).show();
//                Toast.makeText(getContext(), "Tickets Are only available at the Entrance for Now", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

}
