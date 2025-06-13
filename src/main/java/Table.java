import java.util.ArrayList;

public class Table {

    private String name;
    private ArrayList<Column> columns = new ArrayList<>();
    private ArrayList<Row> rows = new ArrayList<>();

    public Table(String name) {this.name = name;}

    public Table() {this.name = "";}

    public String getName() { return this.name; }

    public void addColumn(Column column) {
        this.columns.add(column);
    }

    public void addColumns(ArrayList<Column> columns) {
        this.columns.addAll(columns);
    }

    public ArrayList<Column> getColumns() {
        return new ArrayList<>(this.columns);
    }

    public void insert(Object ... values) {

        Row newRow = new Row();

        if (values.length != this.columns.size()) {
            throw new IllegalArgumentException("Column count mismatch");
        }

        // loop through values
        for(int i = 0; i < values.length; i++) {
            Column col = this.columns.get(i);

            // check if types match

            boolean typesMatch = col.getType().isInstance(values[i]);
            if (!typesMatch && values[i] != null) {
                throw new IllegalArgumentException("Type mismatch for column");
            }

            newRow.setValue(col.getName(), values[i]);
        }

        this.rows.add(newRow);
    }

    public void insert(Row row) {
        this.rows.add(row);
    }

    public ArrayList<Row> selectAll() {
        return new ArrayList<>(this.rows);
    }

    public Table select(String ... columnNames) {
        Table result = new Table();
        int numberOfColumns = columnNames.length;

        // Add all columns that have a name in columnNames
        for (String columnName : columnNames) {

            for (Column column : this.columns) {

                if (column.getName().equals(columnName)) {
                    result.addColumn(column);
                }

            }

        }

        // Add rows
        for(Row row : this.rows) {

            Object[] newRow = new Object[numberOfColumns];

            for(int i = 0; i < numberOfColumns; i++) {

                newRow[i] = row.getValue(columnNames[i]);

            }

            result.insert(newRow);

        }

        return result;
    }

    public Table selectDistinct(String ... columnNames) {

        Table selected = this.select(columnNames);

        Table result = new Table();

        result.addColumns(selected.getColumns());

        for(Row row : selected.selectAll()) {
            if(!result.selectAll().contains(row)) {
                result.insert(row);
            }
        }

        return result;
    }

    public void deleteAll() {
        this.rows.clear();
    }

    @Override
    public String toString() {

        int[] widths = getColumnWidths(this.columns, this.rows);
        int numOfColumns = this.columns.size();
        int totalWidth = 2 * numOfColumns + sumArray(widths);


        String topBar = getBar(totalWidth) + "\n";
        String caption = "|" + centerPadding(this.name, totalWidth) + "|\n";
        String midBar = getBar(widths) + "\n";

        String header = getHeaderSection(this.columns, widths);
        String data = getDataSection(this.columns, this.rows, widths);

        return topBar + caption + midBar + header + midBar + data + midBar;

    }

    private static String getBar(int totalWidth) {
        return "+" + "-".repeat(totalWidth) + "+";
    }

    private static String getBar(int[] widths) {
        String bar = "+";

        for(int width : widths) {
            bar += "-".repeat(width + 2) + "+";
        }

        return bar;
    }

    private static String getHeaderSection(ArrayList<Column> columns, int[] widths) {
        String header = "";

        for(int i = 0; i < columns.size(); i++) {
            header += "|" + leftPadding(columns.get(i).getName(), widths[i]);
        }
        return  header + "|\n";
    }

    private static String getDataSection(ArrayList<Column> columns, ArrayList<Row> rows, int[] widths) {
        String data = "";

        for(Row row : rows) {
            String line = "";
            for(int j = 0; j < widths.length; j++) {
                String text = row.getValue(columns.get(j).getName()).toString();
                line += "|" + leftPadding(text, widths[j]);
            }

            data += line + "|\n";
        }

        return data;
    }

    private static int[] getColumnWidths(ArrayList<Column> columns, ArrayList<Row> rows) {
        int size = columns.size();
        int[] widths = new int[size];

        for(int i = 0; i < size; i++) {

            Column column = columns.get(i);
            String columnName = column.getName();

            int maxWidth = columnName.length();

            for(int j = 0; j < rows.size(); j++) {

                Object rowValue = rows.get(j).getValue(columnName);
                int currentWidth = getLength(rowValue);

                if (currentWidth > maxWidth) maxWidth = currentWidth;

            }

            widths[i] = maxWidth;
        }
        return widths;
    }

    private static int sumArray(int[] numbers) {
        int sum = 0;
        for (int num : numbers) sum += num;
        return sum;
    }

    private static String centerPadding(String text, int totalWidth) {
        int availableSpace = totalWidth - text.length();

        if (availableSpace % 2 == 0) {
            return " ".repeat(availableSpace/2) + text + " ".repeat(availableSpace/2);
        } else {
            int left = availableSpace - 1, right = availableSpace + 1;
            return " ".repeat(left/2) + text + " ".repeat(right/2);
        }

    }

    private static String leftPadding(String text, int totalWidth) {
        return " ".repeat(1) + text + " ".repeat(totalWidth - text.length() - 1);
    }

    private static int getLength(Object obj) {
        if (obj == null) return 0;

        String str;

        if (obj instanceof Integer || obj instanceof Long) {
            str = String.valueOf(Math.abs(((Number) obj).longValue()));
            return str.length();
        }

        if (obj instanceof Double || obj instanceof Float) {
            str = String.valueOf(obj).replace(".", " ");
            return str.length();
        }

        if (obj instanceof String) {
            return ((String) obj).length();
        }

        if (obj instanceof Character) {
            return 1;
        }

        if (obj instanceof Boolean) {
            if ((Boolean) obj){
                return 4;
            } else {
                return 5;
            }
        }

        return 0; // Unknown type
    }

}