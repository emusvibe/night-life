package com.cheda.skysevents.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cheda.skysevents.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.doorbell.android.Doorbell;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedBackFragment extends Fragment {
    private RatingBar ratingBar;
    private TextView txtRatingValue;
    private Button btnSubmit;
    EditText comments_email;
    View v;
    int appId = 6574; // Replace with your application's ID
    String apiKey = "1bVy1jxzWUuHONPoJJ4nPNhVobA8yLgRWdAQJqyv1nffzePsISrWe2LN8wFNR3wO"; // Replace with your application's API key
    private Doorbell doorbellDialog;

    public FeedBackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_feedback, container, false);
        comments_email = (EditText) v.findViewById(R.id.comments_email);
        addListenerOnRatingBar();
        addListenerOnButton();

        return v;
    }

    public void addListenerOnRatingBar() {

        ratingBar = (RatingBar)v.findViewById(R.id.ratingBar);
        txtRatingValue = (TextView)v.findViewById(R.id.txtRatingValue);

        //if rating is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                txtRatingValue.setText(String.valueOf(rating));

            }
        });
    }

    public void addListenerOnButton() {

        ratingBar = (RatingBar)v.findViewById(R.id.ratingBar);
        btnSubmit = (Button)v.findViewById(R.id.btnSubmit);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //if click on me, then display the current rating value.
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
// In your activity

                String ratingBarResult = String.valueOf(ratingBar.getRating());
                String useremail = String.valueOf(comments_email.getText());
                Doorbell doorbellDialog = new Doorbell(getActivity(), appId, apiKey); // Create the Doorbell object
                doorbellDialog.setEmail(user.getEmail()); // Prepopulate the email address field
                doorbellDialog.setName(user.getDisplayName()); // Set the name of the user (if known)
                doorbellDialog.setMessageHint("Please Enter your Comments");
                doorbellDialog.addProperty("How i Rate the App",ratingBarResult);
                doorbellDialog.addProperty("Phone Number", user.getPhoneNumber());
                doorbellDialog.addProperty("loggedIn", user.getProviders()); // Optionally add some properties
                doorbellDialog.addProperty("Email",useremail);
                doorbellDialog.addProperty("Username", user.getDisplayName());
//                doorbellDialog.addProperty("loginCount", 123);
                doorbellDialog.setEmailFieldVisibility(View.GONE); // Hide the email field, since we've filled it in already
                doorbellDialog.setPoweredByVisibility(View.GONE); // Hide the "Powered by Doorbell.io" text
                doorbellDialog.show();
//                Toast.makeText(getActivity(),String.valueOf(ratingBar.getRating()), Toast.LENGTH_SHORT).show();

            }

        });

//        // Callback for when a message is successfully sent
//        doorbellDialog.setOnFeedbackSentCallback(new io.doorbell.android.callbacks.OnFeedbackSentCallback() {
//            @Override
//            public void handle(String message) {
//                // Show the message in a different way, or use your own message!
//                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
//            }
//        });
//
//        // Callback for when the dialog is shown
//        doorbellDialog.setOnShowCallback(new io.doorbell.android.callbacks.OnShowCallback() {
//            @Override
//            public void handle() {
//                Toast.makeText(getContext(), "Dialog shown", Toast.LENGTH_LONG).show();
//            }
//        });

    }

}
