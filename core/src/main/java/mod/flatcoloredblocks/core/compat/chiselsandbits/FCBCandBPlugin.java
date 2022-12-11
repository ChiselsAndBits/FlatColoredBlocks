package mod.flatcoloredblocks.core.compat.chiselsandbits;

import com.communi.suggestu.scena.core.dist.Dist;
import com.communi.suggestu.scena.core.dist.DistExecutor;
import mod.chiselsandbits.api.client.variant.state.IClientStateVariantManager;
import mod.chiselsandbits.api.plugin.ChiselsAndBitsPlugin;
import mod.chiselsandbits.api.plugin.IChiselsAndBitsPlugin;
import mod.chiselsandbits.api.variant.state.IStateVariantManager;
import mod.flatcoloredblocks.core.registrars.Blocks;

@ChiselsAndBitsPlugin
public class FCBCandBPlugin implements IChiselsAndBitsPlugin {

    @Override
    public String getId() {
        return "flat-colored-blocks";
    }

    @Override
    public void onConstruction() {
        IStateVariantManager.getInstance().registerProvider(Blocks.COLORED_CONCRETE::get, new ColoredStateVariantProvider(Blocks.COLORED_CONCRETE::get));
        IStateVariantManager.getInstance().registerProvider(Blocks.COLORED_GLASS::get, new ColoredStateVariantProvider(Blocks.COLORED_GLASS::get));
    }

    @Override
    public void onClientConstruction() {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            IClientStateVariantManager.getInstance().registerStateVariantProvider(Blocks.COLORED_CONCRETE::get, new ClientColoredStateVariantProvider());
            IClientStateVariantManager.getInstance().registerStateVariantProvider(Blocks.COLORED_GLASS::get, new ClientColoredStateVariantProvider());
        });
    }
}
