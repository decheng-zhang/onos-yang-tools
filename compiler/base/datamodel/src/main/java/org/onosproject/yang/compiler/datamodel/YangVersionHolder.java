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

package org.onosproject.yang.compiler.datamodel;

/**
 * Abstraction of YANG version entity. It is used categories the node which
 * can hold the YANG version
 */
public interface YangVersionHolder {

    /**
     * Returns the version.
     *
     * @return the version
     */
    String getVersion();

    /**
     * Returns the augmented node schema info for given class canonical name.
     *
     * @param s class canonical name
     * @return augmented node schema info
     * @throws IllegalArgumentException when provided class canonical name is
     *                                  not valid augment node path
     */
    AugmentedSchemaInfo getAugmentedSchemaInfo(String s)
            throws IllegalArgumentException;
}
