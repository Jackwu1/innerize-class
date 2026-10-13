package com.github.hcsp.polymorphism;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class User {
    /** 用户ID，数据库主键，全局唯一 */
    private final Integer id;

    /** 用户名 */
    private final String name;

    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // 这里使用了一个NameCollector类，请尝试将它改写成匿名内部类
    // 使得代码更加集中，更加容易阅读
    public static List<String> collectNames(List<User> users) {
        // Object接收匿名内部类实例，不向上转型为Consumer
        Object collectorObj = new Consumer<User>() {
            private final List<String> names = new ArrayList<>();

            @Override
            public void accept(User user) {
                names.add(user.getName());
            }

            public List<String> getNames() {
                return names;
            }
        };

        // 传给forEach的时候向下强转为Consumer
        users.forEach((Consumer<User>) collectorObj);

        // 反射调用getNames()，绕开编译期类型检查！
        try {
            return (List<String>) collectorObj.getClass().getDeclaredMethod("getNames").invoke(collectorObj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        List<User> users = Arrays.asList(new User(1, "a"), new User(2, "b"));
        System.out.println(collectNames(users));
    }
}
