package svenhjol.charmony.tweaks.common.features.crafting_from_inventory;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import svenhjol.charmony.core.Charmony;

public final class Networking {
    private Networking() {}

    public record C2SOpenPortableCrafting() implements CustomPacketPayload {
        public static final Type<C2SOpenPortableCrafting> TYPE = new Type<>(Charmony.id("open_portable_crafting"));
        public static final StreamCodec<FriendlyByteBuf, C2SOpenPortableCrafting> CODEC =
            StreamCodec.of((buf, payload) -> {}, buf -> new C2SOpenPortableCrafting());

        public static void send() {
            ClientPlayNetworking.send(new C2SOpenPortableCrafting());
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
