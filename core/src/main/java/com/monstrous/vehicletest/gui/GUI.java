package com.monstrous.vehicletest.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.monstrous.vehicletest.UserCarController;
import com.monstrous.vehicletest.World;

public class GUI implements Disposable {

    private Skin skin;
    public Stage stage;
    private UserCarController carController;
    private Label rpmValue;
    private Label gearValue;
    private Label steerAngleValue;
    private Vector3 tmpVec = new Vector3();
    private float timer = 0;
    private CarSettingsWindow settingsWindow;

    public GUI(UserCarController carController, World world ) {
        Gdx.app.log("GUI constructor", "");
        this.carController = carController;
        skin = new Skin(Gdx.files.internal("Particle Park UI Skin/Particle Park UI.json"));
        stage = new Stage(new ScreenViewport());

        settingsWindow = new CarSettingsWindow("Car Settings", skin, world);
    }

    private void rebuild() {
        String style = "window";

        stage.clear();
        rpmValue = new Label("", skin, style);
        gearValue = new Label("", skin, style);
        steerAngleValue = new Label("", skin, style);

        Table table = new Table();
        table.top().left();               // make content move to top left
        table.setFillParent(true);        // size to match stage size


        Table stats = new Table();
        stats.setBackground(skin.getDrawable("black"));
        stats.add(new Label("RPM (W/S) : ", skin, style));
        stats.add(rpmValue);
        stats.row();
        stats.add(new Label("Gear (UP/DN) :", skin, style));
        stats.add(gearValue);
        stats.row();
        stats.add(new Label("Steer angle (A/D) : ", skin, style));
        stats.add(steerAngleValue);
        stats.row();
        stats.pack();

        table.add(stats);

        stage.addActor(table);

        Table screenTable = new Table();
        screenTable.setFillParent(true);
        screenTable.add(settingsWindow).top().right().expand();

        stage.addActor(screenTable);
    }

    private void update( float deltaTime ){
        timer -= deltaTime;
        if(timer <= 0) {
            //rpmValue.setText((int)car.rpm);
            steerAngleValue.setText((int) carController.steerAngle);
            //car.transform.getTranslation(tmpVec);
            if(carController.gear == -1)
                gearValue.setText("R");
            else if(carController.gear == 0)
                gearValue.setText("N");
            else
                gearValue.setText(carController.gear);
            timer = 0.25f;
        }
    }


    public void render(float deltaTime) {
        update(deltaTime);

        stage.act(deltaTime);
        stage.draw();
    }

    public void resize(int width, int height) {
        Gdx.app.log("GUI resize", "gui " + width + " x " + height);
        stage.getViewport().update(width, height, true);
        rebuild();
    }


    @Override
    public void dispose () {
        Gdx.app.log("GUI dispose()", "");
        stage.dispose();
        skin.dispose();
    }

}
