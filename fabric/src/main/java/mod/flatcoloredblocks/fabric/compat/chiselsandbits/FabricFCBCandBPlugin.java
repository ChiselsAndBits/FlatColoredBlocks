package mod.flatcoloredblocks.fabric.compat.chiselsandbits;

import mod.chiselsandbits.api.plugin.ChiselsAndBitsPlugin;
import mod.chiselsandbits.api.plugin.IChiselsAndBitsPlugin;
import mod.flatcoloredblocks.core.compat.chiselsandbits.FCBCandBPlugin;

@ChiselsAndBitsPlugin
public class FabricFCBCandBPlugin implements IChiselsAndBitsPlugin {
    @Override
    public String getId() {
        return "flat-colored-blocks";
    }

    @Override
    public void onConstruction() {
        (new FCBCandBPlugin()).onConstruction();
    }

    @Override
    public void onClientConstruction() {
        (new FCBCandBPlugin()).onClientConstruction();
    }
}
