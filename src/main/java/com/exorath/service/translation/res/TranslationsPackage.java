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

package com.exorath.service.translation.res;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.exorath.service.translation.impl.DynamoDBService;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by toonsev on 11/17/2016.
 */

public class TranslationsPackage {
    @DynamoDBHashKey(attributeName= DynamoDBService.ID_FIELD)
    @SerializedName("id")
    private String packageId;

    @DynamoDBAttribute(attributeName=DynamoDBService.VERSION_FIELD)
    private Integer version;

    @DynamoDBAttribute(attributeName=DynamoDBService.TRANSLATIONS_FIELD)
    @SerializedName("translations")
    private Map<String, Map<String, String>> translationsByKey;

    public TranslationsPackage() {}

    public TranslationsPackage(String packageId, Integer version) {
        this.packageId = packageId;
        this.version = version;
        translationsByKey = new HashMap<>();
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public void setTranslationsByKey(Map<String, Map<String, String>> translationsByKey) {
        this.translationsByKey = translationsByKey;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getPackageId() {
        return packageId;
    }

    public Integer getVersion() {
        return version;
    }

    public Map<String, Map<String, String>> getTranslationsByKey() {
        return translationsByKey;
    }
}
