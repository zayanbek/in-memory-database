import java.util.HashMap;

public class Row {

    private HashMap<String, Object> hashmap = new HashMap<>();

    public Row() {}

    public void setValue(String columnName, Object value) {
        hashmap.put(columnName, value);
    }

    public Object getValue(String columnName) {
        return hashmap.get(columnName);
    }

}
