package mod.flatcoloredblocks.core.compat.chiselsandbits;

import com.communi.suggestu.scena.core.dist.Dist;
import com.communi.suggestu.scena.core.dist.DistExecutor;
import mod.chiselsandbits.api.client.variant.state.IClientStateVariantManager;
import mod.chiselsandbits.api.plugin.ChiselsAndBitsPlugin;
import mod.chiselsandbits.api.plugin.IChiselsAndBitsPlugin;
import mod.chiselsandbits.api.variant.state.IStateVariantManager;
import mod.flatcoloredblocks.core.registrars.Blocks;
import mod.flatcoloredblocks.core.util.Constants;
import net.minecraft.resources.ResourceLocation;

public class FCBCandBPlugin implements IChiselsAndBitsPlugin {

    public String getId() {
        return "flat-colored-blocks";
    }

    @Override
    public void onConstruction() {
        StateVariantProviders.onModConstruction();
    }

    @Override
    public void onClientConstruction() {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            IClientStateVariantManager.getInstance().registerStateVariantProvider(Blocks.COLORED_CONCRETE::get, new ClientColoredStateVariantProvider());
            IClientStateVariantManager.getInstance().registerStateVariantProvider(Blocks.COLORED_GLASS::get, new ClientColoredStateVariantProvider());
            IClientStateVariantManager.getInstance().registerStateVariantProvider(Blocks.COLORED_WOOL::get, new ClientColoredStateVariantProvider());
        });
    }
}
