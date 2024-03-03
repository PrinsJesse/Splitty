package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventTest {

    private Event e;
    private Event e2;
    @BeforeEach
    void setUp() {
        e = new Event("name"
                , "16 maart"
                , "owner"
                , "food");
        e2 = new Event("name"
                , "16 maart"
                , "owner"
                , "food");
    }

    @Test
    public void getNameTest(){
        assertEquals("name", e.getName());
    }

    @Test
    public void getDateTest(){
        assertEquals("16 maart", e.getDate());
    }

    @Test
    public void getOwnerTest(){
        assertEquals("owner", e.getOwner());
    }

    @Test
    public void getDescriptionTest(){
        assertEquals("food", e.getDescription());
    }

    @Test
    public void setDateTest(){
        Event eventT = new Event();
        String date = "20 Maart";
        eventT.setDate(date);
        String dateCheck = eventT.getDate();
        assertEquals(date, dateCheck);
    }

    @Test
    public void setOwnerTest(){
        Event eventT = new Event();
        String owner = "stijn";
        eventT.setOwner(owner);
        String ownerCheck = eventT.getOwner();
        assertEquals(owner, ownerCheck);
    }

    @Test
    public void setDescriptionTest(){
        Event eventT = new Event();
        String desc = "for cinema";
        eventT.setDescription(desc);
        String descCheck = eventT.getDescription();
        assertEquals(desc, descCheck);
    }

    @Test
    public void equalsTest(){
        assertEquals(e,e2);
    }

    @Test
    public void hashcodeTest(){
        assertEquals(e.hashCode(), e2.hashCode());
    }

    @Test
    public void toStringTest(){
        String s = "This is event with name:" +
                " name that is created on 16 maart" +
                " the person that created is: owner " +
                "the description is: food";
        assertEquals(e.toString(), s);
    }


}
