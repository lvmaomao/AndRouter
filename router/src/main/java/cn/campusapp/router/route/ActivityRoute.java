package cn.campusapp.router.route;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import java.lang.ref.WeakReference;

import cn.campusapp.router.router.IRouter;

/**
 * Created by kris on 16/3/10.
 * include information about route
 */
public class ActivityRoute extends BaseRoute {
    public static final int START = 0;
    public static final int FOR_RESULT_ACTIVITY = 1;
    public static final int FOR_RESULT_SUPPORT_FRAGMENT = 2;
    public static final int FOR_RESULT_FRAGMENT = 3;
    private Bundle mExtras; //some option params or not base type params. for example you want to add a parcelable parameter
    private int mInAnimation = -1;  //the animation the next activity in
    private int mOutAnimation = -1;  //the animation the last activity out
    private WeakReference<Activity> mARef; //if you want to set the animation, you must give last activity
    private int mOpenType = 0;
    private WeakReference<Fragment> mSupportFRef; //if you want to use fragment.startActivityForResutl
    private int mRequestCode = 0;  // request code to start activity for result
    private WeakReference<android.app.Fragment> mFRef; //if you want to use android.app.fragment to startActivityForResult, you should set this

    private ActivityRoute(IRouter router, String url){
        super(router, url);
    }

    public Bundle getExtras(){
        return mExtras;
    }

    public int getInAnimation(){
        return mInAnimation;
    }

    public int getOutAnimation(){
        return mOutAnimation;
    }



    public boolean isAnimationValid(){
        return mInAnimation != -1 && mOutAnimation != -1 && mARef!= null && mARef.get() != null;
    }

    public Activity getActivity(){
        if(mARef != null && mARef.get() != null){
            return mARef.get();
        } else {
            return null;
        }
    }

    public Fragment getSupportFragment(){
        return mSupportFRef != null ? mSupportFRef.get() : null;
    }

    public android.app.Fragment getFragment(){
        return mFRef != null ? mFRef.get() : null;
    }

    public int getRequestCode(){
        return mRequestCode;
    }

    public int getOpenType(){
        return mOpenType;
    }

    public void setAnimation(Activity activity, int inAnimation, int outAnimation){
        mARef = new WeakReference<>(activity);
        mInAnimation = inAnimation;
        mOutAnimation = outAnimation;
    }


    /**
     * 设置打开页面的方式为startActivity，这也是默认的打开方式
     */
    public void withOpenMethodStart(Activity activity){
        mOpenType = START;
        mARef = new WeakReference<>(activity);
    }

    public void withOpenMethodStartForResult(Activity activity, int requestCode){
        mRequestCode = requestCode;
        mOpenType = FOR_RESULT_ACTIVITY;
        mARef = new WeakReference<>(activity);
    }

    public void withOpenMethodStartForResult(Fragment fragment, int requestCode){
        mRequestCode = requestCode;
        mOpenType = FOR_RESULT_SUPPORT_FRAGMENT;
        mSupportFRef = new WeakReference<Fragment>(fragment);
    }

    public void withOpenMethodStartForResult(android.app.Fragment fragment, int requestCode){
        mRequestCode = requestCode;
        mOpenType = FOR_RESULT_FRAGMENT;
        mFRef = new WeakReference<>(fragment);
    }




    public static class Builder{
        String mUrl;
        IRouter mRouter;
        Bundle mBundle;
        int mInAnimation;
        int mOutAnimation;
        WeakReference<Activity> mARef;
        private int mOpenType = 0;
        private WeakReference<Fragment> mSupportFRef; //if you want to use fragment.startActivityForResutl
        private int mRequestCode = 0;  // request code to start activity for result
        private WeakReference<android.app.Fragment> mFRef; //if you want to use android.app.fragment to startActivityForResult, you should set this


        public Builder(IRouter router){
            mBundle = new Bundle();
            mRouter = router;
        }

        public Builder setUrl(String url) {
            mUrl = url;
            return this;
        }

        public Builder withParams(String key, Parcelable value){
            mBundle.putParcelable(key, value);
            return this;
        }

        public Builder withParams(String key, int value){
            mBundle.putInt(key, value);
            return this;
        }

        public Builder withParams(String key, double value){
            mBundle.putDouble(key, value);
            return this;
        }

        public Builder withParams(String key, float value){
            mBundle.putFloat(key, value);
            return this;
        }

        public Builder withParams(String key, char value){
            mBundle.putChar(key, value);
            return this;
        }

        public Builder withParams(String key, CharSequence value){
            mBundle.putCharSequence(key, value);
            return this;
        }

        public Builder withParams(String key, String value){
            mBundle.putString(key, value);
            return this;
        }

        public Builder withParams(String key, long value){
            mBundle.putLong(key, value);
            return this;
        }

        /**
         * 设置打开页面的方式为startActivity，这也是默认的打开方式
         */
        public Builder withOpenMethodStart(Activity activity){
            mOpenType = START;
            mARef = new WeakReference<>(activity);
            return this;
        }

        public Builder withOpenMethodStartForResult(Activity activity, int requestCode){
            mRequestCode = requestCode;
            mOpenType = FOR_RESULT_ACTIVITY;
            mARef = new WeakReference<>(activity);
            return this;
        }

        public Builder withOpenMethodStartForResult(Fragment fragment, int requestCode){
            mRequestCode = requestCode;
            mOpenType = FOR_RESULT_SUPPORT_FRAGMENT;
            mSupportFRef = new WeakReference<Fragment>(fragment);
            return this;
        }

        public Builder withOpenMethodStartForResult(android.app.Fragment fragment, int requestCode){
            mRequestCode = requestCode;
            mOpenType = FOR_RESULT_FRAGMENT;
            mFRef = new WeakReference<>(fragment);
            return this;
        }

        /**
         *
         * @param extra
         * @return
         */
        public Builder putAllParams(Bundle extra){
            mBundle.putAll(extra);
            return this;
        }

        /**
         * set the animation of activity transform
         * @param inAnimation
         * @param outAnimation
         * @return
         */
        public Builder withAnimation(Activity activity, int inAnimation, int outAnimation){
            mARef = new WeakReference<Activity>(activity);
            mInAnimation = inAnimation;
            mOutAnimation = outAnimation;
            return this;
        }



        public ActivityRoute build(){
            ActivityRoute route = new ActivityRoute(mRouter, mUrl);
            if(mARef != null && mARef.get() != null && mInAnimation != -1 && mOutAnimation != -1){
                route.setAnimation(mARef.get(), mInAnimation, mOutAnimation);
            }
            route.withOpenMethodStart(mARef != null ? mARef.get() : null);
            route.withOpenMethodStartForResult(mARef != null ? mARef.get() : null, mRequestCode);
            route.withOpenMethodStartForResult(mFRef != null ? mFRef.get() : null, mRequestCode);
            route.withOpenMethodStartForResult(mSupportFRef != null ? mSupportFRef.get() : null, mRequestCode);
            return route;
        }
    }


}
