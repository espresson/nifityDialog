package com.gitonway.lee.niftymodaldialogeffects;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;


public class MainActivity extends Activity {

    private Effectstype effect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 点击事件
     * @param v
     */
    public void dialogShow(View v) {
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);

        switch (v.getId()) {
            //根据每个窗口的类型展现不同的dialog 效果
            case R.id.fadein:
                effect = Effectstype.Fadein;
                break;
            case R.id.slideright:
                effect = Effectstype.Slideright;
                break;
            case R.id.slideleft:
                effect = Effectstype.Slideleft;
                break;
            case R.id.slidetop:
                effect = Effectstype.Slidetop;
                break;
            case R.id.slideBottom:
                effect = Effectstype.SlideBottom;
                break;
            case R.id.newspager:
                effect = Effectstype.Newspager;
                break;
            case R.id.fall:
                effect = Effectstype.Fall;
                break;
            case R.id.sidefall:
                effect = Effectstype.Sidefill;
                break;
            case R.id.fliph:
                effect = Effectstype.Fliph;
                break;
            case R.id.flipv:
                effect = Effectstype.Flipv;
                break;
            case R.id.rotatebottom:
                effect = Effectstype.RotateBottom;
                break;
            case R.id.rotateleft:
                effect = Effectstype.RotateLeft;
                break;
            case R.id.slit:
                effect = Effectstype.Slit;
                break;
            case R.id.shake:
                effect = Effectstype.Shake;
                break;
        }
        /**
         * 设置dialog样式
         */
        dialogBuilder
                .withTitle("弹窗样式")                                  //窗口标题
                .withTitleColor("#FFFFFF")                                  //窗口字体颜色
                .withDividerColor("#631AA1E5")                              //线条颜色
               // .withMessage("这是小窗口内容")                     //内容
                .withMessageColor("#FFFFFFFF")                              //里面内容的字体颜色
                .withDialogColor("#331AA1E5")                               //窗口颜色
              //  .withIcon(getResources().getDrawable(R.drawable.ic_launcher))   //窗口的icon(直接用withIcon(R.drawable.ic_launcher会出现异常))
                .isCancelableOnTouchOutside(false)                           //是否可点击窗口外边取消窗口
                .withDuration(500)                                          //动画速度
                .withEffect(effect)                                         //窗口类型
                .withButton1Text("知道了")                                      //两个button按钮
              //  .withButton2Text("取消")
                .setCustomView(R.layout.custom_view, v.getContext())         //窗口内容下面的layout
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // Toast.makeText(v.getContext(), "确定", Toast.LENGTH_SHORT).show();
                        dialogBuilder.dismiss();
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      //  Toast.makeText(v.getContext(), "取消", Toast.LENGTH_SHORT).show();
                        dialogBuilder.dismiss();
                    }
                }).show();
    }
}
