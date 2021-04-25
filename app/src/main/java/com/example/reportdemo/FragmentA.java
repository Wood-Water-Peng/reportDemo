package com.example.reportdemo;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lib.ActivityLifecycleTracker;
import com.example.lib.BaseReportFragment;
import com.example.lib.PageNodeManager;
import com.example.lib.ViewUtil;
import com.example.lib.event.ViewClickEvent;
import com.example.reportdemo.fragments.FragmentA1;
import com.example.reportdemo.fragments.FragmentA2;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentA#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentA extends BaseReportFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentA1 fragmentA1;
    private FragmentA2 fragmentA2;

    public FragmentA() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentA.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentA newInstance(String param1, String param2) {
        FragmentA fragment = new FragmentA();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = getChildFragmentManager().getFragments().size();
                if (size == 2) {
                    return;
                } else if (size == 1) {
                    getChildFragmentManager().beginTransaction().add(R.id.contenta2, fragmentA2).commit();
                } else {
                    getChildFragmentManager().beginTransaction().add(R.id.contenta1, fragmentA1).commit();
                }
                ViewClickEvent.Builder builder = new ViewClickEvent.Builder();
                builder.setViewPath(ViewUtil.getViewPath(v));

                FragmentActivity activity = (FragmentActivity) v.getContext();
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                List<Fragment> fragments = fragmentManager.getFragments();
                for (int i = 0; i < fragments.size(); i++) {
                    Fragment fragment = fragments.get(i);
                    View view = fragment.getView();
                    Rect rect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
                    if (v.getLocalVisibleRect(rect)) {
                        builder.setFragmentName(fragment.getClass().getCanonicalName());
                        break;
                    }
                }
                builder.build().report();

            }
        });

        view.findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = getChildFragmentManager().getFragments().size();
                if (size == 2) {
                    getChildFragmentManager().beginTransaction().remove(fragmentA2).commit();
                } else if (size == 1) {
                    getChildFragmentManager().beginTransaction().remove(fragmentA1).commit();
                }

            }
        });
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
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentA1 = new FragmentA1();
        fragmentA2 = new FragmentA2();
        getChildFragmentManager().beginTransaction().add(R.id.contenta1,fragmentA1 ).commit();
        getChildFragmentManager().beginTransaction().add(R.id.contenta2, fragmentA2).commit();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_a, container, false);
    }
}
