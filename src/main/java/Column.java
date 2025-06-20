public class Column {

    private String name;
    private Class<?> type;

    public Column(String name, Class<?> type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public Class<?> getType() {
        return this.type;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof Column)) return false;

        Column other = (Column) obj;
        return this.name == other.getName() && this.type == other.getType(); // compare map contents
    }
}
