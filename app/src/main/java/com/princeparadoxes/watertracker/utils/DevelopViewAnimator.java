package com.princeparadoxes.watertracker.utils;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DevelopViewAnimator extends FrameLayout {

    private int lastWhichIndex = 0;

    public DevelopViewAnimator(Context context) {
        super(context);
    }

    public DevelopViewAnimator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setDisplayedChildIndex(int inChildIndex, boolean withAnim) {
        if (inChildIndex >= getChildCount()) {
            inChildIndex = 0;
        } else if (inChildIndex < 0) {
            inChildIndex = getChildCount() - 1;
        }
        boolean hasFocus = getFocusedChild() != null;
        int outChildIndex = lastWhichIndex;
        lastWhichIndex = inChildIndex;
        changeVisibility(getChildAt(inChildIndex), getChildAt(outChildIndex), withAnim);
        if (hasFocus) {
            requestFocus(FOCUS_FORWARD);
        }
    }

    public void setDisplayedChildId(@IdRes int id) {
        setDisplayedChildId(id, true);
    }

    public void setDisplayedChildId(@IdRes int id, boolean withAnim) {
        if (getDisplayedChildId() == id) {
            return;
        }
        for (int i = 0, count = getChildCount(); i < count; i++) {
            if (getChildAt(i).getId() == id) {
                setDisplayedChildIndex(i, withAnim);
                return;
            }
        }
        throw new IllegalArgumentException("No view with ID " + id);
    }

    public void setDisplayedChild(View view) {
        setDisplayedChildId(view.getId());
    }

    public void setDisplayedChild(View view, boolean withAnim) {
        setDisplayedChildId(view.getId(), withAnim);
    }

    public int getDisplayedChildIndex() {
        return lastWhichIndex;
    }

    public View getDisplayedChild() {
        return getChildAt(lastWhichIndex);
    }

    public int getDisplayedChildId() {
        return getDisplayedChild().getId();
    }

    protected void changeVisibility(View inChild, View outChild, boolean withAnim) {
        outChild.setVisibility(INVISIBLE);
        inChild.setVisibility(VISIBLE);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (getChildCount() == 1) {
            child.setVisibility(View.VISIBLE);
        } else {
            child.setVisibility(View.INVISIBLE);
        }
        if (index >= 0 && lastWhichIndex >= index) {
            setDisplayedChildIndex(lastWhichIndex + 1, false);
        }
    }

    @Override
    public void removeAllViews() {
        super.removeAllViews();
        lastWhichIndex = 0;
    }

    @Override
    public void removeView(View view) {
        final int index = indexOfChild(view);
        if (index >= 0) {
            removeViewAt(index);
        }
    }

    @Override
    public void removeViewAt(int index) {
        super.removeViewAt(index);
        final int childCount = getChildCount();
        if (childCount == 0) {
            lastWhichIndex = 0;
        } else if (lastWhichIndex >= childCount) {
            setDisplayedChildIndex(childCount - 1, false);
        } else if (lastWhichIndex == index) {
            setDisplayedChildIndex(lastWhichIndex, false);
        }
    }

    @Override
    public void removeViewInLayout(View view) {
        removeView(view);
    }

    @Override
    public void removeViews(int start, int count) {
        super.removeViews(start, count);
        if (getChildCount() == 0) {
            lastWhichIndex = 0;
        } else if (lastWhichIndex >= start && lastWhichIndex < start + count) {
            setDisplayedChildIndex(lastWhichIndex, false);
        }
    }

    @Override
    public void removeViewsInLayout(int start, int count) {
        removeViews(start, count);
    }

    public void bringChildToPosition(View child, int position) {
        final int index = indexOfChild(child);
        if (index < 0 && position >= getChildCount()) return;
        try {
            Method removeFromArray = ViewGroup.class.getDeclaredMethod("removeFromArray", int.class);
            removeFromArray.setAccessible(true);
            removeFromArray.invoke(this, index);
            Method addInArray = ViewGroup.class.getDeclaredMethod("addInArray", View.class, int.class);
            addInArray.setAccessible(true);
            addInArray.invoke(this, child, position);
            Field mParent = View.class.getDeclaredField("mParent");
            mParent.setAccessible(true);
            mParent.set(child, this);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        lastWhichIndex = ss.lastWhichIndex;
        setDisplayedChildIndex(lastWhichIndex, false);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.lastWhichIndex = lastWhichIndex;
        return savedState;
    }

    public static class SavedState extends BaseSavedState {
        int lastWhichIndex;

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.lastWhichIndex);
        }

        @Override
        public String toString() {
            return "ViewAnimator.SavedState{" +
                    "lastWhichIndex=" + lastWhichIndex +
                    '}';
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        protected SavedState(Parcel in) {
            super(in);
            this.lastWhichIndex = in.readInt();
        }
    }
}
