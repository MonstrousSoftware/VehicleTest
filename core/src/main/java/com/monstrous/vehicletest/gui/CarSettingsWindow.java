package com.monstrous.vehicletest.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.monstrous.vehicletest.Settings;
import com.monstrous.vehicletest.BWorld;
import com.monstrous.vehicletest.World;

public class CarSettingsWindow extends Window {


    public CarSettingsWindow(String title, Skin skin, World world) {
        super(title, skin);


        final Label labelCarMassValue = new Label(String.valueOf(Settings.carMass), skin);
        final Slider sliderCarMass = new Slider(1, 500, 1, false, skin);
        sliderCarMass.setAnimateDuration(0.1f);
        sliderCarMass.setValue(Settings.carMass);
        sliderCarMass.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.carMass = sliderCarMass.getValue();
                labelCarMassValue.setText(String.valueOf(Settings.carMass));
                world.rebuild();
            }
        });


        add(new Label("Mass: ", skin)).pad(5);
        add(sliderCarMass);
        add(labelCarMassValue).width(50);
        row();

        final Label labelStiffnessValue = new Label(String.valueOf(Settings.suspensionStiffness), skin);
        final Slider sliderStiffness = new Slider(1, 1500, 10, false, skin);
        sliderStiffness.setAnimateDuration(0.1f);
        sliderStiffness.setValue(Settings.suspensionStiffness);
        sliderStiffness.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.suspensionStiffness = sliderStiffness.getValue();
                labelStiffnessValue.setText(String.valueOf(Settings.suspensionStiffness));
                world.rebuild();
            }
        });


        add(new Label("Stiffness: ", skin)).pad(5);
        add(sliderStiffness);
        add(labelStiffnessValue).width(50);
        row();

        final Label labelDampingValue = new Label(String.valueOf(Settings.suspensionDamping), skin);
        final Slider sliderDamping = new Slider(0, 50, 1, false, skin);
        sliderDamping.setAnimateDuration(0.1f);
        sliderDamping.setValue(Settings.suspensionDamping);
        sliderDamping.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.suspensionDamping = sliderDamping.getValue();
                labelDampingValue.setText(String.valueOf(Settings.suspensionDamping));
                world.rebuild();
            }
        });


        add(new Label("Damping: ", skin)).pad(5);
        add(sliderDamping);
        add(labelDampingValue).width(50);
        row();

        final Label labelCompressionValue = new Label(String.valueOf(Settings.suspensionCompression), skin);
        final Slider sliderCompression = new Slider(0, 50, 1, false, skin);
        sliderCompression.setAnimateDuration(0.1f);
        sliderCompression.setValue(Settings.suspensionCompression);
        sliderCompression.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.suspensionCompression = sliderCompression.getValue();
                labelCompressionValue.setText(String.valueOf(Settings.suspensionCompression));
                world.rebuild();
            }
        });


        add(new Label("Compression: ", skin)).pad(5);
        add(sliderCompression);
        add(labelCompressionValue).width(50);
        row();

        final Label labelRestValue = new Label(String.valueOf(Settings.suspensionRestLength), skin);
        final Slider sliderRest = new Slider(0.1f, 2.0f, 0.1f, false, skin);
        sliderRest.setAnimateDuration(0.1f);
        sliderRest.setValue(Settings.suspensionRestLength);
        sliderRest.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.suspensionRestLength = sliderRest.getValue();
                labelRestValue.setText(String.valueOf(Settings.suspensionRestLength));
                world.rebuild();
            }
        });


        add(new Label("Rest Length: ", skin)).pad(5);
        add(sliderRest);
        add(labelRestValue).width(50);
        row();

        final Label labelEngineForceValue = new Label(String.valueOf(Settings.engineForce), skin);
        final Slider sliderEngineForce = new Slider(1, 100, 1, false, skin);
        sliderEngineForce.setAnimateDuration(0.1f);
        sliderEngineForce.setValue(Settings.engineForce);
        sliderEngineForce.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.engineForce = sliderEngineForce.getValue();
                labelEngineForceValue.setText(String.valueOf(Settings.engineForce));
            }
        });


        add(new Label("Engine Force: ", skin)).pad(5);
        add(sliderEngineForce);
        add(labelEngineForceValue).width(50);
        row();


        CheckBox cb = new CheckBox("Debug view", skin);
        cb.setChecked(world.showDebug);
        cb.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                world.showDebug = cb.isChecked();
            }
        });


        add(cb).colspan(3).pad(5);
        row();

        pack();

    }
}
