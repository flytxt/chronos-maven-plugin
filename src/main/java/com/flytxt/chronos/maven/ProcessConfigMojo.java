/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Flytxt Mobile Solutions
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
        final ChronosApp app = Utils.readApp(sourceChronosConfigFile);
        if (name != null) {
            app.setName(name);
        }
        app.getContainer().setImage(image);
        Utils.writeApp(app, finalChronosConfigFile);
    }

}
