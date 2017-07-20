package edu.nagojudge.tools.utils.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Main {

    static class node implements Comparable<node> {

        int i;
        int j;
        int sum;

        public node(int i, int j, int sum) {
            this.i = i;
            this.j = j;
            this.sum = sum;
        }

        @Override
        public int compareTo(node o) {
            return this.sum - o.sum;
        }

    }

    static int n;
    static int[][] map = new int[1000][1000];
    static int[][] sum = new int[1000][1000];
    static int[] dx = {0, 1};
    static int[] dy = {1, 0};

    static boolean isValid(int i, int j) {
        return i >= 0 && i < n && j >= 0 && j < n;
    }

    static int bfs() {
        PriorityQueue<node> q = new PriorityQueue<node>();
        node origen = new node(0, 0, map[0][0]);
        q.add(origen);
        sum[0][0] = map[0][0];

        while (!q.isEmpty()) {
            node current = q.poll();
            if (current.i == n - 1 && current.j == n - 1) {
                return map[n - 1][n - 1] + Math.max(sum[n - 1][n - 2], sum[n - 2][n - 1]);
            } else if (current.sum == sum[current.i][current.j]) {
                for (int i = 0; i < 2; i++) {
                    int x = current.i + dx[i];
                    int y = current.j + dy[i];
                    if (isValid(x, y) && (current.sum + map[x][y]) > sum[x][y]) {
                        node node = new node(x, y, current.sum + map[x][y]);
                        sum[x][y] = node.sum;
                        q.add(node);
                    }
                }
            }
        }
        return -1;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("input"));
        //BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = in.readLine()) != null) {
            n = Integer.parseInt(line);
            if (n == 0) {
                break;
            }
            StringTokenizer st;
            for (int j = 0; j < n; j++) {
                st = new StringTokenizer(in.readLine());
                int k = 0;
                while (st.hasMoreTokens()) {
                    map[j][k] = Integer.parseInt(st.nextToken());
                    sum[j][k] = Integer.MIN_VALUE;
                    k++;
                }
            }
            System.out.println(bfs());
        }
    }
}
