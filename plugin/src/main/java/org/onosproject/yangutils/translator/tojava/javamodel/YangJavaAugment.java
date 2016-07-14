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
package org.onosproject.yangutils.translator.tojava.javamodel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.onosproject.yangutils.datamodel.YangAugment;
import org.onosproject.yangutils.datamodel.YangNodeIdentifier;
import org.onosproject.yangutils.translator.exception.TranslatorException;
import org.onosproject.yangutils.translator.tojava.JavaCodeGenerator;
import org.onosproject.yangutils.translator.tojava.JavaCodeGeneratorInfo;
import org.onosproject.yangutils.translator.tojava.JavaFileInfo;
import org.onosproject.yangutils.translator.tojava.JavaQualifiedTypeInfo;
import org.onosproject.yangutils.translator.tojava.TempJavaCodeFragmentFiles;
import org.onosproject.yangutils.utils.io.impl.YangPluginConfig;

import static org.onosproject.yangutils.translator.tojava.GeneratedJavaFileType.GENERATE_INTERFACE_WITH_BUILDER;
import static org.onosproject.yangutils.translator.tojava.YangJavaModelUtils.generateCodeOfAugmentableNode;
import static org.onosproject.yangutils.utils.io.impl.YangIoUtils.getCamelCase;
import static org.onosproject.yangutils.utils.io.impl.YangIoUtils.getCapitalCase;

/**
 * Represents augment information extended to support java code generation.
 */
public class YangJavaAugment
        extends YangAugment
        implements JavaCodeGeneratorInfo, JavaCodeGenerator {

    private static final long serialVersionUID = 806201632L;

    /**
     * Prefix to be added to generated java file for augment node.
     */
    private static final String AUGMENTED = "Augmented";

    /**
     * Contains the information of the java file being generated.
     */
    private JavaFileInfo javaFileInfo;

    /**
     * TargetNodes java qualified info.
     */
    private List<JavaQualifiedTypeInfo> extendedClassInfo;

    /**
     * File handle to maintain temporary java code fragments as per the code
     * snippet types.
     */
    private transient TempJavaCodeFragmentFiles tempFileHandle;

    /**
     * Creates a YANG java augment object.
     */
    public YangJavaAugment() {
        super();
        setJavaFileInfo(new JavaFileInfo());
        setExtendedClassInfo(new ArrayList<>());
        getJavaFileInfo().setGeneratedFileTypes(GENERATE_INTERFACE_WITH_BUILDER);
    }

    /**
     * Returns the generated java file information.
     *
     * @return generated java file information
     */
    @Override
    public JavaFileInfo getJavaFileInfo() {

        if (javaFileInfo == null) {
            throw new TranslatorException("Missing java info in java datamodel node");
        }
        return javaFileInfo;
    }

    /**
     * Sets the java file info object.
     *
     * @param javaInfo java file info object
     */
    @Override
    public void setJavaFileInfo(JavaFileInfo javaInfo) {
        javaFileInfo = javaInfo;
    }

    /**
     * Returns the temporary file handle.
     *
     * @return temporary file handle
     */
    @Override
    public TempJavaCodeFragmentFiles getTempJavaCodeFragmentFiles() {
        return tempFileHandle;
    }

    /**
     * Sets temporary file handle.
     *
     * @param fileHandle temporary file handle
     */
    @Override
    public void setTempJavaCodeFragmentFiles(TempJavaCodeFragmentFiles fileHandle) {
        tempFileHandle = fileHandle;
    }

    /**
     * Prepare the information for java code generation corresponding to YANG
     * augment info.
     *
     * @param yangPlugin YANG plugin config
     * @throws TranslatorException translator operation fail
     */
    @Override
    public void generateCodeEntry(YangPluginConfig yangPlugin) throws TranslatorException {
        try {
            generateCodeOfAugmentableNode(this, yangPlugin);
        } catch (IOException e) {
            throw new TranslatorException("Failed to generate code for augmentable node " + getName());
        }
    }

    /**
     * Create a java file using the YANG augment info.
     *
     * @throws TranslatorException when failed to do translator operations
     */
    @Override
    public void generateCodeExit() throws TranslatorException {
        try {
            getTempJavaCodeFragmentFiles().generateJavaFile(GENERATE_INTERFACE_WITH_BUILDER, this);
        } catch (IOException e) {
            throw new TranslatorException("Failed to generate code for augmentable node " + getName());
        }
    }

    /**
     * Returns augment class name.
     *
     * @return augment class name
     */
    public String getAugmentClassName() {
        YangNodeIdentifier nodeId = getTargetNode().get(getTargetNode().size() - 1).getNodeIdentifier();
        String name = getCapitalCase(getCamelCase(nodeId.getName(), null));
        if (nodeId.getPrefix() != null) {
            return AUGMENTED + getCapitalCase(nodeId.getPrefix()) + name;
        } else {
            return AUGMENTED + name;
        }
    }

    /**
     * Returns extended class info.
     *
     * @return extended class info
     */
    public List<JavaQualifiedTypeInfo> getExtendedClassInfo() {
        return extendedClassInfo;
    }

    /**
     * Sets extended class info.
     *
     * @param augmentedInfo extended class info
     */
    private void setExtendedClassInfo(List<JavaQualifiedTypeInfo> augmentedInfo) {
        extendedClassInfo = augmentedInfo;
    }

    /**
     * Adds to extended class info list.
     *
     * @param augmentedInfo extended class info
     */
    public void addToExtendedClassInfo(JavaQualifiedTypeInfo augmentedInfo) {
        getExtendedClassInfo().add(augmentedInfo);
    }

}
