package ca.qc.johnabbott.cs616.healthhaven.ui.HealthCenter;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.qc.johnabbott.cs616.healthhaven.R;

/**
 * Health Center/Hub Fragment. Looking for the code? It's in the activity.
 *
 * @author      William Fedele
 * @studentID   1679513
 * @githubUser  william-fedele
 */
public class HealthCenterFragment extends Fragment {

    public HealthCenterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_health_hub, container, false);
        return root;
    }
}
