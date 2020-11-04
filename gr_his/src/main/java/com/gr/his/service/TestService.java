package com.gr.his.service;


import com.gr.his.entity.Db;
import com.gr.his.entity.User;

/**
 * 测试
 * @author lfz
 * @since  2020.09.22
 * @des mybatis
 */
public interface TestService {

    public User getUserById(Long id);

    public int addMySQLStorage(Db db);
}
