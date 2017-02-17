package nah.prayer.nahslidemenudrawer;

/**
 * Created by Nah on 2017-02-17.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class NahSlideMunu extends FrameLayout {
    private final Window mWindow;
    private final ViewGroup mAboveView;
    private final NahSlideMunu.BehindLinearLayout mBehindView;
    private final LinearLayout mLeftBehindBase;
    private final LinearLayout mRightBehindBase;
    private final View mOverlay;
    private Scroller mScroller;
    private View mLeftBehindView;
    private View mRightBehindView;
    private Rect mLeftPaddingRect;
    private Rect mRightPaddingRect;
    private int mDurationLeft;
    private int mDurationRight;
    private int mLeftBehindViewWidth;
    private int mRightBehindViewWidth;
    private NahSlideMunu.DragAction mLeftDragAction;
    private NahSlideMunu.DragAction mRightDragAction;

    private Activity this_act;


    public NahSlideMunu(Activity act) {
        this(act, new AccelerateDecelerateInterpolator(), 180);
        this_act = act;
    }

    private OnCloseListner listner;
    public void setOnCloseListner(OnCloseListner listner){
        this.listner = listner;
    }
    public interface OnCloseListner {
        void onClose();
        void onView();
    }
    private boolean bool = false;

    public NahSlideMunu(Activity act, Interpolator ip, int duration) {
        super(act.getApplicationContext());
        this.mLeftDragAction = new NahSlideMunu.DragAction() {
            public boolean onTouchEvent(MotionEvent ev) {
                int action = ev.getAction() & 255;
                float newX;
                switch(action) {
                    case 0:
                        newX = ev.getX();
                        NahSlideMunu.this.mLeftDragAction.mLastMotionX = newX;
                        NahSlideMunu.this.mLeftDragAction.mDraggable = NahSlideMunu.this.mAboveView.getScrollX() != 0;
                        break;
                    case 1:
                        if(NahSlideMunu.this.mLeftDragAction.mDraggable) {
                            int newX1 = NahSlideMunu.this.mAboveView.getScrollX();
                            boolean diffX1 = false;
                            int diffX2;
                            if(NahSlideMunu.this.mLeftDragAction.mOpening) {
                                diffX2 = -(NahSlideMunu.this.mLeftBehindViewWidth + newX1);
                                bool = false;
                            } else {
                                diffX2 = -newX1;
                                bool = true;
                            }

                            NahSlideMunu.this.mScroller.startScroll(newX1, 0, diffX2, 0, NahSlideMunu.this.mDurationLeft);
                            NahSlideMunu.this.invalidate();
                            if(listner != null && bool)
                                listner.onClose();
                        }
                        break;
                    case 2:
                        if(!NahSlideMunu.this.mLeftDragAction.mDraggable) {
                            return false;
                        }

                        newX = ev.getX();
                        float diffX = -(newX - NahSlideMunu.this.mLeftDragAction.mLastMotionX);
                        int x = NahSlideMunu.this.mAboveView.getScrollX();
                        NahSlideMunu.this.mLeftDragAction.mOpening = NahSlideMunu.this.mLeftDragAction.mLastMotionX < newX;
                        NahSlideMunu.this.mLeftDragAction.mLastMotionX = newX;
                        float nextX = (float)x + diffX;
                        if(0.0F < nextX) {
                            NahSlideMunu.this.mAboveView.scrollTo(0, 0);
                        } else if(nextX < (float)(-NahSlideMunu.this.mLeftBehindViewWidth)) {
                            NahSlideMunu.this.mAboveView.scrollTo(-NahSlideMunu.this.mLeftBehindViewWidth, 0);
                        } else {
                            NahSlideMunu.this.mAboveView.scrollBy((int)diffX, 0);
                        }
                }

                return false;
            }
        };
        this.mRightDragAction = new NahSlideMunu.DragAction() {
            public boolean onTouchEvent(MotionEvent ev) {
                int action = ev.getAction() & 255;
                float newX;
                switch(action) {
                    case 0:
                        newX = ev.getX();
                        NahSlideMunu.this.mRightDragAction.mLastMotionX = newX;
                        NahSlideMunu.this.mRightDragAction.mDraggable = NahSlideMunu.this.mAboveView.getScrollX() != 0;
                        break;
                    case 1:
                        if(NahSlideMunu.this.mRightDragAction.mDraggable) {
                            int newX1 = NahSlideMunu.this.mAboveView.getScrollX();
                            boolean diffX1 = false;
                            int diffX2;
                            if(NahSlideMunu.this.mRightDragAction.mOpening) {
                                diffX2 = NahSlideMunu.this.mRightBehindViewWidth - newX1;
                            } else {
                                diffX2 = -newX1;
                            }

                            NahSlideMunu.this.mScroller.startScroll(newX1, 0, diffX2, 0, NahSlideMunu.this.mDurationRight);
                            NahSlideMunu.this.invalidate();
                        }
                        break;
                    case 2:
                        if(!NahSlideMunu.this.mRightDragAction.mDraggable) {
                            return false;
                        }

                        newX = ev.getX();
                        float diffX = -(newX - NahSlideMunu.this.mRightDragAction.mLastMotionX);
                        int x = NahSlideMunu.this.mAboveView.getScrollX();
                        System.out.format("[SLIDE] ACTION_MOVE(r): %f, %d\n", new Object[]{Float.valueOf(newX), Integer.valueOf(x)});
                        mRightDragAction.mOpening = newX < NahSlideMunu.this.mRightDragAction.mLastMotionX;
                        mRightDragAction.mLastMotionX = newX;
                        float nextX = (float)x + diffX;
                        if(nextX < 0.0F) {
                            mAboveView.scrollTo(0, 0);
                        } else if(nextX < (float) NahSlideMunu.this.mRightBehindViewWidth) {
                            mAboveView.scrollBy((int)diffX, 0);
                        } else {
                            mAboveView.scrollTo(NahSlideMunu.this.mRightBehindViewWidth, 0);
                        }
                }

                return false;
            }
        };



        Context context = act.getApplicationContext();
        this.mDurationLeft = duration;
        this.mDurationRight = duration;
        this.mWindow = act.getWindow();
        this.mScroller = new Scroller(context, ip);
        boolean fp = true;
        boolean wp = true;
        this.mBehindView = new NahSlideMunu.BehindLinearLayout(context);
        this.mBehindView.setLayoutParams(new LayoutParams(-1, -1));
        this.mBehindView.setOrientation(LinearLayout.HORIZONTAL);
        this.mLeftBehindBase = new NahSlideMunu.BehindLinearLayout(context);
        this.mBehindView.addView(this.mLeftBehindBase, new LayoutParams(-2, -1));
        this.mBehindView.addView(new View(context), new LayoutParams(0, -1, 1));
        this.mRightBehindBase = new NahSlideMunu.BehindLinearLayout(context);
        this.mBehindView.addView(this.mRightBehindBase, new LayoutParams(-2, -1));
        this.addView(this.mBehindView);
        this.mAboveView = new FrameLayout(context);
        this.mAboveView.setLayoutParams(new LayoutParams(-1, -1));
        this.mOverlay = new NahSlideMunu.OverlayView(this.getContext());
        this.mOverlay.setLayoutParams(new LayoutParams(-1, -1, 80));
        this.mOverlay.setEnabled(true);
        this.mOverlay.setVisibility(View.GONE);
        this.mOverlay.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if(NahSlideMunu.this.mLeftBehindBase.getVisibility() != View.GONE) {
                    NahSlideMunu.this.closeLeftSide();
                } else if(NahSlideMunu.this.mRightBehindBase.getVisibility() != View.GONE) {
                    NahSlideMunu.this.closeRightSide();
                }

            }
        });
        ViewGroup decor = (ViewGroup)this.mWindow.getDecorView();
        ViewGroup above = (ViewGroup)decor.getChildAt(0);
        decor.removeView(above);
        above.setBackground(decor.getBackground());
        //above.setBackgroundDrawable(decor.getBackground());
        this.mAboveView.addView(above);
        this.mAboveView.addView(this.mOverlay);
        decor.addView(this);
        this.addView(this.mAboveView);
    }

    public View getLeftBehindView() {
        return this.mLeftBehindBase.getChildAt(0);
    }

    public View getRightBehindView() {
        return this.mRightBehindBase.getChildAt(0);
    }

    /** @deprecated */
    public View setBehindContentView(int leftBehindLayout) {
        return this.setLeftBehindContentView(leftBehindLayout);
    }

    public View setLeftBehindContentView(int leftBehindLayout) {
        View content = ((LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(leftBehindLayout, this.mLeftBehindBase);
        this.mLeftPaddingRect = new Rect(content.getPaddingLeft(), content.getPaddingTop(), content.getPaddingRight(), content.getPaddingBottom());
        this.mLeftBehindView = content;
        return content;
    }

    public View setRightBehindContentView(int rightBehindLayout) {
        View content = ((LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(rightBehindLayout, this.mRightBehindBase);
        this.mRightPaddingRect = new Rect(content.getPaddingLeft(), content.getPaddingTop(), content.getPaddingRight(), content.getPaddingBottom());
        this.mRightBehindView = content;
        return content;
    }

    public void setScrollInterpolator(Interpolator ip) {
        this.mScroller = new Scroller(this.getContext(), ip);
    }

    /** @deprecated */
    public void setAnimationDuration(int msec) {
        this.setAnimationDurationLeft(msec);
    }

    public void setAnimationDurationLeft(int msec) {
        this.mDurationLeft = msec;
    }

    public void setAnimationDurationRight(int msec) {
        this.mDurationRight = msec;
    }

    /** @deprecated */
    public void close() {
        this.closeLeftSide();
    }

    public void closeLeftSide() {
        int curX = -this.mLeftBehindViewWidth;
        this.mScroller.startScroll(curX, 0, -curX, 0, this.mDurationLeft);
        if(listner != null)
            listner.onClose();
        this.invalidate();
    }

    public void closeRightSide() {
        int curX = this.mRightBehindViewWidth;
        this.mScroller.startScroll(curX, 0, -curX, 0, this.mDurationRight);
        this.invalidate();
    }

    /** @deprecated */
    public void open() {
        this.openLeftSide();
    }

    public void openLeftSide() {
        System.out.format("[SLIDE] openLeftSide: %d\n", new Object[]{Integer.valueOf(this.mLeftBehindViewWidth)});
        this.mLeftBehindBase.setVisibility(View.VISIBLE);
        this.mRightBehindBase.setVisibility(View.GONE);
        int curX = this.mAboveView.getScrollX();
        this.mScroller.startScroll(curX, 0, -this.mLeftBehindViewWidth, 0, this.mDurationLeft);
        this.invalidate();
        if(listner != null)
            listner.onView();
    }

    public void openRightSide() {
        System.out.format("[SLIDE] openRightSide: %d\n", new Object[]{Integer.valueOf(this.mRightBehindViewWidth)});
        this.mRightBehindBase.setVisibility(View.VISIBLE);
        this.mLeftBehindBase.setVisibility(View.GONE);
        int curX = this.mAboveView.getScrollX();
        this.mScroller.startScroll(curX, 0, this.mRightBehindViewWidth, 0, this.mDurationRight);
        this.invalidate();
        if(listner != null)
            listner.onView();
    }

    /** @deprecated */
    public void toggleDrawer() {
        this.toggleLeftDrawer();
    }

    public void toggleLeftDrawer() {
        if(this.isClosed()) {
            System.out.println("[SLIDE] toggleLeft: open");
            this.openLeftSide();
        } else {
            System.out.println("[SLIDE] toggleLeft: close");
            this.closeLeftSide();
        }

    }

    public void toggleRightDrawer() {
        if(this.isClosed()) {
            System.out.println("[SLIDE] toggleRight: open");
            this.openRightSide();
        } else {
            System.out.println("[SLIDE] toggleRight: close");
            this.closeRightSide();
        }

    }

    public boolean isClosed() {
        return this.mAboveView != null && this.mAboveView.getScrollX() == 0;
    }

    private boolean isLeftSideOpened() {
        return this.mLeftBehindBase.getVisibility() == View.VISIBLE && this.mRightBehindBase.getVisibility() == View.GONE;
    }

    private boolean isRightSideOpened() {
        return this.mRightBehindBase.getVisibility() == View.VISIBLE && this.mLeftBehindBase.getVisibility() == View.GONE;
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.mLeftBehindViewWidth = this.mLeftBehindBase.getMeasuredWidth();
        this.mRightBehindViewWidth = this.mRightBehindBase.getMeasuredWidth();
        System.out.format("[SLIDE] onMeasure: %d, %d\n", new Object[]{Integer.valueOf(this.mLeftBehindViewWidth), Integer.valueOf(this.mRightBehindViewWidth)});
        ViewGroup decor = (ViewGroup)this.mWindow.getDecorView();
        Rect rect = new Rect();
        decor.getWindowVisibleDisplayFrame(rect);
        this.mBehindView.fitDisplay(rect);
    }

    public void computeScroll() {
        if(this.mScroller.computeScrollOffset()) {
            this.mAboveView.scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
            this.invalidate();
        } else if(this.mAboveView.getScrollX() == 0) {
            this.mOverlay.setVisibility(View.GONE);
        } else {
            this.mOverlay.setVisibility(View.VISIBLE);
        }

    }

    public boolean onTouchEvent(MotionEvent ev) {
        return this.isLeftSideOpened()?this.mLeftDragAction.onTouchEvent(ev):(this.isRightSideOpened()?this.mRightDragAction.onTouchEvent(ev):true);
    }

    private class BehindLinearLayout extends LinearLayout {
        public BehindLinearLayout(Context context) {
            super(context);
        }

        public void fitDisplay(Rect rect) {
            NahSlideMunu.this.mBehindView.setPadding(rect.left, rect.top, 0, 0);
            this.requestLayout();
        }
    }

    private abstract class DragAction {
        private float mLastMotionX;
        private boolean mOpening;
        private boolean mDraggable;

        private DragAction() {
            this.mLastMotionX = 0.0F;
            this.mOpening = false;
            this.mDraggable = false;
        }

        public abstract boolean onTouchEvent(MotionEvent var1);
    }

    private class OverlayView extends View {
        private static final float CLICK_RANGE = 3.0F;
        private float mDownX;
        private float mDownY;
        private OnClickListener mClickListener;

        public OverlayView(Context context) {
            super(context);
        }

        public void setOnClickListener(OnClickListener listener) {
            this.mClickListener = listener;
            super.setOnClickListener(listener);
        }

        public boolean onTouchEvent(MotionEvent ev) {
            ev.setLocation(ev.getX() - (float) NahSlideMunu.this.mAboveView.getScrollX(), 0.0F);
            NahSlideMunu.this.onTouchEvent(ev);
            int action = ev.getAction() & 255;
            float x = ev.getX();
            float y = ev.getY();
            if(action == 0) {
                this.mDownX = x;
                this.mDownY = y;
            } else if(action == 1 && this.mClickListener != null && Math.abs(this.mDownX - x) < 3.0F && Math.abs(this.mDownY - y) < 3.0F) {
                this.mClickListener.onClick(this);
            }

            return true;
        }
    }

}
