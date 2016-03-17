package cn.campusapp.router.utils;

import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import timber.log.Timber;

/**
 * Created by kris on 16/3/10.
 */
public class UrlUtils {

    private static final String TAG = "UrlUtils";

    /**
     * get the path segments
     * @param url
     * @return
     */
    public static List<String> getPathSegments(String url){
        try {
            return Uri.parse(url).getPathSegments();
        } catch (Exception e){
            Timber.e(e, "url parse fail");
        }
        return new ArrayList<>();
    }

    /**
     * get the scheme of the url
     * @param url
     * @return
     */
    public static String getScheme(String url){
        try{
            return Uri.parse(url).getScheme();
        } catch (Exception e){
            Timber.e(e, "url parse fail");
        }
        return null;
    }


    /**
     * get the protocol of the url
     */
    public static int getPort(String url){
        try{
            return Uri.parse(url).getPort();
        } catch (Exception e){
            Timber.e(e, "url parse fail");
        }
        return -1;
    }

    public static String getHost(String url){
        try{
            return Uri.parse(url).getHost();
        } catch (Exception e){
            Timber.e(e, "url parse fail");
        }
        return null;
    }

    public static HashMap<String, String> getParameters(String url){
        HashMap<String, String> parameters = new HashMap<>();
        try{
            Uri uri = Uri.parse(url);
            Set<String> keys = uri.getQueryParameterNames();

            for(String key : keys){
                parameters.put(key, uri.getQueryParameter(key));
            }
        } catch (Exception e){
            Timber.e(e, "");
        }
        return parameters;
    }


    public static String addQueryParameters(String url, String key, String value){
        try{
            Uri uri = Uri.parse(url);
            return uri.buildUpon().appendQueryParameter(key, value).build().toString();
        } catch (Exception e){
            Timber.e(e, "");
        }
        return url;
    }
}
