chronos-maven-plugin
=====================

Maven plugin for interacting with Chronos

It can both process `chronos.json` config and use it to push it to Chronos host for deployment. 

# Maven documentation

For detailed documentation of goals and configuration options run in your project:
`mvn com.flytxt:chronos-maven-plugin:help -Ddetail=true`

# Basic usage

This plugin plays well with [docker-maven-plugin](https://github.com/spotify/docker-maven-plugin)
so you might want to use it e.g. to include proper versioning of Docker `image` in `chronos.json`.

Add plugin configuration to your `pom.xml`:

```xml
<plugin>
	<groupId>com.flytxt</groupId>
	<artifactId>chronos-maven-plugin</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<configuration>
		<image>${docker-image-prefix}/${project.build.finalName}:${project.version}-${gitShortCommitId}</image>
		<chronosHost>${chronos.url}</chronosHost>
	</configuration>
	<executions>
		<execution>
			<id>processConfig</id>
			<phase>install</phase>
			<goals>
				<goal>processConfig</goal>
			</goals>
		</execution>
		<execution>
			<id>deploy</id>
			<phase>deploy</phase>
			<goals>
				<goal>deploy</goal>
			</goals>
		</execution>
	</executions>
</plugin>
```

By default your template `chronos.json` should be in the root project directory.

Inspired by [marathon-maven-plugin](https://github.com/holidaycheck/marathon-maven-plugin)