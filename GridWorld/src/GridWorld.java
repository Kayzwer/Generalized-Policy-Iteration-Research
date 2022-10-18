import java.util.Arrays;


public class GridWorld {
    public final int height;
    public final int width;
    public final double gamma;
    public double[][] state_values;
    public double[][] state_rewards;
    public boolean[][] is_terminal_state;
    public boolean[][] is_wall;
    public String[][][] policy;

    public GridWorld(int height, int width, double gamma) {
        this.height = height;
        this.width = width;
        this.gamma = gamma;
        this.state_values = new double[height][width];
        this.state_rewards = new double[height][width];
        this.is_terminal_state = new boolean[height][width];
        this.is_wall = new boolean[height][width];
        this.policy = new String[height][width][4];
    }

    public void initPolicy() {
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                if (!this.is_terminal_state[i][j]) {
                    this.policy[i][j] = new String[] {"^", "v", "<", ">"};
                } else {
                    this.policy[i][j] = new String[] {"", "", "", ""};
                }
            }
        }
    }

    public double getUpStateValue(int x, int y) {
        if (y == 0) {
            return this.state_values[y][x];
        } else {
            if (this.is_wall[y - 1][x]) {
                return this.state_values[y][x];
            } else {
                return this.state_values[y - 1][x];
            }
        }
    }

    public double getDownStateValue(int x, int y) {
        if (y == this.height - 1) {
            return this.state_values[y][x];
        } else {
            if (this.is_wall[y + 1][x]) {
                return this.state_values[y][x];
            } else {
                return this.state_values[y + 1][x];
            }
        }
    }

    public double getLeftStateValue(int x, int y) {
        if (x == 0) {
            return this.state_values[y][x];
        } else {
            if (this.is_wall[y][x - 1]) {
                return this.state_values[y][x];
            } else {
                return this.state_values[y][x - 1];
            }
        }
    }

    public double getRightStateValue(int x, int y) {
        if (x == this.width - 1) {
            return this.state_values[y][x];
        } else {
            if (this.is_wall[y][x + 1]) {
                return this.state_values[y][x];
            } else {
                return this.state_values[y][x + 1];
            }
        }
    }

    public double getPolicyExceptedValue(int x, int y) {
        String[] policy = this.policy[y][x];
        double sum = 0.0;
        double count = 0;

        if (policy[0].equals("^")) {
            sum += this.getUpStateValue(x, y);
            count++;
        }

        if (policy[1].equals("v")) {
            sum += this.getDownStateValue(x, y);
            count++;
        }

        if (policy[2].equals("<")) {
            sum += this.getLeftStateValue(x, y);
            count++;
        }

        if (policy[3].equals(">")) {
            sum += this.getRightStateValue(x, y);
            count++;
        }

        return sum / count;
    }
    
    public void policyEvaluation() {
        double[][] new_state_values = new double[this.height][this.width];
        for (int i = 0; i < this.height; i++) {
            System.arraycopy(this.state_values[i], 0, new_state_values[i], 0, this.width);
        }

        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                if (!(this.is_wall[i][j] || this.is_terminal_state[i][j])) {
                    new_state_values[i][j] = this.state_rewards[i][j] + this.gamma * this.getPolicyExceptedValue(j, i);
                }
            }
        }

        this.state_values = new_state_values;
    }

    public String[] getNewPolicy(int x, int y) {
        double[] q_values = new double[] {
                this.getUpStateValue(x, y),
                this.getDownStateValue(x, y),
                this.getLeftStateValue(x, y),
                this.getRightStateValue(x, y)
        };
        double max = q_values[0];
        for (double q_value : q_values) {
            if (q_value > max) {
                max = q_value;
            }
        }

        String[] policy = new String[] {"^", "v", "<", ">"};
        for (int i = 0; i < policy.length; i++) {
            if (q_values[i] != max) {
                policy[i] = "";
            }
        }
        return policy;
    }

    public void policyImprovement() {
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                if (!(this.is_wall[i][j] || this.is_terminal_state[i][j])) {
                    this.policy[i][j] = this.getNewPolicy(j, i);
                }
            }
        }
    }

    public void PolicyIteration(double delta) {
        int step = 0;
        while (true) {
            System.out.println(this);
            System.out.println(this.policyToString());
            String[][][] old_policy = new String[this.height][this.width][4];
            for (int i = 0; i < this.height; i++) {
                System.arraycopy(this.policy[i], 0, old_policy[i], 0, this.width);
            }
            while (true) {
                double[][] old_state_value = new double[this.height][this.width];
                for (int i = 0; i < this.height; i++) {
                    System.arraycopy(this.state_values[i], 0, old_state_value[i], 0, this.width);
                }
                this.policyEvaluation();
                step++;
                if (this.getStateValueDiff(old_state_value) <= delta) {
                    break;
                }
            }
            this.policyImprovement();
            step++;
            if (this.isPolicyStable(old_policy)) {
                break;
            }
        }
        System.out.println("Step: " + step);
    }

    public void ValueIteration() {
        int step = 0;
        while (true) {
            System.out.println(this);
            System.out.println(this.policyToString());
            String[][][] old_policy = new String[this.height][this.width][4];
            for (int i = 0; i < this.height; i++) {
                System.arraycopy(this.policy[i], 0, old_policy[i], 0, this.width);
            }
            this.policyEvaluation();
            step++;
            this.policyImprovement();
            step++;
            if (this.isPolicyStable(old_policy)) {
                break;
            }
        }
        System.out.println("Step: " + step);
    }

    public void ShinValueIteration(int n, double delta) {
        int step = 0;
        while (true) {
            System.out.println(this);
            System.out.println(this.policyToString());
            String[][][] old_policy = new String[this.height][this.width][4];
            for (int i = 0; i < this.height; i++) {
                System.arraycopy(this.policy[i], 0, old_policy[i], 0, this.width);
            }
            for (int i = 0; i < n; i++) {
                double[][] old_state_value = new double[this.height][this.width];
                for (int j = 0; j < this.height; j++) {
                    System.arraycopy(this.state_values[j], 0, old_state_value[j], 0, this.width);
                }
                this.policyEvaluation();
                step++;
                if (this.getStateValueDiff(old_state_value) <= delta) {
                    break;
                }
            }
            this.policyImprovement();
            step++;
            if (this.isPolicyStable(old_policy)) {
                break;
            }
        }
    }

    public double getStateValueDiff(double[][] state_values) {
        double max = 0.0;
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                double diff = Math.abs(state_values[i][j] - this.state_values[i][j]);
                max = Math.max(diff, max);
            }
        }
        return max;
    }

    public boolean isPolicyStable(String[][][] policy) {
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                if (!Arrays.equals(policy[i][j], this.policy[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                if (this.is_wall[i][j]) {
                    output.append("B");
                } else {
                    output.append(this.state_values[i][j]);
                }
                output.append(" ");
            }
            output.append("\n");
        }
        return output.toString();
    }

    public String policyToString() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                if (this.is_wall[i][j]) {
                    output.append("B");
                } else {
                    output.append(Arrays.toString(this.policy[i][j]));
                }
                output.append(" ");
            }
            output.append("\n");
        }
        return output.toString();
    }
}
