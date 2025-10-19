package datastructure1;

public class AC自动机 {
    //字典树的变种
    public static class Node {
        public Node[] next = new Node[26];
        public boolean isEnd = false;
        public int fail;
        public int[] output = new int[10];
        public Node() {
            for (int i = 0; i < 26; i++) {
                next[i] = null;
            }
        }
        public Node(boolean isEnd) {
            this();
            this.isEnd = isEnd;
        }
        public Node(int fail) {
            this();
            this.fail = fail;
        }
        public Node(int fail, int[] output) {
            this();
            this.fail = fail;
            this.output = output;
        }
        public Node(boolean isEnd, int fail, int[] output) {
            this();
            this.isEnd = isEnd;
            this.fail = fail;
            this.output = output;
        }

    }
}
