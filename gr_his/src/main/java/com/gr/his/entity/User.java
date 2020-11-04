package com.gr.his.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 测试
 * @author lfz
 */
public class User {

    private @Setter @Getter Long id;

    private @Setter @Getter String userName;

    private @Setter @Getter String realName;

    private @Setter @Getter Integer age;

    private @Setter @Getter BigDecimal balance;

    /**
     * 无参构造
     */
    public User() {
        super();
    }

    /**
     * 有参构造
     * @param id
     * @param username
     * @param realname
     * @param age
     * @param balance
     */
    public User(Long id, String username, String realname, Integer age, BigDecimal balance) {
        this.id = id;
        this.userName = username;
        this.realName = realname;
        this.age = age;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + userName + '\'' +
                ", realname='" + realName + '\'' +
                ", age=" + age +
                ", balance=" + balance +
                '}';
    }
}
