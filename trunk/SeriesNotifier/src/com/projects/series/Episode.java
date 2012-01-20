package com.projects.series;

public class Episode {
	private String id;
	private String serieId;
	private String serieName;
	private String season;
	private String episode;
	private String date;
	
	public Episode()
	{
		
	}
	
	public Episode(String id, String serieId, String serieName, String season, String episode, String date)
	{
		this.id = id;
		this.serieId = serieId;
		this.serieName = serieName;
		this.season = season;
		this.episode = episode;
		this.date = date;
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

}
