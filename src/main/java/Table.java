import java.util.ArrayList;

public class Table {

    private String name;
    private ArrayList<Column> columns = new ArrayList<>();
    private ArrayList<Row> rows = new ArrayList<>();

    public Table(String name) {this.name = name;}

    public Table() {this.name = "";}

    public void addColumn(Column column) {
        this.columns.add(column);
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

    public ArrayList<Row> selectAll() {
        return new ArrayList<Row>(rows);
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

    public void deleteAll() {
        this.rows.clear();
    }

    @Override
    public String toString(){

        String result = "";
        String bar = "+";
        int totalWidth = 0;

        int[] widths = new int[columns.size()];

        for(int i = 0; i < columns.size(); i++) {
            Column col = columns.get(i);
            int maxWidth = getLength(col.getName()) + 2;
            for(Row row : rows) {
                Object value = row.getValue(col.getName());
                int currentWidth = getLength(value) + 2;

                if (currentWidth > maxWidth) maxWidth = currentWidth;
            }
            widths[i] = maxWidth;
            totalWidth += maxWidth;

            bar += "-".repeat(widths[i]) + "+";
        }

        int count = totalWidth + columns.size() - 1;

        result += "+" + "-".repeat(count) + "+\n"; // top most bar

        int halved = (int) ((count - getLength(this.name)) / 2);

        result += "|" + " ".repeat(halved) + this.name + " ".repeat(halved) + "|\n";

        result += bar + "\n";

        for(int i = 0; i < columns.size(); i++) {
            Column col = columns.get(i);
            result += "| " + col.getName() + " ".repeat(widths[i] - getLength(col.getName()) - 1);
        }

        result += "|\n" + bar;

        for(int i = 0; i < rows.size(); i++){
            result += "\n| ";
            for(int j = 0; j < columns.size(); j++) {
                String value = rows.get(i).getValue(columns.get(j).getName()).toString();
                int number = widths[j] - getLength(value) - 1;

                String buffer = " ".repeat(number);
                result += value + buffer + "| ";
            }
        }

        result += "\n" + bar;

        return result;
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