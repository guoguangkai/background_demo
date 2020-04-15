package com.zgx.ademo.result;

/***java枚举类是一组预定义常量的集合，使用enum关键字声明这个类，常量名称官方建议大写 ***/
/*其实enum类的每一个枚举值也是 static final的，但是我们为什么要选择使用enum枚举类呢，而不直接用常量
        1.static方式的静态变量类型不安全，我们可以在调用的时候传入其他值，导致错误
        2.static方式的静态变量不支持属性扩展，每一个key对应一个值，而enum的每一个key可以拥有自己的属性*/
/*单例模式，即一个类只有一个实例。而枚举其实就是多例，例如开车的方向有几种：前、后、左、右！
//其实创建枚举项就等同于调用本类的无参构造器，所以FRONT、BEHIND、LEFT、RIGHT四个枚举项等同于调用了四次无参构造器
 * 其中FRONT、BEHIND、LEFT、RIGHT都是枚举项，它们都是本类的实例，本类一共就只有四个实例对象。
* 不能使用new来创建枚举类的对象，因为枚举类中的实例就是类中的枚举项，所以在类外只能使用类名.枚举项。*/
//每个枚举类都有两个不用声明就可以调用的static方法，而且这两个方法不是父类中的方法。这又是枚举类特殊的地方，下面是Direction类的特殊方法。
//values()	以数组形式返回枚举类型的所有成员
//valueOf(enumName)	返回带指定名称的指定枚举类型的枚举常量。
public enum ResultCode {
    //在定义枚举项时，多个枚举项之间使用逗号分隔，最后一个枚举项后需要给出分号！枚举常量必须在枚举类中所有成员的上方声明
    //这里定义了五个枚举项：SUCCESS，FAIL，UNAUTHORIZED，NOT_FOUND，INTERNAL_SERVER_ERROR
    SUCCESS(200),
    FAIL(400),
    UNAUTHORIZED(401),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500);

    //其实枚举类和正常的类一样，可以有实例变量
    //ResultCode.valueOf("SUCCESS").code =200
    public int code;

    //枚举类的构造器不可以添加访问修饰符，枚举类的构造器默认是private的。但你自己不能添加private来修饰构造器。
    // 因为Direction类只有唯一的构造器，并且是有参的构造器，所以在创建枚举项时，必须为构造器赋值：ResultCode(“code”)
    ResultCode(int code) {//SUCCESS(200) 200就是参数
        this.code = code;
    }

}
