
package ohtu.dao;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import ohtu.database.Database;
import ohtu.domain.Blogpost;
import ohtu.domain.Book;
import ohtu.domain.Bookmark;
import ohtu.domain.Video;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.junit.Assert.*;


public class BookmarkDaoTest {

    private static TemporaryFolder tempFolder;

    private static Database database;
    private static File databaseFile;
    private static BlogpostDao blogpostDao;
    private static BookmarkDao bookmarkDao;
    private static VideoDao videoDao;
    private static BookDao bookDao;
    private static Blogpost b1;
    private static Video v1, v2;
    private static Book k1;

    @BeforeClass
    public static void setUpClass() throws SQLException, ParseException, IOException {

        tempFolder = new TemporaryFolder();
        tempFolder.create();

        // Assign a test database into the newly created temporary folder.
        databaseFile = new File(tempFolder.getRoot() + "/test.db");

        if (databaseFile.exists()) {
            databaseFile.delete();
        }
        BookmarkDaoTest.database = new Database(databaseFile);
        BookmarkDaoTest.blogpostDao = new BlogpostDao(database);
        BookmarkDaoTest.videoDao = new VideoDao(database);
        BookmarkDaoTest.bookDao = new BookDao(database);
        BookmarkDaoTest.bookmarkDao = new BookmarkDao(database, blogpostDao, videoDao, bookDao);
        BookmarkDaoTest.b1 = new Blogpost(-1, "Data Mining", null, "navamani saravanan", "http://notescompsci.blogspot.com/2013/04/data-mining.html");
        BookmarkDaoTest.v1 = new Video(-1, "Map of Computer Science", null, "https://www.youtube.com/watch?v=SzJ46YA_RaA");
        BookmarkDaoTest.v2 = new Video(-1, "Big Data Analytics", null, "https://www.youtube.com/watch?v=LtScY2guZpo");
        BookmarkDaoTest.k1=new Book(-1,"Introdiction to Algorithms",null,"Thomas H. Cormen","9-780-262-0338-48");
        blogpostDao.create(b1);
        videoDao.create(v1);
        videoDao.create(v2);
        bookDao.create(k1);

    }

    @AfterClass
    public static void tearDownClass() {
        databaseFile.delete();
    }

    @Before
    public void setUp() throws SQLException, ParseException {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void canListAllBookmarks() throws SQLException, ParseException {
        List<Bookmark> bookmarks = bookmarkDao.findAll();
        assertEquals(4, bookmarks.size());
    }

    @Test
    public void canFindBookmarkByExistedId() throws SQLException, ParseException {
        Bookmark found = bookmarkDao.findById(1);
        assertTrue(found.isBlogpost());
        assertEquals(1, found.getId());
    }

    @Test
    public void canFindBookmarkByNonexistedId() throws SQLException, ParseException {
        Bookmark found = bookmarkDao.findById(100);
        assertNull(found);
    }

    @Test
    public void canFindBookmarkWithSamilarTitle() throws SQLException, ParseException {
        String keyword = "data";
        List<Bookmark> bookmarks = bookmarkDao.findByTitle(keyword);
        assertEquals(2, bookmarks.size());
    }

    @Test
    public void canFindAllBookmarksOrderByTitle() throws SQLException, ParseException {
        List<Bookmark> allBookmarks = bookmarkDao.findAllOrderByTitle();
        assertEquals(v2.getTitle(), allBookmarks.get(0).getTitle());
        assertEquals(b1.getTitle(), allBookmarks.get(1).getTitle());
        assertEquals(k1.getTitle(), allBookmarks.get(2).getTitle());
        assertEquals(v1.getTitle(), allBookmarks.get(3).getTitle());
    }
}
