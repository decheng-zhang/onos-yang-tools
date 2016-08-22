/*
 * Copyright 2016-present Open Networking Laboratory
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

package org.onosproject.yangutils.datamodel.javadatamodel;

import org.onosproject.yangutils.datamodel.YangLeaf;

/**
 * Represent YANG java leaf.
 */
public class YangJavaLeaf
        extends YangLeaf
        implements HasJavaQualifiedTypeInfo {

    private static final long serialVersionUID = 208201617L;

    protected JavaQualifiedTypeInfo javaQualifiedTypeInfo;

    /**
     * Returns java qualified type info.
     *
     * @return java qualified type info
     */
    @Override
    public JavaQualifiedTypeInfo getJavaQualifiedTypeInfo() {
        return javaQualifiedTypeInfo;
    }


    @Override
    public String getJavaPackage() {
        return getJavaQualifiedTypeInfo().getPkgInfo();
    }

    @Override
    public String getJavaClassNameOrBuiltInType() {
        return getJavaQualifiedTypeInfo().getClassInfo();
    }
}