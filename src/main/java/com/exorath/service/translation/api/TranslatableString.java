/*
 * Copyright 2017 Exorath
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

package com.exorath.service.translation.api;

/**
 * Created by toonsev on 1/11/2017.
 */
public class TranslatableString {
    private String defaultString;
    private String translationId;

    public TranslatableString(String translationId, String defaultString) {
        this.defaultString = defaultString;
        this.translationId = translationId;
    }

    /**
     * FOR NOW THIS JUST RETURNS THE DEFAULT STRING, UNIMPLEMENTED
      * @param packageId
     * @param playerUuid
     * @return
     */
    public String translate(String packageId, String playerUuid){
        return defaultString;
    }

    public String getDefaultString() {
        return defaultString;
    }

    public String getTranslationId() {
        return translationId;
    }
}
