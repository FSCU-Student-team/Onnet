package Physics;

import Game.Input;
import Game.InputManager;

import java.util.HashMap;
import java.util.Map;

public class ActionManager {

    private final Map<Input, Action> actionMap = new HashMap<>();
    private final InputManager inputManager;

    public ActionManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    public void bind(Input key, Action action) {
        actionMap.put(key, action);
    }

    public void update() {
        int state = inputManager.getInputState();

        for (Map.Entry<Input, Action> entry : actionMap.entrySet()) {
            Input key = entry.getKey();
            Action action = entry.getValue();

            if (Input.isSet(state, key)) {
                action.execute();
            }
        }
    }

    public void updateSpecificAction(Input input) {
        if (Input.isSet(inputManager.getInputState(), input)) actionMap.get(input).execute();
    }
}
