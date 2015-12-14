package mod.flatcoloredblocks;

import mod.flatcoloredblocks.block.BlockFlatColored;
import mod.flatcoloredblocks.block.ItemBlockFlatColored;
import mod.flatcoloredblocks.client.ClientSide;
import mod.flatcoloredblocks.client.DummyClientSide;
import mod.flatcoloredblocks.client.IClientSide;
import mod.flatcoloredblocks.config.ModConfig;
import mod.flatcoloredblocks.craftingitem.ItemColoredBlockCrafter;
import mod.flatcoloredblocks.gui.GuiScreenStartup;
import mod.flatcoloredblocks.gui.ModGuiRouter;
import mod.flatcoloredblocks.integration.IntegerationJEI;
import mod.flatcoloredblocks.network.NetworkRouter;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod(
		name = FlatColoredBlocks.MODNAME,
		modid = FlatColoredBlocks.MODID,
		acceptedMinecraftVersions = "[1.8,)",
		version = FlatColoredBlocks.VERSION,
		dependencies = FlatColoredBlocks.DEPENDENCIES,
		guiFactory = "mod.flatcoloredblocks.gui.ConfigGuiFactory" )
public class FlatColoredBlocks
{

	public static final String MODNAME = "FlatColoredBlocks";
	public static final String MODID = "flatcoloredblocks";
	public static final String VERSION = "mc1.8.x-v1.1";

	public static final String DEPENDENCIES = "required-after:Forge@[11.14.4.1563,)";

	// create creative tab...
	public static FlatColoredBlocks instance;

	public CreativeTab creativeTab;
	public ModConfig config;

	public ItemColoredBlockCrafter itemColoredBlockCrafting;

	private final IntegerationJEI jei = new IntegerationJEI();
	private IClientSide clientSide;

	public FlatColoredBlocks()
	{
		instance = this;
	}

	@EventHandler
	public void preinit(
			final FMLPreInitializationEvent event )
	{
		config = new ModConfig( event.getSuggestedConfigurationFile() );

		BlockFlatColored.loadConfig( config );

		if ( FMLCommonHandler.instance().getSide().isClient() )
		{
			clientSide = new ClientSide();
		}
		else
		{
			clientSide = new DummyClientSide();
		}

		// inform Version Checker Mod of our existence.
		initVersionChecker();

		// configure creative tab.
		creativeTab = new CreativeTab();

		// connect to forge event bus ( using both for 1.8 compat. )
		MinecraftForge.EVENT_BUS.register( this );
		FMLCommonHandler.instance().bus().register( this );

		// create and configure crafting item.
		itemColoredBlockCrafting = new ItemColoredBlockCrafter();
		GameRegistry.registerItem( itemColoredBlockCrafting, "coloredcraftingitem" );
		clientSide.configureCraftingRender( itemColoredBlockCrafting );

		// crafting pattern.
		final ShapedOreRecipe craftingItemRecipe = new ShapedOreRecipe( itemColoredBlockCrafting, " R ", "VrG", " C ", 'R', "dyeRed", 'V', "dyePurple", 'r', "ingotIron", 'G', "dyeGreen",
				'C', "dyeCyan" );
		GameRegistry.addRecipe( craftingItemRecipe );

		// create and configure all blocks.
		for ( int x = 0; x < BlockFlatColored.getNumberOfBlocks(); x++ )
		{
			BlockFlatColored.offset = x * BlockFlatColored.META_SCALE;
			final BlockFlatColored cb = new BlockFlatColored();

			GameRegistry.registerBlock( cb, ItemBlockFlatColored.class, "flatcoloredblock" + x );

			// blacklist with JEI
			if ( !config.ShowBlocksInJEI )
			{
				jei.blackListBlock( cb );
			}

			clientSide.configureBlockRender( cb );
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
		// send blacklist to jei.
		jei.init();
	}

	@EventHandler
	public void postinit(
			final FMLPostInitializationEvent event )
	{
		// configure networking and gui.
		NetworkRouter.instance = new NetworkRouter();
		NetworkRegistry.INSTANCE.registerGuiHandler( this, new ModGuiRouter() );
	}

	@SubscribeEvent
	@SideOnly( Side.CLIENT )
	public void openMainMenu(
			final GuiOpenEvent event )
	{
		// if the max shades has changed in form the user of the new usage.
		if ( config.LAST_MAX_SHADES != BlockFlatColored.getNumberOfShades() )
		{
			if ( event.gui != null && event.gui.getClass() == GuiMainMenu.class )
			{
				event.gui = new GuiScreenStartup();
			}
		}
	}
}
