package com.princeparadoxes.watertracker.ui.screen.main.start;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.view.ViewCompat;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.princeparadoxes.watertracker.ProjectApplication;
import com.princeparadoxes.watertracker.R;
import com.princeparadoxes.watertracker.base.BaseFragment;
import com.princeparadoxes.watertracker.utils.AnimatorUtils;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;

public class StartFragment extends BaseFragment {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  VIEWS  //////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @BindView(R.id.fragment_start_center_text)
    TextView mCenterText;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  FIELDS  /////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private CompositeDisposable mDisposable;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////  INIT SCREEN  /////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectApplication app = ProjectApplication.get(getActivity());
        StartScope.Component component = DaggerStartScope_Component.builder()
                .projectComponent(app.component())
                .build();
        component.inject(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_start;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////  START SCREEN  ////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onStart() {
        super.onStart();
        mDisposable = new CompositeDisposable();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCenterText.setText(R.string.start_thanks);
        Animator animator = createChangeTextAnimatorSet(
                R.string.start_you_are_beautiful,
                R.string.start_teach_you);
        animator.setStartDelay(1000);
        animator.start();

    }

    private Animator createChangeTextAnimatorSet(@StringRes int... texts) {
        AnimatorSet set = new AnimatorSet();
        Animator[] animators = new Animator[texts.length];
        for (int i = 0; i < texts.length; i++) {
            animators[i] = createChangeTextAnimator(texts[i]);
        }
        set.playSequentially(animators);
        return set;
    }

    private Animator createChangeTextAnimator(@StringRes int text) {
        AnimatorSet set = new AnimatorSet();
        Animator out = AnimatorUtils.alphaAnimator(1, 0, mCenterText, 1000, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCenterText.setText(text);
            }
        });
        out.setStartDelay(1000);
        out.setInterpolator(new AccelerateInterpolator());
        Animator in = AnimatorUtils.alphaAnimator(0, 1, mCenterText, 1000);
        in.setInterpolator(new DecelerateInterpolator());
        set.playSequentially(out, in);
        return set;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  STOP SCREEN  ////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onStop() {
        mDisposable.dispose();
        super.onStop();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////  NAVIGATION  ////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  INSTANCE  ///////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public static StartFragment newInstance() {
        Bundle args = new Bundle();
        StartFragment fragment = new StartFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
