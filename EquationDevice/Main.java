import java.util.*;

public class Main {

    public static double[][] copyMatrix(double[][] mat) {
        int m = mat.length;
        int n = mat[0].length;
        double[][] res = new double[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                res[i][j] = mat[i][j];
        return res;
    }

    public static double[][] rref(double[][] aug) {
        int m = aug.length; // سطر ها
        int n = aug[0].length; // ستون ها
        double[][] A = copyMatrix(aug);
        int row = 0;
        double eps = 1e-9;

        for (int col = 0; col < n - 1 && row < m; col++) {
            // Find pivot
            int pivot = -1;
            for (int r = row; r < m; r++) {
                if (Math.abs(A[r][col]) > eps) {
                    pivot = r;
                    break;
                }
            }
            if (pivot == -1) continue;

            // swap
            double[] tmp = A[row]; A[row] = A[pivot]; A[pivot] = tmp;

            // normalize
            double pivVal = A[row][col];
            for (int c = 0; c < n; c++) {
                A[row][c] /= pivVal;
            }

            // eliminate others
            for (int r = 0; r < m; r++) {
                if (r != row && Math.abs(A[r][col]) > eps) {
                    double factor = A[r][col];
                    for (int c = 0; c < n; c++) {
                        A[r][c] -= factor * A[row][c];
                    }
                }
            }
            row++;
        }
        return A;
    }

    public static void solve(double[][] aug) {
        int m = aug.length;
        int nVars = aug[0].length - 1;
        double[][] R = rref(aug);
        double eps = 1e-9;

        // Determine ranks
        int rankCoeff = 0;
        boolean inconsistent = false;
        for (int r = 0; r < m; r++) {
            boolean allZero = true;
            for (int c = 0; c < nVars; c++) {
                if (Math.abs(R[r][c]) > eps) {
                    allZero = false;
                    break;
                }
            }
            if (!allZero) rankCoeff++;
            else if (Math.abs(R[r][nVars]) > eps) {
                inconsistent = true;
            }
        }
        int rankAug = inconsistent ? rankCoeff + 1 : rankCoeff;

        // Print RREF
        System.out.println("RREF:");
        for (double[] row : R) {
            for (double f : row) {
                System.out.printf("%.3f\t", f);
            }
            System.out.println();
        }

        if (rankCoeff == rankAug && rankAug == nVars) {
            // unique solution
            double[] sol = new double[nVars];
            Arrays.fill(sol, 0);
            for (int r = 0; r < m; r++) {
                int pivotCol = -1;
                for (int c = 0; c < nVars; c++) {
                    if (Math.abs(R[r][c] - 1.0) < eps) {
                        pivotCol = c; break;
                    }
                }
                if (pivotCol != -1) {
                    sol[pivotCol] = R[r][nVars];
                }
            }
            System.out.println("دستگاه سازگار و جواب یکتا دارد:");
            System.out.println(Arrays.toString(sol));
        }
        else if (rankCoeff == rankAug && rankAug < nVars) {
            System.out.println("دستگاه سازگار و بی‌نهایت جواب دارد.");
            System.out.println("می‌توانید متغیرهای آزاد را انتخاب و مقادیر را از RREF بخوانید.");
        }
        else {
            System.out.println("دستگاه ناسازگار است.");
            double[] xLS = leastSquares(aug, m, nVars);
            System.out.println("جواب کمترین مربعات (عددی): " + Arrays.toString(xLS));
        }
    }

    public static double[] leastSquares(double[][] aug, int m, int nVars) {
        double[][] A = new double[m][nVars];
        double[] b = new double[m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < nVars; j++) {
                A[i][j] = aug[i][j];
            }
            b[i] = aug[i][nVars];
        }

        // Compute AtA and Atb
        double[][] AtA = new double[nVars][nVars];
        double[] Atb = new double[nVars];
        for (int i = 0; i < nVars; i++) {
            for (int j = 0; j < nVars; j++) {
                double sum = 0;
                for (int k = 0; k < m; k++) {
                    sum += A[k][i] * A[k][j];
                }
                AtA[i][j] = sum;
            }
            double sumB = 0;
            for (int k = 0; k < m; k++) {
                sumB += A[k][i] * b[k];
            }
            Atb[i] = sumB;
        }
        return gaussianSolve(AtA, Atb);
    }

    public static double[] gaussianSolve(double[][] M, double[] b) {
        int n = M.length;
        for (int p = 0; p < n; p++) {
            int max = p;
            for (int i = p + 1; i < n; i++) {
                if (Math.abs(M[i][p]) > Math.abs(M[max][p])) max = i;
            }
            double[] temp = M[p]; M[p] = M[max]; M[max] = temp;
            double t = b[p]; b[p] = b[max]; b[max] = t;

            for (int i = p + 1; i < n; i++) {
                double alpha = M[i][p] / M[p][p];
                b[i] -= alpha * b[p];
                for (int j = p; j < n; j++) {
                    M[i][j] -= alpha * M[p][j];
                }
            }
        }
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < n; j++) {
                sum += M[i][j] * x[j];
            }
            x[i] = (b[i] - sum) / M[i][i];
        }
        return x;
    }

    public static void main(String[] args) {
        // {x1 x2 | b}
        double[][] aug = {
                {2, 3, 7},
                {4, -1, 1}
        };
        solve(aug);
    }
}
