package com.princeparadoxes.watertracker.presentation.base;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.View;

import java.lang.ref.WeakReference;

public class FragmentSwitcherCompat {

    public static boolean hasFragmentsInBackStack(FragmentManager manager) {
        return manager.getBackStackEntryCount() > 0;
    }

    public static boolean canActivityClose(FragmentManager manager) {
        return manager.getBackStackEntryCount() == 1;
    }

    public static FragmentSwitcherBuilder start(FragmentManager manager) {
        return new FragmentSwitcherBuilder(manager);
    }

    public static class FragmentSwitcherBuilder {
        private FragmentManager mManager;
        private Fragment mFragment;
        private FragmentTransaction mTransaction;
        private String mTag;
        private int mContainerId;

        ////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////  INIT  //////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////

        public FragmentSwitcherBuilder(FragmentManager manager) {
            mManager = manager;
        }

        public FragmentSwitcherBuilder fragment(Fragment fragment) {
            checkFragmentManager();
            mFragment = fragment;
            FragmentManager manager = mManager; // TODO check on null. The check above is not enough;
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.setAllowOptimization(true);
            mTransaction = transaction;
            return this;
        }

        public FragmentSwitcherBuilder containerId(@IdRes int containerId) {
            mContainerId = containerId;
            return this;
        }

        public FragmentSwitcherBuilder containerActivity(HasFragmentContainer container) {
            mContainerId = container.fragmentsContainerId();
            return this;
        }

        public FragmentSwitcherBuilder tag(String tag) {
            mTag = tag;
            return this;
        }

        ////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////  ACTIONS  ///////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////

        public void addIfNotAdded() {
            checkFragment();
            checkFragmentManager();
            checkContainerID();
            checkTransaction();
            FragmentManager fragmentManager = mManager;
            if (fragmentManager.findFragmentByTag(mTag) != null) return;
            Fragment fragment = mFragment;
            FragmentTransaction fragmentTransaction = mTransaction;
            fragmentTransaction.add(mContainerId, fragment, mTag);
            fragmentTransaction.commitAllowingStateLoss();
        }

        public FragmentSwitcherBuilder add() {
            checkFragment();
            checkContainerID();
            checkTransaction();
            Fragment fragment = mFragment;
            FragmentTransaction fragmentTransaction = mTransaction;
            fragmentTransaction.add(mContainerId, fragment, mTag);
            fragmentTransaction.commitAllowingStateLoss();
            return this;
        }

        public FragmentSwitcherBuilder addWithAddToBackStack() {
            checkFragment();
            checkContainerID();
            checkTransaction();
            Fragment fragment = mFragment;
            FragmentTransaction fragmentTransaction = mTransaction;
            fragmentTransaction.add(mContainerId, fragment, mTag);
            fragmentTransaction.addToBackStack(mTag);
            fragmentTransaction.commitAllowingStateLoss();
            return this;
        }

        public FragmentSwitcherBuilder replace() {
            checkFragment();
            checkContainerID();
            checkTransaction();
            Fragment fragment = mFragment;
            FragmentTransaction fragmentTransaction = mTransaction;
            fragmentTransaction.replace(mContainerId, fragment, mTag);
            fragmentTransaction.commitAllowingStateLoss();
            return this;
        }

        public FragmentSwitcherBuilder replaceWithAddToBackStack() {
            return replaceWithAddToBackStack(null);
        }

        public FragmentSwitcherBuilder replaceWithAddToBackStack(String tag) {
            checkFragment();
            checkContainerID();
            checkTransaction();
            Fragment fragment = mFragment;
            FragmentTransaction fragmentTransaction = mTransaction;
            fragmentTransaction.replace(mContainerId, fragment, tag);
            fragmentTransaction.addToBackStack(mTag);
            fragmentTransaction.commitAllowingStateLoss();
            return this;
        }

        public FragmentSwitcherBuilder showDialog() {
            checkFragment();
            checkDialogFragment();
            checkTransaction();
            Fragment fragment = mFragment;
            FragmentTransaction fragmentTransaction = mTransaction;
            fragmentTransaction.addToBackStack(mTag);
            ((DialogFragment) fragment).show(fragmentTransaction, mTag);
            return this;
        }

        public FragmentSwitcherBuilder popBackStack() {
            checkFragmentManager();
            FragmentManager manager = mManager;
            manager.popBackStack();
            return this;
        }

        public FragmentSwitcherBuilder popBackStackImmediate() {
            checkFragmentManager();
            FragmentManager manager = mManager;
            manager.popBackStackImmediate();
            return this;
        }

        public FragmentSwitcherBuilder clearStackImmediate() {
            checkFragmentManager();
            FragmentManager manager = mManager;
            manager.popBackStackImmediate(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            return this;
        }

        public FragmentSwitcherBuilder clearStack() {
            checkFragmentManager();
            FragmentManager manager = mManager;
            manager.popBackStack(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            return this;
        }

        public FragmentSwitcherBuilder clearStackTo(int numberFragmentInStack) {
            checkFragmentManager();
            FragmentManager manager = mManager;
            manager.popBackStack(numberFragmentInStack, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            return this;
        }

        public FragmentSwitcherBuilder clearStackTo(Fragment fragment) {
            checkFragmentManager();
            FragmentManager manager = mManager;
            int index = getIndex(fragment.getTag(), manager);
            manager.popBackStack(index, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            return this;
        }

        private int getIndex(String tag, FragmentManager manager) {
            for (int i = 0; i < manager.getBackStackEntryCount(); i++) {
                if (manager.getBackStackEntryAt(i).getName().equalsIgnoreCase(tag)) {
                    return i;
                }
            }
            return -1;
        }

        public FragmentSwitcherBuilder remove() {
            checkFragment();
            checkTransaction();
            Fragment fragment = mFragment;
            FragmentTransaction fragmentTransaction = mTransaction;
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commitAllowingStateLoss();
            return this;
        }

        public FragmentSwitcherBuilder setTargetFragment(Fragment targetFragment, int code) {
            checkFragment();
            Fragment fragment = mFragment;
            fragment.setTargetFragment(targetFragment, code);
            return this;
        }

        ///////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////  ANIMATIONS  /////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////

        public FragmentSwitcherBuilder slideToLeftAndScaleOut() {
            checkFragment();
            checkTransaction();
            FragmentTransaction fragmentTransaction = mTransaction;
//            fragmentTransaction.setCustomAnimations(
//                    R.anim.ui_window_slide_in_left,
//                    R.anim.ui_window_scale_out,
//                    R.anim.ui_window_scale_in_back,
//                    R.anim.ui_window_slide_out_right_back);
//            fragmentTransaction.setCustomAnimations(
//                    R.anim.animation_in_enter,
//                    R.anim.animation_in_exit,
//                    R.anim.animation_out_enter,
//                    R.anim.animation_out_exit);
            return this;
        }

        ////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////  TRANSITIONS  /////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////

        public interface AddTransitionCompatInterface {
            FragmentSwitcherBuilder add(FragmentSwitcherBuilder fragmentSwitcherCompat);
        }

        public FragmentSwitcherBuilder addTransitionsCompat(
                AddTransitionCompatInterface addTransitionCompatInterface) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                return this;
            } else {
                return addTransitionCompatInterface.add(this);
            }
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public FragmentSwitcherBuilder addSharedElement(View sharedElement, String name) {
            checkTransaction();
            FragmentTransaction transaction = mTransaction;
            transaction.addSharedElement(sharedElement, name);
            return this;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public FragmentSwitcherBuilder slideAnimation() {
            checkFragment();
            Fragment fragment = mFragment;
            fragment.setAllowEnterTransitionOverlap(false);
            fragment.setAllowReturnTransitionOverlap(false);
            return enterTransition(new Slide(Gravity.RIGHT))
                    .reenterTransition(new Slide(Gravity.LEFT))
                    .exitTransition(new Slide(Gravity.LEFT))
                    .returnTransition(new Slide(Gravity.RIGHT));
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public FragmentSwitcherBuilder fadeAnimation() {
            checkFragment();
            Fragment fragment = mFragment;
            fragment.setAllowEnterTransitionOverlap(true);
            fragment.setAllowReturnTransitionOverlap(true);
            return enterTransition(new Fade())
                    .reenterTransition(new Fade())
                    .exitTransition(new Fade())
                    .returnTransition(new Fade());
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public FragmentSwitcherBuilder slideWithFadeAnimation() {
            checkFragment();
            Fragment fragment = mFragment;
            fragment.setAllowEnterTransitionOverlap(false);
            fragment.setAllowReturnTransitionOverlap(false);
            TransitionSet enter = new TransitionSet();
            enter.addTransition(new Slide(Gravity.RIGHT));
            enter.addTransition(new Fade());
            TransitionSet reenter = new TransitionSet();
            reenter.addTransition(new Slide(Gravity.LEFT));
            reenter.addTransition(new Fade());
            TransitionSet exit = new TransitionSet();
            exit.addTransition(new Slide(Gravity.LEFT));
            exit.addTransition(new Fade());
            TransitionSet returnT = new TransitionSet();
            returnT.addTransition(new Slide(Gravity.RIGHT));
            returnT.addTransition(new Fade());
            return enterTransition(enter)
                    .reenterTransition(reenter)
                    .exitTransition(exit)
                    .returnTransition(returnT);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public FragmentSwitcherBuilder enterTransition(Transition transition) {
            checkFragment();
            Fragment fragment = mFragment;
            fragment.setEnterTransition(transition);
            return this;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public FragmentSwitcherBuilder exitTransition(Transition transition) {
            checkFragment();
            Fragment fragment = mFragment;
            fragment.setExitTransition(transition);
            return this;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public FragmentSwitcherBuilder returnTransition(Transition transition) {
            checkFragment();
            Fragment fragment = mFragment;
            fragment.setReturnTransition(transition);
            return this;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public FragmentSwitcherBuilder reenterTransition(Transition transition) {
            checkFragment();
            Fragment fragment = mFragment;
            fragment.setReenterTransition(transition);
            return this;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public FragmentSwitcherBuilder setAllowEnterTransitionOverlap(boolean overlap) {
            checkFragment();
            Fragment fragment = mFragment;
            fragment.setAllowEnterTransitionOverlap(overlap);
            return this;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public FragmentSwitcherBuilder setAllowReturnTransitionOverlap(boolean overlap) {
            checkFragment();
            Fragment fragment = mFragment;
            fragment.setAllowReturnTransitionOverlap(overlap);
            return this;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public FragmentSwitcherBuilder sharedElementEnter(Transition transition) {
            checkFragment();
            Fragment fragment = mFragment;
            fragment.setSharedElementEnterTransition(transition);
            return this;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public FragmentSwitcherBuilder sharedElementReturn(Transition transition) {
            checkFragment();
            Fragment fragment = mFragment;
            fragment.setSharedElementReturnTransition(transition);
            return this;
        }

        ////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////  ERRORS  ////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////
        private void checkFragmentManager() {
            if (mManager == null) {
                throw new RuntimeException("Fragment manager must be not null");
            }
        }

        private void checkFragment() {
            if (mFragment == null) {
                throw new RuntimeException("Fragment must be not null");
            }
        }

        private void checkDialogFragment() {
            if (!(mFragment instanceof DialogFragment)) {
                throw new RuntimeException("Fragment must be instance of DialogFragment");
            }
        }

        private void checkContainerID() {
            if (mContainerId == 0) {
                throw new RuntimeException("Container must be not 0");
            }
        }

        private void checkTransaction() {
            if (mTransaction == null) {
                throw new RuntimeException("Transaction must be not null");
            }
        }
    }
}

