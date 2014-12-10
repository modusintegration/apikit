/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.apikit;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.sonatype.plexus.build.incremental.DefaultBuildContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class CreateMojoTest extends AbstractMojoTestCase {
    private CreateMojo mojo;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private File src;
    private File main;
    private File app;
    private File api;
    private File lala;
    private File project;
    private File apiFile;
    private File resources;
    private File mappingsFile;
    private File generatedFlowFile;
    
    @Before
    public void setUp() throws Exception {
        mojo = new CreateMojo();

        project = folder.newFolder("my-project");
        src = new File(project, "src");
        main = new File(src, "main");
        app = new File(main, "app");
        api = new File(main, "api");
        resources = new File(main, "resources");
        lala = new File(api, "lala");
        generatedFlowFile = new File(app, "hello.xml");
        
        api.mkdirs();
        app.mkdirs();
        resources.mkdir();
        lala.mkdirs();
        
        mappingsFile = new File(resources, "commareamappings.json");
        mappingsFile.createNewFile();
        
        // Do
        apiFile = new File(api, "hello.yaml");
        apiFile.createNewFile();
        new File(api, "bye.yml").createNewFile();
        new File(lala, "wow.yaml").createNewFile();

        // Don't
        new File(main, "dont-read.yaml").createNewFile();

        // TODO mock properties like this:
        setVariableValueToObject(mojo, "buildContext", new DefaultBuildContext());
        setVariableValueToObject(mojo, "log", mock(Log.class));
        
        
    }

    @Test
    public void testGetIncludedFiles() throws Exception {
        List<String> files = mojo.getIncludedFiles(project, new String[]{"src/main/api/**/*.yaml", "src/main/**/*.yml"},
                new String[]{});
        HashSet<String> set = new HashSet<String>(files);

        assertTrue(set.contains(new File(project, "src/main/api/hello.yaml").getAbsolutePath()));
        assertTrue(set.contains(new File(project, "src/main/api/bye.yml").getAbsolutePath()));
        assertTrue(set.contains(new File(project, "src/main/api/lala/wow.yaml").getAbsolutePath()));
        assertFalse(set.contains(new File(project, "src/main/dont-read.yaml").getAbsolutePath()));
        assertEquals(3, files.size());
    }

    @Test
    public void testExecute() throws  Exception {
        setVariableValueToObject(mojo, "muleXmlDirectory", app);
        setVariableValueToObject(mojo, "muleXmlOutputDirectory", app);
        setVariableValueToObject(mojo, "specDirectory", project);

        IOUtils.copy(this.getClass().getClassLoader().getResourceAsStream("create-mojo/simple.yaml"),
                new FileOutputStream(apiFile));

        mojo.execute();

        assertTrue(apiFile.exists());
        FileInputStream input = new FileInputStream(apiFile);
        String s = IOUtils.toString(input);
        input.close();

        assertTrue(s.length() > 0);
        
        // check generated content
        assertTrue(generatedFlowFile.exists());
        FileInputStream generatedStream = new FileInputStream(generatedFlowFile);
        String generated = IOUtils.toString(generatedStream);
        generatedStream.close();
        
        assertTrue(generated.length() > 0);
        //TODO Assert content
    }
    
    @Test
    public void testMappings() throws  Exception {
        setVariableValueToObject(mojo, "muleXmlDirectory", app);
        setVariableValueToObject(mojo, "muleXmlOutputDirectory", app);
        setVariableValueToObject(mojo, "specDirectory", project);

        IOUtils.copy(this.getClass().getClassLoader().getResourceAsStream("create-mojo/zz90com1.yaml"),
                new FileOutputStream(apiFile));
        IOUtils.copy(this.getClass().getClassLoader().getResourceAsStream("create-mojo/zz90com1_commareamappings.json"),
        		new FileOutputStream(mappingsFile));
        setVariableValueToObject(mojo, "commareaMappingsSrc", mappingsFile);
  
        mojo.execute();

        assertTrue(apiFile.exists());
        FileInputStream input = new FileInputStream(apiFile);
        String s = IOUtils.toString(input);
        input.close();

        assertTrue(s.length() > 0);
        
        // check generated content
        assertTrue(generatedFlowFile.exists());
        FileInputStream generatedStream = new FileInputStream(generatedFlowFile);
        String generated = IOUtils.toString(generatedStream);
        generatedStream.close();
        
        assertTrue(generated.length() > 0);
        
        //TODO MAPPING not nice
        String flowXml = generated.substring(generated.indexOf("<flow name=\"post:/zz90com1:hello-config\">"), generated.indexOf("</mule>") ).trim();
        
        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = XMLUnit.compareXML("<flow name=\"post:/zz90com1:hello-config\">\n"
        		+ "    <json:json-to-object-transformer xmlns:json=\"http://www.mulesoft.org/schema/mule/json\" returnClass=\"com.gap.cobol.zz90com1.CaZz90PgmCommarea\" />\n"
        		+ "    <message-properties-transformer xmlns=\"\" name=\"Message Properties\">\n"
        		+ "        <add-message-property key=\"AbstractJavaTransformer\" value=\"com.gap.cobol.zz90com1.bind.CaZz90PgmCommareaJavaToHostTransformer\" />\n"
        		+ "    </message-properties-transformer>\n"
        		+ "</flow>\n", flowXml);

        assertTrue(diff.toString(), diff.similar());
        
        
    }
    

}
