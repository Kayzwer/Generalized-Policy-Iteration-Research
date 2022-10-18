# Generalized-Policy-Iteration-Research (SHIN Value Iteration)
SHIN Value Iteration is a newly created algorithm that combine Value Iteration and Policy Iteration. SHIN Value Iteration also can outperform them in some type of Grid World which takes fewer steps to reach convergence.


Example:

![image](https://user-images.githubusercontent.com/68285002/196347919-efad9070-4ed3-4344-b71d-a9b6bda22b89.png)

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

To set a grid to wall

     gridWorld.is_wall[`y-axis`][`x-axis`] = true;
