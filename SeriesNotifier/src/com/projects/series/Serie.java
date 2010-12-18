package com.projects.series;

public class Serie {
	private String name;
	private String id;
	private String desc;
	private String imgUrl;
	
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

}
