package com.gr.his.controller;

import com.gr.his.entity.Db;
import com.gr.his.entity.User;
import com.gr.his.service.TestService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

/**
 * 访问地址
 */
@Api(tags = "测试demo")
@RestController
@RequestMapping(value = "test")
public class TestController {

    //日记对象输出
    private Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private TestService testService;


    @ApiOperation(value = "获取用户信息接口", nickname = "根据用户ID获取用户相关信息")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "int")
    @PostMapping("/postMember")
    public User postMember(@RequestParam Integer id) {
        User userEntity = new User();
        userEntity.setId(Long.valueOf(id));
        userEntity.setUserName("admin");
        return userEntity;
    }

    @ApiOperation(value = "添加用户", nickname = "添加用户接口1", notes = "入参是复杂对象", produces = "application/json")
    @PostMapping("/postUser")
    @ResponseBody
    @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", required = true, dataType = "int")
    public User postUser(@RequestBody User user, @RequestParam("userId") int userId) { // 这里用包装类竟然报错
        if (user.getId() == userId) {
            return user;
        }
        return new User();
    }

    @ApiOperation(value = "添加用户", nickname = "添加用户接口2", notes = "入参是简单对象", produces = "application/json")
    @PostMapping("/addUser")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userName", value = "用户姓名", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "id", value = "用户id", required = true, dataType = "int")})
    public User addUser(String userName, int id) {
        User userEntity = new User();
        userEntity.setUserName(userName);
        userEntity.setId(Long.valueOf(id));
        return userEntity;
    }



    @ApiOperation(value = "addMySQL", nickname = "动态库", notes = "动态添加数据库，表", produces = "application/json")
    @ApiImplicitParam( name = "id", value = "1", required = true, dataType = "Integer",paramType = "query")
    @ResponseBody
    @RequestMapping(value = "/addMySQL", method = RequestMethod.GET)
    public String addMySQL(@RequestParam("id") Integer id) {
        Assert.notNull(id, "id为空");
        String dbName ="lz";
        String dbTable ="ts";
        String tbDato ="";
        int number = testService.addMySQLStorage(new Db(dbName,dbTable,tbDato));
        logger.info(String.valueOf(number));
        return id.toString();
    }
}
