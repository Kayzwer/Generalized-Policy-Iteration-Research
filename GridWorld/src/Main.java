public class Main {
    public static void main(String[] args) {
        GridWorld gridWorld = new GridWorld(4, 4, 1.0);
        gridWorld.state_rewards[0][1] = -1.0;
        gridWorld.state_rewards[0][2] = -1.0;
        gridWorld.state_rewards[0][3] = -1.0;
        gridWorld.state_rewards[1][0] = -1.0;
        gridWorld.state_rewards[1][1] = -1.0;
        gridWorld.state_rewards[1][2] = -1.0;
        gridWorld.state_rewards[1][3] = -1.0;
        gridWorld.state_rewards[2][0] = -1.0;
        gridWorld.state_rewards[2][1] = -1.0;
        gridWorld.state_rewards[2][2] = -1.0;
        gridWorld.state_rewards[2][3] = -1.0;
        gridWorld.state_rewards[3][0] = -1.0;
        gridWorld.state_rewards[3][1] = -1.0;
        gridWorld.state_rewards[3][2] = -1.0;
        gridWorld.is_terminal_state[0][0] = true;
        gridWorld.is_terminal_state[3][3] = true;
        gridWorld.initPolicy();

        gridWorld.PolicyIteration(0.5);
    }
}