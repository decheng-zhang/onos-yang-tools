/*
 * Copyright 2016-present Open Networking Foundation
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
package org.onosproject.yang.compiler.translator.tojava.javamodel;

import org.onosproject.yang.compiler.datamodel.javadatamodel.YangJavaIdentity;
import org.onosproject.yang.compiler.translator.exception.TranslatorException;
import org.onosproject.yang.compiler.translator.tojava.JavaCodeGenerator;
import org.onosproject.yang.compiler.translator.tojava.JavaCodeGeneratorInfo;
import org.onosproject.yang.compiler.translator.tojava.JavaFileInfoTranslator;
import org.onosproject.yang.compiler.translator.tojava.JavaImportData;
import org.onosproject.yang.compiler.translator.tojava.JavaQualifiedTypeInfoTranslator;
import org.onosproject.yang.compiler.translator.tojava.TempJavaCodeFragmentFiles;
import org.onosproject.yang.compiler.utils.io.YangPluginConfig;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.onosproject.yang.compiler.translator.tojava.GeneratedJavaFileType.GENERATE_IDENTITY_CLASS;
import static org.onosproject.yang.compiler.translator.tojava.GeneratedJavaFileType.GENERATE_INTERFACE_WITH_BUILDER;
import static org.onosproject.yang.compiler.translator.tojava.YangJavaModelUtils.updatePackageInfo;
import static org.onosproject.yang.compiler.translator.tojava.utils.JavaFileGeneratorUtils.getFileObject;
import static org.onosproject.yang.compiler.translator.tojava.utils.JavaFileGeneratorUtils.initiateJavaFileGeneration;
import static org.onosproject.yang.compiler.translator.tojava.utils.JavaIdentifierSyntax.createPackage;
import static org.onosproject.yang.compiler.translator.tojava.utils.MethodsGenerator.getFromStringMethodForIdentity;
import static org.onosproject.yang.compiler.translator.tojava.utils.MethodsGenerator.getToStringMethodForIdentity;
import static org.onosproject.yang.compiler.translator.tojava.utils.TranslatorErrorType.FAIL_AT_ENTRY;
import static org.onosproject.yang.compiler.translator.tojava.utils.TranslatorErrorType.FAIL_AT_EXIT;
import static org.onosproject.yang.compiler.translator.tojava.utils.TranslatorUtils.getErrorMsg;
import static org.onosproject.yang.compiler.utils.UtilConstants.CLOSE_CURLY_BRACKET;
import static org.onosproject.yang.compiler.utils.UtilConstants.EMPTY_STRING;
import static org.onosproject.yang.compiler.utils.UtilConstants.JAVA_FILE_EXTENSION;
import static org.onosproject.yang.compiler.utils.io.impl.FileSystemUtil.closeFile;
import static org.onosproject.yang.compiler.utils.io.impl.YangIoUtils.formatFile;
import static org.onosproject.yang.compiler.utils.io.impl.YangIoUtils.getCapitalCase;
import static org.onosproject.yang.compiler.utils.io.impl.YangIoUtils.insertDataIntoJavaFile;

/**
 * Represents input information extended to support java code generation.
 */
public class YangJavaIdentityTranslator extends YangJavaIdentity
        implements JavaCodeGeneratorInfo, JavaCodeGenerator {

    //Contains the information of the imported.
    private transient JavaImportData importData;

    /**
     * File handle to maintain temporary java code fragments as per the code
     * snippet types.
     */
    private transient TempJavaCodeFragmentFiles tempFileHandle;

    /**
     * Creates YANG java container object.
     */
    public YangJavaIdentityTranslator() {
        setJavaFileInfo(new JavaFileInfoTranslator());
        getJavaFileInfo().setGeneratedFileTypes(GENERATE_INTERFACE_WITH_BUILDER);
        importData = new JavaImportData();
    }

    /**
     * Returns the generated java file information.
     *
     * @return generated java file information
     */
    @Override
    public JavaFileInfoTranslator getJavaFileInfo() {
        if (javaFileInfo == null) {
            throw new TranslatorException("Missing java info in java identity" +
                                                  " node " + getName());
        }
        return (JavaFileInfoTranslator) javaFileInfo;
    }

    /**
     * Sets the java file info object.
     *
     * @param javaInfo java file info object
     */
    @Override
    public void setJavaFileInfo(JavaFileInfoTranslator javaInfo) {
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
     * container info.
     *
     * @param yangPlugin YANG plugin config
     * @throws TranslatorException translator operation fail
     */
    @Override
    public void generateCodeEntry(YangPluginConfig yangPlugin) throws TranslatorException {
        try {

            updatePackageInfo(this, yangPlugin);
            JavaQualifiedTypeInfoTranslator basePkgInfo =
                    new JavaQualifiedTypeInfoTranslator();
            JavaFileInfoTranslator itsInfo = getJavaFileInfo();
            String name = itsInfo.getJavaName();
            String className = getCapitalCase(name);
            String path = itsInfo.getPackageFilePath();
            createPackage(this);
            List<String> imports = null;
            boolean isQualified;

            if (getBaseNode() != null && getBaseNode().getReferredIdentity() != null) {
                if (!(getBaseNode().getReferredIdentity() instanceof YangJavaIdentityTranslator)) {
                    throw new TranslatorException(getErrorMsg(FAIL_AT_ENTRY, this,
                                                              EMPTY_STRING));
                }
                YangJavaIdentityTranslator base =
                        (YangJavaIdentityTranslator) getBaseNode().getReferredIdentity();
                JavaFileInfoTranslator info = base.getJavaFileInfo();
                String baseClassName = getCapitalCase(info.getJavaName());
                String basePkg = info.getPackage();
                basePkgInfo.setClassInfo(baseClassName);
                basePkgInfo.setPkgInfo(basePkg);
                isQualified = importData.addImportInfo(basePkgInfo, className,
                                                       javaFileInfo.getPackage());
                if (!isQualified) {
                    imports = importData.getImports(true);
                }
            }

            File file = getFileObject(path, className, JAVA_FILE_EXTENSION, itsInfo);

            initiateJavaFileGeneration(file, GENERATE_IDENTITY_CLASS, imports, this, className);
            //Add to string and from string method to class
            addStringMethodsToClass(file, name);
            insertDataIntoJavaFile(file, CLOSE_CURLY_BRACKET);
            formatFile(file);
            closeFile(file, false);
        } catch (IOException e) {
            throw new TranslatorException(getErrorMsg(FAIL_AT_EXIT, this,
                                                      e.getLocalizedMessage()));
        }
    }

    private void addStringMethodsToClass(File file, String className) throws IOException {
        insertDataIntoJavaFile(file, getToStringMethodForIdentity(getName()));
        insertDataIntoJavaFile(file, getFromStringMethodForIdentity(
                className, getName()));
    }

    /**
     * Create a java file using the YANG container info.
     *
     * @throws TranslatorException translator operation fail
     */
    @Override
    public void generateCodeExit() throws TranslatorException {
        /* Do nothing, file is already generated in entry*/
    }
}

