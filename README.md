## HyperLap2D libGDX Spine Extension

HyperLap2D extension for libgdx runtime that adds Spine rendering support.

### Integration

#### Gradle
Release artifacts are available through ![Bintray](https://img.shields.io/bintray/v/rednblackgames/HyperLap2D/h2d-libgdx-spine-extension) 

Extension needs to be included into your `core` project.
```groovy
dependencies {
    api "com.esotericsoftware.spine:spine-libgdx:$spineVersion"
    api "games.rednblack.editor:h2d-libgdx-spine-extension:$h2dSpineExtension"
}
```

#### Maven
```xml
<dependency>
  <groupId>games.rednblack.editor</groupId>
  <artifactId>h2d-libgdx-spine-extension</artifactId>
  <version>0.0.4</version>
  <type>pom</type>
</dependency>
```

**Spine Runtime compatibility**

| HyperLap2D         | Spine              |
| ------------------ | ------------------ |
| 0.0.5-dev          | 3.8.55.1           |
| 0.0.4              | 3.8.55.1           |
| 0.0.3              | 3.8.55.1           |

### License
Spine is a commercial software distributed with its own license, in order to include Spine support in your project, please, be sure to have a valid [Spine License](https://github.com/EsotericSoftware/spine-runtimes)