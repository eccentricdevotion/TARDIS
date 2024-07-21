package me.eccentric_nz.TARDIS.lazarus.disguise.npc;

import io.netty.channel.*;

import java.net.SocketAddress;

public class NPCChannel extends AbstractChannel {

    private final ChannelConfig config = new DefaultChannelConfig(this);

    public NPCChannel(Channel parent) {
        super(parent);
    }

    @Override
    public ChannelConfig config() {
        config.setAutoRead(true);
        return config;
    }

    @Override
    protected void doBeginRead() throws Exception {
    }

    @Override
    protected void doBind(SocketAddress arg0) throws Exception {
    }

    @Override
    protected void doClose() throws Exception {
    }

    @Override
    protected void doDisconnect() throws Exception {
    }

    @Override
    protected void doWrite(ChannelOutboundBuffer arg0) throws Exception {
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    protected boolean isCompatible(EventLoop arg0) {
        return false;
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    protected SocketAddress localAddress0() {
        return null;
    }

    @Override
    public ChannelMetadata metadata() {
        return new ChannelMetadata(true);
    }

    @Override
    protected AbstractUnsafe newUnsafe() {
        return null;
    }

    @Override
    protected SocketAddress remoteAddress0() {
        return null;
    }
}
