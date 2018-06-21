package com.kh.search.service.impl;

import com.kh.search.dao.KhApiMapperExt;
import com.kh.search.model.KhApi;
import com.kh.search.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 所在的包名: com.kh.search.service.impl
 * 所在的项目名：kh-search
 *
 * @Author:xukh
 * @Description:
 * @Date: Created in 19:34 2018/6/13
 */
@Service
public class UserService implements IUserService {

    @Autowired
    private KhApiMapperExt khApiMapperExt;

    @Override
    public KhApi getOne() {
        return khApiMapperExt.selectByPrimaryKey(1);
    }
}
