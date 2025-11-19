package algthrom;

import java.util.Arrays;

/**
 * 快速傅里叶变换(FFT)算法实现
 *
 * FFT是离散傅里叶变换(DFT)的一种高效算法，用于将时域信号转换为频域信号，
 * 在数字信号处理、图像处理、音频分析等领域有广泛应用。
 */
public class FFT快速傅里叶变换算法 {

    /**
     * 复数类，用于表示复数及其运算
     */
    static class Complex {
        private double real;   // 实部
        private double imag;   // 虚部

        public Complex(double real, double imag) {
            this.real = real;
            this.imag = imag;
        }

        public Complex(double real) {
            this(real, 0);
        }

        public Complex() {
            this(0, 0);
        }

        /**
         * 复数加法
         */
        public Complex add(Complex other) {
            return new Complex(this.real + other.real, this.imag + other.imag);
        }

        /**
         * 复数减法
         */
        public Complex subtract(Complex other) {
            return new Complex(this.real - other.real, this.imag - other.imag);
        }

        /**
         * 复数乘法
         */
        public Complex multiply(Complex other) {
            double newReal = this.real * other.real - this.imag * other.imag;
            double newImag = this.real * other.imag + this.imag * other.real;
            return new Complex(newReal, newImag);
        }

        /**
         * 获取共轭复数
         */
        public Complex conjugate() {
            return new Complex(this.real, -this.imag);
        }

        public double getReal() {
            return real;
        }

        public double getImag() {
            return imag;
        }

        @Override
        public String toString() {
            if (imag >= 0) {
                return String.format("%.2f+%.2fi", real, imag);
            } else {
                return String.format("%.2f%.2fi", real, imag);
            }
        }
    }

    /**
     * 快速傅里叶变换(FFT)
     *
     * 使用分治法将DFT的计算复杂度从O(N^2)降低到O(N log N)
     *
     * @param x 输入的复数序列（长度必须是2的幂）
     * @return 变换后的复数序列
     */
    public static Complex[] fft(Complex[] x) {
        int n = x.length;

        // 基本情况：如果只有一个元素，直接返回
        if (n == 1) {
            return new Complex[]{x[0]};
        }

        // 确保输入长度是2的幂
        if ((n & (n - 1)) != 0) {
            throw new IllegalArgumentException("输入数组长度必须是2的幂");
        }

        // 分治：将输入分为偶数索引和奇数索引两部分
        Complex[] even = new Complex[n / 2];
        Complex[] odd = new Complex[n / 2];

        for (int i = 0; i < n / 2; i++) {
            even[i] = x[2 * i];
            odd[i] = x[2 * i + 1];
        }

        // 递归计算两部分的FFT
        Complex[] evenFFT = fft(even);
        Complex[] oddFFT = fft(odd);

        // 合并结果
        Complex[] result = new Complex[n];
        for (int i = 0; i < n / 2; i++) {
            // 计算旋转因子 W_n^i = e^(-2πij/n)
            double angle = -2 * Math.PI * i / n;
            Complex w = new Complex(Math.cos(angle), Math.sin(angle));

            // 蝶形运算
            Complex term = w.multiply(oddFFT[i]);
            result[i] = evenFFT[i].add(term);
            result[i + n / 2] = evenFFT[i].subtract(term);
        }

        return result;
    }

    /**
     * 逆向快速傅里叶变换(IFFT)
     *
     * 将频域信号转换回时域信号
     *
     * @param x 输入的复数序列
     * @return 逆变换后的复数序列
     */
    public static Complex[] ifft(Complex[] x) {
        int n = x.length;

        // 先对输入取共轭
        Complex[] conjugated = new Complex[n];
        for (int i = 0; i < n; i++) {
            conjugated[i] = x[i].conjugate();
        }

        // 计算FFT
        Complex[] fftResult = fft(conjugated);

        // 对结果取共轭并除以n
        Complex[] result = new Complex[n];
        for (int i = 0; i < n; i++) {
            result[i] = fftResult[i].conjugate();
            result[i].real /= n;
            result[i].imag /= n;
        }

        return result;
    }

    /**
     * 实数序列的FFT变换
     *
     * @param realInput 实数序列
     * @return 变换后的复数序列
     */
    public static Complex[] fft(double[] realInput) {
        // 补零至长度为2的幂
        int n = getNextPowerOfTwo(realInput.length);
        Complex[] complexInput = new Complex[n];

        for (int i = 0; i < realInput.length; i++) {
            complexInput[i] = new Complex(realInput[i]);
        }

        // 剩余位置补零
        for (int i = realInput.length; i < n; i++) {
            complexInput[i] = new Complex(0);
        }

        return fft(complexInput);
    }

    /**
     * 获取大于等于n的最小2的幂
     */
    private static int getNextPowerOfTwo(int n) {
        if (n <= 1) return 1;
        int power = 1;
        while (power < n) {
            power <<= 1;
        }
        return power;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== FFT快速傅里叶变换算法测试 ===\n");

        // 测试1: 简单的实数序列FFT
        System.out.println("测试1: 简单实数序列FFT");
        double[] signal1 = {1, 2, 3, 4};
        System.out.println("原始信号: " + Arrays.toString(signal1));

        Complex[] fftResult1 = fft(signal1);
        System.out.println("FFT结果:");
        for (int i = 0; i < fftResult1.length; i++) {
            System.out.printf("X[%d] = %s\n", i, fftResult1[i]);
        }

        // 验证IFFT
        Complex[] ifftResult1 = ifft(fftResult1);
        System.out.println("\nIFFT验证结果:");
        for (int i = 0; i < ifftResult1.length; i++) {
            System.out.printf("x[%d] = %.6f\n", i, ifftResult1[i].getReal());
        }

        System.out.println("\n" + "=".repeat(50) + "\n");

        // 测试2: 正弦波信号
        System.out.println("测试2: 正弦波信号FFT");
        int N = 16;
        double[] sineWave = new double[N];
        double frequency = 2.0; // 信号频率

        // 生成正弦波: sin(2πft)
        for (int i = 0; i < N; i++) {
            sineWave[i] = Math.sin(2 * Math.PI * frequency * i / N);
        }

        System.out.println("正弦波采样值:");
        for (int i = 0; i < N; i++) {
            System.out.printf("%.3f ", sineWave[i]);
        }
        System.out.println();

        Complex[] fftResult2 = fft(sineWave);
        System.out.println("\nFFT幅度谱:");
        for (int i = 0; i < fftResult2.length; i++) {
            double magnitude = Math.sqrt(
                    fftResult2[i].getReal() * fftResult2[i].getReal() +
                            fftResult2[i].getImag() * fftResult2[i].getImag()
            );
            System.out.printf("X[%d] 幅度: %.3f\n", i, magnitude);
        }

        System.out.println("\n" + "=".repeat(50) + "\n");

        // 测试3: 复合信号
        System.out.println("测试3: 复合信号FFT (包含频率为1和3的正弦波)");
        double[] compositeSignal = new double[N];
        for (int i = 0; i < N; i++) {
            // 组合两个不同频率的正弦波
            compositeSignal[i] = Math.sin(2 * Math.PI * 1 * i / N) +
                    0.5 * Math.sin(2 * Math.PI * 3 * i / N);
        }

        System.out.println("复合信号采样值:");
        for (int i = 0; i < N; i++) {
            System.out.printf("%.3f ", compositeSignal[i]);
        }
        System.out.println();

        Complex[] fftResult3 = fft(compositeSignal);
        System.out.println("\nFFT幅度谱 (显示主要频率成分):");
        for (int i = 0; i <= N/2; i++) {
            double magnitude = Math.sqrt(
                    fftResult3[i].getReal() * fftResult3[i].getReal() +
                            fftResult3[i].getImag() * fftResult3[i].getImag()
            );
            if (magnitude > 0.1) { // 只显示显著的频率成分
                System.out.printf("频率 %d: 幅度 %.3f\n", i, magnitude);
            }
        }
    }
}
