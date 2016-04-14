package layout;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import io.github.ghostwriternr.selene.CardItem;
import io.github.ghostwriternr.selene.CustomCardAdapter;
import io.github.ghostwriternr.selene.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {

    ArrayAdapter<String> mFriendListAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public UserFragment() {
        // Required empty public constructor
    }

    JSONArray jsondata;
    JSONObject myname;
    View rootView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
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
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RequestQueue queue = Volley.newRequestQueue(getContext());
        SharedPreferences sharedPref = getContext().getSharedPreferences("io.github.ghostwriternr", Context.MODE_PRIVATE);
        String fid = sharedPref.getString(getString(R.string.facebook), null);
        String jsonurl = "http://10.5.27.227:8080/api/v1/getfriends?";
        jsonurl += fid;

        StringRequest stringreq = new StringRequest(Request.Method.GET, jsonurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String nametoassign = "";
                        Log.v("Hot", response);
                        try {
                            jsondata = new JSONArray(response);
//                            Log.d("HotFragment", jsondata.toString());
                            List<String> friendList = new ArrayList<>();
                            int i;
                            for (i = 0; i < jsondata.length()-1; i++) {
                                try {
                                    Log.d("JSON DATA", jsondata.getJSONObject(i).getString("name"));
                                    friendList.add(jsondata.getJSONObject(i).getString("name"));
                                } catch (JSONException e){

                                }
                            }
                            myname = jsondata.getJSONObject(i);
                            nametoassign =  myname.getString("myname");
                            Log.d("CHECK", "HERE");
                            mFriendListAdapter = new ArrayAdapter<>(getActivity(), R.layout.friend_item, R.id.friendname, friendList);
//
                            rootView = inflater.inflate(R.layout.fragment_user, container, false);

                            ExpandableHeightListView listView = (ExpandableHeightListView) rootView.findViewById(R.id.friendlist);
                            listView.setAdapter(mFriendListAdapter);
                            listView.setExpanded(true);
                            TextView name = (TextView) rootView.findViewById(R.id.myName);
                            name.setText(nametoassign);
                            CollapsingToolbarLayout collapsingToolbar =
                                    (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);
                            collapsingToolbar.setTitle("Profile");

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
//        View createdView =  inflater.inflate(R.layout.fragment_user, container, false);
//        CollapsingToolbarLayout collapsingToolbar =
//                (CollapsingToolbarLayout) createdView.findViewById(R.id.collapsing_toolbar);
//        collapsingToolbar.setTitle("Profile");
//        setupCardAnimations();
//        return createdView;
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

    private void setupCardAnimations() {
        Fade fade = new Fade();
        fade.setDuration(100);
    }
}
