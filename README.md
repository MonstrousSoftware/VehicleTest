# VehicleTest

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/tommyettinger/gdx-liftoff).

Vehicle physics test. Using ODE4j library rather than Bullet.
Using gdx-gltf for the rendering and to read GLTF model files.



## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3.
- (`teavm`: Experimental web platform using TeaVM and WebGL. : to be done later, requires ODE4j adaptation)

## History

This builds on VehicleDemo which used Bullet and gdx-gtlf and on VehicleODE which experimented with the ODE library.

## Notes

I copied the org.ode4j folder into core/src/main/java. (Copied from antzGames/ode4j-GWT-Compatible-libGDX). Could this be done by linking to a library instead? 
