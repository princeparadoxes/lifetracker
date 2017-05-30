package com.princeparadoxes.watertracker.ui.screen.main.statistic;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.princeparadoxes.watertracker.ProjectApplication;
import com.princeparadoxes.watertracker.R;
import com.princeparadoxes.watertracker.base.BaseFragment;
import com.princeparadoxes.watertracker.data.model.Drink;
import com.princeparadoxes.watertracker.data.model.StatisticType;
import com.princeparadoxes.watertracker.data.repository.DrinkRepository;
import com.princeparadoxes.watertracker.data.rx.SchedulerTransformer;
import com.princeparadoxes.watertracker.utils.AnimatorUtils;
import com.princeparadoxes.watertracker.utils.DimenTools;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindDrawable;
import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;

public class StatisticFragment extends BaseFragment
        implements
        DiscreteScrollView.OnItemChangedListener<StatisticItemViewHolder>,
        DiscreteScrollView.ScrollStateChangeListener<StatisticItemViewHolder> {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  INJECTS  ////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Inject
    DrinkRepository mDrinkRepository;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  VIEWS  //////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @BindView(R.id.statistic_header)
    TextView mStatisticHeader;
    @BindView(R.id.statistic_type_view)
    DiscreteScrollView mTypePicker;
    @BindView(R.id.statistic_forecast_view)
    StatisticChartView mStatisticChartView;

    @BindDrawable(R.drawable.ic_chevron_down)
    Drawable mChevronDownDrawable;
    @BindDrawable(R.drawable.ic_chevron_up)
    Drawable mChevronUpDrawable;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  FIELDS  /////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private CompositeDisposable mDisposable;
    private List<StatisticType> mStatisticTypes;
    private BottomSheetBehavior mStatisticBottomSheetBehavior;
    private StatisticAdapter mStatisticAdapter;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////  INIT SCREEN  /////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectApplication app = ProjectApplication.get(getActivity());
        StatisticScope.Component component = DaggerStatisticScope_Component.builder()
                .projectComponent(app.component())
                .build();
        component.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mStatisticAdapter = new StatisticAdapter();
        mStatisticBottomSheetBehavior = BottomSheetBehavior.from(container);
        mStatisticBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                changeHeaderState(newState);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        mStatisticBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mStatisticBottomSheetBehavior.setHideable(false);
        mStatisticBottomSheetBehavior.setPeekHeight((int) DimenTools.pxFromDp(getContext(), 64));
        return view;
    }

    private void changeHeaderState(int newState) {
        switch (newState) {
            case BottomSheetBehavior.STATE_COLLAPSED:
                createHeaderAnimator(R.string.statistic_header_open, mChevronUpDrawable).start();
                break;
            case BottomSheetBehavior.STATE_EXPANDED:
                createHeaderAnimator(R.string.statistic_header_closed, mChevronDownDrawable).start();
                break;
        }
    }

    @NonNull
    private AnimatorSet createHeaderAnimator(int text, Drawable drawable) {
        AnimatorSet set = new AnimatorSet();
        Animator out = AnimatorUtils.alphaAnimator(1, 0, mStatisticHeader, 200, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mStatisticHeader.setText(text);
                mStatisticHeader.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
            }
        });
        out.setInterpolator(new AccelerateInterpolator());
        Animator in = AnimatorUtils.alphaAnimator(0, 1, mStatisticHeader, 200);
        in.setInterpolator(new DecelerateInterpolator());
        set.playSequentially(out, in);
        return set;
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_statistic;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////  START SCREEN  ////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onStart() {
        super.onStart();

        float a1 = 50;
        float a2 = 50*7;
        float a3 = 50*30;
        float a4 = 50*365;

        List<StatisticModel> statisticModelList = new ArrayList<>();
        statisticModelList.add(new StatisticModel(StatisticType.DAY, a1));
        statisticModelList.add(new StatisticModel(StatisticType.WEEK, a2));
        statisticModelList.add(new StatisticModel(StatisticType.MONTH, a3));
        statisticModelList.add(new StatisticModel(StatisticType.YEAR, a4));
        mStatisticAdapter.setData(statisticModelList);

        mDisposable = new CompositeDisposable();
        mStatisticTypes = Arrays.asList(StatisticType.values());
        mTypePicker.setAdapter(mStatisticAdapter);
        mTypePicker.setOnItemChangedListener(this);
        mTypePicker.setScrollStateChangeListener(this);
        mTypePicker.scrollToPosition(0);
        mTypePicker.setItemTransitionTimeMillis(150);
        mTypePicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());

        mStatisticChartView.setForecast(mStatisticTypes.get(0));

        mDisposable.add(mDrinkRepository.add(new Drink(100, System.currentTimeMillis()))
                .compose(SchedulerTransformer.getInstance())
                .subscribe(this::handleAddDrink, this::handleAddDrinkError));
    }

    private void handleAddDrink(Drink drink) {

    }

    private void handleAddDrinkError(Throwable throwable) {

    }


    @Override
    public void onCurrentItemChanged(@NonNull StatisticItemViewHolder viewHolder, int adapterPosition) {
        mStatisticChartView.setForecast(mStatisticTypes.get(adapterPosition));
        viewHolder.showText();
    }

    @Override
    public void onScrollStart(@NonNull StatisticItemViewHolder currentItemHolder, int adapterPosition) {
        currentItemHolder.hideText();
    }

    @Override
    public void onScrollEnd(@NonNull StatisticItemViewHolder currentItemHolder, int adapterPosition) {

    }

    @Override
    public void onScroll(float scrollPosition,
                         @NonNull StatisticItemViewHolder currentHolder,
                         @NonNull StatisticItemViewHolder newCurrent) {
        StatisticType current = mStatisticTypes.get(mTypePicker.getCurrentItem());
        int nextPosition = mTypePicker.getCurrentItem() + (scrollPosition > 0 ? -1 : 1);
        if (nextPosition >= 0 && nextPosition < mTypePicker.getAdapter().getItemCount()) {
            StatisticType next = mStatisticTypes.get(nextPosition);
            mStatisticChartView.onScroll(1f - Math.abs(scrollPosition), current, next);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  STOP SCREEN  ////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onStop() {
        mDisposable.dispose();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mStatisticBottomSheetBehavior.setBottomSheetCallback(null);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////  NAVIGATION  ////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  INSTANCE  ///////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public static StatisticFragment newInstance() {
        Bundle args = new Bundle();
        StatisticFragment fragment = new StatisticFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
