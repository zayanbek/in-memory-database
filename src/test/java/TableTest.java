import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test ;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class TableTest {

    @Nested
    class selectTest {

        @Test
        void selectInvalidColumnParameterThrowsException() {
            Table users = new Table("users");

            users.addColumn("id", Integer.class);
            users.addColumn("name", String.class);
            users.addColumn("age", Integer.class);

            users.insert(1, "alice", 24);
            users.insert(2, "bob", 19);
            users.insert(3, "john", 24);
            users.insert(4, "steve", 32);

            assertThrows(IllegalArgumentException.class, () -> users.select("id", "email"));
        }

        @Test
        void selectNoParameters() {
            Table users = new Table("users");

            users.addColumn("id", Integer.class);
            users.addColumn("name", String.class);
            users.addColumn("age", Integer.class);

            users.insert(1, "alice", 24);
            users.insert(2, "bob", 19);
            users.insert(3, "john", 24);
            users.insert(4, "steve", 32);

            Table actual = users.select();

            assertEquals(new Table(), actual);

        }

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

            Table actual = users.select("id");


            Table expected = new Table();

            expected.addColumn("id", Integer.class);

            expected.insert(1);
            expected.insert(2);
            expected.insert(3);
            expected.insert(4);

            assertEquals(expected, actual);
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

            Table actual = users.select("name", "age");

            Table expected = new Table();

            expected.addColumn("name", String.class);
            expected.addColumn("age", Integer.class);

            expected.insert("alice", 24);
            expected.insert("bob", 19);
            expected.insert("john", 24);
            expected.insert("steve", 32);

            assertEquals(expected, actual);
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

            Table actual = users.select("age", "name");

            Table expected = new Table();

            expected.addColumn("age", Integer.class);
            expected.addColumn("name", String.class);

            expected.insert(24, "alice");
            expected.insert(19, "bob");
            expected.insert(24, "john");
            expected.insert(32, "steve");

            assertEquals(expected,actual);
        }

    }

    @Nested
    class selectDistinctTest {

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

            Table actual = users.selectDistinct("age");

            Table expected = new Table();

            expected.addColumn("age", Integer.class);

            expected.insert(24);
            expected.insert(19);
            expected.insert(32);

            assertEquals(expected, actual);
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

            Table actual = users.selectDistinct("name", "age");

            Table expected = new Table();

            expected.addColumn("name", String.class);
            expected.addColumn("age", Integer.class);

            expected.insert("alice", 24);
            expected.insert("bob", 19);
            expected.insert("john", 24);
            expected.insert("steve", 32);

            assertEquals(expected, actual);
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

            Table actual = users.selectDistinct("age", "name");

            Table expected = new Table();

            expected.addColumn("age", Integer.class);
            expected.addColumn("name", String.class);

            expected.insert(24, "alice");
            expected.insert(19, "bob");
            expected.insert(24, "john");
            expected.insert(32, "steve");

            assertEquals(expected,actual);
        }

    }

    @Nested
    class updateTest {

        @Test
        void updateTestNameChangeIfOlderThan20() {
            Table users = new Table("users");

            users.addColumn("id", Integer.class);
            users.addColumn("name", String.class);
            users.addColumn("age", Integer.class);

            users.insert(1, "alice", 24);
            users.insert(2, "bob", 19);
            users.insert(3, "john", 24);
            users.insert(4, "steve", 32);

            HashMap<String, Object> changes = new HashMap<>();
            changes.put("name", "legolas");

            users.update(
                    row -> (int) row.getValue("age") > 20,
                    changes
            );

            Table expected = new Table();

            expected.addColumn("id", Integer.class);
            expected.addColumn("name", String.class);
            expected.addColumn("age", Integer.class);

            expected.insert(1, "legolas", 24);
            expected.insert(2, "bob", 19);
            expected.insert(3, "legolas", 24);
            expected.insert(4, "legolas", 32);

            assertEquals(expected, users);
        }

        @Test
        void updateAllNameChangeTest() {
            Table users = new Table("users");

            users.addColumn("id", Integer.class);
            users.addColumn("name", String.class);
            users.addColumn("age", Integer.class);

            users.insert(1, "alice", 24);
            users.insert(2, "bob", 19);
            users.insert(3, "john", 24);
            users.insert(4, "steve", 32);

            HashMap<String, Object> changes = new HashMap<>();
            changes.put("name", "elrond");

            users.updateAll(changes);

            Table expected = new Table();

            expected.addColumn("id", Integer.class);
            expected.addColumn("name", String.class);
            expected.addColumn("age", Integer.class);

            expected.insert(1, "elrond", 24);
            expected.insert(2, "elrond", 19);
            expected.insert(3, "elrond", 24);
            expected.insert(4, "elrond", 32);

            assertEquals(expected, users);
        }

    }

    @Nested
    class deleteTest {

        @Test
        void deleteAllTest() {
            Table users = new Table("users");

            users.addColumn("id", Integer.class);
            users.addColumn("name", String.class);
            users.addColumn("age", Integer.class);

            users.insert(1, "alice", 24);
            users.insert(2, "bob", 19);
            users.insert(3, "john", 24);
            users.insert(4, "steve", 32);

            users.deleteAll();

            ArrayList<Row> expected = new ArrayList<>();

            // selectAll() returns empty ArrayList;
            assertEquals(expected, users.selectAll());
        }

        @Test
        void deleteWhereOlderThan30() {
            Table users = new Table("users");

            users.addColumn("id", Integer.class);
            users.addColumn("name", String.class);
            users.addColumn("age", Integer.class);

            users.insert(1, "alice", 24);
            users.insert(2, "bob", 19);
            users.insert(3, "john", 24);
            users.insert(4, "steve", 32);

            users.deleteWhere(row -> (int) row.getValue("age") > 30);

            Table expected = new Table();

            expected.addColumn("id", Integer.class);
            expected.addColumn("name", String.class);
            expected.addColumn("age", Integer.class);

            expected.insert(1, "alice", 24);
            expected.insert(2, "bob", 19);
            expected.insert(3, "john", 24);

            assertEquals(expected, users);
        }

        @Test
        void deleteWhereNameStartsWithA() {
            Table users = new Table("users");

            users.addColumn("id", Integer.class);
            users.addColumn("name", String.class);
            users.addColumn("age", Integer.class);

            users.insert(1, "alice", 24);
            users.insert(2, "bob", 19);
            users.insert(3, "john", 24);
            users.insert(4, "steve", 32);

            users.deleteWhere(
                    row -> ((String) row.getValue("name")).startsWith("a")
            );

            Table expected = new Table();

            expected.addColumn("id", Integer.class);
            expected.addColumn("name", String.class);
            expected.addColumn("age", Integer.class);

            expected.insert(2, "bob", 19);
            expected.insert(3, "john", 24);
            expected.insert(4, "steve", 32);

            assertEquals(expected, users);
        }

    }

    @Nested
    class helpersTest {

        @Test
        void getIndicesOfRowsWhereTestOlderThan30() {
            Table users = new Table("users");

            users.addColumn("id", Integer.class);
            users.addColumn("name", String.class);
            users.addColumn("age", Integer.class);

            users.insert(1, "alice", 24);
            users.insert(2, "bob", 19);
            users.insert(3, "john", 24);
            users.insert(4, "steve", 32);

            // Age is greater than 30
            ArrayList<Integer> actual = users.getIndicesOfRowsWhere(row -> (int) row.getValue("age") > 30);

            ArrayList<Integer> expected = new ArrayList<>();
            expected.add(3);

            assertEquals(actual, expected);
        }

        @Test
        void getIndicesOfRowsWhereTestNameStartsWithA() {
            Table users = new Table("users");

            users.addColumn("id", Integer.class);
            users.addColumn("name", String.class);
            users.addColumn("age", Integer.class);

            users.insert(1, "alice", 24);
            users.insert(2, "bob", 19);
            users.insert(3, "john", 24);
            users.insert(4, "steve", 32);

            // Name starts with a
            ArrayList<Integer> actual = users.getIndicesOfRowsWhere(
                    row -> ((String) row.getValue("name")).startsWith("a")
            );

            ArrayList<Integer> expected = new ArrayList<>();
            expected.add(0);

            assertEquals(actual, expected);
        }

        @Test
        void getIndicesOfRowsWhereTallerThan60InchesThrowsIllegalArgumentException() {
            Table users = new Table("users");

            users.addColumn("id", Integer.class);
            users.addColumn("name", String.class);
            users.addColumn("age", Integer.class);

            users.insert(1, "alice", 24);
            users.insert(2, "bob", 19);
            users.insert(3, "john", 24);
            users.insert(4, "steve", 32);

            assertThrows(IllegalArgumentException.class, () -> {
                users.getIndicesOfRowsWhere(
                        row -> (int) row.getValue("height") > 60
                );
            });


        }

    }

    @Test
    void equalsTestTrue() {
        Table t1 = new Table();

        t1.addColumn("name", String.class);
        t1.addColumn("age", Integer.class);

        t1.insert("alice", 19);
        t1.insert("bob", 32);

        Table t2 = new Table();

        t2.addColumn("name", String.class);
        t2.addColumn("age", Integer.class);

        t2.insert("alice", 19);
        t2.insert("bob", 32);

        assertEquals(t1, t2);
    }

    @Test
    void equalsTestFalse() {
        Table t1 = new Table();

        t1.addColumn("name", String.class);
        t1.addColumn("age", Integer.class);

        t1.insert("alice", 19);
        t1.insert("bob", 32);

        Table t2 = new Table();

        t2.addColumn("name", String.class);
        t2.addColumn("age", Integer.class);

        t2.insert("alice", 19);
        t2.insert("bob", 21); // mismatch

        assertNotEquals(t1, t2);
    }

}