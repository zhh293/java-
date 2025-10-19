package datastructureclass;

class Spreadsheet {

    int[][] matrix;
    public Spreadsheet(int rows) {
       matrix = new int[rows][26];
    }

    public void setCell(String cell, int value) {
        String substring = cell.substring(0, 1);
        cell = cell.substring(1);
        int i = Integer.parseInt(cell);
        matrix[i -1][substring.charAt(0) - 'A'] = value;
    }

    public void resetCell(String cell) {
        String substring = cell.substring(0, 1);
        cell = cell.substring(1);
        int i = Integer.parseInt(cell);
        matrix[i -1][substring.charAt(0) - 'A'] = 0;
    }

    public int getValue(String formula) {
        String[] split = formula.split("[=+]");
        int result=0;
        for (int i = 0; i < split.length; i++) {
            if(!split[i].isEmpty() &&split[i].charAt(0)!='='&&split[i].charAt(0)!='+'){
                if(split[i].charAt(0)>='A'&&split[i].charAt(0)<='Z'){
                    String substring = split[i].substring(0, 1);
                    String substring1 = split[i].substring(1);
                    Integer value = Integer.parseInt(substring1);
                    result+=matrix[value-1][substring.charAt(0) - 'A'];
                }else{
                    Integer value = Integer.parseInt(split[i]);
                    result+=value;
                }
            }
        }
        return result;
    }
}

/**
 * Your Spreadsheet object will be instantiated and called as such:
 * Spreadsheet obj = new Spreadsheet(rows);
 * obj.setCell(cell,value);
 * obj.resetCell(cell);
 * int param_3 = obj.getValue(formula);
 */