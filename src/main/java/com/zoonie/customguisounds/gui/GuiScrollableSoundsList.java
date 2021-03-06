package com.zoonie.customguisounds.gui;

import net.minecraft.client.renderer.Tessellator;

import com.zoonie.customguisounds.sound.Sound;

public class GuiScrollableSoundsList extends GuiScrollableList
{
	private GuiSetSound parent;

	public GuiScrollableSoundsList(GuiSetSound parent, int width, int height, int top, int bottom, int left, int entryHeight)
	{
		super(parent.mc, width, height, top, bottom, left, entryHeight);
		this.parent = parent;
	}

	@Override
	protected int getSize()
	{
		return GuiSetSound.sounds.size();
	}

	@Override
	protected void elementClicked(int var1, boolean var2)
	{
		parent.selectIndex(var1);
	}

	@Override
	protected boolean isSelected(int var1)
	{
		return parent.indexSelected(var1);
	}

	@Override
	protected void drawBackground()
	{
		parent.drawBackground();
	}

	@Override
	protected int getContentHeight()
	{
		return (getSize()) * super.slotHeight + 1;
	}

	@Override
	protected void drawSlot(int listIndex, int var2, int var3, int var4, Tessellator var5)
	{
		Sound sound = GuiSetSound.sounds.get(listIndex);
		if(sound != null)
		{
			parent.getFontRenderer().drawString(parent.getFontRenderer().trimStringToWidth(sound.getName(), listWidth - 10), this.left + 3, var3 + 2, 0x00BBFF);
		}
	}
}
