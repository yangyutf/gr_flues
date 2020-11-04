package com.gr.his.mapper;

import com.gr.his.entity.Db;
import com.gr.his.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestMapper {

    public User getUserById(@Param("id") Long id);

    public int addMySQLStorage(Db db);
}
