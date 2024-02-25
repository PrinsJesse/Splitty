import org.junit.jupiter.api.Test;
import server.Currency;
import server.Expense;
import server.Transaction;
import server.Type;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseTest {

    Transaction t1 = new Transaction("John", 100.50);
    Transaction t2 = new Transaction("Cyndia", -20.50);
    Transaction t22 = new Transaction("Cyndia", -20.50);
    Transaction t3 = new Transaction("Nikolay", -10.0);
    Transaction t4 = new Transaction("Robin", -40.0);
    Transaction t5 = new Transaction("Marshall", -5.0);
    Transaction t6 = new Transaction("Jakub", -25.0);
    List<Transaction> transactions = List.of(t1, t2, t3, t4, t5, t6);
    List<Transaction> transactionsMinusOne = List.of(t1, t2, t3, t4, t5);
    Expense expense = new Expense("This is a test expense", transactions, Type.Food,
            Currency.EUR, "24-02-2024", 100.50, "John");

    Expense expenseCopy = new Expense("This is a test expense", transactions, Type.Food,
            Currency.EUR, "24-02-2024", 100.50, "John");
    Expense expenseFalseCopy = new Expense("This is a test expense", transactionsMinusOne, Type.Food,
            Currency.EUR, "24-02-2024", 100.50, "John");

    @Test
    public void stringTest() {
        String s = "This is an expense:\n" +
                "This is a test expense\n" +
                "The expense type is: Food.\n" +
                "The total amount spent is: 100.5.\n" +
                "This is how much everyone owes:\n" +
                "\tJohn: 100.5.\n" +
                "\tCyndia: -20.5.\n" +
                "\tNikolay: -10.0.\n" +
                "\tRobin: -40.0.\n" +
                "\tMarshall: -5.0.\n" +
                "\tJakub: -25.0.\n" +
                "The person who paid was: John, on 24-02-2024 and paid in EUR.";
        assertEquals(s, expense.toString());
    }

    @Test
    public void equals1() {
        assertEquals(t2, t22);
    }

    @Test
    public void equals2() {
        assertEquals(expense, expenseCopy);
    }


    @Test
    public void notEquals1() {
        assertNotEquals(t1, t2);
    }

    @Test
    public void notEquals2() {
        assertNotEquals(expense, expenseFalseCopy);
    }

    @Test
    public void equalsNull1() {
        assertNotEquals(null, t5);
    }

    @Test
    public void equalsNull2() {
        assertNotEquals(null, expense);
    }

    @Test
    public void equalsOtherClass() {
        assertNotEquals(expense, t1);
    }
}
