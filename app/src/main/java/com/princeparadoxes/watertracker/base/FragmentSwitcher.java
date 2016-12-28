package com.princeparadoxes.watertracker.base;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;
import android.view.View;

import java.lang.ref.WeakReference;

public class FragmentSwitcher {

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
        private WeakReference<FragmentManager> mManagerReference;
        private WeakReference<Fragment> mFragmentReference;
        private WeakReference<FragmentTransaction> mTransactionReference;
        private int mContainerId;

        ////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////  INIT  //////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////

        public FragmentSwitcherBuilder(FragmentManager manager) {
            mManagerReference = new WeakReference<>(manager);
        }

        public FragmentSwitcherBuilder fragment(Fragment fragment) {
            checkFragmentManager();
            mFragmentReference = new WeakReference<>(fragment);
            FragmentManager manager = mManagerReference.get(); // TODO check on null. The check above is not enough;
            mTransactionReference = new WeakReference<>(manager.beginTransaction());
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

        ////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////  ACTIONS  ///////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////

        public FragmentSwitcherBuilder replace() {
            checkFragment();
            checkContainerID();
            checkTransaction();
            Fragment fragment = mFragmentReference.get();
            FragmentTransaction fragmentTransaction = mTransactionReference.get();
            fragmentTransaction.replace(mContainerId, fragment, fragment.getTag());
            fragmentTransaction.commit();
            return this;
        }

        public FragmentSwitcherBuilder replaceWithAddToBackStack() {
            checkFragment();
            checkContainerID();
            checkTransaction();
            Fragment fragment = mFragmentReference.get();
            FragmentTransaction fragmentTransaction = mTransactionReference.get();
            fragmentTransaction.replace(mContainerId, fragment, fragment.getTag());
            fragmentTransaction.addToBackStack(fragment.getTag());
            fragmentTransaction.commit();
            return this;
        }

        public FragmentSwitcherBuilder showDialog() {
            checkFragment();
            checkDialogFragment();
            checkTransaction();
            Fragment fragment = mFragmentReference.get();
            FragmentTransaction fragmentTransaction = mTransactionReference.get();
            fragmentTransaction.addToBackStack(fragment.getTag());
            ((DialogFragment) fragment).show(fragmentTransaction, fragment.getTag());
            return this;
        }

        public FragmentSwitcherBuilder popBackStack() {
            checkFragmentManager();
            FragmentManager manager = mManagerReference.get();
            manager.popBackStack();
            return this;
        }

        public FragmentSwitcherBuilder popBackStackImmediate() {
            checkFragmentManager();
            FragmentManager manager = mManagerReference.get();
            manager.popBackStackImmediate();
            return this;
        }

        public FragmentSwitcherBuilder clearStackImmediate() {
            checkFragmentManager();
            FragmentManager manager = mManagerReference.get();
            manager.popBackStackImmediate(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            return this;
        }

        public FragmentSwitcherBuilder clearStack() {
            checkFragmentManager();
            FragmentManager manager = mManagerReference.get();
            manager.popBackStack(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            return this;
        }

        public FragmentSwitcherBuilder clearStackTo(int numberFragmentInStack) {
            checkFragmentManager();
            FragmentManager manager = mManagerReference.get();
            manager.popBackStack(numberFragmentInStack, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            return this;
        }

        public FragmentSwitcherBuilder clearStackTo(Fragment fragment) {
            checkFragmentManager();
            FragmentManager manager = mManagerReference.get();
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
            return 0;
        }

        public FragmentSwitcherBuilder setTargetFragment(Fragment targetFragment, int code) {
            checkFragment();
            Fragment fragment = mFragmentReference.get();
            fragment.setTargetFragment(targetFragment, code);
            return this;
        }

        ////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////  TRANSITIONS  /////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public FragmentSwitcherBuilder addSharedElement(View sharedElement, String name) {
            checkTransaction();
            FragmentTransaction transaction = mTransactionReference.get();
            transaction.addSharedElement(sharedElement, name);
            return this;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public FragmentSwitcherBuilder defaultAnimation() {
            checkFragment();
            Fragment fragment = mFragmentReference.get();
            fragment.setAllowEnterTransitionOverlap(false);
            fragment.setAllowReturnTransitionOverlap(false);
            return enterTransition(new Slide(Gravity.RIGHT))
                    .reenterTransition(new Slide(Gravity.LEFT))
                    .exitTransition(new Slide(Gravity.LEFT))
                    .returnTransition(new Slide(Gravity.RIGHT));
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public FragmentSwitcherBuilder enterTransition(Transition transition) {
            checkFragment();
            Fragment fragment = mFragmentReference.get();
            fragment.setEnterTransition(transition);
            return this;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public FragmentSwitcherBuilder exitTransition(Transition transition) {
            checkFragment();
            Fragment fragment = mFragmentReference.get();
            fragment.setExitTransition(transition);
            return this;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public FragmentSwitcherBuilder returnTransition(Transition transition) {
            checkFragment();
            Fragment fragment = mFragmentReference.get();
            fragment.setReturnTransition(transition);
            return this;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public FragmentSwitcherBuilder reenterTransition(Transition transition) {
            checkFragment();
            Fragment fragment = mFragmentReference.get();
            fragment.setReenterTransition(transition);
            return this;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public FragmentSwitcherBuilder sharedElementEnter(Transition transition) {
            checkFragment();
            Fragment fragment = mFragmentReference.get();
            fragment.setSharedElementEnterTransition(transition);
            return this;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public FragmentSwitcherBuilder sharedElementReturn(Transition transition) {
            checkFragment();
            Fragment fragment = mFragmentReference.get();
            fragment.setSharedElementReturnTransition(transition);
            return this;
        }

        ////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////  ERRORS  ////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////
        private void checkFragmentManager() {
            if (mManagerReference.get() == null) {
                throw new RuntimeException("Fragment manager must be not null");
            }
        }

        private void checkFragment() {
            if (mFragmentReference.get() == null) {
                throw new RuntimeException("Fragment must be not null");
            }
        }

        private void checkDialogFragment() {
            if (!(mFragmentReference.get() instanceof DialogFragment)) {
                throw new RuntimeException("Fragment must be instance of DialogFragment");
            }
        }

        private void checkContainerID() {
            if (mContainerId == 0) {
                throw new RuntimeException("Container must be not 0");
            }
        }

        private void checkTransaction() {
            if (mTransactionReference.get() == null) {
                throw new RuntimeException("Transaction must be not null");
            }
        }
    }
}

