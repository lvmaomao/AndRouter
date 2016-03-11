package cn.campusapp.router;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;

import java.lang.ref.WeakReference;

/**
 * Created by kris on 16/3/10.
 * include information about route
 */
public class Route {
    private String mPath;  //the route path, to decide the activity to go
    private Bundle mExtras; //some option params or not base type params. for example you want to add a parcelable parameter
    private int mInAnimation = -1;  //the animation the next activity in
    private int mOutAnimation = -1;  //the animation the last activity out
    private WeakReference<Activity> mARef; //if you want to set the animation, you must give last activity

    private Route(){}
    private Route(String path, Bundle extras, int inAnimation, int outAnimation, WeakReference<Activity> aRef){
        mPath = path;
        mExtras = extras;
        mInAnimation = inAnimation;
        mOutAnimation = outAnimation;
        mARef = aRef;
    }

    public String getPath(){
        return mPath;
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

    public Activity getActivity(){
        if(mARef != null && mARef.get() != null){
            return mARef.get();
        } else {
            return null;
        }
    }

    public static boolean isTheRouteValid(Route route){
        if(route != null
                &&!TextUtils.isEmpty(route.getPath())
                ){
            return true;
        } else {
            return false;
        }
    }

    public static class Builder{
        String mPath;
        Bundle mBundle;
        int mInAnimation;
        int mOutAnimation;
        WeakReference<Activity> mARef;

        public Builder(){
            mBundle = new Bundle();
        }

        public Builder setPath(String path) {
            mPath = path;
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

        public Route build(){
            return new Route(mPath, mBundle, mInAnimation, mOutAnimation, mARef);
        }


    }


}
