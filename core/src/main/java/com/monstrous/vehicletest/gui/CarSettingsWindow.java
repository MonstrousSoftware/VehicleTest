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



        final Label labelCarWValue = new Label(String.valueOf(Settings.chassisWidth), skin);
        final Slider sliderCarW = new Slider(1, 3, 0.1f, false, skin);
        sliderCarW.setAnimateDuration(0.1f);
        sliderCarW.setValue(Settings.chassisWidth);
        sliderCarW.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.chassisWidth = sliderCarW.getValue();
                labelCarWValue.setText(String.valueOf(Settings.chassisWidth));
                world.rebuild();
            }
        });
        add(new Label("Width: ", skin)).pad(5);
        add(sliderCarW);
        add(labelCarWValue).width(50);
        row();

        final Label labelCarHValue = new Label(String.valueOf(Settings.chassisHeight), skin);
        final Slider sliderCarH = new Slider(1, 3, 0.1f, false, skin);
        sliderCarH.setAnimateDuration(0.1f);
        sliderCarH.setValue(Settings.chassisHeight);
        sliderCarH.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.chassisHeight = sliderCarH.getValue();
                labelCarHValue.setText(String.valueOf(Settings.chassisHeight));
                world.rebuild();
            }
        });
        add(new Label("Height: ", skin)).pad(5);
        add(sliderCarH);
        add(labelCarHValue).width(50);
        row();

        final Label labelCarLValue = new Label(String.valueOf(Settings.chassisLength), skin);
        final Slider sliderCarL = new Slider(1, 10, 0.1f, false, skin);
        sliderCarL.setAnimateDuration(0.1f);
        sliderCarL.setValue(Settings.chassisLength);
        sliderCarL.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.chassisLength = sliderCarL.getValue();
                labelCarLValue.setText(String.valueOf(Settings.chassisLength));
                world.rebuild();
            }
        });
        add(new Label("Length: ", skin)).pad(5);
        add(sliderCarL);
        add(labelCarLValue).width(50);
        row();

        final Label labelWheelDownValue = new Label(String.valueOf(Settings.wheelDown), skin);
        final Slider sliderWD = new Slider(-3f, 0f, 0.1f, false, skin);
        sliderWD.setAnimateDuration(0.1f);
        sliderWD.setValue(Settings.wheelDown);
        sliderWD.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.wheelDown = sliderWD.getValue();
                labelWheelDownValue.setText(String.valueOf(Settings.wheelDown));
                world.rebuild();
            }
        });
        add(new Label("Suspension length: ", skin)).pad(5);
        add(sliderWD);
        add(labelWheelDownValue).width(50);
        row();


        final Label labelCarMassValue = new Label(String.valueOf(Settings.chassisMass), skin);
        final Slider sliderCarMass = new Slider(1, 500, 1, false, skin);
        sliderCarMass.setAnimateDuration(0.1f);
        sliderCarMass.setValue(Settings.chassisMass);
        sliderCarMass.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.chassisMass = sliderCarMass.getValue();
                labelCarMassValue.setText(String.valueOf(Settings.chassisMass));
                world.rebuild();
            }
        });


        add(new Label("Mass: ", skin)).pad(5);
        add(sliderCarMass);
        add(labelCarMassValue).width(50);
        row();

        final Label labelStiffnessValue = new Label(String.valueOf(Settings.suspensionCFM), skin);
        final Slider sliderStiffness = new Slider(0, 1, 0.01f, false, skin);
        sliderStiffness.setAnimateDuration(0.1f);
        sliderStiffness.setValue(Settings.suspensionCFM);
        sliderStiffness.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.suspensionCFM = sliderStiffness.getValue();
                labelStiffnessValue.setText(String.valueOf(Settings.suspensionCFM));
                world.rebuild();
            }
        });


        add(new Label("CFM: ", skin)).pad(5);
        add(sliderStiffness);
        add(labelStiffnessValue).width(50);
        row();

        final Label labelDampingValue = new Label(String.valueOf(Settings.suspensionERP), skin);
        final Slider sliderDamping = new Slider(0, 1, 0.01f, false, skin);
        sliderDamping.setAnimateDuration(0.1f);
        sliderDamping.setValue(Settings.suspensionERP);
        sliderDamping.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.suspensionERP = sliderDamping.getValue();
                labelDampingValue.setText(String.valueOf(Settings.suspensionERP));
                world.rebuild();
            }
        });


        add(new Label("ERP: ", skin)).pad(5);
        add(sliderDamping);
        add(labelDampingValue).width(50);
        row();
//
//        final Label labelCompressionValue = new Label(String.valueOf(Settings.suspensionCompression), skin);
//        final Slider sliderCompression = new Slider(0, 50, 1, false, skin);
//        sliderCompression.setAnimateDuration(0.1f);
//        sliderCompression.setValue(Settings.suspensionCompression);
//        sliderCompression.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                Settings.suspensionCompression = sliderCompression.getValue();
//                labelCompressionValue.setText(String.valueOf(Settings.suspensionCompression));
//                world.rebuild();
//            }
//        });
//
//
//        add(new Label("Compression: ", skin)).pad(5);
//        add(sliderCompression);
//        add(labelCompressionValue).width(50);
//        row();
//
//        final Label labelRestValue = new Label(String.valueOf(Settings.suspensionRestLength), skin);
//        final Slider sliderRest = new Slider(0.1f, 2.0f, 0.1f, false, skin);
//        sliderRest.setAnimateDuration(0.1f);
//        sliderRest.setValue(Settings.suspensionRestLength);
//        sliderRest.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                Settings.suspensionRestLength = sliderRest.getValue();
//                labelRestValue.setText(String.valueOf(Settings.suspensionRestLength));
//                world.rebuild();
//            }
//        });
//
//
//        add(new Label("Rest Length: ", skin)).pad(5);
//        add(sliderRest);
//        add(labelRestValue).width(50);
//        row();

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

        Button reset = new TextButton("Reset", skin);
        reset.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                world.rebuild();
            }
        });
        add(reset).colspan(3).pad(5);
        row();

        pack();

    }
}
