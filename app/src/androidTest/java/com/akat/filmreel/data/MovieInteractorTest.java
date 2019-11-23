package com.akat.filmreel.data;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import com.akat.filmreel.data.local.AppDatabase;
import com.akat.filmreel.data.local.AppPreferences;
import com.akat.filmreel.data.local.LocalDataSource;
import com.akat.filmreel.data.local.MovieLocalDataSource;
import com.akat.filmreel.data.model.MovieWithBookmark;
import com.akat.filmreel.data.network.ApiManager;
import com.akat.filmreel.data.network.MovieNetworkDataSource;
import com.akat.filmreel.data.network.NetworkDataSource;
import com.akat.filmreel.util.AppExecutors;
import com.akat.filmreel.util.MockApiManager;
import com.akat.filmreel.util.SingleExecutors;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static com.akat.filmreel.util.LiveDataTestUtil.getValue;
import static com.akat.filmreel.util.TestUtils.clearSingleton;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MovieInteractorTest {
    private AppDatabase database;
    private MovieInteractor interactor;
    private MockWebServer mockWebServer = new MockWebServer();

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();

        // MockWebServer
        MockResponse response = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(getBody())
                .setBodyDelay(0, TimeUnit.MILLISECONDS);
        mockWebServer.enqueue(response);
        mockWebServer.start(8080);

        // Local data source
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        LocalDataSource localDataSource = MovieLocalDataSource.getInstance(database.topRatedDao(), database.bookmarksDao());

        // Preferences
        AppPreferences preferences = mock(AppPreferences.class);
        when(preferences.getLastPage()).thenReturn(0);
        when(preferences.getTotalPages()).thenReturn(1);

        // Network data source
        ApiManager manager = new MockApiManager(mockWebServer.url("/"));
        NetworkDataSource networkDataSource = MovieNetworkDataSource.getInstance(manager, preferences);

        // Repository
        AppExecutors executors = new SingleExecutors();
        MovieRepository repository = MovieRepository.getInstance(
                localDataSource,
                networkDataSource,
                executors);

        interactor = MovieInteractor.getInstance(repository, preferences);
    }

    private String getBody() {
        return "{\n" +
                "  \"page\": 1,\n" +
                "  \"total_results\": 6639,\n" +
                "  \"total_pages\": 332,\n" +
                "  \"results\": [\n" +
                "    {\n" +
                "        \"popularity\": 35.418,\n" +
                "        \"vote_count\": 14292,\n" +
                "        \"video\": false,\n" +
                "        \"poster_path\": \"/9O7gLzmreU0nGkIB6K3BsJbzvNv.jpg\",\n" +
                "        \"id\": 278,\n" +
                "        \"adult\": false,\n" +
                "        \"backdrop_path\": \"/j9XKiZrVeViAixVRzCta7h1VU9W.jpg\",\n" +
                "        \"original_language\": \"en\",\n" +
                "        \"original_title\": \"The Shawshank Redemption\",\n" +
                "        \"genre_ids\": [\n" +
                "            80,\n" +
                "            18\n" +
                "        ],\n" +
                "        \"title\": \"The Shawshank Redemption\",\n" +
                "        \"vote_average\": 8.7,\n" +
                "        \"overview\": \"Framed in the 1940s for the double murder of his wife and her lover, upstanding banker Andy Dufresne begins a new life at the Shawshank prison, where he puts his accounting skills to work for an amoral warden. During his long stretch in prison, Dufresne comes to be admired by the other inmates -- including an older prisoner named Red -- for his integrity and unquenchable sense of hope.\",\n" +
                "        \"release_date\": \"1994-09-23\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"popularity\": 28.154,\n" +
                "        \"vote_count\": 10939,\n" +
                "        \"video\": false,\n" +
                "        \"poster_path\": \"/rPdtLWNsZmAtoZl9PK7S2wE3qiS.jpg\",\n" +
                "        \"id\": 238,\n" +
                "        \"adult\": false,\n" +
                "        \"backdrop_path\": \"/6xKCYgH16UuwEGAyroLU6p8HLIn.jpg\",\n" +
                "        \"original_language\": \"en\",\n" +
                "        \"original_title\": \"The Godfather\",\n" +
                "        \"genre_ids\": [\n" +
                "            80,\n" +
                "            18\n" +
                "        ],\n" +
                "        \"title\": \"The Godfather\",\n" +
                "        \"vote_average\": 8.6,\n" +
                "        \"overview\": \"Spanning the years 1945 to 1955, a chronicle of the fictional Italian-American Corleone crime family. When organized crime family patriarch, Vito Corleone barely survives an attempt on his life, his youngest son, Michael steps in to take care of the would-be killers, launching a campaign of bloody revenge.\",\n" +
                "        \"release_date\": \"1972-03-14\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }

    @After
    public void tearDown() throws Exception {
        database.close();
        mockWebServer.shutdown();

        clearSingleton(MovieLocalDataSource.class);
        clearSingleton(MovieNetworkDataSource.class);
        clearSingleton(MovieRepository.class);
        clearSingleton(MovieInteractor.class);
    }

    @Test
    public void getMovies_fromNetwork() throws InterruptedException {
        List<MovieWithBookmark> movies = getValue(interactor.observeMovies());

        assertNotNull(movies);
        assertThat(movies.size(), equalTo(2));
    }
}