package life.majiang.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode{
    QUSETION_NOT_FOUND(2001,"您找的问题不在了"),
    TARGET_PARAM_NOT_FOUND(2002,"未选中任何问题或评论进行回复"),
    NO_LOGIN(2003,"当前操作需要登录，请登录后重试"),
    SYS_ERROR(2004,"服务器冒烟了"),
    TYPE_PARAM_WRONG(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006, "回复的评论不存在");

    private Integer code;
    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }
}

// 枚举类知识：

//// 定义一个Color枚举类：
//public enum Color {
//    RED("red"), GREEN("green"), BLUE("blue");
//}

//// 编译器编译出的class大概就像这样：
//public final class Color extends Enum { // 继承自Enum，标记为final class
//    // 以下三个属性的实例均分别为全局唯一:
//    // 即不论调用多少次Color.RED访问的都是同一个对象
//    public static final Color RED = new Color("red");
//    public static final Color GREEN = new Color("green");
//    public static final Color BLUE = new Color("blue");
//    // private构造方法，确保外部无法调用new操作符:
//    private Color(String color) {
//        System.out.println("Constructor called for : " + color);
//    }
//
//    public void colorInfo()
//    {
//        System.out.println(this.toString());
//    }
//}

// 第一次调用Color.RED/Color.GREEN/Color.BLUE时，则会一次性完成对三个单例属性的初始化
// 由于无法显示的调用new操作符来调用enum类的构造函数，实例化Color的方式只能通过声明引用指向Color中的三个单例属性
// 即：Color c1 = Color.RED;
// 在调用非构造方法时，实则是根据引用指向的某个单例属性调用该方法完成的：
// 即：c1.colorInfo()，该方法中的this指向的对象，就是public static final Color RED = new Color("red");这个对象
