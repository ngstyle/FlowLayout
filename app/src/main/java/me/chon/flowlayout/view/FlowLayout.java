package me.chon.flowlayout.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.chon.flowlayout.R;


/**
 * Created by chon.
 * Date:2015/12/16 10:24
 */
public class FlowLayout extends ViewGroup {
    /**
     * 是否分散对齐
     */
    boolean isDistributed;
    boolean lastLineFill;

    public static final int LEFT = 0;
    public static final int CENTER = 1;
    public static final int RIGHT = 2;

    /**
     * 对齐方式（左中右，默认居左），只有当取消分散对齐时有效
     * 即isDistributed 为false时有效
     */
    int alignmentType;
    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        alignmentType = a.getInt(R.styleable.FlowLayout_alignmentType,LEFT);
        isDistributed = a.getBoolean(R.styleable.FlowLayout_distributed,true);
        lastLineFill = a.getBoolean(R.styleable.FlowLayout_lastLineFill,true);
        a.recycle();
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        // wrap_content 处理
        int width = 0,height = 0;
        int lineWidth = 0,lineHeight = 0;

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            // 支持padding
            if (lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight()){
                width = Math.max(lineWidth,width);
                height += lineHeight;

                lineWidth = childWidth;
                lineHeight = childHeight;
            }else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight,childHeight);
            }

            if (i == childCount -1){
                width = Math.max(lineWidth,childWidth);
                height += lineHeight;
            }
        }

        setMeasuredDimension(
                modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width + getPaddingLeft() + getPaddingRight(),
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height + getPaddingTop() + getPaddingBottom()
        );

    }

    /**
     * 所有子view的集合
     */
    private List<List<View>> mAllViews = new ArrayList<>();

    /**
     * 每一行的高度集合
     */
    private List<Integer> mLineHeight = new ArrayList<>();
    private List<Integer> mLineWidth = new ArrayList<>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Toast.makeText(getContext(),"onLayout",Toast.LENGTH_SHORT).show();

        mAllViews.clear();
        mLineHeight.clear();
        mLineWidth.clear();

        int lineWidth = 0, lineHeight = 0;
        List<View> lineViews = new ArrayList<>();

        // 控件本身的宽度
        int width = getWidth();

        // 遍历所有子控件，填充好 mAllViews 和 mLineHeight
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if (lineWidth + childWidth > width - getPaddingLeft() - getPaddingRight()){
                mAllViews.add(lineViews);
                mLineHeight.add(lineHeight);
                mLineWidth.add(lineWidth);

                lineWidth = childWidth;
                lineHeight = childHeight;
                lineViews = new ArrayList<>();
            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight,childHeight);
            }

            lineViews.add(child);
        }
        mAllViews.add(lineViews);
        mLineHeight.add(lineHeight);
        mLineWidth.add(lineWidth);

        // 初始左上位置
        int left;
        int top = getPaddingTop();

        for (int i = 0; i < mAllViews.size(); i++) {
            lineViews = mAllViews.get(i);
            lineHeight = mLineHeight.get(i);
            lineWidth = mLineWidth.get(i);

            int availableLineWidth = 0;
            left = getPaddingLeft();                            // 左对齐
            if (isDistributed){
                availableLineWidth = width - lineWidth - getPaddingLeft() - getPaddingRight();
            } else if (alignmentType == RIGHT){
                left = width - lineWidth - getPaddingRight();     // 右对齐
            } else if (alignmentType == CENTER) {
                left = (width - lineWidth)/2;                     // 居中
            }

            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);
                if (child.getVisibility() == View.GONE){
                    continue;
                }

                if (isDistributed && !(i == mAllViews.size() - 1 && !lastLineFill)) {
                    child.setPadding(child.getPaddingLeft() + availableLineWidth / lineViews.size() / 2,
                            child.getPaddingTop(),
                            child.getPaddingRight() + availableLineWidth / lineViews.size() / 2,
                            child.getPaddingBottom());
                }

                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc;
                if (isDistributed) {
                    rc = lc + child.getMeasuredWidth() + availableLineWidth/lineViews.size();

                    if (i == mAllViews.size() - 1 && !lastLineFill){
                        rc = lc + child.getMeasuredWidth();
                    }

                }else {
                    rc = lc + child.getMeasuredWidth();
                }
                int bc = tc + child.getMeasuredHeight();

                child.layout(lc,tc,rc,bc);

                if (isDistributed){
                    if (i == mAllViews.size() - 1 && !lastLineFill){
                        left += lp.leftMargin + child.getMeasuredWidth() + lp.rightMargin;
                    } else {
                        left += lp.leftMargin + child.getMeasuredWidth() + lp.rightMargin + availableLineWidth/lineViews.size();
                    }
                } else {
                    left += lp.leftMargin + child.getMeasuredWidth() + lp.rightMargin;
                }
            }

            top += lineHeight;
        }

    }


    /**
     * 设置对齐方式，只有当不是分散对齐时有效
     * <br>如果需要强制设置非分散对齐，调用 setUnDistributedAndAlignmentType()
     * @param alignmentType
     */
    public void setAlignmentType(int alignmentType) {
        if (isDistributed || this.alignmentType == alignmentType) {
            return;
        }
        this.alignmentType = alignmentType;
        requestLayout();
    }

    /**
     * 设置是否分散对齐
     */
    public void setDistributedAndLastLineFill() {
        this.isDistributed = true;
        alignmentType = LEFT;
        lastLineFill = true;
        requestLayout();
    }

    /**
     * 设置分散对齐且最后行不填充
     */
    public void setDistributedAndLastLineNotFill() {
        this.isDistributed = true;
        alignmentType = LEFT;
        lastLineFill = false;
        requestLayout();
    }


    /**
     * 强制设置非分散对齐，且按照传入的对齐方式
     * @param alignmentType
     */
    public void setUnDistributedAndAlignmentType(int alignmentType) {
        isDistributed = false;
        this.alignmentType = alignmentType;
        requestLayout();
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

}
