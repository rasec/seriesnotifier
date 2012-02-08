package com.projects.series;

public class Episode {
	private String id;
	private String serieId;
	private String serieName;
	private String season;
	private String episode;
	private String date;
	private float rate;
	private boolean alreadyShowed = false;
	
	public Episode()
	{
		
	}
	
	public Episode(String id, String serieId, String serieName, String season, String episode, String date, float rate)
	{
		this.id = id;
		this.serieId = serieId;
		this.serieName = serieName;
		this.season = season;
		this.episode = episode;
		this.date = date;
		this.setRate(rate);
	}
	
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getSerieId() {
		return this.serieId;
	}

	public void setSerieId(String serieId) {
		this.serieId = serieId;
	}
	
	public String getSerieName() {
		return this.serieName;
	}

	public void setSerieName(String serieName) {
		this.serieName = serieName;
	}
	
	public String getSeason() {
		return this.season;
	}

	public void setSeason(String season) {
		this.season = season;
	}
	
	public String getEpisode() {
		return this.episode;
	}

	public void setEpisode(String episode) {
		this.episode = episode;
	}

	public String getDate() {
		return (this.date != null) ? this.date : "n.a." ;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public boolean isAlreadyShowed() {
		return this.alreadyShowed;
	}

	public void markAsShowed() {
		this.alreadyShowed = true;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public float getRate() {
		return rate;
	}

}
