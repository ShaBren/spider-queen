/*******************************************************************************
 * ItemCocoon.java
 * Copyright (c) 2014 Radix-Shock Entertainment.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package spiderqueen.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import spiderqueen.core.SpiderQueen;
import spiderqueen.entity.EntityCocoon;
import spiderqueen.enums.EnumCocoonType;

import com.radixshock.radixcore.constant.Font.Color;

public class ItemCocoon extends Item
{
	private EnumCocoonType cocoonType;
	
	public ItemCocoon(EnumCocoonType cocoonType)
	{
		super();
		this.maxStackSize = 1;
		this.setCreativeTab(SpiderQueen.getInstance().tabSpiderQueen);
		this.cocoonType = cocoonType;
		this.cocoonType.setCocoonItem(this);
	}

	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int posX, int posY, int posZ, int meta, float xOffset, float yOffset, float zOffset)
	{
		if (!world.isRemote)
		{
			final EntityCocoon entityCocoon = new EntityCocoon(world, cocoonType);
			entityCocoon.setPositionAndRotation(posX, posY + 1, posZ, entityCocoon.rotationYaw - 90F, 0F);
			world.spawnEntityInWorld(entityCocoon);
		}
		
		return true;
	}

	@Override
	public void registerIcons(IIconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("spiderqueen:Cocoon" + cocoonType.toString());
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) 
	{	
		if (this == SpiderQueen.getInstance().itemCocoonChicken || this == SpiderQueen.getInstance().itemCocoonCow ||
				this == SpiderQueen.getInstance().itemCocoonHorse || this == SpiderQueen.getInstance().itemCocoonPig ||
				this == SpiderQueen.getInstance().itemCocoonSheep || this == SpiderQueen.getInstance().itemCocoonHuman)
		{
			par3List.add(Color.GRAY + "Produces a typical spider.");
		}
		
		else if (this == SpiderQueen.getInstance().itemCocoonEnderman)
		{
			par3List.add(Color.GRAY + "Produces an " + Color.PURPLE + "Ender Spider" + Color.GRAY + ".");
		}
		
		else if (this == SpiderQueen.getInstance().itemCocoonCreeper)
		{
			par3List.add(Color.GRAY + "Produces a " + Color.GREEN + "Boom Spider" + Color.GRAY + ".");
		}
		
		else if (this == SpiderQueen.getInstance().itemCocoonSkeleton)
		{
			par3List.add(Color.GRAY + "Produces a " + Color.WHITE + "Slinger Spider" + Color.GRAY + ".");
		}
		
		else if (this == SpiderQueen.getInstance().itemCocoonTestificate)
		{
			par3List.add(Color.GRAY + "Produces a " + Color.GOLD + "Pack Spider" + Color.GRAY + ".");
		}
		
		else if (this == SpiderQueen.getInstance().itemCocoonWolf)
		{
			par3List.add(Color.GRAY + "Produces a " + Color.WHITE + "Nova Spider" + Color.GRAY + ".");
		}
		
		else if (this == SpiderQueen.getInstance().itemCocoonZombie)
		{
			par3List.add(Color.GRAY + "Produces a " + Color.DARKGREEN + "Tank Spider" + Color.GRAY + ".");
		}
	}
}
