/**
 * [SIMINOV FRAMEWORK]
 * Copyright [2014-2016] [Siminov Software Solution LLP|support@siminov.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/

package siminov.connect.sample.artist;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;

/**
 * A HorizontalScrollView (HSV) implementation that disallows touch events (so no scrolling can be done by the user).
 * 
 * This HSV MUST contain a single ViewGroup as its only child, and this ViewGroup will be used to display the children Views
 * passed in to the initViews() method.
 */
public class SliderScroll extends HorizontalScrollView {
    
	private ViewGroup parent = null;
	
	public SliderScroll(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public SliderScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SliderScroll(Context context) {
        super(context);
        init(context);
    }

    void init(Context context) {
        // remove the fading as the HSV looks better without it
        setHorizontalFadingEdgeEnabled(false);
        setVerticalFadingEdgeEnabled(false);
    }

    /**
     * @param children
     *            The child Views to add to parent.
     * @param scrollToViewIdx
     *            The index of the View to scroll to after initialisation.
     * @param sizeCallback
     *            A SizeCallback to interact with the HSV.
     */
    public void initViews(View[] children, int scrollToViewIdx, SizeCallback sizeCallback) {
        // A ViewGroup MUST be the only child of the HSV
        parent = (ViewGroup) getChildAt(0);

        // Add all the children, but add them invisible so that the layouts are calculated, but you can't see the Views
        for (int i = 0; i < children.length; i++) {
            children[i].setVisibility(View.INVISIBLE);
            parent.addView(children[i]);
        }

        // Add a layout listener to this HSV
        // This listener is responsible for arranging the child views.
        OnGlobalLayoutListener listener = new MyOnGlobalLayoutListener(parent, children, scrollToViewIdx, sizeCallback);
        getViewTreeObserver().addOnGlobalLayoutListener(listener);
    }

    public ViewGroup getParentView() {
    	return this.parent;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Do not allow touch events.
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Do not allow touch events.
        return false;
    }

    /**
     * An OnGlobalLayoutListener impl that passes on the call to onGlobalLayout to a SizeCallback, before removing all the Views
     * in the HSV and adding them again with calculated widths and heights.
     */
    class MyOnGlobalLayoutListener implements OnGlobalLayoutListener {
        ViewGroup parent;
        View[] children;
        int scrollToViewIdx;
        int scrollToViewPos = 0;
        SizeCallback sizeCallback;

        /**
         * @param parent
         *            The parent to which the child Views should be added.
         * @param children
         *            The child Views to add to parent.
         * @param scrollToViewIdx
         *            The index of the View to scroll to after initialisation.
         * @param sizeCallback
         *            A SizeCallback to interact with the HSV.
         */
        public MyOnGlobalLayoutListener(ViewGroup parent, View[] children, int scrollToViewIdx, SizeCallback sizeCallback) {
            this.parent = parent;
            this.children = children;
            this.scrollToViewIdx = scrollToViewIdx;
            this.sizeCallback = sizeCallback;
        }

        public void onGlobalLayout() {

            final HorizontalScrollView me = SliderScroll.this;

            // The listener will remove itself as a layout listener to the HSV
            me.getViewTreeObserver().removeGlobalOnLayoutListener(this);

            // Allow the SizeCallback to 'see' the Views before we remove them and re-add them.
            // This lets the SizeCallback prepare View sizes, ahead of calls to SizeCallback.getViewSize().
            sizeCallback.onGlobalLayout();

            parent.removeViewsInLayout(0, children.length);

            final int w = me.getMeasuredWidth();
            final int h = me.getMeasuredHeight();

            // Add each view in turn, and apply the width and height returned by the SizeCallback.
            int[] dims = new int[2];
            scrollToViewPos = 0;
            for (int i = 0; i < children.length; i++) {
                sizeCallback.getViewSize(i, w, h, dims);
                children[i].setVisibility(View.VISIBLE);
                parent.addView(children[i], dims[0], dims[1]);
                if (i < scrollToViewIdx) {
                    scrollToViewPos += dims[0];
                }
            }

            // For some reason we need to post this action, rather than call immediately.
            // If we try immediately, it will not scroll.
            new Handler().post(new Runnable() {
                public void run() {
                    me.scrollBy(scrollToViewPos, 0);
                }
            });
        }
    }

    /**
     * Callback interface to interact with the HSV.
     */
    public interface SizeCallback {
        /**
         * Used to allow clients to measure Views before re-adding them.
         */
        public void onGlobalLayout();

        /**
         * Used by clients to specify the View dimensions.
         * 
         * @param idx
         *            Index of the View.
         * @param w
         *            Width of the parent View.
         * @param h
         *            Height of the parent View.
         * @param dims
         *            dims[0] should be set to View width. dims[1] should be set to View height.
         */
        public void getViewSize(int idx, int w, int h, int[] dims);
    }
    
    
    /**
     * Helper for examples with a HSV that should be scrolled by a menu View's width.
     */
    public static class ClickListenerForScrolling implements OnClickListener {
        HorizontalScrollView scrollView;
        View menu;
        /**
         * Menu must NOT be out/shown to start with.
         */
        boolean menuOut = false;

        public ClickListenerForScrolling(HorizontalScrollView scrollView, View menu) {
            super();
            this.scrollView = scrollView;
            this.menu = menu;
        }

        public void onClick(View v) {
            int menuWidth = menu.getMeasuredWidth();

            // Ensure menu is visible
            menu.setVisibility(View.VISIBLE);

            if (!menuOut) {
                // Scroll to 0 to reveal menu
                int left = 0;
                scrollView.smoothScrollTo(left, 0);
            } else {
                // Scroll to menuWidth so menu isn't on screen.
                int left = menuWidth;
                scrollView.smoothScrollTo(left, 0);
            }
            menuOut = !menuOut;
        }
    }

    /**
     * Helper that remembers the width of the 'slide' button, so that the 'slide' button remains in view, even when the menu is
     * showing.
     */
    public static class SizeCallbackForMenu implements SizeCallback {
        int btnWidth;
        View btnSlide;

        public SizeCallbackForMenu(View btnSlide) {
            super();
            this.btnSlide = btnSlide;
        }

        public void onGlobalLayout() {
            btnWidth = 48;
        }

        public void getViewSize(int idx, int w, int h, int[] dims) {
            dims[0] = w;
            dims[1] = h;
            final int menuIdx = 0;
            if (idx == menuIdx) {
                dims[0] = w - btnWidth;
            }
        }
    }
}
