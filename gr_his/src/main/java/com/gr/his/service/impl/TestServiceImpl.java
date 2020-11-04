package com.gr.his.service.impl;


import com.gr.his.entity.Db;
import com.gr.his.entity.User;
import com.gr.his.mapper.TestMapper;
import com.gr.his.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestMapper testMapper;

    @Override
    public User getUserById(Long id) {
        return testMapper.getUserById(id);
    }

    @Override
    public int addMySQLStorage(Db db) {
        return testMapper.addMySQLStorage(db);
    }
}
