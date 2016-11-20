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

package com.exorath.service.translation;

import com.exorath.service.commons.portProvider.PortProvider;
import com.exorath.service.translation.res.Success;
import com.exorath.service.translation.res.TranslationsPackage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import spark.Route;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.put;

/**
 * Created by toonsev on 11/3/2016.
 */
public class Transport {
    private static final Gson GSON = new Gson();

    public static void setup(Service service, PortProvider portProvider) {
        port(portProvider.getPort());
        get("/packages/:packageId", getGetPackageRoute(service), GSON::toJson);
        put("/packages/:packageId", getPutPackageRoute(service), GSON::toJson);
        get("/packages/:packageId/version", getGetVersionRoute(service), GSON::toJson);
    }

    public static Route getGetPackageRoute(Service service) {
        return (req, res) -> {
            TranslationsPackage translationsPackage = service.getPackage(req.params("packageId"));
            if (translationsPackage == null)
                return new JsonObject();
            translationsPackage.setPackageId(null);
            return translationsPackage;
        };
    }

    public static Route getGetVersionRoute(Service service) {
        return (req, res) -> {
            Integer version = service.getPackageVersion(req.params("packageId"));
            JsonObject ret = new JsonObject();
            if (version != null)
                ret.addProperty("version", version);
            return ret;
        };
    }

    public static Route getPutPackageRoute(Service service) {
        return (req, res) -> {
            TranslationsPackage translationsPackage;
            try {
                translationsPackage = GSON.fromJson(req.body(), TranslationsPackage.class);
            } catch (Exception e) {
                e.printStackTrace();
                return new Success(false, e.getMessage());
            }
            translationsPackage.setPackageId(req.params("packageId"));
            return service.updatePackage(translationsPackage);
        };
    }
}
