/*
 * Copyright (c) Baidu Inc. All rights reserved.
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baidu.brcc.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.baidu.brcc.domain.User;
import com.baidu.brcc.domain.base.R;
import com.baidu.brcc.domain.em.UserRole;
import com.baidu.brcc.dto.UserDto;
import com.baidu.brcc.service.UserService;

public class UserControllerTest {
    @Mock
    UserService userService;
    @InjectMocks
    UserController userController;
    private User adminLoginUser = null;
    private User normalLoginUser = null;
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        adminLoginUser = new User();
        adminLoginUser.setName("admin");
        adminLoginUser.setToken("token2");
        adminLoginUser.setId(2L);
        adminLoginUser.setRole(UserRole.SYSADMIN.getValue());

        normalLoginUser = new User();
        normalLoginUser.setName("normal");
        normalLoginUser.setToken("token3");
        normalLoginUser.setId(3L);
        normalLoginUser.setRole(UserRole.NORMAL.getValue());
    }

    @Test
    public void testMyProfile() throws Exception {
        R<UserDto> result = userController.myProfile(adminLoginUser);
        Assert.assertEquals(0, result.getStatus());
    }
}
