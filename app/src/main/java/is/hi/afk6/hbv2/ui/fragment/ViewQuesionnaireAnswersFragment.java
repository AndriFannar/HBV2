package is.hi.afk6.hbv2.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import is.hi.afk6.hbv2.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewQuesionnaireAnswersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewQuesionnaireAnswersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewQuesionnaireAnswersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewQuesionnaireAnswersFragment.
     */
    // TODO: Rename and change types and number of parameters
    //Story 9 - View questionnaire score
    //As a receptionist I want to be able to see the score from the patient's request so that I can
    // put them in the right place on the priority list.
    //
    //Story 10 - View questionnaire answers
    //As a physiotherapist I want to be able to see the questionnaire answers and the score from
    // patients so that I have a better understanding of their health information.
    public static ViewQuesionnaireAnswersFragment newInstance(String param1, String param2) {
        ViewQuesionnaireAnswersFragment fragment = new ViewQuesionnaireAnswersFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_quesionnaire_answers, container, false);
    }
}