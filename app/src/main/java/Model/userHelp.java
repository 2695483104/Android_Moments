package Model;

/**
 * 用户登录&注册过程中需要用到的常量
 * 因为枚举太占用资源 官方不推荐 所以不选择用enum
 */
public class userHelp {
    public static int requestCode_login = 1;
    public static int requestCode_register = 2;
    public static int requestCode_get_moment = 1;
    public static int requestCode_post_moment = 2;

    public static String userName = "userName";
    public static String password = "password";
    public static String phone = "phone";

    public static String requestCode = "requestCode";
    public static String legalUser = "legalUser";
    public static String registerResult = "registerResult";
    public static String text = "text";
    public static String postResult = "postResult";
}
