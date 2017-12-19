/*
 * Copyright 2017-present Open Networking Foundation
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

import org.onosproject.yang.compiler.datamodel.YangSchemaNode;

import static org.onosproject.yang.compiler.datamodel.utils.DataModelUtils.DOT_REGEX;
import static org.onosproject.yang.compiler.datamodel.utils.DataModelUtils.INVAL_ANYDATA;
import static org.onosproject.yang.compiler.datamodel.utils.DataModelUtils.QNAME_PRE;
import static org.onosproject.yang.compiler.datamodel.utils.DataModelUtils.errorMsg;
import static org.onosproject.yang.compiler.datamodel.utils.DataModelUtils.getTargetNode;
import static org.onosproject.yang.runtime.RuntimeHelper.PERIOD;
import static org.onosproject.yang.runtime.impl.UtilsConstants.REV_REGEX;

public final class AnydataHandler {

    /**
     * Prevent creation of anydataHandler.
     */
    private AnydataHandler() {
    }

    /**
     * Returns schema node corresponding to a given class.
     *
     * @param c   generated class
     * @param reg YANG model registry
     * @return module schema node
     * @throws IllegalArgumentException when provided identifier is not
     *                                  not valid
     */
    public static YangSchemaNode getSchemaNode(Class c,
                                               DefaultYangModelRegistry reg) {
        String cn = c.getCanonicalName();
        String[] paths = cn.split(DOT_REGEX);
        // index 6 always we the revision in the given class path if path
        // contains the revision
        int index = 6;
        if (paths[index].matches(REV_REGEX)) {
            index++;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(QNAME_PRE);
        for (int i = 4; i <= index; i++) {
            sb.append(PERIOD);
            sb.append(paths[i]);
        }
        YangSchemaNode module = reg.getForRegClassQualifiedName(sb.toString(),
                                                                false);

        if (module == null) {
            throw new IllegalArgumentException(errorMsg(INVAL_ANYDATA, c));
        }

        YangSchemaNode targetNode = getTargetNode(cn, module, index + 1);
        return targetNode;
    }
}