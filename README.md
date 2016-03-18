# AndRouter
AndRouter is a android framework used to map url to activities or actions. 

## Features
- AndRouter implements activities router, so your only need to define your own activities mapping table and enjoying using url to open activities.
- AndRouter implements Browser router. So you can use it to open a web url. It will use the system browser to open it.
- AndRouter supports adding user-defined router. 
- RouteManager will use the first router which can open the url in the list to open it. So the router added earlier will have higher priority.
- The default scheme of activities router is "activity", and broswer router is "http" and "https". You are allowed to change them.
- You can change the behaviour of the default activity router and browser router.
- You can only add one object of a router class to the Router. Or it will delete the router object which has the same class of that you added and add the new one.

## Usage
### Initialize
If you want to use the default activity router and browser router, you should initialize them.   

```java
    Router.initActivityRouter(getApplicationContext(), new IActivityRouteTableInitializer() {
            @Override
            public void initRouterTable(Map<String, Class<? extends Activity>> router) {
            		// only if the host is equal and pathes match, it matches.
            		// The url "activity://first/kris/26/birthday" is one of the matches. "kris" and 26 are values of key "name" and "age". and the "name" value type is string, the age value type is integer.
            	   router.put("activity://first/:s{name}/:i{age}/birthday", FirstActivity.class); 
                router.put("activity://second/:{name}", SecondActivity.class);  
                router.put("activity://third/home/:i{room1}/printdoor/:s{color}", ThirdActivity.class); 
                router.put("activity://four/buy/:s{name}/:f{price}", FourActivity.class);                           
            }
        });
// if you want to change the default scheme of ActivityRouter
//        Router.initActivityRouter(getApplicationContext(), "hello", new IActivityRouteTableInitializer() {
//            @Override
//            public void initRouterTable(Map<String, Class<? extends Activity>> router) {
//                router.put("hello://second/:{name}", SecondActivity.class);
//            }
//        });
    Router.initBrowserRouter(getApplicationContext());

```

As the code above shows, you need to implement the IActivityRouteTableInitializer interface to add the activity router table. As we can see, I add some routes to the default activities router. For example, the route "activity://first/:s{name}/:i{age}/birthday" will map to FirstActivity.class. The route consists with four parts.

- Scheme: Normally, define which router to use, the "activity" in the example.
- Host: Normally, define where to go, the "first" in the example. Only url has the same host, it will match the route.
- Path: Define key value and path, the "/:s{name}/:i{age}/birthday" in the example. The path segments can be divided to two types. One, the path, such as "birthday", it's fixed. And the value key such as the :s{name}, it defines values in the url. You can replace the ":s{name}" with "kris", and it will be set to the intent extras with putExtra("name", "kris"), so you can get the value of "name" in the routing activity with getExtra("name"). 
ValueKey format is like the table below showes. ":{}" is essential. And the character after ':' define the type of the value, if the url mathes but the value type not matches, it will throw RuntimeException, which you should pay attention to.

|   key format |  :i{key}  | :f{key} | :l{key}  | :d{key}    |   :s{key} or :{key} | :c{key} |
|:-------:     |:--------: | :------:| :------: | :--------: |   :-------: | :----:|
|   type       |   integer |  float  |   long   |   double   |     string | char|
- Query parameters: you can add some option parameters in the Query parameters. It will also add to the Intent extras. But it will not influence the route match. For example, the url "activity://first/kris/26?wifename=marry" matches to "activity://first/:{name}/:i{age}".

### Definition of match
If a url matches a route, the url's scheme and host are equal to the route. And the pathes except the value key are equal.   
#### Match Examples
If the route is "activity://main/:{name}/feed/:i{page}/list".    
##### Matches:
   
- activity://main/kris/feed/12/list   
- activity://main/bob/feed/13/list   
- activity://main/jim/feed/14/list?q=8   

##### Not Matches

- hello://main/kris/feed/12/list  |            scheme not equal
- activity://second/kris/feed/12/list    |  host not equal
- activity://main/kris/fed/12/list        |   path not match
- activity://main/kris/12/list            |       path not match

### Useage of Activity Router
If you have initialize the Android Router, you can use it to open activity url like the code below.

open with startActivity(), it matches the route as "activity://second/:{name}"

```java
	
 	private void openSecondActivity(){
        Router.open("activity://second/汤二狗");
   }
```
in the SecondActivity you can get the value of name

```java
	public void getTheValueOfName(){
		String name = getIntent().getStringExtra("name")
	}
```
open with animation

```java
    private void openSecondActivityWithVerticalAnim(){
        ActivityRoute activityRoute = (ActivityRoute) Router.getRoute("activity://second/汤二狗");
        activityRoute
                .setAnimation(this, R.anim.in_from_left, R.anim.out_to_right)
                .open();
    }
```
open for result

```java
    private void openSecondActivityForResult(){
        ActivityRoute activityRoute = (ActivityRoute) Router.getRoute("activity://second/汤二狗");
        activityRoute.withOpenMethodStartForResult(this, 200)
                .open();
    }
```
add extra parameters

```java
    private void openSecondActivityWithExtraValue(){
        Date date = new Date();
        ActivityRoute activityRoute = (ActivityRoute) Router.getRoute("activity://third");
        activityRoute
                .withParams("date", date)
                .open();
    }

```
you can get the extra parameters in the ThirdActivity

```java
	Date date = (Date) getIntent().getSerializableExtra("date");
```
### Usage of Browser Router
And you can use Router to open web url.

```java
	Router.open("http://www.baidu.com");
```

### Add your own Router
you can define your own router and route.

```java
private static class TestRouter extends BaseRouter{

        @Override
        public void open(IRoute route) {
            Timber.i(route.getUrl());
        }

        @Override
        public void open(String url) {
            Timber.i(url);
        }

        @Override
        public IRoute getRoute(String url) {
            return new TestRoute(this, url);
        }

        @Override
        public boolean canOpenTheRoute(IRoute route) {
            return (route instanceof TestRoute);
        }

        @Override
        public boolean canOpenTheUrl(String url) {
            return TextUtils.equals(UrlUtils.getScheme(url), "test");
        }

        @Override
        public Class<? extends IRoute> getCanOpenRoute() {
            return TestRoute.class;
        }
    }

    private static class TestRoute extends BaseRoute {

        public TestRoute(IRouter router, String url) {
            super(router, url);
        }
    }
```

And add it to the RouterManager

```java
	Router.addRouter(new TestRouter());
```
## Install
Users of your library will need add the jitpack.io repository:

```
allprojects {
 repositories {
    jcenter()
    maven { url "https://jitpack.io" }
 }
}
```

and:

```
dependencies {
    compile 'com.github.campusappcn:AndRouter:1.0.0'
}
```

Note: do not add the jitpack.io repository under buildscript

## Feedback
If you have any problem, welcome to give some issues.

## License
```
Copyright 2015 杭州树洞网络科技有限公司

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```