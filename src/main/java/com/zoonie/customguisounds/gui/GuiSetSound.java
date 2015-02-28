package com.zoonie.customguisounds.gui;

import java.awt.Component;
import java.awt.HeadlessException;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.client.config.GuiSlider;

import com.zoonie.customguisounds.config.MappingsConfigManager;
import com.zoonie.customguisounds.sound.Sound;
import com.zoonie.customguisounds.sound.SoundFileHandler;
import com.zoonie.customguisounds.sound.SoundPlayer;

public class GuiSetSound extends GuiScreen
{
	protected static ArrayList<Sound> sounds;
	private JFileChooser fileChooser;
	private GuiScreen gui;
	private GuiScrollableSoundsList soundsList;
	private GuiButton playButton, saveButton;
	private GuiSlider volumeSlider;
	private GuiCheckBox loopCheckBox, resumeCheckBox, guiCloseCheckBox;
	private Sound selectedSound, initialSound, initialCloseSound;
	private int selected = -1;
	private String sourcename;
	public static boolean open;
	private int rightAlign;

	public GuiSetSound(GuiScreen gui)
	{
		fileChooser = new JFileChooser(Minecraft.getMinecraft().mcDataDir) {
			@Override
			protected JDialog createDialog(Component parent) throws HeadlessException
			{
				JDialog dialog = super.createDialog(parent);
				dialog.setLocationByPlatform(true);
				dialog.setAlwaysOnTop(true);
				return dialog;
			}
		};
		fileChooser.setFileFilter(new FileNameExtensionFilter(I18n.format("gui.files") + " (.ogg, .wav, .mp3)", "ogg", "wav", "mp3"));

		initialSound = MappingsConfigManager.mappings.get(gui.getClass().getSimpleName());
		initialCloseSound = MappingsConfigManager.mappings.get(gui.getClass().getSimpleName() + "Close");

		if(initialSound == null)
			initialSound = initialCloseSound;

		this.gui = gui;
		open = true;
	}

	@Override
	public void initGui()
	{
		super.initGui();

		int maxWidth = 397;

		int leftMin = 10;
		int leftAlign = Math.max((width / 2) - maxWidth, leftMin);
		int leftWidth = Math.min((width - 210) - leftAlign, maxWidth);

		int rightMax = width - leftAlign;
		rightAlign = leftAlign + leftWidth + 10;
		int rightWidth = Math.min(rightMax - rightAlign, maxWidth);

		soundsList = new GuiScrollableSoundsList(this, leftWidth, 0, 30, height - 25, leftAlign, 18);

		this.buttonList.add(saveButton = new GuiButton(0, rightAlign, height / 2 + 50, rightWidth / 2 - 1, 20, I18n.format("save")));
		saveButton.enabled = false;
		this.buttonList.add(new GuiButton(1, leftAlign, height - 25, leftWidth, 20, I18n.format("gui.add")));
		this.buttonList.add(playButton = new GuiButton(2, rightAlign, height / 2, rightWidth, 20, I18n.format("play")));
		playButton.enabled = false;
		this.buttonList.add(new GuiButton(3, rightAlign + rightWidth / 2 + 2, height / 2 + 50, rightWidth / 2 - 1, 20, I18n.format("cancel")));

		GuiButton label = new GuiButton(4, leftAlign, 10, leftWidth, 20, I18n.format("gui.sounds.list"));
		this.buttonList.add(label);
		label.enabled = false;

		this.buttonList.add(loopCheckBox = new GuiCheckBox(5, rightAlign, height / 2 - 35, " " + I18n.format("loop"), false));
		loopCheckBox.enabled = false;

		this.buttonList.add(resumeCheckBox = new GuiCheckBox(6, rightAlign, height / 2 - 20, " " + I18n.format("gui.resume"), false));
		resumeCheckBox.enabled = false;

		this.buttonList.add(guiCloseCheckBox = new GuiCheckBox(7, rightAlign, height / 2 - 90, " " + I18n.format("gui.sound.close"), false));

		sounds = SoundFileHandler.getSounds();

		this.buttonList.add(volumeSlider = new GuiSlider(8, rightAlign, height / 2 + 25, rightWidth, 20, I18n.format("volume") + ": ", "%", 0, 100, 100, false, true));
		volumeSlider.enabled = false;
		if(initialSound != null)
		{
			selectedSound = initialSound;

			volumeSlider.setValue(selectedSound.getVolume() * 100);
			volumeSlider.updateSlider();

			selected = sounds.indexOf(selectedSound);
			sourcename = selectedSound.getName();

			loopCheckBox.enabled = true;
			loopCheckBox.setIsChecked(selectedSound.looping());

			resumeCheckBox.enabled = false;
			resumeCheckBox.setIsChecked(selectedSound.resumeOnGuiOpen());

			guiCloseCheckBox.enabled = true;
			guiCloseCheckBox.setIsChecked(selectedSound.guiCloseSound());
		}
		if(initialCloseSound != null)
			SoundPlayer.getSoundPlayer().stopSound(initialCloseSound.getName());
	}

	@Override
	public void drawScreen(int p_571_1_, int p_571_2_, float p_571_3_)
	{
		soundsList.drawScreen(p_571_1_, p_571_2_, p_571_3_);
		super.drawScreen(p_571_1_, p_571_2_, p_571_3_);

		if(selectedSound != null)
		{
			selectIndex(selected);
			volumeSlider.enabled = true;
			SoundPlayer.getSoundPlayer().adjustVolume(sourcename, (float) volumeSlider.getValue() / 100);
		}
		else
			volumeSlider.enabled = false;

		if(SoundPlayer.getSoundPlayer().isPlaying(sourcename))
		{
			playButton.displayString = I18n.format("stop");
		}
		else
		{
			playButton.displayString = I18n.format("play");
		}

		this.drawString(fontRendererObj, I18n.format("gui") + ":", rightAlign, height / 2 - 70, 0xFFFFFF);
		this.drawString(fontRendererObj, gui.getClass().getSimpleName(), rightAlign + 40, height / 2 - 70, 0x00E600);

		this.drawString(fontRendererObj, I18n.format("sound") + ":", rightAlign, height / 2 - 50, 0xFFFFFF);
		if(selectedSound != null)
			this.drawString(fontRendererObj, selectedSound.getName(), rightAlign + 40, height / 2 - 50, 0x00BBFF);
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		if(button.enabled)
		{
			switch(button.id)
			{
			case 0:
				if(selectedSound != null)
				{
					if(sourcename != null && !sourcename.equals(selectedSound.getName()))
						SoundPlayer.getSoundPlayer().stopSound(sourcename);

					Sound sound = new Sound(selectedSound.getLocation());
					sound.setVolume((float) volumeSlider.getValue() / 100);
					sound.setLooping(loopCheckBox.isChecked());
					sound.setResumeOnGuiOpen(resumeCheckBox.isChecked());
					sound.setCloseSound(guiCloseCheckBox.isChecked());

					if(guiCloseCheckBox.isChecked())
					{
						SoundPlayer.getSoundPlayer().stopSound(sourcename);
						MappingsConfigManager.mappings.put(gui.getClass().getSimpleName() + "Close", sound);
					}
					else
						MappingsConfigManager.mappings.put(gui.getClass().getSimpleName(), sound);

					MappingsConfigManager.write();
				}
				open = false;
				mc.displayGuiScreen(gui);
				break;
			case 1:
				boolean fullscreen = Minecraft.getMinecraft().isFullScreen();
				if(fullscreen)
					Minecraft.getMinecraft().toggleFullscreen();
				int fcReturn = fileChooser.showOpenDialog(null);
				if(fullscreen)
					Minecraft.getMinecraft().toggleFullscreen();
				if(fcReturn == JFileChooser.APPROVE_OPTION)
				{
					if(fileChooser.getSelectedFile().exists())
					{
						Sound sound = new Sound(fileChooser.getSelectedFile());
						if(!SoundFileHandler.soundLoaded(sound))
						{
							selectedSound = SoundFileHandler.copyFileToSoundsFolder(sound.getLocation());
							selected = sounds.indexOf(selectedSound);
						}
					}
				}
				break;
			case 2:
				if(playButton.displayString.equals(I18n.format("play")))
				{
					BlockPos pos = mc.theWorld != null ? mc.thePlayer.getPosition() : new BlockPos(0, 0, 0);
					sourcename = SoundPlayer.getSoundPlayer().playNewSound(selectedSound.getLocation(), pos, loopCheckBox.isChecked(), mc.theWorld != null, 1F);
				}
				else
					SoundPlayer.getSoundPlayer().stopSound(sourcename);
				break;
			case 3:
				if(initialSound == null || !sourcename.equals(initialSound.getName()))
					SoundPlayer.getSoundPlayer().stopSound(sourcename);
				open = false;
				mc.displayGuiScreen(gui);
				break;
			case 5:
				SoundPlayer.getSoundPlayer().setLooping(sourcename, loopCheckBox.isChecked());
				break;
			case 7:
				if(guiCloseCheckBox.isChecked())
				{
					Sound sound = MappingsConfigManager.mappings.get(gui.getClass().getSimpleName() + "Close");
					if(sound != null)
						selectedSound = sound;
				}
				else
				{
					Sound sound = MappingsConfigManager.mappings.get(gui.getClass().getSimpleName());
					if(sound != null)
						selectedSound = sound;
				}
				selected = sounds.indexOf(selectedSound);
				break;
			}
		}
	}

	public void selectIndex(int selected)
	{
		if(selected >= 0 && selected < sounds.size())
		{
			this.selectedSound = sounds.get(selected);
			this.selected = selected;

			playButton.enabled = true;
			saveButton.enabled = true;

			if(!guiCloseCheckBox.isChecked())
			{
				loopCheckBox.enabled = true;
				resumeCheckBox.enabled = true;
			}
			else
			{
				loopCheckBox.setIsChecked(false);
				loopCheckBox.enabled = false;

				resumeCheckBox.setIsChecked(false);
				resumeCheckBox.enabled = false;
			}
		}
	}

	public boolean indexSelected(int var1)
	{
		return var1 == selected;
	}

	public FontRenderer getFontRenderer()
	{
		return fontRendererObj;
	}

	protected void drawBackground()
	{
		drawDefaultBackground();
	}

	@Override
	public void onGuiClosed()
	{
		if(open)
		{
			SoundPlayer.getSoundPlayer().stopSound(sourcename);
			open = false;
		}
	}
}
