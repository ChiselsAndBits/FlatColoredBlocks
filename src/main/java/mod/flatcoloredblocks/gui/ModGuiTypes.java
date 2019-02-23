package mod.flatcoloredblocks.gui;

import java.lang.reflect.Constructor;

import mod.flatcoloredblocks.FlatColoredBlocks;
import mod.flatcoloredblocks.craftingitem.ContainerColoredBlockCrafter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * Registry of Guis
 */
@SuppressWarnings( "unused" )
public enum ModGuiTypes
{

	colored_crafter( ContainerColoredBlockCrafter.class );

	private final Class<? extends Container> container;
	private final Class<?> gui;

	public final Constructor<?> container_construtor;
	public final Constructor<?> gui_construtor;

	private ModGuiTypes(
			final Class<? extends Container> c )
	{
		try
		{
			container = c;
			container_construtor = container.getConstructor( EntityPlayer.class, World.class, int.class, int.class, int.class );
		}
		catch ( final Exception e )
		{
			throw new RuntimeException( e );
		}

		// by default...
		Class<?> g = null;
		Constructor<?> g_construtor = null;

		// attempt to get gui class/constructor...
		try
		{
			g = (Class<?>) container.getMethod( "getGuiClass" ).invoke( null );
			g_construtor = g.getConstructor( EntityPlayer.class, World.class, int.class, int.class, int.class );
		}
		catch ( final Exception e )
		{
			// if and only if we are on the client should this be considered an
			// error...
			if ( FMLEnvironment.dist == Dist.CLIENT )
			{
				throw new RuntimeException( e );
			}

		}

		gui = g;
		gui_construtor = g_construtor;

	}

	public ResourceLocation getID()
	{
		return new ResourceLocation( FlatColoredBlocks.MODID, toString() );
	}

	public IInteractionObject create(
			EntityPlayer player,
			World worldIn,
			int x,
			int y,
			int z )
	{
		final ModGuiTypes self = this;
		return new IInteractionObject() {

			@Override
			public boolean hasCustomName()
			{
				return false;
			}

			@Override
			public ITextComponent getName()
			{
				return null;
			}

			@Override
			public ITextComponent getCustomName()
			{
				return null;
			}

			@Override
			public String getGuiID()
			{
				return self.getID().toString();
			}

			@Override
			public Container createContainer(
					InventoryPlayer arg0,
					EntityPlayer player )
			{
				return ModGuiRouter.createContainer( ModGuiTypes.colored_crafter, player, worldIn, 0, 0, 0 );
			}

		};
	}
}
