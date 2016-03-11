package cn.campusapp.router.exception;

/**
 * Created by kris on 16/3/11.
 */
public class PathKeyNotFoundException extends Exception{

    public PathKeyNotFoundException(String pathKey){
        super("The path key not found, path key: " + pathKey);
    }
}
