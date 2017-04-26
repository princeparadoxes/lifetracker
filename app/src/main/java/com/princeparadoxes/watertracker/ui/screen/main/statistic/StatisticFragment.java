package com.princeparadoxes.watertracker.ui.screen.main.statistic;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.princeparadoxes.watertracker.ProjectApplication;
import com.princeparadoxes.watertracker.R;
import com.princeparadoxes.watertracker.base.BaseFragment;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;

public class StatisticFragment extends BaseFragment
        implements
        DiscreteScrollView.OnItemChangedListener<StatisticTypeAdapter.ViewHolder>,
        DiscreteScrollView.ScrollStateChangeListener<StatisticTypeAdapter.ViewHolder> {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  VIEWS  //////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @BindView(R.id.statistic_type_view)
    DiscreteScrollView mTypePicker;
    @BindView(R.id.statistic_forecast_view)
    StatisticTypeView mStatisticTypeView;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  FIELDS  /////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private CompositeDisposable mDisposable;
    private List<StatisticType> mStatisticTypes;

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
        mDisposable = new CompositeDisposable();
        mStatisticTypes = Arrays.asList(StatisticType.values());
        mTypePicker.setAdapter(new StatisticTypeAdapter(mStatisticTypes));
        mTypePicker.setOnItemChangedListener(this);
        mTypePicker.setScrollStateChangeListener(this);
        mTypePicker.scrollToPosition(0);
        mTypePicker.setItemTransitionTimeMillis(150);
        mTypePicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());

        mStatisticTypeView.setForecast(mStatisticTypes.get(0));
    }


    @Override
    public void onCurrentItemChanged(@NonNull StatisticTypeAdapter.ViewHolder viewHolder, int adapterPosition) {
        mStatisticTypeView.setForecast(mStatisticTypes.get(adapterPosition));
        viewHolder.showText();
    }

    @Override
    public void onScrollStart(@NonNull StatisticTypeAdapter.ViewHolder currentItemHolder, int adapterPosition) {
        currentItemHolder.hideText();
    }

    @Override
    public void onScrollEnd(@NonNull StatisticTypeAdapter.ViewHolder currentItemHolder, int adapterPosition) {

    }

    @Override
    public void onScroll(float scrollPosition,
                         @NonNull StatisticTypeAdapter.ViewHolder currentHolder,
                         @NonNull StatisticTypeAdapter.ViewHolder newCurrent) {
        StatisticType current = mStatisticTypes.get(mTypePicker.getCurrentItem());
        int nextPosition = mTypePicker.getCurrentItem() + (scrollPosition > 0 ? -1 : 1);
        if (nextPosition >= 0 && nextPosition < mTypePicker.getAdapter().getItemCount()) {
            StatisticType next = mStatisticTypes.get(nextPosition);
            mStatisticTypeView.onScroll(1f - Math.abs(scrollPosition), current, next);
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
