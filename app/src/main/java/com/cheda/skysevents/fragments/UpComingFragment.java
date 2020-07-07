package com.cheda.skysevents.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cheda.skysevents.DetailActivity;
import com.cheda.skysevents.FavoritesActivity;
import com.cheda.skysevents.R;
import com.cheda.skysevents.adapters.SkyEventsListAdapter;
import com.cheda.skysevents.model.SkyEvent;
import com.cheda.skysevents.service.Client;
import com.cheda.skysevents.service.JSONResponse;
import com.cheda.skysevents.service.RequestInterface;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SkyListCallback} interface
 * to handle interaction events.
 */
public class UpComingFragment extends Fragment implements SearchView.OnQueryTextListener{

    private SkyListCallback listCallback;
    private RecyclerView mRecycler;
   // public SkyEventsListAdapter.FavListener mFavlistener;
    private ArrayList<SkyEvent> mArrayList;
    public SkyEventsListAdapter mAdapter;
    private SwipeRefreshLayout swipeContainer;


    public UpComingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_up_coming, container, false);

        setHasOptionsMenu(true);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //  initViews();
                loadJSON();
            }
        });

        swipeContainer.setColorSchemeResources(R.color.color3,R.color.color2,R.color.color1,R.color.color4);

        loadJSON();

        mRecycler = (RecyclerView)v.findViewById(R.id.fragment_up_coming);
        assert mRecycler != null;
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mArrayList = new ArrayList<>();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(mRecycler);


        return v;
    }

    private ItemTouchHelper.Callback createHelperCallback() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.ANIMATION_TYPE_SWIPE_SUCCESS | ItemTouchHelper.ACTION_STATE_SWIPE) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        };

        return simpleCallback;
    }
    private void moveItem(int oldPos, int newPos){

        mAdapter.notifyItemMoved(oldPos, newPos);
    }

    public void loadJSON(){

        Client Client = new Client();
        RequestInterface request = Client.getClient().create(RequestInterface.class);
        Call<JSONResponse> call = request.getJSON();
        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                mArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getEvent()));
                mAdapter = new SkyEventsListAdapter(getContext(), mArrayList, listCallback, new SkyEventsListAdapter.FavListener() {
                    @Override
                    public void onClickFavItem(SkyEvent event) {
//                        Intent intent = new Intent(getActivity(), DetailActivity.class);
//                        intent.putExtra(DetailFragment.ARG_PARAM1, event.getName());
//                        intent.putExtra(DetailFragment.ARG_PARAM2, event.getDate());
//                        intent.putExtra(DetailFragment.ARG_PARAM3, event.getVenue());
//                        intent.putExtra(DetailFragment.ARG_PARAM4, event.getAdmission());
//                        intent.putExtra(DetailFragment.ARG_PARAM5, event.getPoster());
//                        startActivity(intent);
//                        Toast.makeText(getContext(), "Twopane is false", Toast.LENGTH_SHORT).show();
                    }
                });
                mRecycler.smoothScrollToPosition(0);
                mRecycler.setAdapter(mAdapter);
                swipeContainer.setRefreshing(false);
//                updateEmptyView();
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error",t.getMessage());

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu2, menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setQueryHint("Search by event, venue, date");
        searchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(search, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                try {
                    mAdapter.setFilter(mArrayList);
                } catch (Exception e)
                {
                    Toast.makeText(getContext(), "Check you Data connection", Toast.LENGTH_SHORT).show();
                }

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        });

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        try {
            mAdapter.getFilter().filter(newText);
        } catch (Exception e)
        {
            Toast.makeText(getContext(), "Check you Data connection", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(SkyEvent event) {
//        if (listCallback != null) {
//            listCallback.onSkyEventSelected(event);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SkyListCallback) {
            listCallback = (SkyListCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SkyListCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listCallback = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface SkyListCallback {
        // TODO: Update argument type and name
        void onSkyEventSelected(SkyEvent event);
    }
}
