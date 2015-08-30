package com.sequoiahack.jarvis.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sequoiahack.jarvis.R;
import com.sequoiahack.jarvis.widget.JarvisTextView;

public class FirstFragment extends Fragment {

    public FirstFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        JarvisTextView textView = (JarvisTextView) view.findViewById(R.id.fragment_first_text);
       /* textView.animate() setText();

        Typewriter writer = new Typewriter(this);
        setContentView(writer);
*/
        //Add a character every 150ms
    //    textView.setCharacterDelay(175);
    //    textView.animateText("Yes Master !");
    }

}
