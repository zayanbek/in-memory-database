import org.junit.jupiter.api.Test ;

import static org.junit.jupiter.api.Assertions.*;

class TableTest {

    @Test
    void selectSingularColumn() {
        Table users = new Table("users");

        users.addColumn("id", Integer.class);
        users.addColumn("name", String.class);
        users.addColumn("age", Integer.class);

        users.insert(1, "alice", 24);
        users.insert(2, "bob", 19);
        users.insert(3, "john", 24);
        users.insert(4, "steve", 32);

        Table newTable = users.select("id");

        String expected = """
               +----+
               | id |
               +----+
               | 1  |
               | 2  |
               | 3  |
               | 4  |
               +----+""";

        assertEquals(expected, newTable.toString());
    }

    @Test
    void selectPluralColumns() {
        Table users = new Table("users");

        users.addColumn("id", Integer.class);
        users.addColumn("name", String.class);
        users.addColumn("age", Integer.class);

        users.insert(1, "alice", 24);
        users.insert(2, "bob", 19);
        users.insert(3, "john", 24);
        users.insert(4, "steve", 32);

        Table newTable = users.select("name", "age");

        String expected = """
                +-------+-----+
                | name  | age |
                +-------+-----+
                | alice | 24  |
                | bob   | 19  |
                | john  | 24  |
                | steve | 32  |
                +-------+-----+""";

        assertEquals(expected,newTable.toString());
    }

    @Test
    void selectPluralAndReverseColumns() {
        Table users = new Table("users");

        users.addColumn("id", Integer.class);
        users.addColumn("name", String.class);
        users.addColumn("age", Integer.class);

        users.insert(1, "alice", 24);
        users.insert(2, "bob", 19);
        users.insert(3, "john", 24);
        users.insert(4, "steve", 32);

        Table newTable = users.select("age", "name");

        String expected = """
                +-----+-------+
                | age | name  |
                +-----+-------+
                | 24  | alice |
                | 19  | bob   |
                | 24  | john  |
                | 32  | steve |
                +-----+-------+""";

        System.out.println(newTable);

        assertEquals(expected,newTable.toString());
    }

    @Test
    void selectDistinctSingularColumn() {
        Table users = new Table("users");

        users.addColumn("id", Integer.class);
        users.addColumn("name", String.class);
        users.addColumn("age", Integer.class);

        users.insert(1, "alice", 24);
        users.insert(2, "bob", 19);
        users.insert(3, "john", 24);
        users.insert(4, "steve", 32);

        Table newTable = users.selectDistinct("age");

        System.out.println(users);
        System.out.println(newTable);

        String expected = """
                +-----+
                | age |
                +-----+
                | 24  |
                | 19  |
                | 32  |
                +-----+""";

        assertEquals(expected, newTable.toString());
    }

    @Test
    void selectDistinctPluralColumns() {
        Table users = new Table("users");

        users.addColumn("id", Integer.class);
        users.addColumn("name", String.class);
        users.addColumn("age", Integer.class);

        users.insert(1, "alice", 24);
        users.insert(2, "bob", 19);
        users.insert(3, "john", 24);
        users.insert(4, "steve", 32);

        Table newTable = users.selectDistinct("name","age");

        System.out.println(users);
        System.out.println(newTable);

        String expected = """
                +-------+-----+
                | name  | age |
                +-------+-----+
                | alice | 24  |
                | bob   | 19  |
                | john  | 24  |
                | steve | 32  |
                +-------+-----+""";

        assertEquals(expected, newTable.toString());
    }

    @Test
    void selectDistinctPluralAndReverseColumns() {
        Table users = new Table("users");

        users.addColumn("id", Integer.class);
        users.addColumn("name", String.class);
        users.addColumn("age", Integer.class);

        users.insert(1, "alice", 24);
        users.insert(2, "bob", 19);
        users.insert(3, "john", 24);
        users.insert(4, "steve", 32);

        Table newTable = users.selectDistinct("age","name");

        System.out.println(users);
        System.out.println(newTable);

        String expected = """
                +-----+-------+
                | age | name  |
                +-----+-------+
                | 24  | alice |
                | 19  | bob   |
                | 24  | john  |
                | 32  | steve |
                +-----+-------+""";

        assertEquals(expected, newTable.toString());
    }
}