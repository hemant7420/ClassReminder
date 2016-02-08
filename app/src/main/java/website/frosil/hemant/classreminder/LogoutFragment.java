package website.frosil.hemant.classreminder;

/* # CSIT 6000B    #  HEMANT SINGHI        20294499          hssinghi@connect.ust.hk */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LogoutFragment extends Fragment {

    public LogoutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Intent intent = new Intent(getActivity(), WelcomeActivity.class);
        getActivity().stopService(new Intent(getActivity(),DisplayAlarmService.class));
        getActivity().finish();
        startActivity(intent);

        return inflater.inflate(R.layout.fragment_default, container, false);
    }


}