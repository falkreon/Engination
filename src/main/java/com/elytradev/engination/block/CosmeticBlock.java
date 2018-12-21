/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 Isaac Ellingson (Falkreon)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.elytradev.engination.block;

import java.util.ArrayList;
import java.util.List;

import com.elytradev.engination.StringOps;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.client.item.TooltipOptions;
import net.minecraft.item.ItemStack;
import net.minecraft.text.ComponentTranslationException;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.TextComponent;
import net.minecraft.text.TextFormat;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Language;
import net.minecraft.world.BlockView;
import net.minecraft.world.loot.context.LootContext.Builder;

public class CosmeticBlock extends Block {
	protected String group = null;
	
	public CosmeticBlock(Material material, DyeColor color, String group) {
		this(material, color);
		this.group = group;
	}
	
	public CosmeticBlock(Material material, DyeColor color) {
		super(Settings.of(material, color)
				.strength(1.0f, 15.0f)
				);
	}
	
	public CosmeticBlock(Settings settings, String group) {
		super(settings);
		this.group = group;
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void addInformation(ItemStack var1, BlockView var2, List<TextComponent> var3, TooltipOptions var4) {
		if (group!=null) {
			String key = "blockgroup.engination."+group+".tip";
			List<String> localized = StringOps.wordWrap(StringOps.localize(key), 36);
			for(String s : localized) {
				var3.add(new StringTextComponent(s).applyFormat(TextFormat.GRAY, TextFormat.ITALIC));
			}
			//var3.add(new TranslatableTextComponent("blockgroup.engination."+group+".tip").applyFormat(TextFormat.ITALIC, TextFormat.GRAY));
		}
		super.addInformation(var1, var2, var3, var4);
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState var1, Builder var2) {
		ArrayList<ItemStack> result = new ArrayList<>();
		result.add(new ItemStack(this, 1));
		return result;
	}
	
}
