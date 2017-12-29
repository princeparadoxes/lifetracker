package com.princeparadoxes.watertracker.ui.screen.main.statistic;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.princeparadoxes.watertracker.ProjectApplication;
import com.princeparadoxes.watertracker.R;
import com.princeparadoxes.watertracker.base.BaseFragment;
import com.princeparadoxes.watertracker.data.model.StatisticType;
import com.princeparadoxes.watertracker.data.repository.DrinkRepository;
import com.princeparadoxes.watertracker.data.rx.SchedulerTransformer;
import com.princeparadoxes.watertracker.data.sp.ProjectPreferences;
import com.princeparadoxes.watertracker.utils.AnimatorUtils;
import com.princeparadoxes.watertracker.utils.DimenTools;
import com.princeparadoxes.watertracker.utils.SpannableUtils;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindDrawable;
import butterknife.BindView;
import io.reactivex.Observable;
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
    @Inject
    ProjectPreferences mProjectPreferences;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  VIEWS  //////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @BindView(R.id.statistic_header)
    SwipeLayout mHeaderView;
    @BindView(R.id.statistic_header_start)
    TextView mStatisticHeaderStart;
    @BindView(R.id.statistic_header_center)
    TextView mStatisticHeaderCenter;
    @BindView(R.id.statistic_header_end)
    TextView mStatisticHeaderEnd;
    @BindView(R.id.statistic_type_view)
    DiscreteScrollView mTypePicker;
    @BindView(R.id.statistic_forecast_view)
    StatisticChartView mStatisticChartView;
    @BindView(R.id.statistic_top_border)
    View mTopBorderView;

    @BindDrawable(R.drawable.ic_chevron_down)
    Drawable mChevronDownDrawable;
    @BindDrawable(R.drawable.ic_chevron_up)
    Drawable mChevronUpDrawable;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  FIELDS  /////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private CompositeDisposable mDisposable;
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
        mDisposable = new CompositeDisposable();
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_statistic;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mStatisticAdapter = new StatisticAdapter();
        initHeader();
        initBehavior(container);
        onCollapsed();
        return view;
    }

    ///////////////////////////////////////////////////////////////////////////
    // HEADER
    ///////////////////////////////////////////////////////////////////////////

    private void initHeader() {
        mHeaderView.setLeftSwipeEnabled(true);
        mHeaderView.setRightSwipeEnabled(true);
    }

    private void changeHeaderState(int newState) {
        switch (newState) {
            case BottomSheetBehavior.STATE_COLLAPSED:
                onCollapsed();
                break;
            case BottomSheetBehavior.STATE_EXPANDED:
                onExpanded();
                break;
        }
    }

    private void onCollapsed() {
        mTopBorderView.setVisibility(View.VISIBLE);
        createHeaderAnimator(R.string.statistic_header_open, mChevronUpDrawable).start();
        loadDaySumm();
        loadLastDrink();

    }

    private void onExpanded() {
        loadAllStatistic();
        mTopBorderView.setVisibility(View.INVISIBLE);
        createHeaderAnimator(R.string.statistic_header_closed, mChevronDownDrawable).start();
//        SpannableStringBuilder builder = new SpannableStringBuilder();
//        builder.append(SpannableUtils.getAbsoluteSizeSpan(getContext(), "Day norm", 12))
//                .append("\n")
//                .append(String.valueOf(mProjectPreferences.getCurrentDayNorm()))
//                .append("ml");
//
//        mStatisticHeaderStart.setText(builder);
//        mStatisticHeaderEnd.setText("Send feedback");
    }

    @NonNull
    private AnimatorSet createHeaderAnimator(int text, Drawable drawable) {
        AnimatorSet set = new AnimatorSet();
        Animator out = AnimatorUtils.alphaAnimator(1, 0, mStatisticHeaderCenter, 200, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mStatisticHeaderCenter.setText(text);
                mStatisticHeaderCenter.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
            }
        });
        out.setInterpolator(new AccelerateInterpolator());
        Animator in = AnimatorUtils.alphaAnimator(0, 1, mStatisticHeaderCenter, 200);
        in.setInterpolator(new DecelerateInterpolator());
        set.playSequentially(out, in);
        return set;
    }

    ///////////////////////////////////////////////////////////////////////////
    // BEHAVIOR
    ///////////////////////////////////////////////////////////////////////////

    private void initBehavior(ViewGroup container) {
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
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////  START SCREEN  ////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onStart() {
        super.onStart();
        initTypePicker();
    }

    private void initTypePicker() {
        mTypePicker.setAdapter(mStatisticAdapter);
        mTypePicker.setOnItemChangedListener(this);
        mTypePicker.setScrollStateChangeListener(this);
        mTypePicker.scrollToPosition(0);
        mTypePicker.setItemTransitionTimeMillis(150);
        mTypePicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());
    }

    ///////////////////////////////////////////////////////////////////////////
    // LOAD ALL STATISTIC
    ///////////////////////////////////////////////////////////////////////////


    private void loadAllStatistic() {
        mDisposable.add(Observable.fromArray(StatisticType.values())
                .concatMap(statisticType -> mDrinkRepository.getSumByPeriod(statisticType))
                .toList()
                .toObservable()
                .compose(SchedulerTransformer.getInstance())
                .subscribe(this::handleLoadAllStatistic, this::handleLoadAllStatisticError));
    }

    private void handleLoadAllStatistic(List<StatisticModel> statisticModels) {
        mStatisticAdapter.setData(statisticModels);
    }

    private void handleLoadAllStatisticError(Throwable throwable) {
        throwable.printStackTrace();
    }

    ///////////////////////////////////////////////////////////////////////////
    // LOAD DAY STATISTIC
    ///////////////////////////////////////////////////////////////////////////

    private void loadDaySumm() {
        mDisposable.add(mDrinkRepository.getDaySum()
                .compose(SchedulerTransformer.getInstance())
                .subscribe(this::handleLoadDaySumm, this::handleLoadDaySummError));
    }

    private void handleLoadDaySumm(Float daySum) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(SpannableUtils.getAbsoluteSizeSpan(getContext(), "Day", 12))
                .append("\n")
                .append(String.valueOf(daySum))
                .append("ml");
        mStatisticHeaderStart.setText(builder);
    }

    private void handleLoadDaySummError(Throwable throwable) {
        throwable.printStackTrace();
    }

    ///////////////////////////////////////////////////////////////////////////
    // LOAD LAST DRINK
    ///////////////////////////////////////////////////////////////////////////

    private void loadLastDrink() {
        mDisposable.add(mDrinkRepository.getLastByDay()
                .compose(SchedulerTransformer.getInstance())
                .subscribe(this::handleLoadLastDrink, this::handleLoadLastDrinkError));
    }

    private void handleLoadLastDrink(Float lastByDay) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(SpannableUtils.getAbsoluteSizeSpan(getContext(), "Last", 12))
                .append("\n")
                .append(String.valueOf(lastByDay))
                .append("ml");
        mStatisticHeaderEnd.setText(builder);
    }

    private void handleLoadLastDrinkError(Throwable throwable) {
        throwable.printStackTrace();
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(SpannableUtils.getAbsoluteSizeSpan(getContext(), "Last", 12))
                .append("\n")
                .append("none");
        mStatisticHeaderEnd.setText(builder);
    }

    ///////////////////////////////////////////////////////////////////////////
    // TYPE PICKER
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void onCurrentItemChanged(@NonNull StatisticItemViewHolder viewHolder, int adapterPosition) {
        mStatisticChartView.bindView(
                new float[]{0f, 2f, 1.4f, 4.f, 3.5f, 4.3f, 2f, 4f, 6.f},
                new float[]{0f, 2f, 1.4f, 4.f, 3.5f, 4.3f, 2f, 4f, 6.f});
        viewHolder.onChanged();
    }

    @Override
    public void onScrollStart(@NonNull StatisticItemViewHolder currentItemHolder, int adapterPosition) {
        currentItemHolder.onUnchanged();
    }

    @Override
    public void onScrollEnd(@NonNull StatisticItemViewHolder currentItemHolder, int adapterPosition) {

    }

    @Override
    public void onScroll(float scrollPosition,
                         @NonNull StatisticItemViewHolder currentHolder,
                         @NonNull StatisticItemViewHolder newCurrent) {
//        StatisticType current = mStatisticTypes.get(mTypePicker.getCurrentItem());
//        int nextPosition = mTypePicker.getCurrentItem() + (scrollPosition > 0 ? -1 : 1);
//        if (nextPosition >= 0 && nextPosition < mTypePicker.getAdapter().getItemCount()) {
//            StatisticType next = mStatisticTypes.get(nextPosition);
//            mStatisticChartView.onScroll(1f - Math.abs(scrollPosition), current, next);
//        }
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
