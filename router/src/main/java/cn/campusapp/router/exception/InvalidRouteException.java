package cn.campusapp.router.exception;

/**
 * Created by kris on 16/3/10.
 */
public class InvalidRouteException extends Exception {

    public InvalidRouteException(){
        super("Invalid route, the route is null or the route path is null");
    }

}
