package cn.campusapp.router;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.campusapp.router.exception.NotAllKeySetException;

/**
 * 根据路径规则和参数生成路由路径
 * Created by kris on 16/3/10.
 */
public class RoutePathBuilder {
    private static final String TAG = "RoutePathBuilder";
    String mPath;
    String mMatchPath;

    /**
     *
     * @param matchPath 匹配的路由
     */
    public RoutePathBuilder(String matchPath){
        mPath = matchPath;
        mMatchPath = matchPath;
    }



    public RoutePathBuilder withKeyValue(String key, int value){
        mPath = mPath.replace(String.format(":i{%s}", key), Integer.toString(value));
        return this;
    }

    public RoutePathBuilder withKeyValue(String key, float value){
        mPath = mPath.replace(String.format(":f{%s}", key), Float.toString(value));
        return this;
    }

    public RoutePathBuilder withKeyValue(String key, long value){
        mPath = mPath.replace(String.format(":l{%s}", key), Long.toString(value));
        return this;
    }

    public RoutePathBuilder withKeyValue(String key, double value){
        mPath = mPath.replace(String.format(":d{%s}", key), Double.toString(value));
        return this;
    }



    public RoutePathBuilder withKeyValue(String key, String value){
        mPath = mPath.replace(String.format(":{%s}", key), value);
        mPath = mPath.replace(String.format(":s{%s}", key), value);
        return this;
    }

    public RoutePathBuilder withKeyValue(String key, char value){
        mPath = mPath.replace(String.format(":c{%s}", key), Character.toString(value));
        return this;
    }

    public String build() throws NotAllKeySetException{
        Matcher matcher = Pattern.compile(":[i, f, l, d, s, c]?\\{[a-zA-Z0-9]+?\\}").matcher(mPath);
        if(matcher.find()){
            throw new NotAllKeySetException(mPath);
        }
        return mPath;
    }




}
