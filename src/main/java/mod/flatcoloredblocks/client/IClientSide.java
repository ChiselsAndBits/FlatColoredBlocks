package mod.flatcoloredblocks.client;

import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

public interface IClientSide
{

	void preinit();

	void init(
			FMLLoadCompleteEvent ev );

}
