package com.cheda.skysevents.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cheda.skysevents.R;
import com.cheda.skysevents.fragments.UpComingFragment;
import com.cheda.skysevents.model.SkyEvent;
import com.cheda.skysevents.service.SharedPreference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SkyEventsListAdapterbckup extends RecyclerView.Adapter<SkyEventsListAdapterbckup.ViewHolder> implements Filterable {

    private boolean mTwoPane;
    private LayoutInflater mLayoutInflater;
    private Context context;
    private ArrayList<SkyEvent> mArrayList;
    private ArrayList<SkyEvent> mFilteredList;
    SharedPreference sharedPreference;
    private UpComingFragment.SkyListCallback listCallback;
    private FavListener mFavlistener;
    public interface FavListener{
        void onClickFavItem(SkyEvent event);
    }

    public SkyEventsListAdapterbckup(Context context, ArrayList<SkyEvent> arrayList, UpComingFragment.SkyListCallback listCallback, FavListener mFavlistener) {
        this.context = context;
        this.mArrayList = arrayList;
        this.mFilteredList = arrayList;
        sharedPreference = new SharedPreference();
        this.mLayoutInflater = LayoutInflater.from(context);
        this.listCallback = listCallback;
        this.mFavlistener = mFavlistener;
    }

    @Override
    public SkyEventsListAdapterbckup.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_list_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SkyEventsListAdapterbckup.ViewHolder viewHolder, int i) {
        final SkyEvent events = mFilteredList.get(i);
        viewHolder.events = mFilteredList.get(i);
        viewHolder.tv_name.setText(mFilteredList.get(i).getName());
        viewHolder.tv_venue.setText(mFilteredList.get(i).getVenue());
        viewHolder.tv_date.setText(mFilteredList.get(i).getDate());
        viewHolder.tv_admission.setText(mFilteredList.get(i).getAdmission());

        //load album cover using picasso
        Picasso.with(context)
                .load(mFilteredList.get(i).getPoster())
                .placeholder(R.drawable.sky_logo)
                .into(viewHolder.poster);


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mFavlistener.onClickFavItem(events);
                listCallback.onSkyEventSelected(events);

//                if (mTwoPane) {
//                    SkyEvent clickedDataItem = null;
//
//                    int pos = viewHolder.getAdapterPosition();
//                    clickedDataItem = mFilteredList.get(pos);
//                    Bundle arguments = new Bundle();
////                    arguments.putString(DetailFragment.ARG_PARAM1, viewHolder.events.getName());
////                    arguments.putString(DetailFragment.ARG_PARAM1, mFilteredList.get(pos).getName());
//                    arguments.putString(DetailFragment.ARG_PARAM1, viewHolder.events.getName());
//                    arguments.putString(DetailFragment.ARG_PARAM2, viewHolder.events.getDate());
//                    arguments.putString(DetailFragment.ARG_PARAM3, viewHolder.events.getVenue());
//                    arguments.putString(DetailFragment.ARG_PARAM4, viewHolder.events.getAdmission());
//                    arguments.putString(DetailFragment.EXTRA_NAME, viewHolder.tv_name.getText().toString());
//////                        arguments.putString("name", mFilteredList.get(pos).getName());
//////                        arguments.putString("date", mFilteredList.get(pos).getDate());
//////                        arguments.putString("venue", mFilteredList.get(pos).getVenue());
//////                        arguments.putString("admission", mFilteredList.get(pos).getAdmission());
//////                        arguments.putString("poster", mFilteredList.get(pos).getPoster());
//
//                    DetailFragment fragment = new DetailFragment();
//                    FragmentTransaction ft = ((MainActivity)context).getSupportFragmentManager().beginTransaction();
//                    ft.replace(R.id.event_detail_container, fragment);
//                    ft.commit();

//                    DetailFragment fragment = new DetailFragment();
//                    fragment.setArguments(arguments);
//
//                            .replace(R.id.event_detail_container, fragment)
//                            .commit();
//                }
//                else {

//                    int pos = viewHolder.getAdapterPosition();
//                    clickedDataItem = mFilteredList.get(pos);
//                        Intent intent = new Intent(context.getApplicationContext(),DetailActivity.class);
//                        intent.putExtra(DetailActivity.EXTRA_NAME, viewHolder.tv_name.getText());
//                        intent.putExtra("name", mFilteredList.get(pos).getName());
//                        intent.putExtra("date", mFilteredList.get(pos).getDate());
//                        intent.putExtra("venue", mFilteredList.get(pos).getVenue());
//                        intent.putExtra("admission", mFilteredList.get(pos).getAdmission());
//                        intent.putExtra("poster", mFilteredList.get(pos).getPoster());
//                        intent.putExtra(DetailFragment.ARG_PARAM1, viewHolder.events.name);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        Toast.makeText(v.getContext(), "you clicked "+ clickedDataItem.getName(),Toast.LENGTH_SHORT).show();
//                        context.startActivity(intent);

//                    Context context = v.getContext();
//                    Intent intent = new Intent(context, DetailActivity.class);
//                    intent.putExtra(DetailFragment.ARG_PARAM1, viewHolder.events.getName());
//                    intent.putExtra(DetailFragment.ARG_PARAM2, viewHolder.events.getDate());
//                    intent.putExtra(DetailFragment.ARG_PARAM3, viewHolder.events.getVenue());
//                    intent.putExtra(DetailFragment.ARG_PARAM4, viewHolder.events.getAdmission());
//                    intent.putExtra(DetailFragment.ARG_PARAM5, viewHolder.events.getPoster());
//                    context.startActivity(intent);
////
////
//                }


            }

        });

        viewHolder.poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listCallback.onSkyEventSelected(events);
                mFavlistener.onClickFavItem(events);

            }
        });

        // set favorites code /*If a event exists in shared preferences then set heart_red drawable * and set a tag*/
        if (checkFavoriteItem(events)) {
            viewHolder.favoriteImg.setImageResource(R.drawable.heart_red);
            viewHolder.favoriteImg.setTag("red");
            sharedPreference.saveFavorites(context, mFilteredList);
            notifyDataSetChanged();
            viewHolder.mListItem.setBackgroundColor(
                    context.getResources().getColor(R.color.colorGrey));
        } else {
            viewHolder.favoriteImg.setImageResource(R.drawable.heart_grey);
            viewHolder.favoriteImg.setTag("grey");
            viewHolder.mListItem.setBackgroundColor(
                    context.getResources().getColor(R.color.colorWhite));
        }

    }

    /*Checks whether a particular event exists in SharedPreferences*/
    public boolean checkFavoriteItem(SkyEvent checkEvent) {
        boolean check = false;
//        ArrayList<SkyEvent> favorites = sharedPreference.getFavorites(context);
        List<SkyEvent> favorites = sharedPreference.getFavorites(context);
        if (favorites != null) {
            for (SkyEvent events : favorites) {
                if (events.equals(checkEvent)) {
                    check = true;

//                    notifyDataSetChanged();
                    break;
                }
            }
        }
        return check;
    }


    @Override
    public int getItemCount() {
//        if (null == mFilteredList) return 0;
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = mArrayList;
                } else {

                    ArrayList<SkyEvent> filteredList = new ArrayList<>();

                    for (SkyEvent events : mArrayList) {

                        if (events.getVenue().toLowerCase().contains(charString) || events.getName().toLowerCase().contains(charString) || events.getDate().toLowerCase().contains(charString)) {

                            filteredList.add(events);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<SkyEvent>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void setFilter(ArrayList<SkyEvent> mFilteredList) {

        mArrayList = new ArrayList<>();
        mArrayList.addAll(mFilteredList);
        notifyDataSetChanged();

    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tv_name,tv_venue,tv_date,tv_admission;
        private ImageView poster, favoriteImg, shareImageView;
        private CardView mCardView;
        private RelativeLayout mListItem;
        public SkyEvent events;


        public ViewHolder(final View view) {
            super(view);

//            tv_name = (TextView)view.findViewById(R.id.id);
//            tv_venue = (TextView)view.findViewById(R.id.content);
            tv_name = (TextView)view.findViewById(R.id.tv_name);
            tv_venue = (TextView)view.findViewById(R.id.tv_venue);
            tv_date = (TextView)view.findViewById(R.id.tv_date);
            tv_admission = (TextView)view.findViewById(R.id.tv_admission);
            poster = (ImageView) view.findViewById(R.id.poster);
            mCardView = (CardView) view.findViewById(R.id.card_view);
            mListItem = (RelativeLayout) view.findViewById(R.id.list_item_layout);
            favoriteImg = (ImageView)view.findViewById(R.id.fav);
            shareImageView = (ImageView)view.findViewById(R.id.send);

//            view.setOnClickListener(this);
            // added - symantic change in clicking starting at API 23
//            mListItem.setOnClickListener(this);

            /*
             * NOTE: have to set the onClickListener to the CardView - to
             *    accurately change selection color for group
             */
//            mCardView.setOnClickListener(this);

            favoriteImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int posit = getAdapterPosition();
                    String pos = favoriteImg.getTag().toString();
                    if (pos.equalsIgnoreCase("grey") && posit != RecyclerView.NO_POSITION){

                        sharedPreference.addFavorite(context,mFilteredList.get(getAdapterPosition()));
                        Toast.makeText(context,context.getString(R.string.add_favr), Toast.LENGTH_SHORT).show();
                        favoriteImg.setTag("red");
                        favoriteImg.setImageResource(R.drawable.heart_red);
                      //  sharedPreference.saveFavorites(context,mFilteredList);
//                        notifyItemInserted(posit);
                    }
                    else {
                        sharedPreference.removeFavorite(context,mFilteredList.get(getAdapterPosition()));
                        favoriteImg.setTag("grey");
                        favoriteImg.setImageResource(R.drawable.heart_grey);
                        Toast.makeText(context,context.getString(R.string.remove_favr), Toast.LENGTH_SHORT).show();
                        sharedPreference.saveFavorites(context,mFilteredList);
//   notifyItemRemoved(posit);
                    }

                }
            });

//            favoriteImg.setOnLongClickListener(new View.OnLongClickListener() {
//
//                @Override
//                public boolean onLongClick(View v) {
//                    int posit = getAdapterPosition();
//                    String pos = favoriteImg.getTag().toString();
//                    if (pos.equalsIgnoreCase("grey") && posit != RecyclerView.NO_POSITION){
//
//                     sharedPreference.addFavorite(context,mFilteredList.get(getAdapterPosition()));
//                        favoriteImg.setTag("red");
//                        favoriteImg.setImageResource(R.drawable.heart_red);
//
//                        Toast.makeText(context,context.getString(R.string.add_favr), Toast.LENGTH_SHORT).show();
////                        notifyItemInserted(posit);
//                    }
//                    else {
//                     sharedPreference.removeFavorite(context,mFilteredList.get(getAdapterPosition()));
//                        favoriteImg.setTag("grey");
//                        favoriteImg.setImageResource(R.drawable.heart_grey);
//                        Toast.makeText(context,context.getString(R.string.remove_favr), Toast.LENGTH_SHORT).show();
//                        sharedPreference.saveFavorites(context,mFilteredList);
////   notifyItemRemoved(posit);
//                    }
//                    return true;
//                }
//            });

//            shareImageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getResources().getResourcePackageName(poster.getId())
//                            + '/' + "drawable" + '/' + context.getResources().getResourceEntryName((int)poster.getTag()));
//                    Intent shareIntent = new Intent();
//                    shareIntent.setAction(Intent.ACTION_SEND);
//                    shareIntent.putExtra(Intent.EXTRA_STREAM,imageUri);
//                    shareIntent.setType("image/jpeg");
//                    context.startActivity(Intent.createChooser(shareIntent, context.getResources().getText(R.string.send_to)));
//
//
//
////                    Intent shareIntent = new Intent();
////                    shareIntent.setAction(Intent.ACTION_SEND);
////                    shareIntent.putExtra(Intent.EXTRA_TEXT,"image text");
////                    shareIntent.putExtra(Intent.EXTRA_STREAM,Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getResources().getResourcePackageName(itemView.getId())
////                            + '/' + "drawable" + '/' + context.getResources().getResourceEntryName((byte)itemView.getTag())));
////                  //  shareIntent.putExtra(Intent.EXTRA_STREAM,imageUri);
////                    shareIntent.setType("image/jpeg");
//////                    Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getResources().getResourcePackageName(itemView.getId())
//////                            + '/' + "drawable" + '/' + context.getResources().getResourceEntryName((byte)itemView.getTag()));
////                    context.startActivity(Intent.createChooser(shareIntent,context.getResources().getText(R.string.send_to)));
//                }
//            });

//            poster.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int pos =  getAdapterPosition();
////                    ImageView poster = (ImageView) v.findViewById(R.id.poster);
//                    if (pos != RecyclerView.NO_POSITION){
//                        Events clickedDataItem = mFilteredList.get(pos);
//                        Intent intent = new Intent(context.getApplicationContext(),SkysEventDetailActivity.class);
////                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context.getApplicationContext(),v.findViewById(R.id.poster),"logo_shared");
//                        intent.putExtra(SkysEventDetailActivity.EXTRA_NAME, tv_name.getText());
//                        intent.putExtra("name", mFilteredList.get(pos).getName());
//                        intent.putExtra("date", mFilteredList.get(pos).getDate());
//                        intent.putExtra("venue", mFilteredList.get(pos).getVenue());
//                        intent.putExtra("admission", mFilteredList.get(pos).getAdmission());
//                        intent.putExtra("poster", mFilteredList.get(pos).getPoster());
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////
//                        context.startActivity(intent);
//                        Toast.makeText(v.getContext(), "you clicked "+ clickedDataItem.getName(),Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });



//            mCardView.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    int pos = getAdapterPosition();
//                    if (mTwoPane) {
//                        Bundle arguments = new Bundle();
////                        arguments.putString(SkysEventDetailFragment.ARG_ITEM_ID,mFilteredList.get());
//                        arguments.putString(SkysEventDetailFragment.EXTRA_NAME, String.valueOf(tv_name.getText()));
////                        arguments.putString("name", mFilteredList.get(pos).getName());
////                        arguments.putString("date", mFilteredList.get(pos).getDate());
////                        arguments.putString("venue", mFilteredList.get(pos).getVenue());
////                        arguments.putString("admission", mFilteredList.get(pos).getAdmission());
////                        arguments.putString("poster", mFilteredList.get(pos).getPoster());
//                        SkysEventDetailFragment fragment = new SkysEventDetailFragment();
//                        fragment.setArguments(arguments);
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.skysevent_detail_container, fragment)
//                                .commit();
//                    } else {
//
////                        Events clickedDataItem = mFilteredList.get(pos);
////                        Intent intent = new Intent(context.getApplicationContext(),SkysEventDetailActivity.class);
////                        intent.putExtra(SkysEventDetailActivity.EXTRA_NAME, tv_name.getText());
////                        intent.putExtra("name", mFilteredList.get(pos).getName());
////                        intent.putExtra("date", mFilteredList.get(pos).getDate());
////                        intent.putExtra("venue", mFilteredList.get(pos).getVenue());
////                        intent.putExtra("admission", mFilteredList.get(pos).getAdmission());
////                        intent.putExtra("poster", mFilteredList.get(pos).getPoster());
////                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                        Toast.makeText(v.getContext(), "you clicked "+ clickedDataItem.getName(),Toast.LENGTH_SHORT).show();
////                        context.startActivity(intent);
//
//
////                        Context context = v.getContext();
////                        Intent intent = new Intent(context, SkysEventDetailActivity.class);
////                        intent.putExtra(SkysEventDetailFragment.ARG_ITEM_ID, events.getName());
//
//
//                    }
//
//
//                }
//            });
//







        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
//            mCursor.moveToPosition(adapterPosition);

        }

//        public void checkRippleAnimation(View view){
//            Intent intent1 = new Intent(context.getApplicationContext(),RippleActivity.class);
//            context.startActivity(intent1);
//        }
//
//        public void sharedElementsTransition(View view){
//
//        }

    }



}