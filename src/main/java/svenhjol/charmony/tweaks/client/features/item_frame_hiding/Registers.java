package svenhjol.charmony.tweaks.client.features.item_frame_hiding;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.tweaks.common.features.item_frame_hiding.Networking;

public class Registers extends Setup<ItemFrameHiding> {
    public Registers(ItemFrameHiding feature) {
        super(feature);

        // Create a particle to show when applying and removing amethyst.
        // Register before Minecraft loads particle resources. Registering this
        // through Charmony's CLIENT_STARTED callback is too late in 1.21.10:
        // the Fabric sprite set has already been created and has no loaded
        // particle JSON at that point.
        ParticleFactoryRegistry.getInstance().register(
            feature.common.get().registers.particleType,
            Particle::new
        );

        // Payload receivers must be registered during client initialization, before play connection setup.
        ClientPlayNetworking.registerGlobalReceiver(Networking.S2CAddAmethyst.TYPE,
            feature().handlers::addToItemFrame);
        ClientPlayNetworking.registerGlobalReceiver(Networking.S2CRemoveAmethyst.TYPE,
            feature().handlers::removeFromItemFrame);
    }
}
