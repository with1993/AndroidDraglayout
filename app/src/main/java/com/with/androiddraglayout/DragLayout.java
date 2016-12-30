package com.with.androiddraglayout;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.nineoldandroids.view.ViewHelper;

public class DragLayout extends FrameLayout {
	private View redView;// �캢��
	private View blueView;// ������

	private ViewDragHelper viewDragHelper;

	// ���ɸ���Ĺ��췽��:alt+shift+s->c
	public DragLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public DragLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DragLayout(Context context) {
		super(context);
		init();
	}

	private void init() {
		viewDragHelper = ViewDragHelper.create(this, callback);
	}

	/**
	 * ��DragLayout��xml���ֵĽ�����ǩ����ȡ��ɻ�ִ�и÷�������ʱ��֪���Լ��м�����View�� һ��������ʼ����View������
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		redView = getChildAt(0);
		blueView = getChildAt(1);
	}

	// @Override
	// protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	// //Ҫ�������Լ�����View
	// // int size = getResources().getDimension(R.dimen.width);//100dp
	// // int measureSpec =
	// MeasureSpec.makeMeasureSpec(redView.getLayoutParams().width,MeasureSpec.EXACTLY);
	// // redView.measure(measureSpec,measureSpec);
	// // blueView.measure(measureSpec, measureSpec);
	//
	// //���˵û������Ķ���View�Ĳ������󣬿��������·���
	// measureChild(redView, widthMeasureSpec, heightMeasureSpec);
	// measureChild(blueView, widthMeasureSpec, heightMeasureSpec);
	// }

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int left = getPaddingLeft();
		int top = getPaddingTop();
		redView.layout(left, top, left + redView.getMeasuredWidth(), top
				+ redView.getMeasuredHeight());
		blueView.layout(left, redView.getBottom(),
				left + blueView.getMeasuredWidth(), redView.getBottom()
						+ blueView.getMeasuredHeight());
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// ��ViewDragHelper�������ж��Ƿ�Ӧ������
		boolean result = viewDragHelper.shouldInterceptTouchEvent(ev);
		return result;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// �������¼�����ViewDragHelper����������
		viewDragHelper.processTouchEvent(event);
		return true;
	}

	private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
		/**
		 * �����ж��Ƿ񲶻�ǰchild�Ĵ����¼� child: ��ǰ��������View return: true:�Ͳ��񲢽��� false��������
		 */
		@Override
		public boolean tryCaptureView(View child, int pointerId) {
			return child == blueView || child == redView;
		}

		/**
		 * ��view����ʼ����ͽ����Ļص� capturedChild:��ǰ���������view
		 */
		@Override
		public void onViewCaptured(View capturedChild, int activePointerId) {
			super.onViewCaptured(capturedChild, activePointerId);
			// Log.e("tag", "onViewCaptured");
		}

		/**
		 * ��ȡviewˮƽ�������ק��Χ,����Ŀǰ�������Ʊ߽�,���ص�ֵĿǰ������ָ̧���ʱ��view�����ƶ��Ķ�������ļ�������; ��ò�Ҫ����0
		 */
		@Override
		public int getViewHorizontalDragRange(View child) {
			return getMeasuredWidth() - child.getMeasuredWidth();
		}

		/**
		 * ��ȡview��ֱ�������ק��Χ����ò�Ҫ����0
		 */
		public int getViewVerticalDragRange(View child) {
			return getMeasuredHeight() - child.getMeasuredHeight();
		};

		/**
		 * ����child��ˮƽ������ƶ� left:
		 * ��ʾViewDragHelper��Ϊ�����õ�ǰchild��left�ı��ֵ,left=chile.getLeft()+dx dx:
		 * ����childˮƽ�����ƶ��ľ��� return: ��ʾ����������child��left��ɵ�ֵ
		 */
		@Override
		public int clampViewPositionHorizontal(View child, int left, int dx) {
			if (left < 0) {
				// ������߽�
				left = 0;
			} else if (left > (getMeasuredWidth() - child.getMeasuredWidth())) {
				// �����ұ߽�
				left = getMeasuredWidth() - child.getMeasuredWidth();
			}
			return left;
		}

		/**
		 * ����child�ڴ�ֱ������ƶ� top:
		 * ��ʾViewDragHelper��Ϊ�����õ�ǰchild��top�ı��ֵ,top=chile.getTop()+dy dy:
		 * ����child��ֱ�����ƶ��ľ��� return: ��ʾ����������child��top��ɵ�ֵ
		 */
		public int clampViewPositionVertical(View child, int top, int dy) {
			if (top < 0) {
				top = 0;
			} else if (top > getMeasuredHeight() - child.getMeasuredHeight()) {
				top = getMeasuredHeight() - child.getMeasuredHeight();
			}
			return top;
		};

		/**
		 * ��child��λ�øı��ʱ��ִ��,һ��������������View�İ����ƶ� changedView��λ�øı��child
		 * left��child��ǰ���µ�left top: child��ǰ���µ�top dx: ����ˮƽ�ƶ��ľ��� dy: ���δ�ֱ�ƶ��ľ���
		 */
		@Override
		public void onViewPositionChanged(View changedView, int left, int top,
				int dx, int dy) {
			super.onViewPositionChanged(changedView, left, top, dx, dy);
			if (changedView == blueView) {
				// blueView�ƶ���ʱ����Ҫ��redView�����ƶ�
				redView.layout(redView.getLeft() + dx, redView.getTop() + dy,
						redView.getRight() + dx, redView.getBottom() + dy);
			} else if (changedView == redView) {
				// redView�ƶ���ʱ����Ҫ��blueView�����ƶ�
				blueView.layout(blueView.getLeft() + dx,
						blueView.getTop() + dy, blueView.getRight() + dx,
						blueView.getBottom() + dy);
			}
			
			//1.����view�ƶ��İٷֱ�
			float fraction = changedView.getLeft()*1f/(getMeasuredWidth()-changedView.getMeasuredWidth());
			Log.e("tag", "fraction:"+fraction);
			//2.ִ��һϵ�еİ��涯��
			executeAnim(fraction);
		}

		/**
		 * ��ָ̧���ִ�и÷����� releasedChild����ǰ̧���view xvel: x������ƶ����ٶ� ���������ƶ��� ���������ƶ�
		 * yvel: y�����ƶ����ٶ�
		 */
		@Override
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			super.onViewReleased(releasedChild, xvel, yvel);
			int centerLeft = getMeasuredWidth() / 2
					- releasedChild.getMeasuredWidth() / 2;
			if (releasedChild.getLeft() < centerLeft) {
				// �����ߣ�Ӧ���������ƶ�
				viewDragHelper.smoothSlideViewTo(releasedChild, 0,
						releasedChild.getTop());
				ViewCompat.postInvalidateOnAnimation(DragLayout.this);
			} else {
				// ���Ұ�ߣ�Ӧ�����һ����ƶ�
				viewDragHelper.smoothSlideViewTo(releasedChild,
						getMeasuredWidth() - releasedChild.getMeasuredWidth(),
						releasedChild.getTop());
				ViewCompat.postInvalidateOnAnimation(DragLayout.this);
			}
		}
	};
	/**
	 * ִ�а��涯��
	 * @param fraction
	 */
	private void executeAnim(float fraction){
		//fraction: 0 - 1
		//����
//		ViewHelper.setScaleX(redView, 1+0.5f*fraction);
//		ViewHelper.setScaleY(redView, 1+0.5f*fraction);
		//��ת
		ViewHelper.setRotation(redView,720*fraction);//Χ��z��ת
		//ViewHelper.setRotationX(redView,360*fraction);//Χ��x��ת
//		ViewHelper.setRotationY(redView,360*fraction);//Χ��y��ת
		ViewHelper.setRotation(blueView,720*fraction);//Χ��z��ת
		//ƽ��
//		ViewHelper.setTranslationX(redView,80*fraction);
		//͸��
//		ViewHelper.setAlpha(redView, 1-fraction);
		
		//���ù�����ɫ�Ľ���
		//redView.setBackgroundColor((Integer) ColorUtil.evaluateColor(fraction,Color.RED,Color.GREEN));
//		setBackgroundColor((Integer) ColorUtil.evaluateColor(fraction,Color.RED,Color.GREEN));
	}

	public void computeScroll() {
		if (viewDragHelper.continueSettling(true)) {
			ViewCompat.postInvalidateOnAnimation(DragLayout.this);
		}
	};
}
