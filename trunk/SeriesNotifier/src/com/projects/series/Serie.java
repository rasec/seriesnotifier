package com.projects.series;

public class Serie {
	private String name;
	private String id;
	
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

	public String getName() {
		return name;
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

}
