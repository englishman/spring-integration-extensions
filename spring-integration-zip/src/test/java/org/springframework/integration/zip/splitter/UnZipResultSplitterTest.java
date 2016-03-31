/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.integration.zip.splitter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("UnZipResultSplitterTest-context.xml")
public class UnZipResultSplitterTest {

    @Autowired
    private MessageChannel input;

    @Autowired
    private QueueChannel output;

    @Test
    public void splitUnZippedData() {

        final String headerName = "headerName";
        final String headerValue = "headerValue";

        Map<String, Object> payload = new HashMap<String, Object>();
        payload.put("file1", "data1");
        payload.put("file2", "data2");

        Message<?> inMessage = MessageBuilder.withPayload(payload)
                .setHeader(headerName, headerValue)
                .build();

        input.send(inMessage);

        // check message 1
        Message<?> outMessage1 = output.receive();
        assertNotNull(outMessage1);
        MessageHeaders headers1 = outMessage1.getHeaders();
        assertNotNull(headers1);
        assertEquals(headerValue, headers1.get(headerName));

        // check message 2
        Message<?> outMessage2 = output.receive();
        assertNotNull(outMessage2);
        MessageHeaders headers2 = outMessage1.getHeaders();
        assertNotNull(headers2);
        assertEquals(headerValue, headers2.get(headerName));
    }
}