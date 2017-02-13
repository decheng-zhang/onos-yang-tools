/*
 * Copyright 2017-present Open Networking Laboratory
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

package org.onosproject.yang.runtime.impl;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.onosproject.yang.model.DataNode;
import org.onosproject.yang.model.ResourceId;
import org.onosproject.yang.runtime.helperutils.HelperContext;

import static org.onosproject.yang.runtime.helperutils.SerializerHelper.addDataNode;
import static org.onosproject.yang.runtime.helperutils.SerializerHelper.addToResourceId;
import static org.onosproject.yang.runtime.helperutils.SerializerHelper.getResourceId;
import static org.onosproject.yang.runtime.helperutils.SerializerHelper.initializeDataNode;
import static org.onosproject.yang.runtime.helperutils.SerializerHelper.initializeResourceId;
import static org.onosproject.yang.runtime.impl.TestUtils.validateResourceId;

/**
 * Tests the serializer helper methods.
 */

@FixMethodOrder(MethodSorters.DEFAULT)
public class AddToDataNodeRidTest {

    public static final String LNS = "yrt:list";

    TestYangSerializerContext context = new TestYangSerializerContext();

    /*
     * Reference for data node info.
     */
    HelperContext info;

    /*
     * Reference for data node builder.
     */
    DataNode.Builder dBlr;

    /*
     * Reference for resource id.
     */
    ResourceId id;

    /*
     * Reference for the value.
     */
    String value;

    /*
     * Reference for string array to used for resource id testing.
     */
    String[] nA;
    String[] nsA;
    String[] valA;

    /**
     * Test add to data node after initializing it with resource builder.
     */
    @Test
    public void addToDataListTest() {

        ResourceId.Builder rIdBlr = initializeResourceId(context);
        rIdBlr = addToResourceId(rIdBlr, "l1", LNS, value);
        rIdBlr = addToResourceId(rIdBlr, "c1", LNS, value);
        dBlr = initializeDataNode(rIdBlr);
        value = "0";
        dBlr = addDataNode(dBlr, "l1", null, value, null);
        info = (HelperContext) dBlr.appInfo();
        id = getResourceId(dBlr);

        //Tree validation
        nA = new String[]{"/", "l1", "c1", "l1", ""};
        nsA = new String[]{null, LNS, LNS, LNS, ""};
        valA = new String[]{"0", ""};
        validateResourceId(nA, nsA, valA, id);
    }
}
