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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // same object
        if (obj == null || getClass() != obj.getClass()) return false; // null or wrong type

        Row other = (Row) obj;
        return this.data.equals(other.getValues()); // compare map contents
    }

}
