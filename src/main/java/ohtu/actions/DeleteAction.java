package ohtu.actions;

import ohtu.dao.BlogpostDao;
import ohtu.dao.BookmarkDao;
import ohtu.domain.Blogpost;
import ohtu.domain.Bookmark;
import ohtu.io.IO;
import ohtu.main.Main;
import ohtu.ui.UiController;

import java.sql.SQLException;

public class DeleteAction extends Action {

	private UiController uiController;
	private BookmarkDao bookmarkDao;
	private BlogpostDao blogpostDao;

	public DeleteAction(IO io, UiController uiController, BookmarkDao bookmarkDao, BlogpostDao blogpostDao) {
		super(io);
		this.uiController = uiController;
		this.bookmarkDao = bookmarkDao;
		this.blogpostDao = blogpostDao;
	}

	@Override
	public void act() {

		uiController.printEmptyLine();

		try {
			if (bookmarkDao.findAll().isEmpty()) {
				uiController.printNoBookmarks();
				uiController.printEmptyLine();
				return;
			}

		} catch (Exception e) {
			Main.LOG.warning(e.getMessage());
			return;
		}

		int id = uiController.askForIdToRemove();
		Bookmark bookmark;
		try {
			bookmark = bookmarkDao.findById(id);
		} catch (Exception e) {
			Main.LOG.warning(e.getMessage());
			return;
		}

		if (bookmark == null) {
			uiController.printBookmarkNotFound();
			uiController.printEmptyLine();
			return;
		}

		uiController.printDeleteConfirmation(bookmark.getId(), bookmark.getTitle(), bookmark.getClass().getSimpleName());

		char c = uiController.askForCharacter(new char[]{'Y', 'N'}, "Really delete");

		if (c == 'Y') {
			if (bookmark instanceof Blogpost) {
				try {
					blogpostDao.delete(id);
					uiController.printDeleteSuccessful(id);
				} catch (SQLException e) {
					Main.LOG.warning(e.getMessage());
				}
			}
		} else {
			uiController.printAbortDelete();
		}

		uiController.printEmptyLine();

	}
}
