package hr.foi.teamup.fragments;

import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hr.foi.teamup.R;

/**
 * fragment that is able to change fragments dynamically
 * Created by Tomislav Turek on 20.01.16..
 */
public abstract class LayoutExchangeFragment extends Fragment {

    /**
     * sets the new layout
     * @param layoutId id of the layout (R.layout.l)
     */
    public void setViewLayout(int layoutId) {
        setViewLayout(layoutId, null);
    }

    /**
     * sets the new layout and customizes the elements which
     * is implemented inside ViewCustomization implementation
     * @param layoutId id of the layout (R.layout.l)
     * @param customization element customizations (color change, text setting, ...)
     */
    public void setViewLayout(int layoutId, ViewCustomization customization){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mainView= inflater.inflate(layoutId, null);

        if(customization != null)
            customization.customize(mainView);

        ViewGroup rootView = (ViewGroup) getView();
        rootView.removeAllViews();
        rootView.addView(mainView);
    }

}
