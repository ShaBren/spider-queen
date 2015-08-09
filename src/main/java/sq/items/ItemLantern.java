package sq.items;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import sq.core.SpiderCore;
import sq.core.minecraft.ModBlocks;

public class ItemLantern extends Item
{
	public ItemLantern()
	{
		super();
		
		final String name = "lantern";
		setUnlocalizedName(name);
		setTextureName("sq:" + name);
		setCreativeTab(SpiderCore.getCreativeTab());
		setMaxStackSize(1);
		
		GameRegistry.registerItem(this, name);
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int posX, int posY, int posZ, int meta, float xOffset, float yOffset, float zOffset)
	{
		if (!world.isRemote && world.isAirBlock(posX, posY + 1, posZ))
		{
			if (!player.capabilities.isCreativeMode)
			{
				stack.stackSize--;
			}
			
			world.setBlock(posX, posY + 1, posZ, ModBlocks.lantern);
		}
		
		return super.onItemUse(stack, player, world, posX, posY, posZ, meta, xOffset, yOffset, zOffset);
	}
}
