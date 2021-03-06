package ohtu.main;

import java.sql.SQLException;
import ohtu.actions.*;
import ohtu.dao.BlogpostDao;
import ohtu.dao.BookDao;
import ohtu.dao.BookmarkDao;
import ohtu.dao.VideoDao;
import ohtu.database.Database;
import ohtu.enums.Commands;
import ohtu.io.IO;
import ohtu.enums.Color;
import ohtu.ui.UiController;
import ohtu.user.UserController;

import java.util.Arrays;
import ohtu.dao.ObjectDaoTemplate;
import ohtu.tools.DaoBuilder;
import ohtu.user.UserDbController;

public class App {

    private UiController uiController;

    private UserDbController userDbController;

    private final ExitAction exit;
    private final ListAction browse;
    private final DeleteAction delete;
    private final AddAction add;
    private final ModifyAction modify;
    private final HelpAction help;

    private boolean hasPrintedInitialInstructions = false;
    private IO io;
    private Database database;
    private final boolean requireLogin;
    
    private final BlogpostDao blogpostDao;
    private final VideoDao videoDao;
    private final BookDao bookDao;

    public App(IO io, Database db, boolean requireLogin) {
        this.io = io;
        database = db;
        this.requireLogin = requireLogin;

        this.blogpostDao = DaoBuilder.buildBlogpostDao(db);
        this.videoDao = DaoBuilder.buildVideoDao(db);
        this.bookDao = DaoBuilder.buildBookDao(db);

        BookmarkDao bookmarkDao = DaoBuilder.buildBookmarkDao(db, blogpostDao, videoDao, bookDao);

        uiController = new UiController(io); // Either ConsoleIO or StubIO.

        userDbController = new UserDbController(db);

        exit = new ExitAction(io, uiController);
        delete = new DeleteAction(io, uiController, userDbController, bookmarkDao, blogpostDao, videoDao, bookDao);
        browse = new ListAction(io, uiController, bookmarkDao);
        add = new AddAction(io, uiController, blogpostDao, videoDao, bookDao);
        modify = new ModifyAction(io, uiController, userDbController, bookmarkDao, blogpostDao, videoDao, bookDao);
        help = new HelpAction(io, uiController);
    }

    public void run() throws SQLException {

        boolean appRunning = true;

        uiController.printGreeting();
        uiController.printVersion(Main.APP_VERSION);
        uiController.printEmptyLine();
        uiController.printWhereToGetLatestVersion(Main.APP_URL);

        outer:
        while (appRunning) {

            // Only if this has been set, we'll require the user to log in.
            if (requireLogin) {

                inner:
                while (!UserController.isLoggedIn()) {
                    // Loop this until the user has logged in or exited the app.
                    // Will never enter this section again. I just wanted to include this in the main logic loop.

                    uiController.printEmptyLine();
                    uiController.printLoginInstructions();

                    char code = uiController.askForCharacter(new char[]{'L', 'R', 'E'}, "Your choice");

                    if (code == 'E') {
                        appRunning = false;
                        exit.act();
                        break outer;
                    }

                    UserController userController = new UserController(uiController, userDbController, io);

                    if (code == 'L') {
                        // Login functionality.
                        userController.login();
                    } else if (code == 'R') {
                        // Registration and immediate login.
                        userController.registerAndLogin();
                    }

                    // Once the user is logged in, you can use "UserController.getUserId()" to get their ID.
                    // Notice the uppercase 'U'. It's a static method.
                    int id = UserController.getUserId(); // Like this.
                }

            } else {
                // This will log in the user with ID of 0.
                // Use that user for tests.
                UserController.autoLoginDefaultUser();
            }

            ObjectDaoTemplate.setUser_id(UserController.getUserId());

            if (!hasPrintedInitialInstructions) {
                // We'll only print these once, at the beginning. User can manually print them again.
                uiController.printInstructions();
                hasPrintedInitialInstructions = true;
            }

            char character = uiController.askForCharacter(
                    new char[]{'A', 'L', 'E', 'D', 'M', 'X', 'S'}, "Choose a command ('" + Color.commandText('X') + "' lists them)"
            );

            Commands command = Arrays.stream(Commands.values()).filter(a -> a.getCommand() == character).findFirst().get();

            switch (command) {

                case SEARCH:
                    browse.search();
                    break;

                case LIST:
                    browse.act();
                    break;

                case ADD:
                    add.act();
                    break;

                case DELETE:
                    delete.act();
                    break;

                case MODIFY:
                    modify.act();
                    break;

                case HELP:
                    help.act();
                    break;

                case EXIT:
                    appRunning = false;
                    exit.act();
                    break;

                default:
                    throw new IllegalArgumentException("This method was called with an illegal argument.");
            }
        }

        // The main loop has exited, so the program will terminate.
    }
}
