/*
 * Copyright 2016 Exorath
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.exorath.service.translation.impl;

import com.amazonaws.services.dynamodbv2.model.AttributeAction;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.jcabi.dynamo.*;
import com.jcabi.dynamo.mock.H2Data;
import com.jcabi.dynamo.mock.MkRegion;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by toonsev on 11/19/2016.
 */
public class DynamoDBServiceTest {
    private static final String TABLE_NAME = "testtable";
    private DynamoDBService service;


    @Before
    public void setup() throws Exception{
        final Region region = new MkRegion(
                new H2Data().with(
                        "users",
                        new String[] {"id"},
                        new String[] {"name", "age"}
                )
        );
        final Table table = region.table("users");

        table.put(
                new Attributes()
                        .with("id", 10783)
                        .with("name", "Jeff Lebowski")
                        .with("age", 35)
        );

        for (Item id : table.frame().through(new ScanValve())) {
            id.put("age", new AttributeValueUpdate(new AttributeValue("38"), AttributeAction.PUT));
        }
        for (Item id : table.frame().through(new ScanValve())) {
            System.out.println(id);
        }

    }
    @Test
    public void getPackageReturnsNullByDefault() {}
}
