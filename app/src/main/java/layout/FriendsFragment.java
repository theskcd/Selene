package layout;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.ghostwriternr.selene.CardItem;
import io.github.ghostwriternr.selene.CustomCardAdapter;
import io.github.ghostwriternr.selene.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FriendsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendsFragment extends Fragment {

    ArrayAdapter<String> mVideoListAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FriendsFragment() {
        // Required empty public constructor
    }

    String objectforreturn;
    JSONArray jsondata;
    View rootView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendsFragment newInstance(String param1, String param2) {
        FriendsFragment fragment = new FriendsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setupCardAnimations();
    }

    private void setupCardAnimations() {
        Slide slide = new Slide();
        slide.setDuration(400);
        setEnterTransition(slide);
//        Fade fade = new Fade();
//        fade.setDuration(400);
//        setExitTransition(fade);
    }

    public String getDataFromSever()
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        SharedPreferences sharedPref = getContext().getSharedPreferences("io.github.ghostwriternr", Context.MODE_PRIVATE);
        String fid = sharedPref.getString(getString(R.string.facebook), null);
        String jsonurl = "http://10.117.11.116:8080/api/v1/getSelfSongs?";
        jsonurl += fid;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, jsonurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("HotFragment", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
        return objectforreturn;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_friends, container, false);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        SharedPreferences sharedPref = getContext().getSharedPreferences("io.github.ghostwriternr", Context.MODE_PRIVATE);
        String fid = sharedPref.getString(getString(R.string.facebook), null);
        String jsonurl = "http://10.117.11.116:8080/api/v1/getselfsongs?";
        jsonurl += fid;

        StringRequest stringreq = new StringRequest(Request.Method.GET, jsonurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("Hot", response);
                        try {
                            jsondata = new JSONArray(response);
//                            Log.d("HotFragment", jsondata.toString());
                            List<String> videoList = new ArrayList<>();
                            for (int i = 0; i < jsondata.length(); i++) {
                                try {
                                    Log.d("JSON DATA", jsondata.getJSONObject(i).getJSONObject("songDetails").getString("track_name"));
                                    videoList.add(jsondata.getJSONObject(i).getJSONObject("songDetails").getString("track_name"));
                                } catch (JSONException e){

                                }
                            }

                            List<CardItem> cardItems = new ArrayList<>();
                            for (int i = 0; i < jsondata.length(); i++) {
                                CardItem item = new CardItem(jsondata.getJSONObject(i).getJSONObject("songDetails").getString("album_coverart_100x100"), jsondata.getJSONObject(i).getJSONObject("songDetails").getString("track_name"), jsondata.getJSONObject(i).getJSONObject("songDetails").getString("artist_name"), jsondata.getJSONObject(i).getJSONObject("songDetails").getString("album_name"), jsondata.getJSONObject(i).getJSONObject("songDetails").getString("track_spotify_id"), jsondata.getJSONObject(i).getJSONObject("songDetails").getString("track_soundcloud_id"), jsondata.getJSONObject(i).getString("videoId"));
                                cardItems.add(item);
                            }
                            Log.d("CHECK", "HERE");
//                            mVideoListAdapter = new ArrayAdapter<>(getActivity(), R.layout.card_item, R.id.SongTitle, videoList);
//
                            rootView = inflater.inflate(R.layout.card_video_list, container, false);
//
                            CustomCardAdapter mVideoListAdapter = new CustomCardAdapter(getActivity(),cardItems);

                            ListView listView = (ListView) rootView.findViewById(R.id.TestDetailsView);
                            listView.setAdapter(mVideoListAdapter);



                        } catch (JSONException e) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        stringreq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringreq);
        Log.d("CHECK", "HERE1");
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
