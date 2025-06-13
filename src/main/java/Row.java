import java.util.HashMap;

public class Row {

    private HashMap<String, Object> data = new HashMap<>();

    public Row() {}

    public void setValue(String columnName, Object value) {
        this.data.put(columnName, value);
    }

    public Object getValue(String columnName) {
        return this.data.get(columnName);
    }

    public HashMap<String, Object> getValues() {
        return this.data;
    }

}
