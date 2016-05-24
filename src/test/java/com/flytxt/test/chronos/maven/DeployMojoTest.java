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

import java.io.FileNotFoundException;

import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.configuration.DefaultPlexusConfiguration;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.flytxt.chronos.maven.ChronosApp;
import com.flytxt.chronos.maven.DeployMojo;
import com.flytxt.chronos.maven.Utils;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import com.squareup.okhttp.mockwebserver.rule.MockWebServerRule;

/**
 * The DeployMojoTest class
 *
 * @author gazal
 *
 */
public class DeployMojoTest extends AbstractChronosMojoTestWithJUnit4 {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Rule
    public final MockWebServerRule server = new MockWebServerRule();

    private String getChronosHost() {
        return server.getUrl("").toString();
    }

    private DeployMojo lookupDeployMojo(final String chronosFile) throws Exception {
        final PlexusConfiguration pluginCfg = new DefaultPlexusConfiguration("configuration");
        pluginCfg.addChild("chronosHost", getChronosHost());
        pluginCfg.addChild("finalChronosConfigFile", chronosFile);
        return (DeployMojo) lookupChronosMojo("deploy", pluginCfg);
    }

    private DeployMojo lookupDeployMojo() throws Exception {
        return lookupDeployMojo(getTestChronosConfigFile());
    }

    @Test
    public void testSuccessfulDeployApp() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(204));

        final DeployMojo mojo = lookupDeployMojo();
        assertNotNull(mojo);

        mojo.execute();

        assertEquals(1, server.getRequestCount());

        final RecordedRequest createAppRequest = server.takeRequest();
        assertEquals(Utils.API_PATH, createAppRequest.getPath());
        assertEquals("POST", createAppRequest.getMethod());
        final ChronosApp requestApp = Utils.OBJECT_MAPPER.readValue(createAppRequest.getBody().readUtf8(), ChronosApp.class);
        assertNotNull(requestApp);
    }

    @Test
    public void testDeployFailedDueToMissingChronosConfigFile() throws Exception {
        thrown.expect(MojoExecutionException.class);
        thrown.expectCause(CoreMatchers.isA(FileNotFoundException.class));

        final DeployMojo mojo = lookupDeployMojo("/invalid/path/to/chronos.json");
        assertNotNull(mojo);

        mojo.execute();

        assertEquals(0, server.getRequestCount());
    }

    @Test
    public void testDeployFailedDueToServerError() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(500));
        thrown.expect(MojoExecutionException.class);
        thrown.expectMessage("error response from Chronos, status: ");

        final DeployMojo mojo = lookupDeployMojo();
        assertNotNull(mojo);

        mojo.execute();

        assertEquals(1, server.getRequestCount());

        final RecordedRequest getAppRequest = server.takeRequest();
        assertEquals(Utils.API_PATH, getAppRequest.getPath());
        assertEquals("POST", getAppRequest.getMethod());
    }

}
