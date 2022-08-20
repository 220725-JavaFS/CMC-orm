package com.revature;

import java.util.Objects;

public class Movie {

	private int movie_id;
	private String title;
	private String image_url;
	private String description;
	private String genre;

	public Movie() {
		super();
	}

	public Movie(int movie_id, String title, String image_url, String description, String genre) {
		super();
		this.movie_id = movie_id;
		this.title = title;
		this.image_url = image_url;
		this.description = description;
		this.genre = genre;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, genre, image_url, movie_id, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Movie other = (Movie) obj;
		return Objects.equals(description, other.description) && Objects.equals(genre, other.genre)
				&& Objects.equals(image_url, other.image_url) && movie_id == other.movie_id
				&& Objects.equals(title, other.title);
	}

	@Override
	public String toString() {
		return "Movie [ID=" + movie_id + ", title=" + title + ", image_url=" + image_url + ", description="
				+ description + ", genre=" + genre + "]";
	}

	public int getMovie_id() {
		return movie_id;
	}

	public void setMovie_id(int movie_id) {
		this.movie_id = movie_id;
	}

}
