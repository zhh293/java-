package algthrom;

public class 牛顿迭代法 {
    // 定义方程 f(x) = 3x² - e^x
    private static double function(double x) {
        return 3 * x * x - Math.exp(x);
    }

    // 定义方程的导数 f'(x) = 6x - e^x
    private static double derivative(double x) {
        return 6 * x - Math.exp(x);
    }

    // 使用牛顿迭代法求解方程的根
    public static double solve(double initialGuess, double precision) {
        double x = initialGuess;
        double nextX;
        int iterations = 0;

        do {
            double f = function(x);
            double df = derivative(x);

            // 防止除以零
            if (Math.abs(df) < 1e-12) {
                throw new ArithmeticException("导数接近零，无法继续迭代");
            }

            // 牛顿迭代公式: x(n+1) = x(n) - f(x(n))/f'(x(n))
            nextX = x - f / df;
            iterations++;

            // 检查是否达到精度要求
            if (Math.abs(nextX - x) < precision) {
                break;
            }

            x = nextX;
        } while (iterations < 1000); // 限制最大迭代次数，防止无限循环

        System.out.println("迭代次数: " + iterations);
        return nextX;
    }

    public static void main(String[] args) {
        double precision = 1e-8; // 精度要求: 10的-8次方
        double initialGuess = 0.5; // 初始猜测值，在(0,1)区间内

        try {
            double root = solve(initialGuess, precision);
            System.out.printf("方程的根: %.10f%n", root);
            System.out.printf("验证: 3x² - e^x = %.10f%n", function(root));
        } catch (ArithmeticException e) {
            System.out.println("求解失败: " + e.getMessage());
        }
    }
}
