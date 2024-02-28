package jade;

public class LevelScene extends Scene{

    public LevelScene(){
        System.out.println("inside level scene");
        Window.get().r = Window.get().g = Window.get().b = 1;
    }

    @Override
    public void update(float dt) {

    }
}
