package cn.campusapp.router.utils;

import android.net.Uri;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by kris on 16/3/10.
 */
public class UrlUtils {

    private static final String TAG = "UrlUtils";
    private static final String TEMP_URL = "http://sixwolf.net/"; //because the route path don't have the host, it's can't be parse, so i give my blog address as the host

    public static List<String> getPathSegments(String path){
        try {
            String urlStr = TEMP_URL + cleanPath(path);
            return Uri.parse(urlStr).getPathSegments();
        } catch (Exception e){
            Timber.e(e, "url parse fail");
        }
        return new ArrayList<>();
    }

    /**
     * delete the “/” int the begin of the path
     * @param path
     * @return
     */
    private static String cleanPath(String path){
        if(!TextUtils.isEmpty(path) && path.startsWith("/")){
            return path.substring(1, path.length());
        } else {
            return path;
        }
    }


    public static Map<String, String> getOptionParams(String path){
        Map<String, String> map = new HashMap<>();
        try{
            String urlStr = TEMP_URL + cleanPath(path);
            Uri uri = Uri.parse(urlStr);
            for(String key : uri.getQueryParameterNames()){
                map.put(key, uri.getQueryParameter(key));
            }
        } catch (Exception e) {
            Timber.e(e, "get the param of the url fail");
        }
        return map;
    }
}
