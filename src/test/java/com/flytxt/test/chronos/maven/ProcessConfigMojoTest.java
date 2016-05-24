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

import org.codehaus.plexus.configuration.DefaultPlexusConfiguration;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.junit.Test;

import com.flytxt.chronos.maven.ChronosApp;
import com.flytxt.chronos.maven.ProcessConfigMojo;
import com.flytxt.chronos.maven.Utils;

/**
 * The ProcessConfigMojoTest class
 *
 * @author gazal
 *
 */
public class ProcessConfigMojoTest extends AbstractChronosMojoTestWithJUnit4 {

    private static final String NAME = "dummyAppName";

    private static final String IMAGE = "dummyImageName";

    private String getProcessedChronosConfigFile() {
        return getTestPath("target") + "/chronos.json";
    }

    private ProcessConfigMojo lookupProcessConfigMojo() throws Exception {
        final PlexusConfiguration pluginCfg = new DefaultPlexusConfiguration("configuration");
        pluginCfg.addChild("sourceChronosConfigFile", getTestChronosConfigFile());
        pluginCfg.addChild("finalChronosConfigFile", getProcessedChronosConfigFile());
        pluginCfg.addChild("name", NAME);
        pluginCfg.addChild("image", IMAGE);
        return (ProcessConfigMojo) lookupChronosMojo("processConfig", pluginCfg);
    }

    @Test
    public void testProcessConfig() throws Exception {
        final ProcessConfigMojo mojo = lookupProcessConfigMojo();
        assertNotNull(mojo);

        mojo.execute();

        final ChronosApp app = Utils.readApp(getProcessedChronosConfigFile());
        assertNotNull(app);

        final String name = app.getName();
        assertNotNull(name);
        assertTrue(name.contains(NAME));

        final String image = app.getContainer().getImage();
        assertNotNull(image);
        assertTrue(image.contains(IMAGE));
    }

}
