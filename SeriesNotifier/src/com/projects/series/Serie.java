package com.projects.series;

import android.content.Context;

import com.projects.seriesnotifier.R;

public class Serie {
	private String name;
	private String id;
	private String desc;
	private String imgUrl;
	private String estado;
	private boolean fav;
	private String rate;
	private String votes;
	
	public Serie()
	{
		
	}
	
	public Serie(String name)
	{
		this.name = name;
	}
	
	public Serie(String name, String id)
	{
		this.name = name;
		this.id = id;
	}
	
	public Serie(String name, String id, String desc, String imgUrl)
	{
		this.name = name;
		this.id = id;
		this.desc = desc;
		this.imgUrl = imgUrl;
	}
	
	public Serie(String name, String id, String desc, String imgUrl, String estado)
	{
		this.name = name;
		this.id = id;
		this.desc = desc;
		this.imgUrl = imgUrl;
		this.estado = estado;
	}
	
	public Serie(String name, String id, String desc, String imgUrl, String estado, String rate, String votes)
	{
		this.name = name;
		this.id = id;
		this.desc = desc;
		this.imgUrl = imgUrl;
		this.estado = estado;
		this.rate = rate;
		this.votes = votes;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getDesc() {
		return this.desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public String getImgUrl() {
		return this.imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado, Context context) {
		if(estado.equals("Continuing")){
			this.estado = context.getString(R.string.Continuing);
		}else if(estado.equals("Ended")){
			this.estado = context.getString(R.string.Ended);
		}else{
			this.estado = estado;
		}
	}
	
	public boolean isFav()
	{
		return this.fav;
	}
	
	public void setFav(boolean fav)
	{
		this.fav = fav;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getRate() {
		return rate;
	}

	public void setVotes(String votes) {
		this.votes = votes;
	}

	public String getVotes() {
		return votes;
	}

}
