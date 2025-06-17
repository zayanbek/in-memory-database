import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class Table {

    // Member variables

    private String name;
    private ArrayList<Column> columns = new ArrayList<>();
    private ArrayList<Row> rows = new ArrayList<>();

    // Constructors

    public Table(String name) {this.name = name;}

    public Table() {this.name = "";}

    // Getter

    public String getName() { return this.name; }

    public ArrayList<Column> getColumns() {
        return new ArrayList<>(this.columns);
    }

    // Columns

    public void addColumn(String name, Class<?> type) {
        this.columns.add(new Column(name, type));
    }

    public void addColumn(Column column) {
        this.columns.add(column);
    }

    public void addColumns(ArrayList<Column> columns) {
        this.columns.addAll(columns);
    }

    // Insert

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

    // Select

    public ArrayList<Row> selectAll() {
        return new ArrayList<>(this.rows);
    }

    public Table select(String ... columnNames) {
        Table result = new Table();
        int numberOfColumns = columnNames.length;

        // Add all columns that have a name in columnNames
        for (String columnName : columnNames) {
            boolean found = false;

            for (Column column : this.columns) {

                if (column.getName().equals(columnName)) {
                    result.addColumn(column);
                    found = true;
                }

            }

            if (!found) {
                throw new IllegalArgumentException("Column not found");
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

    public Table selectWhere(Predicate<Row> condition, String ... columnNames) {
        Table result = new Table();

        result.addColumns(this.select(columnNames).getColumns());

        for(int index : getIndicesOfRowsWhere(condition)) result.insert(this.rows.get(index));

        return result;
    }

    // Update

    public void update(Predicate<Row> condition, HashMap<String, Object> changes) {

        for (int index : getIndicesOfRowsWhere(condition)) {
            Row row = this.rows.get(index);
            for (Map.Entry<String, Object> entry : changes.entrySet()) {
                row.setValue(entry.getKey(), entry.getValue());
            }
        }

    }

    public void update(HashMap<String, Object> changes) {
        this.update(row -> true, changes);
    }

    // Delete

    public void deleteWhere(Predicate<Row> condition) {
        this.rows.removeIf(condition);
    }

    public void deleteAll() {
        this.rows.clear();
    }

    // Helper

    public boolean isEmpty() {
        return (this.columns.isEmpty() && this.rows.isEmpty());
    }

    private ArrayList<Integer> getIndicesOfRowsWhere(Predicate<Row> condition) {

        ArrayList<Integer> indices = new ArrayList<>();

        for(int i = 0; i < this.rows.size(); i++) {
            if (condition.test(this.rows.get(i))) {
                indices.add(i);
            }
        }
        return indices;
    }

    // To String

    @Override
    public String toString() {

        if (!this.isEmpty()) {
            int[] widths = getColumnWidths(this.columns, this.rows);
            int numOfColumns = this.columns.size() + 1;
            int totalWidth = (2 * numOfColumns) + sumArray(widths);


            String topBar = getBar(totalWidth) + "\n";
            String caption = "|" + centerPadding(this.name, totalWidth) + "|\n";
            String midBar = getBar(widths);

            String header = getHeaderSection(this.columns, widths);
            String data = getDataSection(this.columns, this.rows, widths);

            if (!this.name.isEmpty()) {
                return topBar + caption + midBar + "\n" + header + midBar + "\n" + data + midBar;
            } else {
                return midBar + "\n" + header + midBar + "\n" + data + midBar;
            }

        }

        return "";

    }

    private static String getBar(int totalWidth) {
        return "+" + "-".repeat(totalWidth) + "+";
    }

    private static String getBar(int[] widths) {
        StringBuilder bar = new StringBuilder("+");

        for(int width : widths) {
            bar.append("-".repeat(width + 2));
            bar.append("+");
        }

        return bar.toString();
    }

    private static String getHeaderSection(ArrayList<Column> columns, int[] widths) {
        StringBuilder header = new StringBuilder();

        for(int i = 0; i < columns.size(); i++) {
            header.append("|");
            header.append(leftPadding(columns.get(i).getName(), widths[i]));
        }
        return  header.append("|\n").toString();
    }

    private static String getDataSection(ArrayList<Column> columns, ArrayList<Row> rows, int[] widths) {
        StringBuilder data = new StringBuilder();

        for(Row row : rows) {
            StringBuilder line = new StringBuilder();
            for(int j = 0; j < widths.length; j++) {
                Object value = row.getValue(columns.get(j).getName());
                line.append("|");
                line.append(leftPadding(value.toString(), widths[j]));
            }

            data.append(line);
            data.append("|\n");
        }

        return data.toString();
    }

    private static int[] getColumnWidths(ArrayList<Column> columns, ArrayList<Row> rows) {
        int size = columns.size();
        int[] widths = new int[size];

        for(int i = 0; i < size; i++) {

            Column column = columns.get(i);
            String columnName = column.getName();

            int maxWidth = columnName.length();


            for(Row row: rows) {
                Object rowValue = row.getValue(columnName);
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
        return " " + text + " ".repeat(totalWidth - text.length() + 1);
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