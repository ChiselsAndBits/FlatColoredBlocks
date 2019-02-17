package mod.flatcoloredblocks.craftingitem;

import org.lwjgl.opengl.GL11;

import mod.flatcoloredblocks.FlatColoredBlocks;
import net.java.games.input.Mouse;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * GuiContainer for crafting item's gui manages render, scroll bar and defers
 * scroll position to Container.
 */
public class GuiColoredBlockCrafter extends GuiContainer
{

	private static final ResourceLocation CRAFTER_GUI_TEXTURE = new ResourceLocation( FlatColoredBlocks.MODID, "textures/gui/container/coloredcrafting.png" );

	private final ContainerColoredBlockCrafter myContainer;
	private boolean wasClicking = false;
	private boolean isScrolling = false;
	private float currentScroll = 0;

	public GuiColoredBlockCrafter(
			final EntityPlayer player,
			final World world,
			final int x,
			final int y,
			final int z )
	{
		super( new ContainerColoredBlockCrafter( player, world, x, y, z ) );

		myContainer = (ContainerColoredBlockCrafter) inventorySlots;
		allowUserInput = false;
		ySize = 239;
		xSize = 195;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(
			final int mouseX,
			final int mouseY )
	{
		fontRenderer.drawString( FlatColoredBlocks.instance.itemColoredBlockCrafting.getDisplayName( null ).getFormattedText(), 8, 6, 0x404040 );
		fontRenderer.drawString( I18n.format( "container.inventory", new Object[0] ), 8, ySize - 93, 0x404040 );
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(
			final float partialTicks,
			final int mouseX,
			final int mouseY )
	{
		GlStateManager.color4f( 1.0F, 1.0F, 1.0F, 1.0F );
		mc.getTextureManager().bindTexture( CRAFTER_GUI_TEXTURE );
		final int xOffset = ( width - xSize ) / 2;
		final int yOffset = ( height - ySize ) / 2;
		this.drawTexturedModalRect( xOffset, yOffset, 0, 0, xSize, ySize );

		final int scrollBarLeft = guiLeft + 175;
		final int scrollBarTop = guiTop + 18;
		final int scrollBarBottom = scrollBarTop + 112 + 14;

		final int scrollNobOffsetX = 232;
		final int scrollNobOffsetY = 0;

		mc.getTextureManager().bindTexture( CRAFTER_GUI_TEXTURE );

		GlStateManager.color4f( 1.0F, 1.0F, 1.0F, 1.0F );
		GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );

		final int rowsOfScrolling = Math.max( ( myContainer.getItemCount() + 8 ) / 9 - 7, 0 );
		if ( rowsOfScrolling <= 0 )
		{
			myContainer.setScroll( currentScroll = 0 );
		}

		drawTexturedModalRect( scrollBarLeft, scrollBarTop + (int) ( ( scrollBarBottom - scrollBarTop - 17 ) * currentScroll ), scrollNobOffsetX + ( rowsOfScrolling > 0 ? 0 : 12 ),
				scrollNobOffsetY, 12, 15 );
	}

	@Override
	public boolean mouseScrolled(
			double mouseWheelChange )
	{
		if ( mouseWheelChange != 0 )
		{
			final int rowsToScroll = myContainer.getItemCount() / 9 - 7;

			if ( mouseWheelChange > 0 )
			{
				mouseWheelChange = 1;
			}
			if ( mouseWheelChange < 0 )
			{
				mouseWheelChange = -1;
			}

			currentScroll = (float) ( currentScroll - (double) mouseWheelChange / (double) rowsToScroll );
			currentScroll = MathHelper.clamp_float( currentScroll, 0.0F, 1.0F );
			myContainer.setScroll( currentScroll );
		}
	}

	@Override
	public void drawScreen(
			final int mouseX,
			final int mouseY,
			final float partialTicks )
	{
		final boolean isMouseDown = Mouse.isButtonDown( 0 );

		final int scrollBarLeft = guiLeft + 175;
		final int scrollBarTop = guiTop + 18;
		final int scrollBarRight = scrollBarLeft + 14;
		final int scrollBarBottom = scrollBarTop + 112 + 14;

		if ( !wasClicking && isMouseDown && mouseX >= scrollBarLeft && mouseY >= scrollBarTop && mouseX < scrollBarRight && mouseY < scrollBarBottom )
		{
			isScrolling = true;
		}

		if ( !isMouseDown )
		{
			isScrolling = false;
		}

		wasClicking = isMouseDown;

		if ( isScrolling )
		{
			currentScroll = ( mouseY - scrollBarTop - 7.5F ) / ( scrollBarBottom - scrollBarTop - 15.0F );
			currentScroll = MathHelper.clamp_float( currentScroll, 0.0F, 1.0F );
			myContainer.setScroll( currentScroll );
		}

		this.drawDefaultBackground();
		super.drawScreen( mouseX, mouseY, partialTicks );
		this.func_191948_b( mouseX, mouseY );
	}

}
