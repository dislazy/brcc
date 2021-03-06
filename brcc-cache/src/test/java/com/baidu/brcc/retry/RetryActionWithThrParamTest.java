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
package com.baidu.brcc.retry;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.dao.CannotAcquireLockException;

import com.baidu.brcc.domain.base.R;

public class RetryActionWithThrParamTest {

    @Test
    public void testAction() throws Exception {
        RetryActionWithThrParam<String, String, String, R> retryActionWithThrParam = new RetryActionWithThrParam<>(
                "print",
                3,
                "a",
                "b",
                "c"
        );
        ThrFunction<String, String, String, R> thrFunction = (a, b, c) -> {
            System.out.println("p1 is " + a);
            System.out.println("p2 is " + b);
            System.out.println("p3 is " + c);
            return R.ok();
        };

        R result = retryActionWithThrParam.action(thrFunction);
        Assert.assertEquals(0, result.getStatus());
    }

    @Test
    public void testActionDataAccessException() throws Exception {
        RetryActionWithThrParam<String, String, String, R> retryActionWithThrParam = new RetryActionWithThrParam<>(
                "print",
                3,
                "a",
                "b",
                "c"
        );
        R result = retryActionWithThrParam.action(
                (a, b, c) -> {
                    throw new CannotAcquireLockException("throw dae");
                }
        );
        Assert.assertEquals(null, result);
    }

    @Test
    public void testActionException() throws Exception {
        RetryActionWithThrParam<String, String, String, R> retryActionWithThrParam = new RetryActionWithThrParam<>(
                "print",
                3,
                "a",
                "b",
                "c"
        );
        R result = retryActionWithThrParam.action(
                (a, b, c) -> {
                    throw new RuntimeException("run time exception");
                }
        );
        Assert.assertEquals(null, result);
    }
}
