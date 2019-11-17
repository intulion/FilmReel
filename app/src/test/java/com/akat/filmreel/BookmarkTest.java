package com.akat.filmreel;

import com.akat.filmreel.data.model.Bookmark;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Bookmark.class)
public class BookmarkTest {

    private Date constDate;

    @Before
    public void setUp() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2019, 11, 17, 0, 0, 0);
        constDate = calendar.getTime();

        PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(constDate);
    }

    @Test
    public void testDefaultValues() {
        long movieId = 550;
        Bookmark bookmark = new Bookmark(movieId);

        assertEquals(movieId, bookmark.getMovieId());
        assertTrue(bookmark.getBookmark());
        assertEquals(constDate, bookmark.getBookmarkDate());
    }
}