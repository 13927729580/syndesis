/*
 * Copyright (C) 2016 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.google.sheets.stream;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import org.apache.camel.CamelContext;
import org.apache.camel.component.google.sheets.AbstractGoogleSheetsTestSupport;
import org.apache.camel.component.google.sheets.BatchGoogleSheetsClientFactory;
import org.apache.camel.component.google.sheets.server.GoogleSheetsApiTestServerRule;
import org.apache.camel.util.IntrospectionSupport;

/**
 * Abstract base class for GoogleSheets Integration tests generated by Camel API
 * component maven plugin.
 */
public class AbstractGoogleSheetsStreamTestSupport extends AbstractGoogleSheetsTestSupport {

    @Override
    protected CamelContext createCamelContext() throws Exception {

        final CamelContext context = super.createCamelContext();

        final GoogleSheetsStreamConfiguration configuration = new GoogleSheetsStreamConfiguration();
        IntrospectionSupport.setProperties(configuration, getTestOptions());

        // add GoogleSheetsComponent to Camel context
        final GoogleSheetsStreamComponent component = new GoogleSheetsStreamComponent(context);
        component.setClientFactory(new BatchGoogleSheetsClientFactory(
                new NetHttpTransport.Builder()
                        .trustCertificatesFromJavaKeyStore(
                                getClass().getResourceAsStream("/" + GoogleSheetsApiTestServerRule.SERVER_KEYSTORE),
                                GoogleSheetsApiTestServerRule.SERVER_KEYSTORE_PASSWORD)
                        .build(),
                new JacksonFactory()) {
            @Override
            protected void configure(Sheets.Builder clientBuilder) {
                clientBuilder.setRootUrl(String.format("https://localhost:%s/", googleSheetsApiTestServerRule.getServerPort()));
            }
        });
        component.setConfiguration(configuration);
        context.addComponent("google-sheets-stream", component);

        return context;
    }
}
