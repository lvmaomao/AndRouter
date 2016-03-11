package cn.campusapp.router;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.campusapp.router.utils.UrlUtils;
import timber.log.Timber;

/**
 * 该类用来创建路由规则，当然你也可以自己手动创建
 * Created by kris on 16/3/10.
 */
public class RouteRuleBuilder {
    private static final String TAG = "RouteRuleBuilder";

    private String mPath = "/";
    private List<String> mKeys = new ArrayList<>();

    public RouteRuleBuilder(){
    }

    /**
     * 增加一个路径的segment
     * @param path
     * @return
     */
    public RouteRuleBuilder addPath(String path){
        mPath = mPath + path + "/";
        return this;
    }

    public RouteRuleBuilder addParameter(String key, Class<?> type) {
        String typeChar = "";
        if(type.equals(Integer.class)){
            //整形
            typeChar = "i";
        } else if(type.equals(Float.class)){
            typeChar = "f";
        } else if(type.equals(Long.class)){
            typeChar = "l";
        } else if(type.equals(Double.class)){
            typeChar = "d";
        } else if(type.equals(String.class) || type.equals(CharSequence.class)){
            typeChar = "s";
        } else {
            typeChar = "s";
        }
        String keyFormat = String.format(":%s{%s}/", typeChar, key);
        if(mKeys.contains(keyFormat)){
            Log.e(TAG, "", new KeyDuplicateException(keyFormat));
        }
        mPath = mPath + keyFormat;
        mKeys.add(keyFormat);
        return this;
    }

    public String build(){
        if(mPath.endsWith("/")){
            mPath = mPath.substring(0, mPath.length() - 1);
        }
        return mPath;
    }

    /**
     * 判断一条路由规则是否合法
     * @param routeRule
     * @return
     */
    public static boolean isRouteRuleValid(String routeRule){
        String pattern = ":[i, f, l, d, s]?\\{[a-zA-Z0-9]+\\}"; //key 支持大小写字母及数字
        Pattern p = Pattern.compile(pattern);
        List<String> pathSegs = UrlUtils.getPathSegments(routeRule);
        List<String> checkedSegs = new ArrayList<>();
        for(String seg : pathSegs){
            if(seg.startsWith(":")){
                Matcher matcher = p.matcher(seg);
                if(!matcher.matches()){
                    Timber.w("The key format not match : %s" , seg);
                    return false;
                }
                if(checkedSegs.contains(seg)){
                    Timber.w("The key is duplicated : %s" , seg);
                    return false;
                }
                checkedSegs.add(seg);

            }
        }
        return true;
    }

    public static class KeyDuplicateException extends Exception{
        public KeyDuplicateException(String key){
            super("The key is duplicated: "+ key);
        }
    }
}
