class GridWorld:
    def __init__(self, x:int, y:int, initial_state_value:float = 0.0) -> None:
        if x <= 0 or y <= 0:
            raise Exception("x or y must greater than 0")
        self.x = x
        self.y = y
        self.__initial_state_value = initial_state_value
        self.world = [[initial_state_value for _ in range(x)] for _ in range(y)]
        self.policy = [[["^", "v", "<", ">"] for _ in range(x)] for _ in range(y)]
        self.terminal_states = []
    
    def __str__(self) -> str:
        world = ""
        for y in self.world:
            for x in y:
                world += str(round(x, 3) if type(x) is float else x) + "\t"
            world += "\n"
        return world
    
    def policy_to_str(self) -> str:
        policy = ""
        for y in self.policy:
            for x in y:
                policy += str(x) + "\t"
            policy += "\n"
        return policy
    
    def __len__(self) -> int:
        return self.x * self.y
    
    def set_block(self, x:int, y:int) -> None:
        if x >= self.x or y >= self.y or x < 0 or y < 0:
            raise Exception("Input is out of gridworld range")
        self.world[y][x] = "B"

    def build_wall(self, start:tuple, end:tuple) -> None:
        for x, y in zip(start, end):
            if x >= self.x or y >= self.y or x < 0 or y < 0:
                raise Exception("Input is out of gridworld range")
        if start == end:
            raise Exception("Use set_block() to build single block")

        if start[1] > end[1] or start[0] > end[0]:
            raise Exception("Invalid input, must be in form of (x, i), (x, j) or (i, y), (j, y) where i < j")
        
        if start[0] == end[0]:
            for i in range(start[1], end[1] + 1):
                self.world[i][start[0]] = "B"
        elif start[1] == end[1]:
            for i in range(start[0], end[0] + 1):
                self.world[start[1]][i] = "B"
        else:
            raise Exception("Invalid input, must be in form of (x, i), (x, j) or (i, y), (j, y) where i < j")
    
    def set_terminal_state(self, x:int, y:int) -> None:
        self.terminal_states.append((x, y))
        self.world[y][x] = 0
        self.policy[y][x] = []
    
    def reset(self, initial_state_value:float = None) -> None:
        if not initial_state_value:
            self.world = [[self.__initial_state_value for _ in range(self.x)] for _ in range(self.y)]
        else:
            self.world = [[initial_state_value for _ in range(self.x)] for _ in range(self.y)]
        self.terminal_states = []
        self.policy = [[["^", "v", "<", ">"] for _ in range(self.x)] for _ in range(self.y)]
    
    def set_state_value(self, x:int, y:int, value:float) -> None:
        if x < 0 or x >= self.x or y < 0 or y >= self.y:
            raise Exception("Input is out of gridworld range")
        if self.world[y][x] == "B":
            raise Exception("Given position is a block")
        else:
            self.world[y][x] = value
    
    def policy_evaluation(self, iteration:int, gamma:float, step_cost:float) -> None:
        for _ in range(iteration):
            new_world = [[x for x in y] for y in self.world]
            for y in range(self.y):
                for x in range(self.x):
                    if (x, y) in self.terminal_states:
                        continue
                    up, down, left, right = self.get_directions_value(x, y)
                    directions = []
                    for direction in self.policy[y][x]:
                        if direction == "^":
                            directions.append(up)
                        if direction == "v":
                            directions.append(down)
                        if direction == "<":
                            directions.append(left)
                        if direction == ">":
                            directions.append(right)
                    new_world[y][x] = step_cost + gamma * sum(directions) / len(directions)
            self.world = new_world
        
    def get_state_value(self, x:int, y:int) -> float:
        if x < 0 or x >= self.x or y < 0 or y >= self.y:
            raise Exception("Input is out of gridworld range")
        if self.world[y][x] == "B":
            return 0.0
        else:
            return self.world[y][x]
    
    def policy_improvement(self) -> None:
        for y in range(self.y):
            for x in range(self.x):
                if (x, y) in self.terminal_states:
                    continue
                self.policy[y][x] = self.best_direction(x, y)
    
    def best_direction(self, x:int, y:int) -> list:
        directions = []
        up, down, left, right = self.get_directions_value(x, y)
        _max = max(up, down, left, right)
        for i, value in enumerate([up, down, left, right]):
            if value == _max:
                if i == 0:
                    directions.append("^")
                elif i == 1:
                    directions.append("v")
                elif i == 2:
                    directions.append("<")
                else:
                    directions.append(">")
        return directions
    
    def get_directions_value(self, x:int, y:int) -> tuple:
        try:
            up = self.get_state_value(x, y - 1)
        except:
            up = self.get_state_value(x, y)
        try:
            down = self.get_state_value(x, y + 1)
        except:
            down = self.get_state_value(x, y)
        try:
            left = self.get_state_value(x - 1, y)
        except:
            left = self.get_state_value(x, y)
        try:
            right = self.get_state_value(x + 1, y)
        except:
            right = self.get_state_value(x, y)
        
        return (up, down, left, right)
    
    def value_iteration(self, iteration:int, gamma:float, step_cost:float) -> None:
        for _ in range(iteration):
            self.policy_evaluation(1, gamma, step_cost)
            self.policy_improvement()


if __name__ == "__main__":
    temp = GridWorld(4, 4)
    temp.set_terminal_state(0, 0)
    temp.set_terminal_state(3, 3)
    print(temp)
    temp.policy_evaluation(3, 1, -1)
    print(temp)
    temp.policy_improvement()
    print(temp.policy_to_str())
