package com.zamron.net;

import java.nio.channels.ClosedChannelException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.channel.*;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelUpstreamHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;

import com.zamron.net.packet.Packet;
import com.zamron.world.World;
import com.zamron.world.entity.impl.player.Player;
import com.google.common.collect.ImmutableSet;

import io.netty.handler.timeout.ReadTimeoutException;

/**
 * An implementation of netty's {@link SimpleChannelUpstreamHandler} to handle
 * all of netty's incoming events.
 *
 * @author Gabriel Hannason
 */
public class ChannelHandler extends IdleStateAwareChannelUpstreamHandler {

    private static final ImmutableSet<String> IGNORED_ERRORS = ImmutableSet.of(
            "An existing connection was forcibly closed by the remote host",
            "An established connection was aborted by the software in your host machine",
            "Connection reset",
            "Connection reset by peer");

    /**
     * The logger for this class.
     */
    private static final Logger logger = Logger.getLogger(ChannelHandler.class.getName());

    private volatile Player player;

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        Channel channel = ctx.getChannel();
        if (channel.isOpen())
            channel.close();

        Throwable cause = e.getCause();
        if (cause instanceof ReadTimeoutException || cause instanceof ClosedChannelException) return;

        String message = cause.getMessage();
        if (IGNORED_ERRORS.contains(message)) return;

        logger.log(Level.WARNING, "Exception occurred for channel: " + channel + ", closing...", cause);
    }

    @Override
    public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent e) throws Exception {
        e.getChannel().close();
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        Object msg = e.getMessage();
        if (msg instanceof Player) {
            if (player == null)
                player = (Player) msg;
        } else if (msg.getClass() == Packet.class) {
            Packet packet = (Packet) msg;
            player.getSession().handleIncomingMessage(packet);
        }
    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        if (World.queueLogout(player)) {
            player.getLogoutTimer().reset();
        }
    }

}
