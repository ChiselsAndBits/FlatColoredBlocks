package mod.flatcoloredblocks;

import java.io.File;
import java.util.ArrayList;

import mod.flatcoloredblocks.block.BlockFlatColored;
import mod.flatcoloredblocks.block.BlockHSVConfiguration;
import mod.flatcoloredblocks.block.EnumFlatBlockType;
import mod.flatcoloredblocks.block.ItemBlockFlatColored;
import mod.flatcoloredblocks.client.IClientSide;
import mod.flatcoloredblocks.config.ModConfig;
import mod.flatcoloredblocks.craftingitem.ItemColoredBlockCrafter;
import mod.flatcoloredblocks.gui.GuiScreenStartup;
import mod.flatcoloredblocks.network.NetworkRouter;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod( FlatColoredBlocks.MODID )
public class FlatColoredBlocks
{
	// create creative tab...
	public static FlatColoredBlocks instance;

	public static final String MODID = "flatcoloredblocks";

	public CreativeTab creativeTab;
	public ModConfig config;

	public ItemColoredBlockCrafter itemColoredBlockCrafting;

	private IClientSide clientSide;

	public BlockHSVConfiguration normal;
	public BlockHSVConfiguration transparent;
	public BlockHSVConfiguration glowing;

	public FlatColoredBlocks()
	{
		instance = this;

		// configure creative tab.
		creativeTab = new CreativeTab();

		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		// configure networking and gui.
		NetworkRouter.instance = new NetworkRouter();

		config = new ModConfig( new File( FMLPaths.CONFIGDIR.get().toFile(), MODID ) );
		initHSVFromConfiguration( config );

		// TODO: GUIFACTORY
		DistExecutor.runWhenOn( Dist.CLIENT, () -> () ->
		{
			clientSide.preinit();
			clientSide.init();

			// ModLoadingContext.get().registerExtensionPoint(
			// ExtensionPoint.GUIFACTORY, new ModGuiRouter() );
		} );

		initRegistration();
		MinecraftForge.EVENT_BUS.register( this );
	}

	public int getFullNumberOfShades()
	{
		return normal.getNumberOfShades()
				+ transparent.getNumberOfShades() * FlatColoredBlocks.instance.config.TRANSPARENCY_SHADES
				+ glowing.getNumberOfShades() * FlatColoredBlocks.instance.config.GLOWING_SHADES;
	}

	public int getFullNumberOfBlocks()
	{
		return normal.getNumberOfBlocks()
				+ transparent.getNumberOfBlocks() * FlatColoredBlocks.instance.config.TRANSPARENCY_SHADES
				+ glowing.getNumberOfBlocks() * FlatColoredBlocks.instance.config.GLOWING_SHADES;
	}

	ArrayList<BlockFlatColored> blocks = new ArrayList<BlockFlatColored>();
	ArrayList<ItemBlockFlatColored> items = new ArrayList<ItemBlockFlatColored>();

	public void initHSVFromConfiguration(
			final ModConfig config )
	{
		normal = new BlockHSVConfiguration( EnumFlatBlockType.NORMAL, config );
		transparent = new BlockHSVConfiguration( EnumFlatBlockType.TRANSPARENT, config );
		glowing = new BlockHSVConfiguration( EnumFlatBlockType.GLOWING, config );
	}

	void initRegistration()
	{
		items.clear();
		blocks.clear();

		// create and configure crafting item.
		itemColoredBlockCrafting = new ItemColoredBlockCrafter();
		itemColoredBlockCrafting.setRegistryName( FlatColoredBlocks.MODID, "coloredcraftingitem" );

		final BlockHSVConfiguration configs[] = new BlockHSVConfiguration[] { normal, transparent, glowing };

		// create and configure all blocks.
		for ( final BlockHSVConfiguration hsvconfig : configs )
		{
			for ( int v = 0; v < hsvconfig.MAX_SHADE_VARIANT; ++v )
			{
				for ( int x = 0; x < hsvconfig.getNumberOfBlocks(); ++x )
				{
					final int offset = x * BlockHSVConfiguration.META_SCALE;
					final BlockFlatColored cb = BlockFlatColored.construct( hsvconfig, offset, v );
					final ItemBlockFlatColored cbi = new ItemBlockFlatColored( cb );

					final String regName = hsvconfig.getBlockName( v ) + x;

					// use the same name for item/block combo.
					cb.setRegistryName( MODID, regName );
					cbi.setRegistryName( MODID, regName );

					// register both.
					blocks.add( cb );
					items.add( cbi );

					// blacklist with JEI
					if ( !config.ShowBlocksInJEI )
					{
						// TODO: JEI BLACK LIST jei.blackListBlock( cbi );
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void registerBlocks(
			RegistryEvent.Register<Block> registry )
	{
		for ( BlockFlatColored block : blocks )
		{
			registry.getRegistry().register( block );
		}
	}

	@SubscribeEvent
	public void registerItems(
			RegistryEvent.Register<Item> registry )
	{
		registry.getRegistry().register( itemColoredBlockCrafting );

		for ( ItemBlockFlatColored item : items )
		{
			registry.getRegistry().register( item );
		}
	}

	@SubscribeEvent
	@OnlyIn( Dist.CLIENT )
	public void openMainMenu(
			final GuiOpenEvent event )
	{
		// if the max shades has changed in form the user of the new usage.
		if ( config.LAST_MAX_SHADES != FlatColoredBlocks.instance.getFullNumberOfShades()
				&& event.getGui() != null
				&& event.getGui().getClass() == GuiMainMenu.class )
		{
			event.setGui( new GuiScreenStartup() );
		}
	}
}
