package mod.flatcoloredblocks;

import java.util.ArrayList;

import mod.flatcoloredblocks.block.BlockFlatColored;
import mod.flatcoloredblocks.block.BlockHSVConfiguration;
import mod.flatcoloredblocks.block.EnumFlatBlockType;
import mod.flatcoloredblocks.block.ItemBlockFlatColored;
import mod.flatcoloredblocks.client.ClientSide;
import mod.flatcoloredblocks.client.DummyClientSide;
import mod.flatcoloredblocks.client.IClientSide;
import mod.flatcoloredblocks.commands.ExportFCBlockList;
import mod.flatcoloredblocks.config.ModConfig;
import mod.flatcoloredblocks.craftingitem.FlatColoredBlockRecipe;
import mod.flatcoloredblocks.craftingitem.ItemColoredBlockCrafter;
import mod.flatcoloredblocks.gui.GuiScreenStartup;
import mod.flatcoloredblocks.gui.ModGuiRouter;
import mod.flatcoloredblocks.network.NetworkRouter;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkRegistry;

@Mod(
		name = FlatColoredBlocks.MODNAME,
		modid = FlatColoredBlocks.MODID,
		acceptedMinecraftVersions = "[1.12]",
		version = FlatColoredBlocks.VERSION,
		dependencies = FlatColoredBlocks.DEPENDENCIES,
		guiFactory = "mod.flatcoloredblocks.gui.ConfigGuiFactory" )
public class FlatColoredBlocks
{
	// create creative tab...
	public static FlatColoredBlocks instance;

	public static final String MODNAME = "FlatColoredBlocks";
	public static final String MODID = "flatcoloredblocks";
	public static final String VERSION = "@VERSION@";

	public static final String DEPENDENCIES = "required-after:forge@[" // forge.
			+ net.minecraftforge.common.ForgeVersion.majorVersion + '.' // majorVersion
			+ net.minecraftforge.common.ForgeVersion.minorVersion + '.' // minorVersion
			+ net.minecraftforge.common.ForgeVersion.revisionVersion + '.' // revisionVersion
			+ net.minecraftforge.common.ForgeVersion.buildVersion + ",)"; // buildVersion";

	public CreativeTab creativeTab;
	public ModConfig config;

	public ItemColoredBlockCrafter itemColoredBlockCrafting;

	public final IntegerationJEI jei = new IntegerationJEI();
	private IClientSide clientSide;

	public BlockHSVConfiguration normal;
	public BlockHSVConfiguration transparent;
	public BlockHSVConfiguration glowing;

	public FlatColoredBlocks()
	{
		instance = this;
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

	@EventHandler
	public void preinit(
			final FMLPreInitializationEvent event )
	{
		config = new ModConfig( event.getSuggestedConfigurationFile() );
		initHSVFromConfiguration( config );

		if ( FMLCommonHandler.instance().getSide().isClient() )
		{
			clientSide = ClientSide.instance;
			ClientCommandHandler.instance.registerCommand( new ExportFCBlockList() );
		}
		else
		{
			clientSide = new DummyClientSide();
		}

		// inform Version Checker Mod of our existence.
		initVersionChecker();

		// configure creative tab.
		creativeTab = new CreativeTab();

		MinecraftForge.EVENT_BUS.register( this );
		initRegistration();

		clientSide.preinit();
	}

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
						jei.blackListBlock( cbi );
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
			clientSide.configureBlockRender( block, null );
		}
	}

	@SubscribeEvent
	public void registerItems(
			RegistryEvent.Register<Item> registry )
	{
		registry.getRegistry().register( itemColoredBlockCrafting );

		clientSide.configureCraftingRender( itemColoredBlockCrafting );

		for ( ItemBlockFlatColored item : items )
		{
			registry.getRegistry().register( item );
			clientSide.configureBlockRender( null, item );
		}
	}

	@SubscribeEvent
	public void registerRecipes(
			RegistryEvent.Register<IRecipe> registry )
	{
		if ( config.allowCraftingTable )
		{
			registry.getRegistry().register( new FlatColoredBlockRecipe() );
		}
	}

	private void initVersionChecker()
	{
		final NBTTagCompound compound = new NBTTagCompound();
		compound.setString( "curseProjectName", "flat-colored-blocks" );
		compound.setString( "curseFilenameParser", "flatcoloredblocks-[].jar" );
		FMLInterModComms.sendRuntimeMessage( MODID, "VersionChecker", "addCurseCheck", compound );
	}

	@EventHandler
	public void init(
			final FMLInitializationEvent event )
	{
	}

	@EventHandler
	public void postinit(
			final FMLPostInitializationEvent event )
	{
		clientSide.init();

		// configure networking and gui.
		NetworkRouter.instance = new NetworkRouter();
		NetworkRegistry.INSTANCE.registerGuiHandler( this, new ModGuiRouter() );
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
