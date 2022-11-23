package com.gitonway.lee.niftymodaldialogeffects.lib;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import com.gitonway.lee.niftymodaldialogeffects.lib.effects.BaseEffects;
import com.gitonway.lee.niftymodaldialogeffects.lib.pdfview.PDFView;
import com.gitonway.lee.niftymodaldialogeffects.lib.pdfview.listener.OnDrawListener;
import com.gitonway.lee.niftymodaldialogeffects.lib.pdfview.listener.OnLoadCompleteListener;
import com.gitonway.lee.niftymodaldialogeffects.lib.pdfview.listener.OnPageChangeListener;

import java.io.File;

import es.voghdev.pdfviewpager.library.PDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.BasePDFPagerAdapter;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;

/**
 * 自定义dialog
 */
public class NiftyDialogBuilder extends Dialog implements DialogInterface, OnPageChangeListener
        , OnLoadCompleteListener, OnDrawListener {

    private final String defTextColor = "#FFFFFFFF";//字体颜色

    private final String defDividerColor = "#11000000";//线条颜色

    private final String defMsgColor = "#FFFFFFFF";//内容字体颜色

    private final String defDialogColor = "#FFE74C3C";//dialog的背景色

    private static Context tmpContext;//上下文

    private Effectstype type = null;//各个窗口的类型

    private LinearLayout mLinearLayoutView;

    private RelativeLayout mRelativeLayoutView;

    private LinearLayout mLinearLayoutMsgView;

    private LinearLayout mLinearLayoutTopView;

    private FrameLayout mFrameLayoutCustomView;

    private View mDialogView;

    private View mDivider;

    private TextView mTitle;

    private TextView mMessage;

    private ImageView mIcon;

    private Button mButton1;//两个按钮

    private Button mButton2;

    private int mDuration = -1;

    private static int mOrientation = 1;

    private boolean isCancelable = true;//是否可取消

     PDFView pdfView;

    private static NiftyDialogBuilder instance;

    public NiftyDialogBuilder(Context context) {
        super(context);
        init(context);

    }

    /**
     * 构造器
     * @param context
     * @param theme
     */
    public NiftyDialogBuilder(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams params = getWindow().getAttributes();//获取屏幕的宽高并设置
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    public static NiftyDialogBuilder getInstance(Context context) {//单例

        if (instance == null || !tmpContext.equals(context)) {
            synchronized (NiftyDialogBuilder.class) {
                if (instance == null || !tmpContext.equals(context)) {
                    instance = new NiftyDialogBuilder(context, R.style.dialog_untran);
                }
            }
        }
        tmpContext = context;
        return instance;

    }
    /**
     * 初始化控件
     * @param context
     */
    @SuppressLint("NewApi")
    private void init(Context context) {

        mDialogView = View.inflate(context, R.layout.dialog_layout, null);
        pdfView = (PDFView) mDialogView.findViewById(R.id.pdfView);
        pdfView.setScaleX(2.5f);
        pdfView.setScaleY(2.5f);
        mLinearLayoutView = (LinearLayout) mDialogView.findViewById(R.id.parentPanel);
        mRelativeLayoutView = (RelativeLayout) mDialogView.findViewById(R.id.main);
        mLinearLayoutTopView = (LinearLayout) mDialogView.findViewById(R.id.topPanel);
        mLinearLayoutMsgView = (LinearLayout) mDialogView.findViewById(R.id.contentPanel);
        mFrameLayoutCustomView = (FrameLayout) mDialogView.findViewById(R.id.customPanel);

        mTitle = (TextView) mDialogView.findViewById(R.id.alertTitle);
        mMessage = (TextView) mDialogView.findViewById(R.id.message);
        mIcon = (ImageView) mDialogView.findViewById(R.id.icon);
        mDivider = mDialogView.findViewById(R.id.titleDivider);
        mButton1 = (Button) mDialogView.findViewById(R.id.button1);
        mButton2 = (Button) mDialogView.findViewById(R.id.button2);

        setContentView(mDialogView);

        this.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                mLinearLayoutView.setVisibility(View.VISIBLE);
                if (type == null) {
                    type = Effectstype.Slidetop;
                }
                start(type);


            }
        });
        mRelativeLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCancelable) dismiss();
            }
        });
    }

    /**
     * 设置控件
     */
    public void toDefault() {
        mTitle.setTextColor(Color.parseColor(defTextColor));
        mDivider.setBackgroundColor(Color.parseColor(defDividerColor));
        mMessage.setTextColor(Color.parseColor(defMsgColor));
        mLinearLayoutView.setBackgroundColor(Color.parseColor(defDialogColor));
    }

    /**
     * 设置间隔线条的颜色 参数类型为字符串
     * @param colorString
     * @return
     */
    public NiftyDialogBuilder withDividerColor(String colorString) {
        mDivider.setBackgroundColor(Color.parseColor(colorString));
        return this;
    }

    /**
     * 设置间隔线条颜色，参数类型是int
     * @param color
     * @return
     */
    public NiftyDialogBuilder withDividerColor(int color) {
        mDivider.setBackgroundColor(color);
        return this;
    }

    /**
     * 设置title字体
     * @param title
     * @return
     */
    public NiftyDialogBuilder withTitle(CharSequence title) {
        toggleView(mLinearLayoutTopView, title);
        mTitle.setText(title);
        return this;
    }

    /**
     * 设置title的字体颜色  String类型
     * @param colorString
     * @return
     */
    public NiftyDialogBuilder withTitleColor(String colorString) {
        mTitle.setTextColor(Color.parseColor(colorString));
        return this;
    }

    /**
     * 设置title的字体颜色  int类型
     * @param color
     * @return
     */
    public NiftyDialogBuilder withTitleColor(int color) {
        mTitle.setTextColor(color);
        return this;
    }

    /**
     * 设置内容字体 int 类型（配置在string.xml中的）
     * @param textResId
     * @return
     */
    public NiftyDialogBuilder withMessage(int textResId) {
        toggleView(mLinearLayoutMsgView, textResId);
        mMessage.setText(textResId);
        return this;
    }

    /**
     * 设置内容字体 CharSequence类型
     * @param msg
     * @return
     */
    public NiftyDialogBuilder withMessage(CharSequence msg) {
        toggleView(mLinearLayoutMsgView, msg);
        mMessage.setText(msg);
        return this;
    }

    /**
     * 设置内容字体 CharSequence类型
     * @param msg
     * @return
     */
    public NiftyDialogBuilder withMessagepdf(CharSequence msg) {
        toggleView(mLinearLayoutMsgView, msg);
        mMessage.setText(msg);
        return this;
    }

    /**
     * 内容字体颜色 string
     * @param colorString
     * @return
     */
    public NiftyDialogBuilder withMessageColor(String colorString) {
        mMessage.setTextColor(Color.parseColor(colorString));
        return this;
    }

    /**
     * 内容字体颜色 int
     * @param color
     * @return
     */
    public NiftyDialogBuilder withMessageColor(int color) {
        mMessage.setTextColor(color);
        return this;
    }

    /**
     * 设置dialog的背景颜色 string
     * @param colorString
     * @return
     */
    public NiftyDialogBuilder withDialogColor(String colorString) {
        mLinearLayoutView.getBackground().setColorFilter(ColorUtils.getColorFilter(Color
                .parseColor(colorString)));
        return this;
    }

    /**
     * 设置dialog的背景颜色 int
     * @param color
     * @return
     */
    public NiftyDialogBuilder withDialogColor(int color) {
        mLinearLayoutView.getBackground().setColorFilter(ColorUtils.getColorFilter(color));
        return this;
    }

    /**
     * 设置dialog的icon
     * @param drawableResId
     * @return
     */
    public NiftyDialogBuilder withIcon(int drawableResId) {
        mIcon.setImageResource(drawableResId);
        return this;
    }

    /**
     * 设置dialog的icon
     * @param icon
     * @return
     */
    public NiftyDialogBuilder withIcon(Drawable icon) {
        mIcon.setImageDrawable(icon);
        return this;
    }

    /**
     * 设置dialog出现的速度
     * @param duration
     * @return
     */
    public NiftyDialogBuilder withDuration(int duration) {
        this.mDuration = duration;
        return this;
    }

    public NiftyDialogBuilder withEffect(Effectstype type) {
        this.type = type;
        return this;
    }

    /**
     * 设置button的背景
     * @param resid
     * @return
     */
    public NiftyDialogBuilder withButtonDrawable(int resid) {
        mButton1.setBackgroundResource(resid);
        mButton2.setBackgroundResource(resid);
        return this;
    }

    /**
     * 设置button的字体
     * @param text
     * @return
     */
    public NiftyDialogBuilder withButton1Text(CharSequence text) {
        mButton1.setVisibility(View.VISIBLE);
        mButton1.setText(text);

        return this;
    }

    public NiftyDialogBuilder withButton2Text(CharSequence text) {
        mButton2.setVisibility(View.VISIBLE);
        mButton2.setText(text);
        return this;
    }

    /**
     * 给button添加点击
     * @param click
     * @return
     */
    public NiftyDialogBuilder setButton1Click(View.OnClickListener click) {
        mButton1.setOnClickListener(click);
        return this;
    }

    public NiftyDialogBuilder setButton2Click(View.OnClickListener click) {
        mButton2.setOnClickListener(click);
        return this;
    }

    /**
     * 添加自定义的layout
     * @param resId
     * @param context
     * @return
     */
    public NiftyDialogBuilder setCustomView(int resId, Context context) {
        View customView = View.inflate(context, resId, null);
        if (mFrameLayoutCustomView.getChildCount() > 0) {
            mFrameLayoutCustomView.removeAllViews();
        }
        mFrameLayoutCustomView.addView(customView);
        mFrameLayoutCustomView.setVisibility(View.VISIBLE);
        return this;
    }

    public NiftyDialogBuilder setCustomView(View view, Context context) {
        if (mFrameLayoutCustomView.getChildCount() > 0) {
            mFrameLayoutCustomView.removeAllViews();
        }
        mFrameLayoutCustomView.addView(view);
        mFrameLayoutCustomView.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * 设置dialog点击外边界是否可取消
     * @param cancelable
     * @return
     */
    public NiftyDialogBuilder isCancelableOnTouchOutside(boolean cancelable) {
        this.isCancelable = cancelable;
        this.setCanceledOnTouchOutside(cancelable);
        return this;
    }

    /**
     * 设置dialog是否可取消
     * @param cancelable
     * @return
     */
    public NiftyDialogBuilder isCancelable(boolean cancelable) {
        this.isCancelable = cancelable;
        this.setCancelable(cancelable);
        return this;
    }

    /**
     * 设置view隐藏
     * @param view
     * @param obj
     */
    private void toggleView(View view, Object obj) {
        if (obj == null) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }
  public static   String assetFileName="";

    public static void setAssetFileName(String assetFileName) {
        NiftyDialogBuilder.assetFileName = assetFileName;
    }

    /**
     * 显示
     */
    @Override
    public void show() {
        super.show();
        //从assets目录读取pdf
        displayFromAssets(assetFileName);
        //从文件中读取pdf
//        displayFromFile( new File( "fileName"));
    }

    /**
     * 动画开始
     * @param type
     */
    private void start(Effectstype type) {
        BaseEffects animator = type.getAnimator();
        if (mDuration != -1) {
            animator.setDuration(Math.abs(mDuration));
        }
        animator.start(mRelativeLayoutView);
    }

    /**
     * 消灭dialog
     */
    @Override
    public void dismiss() {
        super.dismiss();
        mButton1.setVisibility(View.GONE);
        mButton2.setVisibility(View.GONE);
    }


    private void displayFromAssets(String assetFileName) {
        pdfView.fromAsset(assetFileName)   //设置pdf文件地址
                .defaultPage(1)         //设置默认显示第1页
                .onPageChange(this)     //设置翻页监听
                .onLoad(this)           //设置加载监听
                .onDraw(this)            //绘图监听
                .showMinimap(false)     //pdf放大的时候，是否在屏幕的右上角生成小地图
                .swipeVertical(true)  //pdf文档翻页是否是垂直翻页，默认是左右滑动翻页
                .enableSwipe(true)   //是否允许翻页，默认是允许翻页
//                 .pages()  //把 5 过滤掉
                .load();
    }

    private void displayFromFile(File file) {
        pdfView.fromFile(file)   //设置pdf文件地址
                .defaultPage(6)         //设置默认显示第1页
                .onPageChange(this)     //设置翻页监听
                .onLoad(this)           //设置加载监听
                .onDraw(this)            //绘图监听
                .showMinimap(false)     //pdf放大的时候，是否在屏幕的右上角生成小地图
                .swipeVertical(false)  //pdf文档翻页是否是垂直翻页，默认是左右滑动翻页
                .enableSwipe(true)   //是否允许翻页，默认是允许翻
                // .pages( 2 ，5  )  //把2  5 过滤掉
                .load();
    }

    /**
     * 翻页回调
     *
     * @param page
     * @param pageCount
     */
    @Override
    public void onPageChanged(int page, int pageCount) {
//        Toast.makeText(getContext(), "page= " + page +
//                " pageCount= " + pageCount, Toast.LENGTH_SHORT).show();
    }

    /**
     * 加载完成回调
     *
     * @param nbPages 总共的页数
     */
    @Override
    public void loadComplete(int nbPages) {
        //Toast.makeText(getContext(), "加载完成" + nbPages, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {
        // Toast.makeText( MainActivity.this ,  "pageWidth= " + pageWidth + "
        // pageHeight= " + pageHeight + " displayedPage="  + displayedPage , Toast.LENGTH_SHORT).show();
    }
}
