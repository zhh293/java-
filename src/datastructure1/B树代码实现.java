package datastructure1;


    public class B树代码实现 {
        private int t; // 最小度数
        private BTreeNode root;

        public B树代码实现(int t) {
            this.t = t;
            root = new BTreeNode(true);
        }

        public void insert(int k) {
            BTreeNode r = root;
            if (r.keys.size() == (2 * t) - 1) {
                BTreeNode s = new BTreeNode(false);
                root = s;
                s.child.add(0, r);
                splitChild(s, 0, r);
                insertNonFull(s, k);
            } else {
                insertNonFull(r, k);
            }
        }

        private void insertNonFull(BTreeNode x, int k) {
            int i = x.keys.size() - 1;
            if (x.leaf) {
                x.keys.add(0);
                while (i >= 0 && k < x.keys.get(i)) {
                    x.keys.set(i + 1, x.keys.get(i));
                    i--;
                }
                x.keys.set(i + 1, k);
            } else {
                while (i >= 0 && k < x.keys.get(i)) {
                    i--;
                }
                i++;
                if (x.child.get(i).keys.size() == (2 * t) - 1) {
                    splitChild(x, i, x.child.get(i));
                    if (k > x.keys.get(i)) {
                        i++;
                    }
                }
                insertNonFull(x.child.get(i), k);
            }
        }

        private void splitChild(BTreeNode x, int i, BTreeNode y) {
            BTreeNode z = new BTreeNode(y.leaf);
            x.child.add(i + 1, z);
            x.keys.add(i, y.keys.get(t - 1));

            z.keys = (java.util.ArrayList<Integer>) y.keys.subList(t, y.keys.size());
            y.keys = (java.util.ArrayList<Integer>) y.keys.subList(0, t - 1);

            if (!y.leaf) {
                z.child = (java.util.ArrayList<BTreeNode>) y.child.subList(t, y.child.size());
                y.child = (java.util.ArrayList<BTreeNode>) y.child.subList(0, t);
            }
        }

        public void delete(int k) {
            delete(root, k);
            if (root.keys.size() == 0) {
                root = root.child.size() > 0 ? root.child.get(0) : new BTreeNode(true);
            }
        }

        private void delete(BTreeNode x, int k) {
            int idx = findKey(x, k);
            if (idx < x.keys.size() && x.keys.get(idx) == k) {
                if (x.leaf) {
                    x.keys.remove(idx);
                } else {
                    deleteInternalNode(x, k, idx);
                }
            } else {
                if (x.leaf) {
                    return;
                }
                boolean flag = idx == x.keys.size();
                if (x.child.get(idx).keys.size() < t) {
                    fixShortage(x, idx);
                }
                if (flag && idx > x.keys.size()) {
                    delete(x.child.get(idx - 1), k);
                } else {
                    delete(x.child.get(idx), k);
                }
            }
        }

        private int findKey(BTreeNode x, int k) {
            int idx = 0;
            while (idx < x.keys.size() && x.keys.get(idx) < k) {
                idx++;
            }
            return idx;
        }

        private void deleteInternalNode(BTreeNode x, int k, int idx) {
            if (x.child.get(idx).keys.size() >= t) {
                int pred = getPredecessor(x, idx);
                x.keys.set(idx, pred);
                delete(x.child.get(idx), pred);
            } else if (x.child.get(idx + 1).keys.size() >= t) {
                int succ = getSuccessor(x, idx);
                x.keys.set(idx, succ);
                delete(x.child.get(idx + 1), succ);
            } else {
                merge(x, idx);
                delete(x.child.get(idx), k);
            }
        }

        private int getPredecessor(BTreeNode x, int idx) {
            BTreeNode curr = x.child.get(idx);
            while (!curr.leaf) {
                curr = curr.child.get(curr.child.size() - 1);
            }
            return curr.keys.get(curr.keys.size() - 1);
        }

        private int getSuccessor(BTreeNode x, int idx) {
            BTreeNode curr = x.child.get(idx + 1);
            while (!curr.leaf) {
                curr = curr.child.get(0);
            }
            return curr.keys.get(0);
        }

        private void fixShortage(BTreeNode x, int idx) {
            if (idx != 0 && x.child.get(idx - 1).keys.size() >= t) {
                borrowFromPrev(x, idx);
            } else if (idx != x.child.size() - 1 && x.child.get(idx + 1).keys.size() >= t) {
                borrowFromNext(x, idx);
            } else {
                if (idx != 0) {
                    merge(x, idx - 1);
                } else {
                    merge(x, idx);
                }
            }
        }

        private void borrowFromPrev(BTreeNode x, int idx) {
            BTreeNode child = x.child.get(idx);
            BTreeNode sibling = x.child.get(idx - 1);

            child.keys.add(0, x.keys.get(idx - 1));
            x.keys.set(idx - 1, sibling.keys.get(sibling.keys.size() - 1));
            sibling.keys.remove(sibling.keys.size() - 1);

            if (!child.leaf) {
                child.child.add(0, sibling.child.get(sibling.child.size() - 1));
                sibling.child.remove(sibling.child.size() - 1);
            }
        }

        private void borrowFromNext(BTreeNode x, int idx) {
            BTreeNode child = x.child.get(idx);
            BTreeNode sibling = x.child.get(idx + 1);

            child.keys.add(x.keys.get(idx));
            x.keys.set(idx, sibling.keys.get(0));
            sibling.keys.remove(0);

            if (!child.leaf) {
                child.child.add(sibling.child.get(0));
                sibling.child.remove(0);
            }
        }

        private void merge(BTreeNode x, int idx) {
            BTreeNode child = x.child.get(idx);
            BTreeNode sibling = x.child.get(idx + 1);

            child.keys.add(x.keys.get(idx));
            child.keys.addAll(sibling.keys);

            if (!child.leaf) {
                child.child.addAll(sibling.child);
            }

            x.keys.remove(idx);
            x.child.remove(idx + 1);
        }

        public void printTree() {
            printTree(root, 0);
        }

        private void printTree(BTreeNode x, int l) {
            System.out.print("Level " + l + " " + x.keys.size() + ": ");
            for (int key : x.keys) {
                System.out.print(key + " ");
            }
            System.out.println();
            l++;
            if (!x.leaf) {
                for (BTreeNode child : x.child) {
                    printTree(child, l);
                }
            }
        }

        class BTreeNode {
            boolean leaf;
            java.util.ArrayList<Integer> keys;
            java.util.ArrayList<BTreeNode> child;

            public BTreeNode(boolean leaf) {
                this.leaf = leaf;
                keys = new java.util.ArrayList<>();
                child = new java.util.ArrayList<>();
            }
        }
    }
