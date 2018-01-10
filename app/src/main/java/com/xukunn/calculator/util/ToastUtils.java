package com.xukunn.calculator.util;

import android.content.Context;
import android.widget.Toast;

/**
 *
 * @author wanglc
 * @date 2015-5-7
 * @description
 * Toast工具类
 * 单例Toast，保证Toast只会出现一次，手快人的福音
 * @modifyList
 * 
 */
public class ToastUtils {

    private static Toast mToast = null;
    
    /**
     *  Toast.makeText(Context, String, Toast.LENGTH_SHORT);
     * @param ctx Context
     * @param resID ResourceID
     */
    public static void showShortToast(Context ctx, String content) {
        if (mToast == null) {
            mToast = Toast.makeText(ctx, content, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(content);
        }
        mToast.show();
    }
    /**
     *  Toast.LENGTH_SHORT;
     * @param ctx Context
     * @param resID ResourceID
     */
    public static void showShortToast(Context ctx, int resID) {
        if (mToast == null) {
            mToast = Toast.makeText(ctx, resID, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(resID);
        }
        mToast.show();
    }

    /**
     *  Toast.LENGTH_LONG
     * @param ctx Context
     * @param resID ResourceID
     */
    public static void showLongToast(Context ctx, String content) {
        if (mToast == null) {
            mToast = Toast.makeText(ctx, content, Toast.LENGTH_LONG);
        } else {
            mToast.setText(content);
        }
        mToast.show();
    }
    /**
     * Toast.LENGTH_LONG
    `* @param ctx Context
     * @param resID ResourceID
     */
    public static void showLongToast(Context ctx, int resID) {
        if (mToast == null) {
            mToast = Toast.makeText(ctx, resID, Toast.LENGTH_LONG);
        } else {
            mToast.setText(resID);
        }
        mToast.show();
    }
}
