package com.akat.filmreel.data.model;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Movie {

    private long id;
    private Double popularity;
    private Integer voteCount;
    private Boolean video;
    private String posterPath;
    private Boolean adult;
    private String backdropPath;
    private String originalLanguage;
    private String originalTitle;
    private String title;
    private Double voteAverage;
    private String overview;
    private Date releaseDate;
    private List<Integer> genreIds = null;
    private Boolean bookmark;
    private Date bookmarkDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public Boolean getBookmark() {
        return bookmark;
    }

    public boolean isBookmarked() {
        if (bookmark == null) return false;
        return bookmark;
    }

    public void setBookmark(Boolean bookmark) {
        this.bookmark = bookmark;
    }

    public Date getBookmarkDate() {
        return bookmarkDate;
    }

    public void setBookmarkDate(Date bookmarkDate) {
        this.bookmarkDate = bookmarkDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return id == movie.id &&
                title.equals(movie.title) &&
                Objects.equals(popularity, movie.popularity) &&
                Objects.equals(voteCount, movie.voteCount) &&
                Objects.equals(video, movie.video) &&
                Objects.equals(posterPath, movie.posterPath) &&
                Objects.equals(adult, movie.adult) &&
                Objects.equals(backdropPath, movie.backdropPath) &&
                Objects.equals(originalLanguage, movie.originalLanguage) &&
                Objects.equals(originalTitle, movie.originalTitle) &&
                Objects.equals(voteAverage, movie.voteAverage) &&
                Objects.equals(overview, movie.overview) &&
                Objects.equals(releaseDate, movie.releaseDate) &&
                Objects.equals(genreIds, movie.genreIds) &&
                Objects.equals(bookmark, movie.bookmark) &&
                Objects.equals(bookmarkDate, movie.bookmarkDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, popularity, voteCount, video, posterPath, adult, backdropPath, originalLanguage, originalTitle, title, voteAverage, overview, releaseDate, genreIds, bookmark, bookmarkDate);
    }
}
