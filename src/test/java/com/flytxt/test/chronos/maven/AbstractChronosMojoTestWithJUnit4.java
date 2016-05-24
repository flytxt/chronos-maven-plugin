/**
 * © Copyright 2015 Flytxt BV. ALL RIGHTS RESERVED.
 *
 * All rights, title and interest (including all intellectual property rights) in this software and any derivative works based upon or derived from this software
 * belongs exclusively to Flytxt BV. Access to this software is forbidden to anyone except current employees of Flytxt BV and its affiliated companies who have
 * executed non-disclosure agreements explicitly covering such access. While in the employment of Flytxt BV or its affiliate companies as the case may be,
 * employees may use this software internally, solely in the course of employment, for the sole purpose of developing new functionalities, features, procedures,
 * routines, customizations or derivative works, or for the purpose of providing maintenance or support for the software. Save as expressly permitted above,
 * no license or right thereto is hereby granted to anyone, either directly, by implication or otherwise. On the termination of employment, the license granted
 * to employee to access the software shall terminate and the software should be returned to the employer, without retaining any copies.
 *
 * This software is (i) proprietary to Flytxt BV; (ii) is of significant value to it; (iii) contains trade secrets of Flytxt BV; (iv) is not publicly available;
 * and (v) constitutes the confidential information of Flytxt BV. Any use, reproduction, modification, distribution, public performance or display of this software
 * or through the use of this software without the prior, express written consent of Flytxt BV is strictly prohibited and may be in violation of applicable laws.
 */
package com.flytxt.test.chronos.maven;

import java.io.File;

import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * The AbstractChronosMojoTestWithJUnit4 class
 *
 * @author gazal
 *
 */
@RunWith(JUnit4.class)
public abstract class AbstractChronosMojoTestWithJUnit4 extends AbstractMojoTestCase {

    private String groupId;

    private String artifactId;

    private String version;

    @Before
    @Override
    public void setUp() throws Exception {
        final File pluginPom = getTestFile("pom.xml");
        final Xpp3Dom pluginPomDom = Xpp3DomBuilder.build(ReaderFactory.newXmlReader(pluginPom));
        artifactId = pluginPomDom.getChild("artifactId").getValue();
        groupId = resolveFromRootThenParent(pluginPomDom, "groupId");
        version = resolveFromRootThenParent(pluginPomDom, "version");
        super.setUp();
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    private String resolveFromRootThenParent(final Xpp3Dom pluginPomDom, final String element) throws Exception {
        Xpp3Dom elementDom = pluginPomDom.getChild(element);
        if (elementDom == null) {
            final Xpp3Dom pluginParentDom = pluginPomDom.getChild("parent");
            if (pluginParentDom != null) {
                elementDom = pluginParentDom.getChild(element);
                if (elementDom == null) {
                    throw new Exception("unable to determine " + element);
                } else {
                    return elementDom.getValue();
                }
            } else {
                throw new Exception("unable to determine " + element);
            }
        } else {
            return elementDom.getValue();
        }
    }

    protected String getTestChronosConfigFile() {
        return getClass().getResource("/chronos.json").getFile();
    }

    protected Mojo lookupChronosMojo(final String goal, final PlexusConfiguration pluginCfg) throws Exception {
        return lookupMojo(groupId, artifactId, version, goal, pluginCfg);
    }

}
