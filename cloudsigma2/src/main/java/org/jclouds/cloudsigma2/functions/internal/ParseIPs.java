/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.cloudsigma2.functions.internal;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import org.jclouds.cloudsigma2.CloudSigma2Api;
import org.jclouds.cloudsigma2.domain.IP;
import org.jclouds.cloudsigma2.domain.PaginatedCollection;
import org.jclouds.cloudsigma2.options.PaginationOptions;
import org.jclouds.collect.IterableWithMarker;
import org.jclouds.collect.internal.ArgsToPagedIterable;
import org.jclouds.http.functions.ParseJson;
import org.jclouds.json.Json;

import javax.inject.Singleton;
import java.beans.ConstructorProperties;
import java.util.List;

/**
 * @author Vladimir Shevchenko
 */
@Singleton
public class ParseIPs extends ParseJson<ParseIPs.IPs> {
    static class IPs extends PaginatedCollection<IP> {

        @ConstructorProperties({"objects", "meta"})
        public IPs(Iterable<IP> objects, PaginationOptions paginationOptions) {
            super(objects, paginationOptions);
        }
    }

    @Inject
    public ParseIPs(Json json) {
        super(json, TypeLiteral.get(IPs.class));
    }

    public static class ToPagedIterable extends ArgsToPagedIterable<IP, ToPagedIterable> {

        private CloudSigma2Api api;

        @Inject
        public ToPagedIterable(CloudSigma2Api api) {
            this.api = api;
        }

        @Override
        protected Function<Object, IterableWithMarker<IP>> markerToNextForArgs(List<Object> args) {
            return new Function<Object, IterableWithMarker<IP>>() {
                @Override
                public IterableWithMarker<IP> apply(Object input) {
                    PaginationOptions paginationOptions = PaginationOptions.class.cast(input);
                    return api.listIPs(paginationOptions);
                }
            };
        }
    }
}
