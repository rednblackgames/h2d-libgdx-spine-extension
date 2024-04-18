## HyperLap2D libGDX Spine Extension

HyperLap2D extension for libgdx runtime that adds [Spine](http://en.esotericsoftware.com/) rendering support.

### Integration

#### Gradle
![maven-central](https://img.shields.io/maven-central/v/games.rednblack.hyperlap2d/libgdx-spine-extension?color=blue&label=release)
![sonatype-nexus](https://img.shields.io/nexus/s/games.rednblack.hyperlap2d/libgdx-spine-extension?label=snapshot&server=https%3A%2F%2Foss.sonatype.org)

Extension needs to be included into your `core` project.
```groovy
dependencies {
    api "com.esotericsoftware.spine:spine-libgdx:$spineVersion"
    api "games.rednblack.hyperlap2d:libgdx-spine-extension:$h2dSpineExtension"
}
```

#### Maven
```xml
<dependency>
  <groupId>games.rednblack.editor</groupId>
  <artifactId>libgdx-spine-extension</artifactId>
  <version>0.1.4</version>
  <type>pom</type>
</dependency>
```

**Spine Runtime compatibility**

| HyperLap2D     | Spine  |
|----------------|--------|
| 0.1.5-SNAPSHOT | 4.1.0  |
| 0.1.4          | 4.1.0  |

### License
Spine is a commercial software distributed with its own license, in order to include Spine support in your project, please, be sure to have a valid [Spine License](https://github.com/EsotericSoftware/spine-runtimes)