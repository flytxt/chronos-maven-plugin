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
package com.flytxt.chronos.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * The ProcessConfigMojo class
 *
 * @author gazal
 *
 */
@Mojo(name = "processConfig", defaultPhase = LifecyclePhase.VERIFY)
public class ProcessConfigMojo extends AbstractChronosMojo {

    /**
     * Path to JSON file to read from when processing Chronos config. Default is ${basedir}/chronos.json
     */
    @Parameter(property = "sourceChronosConfigFile", defaultValue = "${basedir}/chronos.json")
    private String sourceChronosConfigFile;

    /**
     * Image name as specified in pom.xml.
     */
    @Parameter(property = "image", required = true)
    private String image;

    /**
     * name to use for the Chronos config.
     */
    @Parameter(property = "name", required = false)
    private String name;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("processing Chronos config file from " + sourceChronosConfigFile + " to " + finalChronosConfigFile);
        final ChronosApp app = readApp(sourceChronosConfigFile);
        if (name != null) {
            app.setName(name);
        }
        app.getContainer().setImage(image);
        writeApp(app, finalChronosConfigFile);
    }

}
