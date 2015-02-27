package com.zoonie.customguimusic.sound;

import java.io.File;
import java.io.Serializable;

public class Sound implements Serializable, Comparable
{

	private File location;
	private float volume = 1F;
	private boolean looping = false;
	private boolean resumeOnGuiOpen = false;
	private boolean guiCloseSound = false;

	public Sound(File location)
	{
		this.location = location;
	}

	public File getLocation()
	{
		return location;
	}

	public String getName()
	{
		return location.getName();
	}

	public float getVolume()
	{
		return volume;
	}

	public void setVolume(float volume)
	{
		this.volume = volume;
	}

	public boolean looping()
	{
		return looping;
	}

	public void setLooping(boolean looping)
	{
		this.looping = looping;
	}

	public boolean resumeOnGuiOpen()
	{
		return resumeOnGuiOpen;
	}

	public void setResumeOnGuiOpen(boolean resumeOnGuiOpen)
	{
		this.resumeOnGuiOpen = resumeOnGuiOpen;
	}

	public boolean guiCloseSound()
	{
		return guiCloseSound;
	}

	public void setCloseSound(boolean closeSound)
	{
		this.guiCloseSound = closeSound;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((location.getName() == null) ? 0 : location.getName().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		Sound other = (Sound) obj;
		if(location.getName() == null)
		{
			if(other.location.getName() != null)
				return false;
		}
		else if(!location.getName().equals(other.location.getName()))
			return false;
		return true;
	}

	@Override
	public int compareTo(Object arg0)
	{
		Sound other = (Sound) arg0;
		return this.location.getName().compareToIgnoreCase(other.location.getName());
	}
}
