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

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;
import com.exorath.service.commons.dynamoDBProvider.DynamoDBProvider;
import com.exorath.service.commons.tableNameProvider.TableNameProvider;
import com.exorath.service.translation.Service;
import com.exorath.service.translation.res.Success;
import com.exorath.service.translation.res.TranslationsPackage;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by toonsev on 11/20/2016.
 */
public class DynamoDBService implements Service {
    private static final Logger LOG = LoggerFactory.getLogger(DynamoDBService.class);
    public static final String ID_FIELD = "id";
    public static final String VERSION_FIELD = "version";
    public static final String TRANSLATIONS_FIELD = "translations";

    private DynamoDBMapperConfig config;
    private DynamoDBMapper mapper;

    public DynamoDBService(DynamoDBProvider dbProvider, TableNameProvider tableNameProvider) {
         AmazonDynamoDBClient client = dbProvider.getClient();
        String tableName = tableNameProvider.getTableName();
        config = DynamoDBMapperConfig.builder().withTableNameOverride(DynamoDBMapperConfig.TableNameOverride.withTableNameReplacement(tableName)).build();
        mapper = new DynamoDBMapper(client, config);
        createTable(new DynamoDB(client), tableName);
    }

    private void createTable(DynamoDB dynamoDB, String tableName){
        CreateTableRequest createTableRequest = mapper.generateCreateTableRequest(TranslationsPackage.class).withProvisionedThroughput(new ProvisionedThroughput(1l, 1l));
        Table table = null;
        try {
            table = dynamoDB.createTable(createTableRequest);
        } catch (ResourceInUseException ex) {
            table = dynamoDB.getTable(tableName);
            LOG.info("DynamoDB table " + tableName + " already existed. Waiting for it to activate.");
        }
        try {
            table.waitForActive();
        } catch (InterruptedException ex) {
            LOG.error("DynamoDB table " + tableName + " could not activate!\n" + ex.getMessage());
            System.exit(1);
        }
    }
    @Override
    public TranslationsPackage getPackage(String packageId) {
        try {
            Map<String, AttributeValue> eav = new HashMap<>();
            eav.put(":id", new AttributeValue().withS(packageId));

            TranslationsPackage translationsPackage = mapper.load(TranslationsPackage.class, packageId);
            return translationsPackage;
        }catch (ResourceNotFoundException notFoundE){
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Integer getPackageVersion(String packageId) {
        try {
            Map<String, AttributeValue> eav = new HashMap<>();
            eav.put(":id", new AttributeValue().withS(packageId));
            DynamoDBQueryExpression<TranslationsPackage> queryExpression = new DynamoDBQueryExpression<TranslationsPackage>()
                    .withKeyConditionExpression(ID_FIELD + " = :id")
                    .withExpressionAttributeValues(eav)
                    .withSelect(Select.SPECIFIC_ATTRIBUTES)
                    .withProjectionExpression(VERSION_FIELD);
            PaginatedQueryList<TranslationsPackage> paginatedQueryList = mapper.query(TranslationsPackage.class, queryExpression);
            if (paginatedQueryList.size() != 1)
                return null;
            TranslationsPackage translationsPackage = paginatedQueryList.get(0);
            return translationsPackage.getVersion();
        } catch (ResourceNotFoundException notFoundE){
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Success updatePackage(TranslationsPackage translationsPackage) {
        try {
            ExpectedAttributeValue expectedVersionValue = new ExpectedAttributeValue(new AttributeValue().withN(translationsPackage.getVersion().toString())).withComparisonOperator(ComparisonOperator.LT);
            DynamoDBSaveExpression saveExpression = new DynamoDBSaveExpression()
                    .withExpectedEntry(ID_FIELD, new ExpectedAttributeValue(false))
                    .withExpectedEntry(VERSION_FIELD, expectedVersionValue)
                    .withConditionalOperator(ConditionalOperator.OR);
            mapper.save(translationsPackage, saveExpression);
            return new Success(true);
        }catch (ConditionalCheckFailedException e){
          return new Success(false, "Current version is larger or equal to the provided version");
        } catch (Exception e) {
            e.printStackTrace();
            return new Success(false, e.getMessage());
        }
    }
}
