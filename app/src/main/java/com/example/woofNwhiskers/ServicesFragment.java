package com.example.woofNwhiskers;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.example.codeseasy.com.firebaseauth.R;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServicesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServicesFragment extends Fragment {

    FirebaseAuth auth;
    Button button;
    TextView textView;
    LinearLayout linkServiceProv, linkServiceSeek;
    String name, email, uid, dp;

    FirebaseUser user;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ServicesFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ServicesFragment.
     */
    // TODO: Rename and change types and number of paramejagjyot@gmail.comters
    public static ServicesFragment newInstance(String param1, String param2) {
        ServicesFragment fragment = new ServicesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_services, container, false);
//        return inflater.inflate(R.layout.fragment_services, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
//        button = findViewById(R.id.logout);
        linkServiceProv = view.findViewById(R.id.linkProvServ);
        linkServiceSeek = view.findViewById(R.id.linkSeekServ);

//        textView = findViewById(R.id.user_details);
        if (user !=null){
            //User is signed in stay in my profile
            //set email of logged in user
            //name=User.child("Name").getValue();

            email = user.getEmail();
            uid=user.getUid();
            Log.i("currentUser",email+uid);
        }
        else {
            //user not signed in, go to login/register
            startActivity(new Intent(getContext(), MainActivity.class));
            getActivity().finish();
        }
//        else {
//            textView.setText(user.getEmail());
//        }

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(getApplicationContext(), Login.class);
//                startActivity(intent);
//                finish();
//            }
//        });

        linkServiceProv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ServiceProviderNConfirm.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        linkServiceSeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ServiceSeeker.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }
}