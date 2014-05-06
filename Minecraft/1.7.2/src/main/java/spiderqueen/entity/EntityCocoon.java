/*******************************************************************************
 * EntityCocoon.java
 * Copyright (c) 2014 Radix-Shock Entertainment.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package spiderqueen.entity;

import io.netty.buffer.ByteBuf;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import spiderqueen.core.SpiderQueen;
import spiderqueen.enums.EnumCocoonType;

import com.radixshock.radixcore.logic.LogicHelper;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityCocoon extends EntityCreature implements IEntityAdditionalSpawnData
{
	private EnumCocoonType cocoonType;
	private boolean isEaten;
	private int currentDamage;
	private int timeSinceHit;
	private int rockDirection = 1;

	public EntityCocoon(World world)
	{
		super(world);
		this.setSize(1F, 1F);
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
	}

	public EntityCocoon(World world, EnumCocoonType cocoonType) 
	{
		super(world);
		this.cocoonType = cocoonType;
	}

	@Override
	protected void entityInit() 
	{
		super.entityInit();
	}

	@Override
	public boolean isAIEnabled()
	{
		return true;
	}

	@Override
	protected boolean isMovementCeased()
	{
		return true;
	}

	@Override
	public AxisAlignedBB getCollisionBox(Entity entity)
	{
		return entity.boundingBox;
	}

	@Override
	public AxisAlignedBB getBoundingBox()
	{
		return this.boundingBox;
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return true;
	}

	@Override
	public boolean canBePushed()
	{
		return true;
	}

	@Override
	public void onUpdate() 
	{
		super.onUpdate();

		if (timeSinceHit > 0)
		{
			timeSinceHit--;
		}

		if (currentDamage > 0)
		{
			final Random rand = new Random();

			rotationPitch += rand.nextFloat();
			rotationPitch -= rand.nextFloat();
			currentDamage--;
		}

		if (cocoonType == EnumCocoonType.ENDERMAN && !isEaten)
		{
			worldObj.spawnParticle("portal", posX + (rand.nextDouble() - 0.5D) * (double)width, posY + 1 + rand.nextDouble() * (double)0.25D, posZ + rand.nextDouble() - 0.5D * (double)width, (rand.nextDouble() - 0.5D) * 2.0D, -rand.nextDouble(), (rand.nextDouble() - 0.5D) * 2.0D);
		}
	}
	
	@Override
	public void onEntityUpdate() 
	{
		super.onEntityUpdate();
	}

	@Override
	public void onLivingUpdate() 
	{
		super.onLivingUpdate();
	}

	@Override
	public boolean attackEntityFrom(DamageSource damageSource, float damage)
	{
		final Entity entity = damageSource.getEntity();
		
		if (entity instanceof EntityPlayer)
		{
			timeSinceHit = 10;
			currentDamage += damage * 10;

			setBeenAttacked();

			if (currentDamage > 80)
			{
				if (isEaten())
				{
					worldObj.spawnParticle("largesmoke", posX - motionX * 2, posY - motionY * 2, posZ - motionZ * 2, motionX, motionY, motionZ);
				}

				if (!worldObj.isRemote && !isEaten())
				{
					dropItem(cocoonType.getCocoonItem(), 1);
				}

				setDead();
			}
		}
		
		return true;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) 
	{
		try
		{
			cocoonType = (EnumCocoonType)EnumCocoonType.class.getFields()[nbt.getInteger("cocoonType")].get(EnumCocoonType.class);
		}

		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}

		isEaten = nbt.getBoolean("isEaten");
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) 
	{
		nbt.setInteger("cocoonType", cocoonType.ordinal());
		nbt.setBoolean("isEaten", isEaten);
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) 
	{
		buffer.writeInt(cocoonType.ordinal());
		buffer.writeBoolean(isEaten);
	}

	@Override
	public void readSpawnData(ByteBuf buffer)
	{
		try
		{
			cocoonType = (EnumCocoonType)EnumCocoonType.class.getFields()[buffer.readInt()].get(EnumCocoonType.class);
		}

		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}

		isEaten = buffer.readBoolean();
	}

	@Override
	public boolean interact(EntityPlayer entityPlayer)
	{
		if(!isEaten) 
		{
			entityPlayer.heal(3);
			entityPlayer.getFoodStats().addStats(4, 0.4f);

			worldObj.spawnParticle("largesmoke", posX, posY + 2, posZ, motionX, motionY, motionZ);
			worldObj.spawnParticle("largesmoke", posX, posY + 2, posZ, motionX, motionY, motionZ);
			isEaten = true;

			if (!worldObj.isRemote)
			{
				final boolean doDropEgg = LogicHelper.getBooleanWithProbability(15);
				final int dropAmount = LogicHelper.getNumberInRange(1, 2);
				
				entityDropItem(new ItemStack(SpiderQueen.getInstance().itemWeb, LogicHelper.getNumberInRange(0, 5), 0), 0);
				
				if (doDropEgg)
				{
					entityDropItem(new ItemStack(SpiderQueen.getInstance().itemSpiderEgg, dropAmount, 0), 0);
				}
				
				try
				{
					worldObj.playSoundAtEntity(this, cocoonType.getDeathSound(), getSoundVolume(), getSoundPitch());
				}

				catch (Throwable e)
				{
					e.printStackTrace();
				}
			}
		}

		return true;
	}

	@Override
    protected boolean canDespawn()
    {
        return false;
    }
	
	public EnumCocoonType getCocoonType()
	{
		return cocoonType;
	}

	public boolean isEaten() 
	{
		return isEaten;
	}

	public void setEaten(boolean isEaten) 
	{
		this.isEaten = isEaten;
	}

	public int getTimeSinceHit()
	{
		return timeSinceHit;
	}

	public int getCurrentDamage()
	{
		return currentDamage;
	}
}
