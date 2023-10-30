# VehicleTest

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/tommyettinger/gdx-liftoff).

Vehicle physics test. Using ODE4j library rather than Bullet.
Using gdx-gltf for the rendering and to read GLTF model files.
HTML client using TeaVM extension.


## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3.
- `teavm`: Experimental web platform using TeaVM and WebGL.
## History

This builds on VehicleDemo which used Bullet and gdx-gtlf and on VehicleODE which experimented with the ODE library.

## Notes

~~I copied the org.ode4j folder into core/src/main/java. . Could this be done by linking to a library instead?~~ 

30/10/2023: 
- Replaced the copied org.ode4j (Copied from antzGames/ode4j-GWT-Compatible-libGDX) by linking in the gradle build script to
   api "com.github.antzGames:gdx-ode4j:master-SNAPSHOT"
- Updated to libgdx 1.12 and gdx-teavm 1.0.0-b6 to solve run-time errors.

